/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.abdera.AbderaResourceReaderStrategy;
import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.drools.ChangeSet;
import org.drools.grid.api.CompositeResourceDescriptor;
import org.drools.grid.api.ResourceDescriptor;
import org.drools.grid.api.impl.CompositeResourceDescriptorImpl;
import org.drools.io.Resource;
import org.drools.io.impl.BaseResource;
import org.drools.xml.ChangeSetSemanticModule;
import org.drools.xml.SemanticModules;
import org.drools.xml.XmlChangeSetReader;

/**
 *
 * @author esteban
 */
public class ScannerTask implements Runnable {

    private final List<AssetScannerListener> listeners = Collections.synchronizedList(new ArrayList<AssetScannerListener>());
    private final ScheduledExecutorService scheduler;
    
    private SemanticModules changeSetSemanticModules;
    
    private ResourceReaderStrategy resourceReaderStrategy;

    protected ScannerTask(){
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        this.changeSetSemanticModules = new SemanticModules();
        this.changeSetSemanticModules.addSemanticModule(new ChangeSetSemanticModule());
    }
    
    public ScannerTask(ResourceReaderStrategy resourceReaderStrategy) {
        this();
        this.resourceReaderStrategy = resourceReaderStrategy;
    }
    
    public ScannerTask(String guvnorURL, GuvnorAuthenticatorProvider authenticatorProvider) {
        this();
        
        //default reader streategy: Abdera
        this.resourceReaderStrategy = new AbderaResourceReaderStrategy(guvnorURL, authenticatorProvider);
    }
    
    public void run() {
        //get the resources
        try{
            final List<ResourceDescriptor> resources = Collections.unmodifiableList(this.getResourceDescriptors());
        
            //notify the listeners in a separate thread
            scheduler.schedule(new Runnable(){

                public void run() {
                    synchronized(listeners) {
                        for (AssetScannerListener assetScannerListener : listeners) {
                            assetScannerListener.onAssetsScanned(resources);
                        }
                    }
                }

            }, 0, TimeUnit.MILLISECONDS);
        } catch (RuntimeException ex){
            System.out.println("Error getting Resource Descriptors:");
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public void hydrateInternalResources(CompositeResourceDescriptor resourceDescriptor){
        try {
            //get the change-set content
            String changeSetContent = this.getAssetContent(resourceDescriptor.getResourceURL().toString());
            
            //parse the content
            XmlChangeSetReader reader = new XmlChangeSetReader( this.changeSetSemanticModules,
                                                            null,
                                                            0 );
            ChangeSet changeSet = reader.read(new ByteArrayInputStream(changeSetContent.getBytes()));

            List<ResourceDescriptor> childrenResources = new ArrayList<ResourceDescriptor>();
            
            //TODO: only added resources are supported?
            Collection<Resource> resourcesAdded = changeSet.getResourcesAdded();
            for (Resource resource : resourcesAdded) {
                String resourceURL = ((BaseResource)resource).getURL().toString();
                if (resourceURL.endsWith("/source") || resourceURL.endsWith("/source/")){
                    resourceURL = resourceURL.substring(0, resourceURL.lastIndexOf("/source"));
                }
                childrenResources.add(this.getResourceDescriptor(resourceURL));
            }
            
            ((CompositeResourceDescriptorImpl)resourceDescriptor).setInternalResources(childrenResources);
            
        } catch (Exception ex) {
            throw new IllegalStateException("Error hydrating the internal resources of "+resourceDescriptor, ex);
        }
    }
    
    
    public List<ResourceDescriptor> getResourceDescriptors(){
        return this.resourceReaderStrategy.getResourceDescriptors();
    }
    
    private ResourceDescriptor getResourceDescriptor(String url){
        return this.resourceReaderStrategy.getResourceDescriptor(url);
    }
    
    private String getAssetContent(String sourceURL) throws IOException{
        return this.resourceReaderStrategy.getAssetContent(sourceURL);
    }

    public void setResourceReaderStrategy(ResourceReaderStrategy resourceReaderStrategy) {
        this.resourceReaderStrategy = resourceReaderStrategy;
    }

    public void addListener(AssetScannerListener listener){
        this.listeners.add(listener);
    }
    
    public void removeListener(AssetScannerListener listener){
        this.listeners.remove(listener);
    }
    
    public void addListeners(List<AssetScannerListener> listeners){
        this.listeners.addAll(listeners);
    }
    
}

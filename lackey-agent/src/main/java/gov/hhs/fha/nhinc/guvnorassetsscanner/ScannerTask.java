/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import gov.hhs.fha.nhinc.guvnorassetsscanner.abdera.ResourceDescriptorTranslator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.io.IOUtils;
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

    private String requestURL;
    private GuvnorAuthenticatorProvider authenticatorProvider;
    private final List<AssetScannerListener> listeners = Collections.synchronizedList(new ArrayList<AssetScannerListener>());
    private final ScheduledExecutorService scheduler;
    
    private SemanticModules changeSetSemanticModules;

    public ScannerTask(String guvnorURL, GuvnorAuthenticatorProvider authenticatorProvider) {
        this.requestURL = guvnorURL;
        this.authenticatorProvider = authenticatorProvider;
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        this.changeSetSemanticModules = new SemanticModules();
        this.changeSetSemanticModules.addSemanticModule(new ChangeSetSemanticModule());
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
    
    
    private List<ResourceDescriptor> getResourceDescriptors(){
        RequestOptions options = this.createRequestOptions();
        
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(requestURL, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        List<ResourceDescriptor> results = new ArrayList<ResourceDescriptor>();
        
        Document<Feed> document = resp.getDocument();
        for (Entry entry : document.getRoot().getEntries()) {
            results.add(ResourceDescriptorTranslator.toResourceDescriptor(entry));
        }
        
        return results;
    }
    
    private ResourceDescriptor getResourceDescriptor(String url){
        RequestOptions options = this.createRequestOptions();
        
        //TODO: change this to a real encoder
        url = url.replaceAll("\\s", "%20");
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(url, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        Document<Entry> document = resp.getDocument();
        return ResourceDescriptorTranslator.toResourceDescriptor(document.getRoot());
    }
    
    private String getAssetContent(String sourceURL) throws IOException{
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        RequestOptions options = this.createRequestOptions();

        options.setAccept("application/octet-stream");
        
        ClientResponse resp = client.get(sourceURL, options);
        
        if (resp.getType() != Response.ResponseType.SUCCESS) {
            throw new RuntimeException(resp.getStatusText());
        }
        
        return IOUtils.toString(resp.getInputStream());
    }
    
    private RequestOptions createRequestOptions() {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        RequestOptions options = client.getDefaultRequestOptions();

        options.setAccept("application/atom+xml");

        //add authorization parameters
        if (this.authenticatorProvider != null){
            this.authenticatorProvider.addAuthenticationOptions(options);
        }

        return options;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.BasicAuthenticatorProvider;
import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import gov.hhs.fha.nhinc.guvnorassetsscanner.io.URLResourceReaderStrategy;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import junit.framework.Assert;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.commons.io.IOUtils;
import org.drools.grid.api.CompositeResourceDescriptor;
import org.drools.grid.api.ResourceDescriptor;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author esteban
 */
public class AssetScannerTest {
    
    @Ignore("make sure you have a running instance of Guvnor!")
    public void testScannerAgainstRunningGuvnor() throws Exception{
    
        String url = "http://localhost:8080/drools-guvnor/rest/packages/urn.gov.hhs.fha.nhinc.adapter.fact/assets/";
        GuvnorAuthenticatorProvider authenticatorProvider = new BasicAuthenticatorProvider("admin", "admin");
        AssetScannerListener listener = new AssetScannerListener() {

            public void onAssetsScanned(List<ResourceDescriptor> assets) {
                System.out.println("\n\n\n");
                System.out.println("Resource:");
                for (ResourceDescriptor resourceDescriptor : assets) {
                    System.out.println("\t"+resourceDescriptor.getName()+" ["+resourceDescriptor.getId()+"]");
                }
                System.out.println("\n\n\n");
            }
        };
        
        AssetsScanner scanner = new AssetsScanner(url, authenticatorProvider, 10);
        
        scanner.addListener(listener);
        
        scanner.start();
        
        Thread.sleep(60000);
        
        scanner.stop();
    }
    
    @Ignore("make sure you have a running instance of Guvnor!")
    public void testInternalResourceScannerAgainstRunningGuvnor() throws Exception{
    
        String url = "http://localhost:8080/drools-guvnor/rest/packages/urn.gov.hhs.fha.nhinc.adapter.fact/assets/";
        GuvnorAuthenticatorProvider authenticatorProvider = new BasicAuthenticatorProvider("admin", "admin");
        
        final AssetsScanner scanner = new AssetsScanner(url, authenticatorProvider, 10);
        
        AssetScannerListener listener = new AssetScannerListener() {

            public void onAssetsScanned(List<ResourceDescriptor> assets) {
                System.out.println("\n\n\n");
                try{
                    for (ResourceDescriptor resourceDescriptor : assets) {
                        if (resourceDescriptor instanceof CompositeResourceDescriptor){
                            scanner.hydrateInternalResources((CompositeResourceDescriptor)resourceDescriptor);
                            System.out.println("Composite Resource ["+resourceDescriptor.getName()+"]:");
                            for (ResourceDescriptor internalResourceDescriptor : ((CompositeResourceDescriptor)resourceDescriptor).getInternalResources()) {
                                System.out.println("\t"+internalResourceDescriptor.getName()+" ["+internalResourceDescriptor.getId()+"]");
                            }
                            System.out.println("\n\n");
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("\n\n\n");
            }
        };
        
        scanner.addListener(listener);
        
        scanner.start();
        
        Thread.sleep(60000);
        
        scanner.stop();
    }
    
    @Test
    public void testFileResource() throws IOException{
        URL fullAssetsTemplateURL = this.getClass().getResource("/fullAssets.xml");
        URL changeSetTemplateURL = this.getClass().getResource("/changeSet.xml");
        URL process1EntryURL = this.getClass().getResource("/process1Entry.xml");
        URL process2EntryURL = this.getClass().getResource("/process2Entry.xml");
        
        //create change-set file
        StringTemplate changeSetTemplate = new StringTemplate(IOUtils.toString(changeSetTemplateURL));
        changeSetTemplate.setAttribute("process1URL", process1EntryURL.toString());
        changeSetTemplate.setAttribute("process2URL", process2EntryURL.toString());
        File changeSetTempFile = File.createTempFile("changeSet", "xml");
        IOUtils.copy(new ByteArrayInputStream(changeSetTemplate.toString().getBytes()), new FileOutputStream(changeSetTempFile));
        
        //create full assets file
        StringTemplate assetsTemplate = new StringTemplate(IOUtils.toString(fullAssetsTemplateURL));
        assetsTemplate.setAttribute("changeSetURL", changeSetTempFile.toURI().toURL().toString());
        File fullAssetsTempFile = File.createTempFile("fullAssets", "xml");
        IOUtils.copy(new ByteArrayInputStream(assetsTemplate.toString().getBytes()), new FileOutputStream(fullAssetsTempFile));
        
        //create the scanner task
        ResourceReaderStrategy readerStrategy = new URLResourceReaderStrategy(fullAssetsTempFile.toURI().toURL());
        ScannerTask scannerTask = new ScannerTask(readerStrategy);
        
        //get all the resources( 1 change-set and 2 processes)
        List<ResourceDescriptor> resourceDescriptors = scannerTask.getResourceDescriptors();

        Assert.assertEquals(3, resourceDescriptors.size());
        
        //get the change-set
        CompositeResourceDescriptor changeSet = null;
        for (ResourceDescriptor resourceDescriptor : resourceDescriptors) {
            if (resourceDescriptor instanceof CompositeResourceDescriptor){
                changeSet = (CompositeResourceDescriptor) resourceDescriptor;
            }
        }
        Assert.assertNotNull(changeSet);
        
        System.out.println(changeSet.getResourceURL());
        
        //Hydrate the change-set
        scannerTask.hydrateInternalResources(changeSet);
        
        Assert.assertEquals(2, changeSet.getInternalResources().size());
        
        for (ResourceDescriptor resourceDescriptor : changeSet.getInternalResources()) {
            System.out.println("\t"+resourceDescriptor.getName());
        }
    }
}

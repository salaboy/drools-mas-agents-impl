/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.BasicAuthenticatorProvider;
import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.util.List;
import org.drools.grid.api.CompositeResourceDescriptor;
import org.drools.grid.api.ResourceDescriptor;
import org.junit.Ignore;

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
}

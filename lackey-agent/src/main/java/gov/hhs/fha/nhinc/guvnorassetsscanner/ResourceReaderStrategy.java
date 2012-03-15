/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner;

import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.io.IOException;
import java.util.List;
import org.drools.grid.api.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public interface ResourceReaderStrategy {
    
    public List<ResourceDescriptor> getResourceDescriptors();
    
    public ResourceDescriptor getResourceDescriptor(String url);
    
    public String getAssetContent(String sourceURL) throws IOException;
    
}

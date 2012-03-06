/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator;

import gov.hhs.fha.nhinc.guvnorassetsscanner.util.Base64;
import org.apache.abdera.protocol.client.RequestOptions;

/**
 *
 * @author esteban
 */
public class SAMLAuthenticatorProvider implements GuvnorAuthenticatorProvider {
    
    private String samlToken;
    
    private String samlTokenHeaderName = "SAMLResponse";
    private String samlTokenEncodedHeaderName = "SAMLResponseEncoded";
    
    /**
     * A valid, Base64 encoded SAMLToken
     * @param samlToken 
     */
    public SAMLAuthenticatorProvider(String samlToken) {
        this.samlToken = samlToken;
    }
    
    public void addAuthenticationOptions(RequestOptions options) {
        options.setHeader(samlTokenHeaderName, this.samlToken);
        options.setHeader(samlTokenEncodedHeaderName, "true");
    }

    public void setSamlTokenEncodedHeaderName(String samlTokenEncodedHeaderName) {
        this.samlTokenEncodedHeaderName = samlTokenEncodedHeaderName;
    }

    public void setSamlTokenHeaderName(String samlTokenHeaderName) {
        this.samlTokenHeaderName = samlTokenHeaderName;
    }
    
}

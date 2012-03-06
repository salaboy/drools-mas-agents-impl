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
public class BasicAuthenticatorProvider implements GuvnorAuthenticatorProvider {
    private String authenticationString;

    public BasicAuthenticatorProvider(String username, String password) {
        this.authenticationString = new Base64().encodeToString(((username+":"+password).getBytes()));
    }
    
    public void addAuthenticationOptions(RequestOptions options) {
        options.setAuthorization("Basic " + authenticationString);
    }
    
}

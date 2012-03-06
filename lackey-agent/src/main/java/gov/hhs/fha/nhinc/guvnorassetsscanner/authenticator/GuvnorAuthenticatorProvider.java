/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator;

import org.apache.abdera.protocol.client.RequestOptions;

/**
 *
 * @author esteban
 */
public interface GuvnorAuthenticatorProvider {
    
    public void addAuthenticationOptions(RequestOptions options);
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner.abdera;

import gov.hhs.fha.nhinc.guvnorassetsscanner.ResourceReaderStrategy;
import gov.hhs.fha.nhinc.guvnorassetsscanner.authenticator.GuvnorAuthenticatorProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.commons.io.IOUtils;
import org.drools.grid.api.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public class AbderaResourceReaderStrategy implements ResourceReaderStrategy {

    private GuvnorAuthenticatorProvider authenticatorProvider;
    private String requestURL;

    public AbderaResourceReaderStrategy(String requestURL, GuvnorAuthenticatorProvider authenticatorProvider) {
        this.requestURL = requestURL;
        this.authenticatorProvider = authenticatorProvider;
    }
    
    public List<ResourceDescriptor> getResourceDescriptors(){
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
    
    public ResourceDescriptor getResourceDescriptor(String url){
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
    
    public String getAssetContent(String sourceURL) throws IOException{
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
}

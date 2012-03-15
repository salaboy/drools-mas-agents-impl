/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner.io;

import gov.hhs.fha.nhinc.guvnorassetsscanner.ResourceReaderStrategy;
import gov.hhs.fha.nhinc.guvnorassetsscanner.abdera.ResourceDescriptorTranslator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.commons.io.IOUtils;
import org.drools.grid.api.ResourceDescriptor;

/**
 *
 * @author esteban
 */
public class URLResourceReaderStrategy implements ResourceReaderStrategy {

    private URL requestURL;

    public URLResourceReaderStrategy(URL requestURL) {
        this.requestURL = requestURL;
    }
    
    public List<ResourceDescriptor> getResourceDescriptors(){
        InputStream openStream = null;
        try {
            List<ResourceDescriptor> results = new ArrayList<ResourceDescriptor>();
            
            openStream = requestURL.openStream();
            Document<Feed> document = Abdera.getNewParser().parse(openStream);
            for (Entry entry : document.getRoot().getEntries()) {
                results.add(ResourceDescriptorTranslator.toResourceDescriptor(entry));
            }
            
            return results;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read from "+requestURL,ex);
        } finally{
            if (openStream != null){
                try {
                    openStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(URLResourceReaderStrategy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public ResourceDescriptor getResourceDescriptor(String url){
        InputStream openStream = null;
        try {
            URL resourceURL = new URL(url);
            openStream = resourceURL.openStream();
            Document<Entry> document = Abdera.getNewParser().parse(openStream);
            
            return ResourceDescriptorTranslator.toResourceDescriptor(document.getRoot());
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read from "+url,ex);
        } finally{
            if (openStream != null){
                try {
                    openStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(URLResourceReaderStrategy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public String getAssetContent(String sourceURL) throws IOException{
        return this.getStreamContent(new URL(sourceURL));
    }
    
    private String getStreamContent(URL url) throws IOException{
        return IOUtils.toString(url);
    }
 
}

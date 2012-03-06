/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.guvnorassetsscanner.abdera;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.abdera.model.AtomDate;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.drools.grid.api.ResourceDescriptor;
import org.drools.grid.api.impl.CompositeResourceDescriptorImpl;
import org.drools.grid.api.impl.ResourceDescriptorImpl;

/**
 *
 * @author esteban
 */
public class ResourceDescriptorTranslator {
    
    public static final String NS = "";
    public static final QName METADATA = new QName(NS, "metadata");
    public static final QName VALUE = new QName(NS, "value");
    public static final QName ARCHIVED = new QName(NS, "archived");
    public static final QName UUID = new QName(NS, "uuid");
    public static final QName STATE = new QName(NS, "state");
    public static final QName FORMAT = new QName(NS, "format");
    public static final QName CATEGORIES = new QName(NS, "categories");
    public static final QName CREATED = new QName(NS, "created");
    public static final QName LAST_MODIFIED = new QName(NS, "lastModified");
    public static final QName TITLE = new QName(NS, "title");
    public static final QName VERSION = new QName(NS, "version");
    
    public static final String CHANGE_SET_FORMAT = "changeset";
    
    public static ResourceDescriptor toResourceDescriptor(Entry entry){
        ExtensibleElement metadataExtension = entry.getExtension(METADATA);
        
        //Format
        String format = ((ExtensibleElement) metadataExtension.getExtension(FORMAT)).getSimpleExtension(VALUE);

        ResourceDescriptorImpl descriptor = null;
        if (format.equals(CHANGE_SET_FORMAT)){
            descriptor = new CompositeResourceDescriptorImpl();
        }else{
            descriptor = new ResourceDescriptorImpl();
        }
        
        descriptor.setType(format);
        
        //UUID
        String uuid = ((ExtensibleElement) metadataExtension.getExtension(UUID)).getSimpleExtension(VALUE);
        descriptor.setId(uuid);
        
        //Author
        String author = entry.getAuthor().getName();
        descriptor.setAuthor(author);
        
        //Creation Time
        String creationTime = ((ExtensibleElement) metadataExtension.getExtension(CREATED)).getSimpleExtension(VALUE);
        descriptor.setCreationTime(AtomDate.parse(creationTime));
        
        
        //Description
        String description = entry.getSummary();
        descriptor.setDescription(description);
        
        //No documentation
        descriptor.setDocumentation(null);
        
        //Last Modification Time
        Date lastModificationTime = entry.getPublished();
        descriptor.setLastModificationTime(lastModificationTime);
        
        
        //Name
        String name = entry.getTitle();
        descriptor.setName(name);

        //Resource URL
        try {
            descriptor.setResourceURL(new URL(URLDecoder.decode(entry.getContentSrc().toASCIIString(), "UTF-8")));
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid format for \"sourceLink\":"+entry.getContentSrc().toASCIIString());
        }
        
        //Status
        String status = ((ExtensibleElement) metadataExtension.getExtension(STATE)).getSimpleExtension(VALUE);
        descriptor.setStatus(status);
        
        //Version
        String version = ((ExtensibleElement) metadataExtension.getExtension(VERSION)).getSimpleExtension(VALUE);
        descriptor.setVersion(version);
        
        //Categories
        ExtensibleElement categoriesExtension = metadataExtension.getExtension(CATEGORIES);
        if (categoriesExtension != null){
            Set<String> categories = new HashSet<String>();
            List<Element> categoriesValues = categoriesExtension.getExtensions(VALUE);
            for (Element category : categoriesValues) {
                categories.add(category.getText());
            }
            descriptor.setCategories(categories);
        }
        
        return descriptor;
    }
}

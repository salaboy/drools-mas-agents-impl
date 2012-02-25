/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.drools.mas.action.helpers;

import gov.hhs.fha.nhinc.dsa.DSAIntegration;
import gov.hhs.fha.nhinc.dsa.DSAIntegrationPortType;
import gov.hhs.fha.nhinc.dsa.GetDirectoryAttributeRequestType;
import gov.hhs.fha.nhinc.dsa.GetDirectoryAttributeResponseType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author salaboy
 */
public class LDAPHelper {
    private static Logger logger = LoggerFactory.getLogger(LDAPHelper.class);
    
    public static Map<String, String> queryEntity(String endpoint, String id, List<String> names){
          
        Map<String, String> result = new HashMap<String, String>();
        GetDirectoryAttributeRequestType request = new GetDirectoryAttributeRequestType();
        request.setUid(id);
        if(names != null){
            request.getNames().addAll(names);
        }
        DSAIntegrationPortType port;
        try{
            port = getPort(endpoint);
        }catch (Exception e){
            logger.error(" ??? LDAPHelper: Query Entity Failed: "+e);
            logger.error(" ??? LDAPHelper: Returning Mock Data ... ");
            result.put("cn", "cn_"+id);
            result.put("mobile", "mobile_"+id);
            result.put("employeeNumber", "employeeNumber_"+id);
            result.put("displayName", "displayName_"+id);
            result.put("gender", "gender_"+id);
            return result;
            
        }
        GetDirectoryAttributeResponseType response = port.getDirectoryAttribute(request);
        
        
        for(int i = 0; i < names.size(); i++){
            result.put(names.get(i), response.getValues().get(i));
        }
        
        if(logger.isInfoEnabled()){
            logger.info(" >>> LDAPHelper: queryEntity id: " +id);
        }
        if(logger.isDebugEnabled()){
            logger.debug(" ### LDAPHelper: queryEntity results: "+result);
        }
        return result;
        
    }
    
    public static Map<String, String> queryEntity(String endpoint, String id){
        List<String> names = new ArrayList<String>();
        names.add("cn");
        names.add("mobile");
        names.add("employeeNumber");
        names.add("displayName");
        names.add("gender");
        return queryEntity(endpoint, id, names);
    }
    
    
    private static DSAIntegrationPortType getPort(String endpoint) {
        DSAIntegration service = new DSAIntegration();
        DSAIntegrationPortType port = service.getDSAIntegrationPortSoap11();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                endpoint);
        return port;
    }
}

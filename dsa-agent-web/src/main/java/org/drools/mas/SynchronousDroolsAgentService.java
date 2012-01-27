package org.drools.mas;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.drools.mas.ACLMessage;

/**
 *
 * @author salaboy
 */
@WebService
public interface SynchronousDroolsAgentService {
    @WebMethod
    List<ACLMessage> tell(ACLMessage message);
    
}

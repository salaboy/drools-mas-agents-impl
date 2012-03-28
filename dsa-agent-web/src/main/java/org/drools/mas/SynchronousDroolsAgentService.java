/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import org.drools.mas.ACLMessage;

import javax.jws.WebService;
import java.util.List;

/**
 *
 * @author salaboy
 */
@WebService
public interface SynchronousDroolsAgentService {
    List<ACLMessage> tell(ACLMessage message);
    public void dispose();
}

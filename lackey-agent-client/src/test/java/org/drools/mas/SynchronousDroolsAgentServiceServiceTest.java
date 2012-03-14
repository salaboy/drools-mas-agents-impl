/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.drools.mas.mock.MockFact;
import org.drools.mas.helpers.DialogueHelper;
import org.drools.mas.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.drools.grid.api.impl.*;

/**
 *
 * @author salaboy
 */
public class SynchronousDroolsAgentServiceServiceTest {

    public SynchronousDroolsAgentServiceServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

   
   @Test
    public void testSimpleInformWithHelper() {
        DialogueHelper agentHelper = new DialogueHelper("http://localhost:8080/lackey-agent/services/AsyncAgentService?wsdl");
        
        MockFact fact = new MockFact("patient1", 18);
        

        agentHelper.invokeInform("me", "you", fact);
        
        Object result = agentHelper.getReturn(true);
        assertNull(result);
       


    }
   
   @Test
    public void testLackeyAndActionAgentInAction() throws InterruptedException, MalformedURLException {
        DialogueHelper agentHelper = new DialogueHelper("http://localhost:8080/lackey-agent/services/AsyncAgentService?wsdl");
        
        
        
        
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);
        ResourceDescriptorImpl resourceDescriptorImpl = new ResourceDescriptorImpl();
        resourceDescriptorImpl.setId("1");
        resourceDescriptorImpl.setAuthor("salaboy");
        resourceDescriptorImpl.setName("my resource");
        resourceDescriptorImpl.setStatus("draft");
        resourceDescriptorImpl.setVersion("1");
        resourceDescriptorImpl.setCreationTime(new Date());
        resourceDescriptorImpl.setType("DRL");
        resourceDescriptorImpl.setDescription("this is my resource description");
        resourceDescriptorImpl.setResourceURL(new URL("file:/Users/salaboy/myports.txt"));
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        
        args.put("descriptor", resourceDescriptorImpl.getId());
        
        AgentID agentID = new AgentID("otherAgent");
        args.put("agentID", agentID.getName());
        
        
        
        agentHelper.invokeInform("", "", resourceDescriptorImpl);
        
        
        agentHelper.invokeInform("", "", agentID);
        
        
        
        
        String reqId = agentHelper.invokeRequest("forceInform", args);
        
        
        
        Thread.sleep(4000);
        
        List<ACLMessage> agentAnswers = agentHelper.getAgentAnswers(reqId);
        System.out.println(" >>>>>>>>>>>>> Answers = "+agentAnswers);
        assertEquals(2, agentAnswers.size());
        assertEquals(Act.AGREE, agentAnswers.get(0).getPerformative() );
        assertEquals(Act.INFORM, agentAnswers.get(1).getPerformative() );
        
        System.out.println(" #### agentAnswers: "+agentAnswers);
        
        
        
        
       
       


    }
}

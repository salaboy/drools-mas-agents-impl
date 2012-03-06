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

import java.util.LinkedHashMap;
import org.drools.mas.mock.MockFact;
import org.drools.mas.helpers.RequestHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author salaboy
 */
public class SynchronousDroolsAgentServiceServiceTest {
    private final String endpoint = "http://localhost:8080/action-agent/services/AsyncAgentService?WSDL";
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
        RequestHelper agentHelper = new RequestHelper(endpoint);

        MockFact fact = new MockFact("patient1", 18);

        agentHelper.invokeInform("me", "you", fact);

        Object result = agentHelper.getReturn(true);
        assertNull(result);



    }

    @Test
    public void testSimpleInform() {
        RequestHelper agentHelper = new RequestHelper(endpoint);

        MockFact fact = new MockFact("patient1", 18);

        agentHelper.invokeInform("me", "you", fact);

        Object result = agentHelper.getReturn(true);
        assertNull(result);



    }

    @Test
    public void testSimpleRequestToDeliverMessage() throws InterruptedException {
        RequestHelper agentHelper = new RequestHelper(endpoint);

        
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("refId", "284d7e8d-6853-46cb-bef2-3c71e565f90d");
        args.put("conversationId", "502ed27e-682d-43b3-ac2a-8bba3b597d13");
        args.put("subjectAbout", new String[] { "patient1", "docx", "id1", "id2", "id3" } );
        args.put("sender", "docx");
        args.put("mainRecipients", new String[] {"id1"} );
        args.put("secondaryRecipients", new String[] {"id2"});
        args.put("hiddenRecipients", new String[] {"id3"});
        args.put("type", "ALERT");
        args.put("header", "Risk threshold exceeded : MockPTSD (30%)");
        args.put("body", "<h2>MockPTSD</h2><br/>(This is free form HTML with an optionally embedded survey)<br/>Dear @{recipient.displayName},<br/>"
                + "<p>Your patient @{patient.displayName} has a high risk of developing the disease known as MockPTSD. <br/>"
                + "     The estimated rate is around 30.000000000000004%."
                + "</p>"
                + "<p>"
                + "They will contact you shortly."
                + "</p>"
                + "MockPTSD:<p class='agentEmbed' type='survey' id='01f0bb3d-7f67-450a-ad8d-be29c5611055' /p>"
                + "<br/>Thank you very much, <br/><p>Your Friendly Clinical Decision Support Agent, on behalf of @{provider.displayName}</p>");
        args.put("priority", "Critical");
        args.put("deliveryDate", "Tue Oct 11 23:46:36 CEST 2011");
        args.put("status", "New");


        agentHelper.invokeRequest("deliverMessage", args);
        
        Thread.sleep(4000);
        
        Object result = agentHelper.getReturn(false);
        assertNotNull(result);

        System.out.println("Results = "+result);

        assertEquals(true, result.toString().contains("refId"));
        assertEquals(true, result.toString().contains("convoId"));
    }
}

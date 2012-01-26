/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.salaboy.jbpm5.dev.guide.newactionagentclient;

import java.util.HashMap;
import org.drools.mas.body.content.*;

import java.util.Map;

import java.util.LinkedHashMap;


import org.drools.mas.util.*;
import org.drools.mas.body.acts.*;

import java.util.List;
import mock.MockFact;
import org.drools.mas.ACLMessage;
import org.drools.mas.Act;
import org.drools.mas.AgentID;
import org.drools.mas.Encodings;

import org.drools.mas.*;
import org.drools.mas.body.content.Rule;
import org.drools.mas.mappers.MyMapArgsEntryType;
import org.drools.runtime.rule.Variable;
import org.junit.*;
import static org.junit.Assert.*;

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

    //The following test simulate the following ACLMessage:
    //    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    //   <soapenv:Header/>
    //   <soapenv:Body>
    //      <message>
    //	<org.drools.mas.ACLMessage>
    //  <id>0</id>
    //  <messageType>DEFAULT_ACL_MESSAGE_TYPE</messageType>
    //  <conversationId>0</conversationId>
    //  <replyBy>0</replyBy>
    //  <ontology>KMR2</ontology>
    //  <language>DROOLS_DRL</language>
    //  <encoding>JSON</encoding>
    //  <sender>
    //    <name>me@org.DROOLS</name>
    //  </sender>
    //  <receiver>
    //    <org.drools.mas.AgentID>
    //      <name>you@org.DROOLS</name>
    //    </org.drools.mas.AgentID>
    //  </receiver>
    //  <performative>INFORM</performative>
    //  <body class="org.drools.mas.body.acts.Inform">
    //    <proposition>
    //      <encodedContent>{&quot;org.kmr2.mock.MockFact&quot;:{&quot;name&quot;:&quot;patient1&quot;,&quot;age&quot;:18}}</encodedContent>
    //      <encoded>true</encoded>
    //    </proposition>
    //  </body>
    //</org.drools.mas.ACLMessage>
    //      </message>
    //   </soapenv:Body>
    //</soapenv:Envelope>
    @Ignore
    public void hello() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();

        ACLMessage informMessage = new ACLMessage();
        informMessage.setId("0");
        informMessage.setPerformative(Act.INFORM);
        informMessage.setMessageType("DEFAULT_ACL_MESSAGE_TYPE");
        informMessage.setConversationId("0");
        informMessage.setReplyBy(0);
        informMessage.setOntology("KMR2");
        informMessage.setLanguage("DROOLS_DRL");
        informMessage.setEncoding(Encodings.JSON);
        AgentID sender = new AgentID();
        sender.setName("me@org.DROOLS");
        informMessage.setSender(sender);

        Inform inform = new Inform();
        inform.setPerformative(Act.INFORM);
        Info info = new Info();
        info.setEncodedContent("{\"mock.MockFact\":{\"name\":\"patient1\",\"age\":18}}");
        info.setEncoded(true);
        info.setEncoding(Encodings.JSON);
        inform.setProposition(info);
        informMessage.setBody(inform);

        AgentID receiver = new AgentID();
        receiver.setName("you@org.DROOLS");

        List<AgentID> receivers = informMessage.getReceiver();
        receivers.add(receiver);

        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(informMessage);

        assertNotNull(answers);
        assertEquals(0, answers.size());

        ACLMessage queryIfMessage = new ACLMessage();
        queryIfMessage.setId("1");
        queryIfMessage.setPerformative(Act.QUERY_IF);
        queryIfMessage.setMessageType("DEFAULT_ACL_MESSAGE_TYPE");
        queryIfMessage.setConversationId("1");
        queryIfMessage.setReplyBy(0);
        queryIfMessage.setOntology("KMR2");
        queryIfMessage.setLanguage("DROOLS_DRL");
        queryIfMessage.setEncoding(Encodings.JSON);

        //I'm using the same sender
        queryIfMessage.setSender(sender);

        QueryIf queryIf = new QueryIf();
        queryIf.setPerformative(Act.QUERY_IF);
        info = new Info();

        info.setEncodedContent("{\"mock.MockFact\":{\"name\":\"patient1\",\"age\":18}}");
        info.setEncoded(true);
        info.setEncoding(Encodings.JSON);
        queryIf.setProposition(info);
        queryIfMessage.setBody(queryIf);

        //I'm using the same receiver
        receivers = queryIfMessage.getReceiver();
        receivers.add(receiver);
        answers = synchronousDroolsAgentServicePort.tell(queryIfMessage);



        assertNotNull(answers);
        assertEquals(1, answers.size());


    }

    @Ignore
    public void testSimpleInform() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        MockFact fact = new MockFact("patient1", 18);
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        ACLMessage info = factory.newInformMessage("me", "you", fact);

        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(info);

        assertNotNull(answers);
        assertEquals(0, answers.size());
//        assertNull(mainResponseInformer.getResponses(info));
//        StatefulKnowledgeSession target = mainAgent.getInnerSession("session1");
//        assertTrue(target.getObjects().contains(fact));

    }
    
    @Test
    public void testSimpleInformWithHelper() {
        SynchronousRequestHelper agentHelper = new SynchronousRequestHelper("http://localhost:8080/new-action-agent/services/?WSDL");
        
        MockFact fact = new MockFact("patient1", 18);
        

        agentHelper.invokeInform("me", "you", fact);
        
        Object result = agentHelper.getReturn(true);
        assertNull(result);
       


    }

    @Ignore
    public void testInformAsTrigger() {
        MockFact fact = new MockFact("patient1", 22);
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);


        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        ACLMessage info = factory.newInformMessage("me", "you", fact);
        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(info);

        assertNotNull(answers);
        assertEquals(0, answers.size());


//        assertNull(mainResponseInformer.getResponses(info));
//        StatefulKnowledgeSession target = mainAgent.getInnerSession("session2");
//        for (Object o : target.getObjects())
//            System.err.println("\t Inform-Trigger test : " + o);
//        assertTrue(target.getObjects().contains(new Double(22.0)));
//        assertTrue(target.getObjects().contains(new Integer(484)));
    }

    @Ignore
    public void testQueryIf() {
        
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        MockFact fact = new MockFact("patient1", 18);
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        ACLMessage info = factory.newInformMessage("me", "you", fact);
        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(info);
        
        assertEquals(0, answers.size());
        
        ACLMessage qryif = factory.newQueryIfMessage("me", "you", fact);
        answers = synchronousDroolsAgentServicePort.tell(qryif);
        
        assertEquals(1, answers.size());

        ACLMessage answer = answers.get(0);
        MessageContentEncoder.decodeBody(answer.getBody(), answer.getEncoding());
        assertEquals(Act.INFORM_IF, answer.getPerformative());
        // We cannot get the Data inside INFO because it's transient. We can do this when we are executing locally,
        // but not in a remote environment. Should we change that variable to not Transient and do those mappings?
        
        //assertEquals((((InformIf) answer.getBody()).getProposition().getData()), fact);
        assertEquals(((MockFact)(((InformIf) answer.getBody()).getProposition().getData())).getName(), fact.getName());
    }

    @Ignore
    public void testQueryRef() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        MockFact fact = new MockFact("patient1", 18);
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        ACLMessage info = factory.newInformMessage("me", "you", fact);
        synchronousDroolsAgentServicePort.tell(info);
        Query query = MessageContentFactory.newQueryContent("testQuery", new Object[]{MessageContentHelper.variable("?mock"), "patient1", MessageContentHelper.variable("?age")});
        ACLMessage qryref = factory.newQueryRefMessage("me", "you", query);
        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(qryref);

        assertNotNull(answers);
        assertEquals(2, answers.size());

        ACLMessage answer = answers.get(0);
        assertEquals(Act.AGREE, answer.getPerformative());
        ACLMessage answer2 = answers.get(1);
        assertEquals(Act.INFORM_REF, answer2.getPerformative());

    }

    @Ignore
    public void testRequest() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x", new Double(36));

        Action action = MessageContentFactory.newActionContent("squareRoot", args);
        ACLMessage req = factory.newRequestMessage("me", "you", action);

        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(req);

        assertNotNull(answers);
        assertEquals(2, answers.size());

        ACLMessage answer = answers.get(0);
        assertEquals(Act.AGREE, answer.getPerformative());
        ACLMessage answer2 = answers.get(1);
        assertEquals(Act.INFORM, answer2.getPerformative());

        assertTrue(((Inform) answer2.getBody()).getProposition().getEncodedContent().contains("6.0"));

    }
//
//
//
//
// 

    @Ignore
    public void testRequestWhen() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        Double in = new Double(36);

        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        Map<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("x", in);


        Rule condition = new Rule();
        condition.setDrl("String( this == \"actionTrigger\" || this == \"actionTrigger2\")");
        Action action = MessageContentFactory.newActionContent("squareRoot", args);
        ACLMessage req = factory.newRequestWhenMessage("me", "you", action, condition);

        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(req);

        ACLMessage info = factory.newInformMessage("me", "you", new String("actionTrigger"));
        answers = synchronousDroolsAgentServicePort.tell(info);


        ACLMessage info2 = factory.newInformMessage("me", "you", new String("actionTrigger2"));
        answers = synchronousDroolsAgentServicePort.tell(info);



        //assertEquals(1, answers.size());
        //assertEquals(6.0, (Double) ans.iterator().next().get("$return"), 1e-6);




    }
//
//
//
//
//
//    
//    @Ignore
//    public void testRequestWhenever() {
//
//        Double in = new Double(36);
//
//        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);
//
//        Map<String,Object> args = new LinkedHashMap<String,Object>();
//        args.put("x", in);
//
//
//        Rule condition = new Rule("String( this == \"actionTrigger\" || this == \"actionTrigger2\")");
//
//        ACLMessage req = factory.newRequestWheneverMessage("me", "you", new Action("squareRoot", args), condition);
//        mainAgent.tell(req);
//
//        ACLMessage info = factory.newInformMessage("me","you",new String("actionTrigger"));
//        mainAgent.tell(info);
//
//
//        ACLMessage info2 = factory.newInformMessage("me","you",new String("actionTrigger2"));
//        mainAgent.tell(info2);
//
//
//        StatefulKnowledgeSession s2 = mainAgent.getInnerSession("session2");
//        QueryResults ans = s2.getQueryResults("squareRoot", in, Variable.v);
//        assertEquals(2, ans.size());
//        Iterator<QueryResultsRow> iter = ans.iterator();
//        assertEquals(6.0,(Double) iter.next().get("$return"),1e-6);
//        assertEquals(6.0,(Double) iter.next().get("$return"),1e-6);
//
//
//        fail("INCOMPLETE TEST : Needs open queries to send answer back with a message, but keep trigger rule!");
//
//    }
//
//
//
//
//
//

    @Ignore
    public void testRequestWithMultipleOutputs() {
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        Map<String, Object> args = new LinkedHashMap<String, Object>();
        Double x = 32.0;

        args.put("x", x);
        args.put("?y", Variable.v);
        args.put("?inc", Variable.v);


        Action action = MessageContentFactory.newActionContent("randomSum", args);
        ACLMessage req = factory.newRequestMessage("me", "you", action);



        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(req);

        assertNotNull(answers);
        assertEquals(2, answers.size());

        ACLMessage answer = answers.get(0);
        assertEquals(Act.AGREE, answer.getPerformative());
        ACLMessage answer2 = answers.get(1);
        assertEquals(Act.INFORM_REF, answer2.getPerformative());

        //answer2.getBody().decode(answer2.getEncoding());
        MessageContentEncoder.decodeBody(answer2.getBody(), answer2.getEncoding());
        assertEquals(InformRef.class, answer2.getBody().getClass());

        Ref ref = ((InformRef) answer2.getBody()).getReferences();
        assertNotNull(ref.getReferences());
        boolean containsInc = false;
        boolean containsY = false;
        Double z = null;
        Double y = null;
        for (MyMapArgsEntryType entry : ref.getReferences()) {
            if (entry.getKey().equals("?inc")) {
                containsInc = true;
                assertEquals(Double.class, entry.getValue().getClass());
                z = (Double) entry.getValue();
            }
            if (entry.getKey().equals("?y")) {
                containsY = true;
                assertEquals(Double.class, entry.getValue().getClass());
                y = (Double) entry.getValue();
            }
        }
        assertTrue(containsInc);
        assertTrue(containsY);



        assertEquals(y, x + z, 1e-6);

    }

    @Ignore
    public void testSimpleInformInNewSession() {
        MockFact fact = new MockFact("patient3", 18);
        MockFact fact2 = new MockFact("patient3", 44);
        SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        ACLMessage info = factory.newInformMessage("me", "you", fact);
        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(info);

        assertEquals(0, answers.size());
//        StatefulKnowledgeSession target = mainAgent.getInnerSession("patient3");
//        assertNotNull(target);
//        assertTrue(target.getObjects().contains(fact));

        ACLMessage info2 = factory.newInformMessage("me", "you", fact2);
        answers = synchronousDroolsAgentServicePort.tell(info2);
        assertEquals(0, answers.size());
        //assertTrue(target.getObjects().contains(fact2));

    }

    @Ignore
    public void testNotUnderstood() {
                SynchronousDroolsAgentService synchronousDroolsAgentServicePort = new SynchronousDroolsAgentServiceImplService().getSynchronousDroolsAgentServiceImplPort();
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);

        Action action = MessageContentFactory.newActionContent("nonExistingRequest", new LinkedHashMap());
        ACLMessage notUnd = factory.newRequestMessage("me", "you", action);
        List<ACLMessage> answers = synchronousDroolsAgentServicePort.tell(notUnd);

        assertEquals(Act.NOT_UNDERSTOOD, answers.get(0).getPerformative());

    }
}

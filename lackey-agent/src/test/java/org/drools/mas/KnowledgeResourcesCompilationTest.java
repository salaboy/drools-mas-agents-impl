/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.mas;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.drools.grid.api.impl.ResourceDescriptorImpl;
import org.drools.mas.body.content.Action;
import org.drools.mas.core.DroolsAgent;
import org.drools.mas.util.ACLMessageFactory;
import org.drools.mas.util.MessageContentFactory;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.*;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.drools.builder.ResourceType;

/**
 *
 * @author salaboy
 */
public class KnowledgeResourcesCompilationTest {
    private static Logger logger = LoggerFactory.getLogger(KnowledgeResourcesCompilationTest.class);
    private Server server;
    public KnowledgeResourcesCompilationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
         DeleteDbFiles.execute("~", "mydb", false);

        logger.info("Staring DB for white pages ...");
        try {
            server = Server.createTcpServer(null).start();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        logger.info("DB for white pages started! ");
    }

    @After
    public void tearDown() {
        
        logger.info("Stopping DB ...");
        server.stop();
        logger.info("DB Stopped!");
    }
    /*
     * Test for check that the resources provided inside this agent 
     * at least compile without errors. To ensure that the agent can be 
     * initialized correctly
     */
//    @Test
//    public void compilationTest() {
//        
//        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        DroolsAgent agent = (DroolsAgent) context.getBean("agent");
//        
//        assertNotNull(agent);
//        
//        agent.dispose();
//        
//    }
    
    
    @Test
    public void lackeyAgentInformsOtherAgent() throws MalformedURLException, InterruptedException {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DroolsAgent agent = (DroolsAgent) context.getBean("agent");
        
        assertNotNull(agent);
        
        ACLMessageFactory factory = new ACLMessageFactory(Encodings.XML);
        ResourceDescriptorImpl resourceDescriptorImpl = new ResourceDescriptorImpl();
        resourceDescriptorImpl.setId("1");
        resourceDescriptorImpl.setAuthor("salaboy");
        resourceDescriptorImpl.setName("my resource");
        resourceDescriptorImpl.setStatus("draft");
        resourceDescriptorImpl.setVersion("1");
        resourceDescriptorImpl.setCreationTime(new Date());
        resourceDescriptorImpl.setType(ResourceType.DRL);
        resourceDescriptorImpl.setDescription("this is my resource description");
        resourceDescriptorImpl.setResourceURL(new URL("file:/Users/salaboy/myports.txt"));
        Map<String, Object> args = new HashMap<String, Object>();
        
        args.put("descriptor", resourceDescriptorImpl.getId());
        
        AgentID agentID = new AgentID("otherAgent");
        args.put("agentID", agentID.getName());
        
        
        ACLMessage inf = factory.newInformMessage("", "", resourceDescriptorImpl);
        agent.tell(inf);
        
        ACLMessage inf2 = factory.newInformMessage("", "", agentID);
        agent.tell(inf2);
        
        
        Action action = MessageContentFactory.newActionContent("forceInform", args);
        ACLMessage req = factory.newRequestMessage("", "", action);

        
        agent.tell(req);
        
        Thread.sleep(4000);
        
        List<ACLMessage> agentAnswers = agent.getAgentAnswers(req.getId());
//        assertEquals(2, agentAnswers.size());
//        assertEquals(Act.AGREE, agentAnswers.get(0).getPerformative() );
//        assertEquals(Act.INFORM, agentAnswers.get(1).getPerformative() );
        
        System.out.println(" #### agentAnswers: "+agentAnswers);
        
        
        Thread.sleep(40000);
        
        agent.dispose();
        
    }
}

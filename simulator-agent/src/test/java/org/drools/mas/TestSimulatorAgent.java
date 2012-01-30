/*
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect.
 * All rights reserved.Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *    END OF TERMS AND CONDITIONS
 */

package org.drools.mas;

import gov.hhs.fha.nhinc.kmr2.simulatorAgent.Configuration;
import org.drools.mas.body.acts.Inform;
import org.drools.mas.core.DroolsAgent;
import org.drools.mas.core.DroolsAgentConfiguration;
import org.drools.mas.core.DroolsAgentFactory;
import org.drools.mas.core.DroolsAgentResponseInformer;
import org.drools.mas.util.ACLMessageFactory;
import org.drools.mas.util.MessageContentFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestSimulatorAgent {



    private static DroolsAgent mainAgent;

    private static MockResponseInformer mainResponseInformer;


    private ACLMessageFactory factory = new ACLMessageFactory( Encodings.XML );


    @BeforeClass
    public static void createAgents() {

        mainResponseInformer = new MockResponseInformer();

        DroolsAgentConfiguration mainConfig = new DroolsAgentConfiguration();
        mainConfig.setAgentId( "Mock Sim Agent" );
        mainConfig.setChangeset("simulatorAgent_changeset.xml");
        mainConfig.setDefaultSubsessionChangeSet("simulatorAgent_defSession_changeset.xml");
        mainConfig.setResponseInformer( mainResponseInformer );
        DroolsAgentConfiguration.SubSessionDescriptor subDescr1 = new DroolsAgentConfiguration.SubSessionDescriptor(
                "session1",
                "simulatorAgent_defSession_changeset.xml",
                "NOT_USED_YET" );
        mainConfig.addSubSession(subDescr1);
        mainAgent = DroolsAgentFactory.getInstance().spawn( mainConfig );

    }



    @AfterClass
    public static void cleanUp() {
        if (mainAgent != null) {
            mainAgent.dispose();
        }

    }


    private void sleep( long millis ) {
        try {
            Thread.sleep( millis );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }









    private String probe( String userId ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put( "userId", userId );

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("probe", args) );
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);
        return ret(ans);
    }

    private String ret(ACLMessage ans) {
        if ( ! ans.getPerformative().equals( Act.INFORM ) ) {
            System.err.println( ans );
        }
        assertEquals( Act.INFORM, ans.getPerformative() );
        return ((Inform) ans.getBody()).getProposition().getEncodedContent();
    }





    private String getPlanningModels( String userId ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId",userId);
        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getPlanningModels", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }

    private String searchConfigurations( String userId, String name,
                                         Date after, Date before, Date run,
                                         String author,
                                         Double scoreMin, Double scoreMax,
                                         Integer start, Integer batch ) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);

        args.put("nameContains", name);
        args.put("lastSavedAfter", after);
        args.put("lastSavedBefore", before) ;
        args.put("lastRunBefore", run );
        args.put("authorContains", author );
        args.put("scoreAtLeast", scoreMin );
        args.put("scoreUpTo", scoreMax);
        args.put("startIndex", start );
        args.put("batchSize", batch );

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("searchConfigurations", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }

    private String getConfigurationDetails( String userId, String modelId,
                                            String configId, Boolean blank ) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);

        args.put("modelId", modelId);
        args.put("configId", configId) ;
        args.put("blank", blank );


        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getConfigurationDetail", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }



    private String saveConfiguration( String userId, String configId,
                                      String modelId, Configuration config ) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);

        args.put("modelId", modelId);
        args.put("configId", configId) ;
        args.put("config", config );


        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("saveConfiguration", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }

    private String getSimulations(String userId, String modelId) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);

        args.put("modelId", modelId);

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getSimulations", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);

    }

    private String launchSimulation(String userId, String modelId, Configuration config, String initId ) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);
        args.put("modelId", modelId);
        args.put("configuration", config);
        args.put("startingSolutionId", initId);


        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("launchSimulation", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);

    }

    private String commandSimulation( String userId, String modelId, String sid, String cmd ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);
        args.put("modelId", modelId);
        args.put("simulationId", sid);
        args.put("action", cmd);


        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("commandSimulation", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }


    private String searchPlanResults( String userId, String modelId,
                                      Integer startIndex, Integer batchSize,
                                      String status,
                                      String descriptionContains, String authorContains, String nameContains,
                                      Date lastSavedAfter, Date lastSavedBefore,
                                      Double scoreUpTo, Double scoreAtLeast ) {

        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);
        args.put("modelId", modelId);
        args.put("startIndex", startIndex );
        args.put("batchSize", batchSize );
        args.put( "status", status);
        args.put("descriptionContains", descriptionContains );
        args.put("authorContains", authorContains );
        args.put("nameContains", nameContains );
        args.put("lastSavedAfter", lastSavedAfter);
        args.put("lastSavedBefore", lastSavedBefore) ;
        args.put("scoreUpTo", scoreUpTo);
        args.put("scoreAtLeast", scoreAtLeast );


        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("searchPlanResults", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }


    private String getResultDetails(String userId, String resId, String modelId) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);
        args.put("modelId", modelId);
        args.put("resultId", resId );

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getResultDetail", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }


    private String requestResultOperation(String userId, String resId, String action) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId",userId);
        args.put("resultId", resId );
        args.put("action", action);

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("requestResultOperation", args));
        mainAgent.tell(req);
        ACLMessage ans = mainResponseInformer.getResponses(req).get(1);

        return ret(ans);
    }




    private List<String> getSimulationIds(String sims) {
        return getElements( sims, "//simulationId");
    }



    public List<String> getElements(String xml, String xpath) {
        try {
            Document dox = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream( xml.getBytes()) );
            XPath finder = XPathFactory.newInstance().newXPath();

            NodeList nodes = (NodeList) finder.evaluate( xpath, dox, XPathConstants.NODESET );
            List<String> list = new ArrayList<String>();
            for ( int j = 0; j < nodes.getLength(); j++ ) {
                list.add(((Element) nodes.item(j)).getTextContent());
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
        return Collections.emptyList();
    }

    public String getValue(String xml, String xpath) {
        try {
            Document dox = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream( xml.getBytes()) );
            XPath finder = XPathFactory.newInstance().newXPath();

            return (String) finder.evaluate( xpath, dox, XPathConstants.STRING );
        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
        return null;
    }

















    @Test
    public void testProbeWithCreatedSession() {

        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");


        String content2 = probe( "drY" );
        System.out.println("--------------------------------------------------");
        System.out.println( content2 );
        System.out.println("--------------------------------------------------");

    }


    @Test
    public void testGetPlanningModels() {

        String planModels = getPlanningModels( "drX" );
        System.out.println( "Planning Models \n" + planModels );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }



    @Test
    public void testSearchConfigurations() {

        String configResults = searchConfigurations( "drX",
                "test",
                new Date(0), new Date(), new Date(),
                "eafry",
                0.0, 100.0,
                0, 10 );

        System.out.println( "Configs  \n" + configResults );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }



    @Test
    public void testGetConfigDetail() {

        String config = getConfigurationDetails( "drX",
                "-1",
                "Mock Sim",
                true );

        System.out.println( "Configs  \n" + config );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }


    @Test
    public void testSaveConfig() {

        Configuration conf = new Configuration( "-1" );
        conf.setModelId( "Mock Sim" );
        conf.setCreatedDate( new Date() );
        conf.setDuration( "3 Mon" );

        String configId = saveConfiguration("drX",
                conf.getConfigId(),
                conf.getModelId(),
                conf );

        System.out.println( "Save  \n" + configId );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }


    @Test
    public void testLaunchSimulations() {

        String sims = launchSimulation("drX", "Mock Sim", new Configuration(), "-1" );

        System.out.println( "Sims  \n" + sims );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }


    @Test
    public void testGetSimulations() {
        Configuration conf = new Configuration();

        conf.setName( "Test1" );
        conf.setAuthor( "sotty" );
        launchSimulation("drX", "Mock Sim", conf, "-1");

        conf.setName( "Test2" );
        conf.setAuthor( "sotty" );
        launchSimulation("drX", "Mock Sim", conf, "-1" );

        String sims = getSimulations("drX", "Mock Sim");

        System.out.println( "Sims  \n" + sims );


        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }


    @Test
    public void testCommandSimulation() {
        Configuration conf = new Configuration();

        conf.setName( "Test1" );
        conf.setAuthor( "sotty" );
        launchSimulation("drX", "Mock Sim", conf, "-1" );

        String sims = getSimulations("drX", "Mock Sim");
        List<String> sids = getSimulationIds(sims);

        String sid = sids.get(0);
        String res;

        res = commandSimulation( "drX", "Mock Sim", sid, "Pause" );
        System.out.println( "Cmd  \n" + res );
        assertEquals( "true", getElements( res, "//successStatus").get( 0 ) );

        res = commandSimulation( "drX", "Mock Sim", sid, "Start" );
        System.out.println( "Cmd  \n" + res );
        assertEquals( "true", getElements( res, "//successStatus").get( 0 ) );

        res = commandSimulation( "drX", "Mock Sim", sid, "Start" );
        System.out.println( "Cmd  \n" + res );
        assertEquals( "false", getElements( res, "//successStatus").get( 0 ) );

        res = commandSimulation( "drX", "Mock Sim", sid, "Stop" );
        System.out.println( "Cmd  \n" + res );
        assertEquals( "true", getElements( res, "//successStatus").get( 0 ) );

    }


    @Test
    public void testSearchPlanResults() {

        String planResults = searchPlanResults( "drX", "Mock Sim",
                -1, Integer.MAX_VALUE,
                "saved",
                "test", "sot", "test",
                new Date(0), new Date(),
                100.0, 0.0 );
        System.out.println( "Configs  \n" + planResults );

        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }

    @Test
    public void testGetResultsDetail() {

        String results = getResultDetails( "drX", "99", "Mock Sim" );
        System.out.println( "Configs  \n" + results );

        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }



    @Test
    public void testRequestResultOperation() {

        String res = requestResultOperation( "drX", "resId", "Save" );
        System.out.println( "Result  \n" + res );

        String content = probe( "drX" );
        System.out.println("--------------------------------------------------");
        System.out.println( content );
        System.out.println("--------------------------------------------------");

    }





}


class MockResponseInformer implements DroolsAgentResponseInformer {

    private Map<ACLMessage,List<ACLMessage>> responses = new HashMap<ACLMessage, List<ACLMessage>>();

    public synchronized void informResponse(ACLMessage originalMessage, ACLMessage response) {
        if (!responses.containsKey(originalMessage)){
            responses.put(originalMessage, new ArrayList<ACLMessage>());
        }

        responses.get(originalMessage).add(response);
    }

    public List<ACLMessage> getResponses(ACLMessage originalMessage){
        return this.responses.get(originalMessage);
    }

}
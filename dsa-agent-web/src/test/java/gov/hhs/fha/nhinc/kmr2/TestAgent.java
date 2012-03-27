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

package gov.hhs.fha.nhinc.kmr2;

import org.drools.ClassObjectFilter;
import org.drools.definition.type.FactType;
import org.drools.mas.ACLMessage;
import org.drools.mas.Act;
import org.drools.mas.Encodings;
import org.drools.mas.body.acts.Failure;
import org.drools.mas.body.acts.Inform;
import org.drools.mas.core.DroolsAgent;
import org.drools.mas.util.ACLMessageFactory;
import org.drools.mas.util.MessageContentEncoder;
import org.drools.mas.util.MessageContentFactory;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class TestAgent {

    private static DroolsAgent mainAgent;

    private ACLMessageFactory factory = new ACLMessageFactory( Encodings.XML );

    private static Logger logger = LoggerFactory.getLogger( TestAgent.class );
    private static Server server;

    @BeforeClass
    public static void createAgents() {

        DeleteDbFiles.execute("~", "mydb", false);

        logger.info("Staring DB for white pages ...");
        try {
            server = Server.createTcpServer(null).start();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        logger.info("DB for white pages started! ");


        ApplicationContext context = new ClassPathXmlApplicationContext( "META-INF/applicationContext.xml" );
        mainAgent = (DroolsAgent) context.getBean( "agent" );
        assertNotNull (mainAgent );

    }


    @AfterClass
    public static void cleanUp() {
        if (mainAgent != null) {
            mainAgent.dispose();
        }

        logger.info("Stopping DB ...");
        server.stop();
        logger.info("DB Stopped!");

    }




    private void sleep( long millis ) {
        try {
            Thread.sleep( millis );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }







    private void waitForResponse( String id ) {
        do {
            try {
                Thread.sleep( 1000 );
                System.out.println( "Waiting for messages, now : " + mainAgent.getAgentAnswers( id ).size() );
            } catch (InterruptedException e) {
                fail( e.getMessage() );
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } while ( mainAgent.getAgentAnswers( id ).size() < 2);

    }



    private String probe( String patientId ) throws InterruptedException {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("patientId", patientId);

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("probe", args) );
        mainAgent.tell(req);

        waitForResponse( req.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers( req.getId() ).get( 1 );
        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }



    private static class FailureException extends Exception {
        private FailureException(String message) {
            super( "FAILURE:" + message );
        }
    }

    private String ret(ACLMessage ans) throws FailureException {

        if ( ! ans.getPerformative().equals( Act.INFORM ) ) {
            System.err.println( ans );
        }

        if ( ans.getPerformative().equals( Act.FAILURE ) ) {
            throw new FailureException( ((Failure) ans.getBody()).getCause().getData().toString() );
        }
        assertEquals( Act.INFORM, ans.getPerformative() );
        MessageContentEncoder.decodeBody( ans.getBody(), Encodings.XML );
        return (String) ((Inform) ans.getBody()).getProposition().getData();

    }


    private String getSurvey( String userId, String patientId, String surveyId ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId",userId);
        args.put("patientId",patientId);
        args.put("surveyId",surveyId);

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getSurvey", args));
        mainAgent.tell(req);

        waitForResponse( req.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(req.getId()).get(1);
        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    private String setSurvey( String userId, String patientId, String surveyId, String questionId, String value ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId",userId);
        args.put("patientId",patientId);
        args.put("surveyId",surveyId);
        args.put("questionId", questionId);
        args.put("answer",value);

        ACLMessage set = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("setSurvey", args) );
        mainAgent.tell(set);

        waitForResponse( set.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(set.getId()).get(1);
        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    private void setRiskThreshold(String userId, String patientId, String modelId, String type, Integer value) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.clear();
        args.put("userId",userId);
        args.put("patientId",patientId);
        args.put("modelId",modelId);
        args.put("type",type);
        args.put("threshold",value);

        ACLMessage setThold = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("setRiskThreshold", args) );
        mainAgent.tell(setThold);

        waitForResponse( setThold.getId() );
    }

    private String getModels( String userId, String patientId, List tags ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId",userId);
        args.put("patientId",patientId);
        args.put("types", tags);
        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getModels", args));

        mainAgent.tell(req);

        waitForResponse( req.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(req.getId()).get(1);

        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    private String getRiskModels( String userId, String patientId ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId",userId);
        args.put("patientId",patientId);
        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getRiskModels", args));
        mainAgent.tell(req);

        waitForResponse( req.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(req.getId()).get(1);

        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    private String getRiskModelsDetail(String userId, String patientId, String[] modelsIds) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();


        args.put("userId", userId );
        args.put("patientId", patientId );
        args.put("modelIds",modelsIds );

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getRiskModelsDetail", args) );

        mainAgent.tell(req);

        waitForResponse( req.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(req.getId()).get(1);
        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }


    public String startDiagnosticGuideProcess( String userId, String patientId, String disease ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId", userId );
        args.put("patientId", patientId );
        args.put("disease", disease );
        ACLMessage start = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("startDiagnosticGuideProcess", args) );

        mainAgent.tell(start);

        waitForResponse( start.getId() );

        ACLMessage ans = mainAgent.getAgentAnswers(start.getId()).get(1);
//        MessageContentEncoder.decodeBody( ans.getBody(), Encodings.XML );
//        String dxProcessId = (String) ((Inform) ans.getBody()).getProposition().getData();
//        return dxProcessId;
        try {
            return ret(ans);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    public String getDiagnosticProcessStatus( String userId, String patientId, String dxProcessId, boolean forceRefresh ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId", userId);
        args.put("patientId", patientId );
        args.put("dxProcessId",dxProcessId);
        args.put("forceRefresh",forceRefresh );
        ACLMessage reqStatus = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getDiagnosticProcessStatus", args) );

        mainAgent.tell(reqStatus);

        waitForResponse( reqStatus.getId() );

        ACLMessage ans2 = mainAgent.getAgentAnswers( reqStatus.getId() ).get( 1 );
        try {
            return ret(ans2);
        } catch (FailureException e) {
            return e.getMessage();
        }
    }

    public String setDiagnosticActionStatus( String userId, String patientId, String dxProcessId, String actionCtrlQuestId, String status ) {

        System.out.println( "Setting action status " + status + " for action ctrl quest id " + actionCtrlQuestId );

        String ctrl = getSurvey( userId, patientId, actionCtrlQuestId );
        String txQid = getValue( ctrl, "//questionName[.='transition']/../itemId" );

        System.err.println( "Lloking up " + actionCtrlQuestId + " vs " + txQid );


        if ( "Started".equals( status ) ) {
            setSurvey( userId, patientId, actionCtrlQuestId, txQid, "START" );
        } else if ( "Complete".equals( status ) ) {
            setSurvey( userId, patientId, actionCtrlQuestId, txQid, "COMPLETE" );
        }




        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );

        if ( status.contains( "Complete" ) ) {
            System.err.println( statusXML );
        }

        String ans = getValue( statusXML, "//questionnaireId[.='"+ actionCtrlQuestId + "']/../status" );


        System.out.println( "ACTION STATE " + ans );
        return ans;

    }

    public void advanceDiagnosticProcessStatus( String userId, String patientId, String dxProcessId ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId", userId);
        args.put("patientId", patientId );
        args.put("dxProcessId",dxProcessId);
        ACLMessage reqStatus = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("advanceDiagnosticGuideProcess", args) );


        mainAgent.tell(reqStatus);
//           ACLMessage ans2 = mainResponseInformer.getResponses(reqStatus).get(1);
        waitForResponse( reqStatus.getId() );
    }


    public void completeDiagnosticGuideProcess( String userId, String patientId, String dxProcessId, String status ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId", userId);
        args.put("patientId", patientId );
        args.put("dxProcessId",dxProcessId);
        args.put("status", status);
        ACLMessage reqStatus = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("completeDiagnosticGuideProcess", args) );

        mainAgent.tell(reqStatus);
//           ACLMessage ans2 = mainResponseInformer.getResponses(reqStatus).get(1);
        waitForResponse( reqStatus.getId() );
    }



    private List<String> getModels( String xml ) {
        return getElements( xml, "//modelId" );
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

    private String lookupInBody(String statusXML, String actionName) {
        String body = getValue( statusXML, "//" + testDecModel + "." + actionName + "/body" );

        System.err.println( body );

        int start = body.indexOf("id=");
        if ( start > 0 ) {
            int end = body.indexOf( "\'", start+4 );

            return body.substring( start +4, end );
        }
        return null;
    }


    private static final String testDecModel = "gov.hhs.fha.nhinc.kmr2.clinicalAgent.models.decision";





























    @Test
    public void testSetSurvey() {
        String[] qid = new String[8];
        String[] values = new String[8];
        XPath finder = XPathFactory.newInstance().newXPath();


        String xmlSurv = getSurvey( "drX", "99990070", "123456UNIQUESURVEYID" );

        for ( int j = 1; j < 8; j++ ) {
            qid[j] = getValue( xmlSurv, "//org.drools.informer.presentation.QuestionGUIAdapter/questionName[.='question"+j+"']/../itemId" );
            System.out.println("ID OF Q" + j + " >> " + qid[j] );
        }


        for ( int j = 1; j < 8; j++ ) {
            String val = getValue( xmlSurv, "//org.drools.informer.presentation.QuestionGUIAdapter/itemId[.='"+qid[j]+"']/../successType" );
            System.out.println( j + " : " + val );
            switch (j) {
                case 1  :
                case 2  :
                case 4  : assertEquals( "valid", val ); break;
                case 7  : assertEquals( "missing", val ); break;
                default : assertEquals( "invalid", val ); break;
            }
        }


        values[1] = new Integer((int) Math.ceil( 6 * Math.random() )).toString();
        values[2] = Math.random() > 0.5 ? "Urban" : "Rural";
        List v3 = new ArrayList();
        if ( Math.random() > 0.5 ) { v3.add( "CMP" ); }
        if ( Math.random() > 0.5 ) { v3.add( "FSH" ); }
        if ( Math.random() > 0.5 ) { v3.add( "GLF" ); }
        if ( Math.random() > 0.5 ) { v3.add( "HGL" ); }
        if ( v3.size() == 0 ) {
            v3.add( "HGL" );
        }
        values[3] = v3.toString().replace("[","").replace("]","").replace(" ","");
        values[4] = new Integer((int) Math.round( 100 * Math.random() )).toString();
        values[5] = UUID.randomUUID().toString();
        values[6] = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        values[7] = new Integer((int) Math.round( 100 * Math.random() )).toString();


        for ( int j = 1; j < 8; j++ ) {
            setSurvey( "drx", "99990070", "123456UNIQUESURVEYID", qid[j], values[j].toString());
        }


        xmlSurv = getSurvey( "drX", "99990070", "123456UNIQUESURVEYID" );
        for ( int j = 1; j < 8; j++ ) {
            String val = getValue( xmlSurv, "//org.drools.informer.presentation.QuestionGUIAdapter/itemId[.='"+qid[j]+"']/../successType" );
            switch (j) {
                default : assertEquals( "valid", val ); break;
            }
        }



        for ( int j = 1; j < 8; j++ ) {
            String val = getValue( xmlSurv, "//org.drools.informer.presentation.QuestionGUIAdapter/itemId[.='"+qid[j]+"']/../currentAnswer" );
            assertEquals( val, values[j].toString() );
        }


        for ( int j = 1; j < 8; j++ ) {
            setSurvey( "drx", "99990070", "123456UNIQUESURVEYID", qid[j], null);
        }

        xmlSurv = getSurvey( "drX", "99990070", "123456UNIQUESURVEYID" );
        for ( int j = 1; j < 8; j++ ) {
            String val = getValue( xmlSurv, "//org.drools.informer.presentation.QuestionGUIAdapter/itemId[.='"+qid[j]+"']/../successType" );
            switch (j) {
                case 4  :
                case 7  : assertEquals( "missing", val ); break;
                default : assertEquals( "invalid", val ); break;
            }
        }


    }





    @Test
    public void testExceedAndReset() {

        List<String> modelsIds = getElements(getRiskModels("docX", "patient33"), "//modelId");
        String modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));


        FactType alertType = mainAgent.getInnerSession("patient33").getKnowledgeBase().getFactType("org.drools.informer.interaction", "Alert");
        Class alertClass = alertType.getFactClass();
        FactType xType = mainAgent.getInnerSession("patient33").getKnowledgeBase().getFactType("org.drools.informer.interaction", "TicketActor");


        Collection alerts = mainAgent.getInnerSession("patient33").getObjects(new ClassObjectFilter(alertClass));
        assertEquals( 0, alerts.size());


        String sid1 = getValue(modelStats, "//modelId[.='MockPTSD']/../surveyId");
        assertNotNull(sid1);

        String ptsdSurvey = getSurvey("docX", "patient33", sid1);

        String gender = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Gender']/../itemId" );
        String deployments = getValue(ptsdSurvey, "//questionName[.='MockPTSD_Deployments']/../itemId");
        String alcohol = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Alcohol']/../itemId" );
        String age = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Age']/../itemId" );

        assertNotNull(gender);
        assertNotNull(deployments );
        assertNotNull( age );

        setSurvey("drX", "patient33", sid1, deployments, "1");
        setSurvey("drX", "patient33", sid1, gender, "female");
        setSurvey( "drX", "patient33", sid1, alcohol, "yes");
        setSurvey("drX", "patient33", sid1, age, "30" );

        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));
        assertEquals("30", getValue(modelStats, "//modelId[.='MockPTSD']/../relativeRisk"));


        setRiskThreshold( "drX", "patient33", "MockPTSD", "Alert", 25 );


        alerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(alertClass) );
        assertEquals( 2, alerts.size() );
        sleep(12000);
        alerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(alertClass) );
        assertEquals( 0, alerts.size() );
        Collection zalerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(xType.getFactClass()) );
        assertEquals( 0, zalerts.size() );




        for ( Object o :  mainAgent.getInnerSession("patient33").getObjects() )
            System.err.println( o );


        setSurvey( "drX", "patient33", sid1, age, "1" );

        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));
        assertEquals( "16", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );


        setSurvey( "drX", "patient33", sid1, age, "40" );

        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));
        assertEquals( "35", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );



        zalerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(xType.getFactClass()) );
        for (Object o : zalerts) {
            System.err.println(o);
        }



        alerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(alertClass) );
        for (Object o : alerts) {
            System.err.println(o);
        }
        assertEquals( 2, alerts.size() );


    }









    @Test
    public void testSetAndResetAndSet() {

        String[] mids = new String[] { "MockPTSD" };

        String modelStats = getRiskModelsDetail("docX", "patient33", mids);

        String sid1 = getValue( modelStats, "//modelId[.='MockPTSD']/../surveyId" );
        assertNotNull( sid1 );

        String ptsdSurvey = getSurvey("docX", "patient33", sid1);

        String gender = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Gender']/../itemId" );
        String deployments = getValue(ptsdSurvey, "//questionName[.='MockPTSD_Deployments']/../itemId");
        String alcohol = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Alcohol']/../itemId" );
        String age = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Age']/../itemId" );

        assertNotNull( gender );
        assertNotNull( deployments );
        assertNotNull( age );

        String set;
        set = setSurvey( "drX", "patient33", sid1, deployments, "1" );
        System.out.println( set );
        set = setSurvey( "drX", "patient33", sid1, gender, "female" );
        System.out.println( set );
        set = setSurvey("drX", "patient33", sid1, alcohol, "yes" );
        System.out.println( set );
        set = setSurvey( "drX", "patient33", sid1, age, "30" );
        System.out.println(set);

        modelStats = getRiskModelsDetail("docX", "patient33", mids);
        assertEquals("30", getValue(modelStats, "//modelId[.='MockPTSD']/../relativeRisk"));
        assertEquals( "Average", getValue( modelStats, "//modelId[.='MockPTSD']/../severity" ) );




        set = setSurvey( "drX", "patient33", sid1, age, null );
        System.out.println(set);
        set = setSurvey( "drX", "patient33", sid1, gender, null );
        System.out.println( set );

        modelStats = getRiskModelsDetail("docX", "patient33", mids);
        assertEquals( "-1", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );
        assertEquals("n/a", getValue(modelStats, "//modelId[.='MockPTSD']/../severity"));



        set = setSurvey( "drX", "patient33", sid1, age, "25" );
        modelStats = getRiskModelsDetail("docX", "patient33", mids);
        assertEquals( "-1", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk"));
        assertEquals( "n/a", getValue( modelStats, "//modelId[.='MockPTSD']/../severity" ) );



        set = setSurvey( "drX", "patient33", sid1, gender, "male" );
        modelStats = getRiskModelsDetail("docX", "patient33", mids);
        assertEquals( "38", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );
        assertEquals( "Average", getValue( modelStats, "//modelId[.='MockPTSD']/../severity" ) );



        set = setSurvey( "drX", "patient33", sid1, age, "30" );
        modelStats = getRiskModelsDetail("docX", "patient33", mids);
        assertEquals( "40", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );
        assertEquals( "Average", getValue( modelStats, "//modelId[.='MockPTSD']/../severity" ) );


    }








    @Test
    public void testGetModels() {

        String diagModels = getModels("drX", "patient33", Arrays.asList("Diagnostic") );

        System.out.println( diagModels );

        List<String> diagList = getModels( diagModels );
        assertEquals( 1, diagList.size() );
        assertTrue( diagList.containsAll( Arrays.asList("MockDiag" ) ) );



        setRiskThreshold( "drX", "patient33", "MockPTSD", "Display", 35 );

        String riskModels = getRiskModels("drX", "patient33");

        System.err.println( riskModels );

        List<String> riskList = getModels( riskModels );
        assertEquals( 3, riskList.size());
        assertTrue( riskList.containsAll( Arrays.asList( "MockPTSD", "MockCold", "MockDiabetes" ) ) );

        assertEquals( "35", getValue( riskModels, "//modelId[.='MockPTSD']/../displayThreshold" ) );


        String enterpriseRiskModels = getModels("drX", "patient33", Arrays.asList("E", "Risk") );

        System.out.println( enterpriseRiskModels );

        List<String> enterpriseRiskModelsList = getModels( enterpriseRiskModels );
        assertEquals(2, enterpriseRiskModelsList.size());
        assertTrue( enterpriseRiskModelsList.containsAll( Arrays.asList( "MockPTSD", "MockCold" ) ) );


    }




    @Test
    @Ignore
    public void testComplexDiagnosticAction() {


        Map<String,Object> args = new LinkedHashMap<String,Object>();

        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Post Traumatic Stress Disorder");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );

        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );


        System.err.println(statusXML);


        String testActionId = getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/actionId" );
        String testQuestId = getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/questionnaireId" );
        assertNotNull( testActionId );
        assertNotNull( testQuestId );

        String stat1 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, testActionId, "Started" );
        stat1 = getValue( stat1, "//actionStatus" );
        assertEquals("Started", stat1);

        String survXML = getSurvey( "drX", "patient33", testQuestId );
        String confirmQid = getValue( survXML, "//questionName[.='confirm']/../itemId" );

        System.err.println(confirmQid);

        setSurvey("drx", "patient33", testQuestId, confirmQid, "true" );
        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Started", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        sleep( 500 );

        setSurvey("drx", "patient33", testQuestId, confirmQid, "false" );
        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "false", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Started", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        sleep( 500 );

        setSurvey("drx", "patient33", testQuestId, confirmQid, "true" );
        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Started", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        sleep( 2500 );


        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Committed", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        sleep( 2500 );

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Complete", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        survXML = getSurvey( "drX", "patient33", testQuestId );
        System.err.println( survXML );

        assertEquals( "disable", getValue( survXML, "//action" ) );


        completeDiagnosticGuideProcess( "docX", "patient33", dxProcessId, "Complete"  );

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "Complete", getValue( statusXML, "//" + testDecModel + "DiagnosticGuideProcess/status" ) );

    }







    @Test
    public void testGetRiskModelsDetail() {

        List<String> modelsIds = getElements(getRiskModels("docX", "patient33"), "//modelId");
        String modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));
        System.err.println(modelStats);

        String sid1 = getValue( modelStats, "//modelId[.='MockPTSD']/../surveyId" );
        String sid2 = getValue( modelStats, "//modelId[.='MockCold']/../surveyId" );

        assertNotNull( sid1 );
        assertNotNull( sid2 );
        assertTrue(sid1.length() > 1);
        assertTrue(sid2.length() > 1);



        System.out.println(sid1 + " .............................. " + sid2);
        String ptsdSurvey = getSurvey( "docX", "patient33", sid1);
        String coldSurvey = getSurvey( "docX", "patient33", sid2);

        System.out.println("@#*" + coldSurvey);

        String gender = getValue(ptsdSurvey, "//questionName[.='MockPTSD_Gender']/../itemId");
        String deployments = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Deployments']/../itemId" );
        String alcohol = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Alcohol']/../itemId" );
        String age = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Age']/../itemId" );

        String temperature = getValue( coldSurvey, "//questionName[.='MockCold_Temp']/../itemId" );

        assertNotNull( gender );
        assertNotNull( deployments );
        assertNotNull( age );
        assertNotNull( temperature);

        setSurvey("drX", "patient33", sid1, deployments, "1");

        setSurvey("drX", "patient33", sid1, gender, "female");
        setSurvey( "drX", "patient33", sid1, alcohol, "yes" );
        setSurvey( "drX", "patient33", sid1, age, "30" );

        setSurvey( "drX", "patient33", sid2, temperature, "39" );

        setRiskThreshold("drX", "patient33", "MockPTSD", "Alert", 35);



        modelStats = getRiskModelsDetail( "docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));


        System.out.println( modelStats );
        assertEquals( "30", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );
        assertEquals( "Average", getValue( modelStats, "//modelId[.='MockPTSD']/../severity" ) );
        assertEquals( "35", getValue( modelStats, "//modelId[.='MockPTSD']/../alertThreshold" ) );
        assertFalse( "Started".equals( getValue( modelStats, "//modelId[.='MockPTSD']/../dxProcessStatus" ) ) );


        assertEquals( "50", getValue( modelStats, "//modelId[.='MockCold']/../alertThreshold" ) );
        assertEquals( "22", getValue( modelStats, "//modelId[.='MockCold']/../relativeRisk" ) );
        assertEquals( "Low", getValue( modelStats, "//modelId[.='MockCold']/../severity" ) );


        setSurvey( "drX", "patient33", sid1, deployments, "null" );
        setSurvey( "drX", "patient33", sid1, gender, "null" );
        setSurvey( "drX", "patient33", sid1, alcohol, "null" );
        setSurvey( "drX", "patient33", sid1, age, "null" );

        setSurvey( "drX", "patient33", sid2, temperature, "null" );
        System.out.println("@#*" + sid2);
    }


    @Test
    public void testClearRiskModelsSurvey() {

        String[] modelsIds = new String[] {"MockCold"};
        String modelStats = getRiskModelsDetail("docX", "patient33", modelsIds);

        String sid2 = getValue( modelStats, "//modelId[.='MockCold']/../surveyId" );


        assertNotNull(sid2);

        String coldSurvey = getSurvey("docX", "patient33", sid2);
        System.out.println("@#*" + coldSurvey);

        String temperature = getValue(coldSurvey, "//questionName[.='MockCold_Temp']/../itemId");
        assertNotNull(temperature);

        for ( int j = 0; j < 5; j++ ) {
            System.out.println("\n");
            System.err.println("\n");
        }
        System.out.println(getSurvey("docX", "patient33", sid2));
        for ( int j = 0; j < 5; j++ ) {
            System.out.println("\n");
            System.err.println("\n");
        }

        setSurvey("drX", "patient33", sid2, temperature, "null");

        setSurvey( "drX", "patient33", sid2, temperature, "39" );


        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds);



        assertEquals("22", getValue(modelStats, "//modelId[.='MockCold']/../relativeRisk"));


        setSurvey( "drX", "patient33", sid2, temperature, "null" );

        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds);
        assertEquals( "-1", getValue( modelStats, "//modelId[.='MockCold']/../relativeRisk" ) );




        setSurvey( "drX", "patient33", sid2, temperature, "35" );
        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds);

        assertEquals( "30", getValue( modelStats, "//modelId[.='MockCold']/../relativeRisk" ) );


    }







    @Test
    public void testSetDiagnostic() {



        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Post Traumatic Stress Disorder");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );
        System.out.println( dxProcessReturn );

        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );

        String actionId = getValue( statusXML, "//" + testDecModel + ".AskAlcohol/actionId" );
        String actionQuestId = getValue( statusXML, "//" + testDecModel + ".AskAlcohol/questionnaireId" );
        assertNotNull( actionId );


        System.err.println(statusXML);

        String stat1 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, actionQuestId, "Started" );
        assertEquals("Started", stat1);


        String stat3 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, actionQuestId, "Complete" );
        assertEquals("Completed", stat3);

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        System.out.println( statusXML );

        completeDiagnosticGuideProcess( "drX", "patient33", dxProcessId, "Complete" );

    }




    @Test
    @Ignore
    public void testDifferentialSetSurvey() {

        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Post Traumatic Stress Disorder");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );

        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );


        System.err.println(statusXML);


        String testActionId = getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/actionId" );
        String testActionQuestId = getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/questionnaireId" );
//        String testQuestId = getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/questionnaireId" );
        assertNotNull( testActionId );
//        assertNotNull( testQuestId );

        String stat1 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, testActionQuestId, "Started" );
        assertEquals("Started", stat1);

        String testQuestId = lookupInBody( statusXML, "DoExcruciatinglyPainfulTest");

        String survXML = getSurvey( "drX", "patient33", testQuestId );
        String confirmQid = getValue( survXML, "//questionName[.='confirm']/../itemId" );


        String set;
        set = setSurvey("drx", "patient33", testQuestId, confirmQid, "invalidAnswerForBoolean" );
        assertEquals( "invalid", getValue( set, "//updatedQuestions//itemId[.='"+ confirmQid+"']/../successType" ) );
        System.err.println(set);

        set = setSurvey("drx", "patient33", testQuestId, confirmQid, "true" );

        System.err.println(set);


        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
//        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );
        assertEquals( "Started", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/status" ) );

        sleep( 2500 );

        set = setSurvey("drx", "patient33", testQuestId, confirmQid, "false" );
        System.out.println( set );
        assertEquals( "disable", getValue( set, "//updatedQuestions//itemId[.='"+ confirmQid+"']/../action" ) );


        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "true", getValue( statusXML, "//" + testDecModel + ".DoExcruciatinglyPainfulTest/confirm" ) );


        completeDiagnosticGuideProcess( "docX", "patient33", dxProcessId, "Complete"  );

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "Complete", getValue( statusXML, "//" + testDecModel + "DiagnosticGuideProcess/status" ) );

    }


    @Test
    public void testExceedRiskThreshold() {

        List<String> modelsIds = getElements(getRiskModels("docX", "patient33"), "//modelId");
        String modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));


        String sid1 = getValue( modelStats, "//modelId[.='MockPTSD']/../surveyId" );
        assertNotNull( sid1 );

        String ptsdSurvey = getSurvey("docX", "patient33", sid1);

        System.out.println(ptsdSurvey);

        String gender = getValue(ptsdSurvey, "//questionName[.='MockPTSD_Gender']/../itemId");
        String deployments = getValue(ptsdSurvey, "//questionName[.='MockPTSD_Deployments']/../itemId" );
        String alcohol = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Alcohol']/../itemId" );
        String age = getValue( ptsdSurvey, "//questionName[.='MockPTSD_Age']/../itemId" );

        assertNotNull( gender );
        assertNotNull( deployments );
        assertNotNull( age );

        setSurvey("drX", "patient33", sid1, deployments, "1");
        setSurvey("drX", "patient33", sid1, gender, "female");
        setSurvey( "drX", "patient33", sid1, alcohol, "yes" );
        setSurvey( "drX", "patient33", sid1, age, "30" );

        modelStats = getRiskModelsDetail("docX", "patient33", modelsIds.toArray(new String[modelsIds.size()]));
        System.out.println( modelStats );
        assertEquals( "30", getValue( modelStats, "//modelId[.='MockPTSD']/../relativeRisk" ) );


        setRiskThreshold( "drX", "patient33", "MockPTSD", "Alert", 05 );



        


        FactType alertType = mainAgent.getInnerSession("patient33").getKnowledgeBase().getFactType("org.drools.informer.interaction", "Alert");
        Class alertClass = alertType.getFactClass();

        Collection alerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(alertClass) );



        String patientAlertSurveyId = null;
        String ackQuestionId = null;
        for ( Object alert : alerts ) {

            String formId = (String) alertType.get( alert, "formId" );
            String dest = (String) alertType.get( alert, "destination" );

            String form = getSurvey( dest, "patient33", formId );
            assertNotNull( form );

            ackQuestionId = getValue( form, "//org.drools.informer.presentation.QuestionGUIAdapter/questionName[.='transition']/../itemId" );

            setSurvey( "patient33", "patient33", formId, ackQuestionId, "COMPLETE" );
        }

        alerts = mainAgent.getInnerSession("patient33").getObjects( new ClassObjectFilter(alertClass) );
        assertEquals( 0, alerts.size() );


        setRiskThreshold( "drX", "patient33", "MockPTSD", "Alert", 50 );

    }





    @Test
    public void testUnroutableMessage() {
        Map<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId", "uid");
        args.put("surveyId","123");

        ACLMessage req = factory.newRequestMessage("me","you", MessageContentFactory.newActionContent("getSurvey", args));
        mainAgent.tell(req);

        for ( Object o : mainAgent.getMind().getObjects() ) {
            System.out.println( "MIND OBEJT " +o );
        }

        List<ACLMessage> resp =  mainAgent.getAgentAnswers(req.getId());
        assertEquals( 1, resp.size() );
        assertEquals( Act.NOT_UNDERSTOOD, resp.get( 0 ).getPerformative() );

    }







    @Test
    public void testDiagnostic() {


        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Post Traumatic Stress Disorder");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );

        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );

//        System.err.println( statusXML );



        String actionId = getValue( statusXML, "//" + testDecModel + ".AskAlcohol/actionId" );
        String actionQuestId = getValue( statusXML, "//" + testDecModel + ".AskAlcohol/questionnaireId" );
        assertNotNull( actionId );

        String stat1 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, actionQuestId, "Started" );

        assertEquals("Started", stat1);



        String actionBodyId = lookupInBody( statusXML, "AskAlcohol");

        String survXML = getSurvey( "drX", "patient33", actionBodyId );
        String alcoholQid = getValue( survXML, "//questionName[.='question']/../itemId" );

        System.err.println( alcoholQid );



        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        assertEquals( "false", getValue( statusXML, "//canAdvance") );



        setSurvey( "drX", "patient33", actionBodyId, alcoholQid, "true" );



        survXML = getSurvey( "drX", "patient33", actionBodyId );


        System.err.println( survXML );



        String stat = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, actionQuestId, "Complete" );
        assertEquals( "Completed", stat );




        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );



        assertEquals( "Completed", getValue( statusXML, "//actionId[.='"+ actionId + "']/../status" ) );
        assertEquals( "Not Started", getValue( statusXML, "//" + testDecModel + ".AskDeployment/status" ) );



        assertEquals( "true", getValue( statusXML, "//canAdvance") );
        assertEquals( "false", getValue( statusXML, "//canCancel") );



        advanceDiagnosticProcessStatus( "drX", "patient33", dxProcessId );





        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );




        assertEquals( "true", getValue( statusXML, "//canAdvance") );


        completeDiagnosticGuideProcess( "rdrX", "patient33", dxProcessId, "Complete" );

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );


        System.err.println(statusXML);
//
        assertEquals( "1", getValue( statusXML, "//gov.hhs.fha.nhinc.kmr2.clinicalAgent.models.decision.DxDecision/diseaseProbability[.='10']/../stage" ) );


    }



    @Test
    public void testProbe() throws InterruptedException {
        System.out.println( probe( "patient33" ) );
    }




    @Test
    public void testProbe2() throws InterruptedException {
        System.err.println( probe( "123456" ) );
    }






















    @Test
    public void testEmptyDiagnostic() {

        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Uncommon Cold");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );
        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );

        System.err.println( statusXML );

    }



    @Test
    public void testNonExistingDiagnostic() {

        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Imaginary Disease" );
        assertTrue( dxProcessReturn.startsWith( "FAILURE:" ) );
        System.err.println( dxProcessReturn );

    }













    @Test
    @Ignore
    public void testAutoCommittActions() {


        Map<String,Object> args = new LinkedHashMap<String,Object>();

        String dxProcessReturn = startDiagnosticGuideProcess( "docX", "patient33", "Post Traumatic Stress Disorder");
        String dxProcessId = getValue( dxProcessReturn, "//dxProcessId" );


        assertNotNull( dxProcessId );

        String statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );




        String actionId = getValue( statusXML, "//" + testDecModel + ".AskSomething1/actionId" );
        String actionQuestId = getValue( statusXML, "//" + testDecModel + ".AskSomething1/questionnaireId" );
        assertNotNull( actionId );
        assertNotNull( actionQuestId );


        String stat1 = setDiagnosticActionStatus( "drX", "patient33", dxProcessId, actionId, "Started" );
        stat1 = getValue( stat1, "//actionStatus" );
        assertEquals("Started", stat1);

        String survXML = getSurvey( "drX", "patient33", actionQuestId );
        String confirmQid = getValue( survXML, "//questionName[.='question']/../itemId" );
        assertNotNull( confirmQid );

        setSurvey( "drX", "patient33", actionQuestId, confirmQid, "true" );

        survXML = getSurvey( "drX", "patient33", actionQuestId );

        statusXML = getDiagnosticProcessStatus( "drX", "patient33", dxProcessId, true );
        System.out.println(statusXML);

        assertEquals( "true", getValue( statusXML, "//actionId[.='"+ actionId + "']/../question" ) );
        assertEquals( "Complete", getValue( statusXML, "//actionId[.='"+ actionId + "']/../status" ) );
        assertEquals( "Not Started", getValue( statusXML, "//" + testDecModel + ".AskDeployment/status" ) );


    }



}






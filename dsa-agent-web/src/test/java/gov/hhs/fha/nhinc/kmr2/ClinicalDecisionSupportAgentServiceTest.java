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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.kmr2;

//import org.drools.dssagentserver.helpers.SynchronousRequestHelper;
import org.drools.mas.helpers.SynchronousRequestHelper;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ClinicalDecisionSupportAgentServiceTest {

    private static SynchronousRequestHelper helper;


    @BeforeClass
    public static void setUpClass() throws Exception {

//        helper = new SynchronousRequestHelper( "http://localhost:9944/clinical-decision-support-agent/service/SynchronousDroolsAgentService?wsdl" );
        helper = new SynchronousRequestHelper( "http://184.191.173.235:8080/clinical-decision-support-agent/service/SynchronousDroolsAgentService?wsdl" );


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
    public void testGetSurvey() {

        if ( !helper.isInitialized() ) {
            return;
        }



        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","drX");
        args.put("patientId","99990070");
        args.put("surveyId","123456UNIQUESURVEYID");
//        args.put("surveyId","a69670fb-a9e6-4244-bb13-71dad7824d34");

        helper.invokeRequest("getSurvey", args);

        String rs = (String) helper.getReturn( false );

        try {
            Document dox = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream( rs.getBytes() ) );
            assertEquals( "org.drools.informer.presentation.SurveyGUIAdapter", dox.getDocumentElement().getNodeName() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }
    }


//    @Test
//    public void testGetDx() {
//
//        if ( !helper.isInitialized() ) {
//            return;
//        }
//
//        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
//        args.put("userId","drX");
//        args.put("patientId","99990070");
//        args.put("dxProcessId","c1252f36-a582-41b1-8c03-51a6e2c6cd7c");
//
//        args.put("forceRefresh", true);
//        helper.invokeRequest("getDiagnosticProcessStatus", args);
//
//        String rs = (String) helper.getReturn( false );
//
//        System.out.println(rs);
//    }




    @Test
    public void testProbe() {

        if ( !helper.isInitialized() ) {
            return;
        }

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("patientId","99990070");
        helper.invokeRequest("probe", args);

        String rs = (String) helper.getReturn( false );
        System.out.println(rs);

    }

    @Test
    public void testSetSurvey() {
        String[] qid = new String[8];
        String[] values = new String[8];
        XPath finder = XPathFactory.newInstance().newXPath();


        if ( !helper.isInitialized() ) {
            return;
        }

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","patient1");
        args.put("patientId","99990070");
        args.put("surveyId","123456UNIQUESURVEYID");

        helper.invokeRequest("getSurvey", args);


        String rs = (String) helper.getReturn( false );
        System.out.println(rs);
        try {
            Document dox = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(rs.getBytes()) );
            assertEquals( "org.drools.informer.presentation.SurveyGUIAdapter", dox.getDocumentElement().getNodeName() );

            for ( int j = 1; j < 8; j++ ) {
                String xpath = "//org.drools.informer.presentation.QuestionGUIAdapter/questionName[.='question"+j+"']/../itemId";
                qid[j] = (String) finder.evaluate(xpath, dox, XPathConstants.STRING);
                System.out.println("ID OF Q" + j + " >> " + qid[j] );
            }

        } catch (Exception e) {
            fail( e.getMessage() );
        }


        values[1] = new Integer((int) Math.ceil( 6 * Math.random() )).toString();
        values[2] = Math.random() > 0.5 ? "Urban" : "Rural";
        List v3 = new ArrayList();
        if ( Math.random() > 0.5 ) { v3.add( "CMP" ); }
        if ( Math.random() > 0.5 ) { v3.add( "FSH" ); }
        if ( Math.random() > 0.5 ) { v3.add( "GLF" ); }
        if ( Math.random() > 0.5 ) { v3.add( "HGL" ); }
        values[3] = v3.toString().replace("[","").replace("]","").replace(" ","");
        values[4] = new Integer((int) Math.round( 100 * Math.random() )).toString();
        values[5] = UUID.randomUUID().toString();
        values[6] = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        values[7] = new Integer((int) Math.round( 100 * Math.random() )).toString();



        for ( int j = 1; j < 8; j++ ) {
            LinkedHashMap<String,Object> argsx = new LinkedHashMap<String,Object>();
            argsx.put("userId","drx");
            argsx.put("patientId","99990070");
            argsx.put("surveyId","123456UNIQUESURVEYID");
            argsx.put("questionId",qid[j]);
            argsx.put("answer",values[j].toString());

            System.out.println( "Setting " +j + " to " + values[j]);

            helper.invokeRequest("setSurvey", argsx);
        }






        LinkedHashMap<String,Object> args99 = new LinkedHashMap<String,Object>();
        args99.put("userId","patient1");
        args99.put("patientId","99990070");
        args99.put("surveyId","123456UNIQUESURVEYID");

        helper.invokeRequest("getSurvey", args99);

        String rs99 = (String) helper.getReturn( false );

        System.out.println(rs99);

        try {
            Document dox99 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(rs99.getBytes()) );
            assertEquals( "org.drools.informer.presentation.SurveyGUIAdapter", dox99.getDocumentElement().getNodeName() );

            for ( int j = 1; j < 8; j++ ) {
                String xpath = "//org.drools.informer.presentation.QuestionGUIAdapter/itemId[.='"+qid[j]+"']/../currentAnswer";
                String val = (String) finder.evaluate(xpath, dox99, XPathConstants.STRING);
                assertEquals( val, values[j].toString() );
            }
        } catch (Exception e) {
            fail( e.getMessage() );
        }



    }





    @Test
    public void testGetRiskModelsDetail() {
        XPath finder = XPathFactory.newInstance().newXPath();


        if ( !helper.isInitialized() ) {
            return;
        }

        String mid = "MockPTSD";

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","docX");
        args.put("patientId","99990070");
        args.put("modelIds",new String[] {mid} );


        helper.invokeRequest("getRiskModelsDetail", args);
        String xml = (String) helper.getReturn(false);

        System.out.println(xml);


        String sid = null;
        try {
            Document risk = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(xml.getBytes()) );
            assertEquals( "org.kmr2.risk.GetRiskModelsDetail", risk.getDocumentElement().getNodeName() );

            String xpath = "//riskModels/org.kmr2.risk.RiskModelDetail/modelId[.='"+mid+"']/../surveyId";
            sid = (String) finder.evaluate(xpath, risk, XPathConstants.STRING);

        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }



        args.clear();
        args.put("userId","drX");
        args.put("patientId","99990070");
        args.put("surveyId",sid);

        helper.invokeRequest("getSurvey", args);

        String sxml = (String) helper.getReturn(false);

        System.out.println(sxml);

        try {
            Document surv = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(sxml.getBytes()) );
            assertEquals( "org.drools.informer.presentation.SurveyGUIAdapter", surv.getDocumentElement().getNodeName() );
        } catch (Exception e) {
            fail( e.getMessage() );
        }


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
    public void testForceAlert() {
        XPath finder = XPathFactory.newInstance().newXPath();


        if ( !helper.isInitialized() ) {
            return;
        }

        String mid = "MockCold";

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("modelIds",new String[] {mid} );


        helper.invokeRequest("getRiskModelsDetail", args);
        String xml = (String) helper.getReturn(false);


        String sid = null;
        try {
            Document risk = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(xml.getBytes()) );
            assertEquals( "org.kmr2.risk.GetRiskModelsDetail", risk.getDocumentElement().getNodeName() );

            String xpath = "//riskModels/org.kmr2.risk.RiskModelDetail/modelId[.='"+mid+"']/../surveyId";
            sid = (String) finder.evaluate(xpath, risk, XPathConstants.STRING);

        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }



        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("surveyId",sid);
        helper.invokeRequest("getSurvey", args);

        String sxml = (String) helper.getReturn(false);


        String tempId = getValue( sxml, "//questionName[.='MockCold_Temp']/../itemId" );


        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("surveyId",sid);
        args.put("questionId", tempId );
        args.put("value", "2");
        helper.invokeRequest("setSurvey", args);




        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("modelIds",new String[] {mid} );
        helper.invokeRequest("getRiskModelsDetail", args);
        xml = (String) helper.getReturn(false);

        System.out.println(xml);

    }



    @Test
    public void testClearAlert() {
        XPath finder = XPathFactory.newInstance().newXPath();
        String mid = "MockCold";

        if ( !helper.isInitialized() ) {
            return;
        }



        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("modelIds",new String[] {mid} );


        helper.invokeRequest("getRiskModelsDetail", args);
        String xml = (String) helper.getReturn(false);

        System.out.println(xml);


        String sid = null;
        try {
            Document risk = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(xml.getBytes()) );
            assertEquals( "org.kmr2.risk.GetRiskModelsDetail", risk.getDocumentElement().getNodeName() );

            String xpath = "//riskModels/org.kmr2.risk.RiskModelDetail/modelId[.='"+mid+"']/../surveyId";
            sid = (String) finder.evaluate(xpath, risk, XPathConstants.STRING);

        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }



        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("surveyId",sid);
        helper.invokeRequest("getSurvey", args);

        String sxml = (String) helper.getReturn(false);

        System.out.println(sxml);

        String tempId = getValue( sxml, "//questionName[.='MockCold_Temp']/../itemId" );


        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("surveyId",sid);
        args.put("questionId", tempId );
        args.put("value", "60" );
        helper.invokeRequest("setSurvey", args);


        args.clear();
        args.put("userId","1");
        args.put("patientId","99990070");
        args.put("modelIds",new String[] {mid} );
        helper.invokeRequest("getRiskModelsDetail", args);
        xml = (String) helper.getReturn(false);

        System.out.println(xml);
    }





    @Test
    public void testSetRiskThreshold() {
        XPath finder = XPathFactory.newInstance().newXPath();
        String mid = "MockPTSD";

        if ( !helper.isInitialized() ) {
            return;
        }



        Integer value = (int) Math.round(Math.random()*100);

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.clear();
        args.put("userId","drX");
        args.put("patientId","patient33");
        args.put("modelId",mid);
        args.put("type","Alert");
        args.put("threshold",value);
        helper.invokeRequest("setRiskThreshold", args);




        args.clear();
        args.put("userId","docX");
        args.put("patientId","patient33");
        args.put("modelIds",new String[] {mid} );

        helper.invokeRequest("getRiskModelsDetail", args);
        String xml = (String) helper.getReturn(false);

        System.out.println(xml);


        String thold = null;
        try {
            Document risk = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( new ByteArrayInputStream(xml.getBytes()) );
            assertEquals( "org.kmr2.risk.GetRiskModelsDetail", risk.getDocumentElement().getNodeName() );

            String xpath = "//riskModels/org.kmr2.risk.RiskModelDetail/modelId[.='"+mid+"']/../alertThreshold";
            thold = (String) finder.evaluate(xpath, risk, XPathConstants.STRING);
            assertEquals(thold, ""+value);
        } catch (Exception e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }



        args.clear();
        args.put("userId","drX");
        args.put("patientId","patient33");
        args.put("modelId",mid);
        args.put("type","Alert");
        args.put("threshold",50);
        helper.invokeRequest("setRiskThreshold", args);


    }






    @Test
    public void testStartDxGuide() {
        XPath finder = XPathFactory.newInstance().newXPath();

        String mid = "MockPTSD";
        if ( !helper.isInitialized() ) {
            return;
        }



        String value = ""+((int) Math.round(Math.random()*100));

        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.clear();
        args.put("userId", "docX");
        args.put("patientId", "patient33" );
        args.put("decModelId","MockDecision");
        args.put("diagModelId","MockDiag");
        helper.invokeRequest("startDiagnosticGuideProcess", args);

        System.out.println(helper.getReturn(false));


    }


    @Test
    public void testGetDxGuide() {
        XPath finder = XPathFactory.newInstance().newXPath();

        if ( !helper.isInitialized() ) {
            return;
        }


        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.clear();
        args.put("userId", "docX");
        args.put("patientId", "patient33" );
        args.put("dxProcessId","80e2d323-5bb7-4753-9e71-4465d9a205ff");
        args.put("refresh",true);
        helper.invokeRequest("getDiagnosticProcessStatus", args);

        System.out.println(helper.getReturn(false));


    }


    @Test
    public void testGetRiskModels() {
        LinkedHashMap<String,Object> args = new LinkedHashMap<String,Object>();
        args.put("userId","drX");
        args.put("patientId","99990070");

        if ( !helper.isInitialized() ) {
            return;
        }

        helper.invokeRequest("getRiskModels", args);

        System.out.println(helper.getReturn(false));

    }












}

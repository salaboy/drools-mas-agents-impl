package gov.hhs.fha.nhinc.kmr2;


import org.drools.*;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.AssertBehaviorOption;
import org.drools.conf.EventProcessingOption;
import org.drools.io.impl.ClassPathResource;
import org.drools.mas.ACLMessage;
import org.drools.mas.body.content.Action;
import org.drools.mas.util.MessageContentFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class TestDxGuideLogic {
    
    private static boolean verbose = true;

    private static KnowledgeBase kBase;
    
    @BeforeClass()
    public static void setUp() {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add( new ClassPathResource( "org/drools/informer/informer-changeset.xml" ), ResourceType.CHANGE_SET );
        kBuilder.add( new ClassPathResource( "org/drools/informer/interaction/interaction-changeset.xml"), ResourceType.CHANGE_SET );

        kBuilder.add( new ClassPathResource( "org/drools/mas/acl_subsession_def_changeset.xml"), ResourceType.CHANGE_SET );
        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/default/subsession_default.drl" ), ResourceType.DRL );

        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/test/diag_model_list.drl" ), ResourceType.DRL );

        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/declares/dxGuide_declares.drl" ), ResourceType.DRL );
        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/logic/diagnosticGuide.drl" ), ResourceType.DRL );
        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/mock/mock_ptsd_decision.drl" ), ResourceType.DRL );

        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/mock/mock_ptsd_decision.drl" ), ResourceType.DRL );

        kBuilder.add( new ClassPathResource( "gov/hhs/fha/nhinc/kmr2/clinicalAgent/services/startDiagnosticGuideProcess.drl" ), ResourceType.DRL );


        if ( kBuilder.hasErrors() ) {
            fail( kBuilder.getErrors().toString() );
        }

        KnowledgeBaseConfiguration rbconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        rbconf.setOption(EventProcessingOption.STREAM);
        rbconf.setOption(AssertBehaviorOption.EQUALITY);
        kBase = KnowledgeBaseFactory.newKnowledgeBase(rbconf);

        kBase.addKnowledgePackages( kBuilder.getKnowledgePackages() );

    }

    public StatefulKnowledgeSession getKSession() {
        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setProperty(ClockTypeOption.PROPERTY_NAME, ClockType.REALTIME_CLOCK.toExternalForm());
        return kBase.newStatefulKnowledgeSession( conf, null );
    }




    public void startDiagnosticGuideProcess( String userId, String patientId, String disease, StatefulKnowledgeSession ks ) {
        Map<String,Object> args = new LinkedHashMap<String,Object>();

        args.put("userId", userId );
        args.put("patientId", patientId );
        args.put("disease", disease );

        Action action = MessageContentFactory.newActionContent("startDiagnosticGuideProcess", args);

        ks.insert( action );
        ks.fireAllRules();
    }

    private void report(StatefulKnowledgeSession kSession) {
        if ( ! verbose ) { return; }
        System.err.println( "------------------------------" + kSession.getObjects().size() + "------------------------------" );
        for ( Object o : kSession.getObjects() ) {
            System.err.println( "\t" + o );
        }
        System.err.println( "------------------------------" + kSession.getObjects().size() + "------------------------------" );
    }

    @Test
    public void testStart() {
        StatefulKnowledgeSession kSession = getKSession();

        startDiagnosticGuideProcess( "drX", "patient33", "Post Traumatic Stress Disorder", kSession );
        
        report( kSession );
    }



}

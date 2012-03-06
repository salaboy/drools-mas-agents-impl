import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestDRL {

    @Test
    @Ignore
    public void testDRLTraits() {
        KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kBuilder.add( new ClassPathResource( "kmr2_trait.drl" ), ResourceType.DRL );
        if ( kBuilder.hasErrors() ) {
            fail(kBuilder.getErrors().toString());
        }

        KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addKnowledgePackages( kBuilder.getKnowledgePackages() );


    }
}

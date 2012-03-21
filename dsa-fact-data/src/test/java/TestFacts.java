import junit.framework.Assert;
import org.drools.io.impl.ClassPathResource;
import org.junit.Test;
import org.xml.sax.SAXException;
import urn.gov.hhs.fha.nhinc.adapter.fact.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TestFacts {

    
    @Test
    public void testFacts() {
        
        Map<String, Object> map = IndividualFactory.getNamedIndividuals();
        
        System.out.println( map.keySet() );
        
        org.junit.Assert.assertEquals( 12, map.size() );

        assertTrue( map.get( "Jane_Doe" ) instanceof Patient );

        assertTrue( map.get( "Jane_Doe_Diagnosis_1" ) instanceof Diagnosis );

        assertSame( map.get( "Jane_Doe" ), ((Diagnosis) map.get( "Jane_Doe_Diagnosis_1" )).getHasPatientPatient() );
    }

}

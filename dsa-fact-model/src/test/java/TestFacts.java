import junit.framework.Assert;
import org.drools.io.impl.ClassPathResource;
import org.junit.Ignore;
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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestFacts {

    @Test
    public void testVitals() throws JAXBException {

        ConceptPointer cp = new ConceptPointerImpl();
        cp.addConceptCode( "code" );
        cp.addConceptLabel( "label" );
        cp.addContextSystemCode( "vocabCode" );
        cp.addContextSystemLabel( "vocabLabel" );

        Quantitative vp = new QuantitativeImpl();
        vp.addUnit( "mu" );
        vp.addVal( 0.43f );

        VitalSign sign = new VitalSignImpl();
        sign.addDateTimeCreated( new Date() );
        sign.addDateTimeReported( new Date() );
        sign.addDateTimeUpdated( new Date() );
        sign.addHasAdminStatus( cp );
        sign.addHasClinicalStatus( cp );
        sign.addHasComment( "test" );
        sign.addHasDataSource( cp );
        sign.addHasEffectiveDates( new TimeIntervalImpl(  ) );
        sign.addHasVitalHighRefRange( vp );
        sign.addHasVitalLowRefRange( vp );
        sign.addHasVitalSignInterpretation( cp );
        sign.addHasVitalSignResult( cp );


        ObjectFactory factory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(factory.getClass().getPackage().getName());
        Marshaller marshaller;
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );


        StringWriter writer = new StringWriter();

        marshaller.marshal(sign, writer);

        System.out.println( writer.toString() );


        Unmarshaller unmarshal = jaxbContext.createUnmarshaller();

        Object k = unmarshal.unmarshal( new StringReader( writer.toString() ) );
        System.out.println( k );
    }



    String test = "<PatientImpl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\">\n" +
            "    <dyEntryType>Patient</dyEntryType>\n" +
            "    <dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "    <dyReference>false</dyReference>\n" +
            "    <dateTimeCreated>Thu, 09 Feb 2012 09:57:31.940 -0800</dateTimeCreated>\n" +
            "    <patientIEN>2-1</patientIEN>\n" +
            "    <patientID>???</patientID>\n" +
            "    <personID>patient2-1</personID>\n" +
            "    <hasLegalName>\n" +
            "    <dyEntryType>Name</dyEntryType>\n" +
            "    <dyEntryId>bed11530-d6c4-4cc8-8c26-ddb5a83cdda1</dyEntryId>\n" +
            "    <dyReference>false</dyReference>\n" +
            "    <familyName>Jones</familyName>\n" +
            "    <firstName>Fred</firstName>\n" +
            "    </hasLegalName>\n" +
            "    </PatientImpl>";

    @Test
    public void testFromXML() throws JAXBException {


        ObjectFactory factory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(factory.getClass().getPackage().getName());

        Unmarshaller unmarshal = jaxbContext.createUnmarshaller();

        Patient pat = (Patient) unmarshal.unmarshal( new StringReader( test ) );

        assertEquals( "Fred", pat.getHasLegalNameName().getFirstNameString() );
        assertEquals( "Jones", pat.getHasLegalNameName().getFamilyNameString() );
    }





    String testList = "<FactListImpl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\">\n" +
            "    <dyEntryType>FactList</dyEntryType>" +
            "    <dyReference>false</dyReference>\n" +
            "    <dyEntryId>listxxx</dyEntryId>\n" +
            "    <contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "            <dyEntryType>VitalSign</dyEntryType>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/120_5-1</dyEntryId>\n" +
            "            <hasPatient xsi:type=\"Patient\" >\n" +
            "                <dyEntryType>Patient</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "                <dateTimeCreated>2002-05-30T09:00:00</dateTimeCreated>\n" +
            "                <hasDataSource>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>b0047ff5-10b7-40ee-85e9-449b99b8fbdc</dyEntryId>\n" +
            "                    <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "                    <conceptCode>danno</conceptCode>\n" +
            "                </hasDataSource>\n" +
            "                <hasPatientID>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>6b94606e-2f00-4dda-94a3-5c79ca984a1c</dyEntryId>\n" +
            "                    <conceptLabel>Patient Fred Jone</conceptLabel>\n" +
            "                    <conceptCode>http://patients.kmr.org/danno/2-1</conceptCode>\n" +
            "                </hasPatientID>\n" +
            "                <hasLegalName>\n" +
            "                    <dyEntryType>Name</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>f2af792f-8a44-4f74-9062-3cd0e7b512d6</dyEntryId>\n" +
            "                    <familyName>Jones</familyName>\n" +
            "                    <firstName>Fred</firstName>\n" +
            "                </hasLegalName>\n" +
            "            </hasPatient>\n" +
            "            <performedBy>\n" +
            "                <dyEntryType>Agent</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>http://patients.kmr.org/danno/200-1</dyEntryId>\n" +
            "                <hasDataSource>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>f4be872b-711e-426b-a998-329379a46bfe</dyEntryId>\n" +
            "                    <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "                    <conceptCode>danno</conceptCode>\n" +
            "                </hasDataSource>\n" +
            "                <hasLegalName>\n" +
            "                    <dyEntryType>Name</dyEntryType>\n" +
            "                    <dyEntryId>aae555e4-a563-4132-89e3-20e82ff19b54</dyEntryId>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <familyName>Smith</familyName>\n" +
            "                    <firstName>Joe</firstName>\n" +
            "                </hasLegalName>\n" +
            "                <hasAgentID>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>e274565d-268e-43c8-a167-19cdec1f39bb</dyEntryId>\n" +
            "                    <conceptLabel>Agent Joe Smith</conceptLabel>\n" +
            "                    <conceptCode>http://patients.kmr.org/danno/200-1</conceptCode>\n" +
            "                </hasAgentID>\n" +
            "            </performedBy>\n" +
            "            <dateTimeCreated>2002-05-30T09:00:00</dateTimeCreated>\n" +
            "            <dateTimeReported>2002-05-30T09:00:00</dateTimeReported>\n" +
            "            <hasDataSource>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>2978352a-964e-44d9-9c0f-338e5baff972</dyEntryId>\n" +
            "                <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "                <conceptCode>danno</conceptCode>\n" +
            "            </hasDataSource>\n" +
            "            <hasTypeReference>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>63040897-f300-4037-8137-54b63396d087</dyEntryId>\n" +
            "                <conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "                <conceptCode>386725007</conceptCode>\n" +
            "            </hasTypeReference>\n" +
            "            <hasVitalSignResult>\n" +
            "                <dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>887c651c-afb5-4eed-865e-33a5ccf04981</dyEntryId>\n" +
            "                <val>98.6</val>\n" +
            "                <unit>degrees</unit>\n" +
            "            </hasVitalSignResult>\n" +
            "        </contains>\n" +
            "        <contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "            <dyEntryType>VitalSign</dyEntryType>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/120_5-2</dyEntryId>\n" +
            "            <hasPatient>\n" +
            "                <dyEntryType>Patient</dyEntryType>\n" +
            "                <dyReference>true</dyReference>\n" +
            "                <dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "            </hasPatient>\n" +
            "            <performedBy>\n" +
            "                <dyEntryType>Agent</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>http://patients.kmr.org/danno/200-2</dyEntryId>\n" +
            "                <hasDataSource>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>d10be252-8b0d-4126-81c5-c6de8d56885b</dyEntryId>\n" +
            "                    <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "                    <conceptCode>danno</conceptCode>\n" +
            "                </hasDataSource>\n" +
            "                <hasLegalName>\n" +
            "                    <dyEntryType>Name</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>87bb1584-503f-4aca-88d6-47e6b67dd904</dyEntryId>\n" +
            "                    <familyName>Kid</familyName>\n" +
            "                    <firstName>Bill</firstName>\n" +
            "                </hasLegalName>\n" +
            "                <hasAgentID>\n" +
            "                    <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                    <dyReference>false</dyReference>\n" +
            "                    <dyEntryId>2a38ae73-f107-4d71-a886-68ebed7e7b2f</dyEntryId>\n" +
            "                    <conceptLabel>Agent Bill Kid</conceptLabel>\n" +
            "                    <conceptCode>http://patients.kmr.org/danno/200-2</conceptCode>\n" +
            "                </hasAgentID>\n" +
            "            </performedBy>\n" +
            "            <dateTimeCreated>2002-05-30T09:00:00</dateTimeCreated>\n" +
            "            <dateTimeReported>2002-05-30T09:00:00</dateTimeReported>\n" +
            "            <hasDataSource>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>95773c47-08d7-40df-93a6-25474f43f840</dyEntryId>\n" +
            "                <conceptCode>danno</conceptCode>\n" +
            "                <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "            </hasDataSource>\n" +
            "            <hasTypeReference>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>880e18a4-be38-4513-9be3-e6f75fd07bf0</dyEntryId>\n" +
            "                <conceptCode>386725007</conceptCode>\n" +
            "                <conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "            </hasTypeReference>\n" +
            "            <hasTypeReference>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>afb89ae8-4ec5-4c15-9a76-b62be8790d21</dyEntryId>\n" +
            "                <conceptCode>4500638</conceptCode>\n" +
            "                <conceptLabel>VA BODY TEMPERATURE</conceptLabel>\n" +
            "            </hasTypeReference>\n" +
            "            <hasVitalSignResult>\n" +
            "                <dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <dyEntryId>3049b6b8-8291-4c23-84d7-eff28dbf987a</dyEntryId>\n" +
            "                <val>99.6</val>\n" +
            "                <unit>degrees</unit>\n" +
            "            </hasVitalSignResult>\n" +
            "    </contains>\n" +
            "</FactListImpl>";


    @Test
    @Ignore("XML Source is oudated")
    public void testFromXMLFactList() throws JAXBException {

        ObjectFactory factory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(factory.getClass().getPackage().getName());

        Unmarshaller unmarshal = jaxbContext.createUnmarshaller();

        String inXSD = "kmr2_$impl.xsd";
        String xml = testList;
        System.out.println( xml );

        SchemaFactory xfactory =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");


        Source schemaFile = null;
        try {
            schemaFile = new StreamSource( new ClassPathResource( inXSD ).getInputStream() );
        } catch ( IOException e ) {
            fail( e.getMessage() );
        }
        Schema schema = null;
        try {
            schema = xfactory.newSchema( schemaFile );
        } catch (SAXException e) {
            Assert.fail( e.getMessage() );
        }

        Validator validator = schema.newValidator();

        Source source = new StreamSource( new ByteArrayInputStream( xml.getBytes() ) );

        try {
            validator.validate(source);
        }
        catch ( SAXException ex ) {
            fail( ex.getMessage() );
        } catch ( IOException ex ) {
            fail( ex.getMessage() );
        }


    }



    @Test
    public void testToXMLFactList() throws JAXBException {

        FactList list = new FactListImpl();
        VitalSign vs1 = new VitalSignImpl();
        vs1.addDateTimeCreated( new Date() );
        VitalSign vs2 = new VitalSignImpl();
        list.addContains( vs1 );
        list.addContains( vs2 );


        ObjectFactory factory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(factory.getClass().getPackage().getName());
        Marshaller marshaller;
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );



        StringWriter writer = new StringWriter();

        marshaller.marshal(list, writer);

        System.out.println( writer.toString() );

        Unmarshaller unmarshal = jaxbContext.createUnmarshaller();

        Object k = unmarshal.unmarshal( new StringReader( writer.toString() ) );
        System.out.println( k );
    }

}

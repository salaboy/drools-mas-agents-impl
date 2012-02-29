import org.junit.Test;
import urn.gov.hhs.fha.nhinc.adapter.fact.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class TestFacts {

    @Test
    public void testVitals() throws JAXBException {

        ConceptPointer cp = new ConceptPointerImpl();
        cp.addConceptCode( "code" );
        cp.addConceptLabel( "label" );
        cp.addContextSystemCode( "vocabCode" );
        cp.addContextSystemLabel( "vocabLabel" );

        ValueUnitPair vp = new ValueUnitPairImpl();
        vp.addUnit( "mu" );
        vp.addVal( "val" );

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
            "\t<contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "\t\t\t<dyEntryType>VitalSign</dyEntryType>\n" +
            "\t\t\t<dyEntryId>http://patients.kmr.org/danno/120_5-1</dyEntryId>\n" +
            "\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t<hasPatient xsi:type=\"Patient\" >\n" +
            "\t\t\t\t<dyEntryType>Patient</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<dateTimeCreated>2003-01-10 T 00:00:00 Z</dateTimeCreated>\n" +
            "\t\t\t\t<hasDataSource>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>b0047ff5-10b7-40ee-85e9-449b99b8fbdc</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>danno</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>DANNO VistA</conceptLabel>\n" +
            "\t\t\t\t</hasDataSource>\n" +
            "\t\t\t\t<hasPatientID>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>6b94606e-2f00-4dda-94a3-5c79ca984a1c</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>http://patients.kmr.org/danno/2-1</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>Patient Fred Jone</conceptLabel>\n" +
            "\t\t\t\t</hasPatientID>\n" +
            "\t\t\t\t<hasLegalName>\n" +
            "\t\t\t\t\t<dyEntryType>Name</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>f2af792f-8a44-4f74-9062-3cd0e7b512d6</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<familyName>Jones</familyName>\n" +
            "\t\t\t\t\t<firstName>Fred</firstName>\n" +
            "\t\t\t\t</hasLegalName>\n" +
            "\t\t\t</hasPatient>\n" +
            "\t\t\t<performedBy>\n" +
            "\t\t\t\t<dyEntryType>Agent</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>http://patients.kmr.org/danno/200-1</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<hasDataSource>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>f4be872b-711e-426b-a998-329379a46bfe</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>danno</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>DANNO VistA</conceptLabel>\n" +
            "\t\t\t\t</hasDataSource>\n" +
            "\t\t\t\t<hasLegalName>\n" +
            "\t\t\t\t\t<dyEntryType>Name</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>aae555e4-a563-4132-89e3-20e82ff19b54</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<familyName>Smith</familyName>\n" +
            "\t\t\t\t\t<firstName>Joe</firstName>\n" +
            "\t\t\t\t</hasLegalName>\n" +
            "\t\t\t\t<hasAgentID>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>e274565d-268e-43c8-a167-19cdec1f39bb</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>http://patients.kmr.org/danno/200-1</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>Agent Joe Smith</conceptLabel>\n" +
            "\t\t\t\t</hasAgentID>\n" +
            "\t\t\t</performedBy>\n" +
            "\t\t\t<dateTimeCreated>2004-03-30T21:31:00Z</dateTimeCreated>\n" +
            "\t\t\t<dateTimeReported>2004-03-30T21:31:00Z</dateTimeReported>\n" +
            "\t\t\t<hasDataSource>\n" +
            "\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>2978352a-964e-44d9-9c0f-338e5baff972</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t<conceptCode>danno</conceptCode>\n" +
            "\t\t\t\t<conceptLabel>DANNO VistA</conceptLabel>\n" +
            "\t\t\t</hasDataSource>\n" +
            "\t\t\t<hasTypeReference>\n" +
            "\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>63040897-f300-4037-8137-54b63396d087</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<terminologySystemLabel>SNOMED-CT</terminologySystemLabel>\n" +
            "\t\t\t\t<terminologySystemCode>2.16.840.1.113883.6.96</terminologySystemCode>\n" +
            "\t\t\t\t<conceptCode>386725007</conceptCode>\n" +
            "\t\t\t\t<conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "\t\t\t</hasTypeReference>\n" +
            "\t\t\t<hasVitalSignResult>\n" +
            "\t\t\t\t<dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>887c651c-afb5-4eed-865e-33a5ccf04981</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<val>98.6</val>\n" +
            "\t\t\t\t<unit>degrees</unit>\n" +
            "\t\t\t</hasVitalSignResult>\n" +
            "\t\t</contains>\n" +
            "\t\t<contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "\t\t\t<dyEntryType>VitalSign</dyEntryType>\n" +
            "\t\t\t<dyEntryId>http://patients.kmr.org/danno/120_5-2</dyEntryId>\n" +
            "\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t<hasPatient>\n" +
            "\t\t\t\t<dyEntryType>Patient</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "\t\t\t\t<dyReference>true</dyReference>\n" +
            "\t\t\t</hasPatient>\n" +
            "\t\t\t<performedBy>\n" +
            "\t\t\t\t<dyEntryType>Agent</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>http://patients.kmr.org/danno/200-2</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<hasDataSource>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>d10be252-8b0d-4126-81c5-c6de8d56885b</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>danno</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>DANNO VistA</conceptLabel>\n" +
            "\t\t\t\t</hasDataSource>\n" +
            "\t\t\t\t<hasLegalName>\n" +
            "\t\t\t\t\t<dyEntryType>Name</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>87bb1584-503f-4aca-88d6-47e6b67dd904</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<familyName>Kid</familyName>\n" +
            "\t\t\t\t\t<firstName>Bill</firstName>\n" +
            "\t\t\t\t</hasLegalName>\n" +
            "\t\t\t\t<hasAgentID>\n" +
            "\t\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t\t<dyEntryId>2a38ae73-f107-4d71-a886-68ebed7e7b2f</dyEntryId>\n" +
            "\t\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t\t<conceptCode>http://patients.kmr.org/danno/200-2</conceptCode>\n" +
            "\t\t\t\t\t<conceptLabel>Agent Bill Kid</conceptLabel>\n" +
            "\t\t\t\t</hasAgentID>\n" +
            "\t\t\t</performedBy>\n" +
            "\t\t\t<dateTimeCreated>2004-03-30T21:31:00Z</dateTimeCreated>\n" +
            "\t\t\t<dateTimeReported>2004-03-30T21:31:00Z</dateTimeReported>\n" +
            "\t\t\t<hasDataSource>\n" +
            "\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>95773c47-08d7-40df-93a6-25474f43f840</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "\t\t\t\t<terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "\t\t\t\t<conceptCode>danno</conceptCode>\n" +
            "\t\t\t\t<conceptLabel>DANNO VistA</conceptLabel>\n" +
            "\t\t\t</hasDataSource>\n" +
            "\t\t\t<hasTypeReference>\n" +
            "\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>880e18a4-be38-4513-9be3-e6f75fd07bf0</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<terminologySystemLabel>SNOMED-CT</terminologySystemLabel>\n" +
            "\t\t\t\t<terminologySystemCode>2.16.840.1.113883.6.96</terminologySystemCode>\n" +
            "\t\t\t\t<conceptCode>386725007</conceptCode>\n" +
            "\t\t\t\t<conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "\t\t\t</hasTypeReference>\n" +
            "\t\t\t<hasTypeReference>\n" +
            "\t\t\t\t<dyEntryType>ConceptPointer</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>afb89ae8-4ec5-4c15-9a76-b62be8790d21</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<terminologySystemLabel>VA</terminologySystemLabel>\n" +
            "\t\t\t\t<terminologySystemCode>2.16.840.1.113883.6.233</terminologySystemCode>\n" +
            "\t\t\t\t<conceptCode>4500638</conceptCode>\n" +
            "\t\t\t\t<conceptLabel>VA BODY TEMPERATURE</conceptLabel>\n" +
            "\t\t\t</hasTypeReference>\n" +
            "\t\t\t<hasVitalSignResult>\n" +
            "\t\t\t\t<dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "\t\t\t\t<dyEntryId>3049b6b8-8291-4c23-84d7-eff28dbf987a</dyEntryId>\n" +
            "\t\t\t\t<dyReference>false</dyReference>\n" +
            "\t\t\t\t<val>99.6</val>\n" +
            "\t\t\t\t<unit>degrees</unit>\n" +
            "\t\t\t</hasVitalSignResult>\n" +
            "\t</contains>\n" +
            "</FactListImpl>";


    @Test
    public void testFromXMLFactList() throws JAXBException {


        ObjectFactory factory = new ObjectFactory();
        JAXBContext jaxbContext = JAXBContext.newInstance(factory.getClass().getPackage().getName());

        Unmarshaller unmarshal = jaxbContext.createUnmarshaller();

        FactList list = (FactList) unmarshal.unmarshal( new StringReader( testList ) );

        assertEquals( 2, list.getContains().size() );

        ReferenceAdapter loader = unmarshal.getAdapter(ReferenceAdapter.class);

        assertEquals( 18, loader.getObjects().size() );

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

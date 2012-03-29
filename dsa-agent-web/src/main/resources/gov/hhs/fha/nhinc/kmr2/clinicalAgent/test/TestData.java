package gov.hhs.fha.nhinc.kmr2.clinicalAgent.test;


public class TestData {
    
    public static final String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<FactListImpl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\" >\n" +
            "    <contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "        <dyEntryType>VitalSign</dyEntryType>\n" +
            "        <dyEntryId>http://patients.kmr.org/danno/120_5-1</dyEntryId>\n" +
            "        <dyReference>false</dyReference>\n" +
            "        <hasPatient xsi:type=\"Patient\" >\n" +
            "            <dyEntryType>Patient</dyEntryType>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <!--dateTimeCreated>2012-03-01T00:28:43Z</dateTimeCreated-->\n" +
            "            <hasDataSource>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>b0047ff5-10b7-40ee-85e9-449b99b8fbdc</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>danno</conceptCode>\n" +
            "                <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "            </hasDataSource>\n" +
            "            <hasPatientID>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>6b94606e-2f00-4dda-94a3-5c79ca984a1c</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>http://patients.kmr.org/danno/2-1</conceptCode>\n" +
            "                <conceptLabel>Patient Fred Jone</conceptLabel>\n" +
            "            </hasPatientID>\n" +
            "            <hasLegalName>\n" +
            "                <dyEntryType>Name</dyEntryType>\n" +
            "                <dyEntryId>f2af792f-8a44-4f74-9062-3cd0e7b512d6</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <familyName>Jones</familyName>\n" +
            "                <firstName>Fred</firstName>\n" +
            "            </hasLegalName>\n" +
            "        </hasPatient>\n" +
            "        <performedBy>\n" +
            "            <dyEntryType>Agent</dyEntryType>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/200-1</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <hasDataSource>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>f4be872b-711e-426b-a998-329379a46bfe</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>danno</conceptCode>\n" +
            "                <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "            </hasDataSource>\n" +
            "            <hasLegalName>\n" +
            "                <dyEntryType>Name</dyEntryType>\n" +
            "                <dyEntryId>aae555e4-a563-4132-89e3-20e82ff19b54</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <familyName>Smith</familyName>\n" +
            "                <firstName>Joe</firstName>\n" +
            "            </hasLegalName>\n" +
            "            <hasAgentID>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>e274565d-268e-43c8-a167-19cdec1f39bb</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>http://patients.kmr.org/danno/200-1</conceptCode>\n" +
            "                <conceptLabel>Agent Joe Smith</conceptLabel>\n" +
            "            </hasAgentID>\n" +
            "        </performedBy>\n" +
            "        <!--dateTimeCreated>2012-03-01T00:28:43Z</dateTimeCreated-->\n" +
            "        <!--dateTimeReported>2012-03-01T00:28:43Z</dateTimeReported-->\n" +
            "        <hasDataSource>\n" +
            "            <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "            <dyEntryId>2978352a-964e-44d9-9c0f-338e5baff972</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "            <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "            <conceptCode>danno</conceptCode>\n" +
            "            <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "        </hasDataSource>\n" +
            "        <hasTypeReference>\n" +
            "            <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "            <dyEntryId>63040897-f300-4037-8137-54b63396d087</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <terminologySystemLabel>SNOMED-CT</terminologySystemLabel>\n" +
            "            <terminologySystemCode>2.16.840.1.113883.6.96</terminologySystemCode>\n" +
            "            <conceptCode>386725007</conceptCode>\n" +
            "            <conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "        </hasTypeReference>\n" +
            "        <hasVitalSignResult>\n" +
            "            <dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "            <dyEntryId>887c651c-afb5-4eed-865e-33a5ccf04981</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <val>98.6</val>\n" +
            "            <unit>degrees</unit>\n" +
            "        </hasVitalSignResult>\n" +
            "    </contains>\n" +
            "    <contains xsi:type=\"VitalSign\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "        <dyEntryType>VitalSign</dyEntryType>\n" +
            "        <dyEntryId>http://patients.kmr.org/danno/120_5-2</dyEntryId>\n" +
            "        <dyReference>false</dyReference>\n" +
            "        <hasPatient>\n" +
            "            <dyEntryType>Patient</dyEntryType>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/2-1</dyEntryId>\n" +
            "            <dyReference>true</dyReference>\n" +
            "        </hasPatient>\n" +
            "        <performedBy>\n" +
            "            <dyEntryType>Agent</dyEntryType>\n" +
            "            <dyEntryId>http://patients.kmr.org/danno/200-2</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <hasDataSource>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>d10be252-8b0d-4126-81c5-c6de8d56885b</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>danno</conceptCode>\n" +
            "                <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "            </hasDataSource>\n" +
            "            <hasLegalName>\n" +
            "                <dyEntryType>Name</dyEntryType>\n" +
            "                <dyEntryId>87bb1584-503f-4aca-88d6-47e6b67dd904</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <familyName>Kid</familyName>\n" +
            "                <firstName>Bill</firstName>\n" +
            "            </hasLegalName>\n" +
            "            <hasAgentID>\n" +
            "                <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "                <dyEntryId>2a38ae73-f107-4d71-a886-68ebed7e7b2f</dyEntryId>\n" +
            "                <dyReference>false</dyReference>\n" +
            "                <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "                <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "                <conceptCode>http://patients.kmr.org/danno/200-2</conceptCode>\n" +
            "                <conceptLabel>Agent Bill Kid</conceptLabel>\n" +
            "            </hasAgentID>\n" +
            "        </performedBy>\n" +
            "        <dateTimeCreated>2012-03-01T00:28:43Z</dateTimeCreated>\n" +
            "        <dateTimeReported>2012-03-01T00:28:43Z</dateTimeReported>\n" +
            "        <hasDataSource>\n" +
            "            <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "            <dyEntryId>95773c47-08d7-40df-93a6-25474f43f840</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <terminologySystemLabel>KMR</terminologySystemLabel>\n" +
            "            <terminologySystemCode>0.00.000.0.000000.0.0</terminologySystemCode>\n" +
            "            <conceptCode>danno</conceptCode>\n" +
            "            <conceptLabel>DANNO VistA</conceptLabel>\n" +
            "        </hasDataSource>\n" +
            "        <hasTypeReference>\n" +
            "            <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "            <dyEntryId>880e18a4-be38-4513-9be3-e6f75fd07bf0</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <terminologySystemLabel>SNOMED-CT</terminologySystemLabel>\n" +
            "            <terminologySystemCode>2.16.840.1.113883.6.96</terminologySystemCode>\n" +
            "            <conceptCode>386725007</conceptCode>\n" +
            "            <conceptLabel>BODY TEMPERATURE</conceptLabel>\n" +
            "        </hasTypeReference>\n" +
            "        <hasTypeReference>\n" +
            "            <dyEntryType>ConceptPointer</dyEntryType>\n" +
            "            <dyEntryId>afb89ae8-4ec5-4c15-9a76-b62be8790d21</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <terminologySystemLabel>VA</terminologySystemLabel>\n" +
            "            <terminologySystemCode>2.16.840.1.113883.6.233</terminologySystemCode>\n" +
            "            <conceptCode>4500638</conceptCode>\n" +
            "            <conceptLabel>VA BODY TEMPERATURE</conceptLabel>\n" +
            "        </hasTypeReference>\n" +
            "        <hasVitalSignResult>\n" +
            "            <dyEntryType>ValueUnitPair</dyEntryType>\n" +
            "            <dyEntryId>3049b6b8-8291-4c23-84d7-eff28dbf987a</dyEntryId>\n" +
            "            <dyReference>false</dyReference>\n" +
            "            <val>99.6</val>\n" +
            "            <unit>degrees</unit>\n" +
            "        </hasVitalSignResult>\n" +
            "    </contains>\n" +
            "</FactListImpl>";
}

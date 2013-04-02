Introduction
---------------------
Episteme UI developed with knockout.js

## Installation

###1.Apache Tomcat
Download from http://tomcat.apache.org/  
Tests done using Tomcat 7.0.37 at localhost:8080

###2.HTML pages deployment
https://github.com/gsi-upm/Episteme/tree/master/web  
Put the web content in the webapps/ROOT tomcat folder.  
Now you can access: http://localhost:8080/#/main/select
####2.1.Web pages configuration
Modify js/configuration.js with the following urls:  
endPoint = 'http://localhost:8080/LMF-2.6.0/';  
endPointSOLR = 'http://localhost:8080/LMF-2.6.0/solr/companies/select?json.nl=map&wt=json&rows=1000';  
endPointJSON = "http://localhost:8080/LMF-2.6.0/config/data/";  
endPointSemanticMatcher = "http://lab.gsi.dit.upm.es/episteme/tomcat/Episteme/CompanyMatcher";  

###3.LMF installation
Download from https://code.google.com/p/lmf/downloads/list  
Test done using LMF-2.6.0.war
####3.1.LMF deployment
Configure maximun upload size of Tomcat server modifying /manager/WEB-INF/web.xml:  

    <multipart-config>
    <!-- 50MB max -->
    <max-file-size>52428800</max-file-size>
    <max-request-size>52428800</max-request-size>
    <file-size-threshold>0</file-size-threshold>
    </multipart-config>

Then deploy LMF-2.6.0.war and you can access: http://localhost:8080/LMF-2.6.0  
####3.1.LMF configuration
Create new core (Semantic Search -> cores)  
Core name: companies  
And add the following code

    @filter rdf:type is <http://kmm.lboro.ac.uk/ecos/1.0#Enterprise> ;
    name = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#fn> :: xsd:string ;
    Province = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#adr> / <http://www.w3.org/2006/vcard/ns#locality> :: xsd:string ;
    logo = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#logo> :: xsd:string ;
    Type = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#org> / <http://www.w3.org/2006/vcard/ns#organisation-unit> :: xsd:string ;
    Description = <http://kmm.lboro.ac.uk/ecos/1.0#Specific> / <http://kmm.lboro.ac.uk/ecos/1.0#Plan> / <http://kmm.lboro.ac.uk/ecos/1.0#detail> :: xsd:string ;
    Provenance = <http://kmm.lboro.ac.uk/ecos/1.0#provenance> / <http://kmm.lboro.ac.uk/ecos/1.0#name> :: xsd:string ;
    Ranking = <http://kmm.lboro.ac.uk/ecos/1.0#ranking> / <http://kmm.lboro.ac.uk/ecos/1.0#value> :: xsd:string ;

###4.RDF Uploader
https://github.com/gsi-upm/Episteme/tree/master/RDFuploader  
You can use the included and processed companies RDF.  

* INES companies:  
Put the RDF files in upload folder and execute the following command:  
java -jar importercustom.jar http://localhost:8080/LMF-2.6.0/ http://ines  

* VULKA companies:  
Put the RDF files in upload folder and execute the following command:  
java -jar importercustom.jar http://localhost:8080/LMF-2.6.0/ http://vulka  

###5.Semantic matcher configuration
To properly configure the user must open with any text editor file "JSONTreatment.java". This file is located in the directory 'src / com / upm / dit / gsi / knowledge / json' and should modify the following lines:

	…..
	querySkills = "http://minsky.gsi.dit.upm.es/episteme/tomcat/LMF/sparql/select?query="+querySkills+"&output=json";
	……
	String queryOportunitie = "http://minsky.gsi.dit.upm.es/episteme/tomcat/LMF/config/data/episteme.search." + offer;
	……
	queryEnterprise ="http://minsky.gsi.dit.upm.es/episteme/tomcat/LMF/sparql/select?query="+queryEnterprise+"&output=json";
	……

###6.RDF Processor (optional)
https://github.com/gsi-upm/Episteme/tree/master/RDFprocessor  
Once you have obtained the RDF companies using Scrappy it's neccesary apply a post-processing in order to 
add skills and provenance fields:

* INES companies:  
Put the RDF files in RDFprocessor/data folder and execute the following command:  
java-jar RDFprocessor.jar ines  

* VULKA companies:  
Put the RDF files in RDFprocessor/data folder and execute the following command:  
java-jar RDFprocessor.jar vulka  

When the RDF are post-processed, now you can use RDF Uploader to upload the RDF files to LMF.  


## Contributors

* Felipe Echanique (<neoner2002@gmail.com>) 
* Adriano José Martín (adrianojosemartin@gmail.com)  
* Carlos Crespo (carlos.crespog@gmail.com )  
* Roberto Bermejo (rober.bv@gmail.com)  

at [Grupo de Sistemas Inteligentes](http://www.gsi.dit.upm.es/), ETSIT-UPM.


## License

```
Copyright 2012 UPM-GSI: Group of Intelligent Systems - Universidad Politécnica de Madrid (UPM)

Licensed under the Apache License, Version 2.0 (the "License"); 
You may not use this file except in compliance with the License. 
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by 
applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific 
language governing permissions and limitations under the License.
```
![GSI Logo](http://gsi.dit.upm.es/templates/jgsi/images/logo.png)

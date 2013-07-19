Introduction
---------------------
Episteme UI developed with knockout.js

## Requirements

### 1. Apache Tomcat
The Apache Tomcat will be used for the backend, necessary to deploy:

- Linked Media Framework (LMF)
- Semantic Matcher servlet

For the frontend you can use the same Apache Tomcat or any webserver, making sure it executes in the same domain as the backend, otherwise you will need to configure the webserver to accept cross-site HTTPRequests.


Tested with Apache Tomcat/7.0.41

### 2. Linked Media Framework (LMF)

LMF version 2.6.0 is needed or any version that supports configuring solr local only indexing.
You can download it from [https://code.google.com/p/lmf/downloads/list](https://code.google.com/p/lmf/downloads/list)

It is advisable to download the WAR version and deploy it under the Apache Tomcat already downloaded.


## Installation


###1.Apache Tomcat
Download from 
```
http://tomcat.apache.org/
```

Extract the archive. This will be the 'tomcat home'.

To start Apache Tomcat execute 
```
bin/startup.sh
```
To test if it is working navigate to http://localhost:8080

Tests done using Tomcat 7.0.41 at localhost:8080

###2. LMF Instalation
#### 2.1 LMF Deployment

 + Download from LMF-2.6.0.war [https://code.google.com/p/lmf/downloads/list](https://code.google.com/p/lmf/downloads/list)
 + Move LMF-2.6.0.war into 'tomcat home'/webapps/ directory. Tomcat automatically will extract the content and deploy it.

LMF 3.0.0 has also been tested. LMF 3.0.0 implements http authentication by default, you will need to configure it so episteme
works. For developement purposes you can desactivate the authentication feature from LMF administration backend.

Core services -> configuration -> security.enabled (false)

In order to change any value at LMF configuration you will need to access the administration panel trought "localhost" or using
an auithenticated user. Via "localhost" security (authentication) is disabled by default.
#### 2.2 LMF Configuration

##### 2.2.1 Create new core

Create new core (Semantic Search -> cores)  
Core name: **companies**

And add the following code

    @filter rdf:type is <http://kmm.lboro.ac.uk/ecos/1.0#Enterprise> ;
    name = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#fn> :: xsd:string ;
    Province = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#adr> / <http://www.w3.org/2006/vcard/ns#locality> :: xsd:string ;
    logo = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#logo> :: xsd:string ;
    Type = <http://www.w3.org/2006/vcard/ns#VCard> / <http://www.w3.org/2006/vcard/ns#org> / <http://www.w3.org/2006/vcard/ns#organisation-unit> :: xsd:string ;
    Description = <http://kmm.lboro.ac.uk/ecos/1.0#Specific> / <http://kmm.lboro.ac.uk/ecos/1.0#Plan> / <http://kmm.lboro.ac.uk/ecos/1.0#detail> :: xsd:string ;
    Provenance = <http://kmm.lboro.ac.uk/ecos/1.0#provenance> / <http://kmm.lboro.ac.uk/ecos/1.0#name> :: xsd:string ;
    Ranking = <http://kmm.lboro.ac.uk/ecos/1.0#ranking> / <http://kmm.lboro.ac.uk/ecos/1.0#value> :: xsd:string ;

##### 2.2.2 Solr local only (very important)
Semantic search -> configuration

**uncheck** *solr.local only*

### 3. Download Episteme frontend, data and CompanyMatcher

Make a git clone 

```
git clone https://github.com/gsi-upm/Episteme.git
```

or download the zipfile

```
https://github.com/gsi-upm/Episteme/archive/master.zip
```

### 4. Upload content to LMF

You can use the included and processed companies RDF. These files are ready to upload to LMF.

Go inside Episteme directory and then into RDFuploader

```
cd RDFuploader
```

* INES companies:
 
```
cd INESrfd
```

```
java -jar ../upload/importercustom.jar http://localhost:8080/LMF-2.6.0/ http://ines
```

After this, go back to RDFuploader directory

```
cd ..
```


* VULKA companies:  

```
cd VULKArfd
```

```
java -jar ../upload/importercustom.jar http://localhost:8080/LMF-2.6.0/ http://vulka
```

###5. RDF Processor (optional)

There is no need to follow this step, by doing step number 4 RDF pre-proccesed is uploaded to the LMF. If you want to upload your own scrapped RDF follow the instructions.

Once you have obtained the RDF companies using Scrappy it's neccesary apply a post-processing in order to 
add skills and provenance fields:

* INES companies:  
Put the RDF files in RDFprocessor/data folder and execute the following command:
```
java -jar RDFprocessor.jar ines
```

* VULKA companies:  
Put the RDF files in RDFprocessor/data folder and execute the following command:
```
java -jar RDFprocessor.jar vulka
```

When the RDF are post-processed, now you can use RDF Uploader to upload the RDF files to LMF.

###6. SemanticMatcher installation

#### 6.1 Deployment

Deploy

```
SemanticMatcher/build/SemanticMatcher.war
```

into tomcat as done in 2.1 LMF deployment. Copy or move SemanticMatcher into 'tomcat home'/webapps/

It will be automatically deployed with the following URL:

```
http://localhost:8080/SemanticMatcher/
```

The servlet will be available in:

```
http://localhost:8080/SemanticMatcher/CompanyMatcher
```

#### 6.2 Configuration
You need to configure the file 

```
'tomcat home'/webapps/CompanyMatcher/conf/jsonTreatment.properties
```

It looks like

```
# Configure LMF Sparql Endpoint
# Example configuration
# lmfSparqlUrl=http://domain.com/LMF/sparql/select?query=


lmfSparqlUrl=http://domain.com/LMF/sparql/select?query=
```

Change the **lmfSparqlUrl** to match your configuration, in this installation case would be 

```
http://localhost:8080/LMF-2.6.0/sparql/select?query=
```

###7. HTML client files

#### 7.1 Deployment
Copy the content of 'web' directory in Episteme into 'tomcat home'/webapps/ROOT in case you want to publish the
web aplication in the root path. Otherwise, copy to tomcat webapps/episteme directory.

The content to copy looks like
```
css
images
js
index.html
```

Copy everything into 
```
'tomcat home'/webapps/ROOT
```
(or /webapps/episteme)

If needed, overwrite existing index.html file.

Check the content accesing http://localhost:8080/ (or http://localhost/episteme:8080/).

####7.2 Configuration

Modify 
```
'tomcat home'/webapps/ROOT/js/configuration.js
```

with the following parameters:  

```
endPoint = 'http://localhost:8080/LMF-2.6.0/';  
endPointSOLR = 'http://localhost:8080/LMF-2.6.0/solr/companies/select?json.nl=map&wt=json&rows=1000';  
endPointJSON = "http://localhost:8080/LMF-2.6.0/config/data/";  
endPointSemanticMatcher = "http://http://localhost:8080/SemanticMatcher/CompanyMatcher";
```

Change what you need to match your configuration.


####7.3 Test


As it is a web client, make sure you get the new configuration.js file. Reload cache if needed. (In most of browsers Control+F5 to force reloading)

Check the content accesing http://localhost:8080/
Visit the following videos and start using Episteme

+ http://www.youtube.com/watch?v=q6WwBgxRvvo
+ http://www.youtube.com/watch?v=urzlFJ-0gBc

## Contributors

* Felipe Echanique (<neoner2002@gmail.com>) 
* Adriano José Martín (<adrianojosemartin@gmail.com>)  
* Carlos Crespo (<carlos.crespog@gmail.com>)  
* Roberto Bermejo (<rober.bv@gmail.com>)
* Pablo Moncada (<pmoncada@dit.upm.es>)

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

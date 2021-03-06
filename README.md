# information-retrieval-system-for-romanian-language


## Setup steps
During the development process of the project I worked with <b>IntelliJ IDEA 2021.2.3 (Community Edition)</b>.
Here are the steps I followed to make the setup:

**NOTE**: With the purpose of avoiding writing absolute paths, make the project's directory the *Sources Root*
1. Create a new IntelliJ Maven project - this will also create a *pom.xml* file dedicated for Maven configuration settings
2. Add Apache Lucene jars (listed [here](#apache-lucene-jars)) to the project (for each jar, repeat the *v* and *vi* steps)
    1. Go to the <b>File</b> tab
    2. Next choose <b>Project Structure</b>
    3. Under <b>Project Settings</b> select <b>Modules</b>
    4. Go to <b>Dependencies</b>
    5. Click on the <b>+ symbol</b> and select the first option - <b>JARs or Directories</b>
    6. Browse to the location where you placed and extracted the Apache Lucene package and select the jar
3. Click <b>Apply</b>
4. Add Apache Tika Maven dependencies - replace your *pom.xml* file with the one in this repositories

## Prerequisetes versions used when developing the project
* [Java - fullversion 1.8.0_281](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html), 281 is the update number of Java version 8
* [Apache Lucene - version 8.11.0](https://www.apache.org/dyn/closer.lua/lucene/java/8.11.0/lucene-8.11.0.zip), click on the HTTP link to download the package's archive
* [Apache Tika Core - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-core/2.1.0)
* [Apache Tika Parsers - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parsers/2.1.0)
* [Apache Tika Serialization - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-serialization/2.1.0)
* [Apache Tika PDF Parser Module - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parser-pdf-module/2.1.0)
* [Apache Tika Microsoft Parser Module - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parser-microsoft-module/2.1.0)

### Apache Lucene Jars
**NOTE**: All the JARs are found in the downloaded zip, lucene-8.11.0
* lucene-analyzers-common-8.11.0.jar
* lucene-core-8.11.0.jar
* lucene-queryparser-8.11.0.jar

## Run the application
**NOTE**: all *.txt* files have to be encoded with *UTF-8*
* For indexing the documents (all documents that will be indexed are placed in the *files* directory) you will <b>run main function</b> inside <b>IndexFiles</b> class, with the argument *--open-mode=mode* where *mode* can be either *CREATE*, or *APPEND* - this action will create a new directory called *index* where the documents' index is placed
* To run a search, next you will <b>run main function</b> inside <b>SearchFiles</b> class, with the following arguments: the first argument must be *--query-path* and the second one will be the path to the file that contains your query (in this repository the query is written inside *query.txt* file) - this action will return the documents' paths in which one or more of the query's words are found along with the similarity score between the query and the document

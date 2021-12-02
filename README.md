# information-retrieval-system-for-romanian-language


## Setup steps
During the development process of the project I worked with <b>IntelliJ IDEA 2021.2.3 (Community Edition)</b>.
Here are the steps I followed to make the setup:
1. Create a new IntelliJ Maven project
2. Add Apache Lucene jars (listed [here](#apache-lucene-jars)) to the project (repeat the next steps for each jar)
    1. Go to the <b>File</b> tab
    2. Next choose <b>Project Structure</b>
    3. Under <b>Project Settings</b> select <b>Modules</b>
    4. Go to <b>Dependencies</b>, click on the <b>+ symbol</b> and select the first option - <b>JARs or Directories</b>
    5. Browse to the location where you placed and extracted the Apache Lucene package and select the jar
3. Click <b>Apply</b>

## Prerequisetes versions used when developing the project
* [Java - fullversion 1.8.0_281](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html), 281 is the update number of Java version 8
* [Apache Lucene - version 8.11.0](https://www.apache.org/dyn/closer.lua/lucene/java/8.11.0/lucene-8.11.0.zip), click on the HTTP link to download the package's archive
* [Apache Tika Core - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-core/2.1.0)
* [Apache Tika Parsers - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parsers/2.1.0)
* [Apache Tika Serialization - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-serialization/2.1.0)
* [Apache Tika PDF Parser Module - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parser-pdf-module/2.1.0)
* [Apache Tika Microsoft Parser Module - version 2.1.0](https://mvnrepository.com/artifact/org.apache.tika/tika-parser-microsoft-module/2.1.0)

### Apache Lucene Jars


# CRSP-Engine [![Build Status](https://travis-ci.org/FraserBrown/CRSP-Engine.svg?branch=master)](https://travis-ci.org/FraserBrown/CRSP-Engine)
#### Continuous RDF Stream Processing Engine (CRSP Engine) using RSPQL
---
[//]: # (Comment)

Our Continuous RSPQL Stream Processing (CRSP) Engine is a new tool that allows for ***continuous RSP-QL queries*** to be applied over streams of RDF ***graph*** data. 
The primary goal for our system is to be able to run any valid RSP-QL query over any valid RDF graph stream and output the correct result. 

[//]: # (Screenshot of program in use)
![](header.png)

## Installation Dependancy Prerequisites:
* Java version: 1.8

### Installing Java JDK 1.8 on Linux
>Create java folder in opt and make it your working directory
```sh
sudo mkdir /opt/java && cd /opt/java
```
>Download [``java 1.8 jdk``](java-jdl-dl) .tar file

>Copy downloaded file into ``/opt/java``
```sh
sudo mv ~/Downloads/jdk-8u161-linux-x64.tar.gz /opt/java
```

> Uzip jdk, then remove original zip
```sh
sudo tar -zxvf jdk-8u161-linux-x64.tar.gz
sudo rm -rf jdk-8u161-linux-x64.tar.gz
```

> Update your alternatives entry for ``java``, ``javac``, ``jar`` telling the system where they are instlaled.
```sh
sudo update-alternatives --install /usr/bin/java java /opt/java/jdk1.8.0_161/bin/java 100
sudo update-alternatives --config java
sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk1.8.0_161/bin/javac 100
sudo update-alternatives --config javac
sudo update-alternatives --install /usr/bin/jar jar /opt/java/jdk1.8.0_161/bin/jar 100
sudo update-alternatives --config jar
```

>Configure engironement variables:
```sh
export JAVA_HOME=/opt/java/jdk1.8.0_161/
export JRE_HOME=/opt/java/jdk1.8.0_161/jre
export PATH=$PATH:/opt/java/jdk1.8.0_161/bin:/opt/java/jdk1.8.0_161/jre/bin
```

>Confirm java setup:
```sh
java -version

# will output:
java version "1.8.0_161"
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)
```

## Installation
OSX & Linux & Windows:

```sh
git clone https://github.com/FraserBrown/CRSP-Engine.git .
```

## Running/Using CRSP Engine:
The CRSP Engine comes with a GUI to make interaction with our system easier.

> 1. To run the GUI please enter the following from the CRSP-Engine folder:
```sh
java -jar out/artifacts/crsp_engine_jar/crsp-engine.jar
```

You should see the following:

![](figures/crsp-gui.png)

> 2. Create input and output files:

Using text files the our system can parse and output data:
* JSON Graph Streams in JSON Format [json stream example][json-graph-stream]
* RSPQL Queries [example RSPQL query](query.txt)
* Output Query Results into an output text file.

> 3. Enter their file paths into the relevent text boxes

> 4. Click ``calculate``
Your specified output file should be populated with results.

## Contributing

### Naming Convention
Depending on the type of contribution you are making please name your branch with one of the following tags in this format (TAG/your_feature_name)
- feature
- hotfix
- bugfix

### Setting Up Git
>1. Clone our code, and checkout our dev branch
```sh
$ git clone https://github.com/FraserBrown/CRSP-Engine.git
$ git checkout dev
```

>2. Create new ``branch`` (example creates a *feature* branch)
```sh
$ git checkout -b feature/new_feature dev
```

>3. Develop your code, commit code, and push changes to your new branch


>4. When development is complete create a pull request


## Development Environment Setup
Install JDK1.8 as shown in [Installing Java JDK 1.8 on Linux](#Installing-Java-JDK-1.8-on-Linux).

### Set up Eclipse


## Meta
Distributed under the XYZ license. See ``LICENSE`` for more information.

<!-- Markdown link & img dfn's -->
[java-jdk-dl]: http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html
[json-graph-stream]:https://raw.githubusercontent.com/streamreasoning/RSP-QL/gh-pages/Example_RDF_Streams/BGN_Location_TempC_Minute_Unioned.json
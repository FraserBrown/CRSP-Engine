# CRSP Engine
---

## Project Status:
---
* Continuous Integration: [![Build Status](https://travis-ci.org/FraserBrown/CRSP-Engine.svg?branch=master)](https://travis-ci.org/FraserBrown/CRSP-Engine)

## What is CRSP?
---

Our Continuous RSPQL Stream Processing (CRSP) Engine is a new tool that allows for ***continuous RSP-QL queries*** to be applied over streams of RDF ***graph*** data. 
The primary goal for our system is to be able to run any valid RSP-QL query over any valid RDF graph stream and output the correct result. 
The CRSP-Engine makes use of a frontend graphical user interface that allows users to input a query and graph stream. 
These inputs are then passed to our Data Stream Management System which will parse the query to ensure the given query's RSP-QL syntax is correct. 
Our engine will then evaluate the query over the given stream and return the result to the user via the graphical user interface.

## How does the CRSP-Engine work?
---

The CRSP-Engine is an extension of the [RDF4J](http://rdf4j.org/) SPARQL parser. 
This extension comes in the form of 3 new key developments:
1. A RSP-QL parser.
2. A Graph stream handler.
3. A frontend GUI.

### CRSP-Engine's Parser
---

Our engine provides validation for queries that have RSP-QL's extended syntax through modifications made to the abstract syntax tree of RDF4J's SPARQL parser. 
These modifications where made through the use of the [JJtree](https://javacc.org/jjtree) preprocessor and the [JavaCC](https://javacc.org/) compiler which allowed for the generation of a new RSP-QL parser that reads in a new grammar specification that was designed by our team, based on syntax provided in [this complex RSP-QL query.](https://github.com/streamreasoning/RSP-QL/blob/gh-pages/Example_of_RSP-QL_query.md)
Our new parser and grammar allows for the CRSP-Engine to understand the concept of continuous ***windows*** that are specified inside of a query and how they are related to RDF graph stream data. 
These ***windows*** can be used by the CRSP-Engine to evaluate a query over a subset of a ***RDF graph stream*** by comparing the query window to the extra time attribute attached to each graph within a stream. 

### CRSP-Engine's Graph implemention.
---

Graphs are represented in the CRSP-Engine as an extension of RDF4J's Model interface.
More specifically, this means graphs are implemented with their ***data*** represented as a Model as well as a unique ***ID*** and ***observedAt*** time which denotes the time that an entire graph was observed at.
It is this ***observedAt*** time that is used by our engine to correlate graphs and windows.

### CRSP-Engine's Graphical User Interface
---

Our engine also features a simple GUI that allows a user to input a query via a text file and graph stream via a json file. After the engine has applied the query over the graph stream, the user can view the result in a outputted text file.

## What is RDF?
---

The Resource Description Framework (RDF) is a data model used to relate items of data to one another. RDF statements are displayed as triples in the form subject–predicate–object where: 
Subject: Denotes a resource
Predicate: Defines relationship between subject and object
Object: Denotes a resource or literal
High level example of an RDF triple:

```
Apple (subject) sells (predicate) IPhone (object)
```

In-depth information on RDF can be found at the following links:

https://www.w3.org/RDF/

https://www.w3.org/TR/rdf-concepts/

## What are RDF Streams?
---

RDF streams are collections of RDF data sent out or received at a continuous rate. Typically these data streams contain RDF triples as shown above with an extra time attribute attached to the triple.

E.g. <s1,p1,o1>,t1 where s1=subject, p1=predicate, o1=object and t1-timestamp.

## What is RSPQL?
---

RSP-QL (RDF Stream Processing Query Language) is a new language for the semantic web that queries over streams of RDF graphs. RSP-QL will expand the application domain of previous existing RSP languages by allowing queries to be directly applied to multiple triples that are grouped inside graphs. Such application domains include real-time reasoning over sensors, urban computing, and social semantic data.

The grammar of RSP-QL is an extension of the features currently present in C-SPARQL, CQELS, and SPARQL-Stream because RSP-QL contains unique features that are missing in all other RSP languages, such as the FROM NAMED WINDOW clause in the dataset declaration or the WINDOW keyword in the WHERE clause.

An example of a simple SELECT query using this new syntax is shown below:

```
PREFIX ex: <http://example.org/> 
SELECT ?p ?o
FROM NAMED WINDOW :win ON s:examples [RANGE PT5m STEP PT1m]
WHERE { 
	WINDOW :win { 
		ex:Paris ?p ?o
	}
}
```

More information on RSP-QL can be found its GitHub page and the W3C Community Group on RDF Stream Processing website:

https://github.com/streamreasoning/RSP-QL

https://www.w3.org/community/rsp/

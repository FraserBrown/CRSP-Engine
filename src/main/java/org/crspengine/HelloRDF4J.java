package org.crspengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class HelloRDF4J {

	public static void main(String[] args) {
		Repository rep = new SailRepository(new MemoryStore());
		rep.initialize();
		String namespace = "http://example.org/";
		ValueFactory f = rep.getValueFactory();
		IRI john = f.createIRI(namespace, "John");
		IRI adam = f.createIRI(namespace, "Adam");
		try (RepositoryConnection conn = rep.getConnection()){
			conn.add(john, RDF.TYPE, FOAF.PERSON);
			conn.add(john,  RDFS.LABEL, f.createLiteral("John"));
			conn.add(adam, RDF.TYPE, FOAF.PERSON);
			conn.add(adam,  RDFS.LABEL, f.createLiteral("Adam"));

		    String queryString = "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
		    queryString += "PREFIX rdfs: <" + RDFS.NAMESPACE + "> \n";
		    queryString += "PREFIX ns1: <Person> \n";
		    queryString += "PREFIX ex: <http://example.org/> \n";
		    queryString += "SELECT ?s \n";
		    queryString += "FROM NAMED WINDOW :wind ON s:trips [RANGE PT1H STEP PT1H] \n";
		    queryString += "WHERE { \n";
		    queryString += "WINDOW :win { \n";
		    queryString += "    ?s a <" + FOAF.NAMESPACE + "Person> \n";
		    queryString += "}";
		    queryString += "}";
		    
		    //create list of each word in the query
		    List<String> wordList = new ArrayList<String>(Arrays.asList(queryString.split(" ")));
		    
		    for(int i=0; i < wordList.size(); i++)
		    {
		    	//remove any newlines
		    	wordList.set(i, wordList.get(i).replace("\n",""));
		    }
		    
		    //remove any empty strings
		    wordList.removeAll(Arrays.asList("", null));
	    	
		    List<String> prefixNames = new ArrayList<String>();
		    List<String> prefixValues = new ArrayList<String>();
		    List<String> variableNames = new ArrayList<String>();
		    List<String> whereClauses = new ArrayList<String>();
		    
		    for(int i=0; i < wordList.size(); i++)
		    {
		    	if(wordList.get(i).equals("PREFIX"))
		    	{
		    		prefixNames.add(wordList.get(i+1));
		    		prefixValues.add(wordList.get(i+2));
		    	}
		    	if(wordList.get(i).contains("?"))
		    	{
		    		variableNames.add(wordList.get(i));
		    	}
		    	if(wordList.get(i).equals("WHERE"))
		    	{
		    		whereClauses.add(wordList.get(i+2));
		    		whereClauses.add(wordList.get(i+3));
		    		whereClauses.add(wordList.get(i+4));
		    	}
		    }
		    
		    for(int i = 0; i < prefixNames.size(); i++) 
		    {
	            //System.out.println(prefixNames.get(i));
	           // System.out.println(prefixValues.get(i));
	        }
		    
		    for(int i = 0; i < variableNames.size(); i++) 
		    {
	           // System.out.println(variableNames.get(i));
		    }
		    
		    for(int i = 0; i < whereClauses.size(); i++) 
		    {
	           // System.out.println(whereClauses.get(i));
		    }
		    
		    
		    TupleQuery query = conn.prepareTupleQuery(queryString);
		    // A QueryResult is also an AutoCloseable resource, so make sure it gets
		    // closed when done.
		    try (TupleQueryResult result = query.evaluate()) {
			// we just iterate over all solutions in the result...
	    	while (result.hasNext()) {
	    	    BindingSet solution = result.next();
	    	    // ... and print out the value of the variable bindings
	    	    // for ?s and ?n
	    	    System.out.println("?s = " + solution.getValue("s"));
	    	}
		    }
		}
		finally {
		    // Before our program exits, make sure the database is properly shut down.
			rep.shutDown();
		}
		
	}

}

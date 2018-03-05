package org.crspengine;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
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
            queryString += "CONSTRUCT \n";
            queryString += "WHERE { \n";
            queryString += "    ?s a <" + FOAF.NAMESPACE + "Person> \n";
            queryString += "}";
            GraphQuery query = conn.prepareGraphQuery(queryString);
            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            try (GraphQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    Statement solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    System.out.println(solution);
                }
            }

			/*RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			Model model = QueryResults.asModel(statements);
			model.setNamespace(RDF.NS);
			model.setNamespace(RDFS.NS);
			model.setNamespace(FOAF.NAMESPACE, "Person");
			model.setNamespace("ex", namespace);
			Rio.write(model, System.out, RDFFormat.TURTLE);*/
        }
        finally {
            // Before our program exits, make sure the database is properly shut down.
            rep.shutDown();
        }

    }

}

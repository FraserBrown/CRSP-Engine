package org.crspengine;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
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
		try (RepositoryConnection conn = rep.getConnection()){
			conn.add(john, RDF.TYPE, FOAF.PERSON);
			conn.add(john,  RDFS.LABEL, f.createLiteral("John"));
			RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			Model model = QueryResults.asModel(statements);
			model.setNamespace(RDF.NS);
			model.setNamespace(RDFS.NS);
			model.setNamespace(FOAF.NAMESPACE, "Person");
			model.setNamespace("ex", namespace);
			Rio.write(model, System.out, RDFFormat.TURTLE);
		}
		
	}

}

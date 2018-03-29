package org.crspengine;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;

public class RSPQLQueryParser {
	
    /* Constructor */
	public RSPQLQueryParser() {
	}
	
    /* Public Functions */

	/***
	 * Parses a query using a modified version of RDF4J's .prepareTupleQuery(String query) method.
	 * @param conn
	 * @param queryString
	 * @return
	 */
	public SailTupleQuery parseRSPQLQuery(RepositoryConnection conn, String queryString) {

		return (SailTupleQuery) conn.prepareTupleQuery(queryString);
	}
}

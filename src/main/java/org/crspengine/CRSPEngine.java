package org.crspengine;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.util.ArrayList;

public class CRSPEngine {

    // Object to store data in one common place/instance.
    // Such as queryString, queryResults, jsonString and graphStream list
    private InternalDataManager idm;

    public CRSPEngine() {
        //internal data manager
        this.idm = new InternalDataManager();
    }

    /* Getters */
    public InternalDataManager getIdm() {
        return idm;
    }

    /* Setters */
    public void setIdm(InternalDataManager idm) {
        this.idm = idm;
    }

    /* Private helper functions */
    private ArrayList<InternalGraph> graphStreamFromJson (String jsonString, ArrayList<InternalGraph> graphStream){

        //Create JsonRDFGraphParser object
        JsonRDFGraphParser jsonRDFGraphParser = new JsonRDFGraphParser(jsonString);

        // create internal json data structure from json string
        jsonRDFGraphParser.parseJsonString();

        // convert internal json tree into graph stream - arraylist of internalgraphs.
        jsonRDFGraphParser.jsonToInternalGraphStream(graphStream);

        return graphStream;
    }

    private String parseAndCalculateQuery(String queryString, String queryResult, ArrayList<InternalGraph> graphStream) {
        // Create a new Repository. Here, we choose a database implementation
        // that simply stores everything in main memory.
        Repository db = new SailRepository(new MemoryStore());
        db.initialize();

        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            //Populate database with a null model. This allows us to parse the query for syntax errors before executing it over a given stream.
            conn.add(new ModelBuilder().build());

            //Create RSPQL parser object
            RSPQLQueryParser rspqlQueryParser = new RSPQLQueryParser();

            //Parse the query for correct RSPQL syntax.
            SailTupleQuery query = rspqlQueryParser.parseRSPQLQuery(conn, queryString);

            //Create query evaluator object
            RSPQLQueryEvaluator rspqlQueryEvaluator = new RSPQLQueryEvaluator();

            //Evaluate the RSPQL query over the RDF graph stream
            queryResult += rspqlQueryEvaluator.evaluteQuery(conn, query, graphStream);

        } finally {
            db.shutDown();
        }
        return queryResult;
    }

    /* Public functions */
    public void run(){
        String queryResult = "";
        ArrayList<InternalGraph> graphStream = this.idm.getGraphStream();

        // parse graph stream from json file
        graphStream = graphStreamFromJson(this.idm.getJsonString(), graphStream);

        // calculate query
        queryResult = parseAndCalculateQuery(this.idm.getQueryString(), queryResult, graphStream);

        this.idm.setQueryResult(queryResult);
        this.idm.setGraphStream(graphStream);
    }
}

import org.crspengine.RSPQLQueryParser;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RSPQLParseTest {

    private RSPQLQueryParser parser;
    private Repository db = new SailRepository(new MemoryStore());
    private RepositoryConnection conn;
    private String queryString = "";
    
    @Before
    public void setUp() {
    	parser = new RSPQLQueryParser();
        db.initialize();
        conn = db.getConnection();
    }
    
    @Test
    public void canParseSelectQuery(){
        queryString =  "PREFIX ex: <http://example.org/>  \n";
	    queryString += "SELECT ?p ?o \n";
	    queryString += "FROM NAMED WINDOW :win ON ex:example [RANGE PT10m STEP PT5m] \n";
	    queryString += "WHERE { \n";
	    queryString += "WINDOW :win { \n";
	    queryString += "	ex:Paris ?p ?o \n";
	    queryString += "	}";
	    queryString += "}";
    	
    	parser.parseRSPQLQuery(conn, queryString);

        // we have made the correct object
        Assert.assertThat(parser, CoreMatchers.instanceOf(RSPQLQueryParser.class));

        // Correctly typed variables
        Assert.assertThat(parser.parseRSPQLQuery(conn, queryString), CoreMatchers.is(CoreMatchers.instanceOf(SailTupleQuery.class)));
    }

    @Test
    public void canParseSelectCountQuery(){
	    String queryString = "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
	    queryString += "PREFIX rdfs: <" + RDFS.NAMESPACE + "> \n";
	    queryString += "PREFIX ns1: <Person> \n";
	    queryString += "PREFIX ex: <http://example.org/> \n";
	    queryString += "SELECT ?s (count(?s) as ?countUsers) \n";
	    queryString += "FROM NAMED WINDOW :win ON ex:trips [RANGE PT1H STEP PT5M] \n";
	    queryString += "WHERE { \n";
	    queryString += "WINDOW :win { \n";
	    queryString += "    ?s a <" + FOAF.NAMESPACE + "Person> \n";
	    queryString += "}";
	    queryString += "   GROUP BY ?s } \n";
    	
    	parser.parseRSPQLQuery(conn, queryString);

        // we have made the correct object
        Assert.assertThat(parser, CoreMatchers.instanceOf(RSPQLQueryParser.class));

        // Correctly typed variables
        Assert.assertThat(parser.parseRSPQLQuery(conn, queryString), CoreMatchers.is(CoreMatchers.instanceOf(SailTupleQuery.class)));
    }
    
    @Test
    public void canParseSelectFilterQuery(){
	    String queryString = " PREFIX f: <http://larkc.eu/csparql/sparql/jena/ext#> ";
	    	   queryString += "PREFIX ex: <http://myexample.org/> ";
	    	   queryString += "SELECT ?opinionMaker ?o (COUNT(?follower) AS ?n) ";
	    	   queryString += "FROM NAMED WINDOW :win ON f:trips [RANGE PT1H STEP PT1H] ";
	    	   queryString += "WHERE { ";
	    	   queryString += "WINDOW :win { ";
	    	   queryString += "?opinionMaker ex:likes ?o . ";
	    	   queryString += "?follower ex:likes ?o . ";
	    	   queryString += "FILTER(?opinionMaker!=?follower)";
	    	   queryString += "FILTER (f:timestamp(?follower,ex:likes,?o) > f:timestamp(?opinionMaker,ex:likes,?o)) ";
	    	   queryString += "} ";
	    	   queryString += "GROUP BY ?opinionMaker ?o ";
	    	   queryString += "HAVING (COUNT(?follower)>3)";
	    	   queryString += "}";
    	
    	parser.parseRSPQLQuery(conn, queryString);

        // we have made the correct object
        Assert.assertThat(parser, CoreMatchers.instanceOf(RSPQLQueryParser.class));

        // Correctly typed variables
        Assert.assertThat(parser.parseRSPQLQuery(conn, queryString), CoreMatchers.is(CoreMatchers.instanceOf(SailTupleQuery.class)));
    }
    
    /*
    @Test
    void failingTest() {
        fail("a failing test");
    }*/

    /*
    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
        // not executed
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
    */
}

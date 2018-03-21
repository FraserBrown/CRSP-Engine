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
	    queryString += "FROM NAMED WINDOW :win ON s:example [RANGE PT10m STEP PT5m] \n";
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
	    queryString += "FROM NAMED WINDOW :win ON s:trips [RANGE PT1H STEP PT1H] \n";
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

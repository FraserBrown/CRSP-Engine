import org.crspengine.InternalGraph;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.*;


public class testInternalGraphStructure {

    private InternalGraph ig;
    @Before
    public void setUp() {
        ig = new InternalGraph();
    }


    @Test
    public void canSetUpEmptyInternalGraph(){
        // initialize a new empty object
        // InternalGraph ig = new InternalGraph();
        ig.setGraphID("");
        ig.setGraphData(new ModelBuilder().build());
        ig.setObservedAt("");

        // we have made the correct object
        Assert.assertThat(ig, CoreMatchers.instanceOf(InternalGraph.class));

        // Correctly typed variables
        Assert.assertThat(ig.getGraphID(), CoreMatchers.is(CoreMatchers.instanceOf(String.class)));
        Assert.assertThat(ig.getGraphData(), CoreMatchers.is(CoreMatchers.instanceOf(Model.class)));
        Assert.assertThat(ig.getObservedAt(), CoreMatchers.is(CoreMatchers.instanceOf(String.class)));

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

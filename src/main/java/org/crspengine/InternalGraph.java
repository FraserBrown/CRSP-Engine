package org.crspengine;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;

import java.util.ArrayList;

public class InternalGraph {

    private ValueFactory vf;    // value factory just in case it's required
    private ArrayList<String> ns;   // list of namespaces for this graph
    private String observedAt;  // observed at meta data
    private Model graphData;    // graph data
    private String graphID; // graphID


    public InternalGraph() {
        this.ns = new ArrayList<String>();
        this.graphID = null;
        this.observedAt = null;
        this.graphData = null;
    }

    public InternalGraph(ArrayList<String> ns, String graphID, String observedAt, Model graphData) {
        this.ns = ns;
        this.observedAt = observedAt;
        this.graphData = graphData;
        this.graphID = graphID;
    }

    public void setObservedAt(String observedAt) {
        this.observedAt = observedAt;
    }

    public void setGraphData(Model graphData) {
        this.graphData = graphData;
    }

    public ArrayList<String> getNs() {
        return ns;
    }

    public void setGraphID(String graphID) {
        this.graphID = graphID;
    }


    public String getGraphID() {
        return graphID;
    }

    public String getObservedAt() {
        return observedAt;
    }

    public Model getGraphData() {
        return graphData;
    }
}

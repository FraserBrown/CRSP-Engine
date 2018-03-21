package org.crspengine;

import java.util.ArrayList;

public class InternalDataManager {
    private String queryString;
    private String jsonString;
    private ArrayList<InternalGraph> graphStream;
    private String queryResult;


    /* Constructor */
    public InternalDataManager() {
         this.queryString = null;
         this.jsonString = null;
         this.graphStream = new ArrayList<InternalGraph>();
         this.queryResult = null;
    }

    /* Getters */
    public String getQueryString() {
        return queryString;
    }
    public String getJsonString() {
        return jsonString;
    }
    public ArrayList<InternalGraph> getGraphStream() {
        return graphStream;
    }
    public String getQueryResult() {
        return queryResult;
    }

    /* Setters */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
    public void setGraphStream(ArrayList<InternalGraph> graphStream) {
        this.graphStream = graphStream;
    }
    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }
}

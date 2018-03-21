package org.crspengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class JsonRDFGraphParser {

    private String jsonStream;
    private JsonParser parser;
    private JsonElement jsonTree;
    private ArrayList<String> namespaces;


    /* Constructor */
    public JsonRDFGraphParser(String jsonStream) {
        this.jsonStream = jsonStream;
        this.parser = new JsonParser();
        this.jsonTree = null;
        this.namespaces = new ArrayList<String>();
    }

    /* Getters */
    public String getJsonStream() {
        return jsonStream;
    }
    public JsonParser getParser() {
        return parser;
    }
    public JsonElement getJsonTree() {
        return jsonTree;
    }
    public ArrayList<String> getNamespaces() {
        return namespaces;
    }

    /* Setters */
    public void setJsonStream(String jsonStream) {
        this.jsonStream = jsonStream;
    }
    public void setParser(JsonParser parser) {
        this.parser = parser;
    }
    public void setJsonTree(JsonElement jsonTree) {
        this.jsonTree = jsonTree;
    }
    public void setNamespaces(ArrayList<String> namespaces) {
        this.namespaces = namespaces;
    }


    /* Private helper functions */
    private ModelBuilder extractRDFData(JsonElement je, ModelBuilder builder, SimpleValueFactory vf){
        if (je.getAsJsonObject().get("@graph").isJsonArray()){
            JsonArray graphData = je.getAsJsonObject().get("@graph").getAsJsonArray();

            builder = new ModelBuilder();
            //iterate through graphData (subgraphs) building a model for each RDF tripple
            for (Iterator<JsonElement> j = graphData.iterator(); j.hasNext();){
                JsonElement rdf_tuple = j.next();

                //extract tuple information
                IRI subject = vf.createIRI(
                        this.getNamespaces().get(0),
                        rdf_tuple.getAsJsonObject().getAsJsonPrimitive("@id").getAsString());
                IRI predicate = vf.createIRI(this.getNamespaces().get(0),"hasTemp"); // TODO: generate predicate from namespaces currently possible
                Literal object = vf.createLiteral(rdf_tuple.getAsJsonObject().getAsJsonPrimitive("hasTemp").getAsString());

                //create graph tuple for this
                builder.defaultGraph().subject(subject).add(predicate, object);
            }
        }
        return builder;
    }

    private InternalGraph extractAndCreateInternalGraph(JsonElement je, ModelBuilder builder, SimpleValueFactory vf){
        // extract meta data from graph
        String graph_id = je.getAsJsonObject().getAsJsonPrimitive("@id").getAsString();
        String observedAt = je.getAsJsonObject().getAsJsonPrimitive("observedAt").getAsString();

        // is there more than one instance of RDF data?
        builder = extractRDFData(je, builder, vf);

        // build sub graph data into model
        Model model = builder.build();

        // create internal graph
        InternalGraph gr = new InternalGraph(this.getNamespaces(), graph_id, observedAt, model);

        return gr;
    }


    private void generateGraphStream(JsonObject jo, ArrayList<InternalGraph> graphStream){
        SimpleValueFactory vf = SimpleValueFactory.getInstance();
        ModelBuilder builder = new ModelBuilder();
        // look at graph streams and pull out either single json graph object or array of graphs
        if (jo.get("@graph").isJsonArray()){
            JsonArray graphs = jo.getAsJsonArray("@graph");

            // internalise the data from the json graph to internal graph
            for (Iterator<JsonElement> i = graphs.iterator(); i.hasNext();){
                JsonElement je = i.next();
                if (je.isJsonObject()){
                    //create and add internal graph to list graphStream
                    graphStream.add(extractAndCreateInternalGraph(je, builder, vf));
                }
            }
        } else if (jo.get("@graph").isJsonObject()) {
            JsonElement je = jo.getAsJsonObject("@graph");
            //create and add internal graph to list graphStream
            graphStream.add(extractAndCreateInternalGraph(je, builder, vf));

        }
    }

    /* Public Functions */
    public void parseJsonString(){
        this.setJsonTree(this.getParser().parse(this.getJsonStream()));
    }

    public void extractGraphContextFromJson(JsonObject jo){
        //Find @vocab primitive, eg. http://www.example.org/data-vocabulary#
        String namespace_ex = jo.getAsJsonObject("@context").getAsJsonPrimitive("@vocab").getAsString();
        //Make sure url ends with a /
        if(namespace_ex.matches(".*//.*/.*")) {
            //Substring to get the only the server name, eg. http://www.example.org/
            namespace_ex = namespace_ex.substring(0, StringUtils.ordinalIndexOf(namespace_ex, "/", 3)+1);
            //Substring to remove www. eg. http://example.org/
            namespace_ex = namespace_ex.replaceFirst(Pattern.quote("www."), "");
            this.getNamespaces().add(namespace_ex);
        } else {
            throw new java.lang.RuntimeException("The @vocab namespace must end with a /");
        }
    }

    public void jsonToInternalGraphStream(ArrayList<InternalGraph> graphStream) {
        // collect context metadata (namespaces and
        if (this.getJsonTree().isJsonObject()) {
            JsonObject jo = this.getJsonTree().getAsJsonObject();
            this.extractGraphContextFromJson(jo);
            this.generateGraphStream(jo, graphStream);
        }
    }

}

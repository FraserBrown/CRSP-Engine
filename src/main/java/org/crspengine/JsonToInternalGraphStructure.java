package org.crspengine;

import com.google.gson.*;

import org.apache.commons.lang.StringUtils;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class JsonToInternalGraphStructure {
    public static void main (String [] args){
        ArrayList<InternalGraph> graphStream = new ArrayList<InternalGraph>();
        ValueFactory vf = SimpleValueFactory.getInstance();
        ModelBuilder builder = new ModelBuilder();

        //gather JSON data
        String jsonStream = "" +
               "{\n" +
                "  \"@context\": {\n" +
                "    \"@vocab\": \"http://www.example.org/data-vocabulary#\",\n" +
                "    \"observedAt\": {\n" +
                "      \"@id\": \"http://www.w3.org/2005/Incubator/ssn/ssnx/ssn#observationSamplingTime\",\n" +
                "      \"@type\": \"http://www.w3.org/2001/XMLSchema#dateTime\"\n" +
                "    },\n" +
                "    \"hasTemp\": {\n" +
                "      \"@id\": \"http://example.org/property-vocabulary#hasPointTempC\",\n" +
                "      \"@type\": \"http://www.w3.org/2001/XMLSchema#decimal\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"@graph\": [\n" +
                "    {\n" +
                "      \"@id\": \"_:1\",\n" +
                "      \"observedAt\": \"2015-01-01T01:00:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"12.5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"8\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:2\",\n" +
                "      \"observedAt\": \"2015-01-01T01:01:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"12.5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.3\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:3\",\n" +
                "      \"observedAt\": \"2015-01-01T01:02:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"12.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.4\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:4\",\n" +
                "      \"observedAt\": \"2015-01-01T01:03:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"11.5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.5\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:5\",\n" +
                "      \"observedAt\": \"2015-01-01T01:04:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"11.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.6\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:6\",\n" +
                "      \"observedAt\": \"2015-01-01T01:05:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"10.5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.7\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:7\",\n" +
                "      \"observedAt\": \"2015-01-01T01:06:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"10.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.8\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:8\",\n" +
                "      \"observedAt\": \"2015-01-01T01:07:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"8.9\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:9\",\n" +
                "      \"observedAt\": \"2015-01-01T01:08:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"9.0\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"9.0\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"@id\": \"_:10\",\n" +
                "      \"observedAt\": \"2015-01-01T01:10:00\",\n" +
                "      \"@graph\": [\n" +
                "        {\n" +
                "          \"@id\": \"Berlin\",\n" +
                "          \"hasTemp\": \"8.5\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Madrid\",\n" +
                "          \"hasTemp\": \"9\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"@id\": \"Paris\",\n" +
                "          \"hasTemp\": \"9.1\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";


        /**
         * Parse Json Graph Stream into list of internal graph structures.
         */
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(jsonStream);

        if (jsonTree.isJsonObject()) {
            JsonObject jo = jsonTree.getAsJsonObject();

            JsonElement context = jo.getAsJsonObject("@context");
            
            //Find @vocab primitive, eg. http://www.example.org/data-vocabulary#
            String namespace_ex = jo.getAsJsonObject("@context").getAsJsonPrimitive("@vocab").getAsString();
            //Make sure url ends with a /
            if(namespace_ex.matches(".*//.*/.*")) {
	            //Substring to get the only the server name, eg. http://www.example.org/
	            namespace_ex = namespace_ex.substring(0, StringUtils.ordinalIndexOf(namespace_ex, "/", 3)+1);
	            //Substring to remove www. eg. http://example.org/
	            namespace_ex = namespace_ex.replaceFirst(Pattern.quote("www."), "");
            }
            else
            {
            	throw new java.lang.RuntimeException("The @vocab namespace must end with a /");
            }
            
            // look at graph streams and pull out either sing json graph object or array of graphs
            if (jo.get("@graph").isJsonArray()){
                JsonArray graphs = jo.getAsJsonArray("@graph");

                // internalise the data from the json graph to internal graph
                for (Iterator<JsonElement> i = graphs.iterator(); i.hasNext();){
                    JsonElement je = i.next();

                    if (je.isJsonObject()){
                        // extract meta data from graph
                        String graph_id = je.getAsJsonObject().getAsJsonPrimitive("@id").getAsString();
                        String observedAt = je.getAsJsonObject().getAsJsonPrimitive("observedAt").getAsString();

                        // is there more than one instance of RDF data?
                        if (je.getAsJsonObject().get("@graph").isJsonArray()){
                            JsonArray graphData = je.getAsJsonObject().get("@graph").getAsJsonArray();

                            //iterate through graphData (subgraphs) building a model for each RDF tripple
                            for (Iterator<JsonElement> j = graphData.iterator(); j.hasNext();){
                                JsonElement rdf_tuple = j.next();

                                //extract tuple information
                                IRI subject = vf.createIRI(
                                        namespace_ex,
                                        rdf_tuple.getAsJsonObject().getAsJsonPrimitive("@id").getAsString());
                                IRI predicate = vf.createIRI(namespace_ex,"hasTemp"); // TODO: generate predicate from namespaces currently possible
                                Literal object = vf.createLiteral(rdf_tuple.getAsJsonObject().getAsJsonPrimitive("hasTemp").getAsString());

                                //create graph tuple for this
                                builder.defaultGraph().subject(subject).add(predicate, object);
                            }
                        }

                        // build sub graph data into model
                        Model model = builder.build();

                        //create internal graph
                        InternalGraph gr = new InternalGraph();
                        gr.setObservedAt(observedAt);
                        gr.setGraphID(graph_id);
                        gr.setGraphData(model);

                        //add internal graph to list graphStream
                        graphStream.add(gr);
                    }
                }
            } else if (jo.get("@graph").isJsonObject()){
                JsonElement graphs = jo.getAsJsonObject("@graph");
            }
        }
//
//        System.out.print(graphStream);

        /**
         * Query Internal graph streams graphData.
         */
        // Create a new Repository. Here, we choose a database implementation
        // that simply stores everything in main memory.
        Repository db = new SailRepository(new MemoryStore());
        db.initialize();

        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            // add the graphData models to db
            for (Iterator<InternalGraph> i = graphStream.iterator(); i.hasNext(); ){
                InternalGraph g = i.next();
                conn.add(g.getGraphData());
            }


            // let's check that our data is actually in the database
            try (RepositoryResult<Statement> result =
                         conn.getStatements(null, null, null)) {
                while (result.hasNext()) {
                    Statement st = result.next();
                    //System.out.println("db contains: " + st);
                }
            }

            String queryString = "" +
                    "PREFIX ex: <http://example.org/> " +
                    "SELECT ?p ?o " +
        		    "FROM NAMED WINDOW :wind ON s:example [RANGE PT1H STEP PT1H] " +
                    "WHERE { " +
        		    "WINDOW :win { \n" +
                    "      ex:Paris ?p ?o" +
                    "}" +
                    "}";

            //System.out.println("\nQuery Running: " + queryString + "\n");

            TupleQuery query = conn.prepareTupleQuery(queryString);
            try (TupleQueryResult r = query.evaluate()){
                // we just iterate over all solutions in the result...
                System.out.println("Results:");
                BindingSet solution = null;
                Set<String> bindingNames;
                while (r.hasNext()) {
                  solution = r.next();
                  bindingNames = solution.getBindingNames();
                  System.out.println(bindingNames);
                  Iterator iterator = bindingNames.iterator();
                  String code;
                  while (iterator.hasNext())
                  {
                	  code = (String) iterator.next();
                	  System.out.println("?"+code+" = "+ solution.getValue(code)+"\n");
                  }
                    //System.out.println("?p = " + solution.getValue("p") + "\n" + "?o = " + solution.getValue("o") + "\n");
                }
            
            }catch (Exception e){
                System.err.println("ERROR");
            }
        }

    }
}

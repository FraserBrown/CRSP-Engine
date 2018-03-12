package org.crspengine;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.apache.commons.lang.StringUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class UserInterface {

	private JFrame mainFrame;
	private JPanel inputPanel;
	
	private JLabel lblJson;
	private JLabel lblQuery;
	private JLabel lblOutput;

	private JTextField tfJson;
	private JTextField tfQuery;
	private JTextField tfOutput;
	
	private static String JsonFileName = "";
	private static String QueryFileName = "";
	private static String OutputFileName = "";

	private JButton btnSubmit;
	
	public UserInterface()
	{
		prepareGUI();
	}
	
	private void prepareGUI() {
		mainFrame = new JFrame("QUERY APP");
		mainFrame.setSize(500, 400);
		mainFrame.setLayout(new GridLayout(2,1));
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3,2));

		lblJson = new JLabel("Json File Name : ");
		lblQuery = new JLabel("Query File Name : ");
		lblOutput = new JLabel("Output File Name : ");
		
		tfJson = new JTextField("test.json");
		tfQuery = new JTextField("query.txt");
		tfOutput = new JTextField("output.txt");
		
		btnSubmit = new JButton("Calculate");
		
		inputPanel.add(lblJson);
		inputPanel.add(tfJson);
		inputPanel.add(lblQuery);
		inputPanel.add(tfQuery);
		inputPanel.add(lblOutput);
		inputPanel.add(tfOutput);
		
		mainFrame.add(inputPanel);
		mainFrame.add(btnSubmit);

	    btnSubmit.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {     
	        	  JsonFileName = tfJson.getText();
	        	  QueryFileName = tfQuery.getText();
	        	  OutputFileName = tfOutput.getText();
	      		
	        	  String jsonStream = getText(JsonFileName);
	        	  String query = getText(QueryFileName);
	        	  String results = calculateQuery(jsonStream, query);
	        	  outputText(results, OutputFileName );
	    		
	          }
	       });
		
	}
	
	private String calculateQuery(String jsonStream, String queryString) {
		
		String queryResult = "";
        ArrayList<InternalGraph> graphStream = new ArrayList<InternalGraph>();
        ValueFactory vf = SimpleValueFactory.getInstance();
        ModelBuilder builder = new ModelBuilder();
        
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
			conn.add(new ModelBuilder().build()); //populate database with null model


            TupleQuery query = conn.prepareTupleQuery(queryString);

            // filter graph stream list based on window times

            for (Iterator<InternalGraph> i = graphStream.iterator(); i.hasNext(); ){
				InternalGraph g = i.next();
				conn.add(g.getGraphData());
			}

			// let's check that our data is actually in the database
			try (RepositoryResult<Statement> result =
						 conn.getStatements(null, null, null)) {
				while (result.hasNext()) {
					Statement st = result.next();
				}
			}

            try (TupleQueryResult r = query.evaluate()){
                // we just iterate over all solutions in the result...
                System.out.println("Results:");
                BindingSet solution = null;
                Set<String> bindingNames;
                while (r.hasNext()) {
                  solution = r.next();
                  bindingNames = solution.getBindingNames();
                  Iterator iterator = bindingNames.iterator();
                  String code;
                  while (iterator.hasNext())
                  {
                	  code = (String) iterator.next();
                	  queryResult = queryResult + "?"+code+" = "+ solution.getValue(code)+"\n";
                  }
                    //System.out.println("?p = " + solution.getValue("p") + "\n" + "?o = " + solution.getValue("o") + "\n");
                }
            
            }catch (Exception e){
                System.err.println("ERROR");
            }
        }
 
		return queryResult;
	}
	
	private void showUI() {
		mainFrame.setVisible(true);
	}
	
	private static String getText(String fileName) {
		
		String stringFile, line;
		stringFile = "";
		line = "";
		
		try {
			FileReader file = new FileReader(fileName);
			
			BufferedReader buffer = new BufferedReader(file);
		
			while ((line = buffer.readLine()) != null)
					{
					if (!stringFile.equals("")) {
						stringFile = stringFile + "\n " + line; 
						}
					else
						{
						stringFile = line;
						}
					}
			
			buffer.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		
		catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		
		return stringFile;
	}

	private static void outputText(String text, String fileName)

	{
		BufferedWriter output = null;
		FileWriter fw = null;
		File file = null;
		
		try {
		file = new File(fileName);
		fw = new FileWriter(file);
		output = new BufferedWriter(fw);
		output.write(text);
		
		System.out.println("Done");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try {
			if (output!= null)
			{
				output.close();
			}
			if(fw!=null)
			{
				fw.close();
			}
		} catch(IOException ex)
		{
			ex.printStackTrace();
		}

	}
	
	
	public static void main (String[] args) throws IOException {
		
		UserInterface UI = new UserInterface();
		UI.showUI();
		
	}

}


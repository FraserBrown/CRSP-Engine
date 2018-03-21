package org.crspengine;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

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

	private String calculateQuery(String jsonString, String queryString) {
		
		String queryResult = "";
		ArrayList<InternalGraph> graphStream = new ArrayList<InternalGraph>();

        //Create JsonRDFGraphParser object
        JsonRDFGraphParser jsonRDFGraphParser = new JsonRDFGraphParser(jsonString);

        // create internal json data structure from json string
        jsonRDFGraphParser.parseJsonString();

        // convert internal json tree into graph stream - arraylist of internalgraphs.
        jsonRDFGraphParser.jsonToInternalGraphStream(graphStream);
        

        /**
         * Query Internal graph streams graphData.
		 *
		 * create empty database
		 *
		 * parse query
		 *
		 * if windowing is used in query
		 * 	find next window over stream
		 * 		for graphs in graph stream
		 * 			if currentGraphTime <= RANGE && currentGraphTime <= STEP
		 * 				add graph to model
		 * 				remove graph from stream
		 *
		 *
         */
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
		output.append(text);
		
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


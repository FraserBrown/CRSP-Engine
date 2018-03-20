package org.crspengine;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.parser.sparql.ast.utility_files.LogicalWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.utility_files.StreamInfo;
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
        ValueFactory vf = SimpleValueFactory.getInstance();

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
			// add the graphData models to db
			conn.add(new ModelBuilder().build()); //populate database with null model


			SailTupleQuery query = (SailTupleQuery) conn.prepareTupleQuery(queryString);

			// filter graph stream list based on window times
			//This will be moved to another class after refactoring.

			//these are temp variables etc to see if it works
			int RANGE = 0;
			int ORIGINAL_RANGE = 0;
			int ORIGINAL_STEP = 0;
			int STEP = 0;
			int MAX_WINDOWS, NUM_WINDOWS;
			String RANGE_UNITS = null;
			String STEP_UNITS = null;
			ModelBuilder windowBuilder = null;
			Model windowModel = null;
			int boundaryCount = 0;
			long startStepTime = -1;
			long startRangeTime = -1;


			// while there is still a graph stream to query
			while (!graphStream.isEmpty()) {
				/** Calculate Window if required in the query */
				if (query.getParsedQuery().streamWindow != null) {
					conn.remove(QueryResults.asModel((conn.getStatements(null, null, null))));

					// initialise window variables
					for (Iterator<StreamInfo> sit = query.getParsedQuery().streamWindow.iterator(); sit.hasNext(); ) {
						StreamInfo si = sit.next();
						LogicalWindow w = (LogicalWindow) si.getWindow();

						RANGE = w.getRangeDescription().getValue();
						ORIGINAL_RANGE = w.getRangeDescription().getValue();
						STEP = w.getStepDescription().getValue();
						ORIGINAL_STEP = w.getStepDescription().getValue();
						RANGE_UNITS = w.getRangeDescription().getTimeUnit().toString().toUpperCase();
						STEP_UNITS = w.getStepDescription().getTimeUnit().toString().toUpperCase();

						NUM_WINDOWS = RANGE / STEP;

						System.out.println("Range: " + w.getRangeDescription().getValue()
								+ w.getRangeDescription().getTimeUnit()
								+ " Step: " + w.getStepDescription().getValue()
								+ w.getStepDescription().getTimeUnit()
						);
					}

					// for each graph in graph stream create window
					for (int i = 0; i < graphStream.size(); i++) {
						InternalGraph g = graphStream.get(i);
						// get graph time stamp
						String currentTimeStampStr = g.getObservedAt();
						int currentTimeStamp = 0;

						// set format of graph time stamp string
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

						// get string value required depending on RANGE_UNITS, convert it to int
						try {
							Date date = simpleDateFormat.parse(currentTimeStampStr);
							Calendar c1 = new GregorianCalendar();
							c1.setTime(date);
							long ct = c1.getTimeInMillis();

							long dStepTimeMili=0;
							long dRangeTimeMili=0;

							//we are at the start of a new window
							if (startRangeTime == -1){
								startRangeTime = c1.getTimeInMillis();
							}
							if (startStepTime == -1) {
								startStepTime = c1.getTimeInMillis();
								conn.add(g.getGraphData());
								graphStream.subList(0, i).clear();
							}else{

								/** TODO: THIS CAN BE A FUNCTION RATHER THAN 2 SWITCH STATEMENTS */
								//STEP difference calculations
								// The STEP_UNITS define where our window will stop
								// therefore we have to pull out that unit from our graph time
								// and then compare it to define the end of our window will end
								dStepTimeMili = (ct - startStepTime);
								switch(STEP_UNITS) {
									case "D":
										STEP = (ORIGINAL_STEP * 60 * 60 * 24) * 1000;
										break;
									case "H":
										STEP = (ORIGINAL_STEP * 60 * 60) * 1000;
										break;
									case "M":
										STEP = (ORIGINAL_STEP * 60) * 1000;
										break;
									case "S":
										STEP = (ORIGINAL_STEP * 1000);
										break;
								}
								//RANGE difference calculations
								// The RANGE_UNITS define where our final window will stop
								// therefore we have to pull out that unit from our graph time
								// and then compare it to define the end of our final window will end
								dRangeTimeMili = (ct - startRangeTime);
								switch(RANGE_UNITS) {
									case "D":
										RANGE = (ORIGINAL_RANGE * 60 * 60 * 24) * 1000;
										break;
									case "H":
										RANGE = (ORIGINAL_RANGE * 60 * 60) * 1000;
										break;
									case "M":
										RANGE = (ORIGINAL_RANGE * 60) * 1000;
										break;
									case "S":
										RANGE = (ORIGINAL_RANGE * 1000);
										break;
								}

								
								if (dRangeTimeMili == RANGE && dStepTimeMili <= STEP ){
									//end of window, add window limit graphGraph to database
									conn.add(g.getGraphData());

									// remove current window from stream
									graphStream.subList(0, graphStream.size()).clear();

									// reset starting step time
									startStepTime = -1;
									startRangeTime = -1;
									break;
								}
								//we are at the max range
								else if (dRangeTimeMili > RANGE){
									//end of window, add window limit graphGraph to database
									//conn.add(g.getGraphData());
									//System.out.println("New Window");

									// remove current window from stream
									graphStream.subList(0, graphStream.size()).clear();

									// reset starting step time
									startStepTime = -1;
									startRangeTime = -1;
									break;
								} else if (dStepTimeMili == STEP) { //end of the current window
									//end of window, add window limit graphGraph to database
									conn.add(g.getGraphData());
									System.out.println("New Window");

									// remove current window from stream
									graphStream.subList(0, i+1).clear();

									// reset starting step time
									startStepTime = -1;				
									break;
								} else if (dStepTimeMili >= STEP) { //outside of the current window
									// remove current window from stream
									graphStream.subList(0, i).clear();
									
									// reset starting step time
									startStepTime = -1;
									break;
								} else if (graphStream.size()-1 == i){	//end of the graph stream
									conn.add(g.getGraphData());
									System.out.println("New Window");

									// remove current window from stream
									graphStream.subList(0, graphStream.size()).clear();

									// reset starting step time
									startStepTime = -1;
									startRangeTime = -1;
									break;
								} else {
									// graph within window add to database
									conn.add(g.getGraphData());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {    // run query over whole graph stream, add all graphData to database
					for (Iterator<InternalGraph> i = graphStream.iterator(); i.hasNext(); ) {
						InternalGraph g = i.next();
						conn.add(g.getGraphData());
					}
				}


				// check that our data is actually in the database
				if (!(conn.getStatements(null, null, null).hasNext())) {
					System.err.println("Error: Empty Database, cannot proceed with query execution");
				}

				try (TupleQueryResult r = query.evaluate()) {
					// we just iterate over all solutions in the result...
					System.out.println("Results:");
					BindingSet solution = null;
					Set<String> bindingNames;
					while (r.hasNext()) {
						solution = r.next();
						bindingNames = solution.getBindingNames();
						Iterator iterator = bindingNames.iterator();
						String code;
						while (iterator.hasNext()) {
							code = (String) iterator.next();
							queryResult = queryResult + "?" + code + " = " + solution.getValue(code) + "\n";
						}
						//System.out.println("?p = " + solution.getValue("p") + "\n" + "?o = " + solution.getValue("o") + "\n");
					}
					queryResult = queryResult + "\n-----\n";    // window output divider in output file

				} catch (Exception e) {
					System.err.println("ERROR");
				}
			}
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


package org.crspengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JTextArea;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTextPane;
import java.awt.ScrollPane;

public class UserInterface {

	//Declare the GUI elements

	//Declare the frame of the GUI
	private JFrame frame;
	
	//Declare the text fields that store the name of the json file, possible query text file and output text file
	private JTextField queryOutputTextField;
	private JTextField jsonInputTextField;
	private JTextField queryInputTextField;
	
	//Declare the necessary labels
	private JLabel lblQueryResults;
	private JLabel lblQueryOutputFile;
	private JLabel lblJson;
	
	//Declare the radio buttons for selecting the input mode for the query (load from text file or input it directly)
	private JRadioButton rdbtnQueryFile;
	private JRadioButton rdbtnQueryString;
	
	//Declare the radio buttons' group
	private ButtonGroup querySource = new ButtonGroup();
	
	//Declare the "Process Query" button
	private JButton btnNewButton;
	
	//Define variables for query input window
	private ScrollPane queryInputScrollPane;
	private JTextPane queryInputTextPane;
	
	//Define variables for query output window.
	private ScrollPane queryResultsScrollPane;
	private JTextPane queryResultsTextPane;

	//Define control variable to check what input source the user is using (the text file or directly inputting the query)
	private int querySourceID = 2;

	//Constructor for the interface. Initializes it and sets it visible.
	public UserInterface(CRSPEngine cre) {
		initialize(cre);
		frame.setVisible(true);
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize(CRSPEngine cre) {
		
		//Initialize the main frame and set its parameteres
		frame = new JFrame();
		frame.setBounds(100, 100, 347, 803);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Initialize the jSon label and add it to the frame.
		lblJson = new JLabel("JSon");
		lblJson.setBounds(22, 11, 109, 24);
		frame.getContentPane().add(lblJson);
		
		//Initialize the 2 radio buttons and add them to the frame
		rdbtnQueryFile = new JRadioButton("Query File");
		rdbtnQueryFile.setBounds(22, 53, 109, 24);
		frame.getContentPane().add(rdbtnQueryFile);
		
		rdbtnQueryString = new JRadioButton("Query String");
		rdbtnQueryString.setBounds(22, 91, 109, 23);
		frame.getContentPane().add(rdbtnQueryString);
		
		//Add the 2 radio buttons to the same button group
		querySource.add(rdbtnQueryFile);
		querySource.add(rdbtnQueryString);
		
		//Initialize 2 listeners to check when the state of a radio button changes and update the input fields accordingly
		//Also update the variable that stores what input source the user selected last for the query
		//(for example if you select "Query File" you won't be able to write a query, but need to use a query file.
		rdbtnQueryFile.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent event) {
						int state = event.getStateChange();
						if (state == ItemEvent.SELECTED)
						{
							queryInputTextField.setEditable(true);
							querySourceID = 0;
	
						}
						else if (state == ItemEvent.DESELECTED)
						{
							queryInputTextField.setEditable(false);
							querySourceID = 1;
							
						}
					}
			
				});
		
		rdbtnQueryString.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent event) {
				int state = event.getStateChange();
				if (state == ItemEvent.SELECTED)
				{
					queryInputTextPane.setEditable(true);
					queryInputTextPane.setOpaque(true);
					querySourceID = 1;
				}
				else if (state == ItemEvent.DESELECTED)
				{
					queryInputTextPane.setEditable(false);
					queryInputTextPane.setOpaque(false);
					querySourceID = 0;
				}
			}
	
		});
		
		
		//Initialize the label for the output file and add it to the frame
		lblQueryOutputFile = new JLabel("Query Output File");
		lblQueryOutputFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblQueryOutputFile.setBounds(22, 319, 109, 21);
		frame.getContentPane().add(lblQueryOutputFile);
		
		//Initialize the text field for the json file input	and add it to the frame	
		jsonInputTextField = new JTextField();
		jsonInputTextField.setColumns(10);
		jsonInputTextField.setBounds(141, 13, 169, 24);
		frame.getContentPane().add(jsonInputTextField);
		jsonInputTextField.setText("test.json");

		//Initialize the text field for the query file input and add it to the frame. Also initially make the text field not editable
		queryInputTextField = new JTextField();
		queryInputTextField.setText("query.txt");
		queryInputTextField.setColumns(10);
		queryInputTextField.setBounds(141, 53, 169, 24);
		frame.getContentPane().add(queryInputTextField);
		queryInputTextField.setEditable(false);
		
		//Initialize the text field for the query output file and add it to the frame
		queryOutputTextField = new JTextField();
		queryOutputTextField.setBounds(141, 316, 169, 24);
		frame.getContentPane().add(queryOutputTextField);
		queryOutputTextField.setColumns(10);
		queryOutputTextField.setText("output.txt");
		
		//Initialize the button "Process Query", and add it to the frame
		btnNewButton = new JButton("Process Query");
		btnNewButton.setBounds(22, 362, 288, 24);
		frame.getContentPane().add(btnNewButton);
		
		//Initialize the "Query Results" label and add it to the frame
		lblQueryResults = new JLabel("Query Results");
		lblQueryResults.setFont(new Font("Lato", Font.PLAIN, 15));
		lblQueryResults.setBounds(22, 397, 288, 14);
		frame.getContentPane().add(lblQueryResults);
		
		//Initialize the "Query Results" scroll pane and add it to the frame
		queryResultsScrollPane = new ScrollPane();
		queryResultsScrollPane.setBounds(22, 417, 288, 324);
		frame.getContentPane().add(queryResultsScrollPane);
		
		//Initialize the "Query Results" text pane and add it to the scroll pane. This way the text pane becomes scrollable. Also make it not editable
		queryResultsTextPane = new JTextPane();
		queryResultsTextPane.setBounds(32, 422, 269, 308);
		queryResultsScrollPane.add(queryResultsTextPane);
  	  	queryResultsTextPane.setEditable(false);
		
		//Initialize the "Query Input" scroll pane and add it to the frame
		queryInputScrollPane = new ScrollPane();
		queryInputScrollPane.setBounds(22, 120, 288, 190);
		frame.getContentPane().add(queryInputScrollPane);
		
		//Initialize the "Query Input" text pane and add it to the scroll pane. This way the text pane becomes scrollable.
		//Also set the initial "Query Input" text pane to not editable and not opaque
		queryInputTextPane = new JTextPane();
		queryInputTextPane.setBounds(22, 121, 288, 187);
		queryInputTextPane.setEditable(false);
		queryInputTextPane.setOpaque(false);
		queryInputScrollPane.add(queryInputTextPane);
		
		
		//Create an action listener that responds when the button "Process Query" is pressed.
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  //Record the input given to the UI thus far
		    	  String JsonFileName = jsonInputTextField.getText();
		    	  String OutputFileName = queryOutputTextField.getText();
	        	  
		    	  //Initialize the initial query with "" and results string with "QUERY IS NOT VALID"
	        	  String query = "", results = "QUERY IS NOT VALID";
	        	  
	        	  //See what the source of the input was
	        	  if (querySourceID == 0)
	        		  query = getText(queryInputTextField.getText());
	        	  else if (querySourceID == 1)
	        		  query = queryInputTextPane.getText();
	        	   
	        	  //If input was received, attempt to process the query
	        	   if (!query.equals("")) {
		        	  //get input from gui, set it in crsp engine data manager
		        	  cre.getIdm().setJsonString(getText(JsonFileName));
		        	  cre.getIdm().setQueryString(query);
		        	  //run program/ CRSP engine logic
					  cre.run();
		        	  //store the answer in results
					  results = cre.getIdm().getQueryResult();
	        	   }
	        	  
	        	  //Output the result to the given output text file and to the GUI text pane.
	        	  outputText(results, OutputFileName);
	        	  queryResultsTextPane.setText(results);
	        	  }
		});
	}
	
	
	//Method that returns a String containing the text from the file fileName
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
	
	
	//Method that outputs the given String text to a file with the name fileName
	private static void outputText(String text, String fileName){
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
	
}

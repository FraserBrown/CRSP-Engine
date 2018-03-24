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

	private JFrame frame;
	private JTextField queryOutputTextField;
	private JTextField jsonInputTextField;
	private JTextField queryInputTextField;
	private JLabel lblQueryResults;
	private JLabel lblQueryOutputFile;
	private JLabel lblJson;
	private JRadioButton rdbtnQueryFile;
	private JRadioButton rdbtnQueryString;
	private ButtonGroup querySource = new ButtonGroup();
	private JButton btnNewButton;
	private static String JsonFileName = "";
	private static String QueryFileName = "";
	private static String OutputFileName = "";
	private ScrollPane scrollPane;
	private JTextPane textPane;
	private ScrollPane scrollPane_1;
	private JTextPane textPane_1;
	private int querySourceID = 2;
	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public UserInterface(CRSPEngine cre) {
		initialize(cre);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(CRSPEngine cre) {
		frame = new JFrame();
		frame.setBounds(100, 100, 347, 803);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblJson = new JLabel("JSon");
		lblJson.setBounds(22, 11, 109, 24);
		frame.getContentPane().add(lblJson);
		
		rdbtnQueryFile = new JRadioButton("Query File");
		rdbtnQueryFile.setBounds(22, 53, 109, 24);
		frame.getContentPane().add(rdbtnQueryFile);
		
		rdbtnQueryString = new JRadioButton("Query String");
		rdbtnQueryString.setBounds(22, 91, 109, 23);
		frame.getContentPane().add(rdbtnQueryString);
		
		querySource.add(rdbtnQueryFile);
		querySource.add(rdbtnQueryString);
		
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
					textPane_1.setEditable(true);
					textPane_1.setOpaque(true);
					querySourceID = 1;
				}
				else if (state == ItemEvent.DESELECTED)
				{
					textPane_1.setEditable(false);
					textPane_1.setOpaque(false);
					querySourceID = 0;
				}
			}
	
		});
		
		
		
		lblQueryOutputFile = new JLabel("Query Output File");
		lblQueryOutputFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblQueryOutputFile.setBounds(22, 319, 109, 21);
		frame.getContentPane().add(lblQueryOutputFile);
				
		jsonInputTextField = new JTextField();
		jsonInputTextField.setColumns(10);
		jsonInputTextField.setBounds(141, 13, 169, 24);
		frame.getContentPane().add(jsonInputTextField);
		jsonInputTextField.setText("test.json");

		queryInputTextField = new JTextField();
		queryInputTextField.setText("query.txt");
		queryInputTextField.setColumns(10);
		queryInputTextField.setBounds(141, 53, 169, 24);
		frame.getContentPane().add(queryInputTextField);
		queryInputTextField.setEditable(false);
		
		queryOutputTextField = new JTextField();
		queryOutputTextField.setBounds(141, 316, 169, 24);
		frame.getContentPane().add(queryOutputTextField);
		queryOutputTextField.setColumns(10);
		queryOutputTextField.setText("output.txt");
		
		btnNewButton = new JButton("Process Query");
		btnNewButton.setBounds(22, 362, 288, 24);
		frame.getContentPane().add(btnNewButton);
		
		lblQueryResults = new JLabel("Query Results");
		lblQueryResults.setFont(new Font("Lato", Font.PLAIN, 15));
		lblQueryResults.setBounds(22, 397, 288, 14);
		frame.getContentPane().add(lblQueryResults);
		
		scrollPane = new ScrollPane();
		scrollPane.setBounds(22, 417, 288, 324);
		frame.getContentPane().add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setBounds(32, 422, 269, 308);
		scrollPane.add(textPane);
		
		scrollPane_1 = new ScrollPane();
		scrollPane_1.setBounds(22, 120, 288, 190);
		frame.getContentPane().add(scrollPane_1);
		
		textPane_1 = new JTextPane();
		textPane_1.setBounds(22, 121, 288, 187);
		textPane_1.setEditable(false);
		textPane_1.setOpaque(false);
		scrollPane_1.add(textPane_1);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	  JsonFileName = jsonInputTextField.getText();
		    	  OutputFileName = queryOutputTextField.getText();
	        	  
	        	  String query = "", results = "QUERY IS NOT VALID";
	        	  
	        	  if (querySourceID == 0)
	        		  query = getText(queryInputTextField.getText());
	        	  else if (querySourceID == 1)
	        		  query = textPane_1.getText();
	        	         	  
	        	   if (!query.equals("")) {
		        	  //get input from gui, set it in crsp engine data manager
		        	  cre.getIdm().setJsonString(getText(JsonFileName));
		        	  cre.getIdm().setQueryString(query);
	
		        	  //run program/ CRSP engine logic
					  cre.run();
	
		        	  //output answers from CRSPEngine data manager to user files
					  results = cre.getIdm().getQueryResult();
				  
	        	   }
	        	  outputText(results, OutputFileName);
	        	  textPane.setText(results);
	        	  textPane.setEditable(false);
	        	  }
		});
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
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}

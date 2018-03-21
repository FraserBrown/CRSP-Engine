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

	/* Constructor */
	public UserInterface(CRSPEngine cre){
		prepareGUI(cre);
	}

	/* Private helper functions */
	private void prepareGUI(CRSPEngine cre) {
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

	        	  //get input from gui, set it in crsp engine data manager
	        	  cre.getIdm().setJsonString(getText(JsonFileName));
	        	  cre.getIdm().setQueryString(getText(QueryFileName));

	        	  //run program/ CRSP engine logic
				  cre.run();

	        	  //output answers from CRSPEngine data manager to user files
				  String results = cre.getIdm().getQueryResult();
	        	  outputText(results, OutputFileName);
	    		
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

	/* Public Funcitons */
	public void showUI() {
		mainFrame.setVisible(true);
	}

}


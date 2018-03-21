package org.crspengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.parser.sparql.ast.utility_files.LogicalWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.utility_files.StreamInfo;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailTupleQuery;

public class RSPQLQueryEvaluator {
	
	private int RANGE = 0;
	private int ORIGINAL_RANGE = 0;
	private int STEP = 0;
	private int ORIGINAL_STEP = 0;
	private String RANGE_UNITS = null;
	private String STEP_UNITS = null;
	private long startStepTime = -1;
	private long startRangeTime = -1;
	
	private String queryResult = "";
	
    /* Constructor */
	public RSPQLQueryEvaluator() {
	}

    /* Private helper functions */
	private int unitCalculations(int newValue, int originalValue, String unitString) {
		switch(unitString) {
		case "D":
			newValue = (originalValue * 60 * 60 * 24) * 1000;
			break;
		case "H":
			newValue = (originalValue * 60 * 60) * 1000;
			break;
		case "M":
			newValue = (originalValue * 60) * 1000;
			break;
		case "S":
			newValue = (originalValue * 1000);
			break;
		}
		return newValue;
	}
	
    /* Public Functions */
	public String evaluteQuery(RepositoryConnection conn, SailTupleQuery query, ArrayList<InternalGraph> graphStream) {
		// while there is still a graph stream to query
		while (!graphStream.isEmpty()) {
			/** Calculate Window if required in the query */
			if (query.getParsedQuery().streamWindow != null) {
				conn.remove(QueryResults.asModel((conn.getStatements(null, null, null))));

				// Initialize window variables
				for (Iterator<StreamInfo> sit = query.getParsedQuery().streamWindow.iterator(); sit.hasNext(); ) {
					StreamInfo si = sit.next();
					LogicalWindow w = (LogicalWindow) si.getWindow();

					RANGE = w.getRangeDescription().getValue();
					ORIGINAL_RANGE = w.getRangeDescription().getValue();
					STEP = w.getStepDescription().getValue();
					ORIGINAL_STEP = w.getStepDescription().getValue();
					RANGE_UNITS = w.getRangeDescription().getTimeUnit().toString().toUpperCase();
					STEP_UNITS = w.getStepDescription().getTimeUnit().toString().toUpperCase();

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
						} else {
							//STEP difference calculations
							// The STEP_UNITS define where our current window will stop
							// therefore we have to pull out that unit from our graph time
							// and then compare it to the query window to define the end of our current window
							dStepTimeMili = (ct - startStepTime);
							STEP = unitCalculations(STEP, ORIGINAL_STEP, STEP_UNITS);
							
							//RANGE difference calculations
							// The RANGE_UNITS define where our final window will stop
							// therefore we have to pull out that unit from our graph time
							// and then compare it to the query window to define the end of our final window
							dRangeTimeMili = (ct - startRangeTime);
							RANGE = unitCalculations(RANGE, ORIGINAL_RANGE, RANGE_UNITS);
							
							if (dRangeTimeMili == RANGE && dStepTimeMili <= STEP ){
								//end of window, add window limit graphGraph to database
								conn.add(g.getGraphData());

								// remove current window from stream
								graphStream.subList(0, graphStream.size()).clear();

								// reset starting step time
								startStepTime = -1;
								startRangeTime = -1;
								break;
							} else if (dRangeTimeMili > RANGE) { //we are at the max range
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
					Iterator<String> iterator = bindingNames.iterator();
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
		return queryResult;
	}
}

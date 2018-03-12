/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.query.parser.sparql;

import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTFromNamedWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTLogicalWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTOperationContainer;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTPhysicalWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTString;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTTimeUnit;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTWindow;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTWindowInWhereClause;
import org.eclipse.rdf4j.query.parser.sparql.ast.ASTWindowOverlap;
import org.eclipse.rdf4j.query.parser.sparql.ast.VisitorException;

/**
 * Processes escape sequences in strings, replacing the escape sequence with their actual value. Escape
 * sequences for SPARQL are documented in section
 * <a href="http://www.w3.org/TR/rdf-sparql-query/#grammarEscapes">A.7 Escape sequences in strings</a>.
 * 
 * @author Arjohn Kampman
 */
public class StringEscapesProcessor {

	/**
	 * Processes escape sequences in ASTString objects.
	 * 
	 * @param qc
	 *        The query that needs to be processed.
	 * @throws MalformedQueryException
	 *         If an invalid escape sequence was found.
	 */
	public static void process(ASTOperationContainer qc)
		throws MalformedQueryException
	{
		StringProcessor visitor = new StringProcessor();
		try {
			qc.jjtAccept(visitor, null);
		}
		catch (VisitorException e) {
			throw new MalformedQueryException(e);
		}
	}

	private static class StringProcessor extends AbstractASTVisitor {

		public StringProcessor() {
		}

		@Override
		public Object visit(ASTString stringNode, Object data)
			throws VisitorException
		{
			String value = stringNode.getValue();
			try {
				value = SPARQLUtil.decodeString(value);
				stringNode.setValue(value);
			}
			catch (IllegalArgumentException e) {
				// Invalid escape sequence
				throw new VisitorException(e.getMessage());
			}

			return super.visit(stringNode, data);
		}

		@Override
		public Object visit(ASTFromNamedWindow node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTWindow node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTTimeUnit node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTWindowOverlap node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTLogicalWindow node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTPhysicalWindow node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object visit(ASTWindowInWhereClause node, Object data) throws VisitorException {
			// TODO Auto-generated method stub
			return null;
		}
	}
}

/* Generated By:JJTree: Do not edit this line. ASTWindowInWhereClause.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.eclipse.rdf4j.query.parser.sparql.ast;

public
class ASTWindowInWhereClause extends SimpleNode {
  public ASTWindowInWhereClause(int id) {
    super(id);
  }

  public ASTWindowInWhereClause(SyntaxTreeBuilder p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SyntaxTreeBuilderVisitor visitor, Object data) throws VisitorException {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ece922002712c804b5ac8535e93295a4 (do not edit this line) */

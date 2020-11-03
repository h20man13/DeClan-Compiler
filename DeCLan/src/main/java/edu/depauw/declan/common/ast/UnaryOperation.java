package edu.depauw.declan.common.ast;

import java.lang.String;
import java.lang.StringBuilder;

import edu.depauw.declan.common.Position;

/**
 * An ASTNode representing a unary operation (+ or -, currently), with a single
 * subexpression and an operator.
 * 
 * @author bhoward
 */
public class UnaryOperation extends AbstractASTNode implements Expression {
	private final OpType operator;
	private final Expression expression;

	/**
	 * Construct a UnaryOperation ast node starting at the given source Position,
	 * with the specified subexpression and operator type.
	 * 
	 * @param start
	 * @param operator
	 * @param expression
	 */
	public UnaryOperation(Position start, OpType operator, Expression expression) {
		super(start);
		this.operator = operator;
		this.expression = expression;
	}

	public OpType getOperator() {
		return operator;
	}

	public Expression getExpression() {
		return expression;
	}

        @Override
	public String toString(){
	  StringBuilder mystring = new StringBuilder();
	  mystring.append("( ");
	  mystring.append(opToString(getOperator()));
	  mystring.append("( ");
	  mystring.append(getExpression().toString());
	  mystring.append(" ))");
	}
  
	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public <R> R acceptResult(ExpressionVisitor<R> visitor) {
		return visitor.visitResult(this);
	}

	/**
	 * Define the allowable unary operators. Not to be confused with the
	 * similarly-named TokenTypes.
	 * 
	 * @author bhoward
	 */
	public enum OpType {
	    PLUS, MINUS, NOT
	}

        private String opToString(OpType op){
	  if(op == PLUS){
	    return "+";
	  } else if (op == MINUS){
	    return "-";
	  } else if (op == NOT){
	    return "!";
	  } else {
	    FATAL("Error Unary OpType Not Found");
	    return null;
	  }
        }
}

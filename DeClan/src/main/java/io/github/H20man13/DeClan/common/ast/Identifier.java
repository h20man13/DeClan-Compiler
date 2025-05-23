package io.github.H20man13.DeClan.common.ast;

import io.github.H20man13.DeClan.common.position.Position;

/**
 * Abn ASTNode representing an identifier (variable, type, or procedure name).
 * 
 * @author bhoward
 */
public class Identifier extends AbstractASTNode implements Expression {
	private final String lexeme;

	/**
	 * Construct an Identifier ast node starting at the given source Position, with
	 * the specified lexeme giving the name of the identifier.
	 * 
	 * @param start
	 * @param lexeme
	 */
	public Identifier(Position start, String lexeme) {
		super(start);
		this.lexeme = lexeme;
	}

	public String getLexeme() {
		return lexeme;
	}

        @Override
        public String toString(){
	  return getLexeme();
        }

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public <R> R acceptResult(ExpressionVisitor<R> visitor) {
		return visitor.visitResult(this);
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean containsIdentifier(String ident) {
		return lexeme.equals(ident);
	}
}

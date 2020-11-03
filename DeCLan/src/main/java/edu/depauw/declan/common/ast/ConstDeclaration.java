package edu.depauw.declan.common.ast;

import edu.depauw.declan.common.Position;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * An ASTNode representing a CONST declaration. It contains the Identifier being
 * declared plus the constant value (currently just a NumValue) being bound to
 * it.
 * 
 * @author bhoward
 */
public class ConstDeclaration extends AbstractASTNode implements Declaration {
	private final Identifier identifier;
	private final NumValue number;

	/**
	 * Construct a ConstDecl ast node starting at the given source position, with
	 * the specified Identifier and NumValue.
	 * 
	 * @param start
	 * @param identifier
	 * @param number
	 */
	public ConstDeclaration(Position start, Identifier identifier, NumValue number) {
		super(start);
		this.identifier = identifier;
		this.number = number;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public NumValue getNumber() {
		return number;
	}

        @Override
	public String toString(){
	  StringBuilder mystring = new StringBuilder();
	  mystring.append("const ");
	  mystring.append(identifier.toString());
	  mystring.append(" = ");
	  mystring.append(number.toString());
	  mystring.append("\n");
	}
  
        @Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
	}
        @Override
	public <R> R acceptResult(DeclarationVisitor<R> visitor) {
		return visitor.visitResult(this);
	}
}

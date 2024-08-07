package io.github.H20man13.DeClan.common.ast;

/**
 * This serves as a common supertype for all of the declaration-like ASTNode
 * classes.
 * 
 * @author bhoward
 */
public interface Declaration {
	/**
	 * Accept a visitor to this node, according to the Visitor pattern, and return a
	 * result of type R. Each implementing class will dispatch to the appropriate
	 * overloaded visit method of the visitor, passing a reference to itself. Note
	 * that in this version, each visitor is responsible for deciding when to visit
	 * subnodes.
	 * 
	 * @param visitor
	 */

    void accept(ASTVisitor visitor);
    <R> R acceptResult(DeclarationVisitor<R> visitor);
    
}

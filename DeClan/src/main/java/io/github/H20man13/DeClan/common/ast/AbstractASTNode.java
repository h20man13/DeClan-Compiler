package io.github.H20man13.DeClan.common.ast;

import io.github.H20man13.DeClan.common.position.Position;

/**
 * Default implementation of ASTNode that keeps track of a starting Position.
 * 
 * @author bhoward
 */
public abstract class AbstractASTNode implements ASTNode {
	private final Position start;

	public AbstractASTNode(Position start) {
		this.start = start;
	}

	@Override
	public Position getStart() {
		return start;
	}

	@Override
	public abstract String toString();
}

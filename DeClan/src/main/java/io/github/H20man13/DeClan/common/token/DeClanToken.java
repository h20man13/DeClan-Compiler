package io.github.H20man13.DeClan.common.token;

import java.util.Objects;

import io.github.H20man13.DeClan.common.Position;

/**
 * A Token represents one lexical unit of a DeCLan source program. A Token
 * object stores a position (line and column numbers, each starting from 1), a
 * TokenType, and a lexeme (string value -- for the fixed tokens, this would be
 * redundant and should be null, but for identifiers and numbers it specifies
 * which particular one it is).
 * 
 * @author bhoward
 */
public class DeClanToken {
	private final DeClanTokenType type;
	private final String lexeme;
	private final Position position;

	/**
	 * Construct a Token object given its components. This is package-private;
	 * tokens should be created using the Token static factory methods.
	 * 
	 * @param line   the line number (starting from 1) where the token was found
	 * @param column the column number (starting from 1) where the token started
	 * @param type   the TokenType of the token
	 * @param lexeme the string value of the token
	 */
	public DeClanToken(Position position, DeClanTokenType type, String lexeme) {
		this.position = position;
		this.type = type;
		this.lexeme = lexeme;
	}

	public DeClanTokenType getType() {
		return type;
	}

	public String getLexeme() {
		return lexeme;
	}

	public Position getPosition() {
		return position;
	}

	// Override the default toString(), hashCode(), and equals() for use in
	// development and debugging.
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(type.toString());
		if (lexeme != null) {
			result.append(" ").append(lexeme);
		}
		result.append(" ").append(position);
		return result.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(lexeme, position, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeClanToken other = (DeClanToken) obj;
		return Objects.equals(lexeme, other.lexeme) && Objects.equals(position, other.position) && type == other.type;
	}

	// Static factory methods
	
	/**
	 * Create a Token for a string literal. The lexeme is just the contents of the
	 * string (without surrounding quotes).
	 * 
	 * @param lexeme
	 * @param line
	 * @param column
	 * @return
	 */
	public static DeClanToken createString(String lexeme, Position position) {
		return new DeClanToken(position, DeClanTokenType.STRING, lexeme);
	}

	/**
	 * Create a Token for a numeric literal.
	 * 
	 * @param lexeme
	 * @param line
	 * @param column
	 * @return
	 */
	public static DeClanToken createNum(String lexeme, Position position) {
		return new DeClanToken(position, DeClanTokenType.NUM, lexeme);
	}

	/**
	 * Create a Token that looks like an identifier. If the lexeme matches one of
	 * the reserved words, create the corresponding keyword token instead.
	 * 
	 * @param lexeme
	 * @param line
	 * @param column
	 * @return
	 */
	public static DeClanToken createId(String lexeme, Position position) {
		if (DeClanTokenType.reserved.containsKey(lexeme)) {
			return new DeClanToken(position, DeClanTokenType.reserved.get(lexeme), lexeme);
		} else {
			return new DeClanToken(position, DeClanTokenType.ID, lexeme);
		}
	}

	/**
	 * Create a Token of a type where the lexeme is always the same.
	 * 
	 * @param type
	 * @param line
	 * @param column
	 * @return
	 */
	public static DeClanToken create(DeClanTokenType type, Position position) {
		return new DeClanToken(position, type, null);
	}
}

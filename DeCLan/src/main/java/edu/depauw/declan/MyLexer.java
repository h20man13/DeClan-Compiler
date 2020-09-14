package edu.depauw.declan;

import java.util.NoSuchElementException;

import edu.depauw.declan.common.Lexer;
import edu.depauw.declan.common.Position;
import edu.depauw.declan.common.Source;
import edu.depauw.declan.common.Token;
import edu.depauw.declan.common.TokenFactory;
import edu.depauw.declan.common.TokenType;
import edu.depauw.declan.common.MyIO;

public class MyLexer implements Lexer {
	private Source source;
	private TokenFactory tokenFactory;
	private Token nextToken;

	public MyLexer(Source source, TokenFactory tokenFactory) {
		this.source = source;
		this.tokenFactory = tokenFactory;
		this.nextToken = null;
	}

	public boolean hasNext() {
		if (nextToken == null) {
			scanNext();
		}

		return nextToken != null;
	}

	public Token next() {
		if (nextToken == null) {
			scanNext();
		}

		if (nextToken == null) {
			throw new NoSuchElementException("No more tokens");
		}

		Token result = nextToken;
		nextToken = null;
		return result;
	}

	public void close() {
		source.close();
	}

	private static enum State {
		INIT, IDENT, COLON
		// TODO add more states here
	}

	/**
	 * Scan through characters from source, starting with the current one, to find
	 * the next token. If found, store it in nextToken and leave the source on the
	 * next character after the token. If no token found, set nextToken to null.
	 */
	private void scanNext() {
		State state = State.INIT;
		StringBuilder lexeme = new StringBuilder();
		Position position = null;

		while (!source.atEOF()) {
			char c = source.current();
			switch (state) {
			case INIT:
				// Look for the start of a token
				if (Character.isWhitespace(c)) {
					source.advance();
					continue;
				} else if (Character.isUpperCase(c)){
				        state = State.KEYWORD;
					lexeme.append(c);
					position = source.getPosition();
					source.advance();
				} else if (Character.isLetter(c)){
					state = State.IDENT;
					lexeme.append(c);
					// Record starting position of identifier or keyword token
					position = source.getPosition();
					source.advance();
					continue;
				} else if (c == ':') {
					state = State.COLON;
					position = source.getPosition();
					source.advance();
					continue;
				} else if (c == '=') {
					position = source.getPosition();
					source.advance();
					nextToken = tokenFactory.makeToken(TokenType.EQ, position);
					return;
				} else {
					// TODO handle other characters here
					position = source.getPosition();
					ERROR("Unrecognized character " + c + " at " + position);
					source.advance();
					continue;
				}
			case KEYWORD:
			    if(Character.isUpperCase(c)){
				lexeme.append(c);
				source.advance();
			    } else if(Character.isLetterOrDigit(c)) {
				state = State.IDENT;
				source.advance();
				continue;
			    } else if(reserved.containsKey(lexeme.toString())){
				nextToken = tokenFactory.makeToken(reserved.get(lexeme.toString()), position);
				return;
			    } else {
				ERROR("Unexpected Character " + c + " Found!!!");
			    }
			case IDENT:
				//Handle next character of an identifier or keyword
				if (Character.isLetterOrDigit(c)) {
					lexeme.append(c);
					source.advance();
					continue;
				} else {
				    nextToken = tokenFactory.makeIdToken(lexeme.toString(), position);
				    return;
				}
			
			case COLON:
				// Check for : vs :=
				if (c == '=') {
					source.advance();
					nextToken = tokenFactory.makeToken(TokenType.ASSIGN, position);
					return;
				} else {
					nextToken = tokenFactory.makeToken(TokenType.COLON, position);
					return;
				}
				
			// TODO and more state cases here
			}
		}

		// Clean up at end of source
		switch (state) {
		case INIT:
			// No more tokens found
			nextToken = null;
			return;
			
		case IDENT:
			// Successfully ended an identifier or keyword
			nextToken = tokenFactory.makeIdToken(lexeme.toString(), position);
			return;
			
		case COLON:
			// Final token was :
			nextToken = tokenFactory.makeToken(TokenType.COLON, position);
			return;
			
		// TODO handle more state cases here as well
		}
	}
}

package io.github.H20man13.DeClan.common.icode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.github.H20man13.DeClan.common.pat.P;

public class Goto implements ICode {
	public String label;

	public Goto(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "GOTO " + label;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Goto){
			Goto objGoto = (Goto)obj;

			return objGoto.label.equals(label);
		} else {
			return false;
		}
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isBranch() {
		return true;
	}

	@Override
	public P asPattern() {
		return P.PAT(P.GOTO(), P.ID());
	}

	@Override
	public boolean containsPlace(String place) {
		return false;
	}

	@Override
	public boolean containsLabel(String label) {
		return this.label.equals(label);
	}

	@Override
	public void replacePlace(String from, String to) {
		//Do nothing
	}

	@Override
	public void replaceLabel(String from, String to) {
		if(this.label.equals(from))
			this.label = to;
	}
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public ICode copy() {
		return new Goto(label);
	}
}

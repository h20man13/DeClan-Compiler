package io.github.H20man13.DeClan.common.icode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.H20man13.DeClan.common.pat.P;

public class Return implements ICode {
	@Override
	public String toString() {
		return "RETURN";
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
	public boolean equals(Object obj){
		if(obj instanceof Return)
			return true;
		else
			return false;
	}

	@Override
	public P asPattern() {
		// TODO Auto-generated method stub
		return P.RETURN();
	}

	@Override
	public boolean containsPlace(String place) {
		return false;
	}

	@Override
	public boolean containsLabel(String label) {
		return false;
	}

	@Override
	public void replacePlace(String from, String to) {
		//Do nothing this is just a placeholder
	}

	@Override
	public void replaceLabel(String from, String to) {
		//Do nothing this is just a placeholder
	}
	
	@Override
	public int hashCode() {
		return 24;
	}
}

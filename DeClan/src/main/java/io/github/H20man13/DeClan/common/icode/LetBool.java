package io.github.H20man13.DeClan.common.icode;

public class LetBool implements ICode {
	public String place;
	public boolean value;
	
	public LetBool(String place, boolean value) {
		this.place = place;
		this.value = value;
	}

	@Override
	public String toString() {
	    return  (value) ? place + " := TRUE" : place + " := FALSE";
	}
}

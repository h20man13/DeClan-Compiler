package io.github.H20man13.DeClan.common.icode.exp;

import io.github.H20man13.DeClan.common.pat.P;

public class StrExp implements Exp{
    public String value;

    public StrExp(String value){
        this.value = value;
    }

    @Override
    public boolean equals(Object exp) {
        if(exp instanceof StrExp){
            StrExp strExp = (StrExp)exp;
            return this.value.equals(strExp.value);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return '\"' + value + '\"';
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public P asPattern(boolean hasContainer) {
        if(hasContainer){
            return P.PAT(P.STR());
        } else {
            return P.STR();
        }
    }

    @Override
    public boolean containsPlace(String place) {
        return false;
    }

    @Override
    public void replacePlace(String from, String to) {
        //Do nothing
    }
    
    @Override
    public int hashCode() {
    	return value.hashCode();
    }

	@Override
	public Exp copy() {
		return new StrExp(value);
	}
}

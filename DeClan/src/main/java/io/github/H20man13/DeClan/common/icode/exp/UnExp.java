package io.github.H20man13.DeClan.common.icode.exp;

import java.util.Objects;

import io.github.H20man13.DeClan.common.pat.P;
import io.github.H20man13.DeClan.common.util.ConversionUtils;
import io.github.H20man13.DeClan.common.util.Utils;

public class UnExp implements Exp {
    public IdentExp right;
    public Operator op;

    public enum Operator{
        BNOT,
        INOT
    }

    public UnExp(Operator op, IdentExp right){
        this.right = right;
        this.op = op;
    }

    @Override
    public boolean equals(Object exp) {
        if(exp instanceof UnExp){
            UnExp unExp = (UnExp)exp;
            boolean operatorEquals = this.op == unExp.op;
            boolean rightEquals = this.right.equals(unExp.right);
            return operatorEquals && rightEquals;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "" + op + " " + right.toString();
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public P asPattern(boolean hasContainer) {
        if(hasContainer){
            return P.PAT(ConversionUtils.unOpToPattern(op), right.asPattern(false));
        } else {
            return null;
        }
    }

    @Override
    public boolean containsPlace(String place) {
        if(right.containsPlace(place))
            return true;
        return false;
    }

    @Override
    public void replacePlace(String from, String to) {
        right.replacePlace(from, to);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(op, right);
    }

	@Override
	public NullableExp copy() {
		return new UnExp(op, (IdentExp)right.copy());
	}
}

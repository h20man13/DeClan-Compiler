package io.github.H20man13.DeClan.common.icode.exp;

import io.github.H20man13.DeClan.common.pat.P;

public class BoolExp implements Exp {
    public boolean trueFalse;

    public BoolExp(boolean trueFalse){
        this.trueFalse = trueFalse;
    }

    @Override
    public boolean equals(Object exp) {
        if(exp instanceof BoolExp){
            BoolExp boolExp = (BoolExp)exp;
            return this.trueFalse == boolExp.trueFalse;
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return (trueFalse) ? "TRUE" : "FALSE";
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
            return P.PAT(P.BOOL());
        } else {
            return P.BOOL();
        }
    }
    
}

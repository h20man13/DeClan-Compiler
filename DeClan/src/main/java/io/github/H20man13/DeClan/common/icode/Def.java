package io.github.H20man13.DeClan.common.icode;

import io.github.H20man13.DeClan.common.exception.ICodeFormatException;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.pat.P;

public class Def implements ICode {
    public String label;
    public Type type;
    public Scope scope;
    public Exp val;

    public Def(ICode.Scope scope, String label, Exp val, ICode.Type type){
        this.scope = scope;
        this.label = label;
        this.type = type;
        this.val = val;
    }

    @Override
    public boolean isConstant() {
        return false;    
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("DEF ");
        sb.append(scope.toString());
        sb.append(' ');
        sb.append(label);
        sb.append(" := ");
        sb.append(val.toString());
        sb.append(" [");
        sb.append(type.toString());
        sb.append("]");

        return sb.toString();
    }

    @Override
    public P asPattern() {
        P expPattern = val.asPattern(true);
        if(this.scope == Scope.GLOBAL) return  P.PAT(P.GLOBAL(), P.DEF(), P.ID(), P.ASSIGN(), expPattern);
        else if(this.scope == Scope.PARAM) return P.PAT(P.PARAM(), P.DEF(), P.ID(), P.ASSIGN(), expPattern);
        else if(this.scope == Scope.GLOBAL) return P.PAT(P.GLOBAL(), P.DEF(), P.ID(), P.ASSIGN(), expPattern);
        else if(this.scope == Scope.LOCAL) return P.PAT(P.DEF(), P.ID(), P.ASSIGN(), expPattern);
        else if(this.scope == Scope.RETURN) return P.PAT(P.RETURN(), P.DEF(), P.ID(), P.ASSIGN(), expPattern);
        else throw new ICodeFormatException(this, "Invalid scope type found when generating pattern: " + this.scope);
    }

    @Override
    public boolean containsPlace(String place) {
        if(label.equals(place))
            return true;
        return this.val.containsPlace(place);
    }

    @Override
    public boolean containsLabel(String label) {
        return false;
    }

    @Override
    public void replacePlace(String from, String to) {
        if(this.label.equals(from))
            this.label = to;
        this.val.replacePlace(from, to);
    }

    @Override
    public void replaceLabel(String from, String to) {
        //Do nothing
    }
}

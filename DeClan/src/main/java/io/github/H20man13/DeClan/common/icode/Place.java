package io.github.H20man13.DeClan.common.icode;

import io.github.H20man13.DeClan.common.pat.P;

public class Place implements ICode {
    public String place;
    public String retPlace;

    public Place(String place, String retPlace){
        this.place = place;
        this.retPlace = retPlace;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(place);
        sb.append(" <- ");
        sb.append(retPlace);
        return sb.toString();
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
    public P asPattern() {
        return P.PAT(P.ID(), P.RETURN(), P.ID());
    }
    
}
package io.github.H20man13.DeClan.common.icode.label;

import io.github.H20man13.DeClan.common.pat.P;
import io.github.H20man13.DeClan.common.pat.P.LABEL;

public class StandardLabel extends Label {

    public StandardLabel(String label) {
        super(label);
    }

    @Override
    public P asPattern() {
        return P.PAT(P.LABEL(), P.ID());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("LABEL ");
        sb.append(label);
        return sb.toString();
    }
}
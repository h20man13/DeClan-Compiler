package io.github.H20man13.DeClan.common.icode;

import io.github.H20man13.DeClan.common.pat.P;

public class SymEntry implements ICode {
    private int symType;
    public String icodePlace;
    public String declanIdent;

    public SymEntry(int symType, String icodePlace, String declanIdent){
        this.symType = symType;
        this.icodePlace = icodePlace;
        this.declanIdent = declanIdent;
    }

    public static final int CONST = 0b1;
    public static final int EXTERNAL = 0b1000;
    public static final int INTERNAL = 0b10000;
    
    public boolean containsQualities(int qualityMask){
        return (this.symType & qualityMask) == qualityMask;
    }

    public boolean missingQualities(int qualityMask){
        return (this.symType ^ qualityMask) == qualityMask;
    }

    @Override
    public boolean isConstant() {
        return containsQualities(SymEntry.CONST);
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public P asPattern() {
        if(containsQualities(CONST | INTERNAL)) return P.PAT(P.ID(), P.CONST(), P.INTERNAL(), P.ID());
        else if(containsQualities(CONST | EXTERNAL)) return P.PAT(P.ID(), P.CONST(), P.EXTERNAL(), P.ID());
        else if(containsQualities(INTERNAL)) return P.PAT(P.ID(), P.INTERNAL(), P.ID());
        else if(containsQualities(EXTERNAL)) return P.PAT(P.ID(), P.EXTERNAL(), P.ID());
        else {
            throw new RuntimeException("Error Unknown pattern type found in P.asPattern");
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(icodePlace);
        sb.append(' ');
        
        if(containsQualities(CONST | INTERNAL)) sb.append("CONST INTERNAL");
        else if(containsQualities(CONST | EXTERNAL)) sb.append("CONST EXTERNAL");
        else if(containsQualities(INTERNAL)) sb.append("INTERNAL");
        else if(containsQualities(EXTERNAL)) sb.append("EXTERNAL");

        sb.append(' ');
        sb.append(declanIdent);
        return sb.toString();
    }
}

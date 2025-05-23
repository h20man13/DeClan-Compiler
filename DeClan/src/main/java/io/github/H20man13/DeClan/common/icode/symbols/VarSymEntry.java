package io.github.H20man13.DeClan.common.icode.symbols;

import java.util.Objects;

import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.pat.P;

public class VarSymEntry extends SymEntry {
    public String declanIdent;
    public String funcName;
    public int paramNumber;
    public ICode.Type codeType;
    
    public VarSymEntry(String icodePlace, int type, String identOrFunctionName, ICode.Type icodeType, boolean isReturn){
        super(type, icodePlace);
        
        this.codeType = icodeType;
        if(isReturn) {
        	this.funcName = identOrFunctionName;
        	this.declanIdent = null;
        } else {
        	this.declanIdent = identOrFunctionName;
            this.funcName = null;
        }
        
        this.paramNumber = -1;
    }
    
    public VarSymEntry(String icodePlace, int type, String declanIdent, String funcName, ICode.Type icodeType){
        super(type, icodePlace);
        this.declanIdent = declanIdent;
        this.funcName = funcName;
        this.paramNumber = -1;
        this.codeType = icodeType;
    }
    
    public VarSymEntry(String icodePlace, int type, String funcName, int paramNumber, ICode.Type icodeType){
        super(type, icodePlace);
        this.funcName = funcName;
        this.paramNumber = paramNumber;
        this.codeType = icodeType;
    }
    
    public VarSymEntry(String icodePlace, int type, String declanIdent, String funcName, int paramNumber, ICode.Type icodeType){
        super(type, icodePlace);
        this.declanIdent = declanIdent;
        this.funcName = funcName;
        this.paramNumber = paramNumber;
        this.codeType = icodeType;
    }
    
    private VarSymEntry(VarSymEntry toCopy) {
    	super(toCopy);
    	this.declanIdent = toCopy.declanIdent;
    	this.funcName = toCopy.funcName;
    	this.paramNumber = toCopy.paramNumber;
    	this.codeType = toCopy.codeType;
    }

    @Override
    public P asPattern() {
    	if(this.funcName == null) {
    		if(containsAllQualities(SymEntry.CONST | SymEntry.EXTERNAL)) return P.PAT(P.ID(), P.CONST(), P.EXTERNAL(), P.ID());
            else if(containsAllQualities(SymEntry.CONST | SymEntry.INTERNAL)) return P.PAT(P.ID(), P.CONST(), P.INTERNAL(), P.ID());
            else if(containsAllQualities(SymEntry.INTERNAL)) return P.PAT(P.ID(), P.INTERNAL(), P.ID());
            else if(containsAllQualities(SymEntry.EXTERNAL)) return P.PAT(P.ID(), P.EXTERNAL(), P.ID());
            else return null;
    	} else {
    		 if(containsAllQualities(SymEntry.CONST | SymEntry.EXTERNAL)) return P.PAT(P.ID(), P.CONST(), P.EXTERNAL(), P.ID(), P.ID());
	        else if(containsAllQualities(SymEntry.CONST | SymEntry.INTERNAL)) return P.PAT(P.ID(), P.CONST(), P.INTERNAL(), P.ID(), P.ID());
	        else if(containsAllQualities(SymEntry.INTERNAL)) return P.PAT(P.ID(), P.INTERNAL(), P.ID(), P.ID());
	        else if(containsAllQualities(SymEntry.EXTERNAL)) return P.PAT(P.ID(), P.EXTERNAL(), P.ID(), P.ID());
	        else return null;
    	}
    }

    @Override
    public SymEntry copy() {
        return new VarSymEntry(this);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("ENTRY ");
        sb.append(icodePlace);
        sb.append(' ');
        
        if(containsAllQualities(CONST)){
            sb.append("CONST ");
        }

        if(containsAllQualities(INTERNAL)){
            sb.append("INTERNAL ");
        } else if(containsAllQualities(EXTERNAL)){
            sb.append("EXTERNAL ");
        }
        
        if(containsAllQualities(RETURN))
        	sb.append("RETURN ");
        else if(containsAllQualities(PARAM))
        	sb.append("PARAM ");
        else if(containsAllQualities(GLOBAL))
        	sb.append("GLOBAL ");

        if(this.declanIdent != null){
        	sb.append(declanIdent);
        	sb.append(' ');
        }
        
        if(this.funcName != null) {
        	sb.append(funcName);
        	if(this.paramNumber > -1) {
        		sb.append(' ');
        		sb.append(this.paramNumber);
        	}
        }
        
        sb.append(" <");
        sb.append(codeType.toString());
        sb.append('>');

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VarSymEntry){
            VarSymEntry otherEntry = (VarSymEntry)obj;
            if(otherEntry.paramNumber == this.paramNumber)
            	if(otherEntry.codeType == this.codeType)
	            	if((otherEntry.declanIdent != null && declanIdent != null)) {
			            if(otherEntry.declanIdent.equals(declanIdent))
			            	if(otherEntry.funcName != null && this.funcName != null) {
			            		if(otherEntry.funcName.equals(funcName))
			            			return super.equals(obj);
			            	} else if(otherEntry.funcName == null && funcName == null)
			            		return super.equals(obj);
	            	} else if(otherEntry.declanIdent == null && this.declanIdent == null) {
	            		if(otherEntry.funcName != null && this.funcName != null) {
		            		if(otherEntry.funcName.equals(funcName))
		            			return super.equals(obj);
		            	} else if(otherEntry.funcName == null && funcName == null)
		            		return super.equals(obj);
	            	}
            
        }
        return false;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(icodePlace, declanIdent, funcName, paramNumber, codeType);
    }
}

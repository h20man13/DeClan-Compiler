package io.github.H20man13.DeClan.common.icode.section;

import java.util.LinkedList;
import java.util.List;

import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.pat.P;

public class DataSec implements ICode {
    public List<Assign> intermediateCode;
    public DataSec(List<Assign> icode){
        this.intermediateCode = icode;
    }

    public DataSec(){
        this(new LinkedList<Assign>());
    }

    public void addInstruction(Assign icode){
        this.intermediateCode.add(icode);
    }

    public void addInstructions(List<Assign> icodes){
        this.intermediateCode.addAll(icodes);
    }

    public int getLength(){
        return intermediateCode.size();
    }

    public Assign getInstruction(int index){
        return intermediateCode.get(index);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof DataSec){
            DataSec dataSec = (DataSec)obj;
            
            if(dataSec.intermediateCode.size() != intermediateCode.size())
                return false;

            int size = dataSec.getLength();

            for(int i = 0; i < size; i++){
                ICode objICode = dataSec.getInstruction(i);
                ICode icode = getInstruction(i);
                if(!objICode.equals(icode))
                    return false;
            }

            return true;
        } else {
            return false;
        }
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
        int size = intermediateCode.size();
        P[] patList = new P[size];
        for(int i = 0; i < size; i++){
            patList[i] = intermediateCode.get(i).asPattern();
        }
        return P.PAT(patList);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("DATA SECTION\r\n");
        for(ICode icode : intermediateCode){
            sb.append(' ');
            sb.append(icode.toString());
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public List<ICode> genFlatCode() {
        LinkedList<ICode> resultList = new LinkedList<ICode>();
        for(ICode icode : intermediateCode){
            resultList.addAll(icode.genFlatCode());
        }
        return resultList;
    }
}

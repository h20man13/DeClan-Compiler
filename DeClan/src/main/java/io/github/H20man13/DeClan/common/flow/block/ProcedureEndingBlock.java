package io.github.H20man13.DeClan.common.flow.block;

import java.util.LinkedList;
import java.util.List;

import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.Return;

public class ProcedureEndingBlock extends BasicBlock {
    private Return ret;
    private Assign placement;
    
    public ProcedureEndingBlock(List<ICode> codeInBlock, Assign place, Return ret) {
        super(codeInBlock);
        this.placement = place;
        this.ret = ret;
    }

    public Assign getPlacement(){
        return placement;
    }

    public Return getReturn(){
        return ret;
    }

    @Override
    public List<ICode> getAllICode(){
        LinkedList<ICode> icode = new LinkedList<ICode>();
        icode.addAll(super.getAllICode());
        if(placement != null){
            icode.add(placement);
        }
        icode.add(ret);
        return icode;
    }

    @Override 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(ret.toString());
        sb.append('\n');
        return sb.toString();
    }
}

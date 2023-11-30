package io.github.H20man13.DeClan.common.flow;

import java.util.Map;

import io.github.H20man13.DeClan.common.flow.block.BasicProcedureBlock;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.symboltable.Environment;
import io.github.H20man13.DeClan.common.symboltable.entry.LiveInfo;

public class ProcedureEntryNode extends BlockNode{
    private BasicProcedureBlock block;

    public ProcedureEntryNode(BasicProcedureBlock block) {
        super(block);
        this.block = block;
    }
}

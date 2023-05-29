package io.github.H20man13.DeClan.common.flow;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.H20man13.DeClan.common.icode.ICode;

public class ExitNode implements FlowGraphNode{
    private FlowGraphNode exit;

    public ExitNode(BlockNode exit){
        this.exit = exit;
        exit.addSuccessor(this);
    }

    @Override
    public void removeDeadCode() {}

    @Override
    public List<ICode> getICode() {
        return new LinkedList<ICode>();
    }
}

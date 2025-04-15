package io.github.H20man13.DeClan.common.flow;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.H20man13.DeClan.common.icode.ICode;

public class ExitNode implements FlowGraphNode{
    public FlowGraphNode exit;

    public ExitNode(FlowGraphNode exit){
        this.exit = exit;
    }

    @Override
    public List<ICode> getICode() {
        return new LinkedList<ICode>();
    }

    @Override
    public String toString(){
        return "EXIT";
    }

	@Override
	public FlowGraphNode copy() {
		return new ExitNode(exit);
	}
}

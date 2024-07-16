package io.github.H20man13.DeClan.common.dag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.util.ConversionUtils;

public class DagOperationNode implements DagNode {
    public enum Op{
        IADD, ISUB, IDIV, IMOD, LAND, 
        LOR, INEG, RNEG, BNOT, GE, LE, 
        LT, GT, NE, EQ, IMUL, RADD, RSUB,
        RMUL, RDIVIDE, IAND, IOR, ILSHIFT, 
        IRSHIFT, INOT, IXOR
    }

    private List<DagNode> children;
    private List<DagNode> ancestors;
    private List<String> identifiers;
    private boolean nodeIsKilled;
    private Op operation;
    private ValueType type;
    private ScopeType scope;

    
    public DagOperationNode(ScopeType scope, String identifier, Op operation, List<DagNode> children, ValueType type){
        this.children = new LinkedList<DagNode>();
        this.children.addAll(children);
        for(DagNode child : children){
            child.addAncestor(this);
        }
        this.identifiers = new LinkedList<String>();
        this.identifiers.add(identifier);
        this.nodeIsKilled = false;
        this.operation = operation;
        this.ancestors = new ArrayList<>();
        this.scope = scope;
        this.type = type;
    }

    public List<String> getIdentifiers(){
        return identifiers;
    }

    public void addIdentifier(String identifier){
        this.identifiers.add(identifier);
    }

    public void killDagNode(){
        this.nodeIsKilled = true;
    }

    public boolean isNodeKilled(){
        return this.nodeIsKilled;
    }

    @Override
    public boolean containsId(IdentExp ident) {
        return this.identifiers.contains(ident) && ConversionUtils.assignScopeToDagScopeType(ident.scope) == scope;
    }

    @Override
    public boolean equals(Object dagNode) {
        if(dagNode instanceof DagOperationNode){
            DagOperationNode opNode = (DagOperationNode)dagNode;
            boolean operationsAreEqual = opNode.operation == this.operation;
            boolean scopesAreEqual = opNode.scope == this.scope;
            boolean typesAreEqual = opNode.type == this.type;
            boolean childrenAreEqual = compareChildren(opNode);
            return operationsAreEqual && scopesAreEqual && typesAreEqual && childrenAreEqual;
        } else {
            return false;
        }
    }

    private boolean compareChildren(DagOperationNode opNode){
        if(this.children.size() == opNode.children.size()){
            for(int i = 0; i < this.children.size(); i++){
                DagNode node1 = this.children.get(i);
                DagNode node2 = opNode.children.get(i);
                if(node1.hashCode() != node2.hashCode()){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addAncestor(DagNode ancestor) {
        this.ancestors.add(ancestor);
    }

    @Override
    public void deleteAncestor(DagNode ancestor) {
        for(int i = 0; i < ancestors.size(); i++){
            DagNode locAncestor = ancestors.get(i);
            if(locAncestor.hashCode() == ancestor.hashCode()){
                ancestors.remove(i);
            }
        }
    }

    @Override
    public boolean isRoot() {
        return ancestors.size() == 0;
    }

    @Override
    public List<DagNode> getChildren() {
        return this.children;
    }

    public Op getOperator(){
        return this.operation;
    }

    @Override
    public ScopeType getScopeType() {
        return this.scope;
    }

    @Override
    public ValueType getValueType() {
        return this.type;
    }
}

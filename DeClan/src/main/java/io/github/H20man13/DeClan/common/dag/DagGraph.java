package io.github.H20man13.DeClan.common.dag;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.github.H20man13.DeClan.common.icode.exp.IdentExp;

public class DagGraph {
    private List<DagNode> dagNodes;

    public DagGraph(){
        this.dagNodes = new ArrayList<DagNode>();
    }

    public DagNode searchForLatestChild(IdentExp identifier){
        for(int i = dagNodes.size() - 1; i >= 0; i--){
            DagNode child = dagNodes.get(i);
            if(child.containsId(identifier)){
                return child;
            }
        }
        return null;
    }

    public void addDagNode(DagNode node){
        this.dagNodes.add(node);
    }

    public List<DagNode> getRoots(){
        List<DagNode> roots = new LinkedList<>();
        for(DagNode node : this.dagNodes){
            if(node.isRoot()){
                roots.add(node);
            }
        }
        return roots;
    }

    public void deleteDagNode(DagNode node){
        for(int i = 0; i < this.dagNodes.size(); i++){
            DagNode nodeToCheck = this.dagNodes.get(i);
            if(node.hashCode() == nodeToCheck.hashCode()){
                this.dagNodes.remove(i);
            }
        }

        for(DagNode child : node.getChildren()){
            child.deleteAncestor(node);
        }
    }

    public DagNode getDagNode(DagNode node){
        for(int i = dagNodes.size() - 1; i >= 0; i--){
            DagNode nodeAtI = dagNodes.get(i);
            if(nodeAtI.equals(node)){
                return nodeAtI;
            }
        }
        return null;
    }

    public List<DagNode> getDagNodes(){
        return this.dagNodes;
    }
}

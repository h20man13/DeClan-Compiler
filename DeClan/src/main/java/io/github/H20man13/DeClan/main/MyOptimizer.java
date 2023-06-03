package io.github.H20man13.DeClan.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.H20man13.DeClan.common.BasicBlock;
import io.github.H20man13.DeClan.common.RegisterGenerator;
import io.github.H20man13.DeClan.common.Tuple;
import io.github.H20man13.DeClan.common.analysis.AnticipatedExpressionsAnalysis;
import io.github.H20man13.DeClan.common.analysis.AvailableExpressionsAnalysis;
import io.github.H20man13.DeClan.common.analysis.ConstantPropogationAnalysis;
import io.github.H20man13.DeClan.common.analysis.LiveVariableAnalysis;
import io.github.H20man13.DeClan.common.analysis.PostponableExpressionsAnalysis;
import io.github.H20man13.DeClan.common.analysis.UsedExpressionAnalysis;
import io.github.H20man13.DeClan.common.flow.BlockNode;
import io.github.H20man13.DeClan.common.flow.EntryNode;
import io.github.H20man13.DeClan.common.flow.ExitNode;
import io.github.H20man13.DeClan.common.flow.FlowGraph;
import io.github.H20man13.DeClan.common.flow.FlowGraphNode;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.End;
import io.github.H20man13.DeClan.common.icode.Goto;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.If;
import io.github.H20man13.DeClan.common.icode.Label;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;
import io.github.H20man13.DeClan.common.util.Utils;

public class MyOptimizer {
    private List<ICode> intermediateCode;
    private FlowGraph globalFlowGraph;
    private AnticipatedExpressionsAnalysis anticipatedAnal;
    private AvailableExpressionsAnalysis availableAnal;
    private PostponableExpressionsAnalysis postponableAnal;
    private UsedExpressionAnalysis usedAnal;
    private ConstantPropogationAnalysis propAnal;
    private LiveVariableAnalysis liveAnal;
    private Map<FlowGraphNode, Set<Exp>> latest;
    private Map<FlowGraphNode, Set<Exp>> earliest;
    private Map<ICode, Set<Exp>> used;
    private Set<Exp> globalFlowSet;
    private RegisterGenerator gen;

    public MyOptimizer(List<ICode> intermediateCode){
        this(intermediateCode, new RegisterGenerator());
    }

    public MyOptimizer(List<ICode> intermediateCode, RegisterGenerator gen){
        this.gen = gen;
        this.intermediateCode = intermediateCode;
        this.latest = new HashMap<FlowGraphNode, Set<Exp>>();
        this.earliest = new HashMap<FlowGraphNode, Set<Exp>>();
        this.used = new HashMap<ICode, Set<Exp>>();
        this.globalFlowSet = new HashSet<Exp>();

        List<Integer> firsts = findFirsts();
        List<BasicBlock> basicBlocks = new LinkedList<BasicBlock>();
        for(int leaderIndex = 0; leaderIndex < firsts.size(); leaderIndex++){
            int beginIndex = firsts.get(leaderIndex);
            int endIndex;
            if(leaderIndex + 1 < firsts.size()){
                endIndex = firsts.get(leaderIndex + 1) - 1;
            } else {
                endIndex = this.intermediateCode.size() - 1;
            }

            List<ICode> basicBlockList = new LinkedList<ICode>();
            for(int i = beginIndex; i <= endIndex; i++){
                basicBlockList.add(intermediateCode.get(i));
            }

            basicBlocks.add(new BasicBlock(basicBlockList));
        }

        HashMap<String, BlockNode> labeledNodes = new HashMap<String, BlockNode>();
        List<BlockNode> dagNodes = new LinkedList<BlockNode>();

        for(int i = 0; i < basicBlocks.size(); i++){
            BasicBlock blockAtIndex = basicBlocks.get(i);
            BlockNode blockNode = new BlockNode(blockAtIndex);
                
            if(Utils.beginningOfBlockIsLabel(blockAtIndex)){
                Label firstLabel = (Label)blockAtIndex.getIcode().get(0);
                String labelName = firstLabel.label;
                labeledNodes.put(labelName, blockNode);
            }

            dagNodes.add(blockNode);
        }

        for(int i = 0; i < dagNodes.size(); i++){
            BlockNode node = dagNodes.get(i);
            BasicBlock block = node.getBlock();
            if(Utils.endOfBlockIsJump(block)){
                ICode lastCode = block.getIcode().get(block.getIcode().size() - 1);
                if(lastCode instanceof If){
                    If lastIf = (If)lastCode;
                    BlockNode trueNode = labeledNodes.get(lastIf.ifTrue);
                    node.addSuccessor(trueNode);
                    trueNode.addPredecessor(node);
                    BlockNode falseNode = labeledNodes.get(lastIf.ifFalse);
                    node.addSuccessor(falseNode);
                    falseNode.addPredecessor(node);
                } else if(lastCode instanceof Goto){
                    Goto lastGoto = (Goto)lastCode;
                    BlockNode labeledNode = labeledNodes.get(lastGoto.label);
                    node.addSuccessor(labeledNode);
                    labeledNode.addPredecessor(node);
                }
            } else if(i + 1 < dagNodes.size()){
                BlockNode nextNode = dagNodes.get(i + 1);
                node.addSuccessor(nextNode);
                nextNode.addPredecessor(node);
            }
        }

        EntryNode entry = new EntryNode(dagNodes.get(0));
        ExitNode exit = new ExitNode(dagNodes.get(dagNodes.size() - 1));

        FlowGraph flowGraph = new FlowGraph(entry, dagNodes, exit);

        this.globalFlowGraph = flowGraph;
    }

    public List<ICode> getICode(){
        if(this.globalFlowGraph == null){
            return this.intermediateCode;
        } else {
            List<ICode> result = new LinkedList<ICode>();
            for(BlockNode block : this.globalFlowGraph.getBlocks()){
                result.addAll(block.getICode());
            }
            return result;
        }
    }

    private List<Integer> findFirsts(){
        List<Integer> firsts = new LinkedList<Integer>();
        for(int i = 0; i < intermediateCode.size(); i++){
            ICode intermediateInstruction = intermediateCode.get(i);
            if(i == 0){
                //First Statement is allways a leader
                firsts.add(i);
            } else if(intermediateInstruction instanceof Label){
                //Target of Jumps are allways leaders
                firsts.add(i);
            } else if(i + 1 < intermediateCode.size() && intermediateInstruction.isBranch()){
                //First instruction following an If/Goto/Proc/Call are leaders
                firsts.add(i + 1);
            }
        }

        return firsts;
    }

    public void removeDeadCode(){

    }

    private void buildGlobalExpressionSemilattice(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            for(ICode icode : block.getICode()){
                if(icode instanceof Assign){
                    Assign ident = (Assign)icode;
                    if(!Utils.setContainsExp(this.globalFlowSet, ident.value)){
                        this.globalFlowSet.add(ident.value);
                    }
                }
            }
        }
    }

    private void buildEarliest(){
        this.earliest.put(this.globalFlowGraph.getExit(), new HashSet<Exp>());
        this.earliest.put(this.globalFlowGraph.getEntry(), new HashSet<Exp>());
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            Set<Exp> earliest = new HashSet<Exp>();
            for(ICode icode : block.getICode()){
                earliest.addAll(this.anticipatedAnal.getInstructionInputSet(icode));
                earliest.removeAll(this.availableAnal.getInstructionInputSet(icode));
            }
            this.earliest.put(block, earliest);
        }
    }

    private void buildUsedExpressions(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            for(ICode icode : block.getICode()){
                Set<Exp> blockUsed = new HashSet<Exp>();
                if(icode instanceof Assign){
                    Assign utilICode = (Assign)icode;
                    blockUsed.add(utilICode.value);
                } else if(icode instanceof If){
                    If ifICode = (If)icode;
                    blockUsed.add(ifICode.exp);
                }
                this.used.put(icode, blockUsed);
            }
        }
    }

    private void runAvailableExpressionAnalysis(){
        this.availableAnal = new AvailableExpressionsAnalysis(this.globalFlowGraph, this.globalFlowSet);
        this.availableAnal.run();
    }

    private void runAnticipatedExpressionAnalysis(){
        this.anticipatedAnal = new AnticipatedExpressionsAnalysis(this.globalFlowGraph, this.globalFlowSet);    
        this.anticipatedAnal.run();
    }

    private void runPosponableExpressionAnalysis(){
        this.postponableAnal = new PostponableExpressionsAnalysis(this.globalFlowGraph, this.globalFlowSet, this.earliest, this.used);
        this.postponableAnal.run();
    }

    private void buildLatest(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            Set<Exp> latest = new HashSet<Exp>();
            
            Map<FlowGraphNode, Set<Exp>> earliestUnionPosponableSaved = new HashMap<FlowGraphNode, Set<Exp>>();
            for (FlowGraphNode sucessor : block.getSuccessors()){
                Set<Exp> earliestUnionPosponable = new HashSet<Exp>();

                Set<Exp> earliestOfSuccessor = earliest.get(sucessor);
                earliestUnionPosponable.addAll(earliestOfSuccessor);

                Set<Exp> postponableInput = postponableAnal.getBlockInputSet(sucessor);
                earliestUnionPosponable.addAll(postponableInput);

                earliestUnionPosponableSaved.put(sucessor, earliestUnionPosponable);
            }

            Set<Exp> intersectionOfAllSucessors = new HashSet<Exp>();
            intersectionOfAllSucessors.addAll(this.globalFlowSet);

            for(FlowGraphNode sucessor : block.getSuccessors()){
                Set<Exp> earliestUnionPosponable = earliestUnionPosponableSaved.get(sucessor);
                intersectionOfAllSucessors.retainAll(earliestUnionPosponable);
            }

            Set<Exp> usedUnionCompliment = new HashSet<Exp>();

            Set<Exp> compliment = new HashSet<Exp>();
            compliment.addAll(this.globalFlowSet);
            compliment.removeAll(intersectionOfAllSucessors);

            for(ICode icode : block.getICode()){
                usedUnionCompliment.addAll(this.used.get(icode));
            }
            usedUnionCompliment.addAll(compliment);

            Set<Exp> earliestUnionPosponable = new HashSet<Exp>();
            earliestUnionPosponable.addAll(this.earliest.get(block));
            earliestUnionPosponable.addAll(this.postponableAnal.getBlockInputSet(block));

            latest.addAll(earliestUnionPosponable);
            latest.retainAll(usedUnionCompliment);

            this.latest.put(block, latest);
        }
    }

    private void runUsedExpressionAnalysis(){
        this.usedAnal = new UsedExpressionAnalysis(this.globalFlowGraph, this.used, this.latest);
        this.usedAnal.run();
    }

    private void runConstantPropogationAnalysis(){
        this.propAnal = new ConstantPropogationAnalysis(this.globalFlowGraph);
        this.propAnal.run();
    }

    private void runLiveVariableAnalysis(){
        this.liveAnal = new LiveVariableAnalysis(this.globalFlowGraph);
        this.liveAnal.run();
    }

    public void runDataFlowAnalysis(){
        if(this.globalFlowGraph != null){
            //buildGlobalExpressionSemilattice();
            runLiveVariableAnalysis();
            //runAvailableExpressionAnalysis();
            //runAnticipatedExpressionAnalysis();
            //buildEarliest();
            //buildUsedExpressions();  
            //runPosponableExpressionAnalysis();
            //buildLatest();
            //runUsedExpressionAnalysis();
            runConstantPropogationAnalysis();
        }
    }

    /*
    public void performPartialRedundancyElimination(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            Set<Exp> latestInstructionUsedOutput = new HashSet<Exp>();
            latestInstructionUsedOutput.addAll(this.latest.get(block));
            latestInstructionUsedOutput.retainAll(this.usedAnal.getBlockOutputSet(block));

            Map<Exp, String> identifierMap = new HashMap<Exp, String>();
            List<ICode> toPreAppendToBeginning = new LinkedList<ICode>();
            for(Exp latestInstructionOutput : latestInstructionUsedOutput){
                String register = gen.genNextRegister();
                toPreAppendToBeginning.add(new Assign(register, latestInstructionOutput));
                identifierMap.put(latestInstructionOutput, register);
            }

            Set<Exp> notLatest = new HashSet<Exp>();
            notLatest.addAll(this.globalFlowSet);
            notLatest.removeAll(this.latest.get(block));

            Set<Exp> notLatestUnionUsedOut = new HashSet<Exp>();
            notLatestUnionUsedOut.addAll(notLatest);
            notLatestUnionUsedOut.addAll(usedAnal.getBlockOutputSet(block));

            Set<Exp> useIntersectionNotLatestUnionUsedOut = new HashSet<Exp>();

            for(ICode icode : block.getICode()){
                useIntersectionNotLatestUnionUsedOut.addAll(this.used.get(icode));
            }

            useIntersectionNotLatestUnionUsedOut.retainAll(notLatestUnionUsedOut);
            

            List<ICode> icodeList = block.getICode();
            for(int i = 0; i < icodeList.size(); i++){
                ICode elem = icodeList.get(i);
                if(elem instanceof Assign){
                    Assign assignElem = (Assign)elem;
                    String place = assignElem.place;
                    Exp codeExp = assignElem.value;
                    if(codeExp != null && place != null){
                        if(Utils.setContainsExp(useIntersectionNotLatestUnionUsedOut, codeExp)){
                            String value = identifierMap.get(codeExp);
                            if(value != null){
                                Assign var = new Assign(place, new IdentExp(value));
                                icodeList.set(i, var);
                            }
                        }
                    }
                }
            }
            
            List<ICode> resultList = new LinkedList<ICode>();
            resultList.addAll(toPreAppendToBeginning);
            resultList.addAll(icodeList);

            block.getBlock().setICode(resultList);
        }
    }
    */

    public void performDeadCodeElimination(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            List<ICode> result = new LinkedList<ICode>();
            for(ICode icode : block.getICode()){
                if(icode instanceof Assign){
                    Assign assICode = (Assign)icode;
                    Set<String> liveVariables = this.liveAnal.getInstructionOutputSet(icode);
                    if(liveVariables.contains(assICode.place)){
                        result.add(assICode);
                    }
                } else {
                    result.add(icode);
                }
            }
            block.getBlock().setICode(result);
        }
    }

    public void performConstantPropogation(){
        for(BlockNode block : this.globalFlowGraph.getBlocks()){
            List<ICode> icodeList = block.getICode();
            for(int i = 0; i < icodeList.size(); i++){
                ICode icode = icodeList.get(i);
                Set<Tuple<String, Object>> values = this.propAnal.getInstructionInputSet(icode);
                if(icode instanceof Assign){
                    Assign varICode = (Assign)icode;
                    if(varICode.value instanceof IdentExp){
                        IdentExp identVal = (IdentExp)varICode.value;
                        Object result = Utils.getValueFromSet(values, identVal.ident);
                        Exp resultExp = Utils.valueToExp(result);
                        if(resultExp != null){
                            varICode.value = resultExp;
                        }
                    } else if(varICode.value instanceof BinExp){
                        BinExp binExpVal = (BinExp)varICode.value;

                        if(binExpVal.left instanceof IdentExp){
                            IdentExp identLeft = (IdentExp)binExpVal.left;
                            Object result = Utils.getValueFromSet(values, identLeft.ident);
                            Exp resultExp = Utils.valueToExp(result);
                            if(resultExp != null)
                                binExpVal.left = resultExp;
                        }

                        if(binExpVal.right instanceof IdentExp){
                            IdentExp identRight = (IdentExp)binExpVal.right;
                            Object result = Utils.getValueFromSet(values, identRight.ident);
                            Exp resultExp = Utils.valueToExp(result);
                            if(resultExp != null){
                                binExpVal.right = resultExp;
                            }
                        }
                    } else if(varICode.value instanceof UnExp){
                        UnExp unExpVal = (UnExp)varICode.value;

                        if(unExpVal.right instanceof IdentExp){
                            IdentExp identRight = (IdentExp)unExpVal.right;
                            Object result = Utils.getValueFromSet(values, identRight.ident);
                            Exp resultExp = Utils.valueToExp(result);
                            if(resultExp != null){
                                unExpVal.right = resultExp;
                            }
                        }
                    }
                }
            }
        }
    }
}

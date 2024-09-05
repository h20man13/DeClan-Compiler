package io.github.H20man13.DeClan.common.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import io.github.H20man13.DeClan.common.flow.FlowGraph;
import io.github.H20man13.DeClan.common.flow.FlowGraphNode;
import io.github.H20man13.DeClan.common.icode.ICode;

public abstract class Analysis<AnalysisType, SetType> implements AnalysisBase {
	private FlowGraph flowGraph;
    private Direction direction;
    private Function<List<Set<SetType>>, Set<SetType>> meetOperation;
    private Set<SetType> semiLattice;

    private Map<AnalysisType, Set<SetType>> mappedOutputs;
    private Map<AnalysisType, Set<SetType>> mappedInputs;
    
    private Function<List<Set<SetType>>, Set<SetType>> unionOperation;
    private Function<List<Set<SetType>>, Set<SetType>> intersectionOperation;
    
	public abstract Set<SetType> transferFunction(AnalysisType type, Set<SetType> inputSet);
	
	public Analysis(FlowGraph flowGraph, Direction direction, Meet meetOperation, Set<SetType> semiLattice) {
		this.flowGraph = flowGraph;
        this.direction = direction;
        this.unionOperation = new Function<List<Set<SetType>>,Set<SetType>>() {
            @Override
            public Set<SetType> apply(List<Set<SetType>> t) {
                Set<SetType> result = new HashSet<SetType>();
                for(Set<SetType> set : t){
                    result.addAll(set);
                }
                return result;
            } 
        };
        this.intersectionOperation = new Function<List<Set<SetType>>,Set<SetType>>() {
            @Override
            public Set<SetType> apply(List<Set<SetType>> t) {
                Set<SetType> result = unionOperation.apply(t);
                for(Set<SetType> set : t){
                    result.retainAll(set);
                }
                return result;
            }
        };

        if(meetOperation == Meet.UNION){
            this.meetOperation = unionOperation;
        } else {
            this.meetOperation = intersectionOperation;
        }

        this.mappedInputs = new HashMap<AnalysisType, Set<SetType>>();
        this.mappedOutputs = new HashMap<AnalysisType, Set<SetType>>();
        this.semiLattice = semiLattice;
	}
	
	public Analysis(FlowGraph flowGraph, Direction direction, Function<List<Set<SetType>>, Set<SetType>> meetOperation, Set<SetType> semilattice){
        this.flowGraph = flowGraph;
        this.direction = direction;
        this.unionOperation = new Function<List<Set<SetType>>,Set<SetType>>() {
            @Override
            public Set<SetType> apply(List<Set<SetType>> t) {
                Set<SetType> result = new HashSet<SetType>();
                for(Set<SetType> set : t){
                    result.addAll(set);
                }
                return result;
            } 
        };
        this.intersectionOperation = new Function<List<Set<SetType>>,Set<SetType>>() {
            @Override
            public Set<SetType> apply(List<Set<SetType>> t) {
                Set<SetType> result = unionOperation.apply(t);
                for(Set<SetType> set : t){
                    result.retainAll(set);
                }
                return result;
            }
        };

        this.meetOperation = meetOperation;
        this.semiLattice = semilattice;
        this.mappedInputs = new HashMap<AnalysisType, Set<SetType>>();
        this.mappedOutputs = new HashMap<AnalysisType, Set<SetType>>();
    }
	
	public Analysis(FlowGraph flowGraph, Direction direction, Function<List<Set<SetType>>, Set<SetType>> meetOperation){
       this(flowGraph, direction, meetOperation, new HashSet<SetType>());
    }

    public Analysis(FlowGraph flowGraph, Direction direction, Meet meetOperation){
        this(flowGraph, direction, meetOperation, new HashSet<SetType>());
    }
    
    public Set<SetType> getInputSet(AnalysisType instruction){
        return mappedInputs.get(instruction);
    }

    public Set<SetType> getOutputSet(AnalysisType instruction){
        return mappedOutputs.get(instruction);
    }
    
    protected void addEmptyInputSet(AnalysisType analysisType) {
    	mappedInputs.put(analysisType, new HashSet<SetType>());
    }
    
    protected void addEmptyOutputSet(AnalysisType analysisType) {
    	mappedOutputs.put(analysisType, new HashSet<SetType>());
    }
    
    protected void addOutputSet(AnalysisType anslysisType, Set<SetType> setToAdd) {
    	mappedOutputs.put(anslysisType, setToAdd);
    }
    
    protected void addInputSet(AnalysisType anslysisType, Set<SetType> setToAdd) {
    	mappedInputs.put(anslysisType, setToAdd);
    }
    
    protected boolean changesHaveOccured(Map<AnalysisType, Set<SetType>> actual, Map<AnalysisType, Set<SetType>> cached){
        Set<AnalysisType> keys = actual.keySet();
        for(AnalysisType key : keys){
            if(!cached.containsKey(key)){
                return true;
            }

            Set<SetType> actualData = actual.get(key);
            Set<SetType> cachedData = cached.get(key);

            if(actualData.size() != cachedData.size()){
                return true;
            }

            if(!actualData.equals(cachedData)){
                return true;
            }
        }

        return false;
    }
    
    protected Map<AnalysisType, Set<SetType>> deepCopyMap(Map<AnalysisType, Set<SetType>> outputsOrInputs) {
        Map<AnalysisType, Set<SetType>> result = new HashMap<AnalysisType, Set<SetType>>();
        for(AnalysisType key : outputsOrInputs.keySet()){
            Set<SetType> resultSet = new HashSet<SetType>();
            resultSet.addAll(outputsOrInputs.get(key));
            result.put(key, resultSet);
        }
        return result;
    }
    
    public void run() {
    	runAnalysis(flowGraph, direction, meetOperation, semiLattice);
    }
    
    protected abstract void runAnalysis(FlowGraph flowGraph, Direction direction, Function<List<Set<SetType>>, Set<SetType>> meetOperation, Set<SetType> semiLattice);
}

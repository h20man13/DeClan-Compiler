package edu.depauw.declan.common.ast;

import java.util.List;
import edu.depauw.declan.common.Position;

/**
 * The Branch class is a class that is a super type for all if and while branches supported in Declan
 * It is designed to facilitate Else and Elif branching
 * @author Jacob Bauer
 */
public abstract class Branch extends AbstractASTNode {
    private List<Statement> toExecute;
  
    public Branch(Position start, List<Statement> toExecute){
        super(start);
	this.toExecute = toExecute;
    }
  
    public List<Statement> getExecStatements(){
	return toExecute;
    }
  
}

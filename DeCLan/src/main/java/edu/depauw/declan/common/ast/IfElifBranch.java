package edu.depauw.declan.common.ast;

import java.util.List;
import edu.depauw.declan.common.Position;

public class IfElifBranch extends Branch implements Statement {
    private final Expression toCheck;
    private final Branch branchTo;
    
    public IfElifBranch(Position start, Expression toCheck, List<Statement> toExecute, Branch branchTo){
      super(start, toExecute);
      this.toCheck = toCheck;
      this.branchTo = branchTo;
    }
  
    public List<Statement> getExecStatements(){
      return super.getExecStatements();
    }

    public Expression getExpression(){
      return toCheck;
    }

    public Branch getNextBranch(){
      return branchTo;
    }
  
    @Override
    public void accept(ASTVisitor visitor) {
      visitor.visit(this);
    }

    @Override
    public <R> R acceptResult(StatementVisitor<R> visitor) {
      return visitor.visitResult(this);
    }
}

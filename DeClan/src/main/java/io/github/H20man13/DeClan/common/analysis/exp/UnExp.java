package io.github.H20man13.DeClan.common.analysis.exp;

public class UnExp implements Exp {
    public IdentExp right;
    public Operator op;

    public enum Operator{
        BNOT,
        NEG
    }

    public UnExp(Operator op, IdentExp right){
        this.right = right;
        this.op = op;
    }

    @Override
    public boolean equals(Object exp) {
        if(exp instanceof UnExp){
            UnExp unExp = (UnExp)exp;
            boolean operatorEquals = this.op == unExp.op;
            boolean rightEquals = this.right.equals(unExp.right);
            return operatorEquals && rightEquals;
        } else {
            return false;
        }
    }
}

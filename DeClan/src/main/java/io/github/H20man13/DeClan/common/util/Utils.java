package io.github.H20man13.DeClan.common.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.depauw.declan.common.ast.Identifier;
import edu.depauw.declan.model.SymbolTable;
import io.github.H20man13.DeClan.common.BasicBlock;
import io.github.H20man13.DeClan.common.Tuple;
import io.github.H20man13.DeClan.common.dag.DagNode;
import io.github.H20man13.DeClan.common.dag.DagNodeFactory;
import io.github.H20man13.DeClan.common.dag.DagNullNode;
import io.github.H20man13.DeClan.common.dag.DagOperationNode;
import io.github.H20man13.DeClan.common.dag.DagValueNode;
import io.github.H20man13.DeClan.common.dag.DagVariableNode;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.Goto;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.If;
import io.github.H20man13.DeClan.common.icode.Label;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.BoolExp;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.icode.exp.IntExp;
import io.github.H20man13.DeClan.common.icode.exp.RealExp;
import io.github.H20man13.DeClan.common.icode.exp.StrExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp.Operator;
import io.github.H20man13.DeClan.common.pat.P;
import io.github.H20man13.DeClan.common.symboltable.Environment;
import io.github.H20man13.DeClan.common.symboltable.LiveInfo;
import io.github.H20man13.DeClan.common.token.IrTokenType;

public class Utils {
    private static DagNodeFactory factory = new DagNodeFactory();

    public static List<ICode> stripFromListExcept(List<ICode> list, ICode item){
        List<ICode> linkedList = new LinkedList<ICode>();

        for(ICode listItem : list){
            if(listItem.hashCode() != item.hashCode()){
                linkedList.add(listItem);
            }
        }

        return linkedList;
    }

    public static boolean setContainsExp(Set<Exp> returnSet, Exp exp){
        for(Exp expInSet : returnSet){
            if(expInSet.equals(exp)){
                return true;
            }
        }
        return false;
    }

    public static BinExp.Operator toBinOp(IrTokenType type){
        switch(type){
            case NE: return BinExp.Operator.NE;
            case EQ: return BinExp.Operator.EQ;
            case GE: return BinExp.Operator.GE;
            case GT: return BinExp.Operator.GT;
            case LE: return BinExp.Operator.LE;
            case LT: return BinExp.Operator.LT;
            case ADD: return BinExp.Operator.ADD;
            case SUB: return BinExp.Operator.SUB;
            case MUL: return BinExp.Operator.MUL;
            case DIV: return BinExp.Operator.DIV;
            case MOD: return BinExp.Operator.MOD;
            case BOR: return BinExp.Operator.BOR;
            case BAND: return BinExp.Operator.BAND;
            default: return null;
        }
    }

    public static Object getValueFromSet(Set<Tuple<String, Object>> tuples, String name){
        for(Tuple<String, Object> tuple : tuples){
            if(tuple.source.equals(name)){
                return tuple.dest;
            }
        }
        return null;
    }

    public static BinExp.Operator getBinOp(DagOperationNode.Op op){
        switch(op){
            case ADD: return BinExp.Operator.ADD;
            case SUB: return BinExp.Operator.SUB;
            case MUL: return BinExp.Operator.MUL;
            case DIV: return BinExp.Operator.DIV;
            case MOD: return BinExp.Operator.MOD;
            case BAND: return BinExp.Operator.BAND;
            case BOR: return BinExp.Operator.BOR;
            case EQ: return BinExp.Operator.EQ;
            case NE: return BinExp.Operator.NE;
            case GE: return BinExp.Operator.GE;
            case GT: return BinExp.Operator.GT;
            case LE: return BinExp.Operator.LE;
            case LT: return BinExp.Operator.LT;
            default: return null;
        }
    }

    public static UnExp.Operator getUnOp(DagOperationNode.Op op){
        switch(op){
            case NEG: return UnExp.Operator.NEG;
            case BNOT: return UnExp.Operator.BNOT;
            default: return null;
        }
    }

    public static String getIdentifier(DagNode node, Environment<String, LiveInfo> table){
        List<String> identifiers = node.getIdentifiers();
        for(String identifier : identifiers){
            if(table.entryExists(identifier)){
                LiveInfo life = table.getEntry(identifier);
                if(life.isAlive){
                    return identifier;
                }
            }
        }

        if(identifiers.size() > 0){
            return identifiers.get(0);
        } else {
            return null;
        }
    }

    public static Exp valueToExp(Object result) {
        if(result instanceof Boolean){
            return new BoolExp((boolean)result);
        } else if(result instanceof Integer){
            return new IntExp((int)result);
        } else if(result instanceof String){
            return new StrExp((String)result);
        } else if(result instanceof Double){
            return new RealExp((double)result);
        } else {
            return null;
        }
    }

    public static Object getValue(Exp value) {
        if(value instanceof BoolExp){
            return ((BoolExp)value).trueFalse;
        } else if(value instanceof IntExp){
            return ((IntExp)value).value;
        } else if(value instanceof RealExp){
            return ((RealExp)value).realValue;
        } else if(value instanceof StrExp){
            return ((StrExp)value).value;
        } else {
            return null;
        }
    }

    public static DagNode createBinaryNode(BinExp.Operator op, String place, DagNode left, DagNode right) {
        switch(op){
            case ADD: return factory.createAdditionNode(place, left, right);
            case SUB: return factory.createSubtractionNode(place, left, right);
            case MUL: return factory.createMultiplicationNode(place, left, right);
            case DIV: return factory.createDivisionNode(place, left, right);
            case BAND: return factory.createAndNode(place, left, right);
            case MOD: return factory.createModuleNode(place, left, right);
            case BOR: return factory.createOrNode(place, left, right);
            case GT: return factory.createGreaterThanNode(place, left, right);
            case GE: return factory.createGreaterThanOrEqualNode(place, left, right);
            case LT: return factory.createLessThanNode(place, left, right);
            case LE: return factory.createLessThanOrEqualNode(place, left, right);
            case EQ: return factory.createEqualsNode(place, left, right);
            case NE: return factory.createNotEqualsNode(place, left, right);
            default: return null;
        }
    }

    public static DagNode createUnaryNode(UnExp.Operator op, String place, DagNode right){
        switch(op){
            case NEG: return factory.createNegationNode(place, right);
            case BNOT: return factory.createNotNode(place, right);
            default: return null;
        }
    }

    public static boolean beginningOfBlockIsLabel(BasicBlock block){
        List<ICode> codeInBlock = block.getIcode();
        if(codeInBlock.size() > 0){
            ICode firstICode = codeInBlock.get(0);
            if(firstICode instanceof Label){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean endOfBlockIsJump(BasicBlock block){
        List<ICode> codeInBlock = block.getIcode();
        if(codeInBlock.size() > 0){
            ICode lastICode = codeInBlock.get(codeInBlock.size() - 1);
            if(lastICode instanceof If){
                return true;
            } else if(lastICode instanceof Goto){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static P binOpToPattern(BinExp.Operator op){
        switch(op){
            case ADD: return P.ADD();
            case SUB: return P.SUB();
            case MUL: return P.MUL();
            case DIV: return P.DIV();
            case BAND: return P.BAND();
            case MOD: return P.MOD();
            case BOR: return P.BOR();
            case GT: return P.GT();
            case GE: return P.GE();
            case LT: return P.LT();
            case LE: return P.LE();
            case EQ: return P.EQ();
            case NE: return P.NE();
            default: return null;
        }
    }

    public static P unOpToPattern(UnExp.Operator op){
        switch(op){
            case NEG: return P.NEG();
            case BNOT: return P.BNOT();
            default: return null;
        }
    }
}

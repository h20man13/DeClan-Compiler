package io.github.H20man13.DeClan.common.builder;

import java.util.LinkedList;
import java.util.List;

import edu.depauw.declan.common.ErrorLog;
import io.github.H20man13.DeClan.common.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.Tuple;
import io.github.H20man13.DeClan.common.builder.section.SymbolSectionBuilder;
import io.github.H20man13.DeClan.common.builder.template.ResetableBuilder;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp.Operator;
import io.github.H20man13.DeClan.main.MyIrFactory;

public abstract class AssignmentBuilder implements ResetableBuilder{
    protected List<ICode> intermediateCode;
    private IrBuilderContext ctx;
    private IrRegisterGenerator gen;
    private MyIrFactory factory;
    private SymbolSectionBuilder symbols;
    
    protected AssignmentBuilder(SymbolSectionBuilder symbols, IrBuilderContext ctx, IrRegisterGenerator gen, ErrorLog errLog){
        this.ctx = ctx;
        this.factory = new MyIrFactory(errLog);
        this.symbols = symbols;
        resetBuilder();
    }

    public SymbolSectionBuilder getSymbolSectionBuilder(){
        return symbols;
    }

    public String buildStringAssignment(String value){
        String place = gen.genNextRegister();
        intermediateCode.add(factory.produceStringAssignment(place, value));
        return place;
    }

    public String buildBoolAssignment(String value){
        String place = gen.genNextRegister();
        Assign result = null;
        if(value.equals("TRUE")){
            result = factory.produceBooleanAssignment(place, true);
        } else {
            result = factory.produceBooleanAssignment(place, false);
        }
        intermediateCode.add(result);
        return place;
    }

    public String buildNumAssignment(String value){
        Assign result = null;
        String place = gen.genNextRegister();
        if(value.contains(".")){
            result = factory.produceRealAssignment(place, Double.parseDouble(value));
        } else {
            result = factory.produceIntAssignment(place, Integer.parseInt(value));
        }
        intermediateCode.add(result);
        return place;
    }

    public String buildRealNegationAssignment(Exp value){
        String place = gen.genNextRegister();
        UnExp unaryExp = new UnExp(UnExp.Operator.RNEG, value);
        intermediateCode.add(factory.produceUnaryOperation(place, unaryExp));
        return place;
    }

    public String buildIntegerNegationAssignment(Exp value){
        String place = gen.genNextRegister();
        UnExp unaryExp = new UnExp(UnExp.Operator.INEG, value);
        intermediateCode.add(factory.produceUnaryOperation(place, unaryExp));
        return place;
    }

    public String buildIntegerNotAssignment(Exp value){
        String place = gen.genNextRegister();
        UnExp unExp = new UnExp(Operator.INOT, value);
        intermediateCode.add(factory.produceUnaryOperation(place, unExp));
        return place;
    }

    public String buildNotAssignment(Exp value){
        String place = gen.genNextRegister();
        UnExp unExp = new UnExp(UnExp.Operator.BNOT, value);
        intermediateCode.add(factory.produceUnaryOperation(place, unExp));
        return place;
    }

    public String buildIntegerAdditionAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IADD ,right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildRealAdditionAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.RADD ,right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerSubtractionAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.ISUB, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildRealSubtractionAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.RSUB, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerMultiplicationAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IMUL, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildRealMultiplicationAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.RMUL, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerDivAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IDIV, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildRealDivisionAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.RDIVIDE, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerModuloAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IMOD, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildLessThanOrEqualAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.LE, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildLessThanAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.LT, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildGreaterThanOrEqualToAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.GE, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildGreaterThanAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.GT, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildLogicalAndAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.LAND, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerOrAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IOR, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerAndAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IAND, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildIntegerExclusiveOrAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IXOR, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildLogicalOrAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.LOR, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildLeftShiftAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.ILSHIFT, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildRightShiftAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.IRSHIFT, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildEqualityAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.EQ, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public String buildInequalityAssignment(Exp left, Exp right){
        String place = gen.genNextRegister();
        BinExp binExp = new BinExp(left, BinExp.Operator.NE, right);
        intermediateCode.add(factory.produceBinaryOperation(place, binExp));
        return place;
    }

    public void buildVariableAssignment(String place, String value){
        intermediateCode.add(factory.produceVariableAssignment(place, value));
    }

    public String buildVariableAssignment(String value){ 
        String place = gen.genNextRegister();
        intermediateCode.add(factory.produceVariableAssignment(place, value));
        return place;
    }

    public void buildProcedureCall(String name, List<Tuple<String, String>> args){
        intermediateCode.add(factory.produceProcedure(name, args));
    }

    public String buildExternalReturnPlacement(String dest){
        String reg = gen.genNextRegister();
        intermediateCode.add(factory.procuceExternalReturnPlacement(reg, dest));
        return reg;
    }

    public void resetBuilder(){
        this.intermediateCode = new LinkedList<ICode>();
    }

    public abstract String buildParamaterAssignment(String place);
}
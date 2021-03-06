package edu.depauw.declan.main;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.ast.ASTVisitor;
import edu.depauw.declan.common.ast.BinaryOperation;
import edu.depauw.declan.common.ast.ConstDeclaration;
import edu.depauw.declan.common.ast.VariableDeclaration;
import edu.depauw.declan.common.ast.ProcedureDeclaration;
import edu.depauw.declan.common.ast.Declaration;
import edu.depauw.declan.common.ast.EmptyStatement;
import edu.depauw.declan.common.ast.IfElifBranch;
import edu.depauw.declan.common.ast.WhileElifBranch;
import edu.depauw.declan.common.ast.ForBranch;
import edu.depauw.declan.common.ast.Expression;
import edu.depauw.declan.common.ast.ElseBranch;
import edu.depauw.declan.common.ast.RepeatBranch;
import edu.depauw.declan.common.ast.Branch;
import edu.depauw.declan.common.ast.ExpressionVisitor;
import edu.depauw.declan.common.ast.Identifier;
import edu.depauw.declan.common.ast.NumValue;
import edu.depauw.declan.common.ast.StrValue;
import edu.depauw.declan.common.ast.BoolValue;
import edu.depauw.declan.common.ast.ProcedureCall;
import edu.depauw.declan.common.ast.FunctionCall;
import edu.depauw.declan.common.ast.Program;
import edu.depauw.declan.common.ast.UnaryOperation;
import edu.depauw.declan.common.ast.Statement;
import edu.depauw.declan.common.ast.Assignment;

import edu.depauw.declan.common.icode.LetVar;
import edu.depauw.declan.common.icode.LetInt;
import edu.depauw.declan.common.icode.LetReal;
import edu.depauw.declan.common.icode.LetUn;
import edu.depauw.declan.common.icode.LetBin;
import edu.depauw.declan.common.icode.LetString;
import edu.depauw.declan.common.icode.LetBool;
import edu.depauw.declan.common.icode.Label;
import edu.depauw.declan.common.icode.ICode;
import edu.depauw.declan.common.icode.If;
import edu.depauw.declan.common.icode.Goto;
import edu.depauw.declan.common.icode.End;
import edu.depauw.declan.common.icode.LetString;
import edu.depauw.declan.common.icode.Return;
import edu.depauw.declan.common.icode.Call;
import edu.depauw.declan.common.icode.Proc;

import edu.depauw.declan.common.symboltable.VariableEntry;
import edu.depauw.declan.common.symboltable.ProcedureEntry;
import edu.depauw.declan.common.symboltable.Environment;

import static edu.depauw.declan.common.MyIO.*;
import static edu.depauw.declan.common.RegisterGenerator.*;

import java.lang.Number;
import java.lang.Object;
import java.lang.Math;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *The my interpreter class is a visitor object that can interpret the entire DeClan Language
 * It also takes in an error log object in order to record errors
 *@author Jacob Bauer
 */

public class MyCodeGenerator implements ASTVisitor, ExpressionVisitor<String> {
  private ErrorLog errorLog;
  private Environment <String, String> varEnvironment;
  private Environment <String, String> procEnvironment;
  private List<ICode> icode;
  private MyTypeChecker typeChecker;
  // TODO declare any data structures needed by the interpreter

    public MyCodeGenerator(ErrorLog errorLog, MyTypeChecker typeChecker) {
	this.errorLog = errorLog;
	this.typeChecker = typeChecker;
	this.varEnvironment = new Environment<>();
	this.procEnvironment = new Environment<>();
	this.icode = new ArrayList<>();
    }

    public List<ICode> getCode(){
	return this.icode;
    }
  @Override
  public void visit(Program program) {
    procEnvironment.addScope();
    varEnvironment.addScope();
    for (Declaration Decl : program.getDecls()) {
      Decl.accept(this);
    }
    for (Statement statement : program.getStatements()) {
      statement.accept(this);
    }
    varEnvironment.removeScope();
    procEnvironment.removeScope();
    icode.add(new End());
  }

    //this function is needed to change a Hex String from the Declan Format to the expected Java format
  private static String ifHexToInt(String lexeme){
    if(lexeme.charAt(0) == '0' && lexeme.length() > 1 && !lexeme.contains(".")){ //is it a hex number
      Long value = Long.parseLong(lexeme.substring(1, lexeme.length() - 1), 16);  
      return ("" + value);
    } else {
      return lexeme; //else return input it is fine
    }
  }
  
  @Override
  public void visit(ConstDeclaration constDecl) {
    Identifier id = constDecl.getIdentifier();
    Expression valueExpr = constDecl.getValue();
    String value = valueExpr.acceptResult(this);
    String place = genNextRegister();
    icode.add(new LetVar(place, value));
    varEnvironment.addEntry(id.getLexeme(), place);
  }

  @Override
  public void visit(VariableDeclaration varDecl) {
    Identifier id = varDecl.getIdentifier();
    String place = genNextRegister();
    varEnvironment.addEntry(id.getLexeme(), place);
  }

  @Override
  public void visit(ProcedureDeclaration procDecl){
    String procedureName = procDecl.getProcedureName().getLexeme();
    icode.add(new Label(procedureName));
    varEnvironment.addScope();
    List <VariableDeclaration> args = procDecl.getArguments();
    for(int i = 0; i < args.size(); i++){
	args.get(i).accept(this);
    }
    List <Declaration> localVars = procDecl.getLocalVariables();
    for(int i = 0; i < localVars.size(); i++){
	localVars.get(i).accept(this);
    }
    List <Statement> Exec = procDecl.getExecutionStatements();
    for(int i = 0; i < Exec.size(); i++){
	Exec.get(i).accept(this);
    }
    Expression retExp = procDecl.getReturnStatement();
    String retPlace = retExp.acceptResult(this);
    String place = genNextRegister();
    icode.add(new LetVar(place, retPlace));
    procEnvironment.addEntry(procedureName, place);
    icode.add(new Return());
    varEnvironment.removeScope();
  }
        
  @Override
  public void visit(ProcedureCall procedureCall) {
    String funcName = procedureCall.getProcedureName().getLexeme();
    List<Expression> valArgs = procedureCall.getArguments();
    List<String> valArgResults = new ArrayList<>();
    for(Expression valArg : valArgs){
	String result = valArg.acceptResult(this);
	valArgResults.add(result);
    }
    icode.add(new Proc(funcName, valArgResults));
  }


  private int WHILESEQNUM = 0;
  private int WHILENUM = 0;

  @Override
  public void visit(WhileElifBranch whilebranch){
    Expression toCheck = whilebranch.getExpression();
    List<Statement> toExec = whilebranch.getExecStatements();
      String test = toCheck.acceptResult(this);
      icode.add(new If(test, "WHILESTAT" +  WHILENUM + "_SEQ_" + WHILESEQNUM, "WHILENEXT" + WHILENUM + "_SEQ_" + WHILESEQNUM)); //Entry Point into series
      icode.add(new Label("WHILECOND" + WHILENUM + "_SEQ_" + WHILESEQNUM)); //label for looping
      icode.add(new If(test, "WHILESTAT" + WHILENUM + "_SEQ_" + WHILESEQNUM, "WHILEEND" + WHILENUM)); //Exit the Loop if the condition fails
      icode.add(new Label("WHILESTAT" +  WHILENUM + "_SEQ_" + WHILESEQNUM));
      for(int i = 0; i < toExec.size(); i++){
	  toExec.get(i).accept(this);
      }
      icode.add(new Goto("WHILECOND" + WHILENUM + "_SEQ_" + WHILESEQNUM));
      if(whilebranch.getNextBranch() != null) {
	icode.add(new Label("WHILENEXT" +  WHILENUM + "_SEQ_" + WHILESEQNUM));
        WHILESEQNUM++;
        whilebranch.getNextBranch().accept(this);
      } else {
	  icode.add(new Label("WHILEEND" + WHILENUM));
	  WHILESEQNUM = 0;
	  WHILENUM++;
      }
  }
    
  private int IFSEQNUM = 0;
  private int IFNUM = 0;
    
  @Override
  public void visit(IfElifBranch ifbranch){
      Expression toCheck = ifbranch.getExpression();
      String test = toCheck.acceptResult(this);
      icode.add(new If(test, "IFSTAT" +  IFNUM + "_SEQ_" + IFSEQNUM, "IFNEXT" + IFNUM + "_SEQ_" + IFSEQNUM));
      List<Statement> toExec = ifbranch.getExecStatements();
      icode.add(new Label("IFSTAT" +  IFNUM + "_SEQ_" + IFSEQNUM));
      for(int i = 0; i < toExec.size(); i++){
	  toExec.get(i).accept(this);
      }
      icode.add(new Goto("IFEND" + IFNUM));
      if(ifbranch.getNextBranch() != null) {
	icode.add(new Label("IFNEXT" +  IFNUM + "_SEQ_" + IFSEQNUM));
        IFSEQNUM++;
        ifbranch.getNextBranch().accept(this);
      } else {
	  icode.add(new Label("IFEND" + IFNUM));
	  IFSEQNUM = 0;
	  IFNUM++;
      }
  }

  @Override
  public void visit(ElseBranch elsebranch){
    List<Statement> toExec = elsebranch.getExecStatements();
    for(int i = 0; i < toExec.size(); i++){
      toExec.get(i).accept(this);
    }
    icode.add(new Label("IFEND" + IFNUM));
    IFSEQNUM = 0;
    IFNUM++;
  }

  private int REPEATNUM = 0;
  @Override
  public void visit(RepeatBranch repeatbranch){
    Expression toCheck = repeatbranch.getExpression();
    List<Statement> toExec = repeatbranch.getExecStatements();
    icode.add(new Label("REPEATBEG" + REPEATNUM));
    for(int i = 0; i < toExec.size(); i++){
	toExec.get(i).accept(this);
    }
    String test = toCheck.acceptResult(this);
    icode.add(new If(test, "REPEATEND" + REPEATNUM, "REPEATBEG" + REPEATNUM));
    icode.add(new Label("REPEATEND" + REPEATNUM));
    REPEATNUM++;
  }

  private int FORNUM = 0; 
  @Override
  public void visit(ForBranch forbranch){
    Expression toMod = forbranch.getModifyExpression();
    List<Statement> toExec = forbranch.getExecStatements();
    if(toMod != null){
      String incriment = toMod.acceptResult(this);
      MyTypeChecker.TypeCheckerTypes incrimentType = toMod.acceptResult(typeChecker);
      if(incrimentType == MyTypeChecker.TypeCheckerTypes.REAL){
	  forbranch.getInitAssignment().accept(this);
	  String target = forbranch.getTargetExpression().acceptResult(this);
	  String curvalue = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new Label("FORBEG" + FORNUM));
	  icode.add(new If(curvalue, If.Op.NE, target, "FORLOOP" + FORNUM, "FOREND" + FORNUM));
	  icode.add(new Label("FORLOOP" + FORNUM));
	  for(int i = 0; i < toExec.size(); i++){
	      toExec.get(i).accept(this);
	  }
	  String entry = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new LetBin(curvalue, curvalue, LetBin.Op.RADD, incriment));
	  icode.add(new Goto("FORBEG" + FORNUM));
	  icode.add(new Label("FOREND" + FORNUM));
      } else {
	  forbranch.getInitAssignment().accept(this);
	  String target = forbranch.getTargetExpression().acceptResult(this);
	  String curvalue = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new Label("FORBEG" + FORNUM));
	  icode.add(new If(target, If.Op.NE, curvalue, "FORLOOP" + FORNUM, "FOREND" + FORNUM));
	  icode.add(new Label("FORLOOP" + FORNUM));
	  for(int i = 0; i < toExec.size(); i++){
	      toExec.get(i).accept(this);
	  }
	  String entry = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new LetBin(curvalue, curvalue, LetBin.Op.IADD, incriment));
	  icode.add(new Goto("FORBEG" + FORNUM));
	  icode.add(new Label("FOREND" + FORNUM));
      }
    } else {
          forbranch.getInitAssignment().accept(this);
	  String target = forbranch.getTargetExpression().acceptResult(this);
	  String curvalue = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new Label("FORBEG" + FORNUM));
	  icode.add(new If(target, If.Op.NE, curvalue, "FORLOOP" + FORNUM, "FOREND" + FORNUM));
	  icode.add(new Label("FORLOOP" + FORNUM));
	  for(int i = 0; i < toExec.size(); i++){
	      toExec.get(i).accept(this);
	  }
	  String entry = varEnvironment.getEntry(forbranch.getInitAssignment().getVariableName().getLexeme());
	  icode.add(new Goto("FORBEG" + FORNUM));
	  icode.add(new Label("FOREND" + FORNUM));
    }
    FORNUM++;
  }
        
  @Override
  public void visit(Assignment assignment) {
    String place = varEnvironment.getEntry(assignment.getVariableName().getLexeme());
    String value = assignment.getVariableValue().acceptResult(this);
    icode.add(new LetVar(place, value));
  }
  
  @Override
  public void visit(EmptyStatement emptyStatement) {
    // Not used
  }
  @Override
  public void visit(UnaryOperation unaryOperation) {
    // Not used
  }

  @Override
  public void visit(BinaryOperation binaryOperation) {
    // Not used
  }

  @Override
  public void visit(NumValue numValue) {
    // Not used
  }

  @Override
  public void visit(StrValue numValue) {
    // Not used
  }

  @Override
  public void visit(BoolValue boolValue) {
    // Not used
  }

  @Override
  public void visit(Identifier identifier) {
    // Not used
  }

  @Override
  public void visit(FunctionCall funcCall){
    // Not used
  }
  
  @Override
  public String visitResult(BinaryOperation binaryOperation) {
      String leftValue = binaryOperation.getLeft().acceptResult(this);
      MyTypeChecker.TypeCheckerTypes leftType = binaryOperation.getLeft().acceptResult(typeChecker);
      String rightValue = binaryOperation.getRight().acceptResult(this);
      MyTypeChecker.TypeCheckerTypes rightType = binaryOperation.getRight().acceptResult(typeChecker);
      String place = genNextRegister();
      icode.add(new LetBin(place, leftValue, LetBin.getOp(leftType, binaryOperation.getOperator(), rightType), rightValue));
      return place;
  }

  @Override
  public String visitResult(FunctionCall funcCall) {
    String funcName = funcCall.getFunctionName().getLexeme();
    List<Expression> valArgs = funcCall.getArguments();
    List<String> valArgResults = new ArrayList<>();
    for(Expression valArg : valArgs){
	String result = valArg.acceptResult(this);
	valArgResults.add(result);
    }
    icode.add(new Call(funcName, valArgResults));
    return procEnvironment.getEntry(funcName);
  }

  @Override
  public String visitResult(UnaryOperation unaryOperation) {
    String value = unaryOperation.getExpression().acceptResult(this);
    MyTypeChecker.TypeCheckerTypes type = unaryOperation.getExpression().acceptResult(typeChecker);
    switch(type){
        case INTEGER:
	    switch(unaryOperation.getOperator()){
	    case MINUS:
		String place = genNextRegister();
		icode.add(new LetUn(place, LetUn.Op.INEG, value));
                return place;
	    default:
		return value;
	    }
        case REAL:
	    switch(unaryOperation.getOperator()){
	    case MINUS:
		String place = genNextRegister();
		icode.add(new LetUn(place, LetUn.Op.RNEG, value));
                return place;
	    default:
		return value;
	    }
       case BOOLEAN:
	   switch(unaryOperation.getOperator()){
	   case NOT:
	       String place = genNextRegister();
	       icode.add(new LetUn(place, LetUn.Op.BNOT, value));
	       return place;
	   default:
	       return value;
	   }
    }
    return "";
  }
    
  @Override
  public String visitResult(Identifier identifier){
    String place = varEnvironment.getEntry(identifier.getLexeme());
    return place;
  }

  @Override
  public String visitResult(NumValue numValue){
      String rawnum = ifHexToInt(numValue.getLexeme());
      String place = genNextRegister();
      if(rawnum.contains(".")){
	  icode.add(new LetReal(place, Double.parseDouble(rawnum)));
      } else {
	  icode.add(new LetInt(place, Integer.parseInt(rawnum)));
      }
      return place;
  }

  @Override
  public String visitResult(BoolValue boolValue){
    String lexeme = boolValue.getLexeme(); //change to hex if you need to otherwise unchanged
    String place = genNextRegister();
    if(lexeme.equals("TRUE")){
	icode.add(new LetBool(place, true));
    } else {
	icode.add(new LetBool(place, false));
    }
    return place;
  }

  @Override
  public String visitResult(StrValue strValue){
      String place = genNextRegister();
      icode.add(new LetString(place, strValue.getLexeme()));
      return place;
  }
}

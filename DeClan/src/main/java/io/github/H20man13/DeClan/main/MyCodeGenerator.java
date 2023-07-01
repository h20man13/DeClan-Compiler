package io.github.H20man13.DeClan.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.Position;
import io.github.H20man13.DeClan.common.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.analysis.LiveVariableAnalysis;
import io.github.H20man13.DeClan.common.arm.ArmCodeGenerator;
import io.github.H20man13.DeClan.common.pat.P;
import io.github.H20man13.DeClan.common.pat.Pattern;
import io.github.H20man13.DeClan.common.arm.ArmRegisterGenerator;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.If;
import io.github.H20man13.DeClan.common.icode.Label;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.BoolExp;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.icode.exp.IntExp;
import io.github.H20man13.DeClan.common.icode.exp.RealExp;
import io.github.H20man13.DeClan.common.icode.exp.StrExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;

public class MyCodeGenerator {
    private List<ICode> intermediateCode;
    private ArmRegisterGenerator rGen;
    private ArmCodeGenerator cGen;
    private IrRegisterGenerator iGen;
    private ErrorLog errorLog;
    private Map<P, Callable<Void>> codeGenFunctions;

    private int i;

    public MyCodeGenerator(File outputFile, LiveVariableAnalysis analysis, List<ICode> intermediateCode, IrRegisterGenerator iGen, ErrorLog errorLog){
        this.intermediateCode = intermediateCode;
        this.cGen = new ArmCodeGenerator();
        this.rGen = new ArmRegisterGenerator(cGen, analysis);
        this.iGen = iGen;
        this.errorLog = errorLog;
        this.codeGenFunctions = new HashMap<>();
        this.i = 0;
        initCodeGenFunctions();
    }

    public void codeGen() throws Exception{
        for(i = 0; i < intermediateCode.size(); i++){
            P possibleMultiplicationAndAdditionPattern = null;
            if(i + 1 < intermediateCode.size()){
                possibleMultiplicationAndAdditionPattern = P.PAT(intermediateCode.get(i).asPattern(), intermediateCode.get(i + 1).asPattern());
            }

            P defaultInstructionPattern = intermediateCode.get(i).asPattern();


            if(possibleMultiplicationAndAdditionPattern != null){
                if(codeGenFunctions.containsKey(possibleMultiplicationAndAdditionPattern)){
                    codeGenFunctions.get(possibleMultiplicationAndAdditionPattern).call();
                } else if(codeGenFunctions.containsKey(defaultInstructionPattern)) {
                    codeGenFunctions.get(defaultInstructionPattern).call();
                } else {
                    errorLog.add("Pattern \n\n" + defaultInstructionPattern.toString() + "\n\n" + "not found", new Position(i, 0));
                    break;
                }
            } else {
                if(codeGenFunctions.containsKey(defaultInstructionPattern)){
                    codeGenFunctions.get(defaultInstructionPattern).call();
                } else {
                    errorLog.add("Pattern \n\n" + defaultInstructionPattern.toString() + "\n\n" + "not found", new Position(i, 0));
                    break;
                }
            }
        }
    }

    private void initCodeGenFunctions() {
        //Init Multiply and Accumulate Patterns
        initMultiplyAndAccumulate0();
        initMultiplyAndAccumulate1();
        initMultiplyAndAccumulate2();
        initMultiplyAndAccumulate3();
        initMultiplyAndAccumulate4();
        initMultiplyAndAccumulate5();
        initMultiplyAndAccumulate6();
        initMultiplyAndAccumulate7();
        initMultiplyAndAccumulate8();
        initMultiplyAndAccumulate9();
        initMultiplyAndAccumulate10();
        initMultiplyAndAccumulate11();

        //Init Add Patterns
        initAdd0();
        initAdd1();
        initAdd2();
        initAdd3();
        
        //Init Sub Patterns
        initSub0();
        initSub1();
        initSub2();
        initSub3();

        //Init Mul Patterns
        initMul0();
        initMul1();
        initMul2();
        initMul3();

        //Init Div patterns
        initDiv0();
        initDiv1();
        initDiv2();
        initDiv3();

        //Init Mod patterns
        initMod0();
        initMod1();
        initMod2();
        initMod3();

        //Init Ge patterns
        initGe0();
        initGe1();
        initGe2();
        initGe3();

        //Init Gt Patterns
        initGt0();
        initGt1();
        initGt2();
        initGt3();

        //Init Lt Patterns
        initLt0();
        initLt1();
        initLt2();
        initLt3();

        //Init Le Patterns
        initLe0();
        initLe1();
        initLe2();
        initLe3();

        //Init eq Patterns
        initEq0();
        initEq1();
        initEq2();
        initEq3();
        initEq4();
        initEq5();
        initEq6();

        //Initialize the ne Patterns
        initNe0();
        initNe1();
        initNe2();
        initNe3();
        initNe4();
        initNe5();
        initNe6();

        //Initialize the Neg patterns
        initNeg0();
        initNeg1();

        //Initiaize the BNot patterns
        initBnot0();
        initBnot1();

        //Initialize Bool Constnat Patterns
        initBool0();
        //Initialize Int Constant Patterns
        initInt0();

        //Initialize If Statement Patterns
        initIf0();
        initIf1();
        initIf2();
        initIf3();

        initIf4();
        initIf5();
        initIf6();
        initIf7();

        initIf8();
        initIf9();
        initIf10();
        initIf11();
        
        initIf12();
        initIf13();
        initIf14();
        initIf15();

        initIf16();
        initIf17();
        initIf18();
        initIf19();
        initIf20();
        initIf21();
        initIf22();


    }

    private void initMultiplyAndAccumulate0(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);
                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident1Right.ident);

                if(ass1.place.equals(ident2Left.ident) || ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    if(ass1.place.equals(ident2Left.ident)){
                        String place2Right = rGen.getReg(ident2Right.ident, icode2);
                        cGen.addInstruction("LDR " + place2Right + ", " + ident2Right.ident);
                        cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                        cGen.addVariable(ass2.place);
                        cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    } else {
                         String place2Left = rGen.getReg(ident2Left.ident, icode2);
                         cGen.addInstruction("LDR " + place2Left + ", " + ident2Left.ident);
                         cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                         cGen.addVariable(ass2.place);
                         cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    }
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);
                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }  
        });
    }

    private void initMultiplyAndAccumulate1(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate1, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String temp = iGen.genNextRegister();
                String place1Left = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Left.value);

                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + temp);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident2Right.ident);

                if(ass1.place.equals(ident2Left.ident) || ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    if(ass1.place.equals(ident2Left.ident)){
                        String place2Right = rGen.getReg(ident2Right.ident, icode2);
                        cGen.addInstruction("LDR " + place2Right + ", " + ident2Right.ident);
                        cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                        cGen.addVariable(ass2.place);
                        cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    } else {
                         String place2Left = rGen.getReg(ident2Left.ident, icode2);
                         cGen.addInstruction("LDR " + place2Left + ", " + ident2Left.ident);
                         cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                         cGen.addVariable(ass2.place);
                         cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    }
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);
                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }
                return null;
            } 
        });
    }

    private void initMultiplyAndAccumulate2(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate2, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);

                String temp = iGen.genNextRegister();
                String place1Right = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + temp);

                if(ass1.place.equals(ident2Left.ident) || ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    if(ass1.place.equals(ident2Left.ident)){
                        String place2Right = rGen.getReg(ident2Right.ident, icode2);
                        cGen.addInstruction("LDR " + place2Right + ", " + ident2Right.ident);
                        cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                        cGen.addVariable(ass2.place);
                        cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    } else {
                         String place2Left = rGen.getReg(ident2Left.ident, icode2);
                         cGen.addInstruction("LDR " + place2Left + ", " + ident2Left.ident);
                         cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                         cGen.addVariable(ass2.place);
                         cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    }
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);
                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    public void initMultiplyAndAccumulate3(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String tempLeft = iGen.genNextRegister();
                String place1Left = rGen.getReg(tempLeft, icode1);
                cGen.addVariable(tempLeft, ".WORD " + int1Left.value);


                String tempRight = iGen.genNextRegister();
                String place1Right = rGen.getReg(tempRight, icode1);
                cGen.addVariable(tempRight, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + tempLeft);
                cGen.addInstruction("LDR " + place1Right +  ", " + tempRight);

                if(ass1.place.equals(ident2Left.ident) || ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    if(ass1.place.equals(ident2Left.ident)){
                        String place2Right = rGen.getReg(ident2Right.ident, icode2);
                        cGen.addInstruction("LDR " + place2Right + ", " + ident2Right.ident);
                        cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                        cGen.addVariable(ass2.place);
                        cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    } else {
                         String place2Left = rGen.getReg(ident2Left.ident, icode2);
                         cGen.addInstruction("LDR " + place2Left + ", " + ident2Left.ident);
                         cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                         cGen.addVariable(ass2.place);
                         cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                    }
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);
                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    private void initMultiplyAndAccumulate4(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate4, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IntExp int2Left = (IntExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);
                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident1Right.ident);

                if(ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);
                    String temp = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp, icode2);
                    cGen.addVariable(temp, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String temp = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp, icode2);
                    cGen.addVariable(temp, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp);

                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }  
        });
    }

    private void initMultiplyAndAccumulate5(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate5, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IntExp int2Left = (IntExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String temp = iGen.genNextRegister();
                String place1Left = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Left.value);

                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + temp);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident2Right.ident);

                if(ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Left = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp2Left, icode2);
                    cGen.addVariable(temp2Left, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Left);

                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String temp2Left = iGen.genNextRegister(); 
                    String place2Left = rGen.getReg(temp2Left, icode2);
                    cGen.addVariable(temp2Left, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Left);

                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }
                return null;
            } 
        });
    }

    private void initMultiplyAndAccumulate6(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate6, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IntExp int2Left = (IntExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);

                String temp = iGen.genNextRegister();
                String place1Right = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + temp);

                if(ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Left = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp2Left, icode2);
                    cGen.addVariable(temp2Left, ".WORD " + int2Left.value);

                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Left);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String temp2Left = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp2Left, icode2);
                    cGen.addVariable(temp2Left, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Left);

                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    public void initMultiplyAndAccumulate7(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate7, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IntExp int2Left = (IntExp)exp2.left;
                IdentExp ident2Right = (IdentExp)exp2.right;

                String tempLeft = iGen.genNextRegister();
                String place1Left = rGen.getReg(tempLeft, icode1);
                cGen.addVariable(tempLeft, ".WORD " + int1Left.value);


                String tempRight = iGen.genNextRegister();
                String place1Right = rGen.getReg(tempRight, icode1);
                cGen.addVariable(tempRight, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + tempLeft);
                cGen.addInstruction("LDR " + place1Right +  ", " + tempRight);

                if(ass1.place.equals(ident2Right.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Place = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp2Place, icode2);
                    cGen.addVariable(temp2Place, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Place);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String temp2Place = iGen.genNextRegister();
                    String place2Left = rGen.getReg(temp2Place, icode2);
                    cGen.addVariable(temp2Place, ".WORD " + int2Left.value);
                    cGen.addInstruction("LDR " + place2Left + ", " + temp2Place);

                    String place2Right = rGen.getReg(ident2Right.ident, icode2);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    private void initMultiplyAndAccumulate8(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate8, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IntExp int2Right = (IntExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);
                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident1Right.ident);

                if(ass1.place.equals(ident2Left.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);
                    String place2Left = rGen.getReg(ident2Left.ident, icode2);
                    cGen.addInstruction("LDR " + place2Left + ", " + ident2Left.ident);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Left);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);

                    String temp = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp, icode2);
                    cGen.addVariable(temp, ".WORD " + int2Right.value);
                    cGen.addInstruction("LDR " + place2Right + ", " + temp);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }  
        });
    }

    private void initMultiplyAndAccumulate9(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate9, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IdentExp ident1Right = (IdentExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IntExp int2Right = (IntExp)exp2.right;

                String temp = iGen.genNextRegister();
                String place1Left = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Left.value);

                String place1Right = rGen.getReg(ident1Right.ident, icode1);

                cGen.addInstruction("LDR " + place1Left + ", " + temp);
                cGen.addInstruction("LDR " + place1Right +  ", " + ident1Right.ident);

                if(ass1.place.equals(ident2Left.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Right = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp2Right, icode2);
                    cGen.addVariable(temp2Right, ".WORD " + int2Right.value);
                    cGen.addInstruction("LDR " + place2Right + ", " + temp2Right);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);

                    String temp2Right = iGen.genNextRegister(); 
                    String place2Right = rGen.getReg(temp2Right, icode2);
                    cGen.addVariable(temp2Right, ".WORD " + int2Right.value);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }
                return null;
            } 
        });
    }

    private void initMultiplyAndAccumulate10(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate10, new Callable<Void>(){
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IdentExp ident1Left = (IdentExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IntExp int2Right = (IntExp)exp2.right;

                String place1Left = rGen.getReg(ident1Left.ident, icode1);

                String temp = iGen.genNextRegister();
                String place1Right = rGen.getReg(temp, icode1);
                cGen.addVariable(temp, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + ident1Left.ident);
                cGen.addInstruction("LDR " + place1Right +  ", " + temp);

                if(ass1.place.equals(ident2Left.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Right = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp2Right, icode2);

                    cGen.addInstruction("LDR " + place2Right + ", " + temp2Right);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);

                    String temp2Right = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp2Right, icode2);
                    cGen.addVariable(temp2Right, ".WORD " + int2Right.value);
                    cGen.addInstruction("LDR " + place2Right + ", " + temp2Right);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    public void initMultiplyAndAccumulate11(){
        codeGenFunctions.put(Pattern.multiplyAndAccumulate11, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode1 = intermediateCode.get(i);
                ICode icode2 = intermediateCode.get(i + 1);

                Assign ass1 = (Assign)icode1;
                Assign ass2 = (Assign)icode2;

                BinExp exp1 = (BinExp)ass1.value;
                BinExp exp2 = (BinExp)ass2.value;

                IntExp int1Left = (IntExp)exp1.left;
                IntExp int1Right = (IntExp)exp1.right;

                IdentExp ident2Left = (IdentExp)exp2.left;
                IntExp int2Right = (IntExp)exp2.right;

                String tempLeft = iGen.genNextRegister();
                String place1Left = rGen.getReg(tempLeft, icode1);
                cGen.addVariable(tempLeft, ".WORD " + int1Left.value);


                String tempRight = iGen.genNextRegister();
                String place1Right = rGen.getReg(tempRight, icode1);
                cGen.addVariable(tempRight, ".WORD " + int1Right.value);


                cGen.addInstruction("LDR " + place1Left + ", " + tempLeft);
                cGen.addInstruction("LDR " + place1Right +  ", " + tempRight);

                if(ass1.place.equals(ident2Left.ident)){
                    String finalPlace = rGen.getReg(ass2.place, icode2);

                    String temp2Right = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp2Right, icode2);
                    cGen.addVariable(temp2Right, ".WORD " + int2Right.value);

                    cGen.addInstruction("LDR " + place2Right + ", " + temp2Right);
                    cGen.addInstruction("MLA " + finalPlace + ", " + place1Left + ", " + place1Right + ", " + place2Right);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("STR " + finalPlace + ", " + ass2.place);
                } else {
                    String finalPlace1 = rGen.getReg(ass1.place, icode1);
                    cGen.addVariable(ass1.place);
                    cGen.addInstruction("ADD " + finalPlace1 + ", " + place1Left + ", " + place1Right);
                    cGen.addInstruction("STR " + finalPlace1 + ", " + ass1.place);

                    String place2Left = rGen.getReg(ident2Left.ident, icode2);

                    String temp2Right = iGen.genNextRegister();
                    String place2Right = rGen.getReg(temp2Right, icode2);
                    cGen.addVariable(temp2Right, ".WORD " + int2Right.value);
                    cGen.addInstruction("LDR " + place2Right + ", " + temp2Right);

                    String finalPlace2 = rGen.getReg(ass2.place, icode2);
                    cGen.addVariable(ass2.place);
                    cGen.addInstruction("MUL " + finalPlace2 + ", " + place2Left + ", " + place2Right);
                    cGen.addInstruction("STR " + finalPlace2 + ", " + ass2.place);
                }

                return null;
            }
        });
    }

    private void initAdd0(){
        codeGenFunctions.put(Pattern.add0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("ADD " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initAdd1(){
        codeGenFunctions.put(Pattern.add1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("ADD " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initAdd2(){
        codeGenFunctions.put(Pattern.add2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("ADD " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initAdd3(){
        codeGenFunctions.put(Pattern.add3, new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("SUB " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initSub0(){
        codeGenFunctions.put(Pattern.sub0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("SUB " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initSub1(){
        codeGenFunctions.put(Pattern.sub1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("SUB " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initSub2(){
        codeGenFunctions.put(Pattern.sub2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("SUB " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initSub3(){
        codeGenFunctions.put(Pattern.sub3, new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("SUB " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initMul0(){
        codeGenFunctions.put(Pattern.mul0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("MUL " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initMul1(){
        codeGenFunctions.put(Pattern.mul1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("MUL " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initMul2(){
        codeGenFunctions.put(Pattern.mul2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("MUL " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initMul3(){
        codeGenFunctions.put(Pattern.mul3, new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("MUL " + finalPlace + ", " + leftReg + ", " + rightReg);
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initDiv0(){
        codeGenFunctions.put(Pattern.div0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("STR " + leftReg + ", total");
                cGen.addInstruction("STR " + rightReg + ", dividend");
                cGen.addInstruction("B div");
                cGen.addInstruction("LDR " + finalPlace + ", result");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initDiv1(){
        codeGenFunctions.put(Pattern.div1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("STR " + leftReg + ", total");
                cGen.addInstruction("STR " + rightReg + ", dividend");
                cGen.addInstruction("B div");
                cGen.addInstruction("LDR " + finalPlace + ", result");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initDiv2(){
        codeGenFunctions.put(Pattern.div2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("STR " + leftReg + ", total");
                cGen.addInstruction("STR " + rightReg + ", dividend");
                cGen.addInstruction("B div");
                cGen.addInstruction("LDR " + finalPlace + ", result");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initDiv3(){
        codeGenFunctions.put(Pattern.div3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("STR " + leftReg + ", total");
                cGen.addInstruction("STR " + rightReg + ", dividend");
                cGen.addInstruction("B div");
                cGen.addInstruction("LDR " + finalPlace + ", result");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initMod0(){
        codeGenFunctions.put(Pattern.mod0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("STR " + leftReg + ", modTotal");
                cGen.addInstruction("STR " + rightReg + ", modDividend");
                cGen.addInstruction("B mod");
                cGen.addInstruction("LDR " + finalPlace + ", modResult");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initMod1(){
        codeGenFunctions.put(Pattern.mod1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("STR " + leftReg + ", modTotal");
                cGen.addInstruction("STR " + rightReg + ", modDividend");
                cGen.addInstruction("B mod");
                cGen.addInstruction("LDR " + finalPlace + ", modResult");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initMod2(){
        codeGenFunctions.put(Pattern.mod2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("STR " + leftReg + ", modTotal");
                cGen.addInstruction("STR " + rightReg + ", modDividend");
                cGen.addInstruction("B mod");
                cGen.addInstruction("LDR " + finalPlace + ", modResult");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initMod3(){
        codeGenFunctions.put(Pattern.mod3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("STR " + leftReg + ", modTotal");
                cGen.addInstruction("STR " + rightReg + ", modDividend");
                cGen.addInstruction("B mod");
                cGen.addInstruction("LDR " + finalPlace + ", modResult");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initGe0(){
        codeGenFunctions.put(Pattern.ge0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GE " + finalPlace + ", #1");
                cGen.addInstruction("MOV LT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initGe1(){
        codeGenFunctions.put(Pattern.ge1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GE " + finalPlace + ", #1");
                cGen.addInstruction("MOV LT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initGe2(){
        codeGenFunctions.put(Pattern.ge2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GE " + finalPlace + ", #1");
                cGen.addInstruction("MOV LT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initGe3(){
        codeGenFunctions.put(Pattern.ge3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GE " + finalPlace + ", #1");
                cGen.addInstruction("MOV LT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initGt0(){
        codeGenFunctions.put(Pattern.gt0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GT " + finalPlace + ", #1");
                cGen.addInstruction("MOV LE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initGt1(){
        codeGenFunctions.put(Pattern.gt1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GT " + finalPlace + ", #1");
                cGen.addInstruction("MOV LE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initGt2(){
        codeGenFunctions.put(Pattern.gt2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GT " + finalPlace + ", #1");
                cGen.addInstruction("MOV LE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initGt3(){
        codeGenFunctions.put(Pattern.gt3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV GT " + finalPlace + ", #1");
                cGen.addInstruction("MOV LE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initLt0(){
        codeGenFunctions.put(Pattern.lt0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LT " + finalPlace + ", #1");
                cGen.addInstruction("MOV GE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initLt1(){
        codeGenFunctions.put(Pattern.lt1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LT " + finalPlace + ", #1");
                cGen.addInstruction("MOV GE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initLt2(){
        codeGenFunctions.put(Pattern.lt2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LT " + finalPlace + ", #1");
                cGen.addInstruction("MOV GE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initLt3(){
        codeGenFunctions.put(Pattern.lt3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LT " + finalPlace + ", #1");
                cGen.addInstruction("MOV GE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initLe0(){
        codeGenFunctions.put(Pattern.le0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LE " + finalPlace + ", #1");
                cGen.addInstruction("MOV GT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initLe1(){
        codeGenFunctions.put(Pattern.le1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LE " + finalPlace + ", #1");
                cGen.addInstruction("MOV GT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initLe2(){
        codeGenFunctions.put(Pattern.le2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LE " + finalPlace + ", #1");
                cGen.addInstruction("MOV GT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initLe3(){
        codeGenFunctions.put(Pattern.le3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV LE " + finalPlace + ", #1");
                cGen.addInstruction("MOV GT " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initEq0(){
        codeGenFunctions.put(Pattern.eq0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initEq1(){
        codeGenFunctions.put(Pattern.eq1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initEq2(){
        codeGenFunctions.put(Pattern.eq2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initEq3(){
        codeGenFunctions.put(Pattern.eq3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initEq4(){
        codeGenFunctions.put(Pattern.eq4, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                BoolExp leftBool = (BoolExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                if(leftBool.trueFalse){
                    cGen.addVariable(temp, ".BYTE " + 1);
                } else {
                    cGen.addVariable(temp, ".BYTE " + 0);
                }

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initEq5(){
        codeGenFunctions.put(Pattern.eq5, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                BoolExp rightBool = (BoolExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                if(rightBool.trueFalse){
                    cGen.addVariable(temp, ".BYTE " + 1);
                } else {
                    cGen.addVariable(temp, ".BYTE " + 0);
                }

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initEq6(){
        codeGenFunctions.put(Pattern.eq6, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                BoolExp leftBool = (BoolExp)assignExp.left;
                BoolExp rightBool = (BoolExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                if(leftBool.trueFalse){
                    cGen.addVariable(tempLeft, ".BYTE " + 1);
                } else {
                    cGen.addVariable(tempLeft, ".BYTE " + 0);
                }

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                if(rightBool.trueFalse){
                    cGen.addVariable(tempRight, ".BYTE " + 1);
                } else {
                    cGen.addVariable(tempRight, ".BYTE " + 0);
                }

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV EQ " + finalPlace + ", #1");
                cGen.addInstruction("MOV NE " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNe0(){
        codeGenFunctions.put(Pattern.ne0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);
                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initNe1(){
        codeGenFunctions.put(Pattern.ne1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + leftInt.value);

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initNe2(){
        codeGenFunctions.put(Pattern.ne2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNe3(){
        codeGenFunctions.put(Pattern.ne3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IntExp leftInt = (IntExp)assignExp.left;
                IntExp rightInt = (IntExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                cGen.addVariable(tempLeft, ".WORD " + leftInt.value);

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                cGen.addVariable(tempRight, ".WORD " + rightInt.value);

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNe4(){
        codeGenFunctions.put(Pattern.ne4, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                BoolExp leftBool = (BoolExp)assignExp.left;
                IdentExp rightIdent = (IdentExp)assignExp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, assignICode);
                if(leftBool.trueFalse){
                    cGen.addVariable(temp, ".BYTE " + 1);
                } else {
                    cGen.addVariable(temp, ".BYTE " + 0);
                }

                String rightReg = rGen.getReg(rightIdent.ident, assignICode);
                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);

                return null;
            }
        });
    }

    private void initNe5(){
        codeGenFunctions.put(Pattern.ne5, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                IdentExp leftIdent = (IdentExp)assignExp.left;
                BoolExp rightBool = (BoolExp)assignExp.right;

                String leftReg = rGen.getReg(leftIdent.ident, assignICode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, assignICode);
                if(rightBool.trueFalse){
                    cGen.addVariable(temp, ".BYTE " + 1);
                } else {
                    cGen.addVariable(temp, ".BYTE " + 0);
                }

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + leftIdent.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNe6(){
        codeGenFunctions.put(Pattern.eq6, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BinExp assignExp = (BinExp)assignICode.value;

                BoolExp leftBool = (BoolExp)assignExp.left;
                BoolExp rightBool = (BoolExp)assignExp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, assignICode);
                if(leftBool.trueFalse){
                    cGen.addVariable(tempLeft, ".BYTE " + 1);
                } else {
                    cGen.addVariable(tempLeft, ".BYTE " + 0);
                }

                String tempRight = iGen.genNextRegister();
                String rightReg = rGen.getReg(tempRight, assignICode);
                if(rightBool.trueFalse){
                    cGen.addVariable(tempRight, ".BYTE " + 1);
                } else {
                    cGen.addVariable(tempRight, ".BYTE " + 0);
                }

                String finalPlace = rGen.getReg(assignICode.place, assignICode);
                cGen.addVariable(assignICode.place);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + tempRight);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("MOV NE " + finalPlace + ", #1");
                cGen.addInstruction("MOV EQ " + finalPlace + ", #0");
                cGen.addInstruction("STR " + finalPlace + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNeg0(){
        codeGenFunctions.put(Pattern.neg0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                UnExp assignExp = (UnExp)assignICode.value;
                
                IdentExp rightIdent = (IdentExp)assignExp.right;

                cGen.addVariable(assignICode.place);

                String reg = rGen.getReg(rightIdent.ident, assignICode);
                
                String holderForNeg1 = iGen.genNextRegister();
                String tempReg = rGen.getReg(holderForNeg1, assignICode);
                cGen.addVariable(holderForNeg1, ".WORD " + -1);
                cGen.addInstruction("LDR " + tempReg + ", " + holderForNeg1);
                cGen.addInstruction("LDR " + reg + ", " + rightIdent.ident);
                cGen.addInstruction("MUL " + reg + ", " + reg + ", " + tempReg);
                cGen.addInstruction("STR " + reg + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initNeg1(){
        codeGenFunctions.put(Pattern.neg1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                UnExp assignExp = (UnExp)assignICode.value;
                
                IntExp rightInt = (IntExp)assignExp.right;

                cGen.addVariable(assignICode.place);


                String temp = iGen.genNextRegister();
                String rReg = rGen.getReg(temp, assignICode);
                cGen.addVariable(temp, ".WORD " + rightInt.value);

                
                String holderForNeg1 = iGen.genNextRegister();
                String tempReg = rGen.getReg(holderForNeg1, assignICode);
                cGen.addVariable(holderForNeg1, ".WORD " + -1);

                cGen.addInstruction("LDR " + tempReg + ", " + holderForNeg1);
                cGen.addInstruction("LDR " + rReg + ", " + temp);
                cGen.addInstruction("MUL " + rReg + ", " + rReg + ", " + tempReg);
                cGen.addInstruction("STR " + rReg + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initBnot0(){
        codeGenFunctions.put(Pattern.bnot0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                UnExp assignExp = (UnExp)assignICode.value;
                
                IdentExp rightIdent = (IdentExp)assignExp.right;

                cGen.addVariable(assignICode.place);

                String reg = rGen.getReg(rightIdent.ident, assignICode);

                cGen.addInstruction("LDR " + reg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + reg + ", #0");
                cGen.addInstruction("MOV EQ " + reg + ", #1");
                cGen.addInstruction("MOV NE " + reg + ", #0");
                cGen.addInstruction("STR " + reg + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initBnot1(){
        codeGenFunctions.put(Pattern.bnot1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                UnExp assignExp = (UnExp)assignICode.value;
                
                IdentExp rightIdent = (IdentExp)assignExp.right;

                cGen.addVariable(assignICode.place);

                String reg = rGen.getReg(rightIdent.ident, assignICode);

                cGen.addInstruction("LDR " + reg + ", " + rightIdent.ident);
                cGen.addInstruction("TST " + reg + ", #0");
                cGen.addInstruction("MOV EQ " + reg + ", #1");
                cGen.addInstruction("MOV NE " + reg + ", #0");
                cGen.addInstruction("STR " + reg + ", " + assignICode.place);
                return null;
            }
        });
    }

    private void initBool0(){
        codeGenFunctions.put(Pattern.bool0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                BoolExp assignExp = (BoolExp)assignICode.value;

                if(assignExp.trueFalse){
                    cGen.addVariable(assignICode.place, ".BYTE " + 1);
                } else {
                    cGen.addVariable(assignICode.place, ".BYTE " + 0);
                }

                return null;
            }
        });
    }

    private void initInt0(){
        codeGenFunctions.put(Pattern.int0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                Assign assignICode = (Assign)icode;
                IntExp assignExp = (IntExp)assignICode.value;

                cGen.addVariable(assignICode.place, ".WORD " + assignExp.value);
                return null;
            }
        });
    }

    public void initIf0(){
        codeGenFunctions.put(Pattern.if0, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LT " + ifStatement.ifTrue);
                cGen.addInstruction("B GE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf1(){
        codeGenFunctions.put(Pattern.if1, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LT " + ifStatement.ifTrue);
                cGen.addInstruction("B GE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf2(){
        codeGenFunctions.put(Pattern.if2, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LT " + ifStatement.ifTrue);
                cGen.addInstruction("B GE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf3(){
        codeGenFunctions.put(Pattern.if3, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LT " + ifStatement.ifTrue);
                cGen.addInstruction("B GE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf4(){
        codeGenFunctions.put(Pattern.if4, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GT " + ifStatement.ifTrue);
                cGen.addInstruction("B LE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf5(){
        codeGenFunctions.put(Pattern.if5, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GT " + ifStatement.ifTrue);
                cGen.addInstruction("B LE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf6(){
        codeGenFunctions.put(Pattern.if6, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GT " + ifStatement.ifTrue);
                cGen.addInstruction("B LE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf7(){
        codeGenFunctions.put(Pattern.if7, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GT " + ifStatement.ifTrue);
                cGen.addInstruction("B LE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf8(){
        codeGenFunctions.put(Pattern.if8, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LE " + ifStatement.ifTrue);
                cGen.addInstruction("B GT " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf9(){
        codeGenFunctions.put(Pattern.if9, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LE " + ifStatement.ifTrue);
                cGen.addInstruction("B GT " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf10(){
        codeGenFunctions.put(Pattern.if10, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LE " + ifStatement.ifTrue);
                cGen.addInstruction("B GT " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf11(){
        codeGenFunctions.put(Pattern.if11, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B LE " + ifStatement.ifTrue);
                cGen.addInstruction("B GT " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf12(){
        codeGenFunctions.put(Pattern.if12, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GE " + ifStatement.ifTrue);
                cGen.addInstruction("B LT " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf13(){
        codeGenFunctions.put(Pattern.if13, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GE " + ifStatement.ifTrue);
                cGen.addInstruction("B LT " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf14(){
        codeGenFunctions.put(Pattern.if14, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GE " + ifStatement.ifTrue);
                cGen.addInstruction("B LT " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf15(){
        codeGenFunctions.put(Pattern.if15, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("CMP " + leftReg + ", " + rightReg);
                cGen.addInstruction("B GE " + ifStatement.ifTrue);
                cGen.addInstruction("B LT " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf16(){
        codeGenFunctions.put(Pattern.if16, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf17(){
        codeGenFunctions.put(Pattern.if17, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf18(){
        codeGenFunctions.put(Pattern.if18, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf19(){
        codeGenFunctions.put(Pattern.if19, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf20(){
        codeGenFunctions.put(Pattern.if20, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                BoolExp left = (BoolExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                if(left.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf21(){
        codeGenFunctions.put(Pattern.if21, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                BoolExp right = (BoolExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                if(right.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf22(){
        codeGenFunctions.put(Pattern.if22, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                BoolExp left = (BoolExp)exp.left;
                BoolExp right = (BoolExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                if(left.trueFalse){
                    cGen.addVariable(tempLeft, ".WORD " + 1);
                } else {
                    cGen.addVariable(tempLeft, ".WORD " + 0);
                }

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                if(right.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B EQ " + ifStatement.ifTrue);
                cGen.addInstruction("B NE " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf23(){
        codeGenFunctions.put(Pattern.if23, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf24(){
        codeGenFunctions.put(Pattern.if24, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + left.value);
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf25(){
        codeGenFunctions.put(Pattern.if25, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf26(){
        codeGenFunctions.put(Pattern.if26, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IntExp left = (IntExp)exp.left;
                IntExp right = (IntExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                cGen.addVariable(tempLeft, ".WORD " + left.value);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                cGen.addVariable(temp, ".WORD " + right.value);

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf27(){
        codeGenFunctions.put(Pattern.if27, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                BoolExp left = (BoolExp)exp.left;
                IdentExp right = (IdentExp)exp.right;

                String temp = iGen.genNextRegister();
                String leftReg = rGen.getReg(temp, icode);
                if(left.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }
                String rightReg = rGen.getReg(right.ident, icode);

                cGen.addInstruction("LDR " + leftReg + ", " + temp);
                cGen.addInstruction("LDR " + rightReg + ", " + right.ident);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);
                return null;
            }
        });
    }

    public void initIf28(){
        codeGenFunctions.put(Pattern.if28, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                IdentExp left = (IdentExp)exp.left;
                BoolExp right = (BoolExp)exp.right;

                String leftReg = rGen.getReg(left.ident, icode);

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                if(right.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }

                cGen.addInstruction("LDR " + leftReg + ", " + left.ident);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);

                return null;
            }
        });
    }

    public void initIf29(){
        codeGenFunctions.put(Pattern.if22, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ICode icode = intermediateCode.get(i);
                If ifStatement = (If)icode;
                BinExp exp = ifStatement.exp;

                BoolExp left = (BoolExp)exp.left;
                BoolExp right = (BoolExp)exp.right;

                String tempLeft = iGen.genNextRegister();
                String leftReg = rGen.getReg(tempLeft, icode);
                if(left.trueFalse){
                    cGen.addVariable(tempLeft, ".WORD " + 1);
                } else {
                    cGen.addVariable(tempLeft, ".WORD " + 0);
                }

                String temp = iGen.genNextRegister();
                String rightReg = rGen.getReg(temp, icode);
                if(right.trueFalse){
                    cGen.addVariable(temp, ".WORD " + 1);
                } else {
                    cGen.addVariable(temp, ".WORD " + 0);
                }

                cGen.addInstruction("LDR " + leftReg + ", " + tempLeft);
                cGen.addInstruction("LDR " + rightReg + ", " + temp);
                cGen.addInstruction("TST " + leftReg + ", " + rightReg);
                cGen.addInstruction("B NE " + ifStatement.ifTrue);
                cGen.addInstruction("B EQ " + ifStatement.ifFalse);

                return null;
            }
        });
    }

}
package io.github.H20man13.DeClan;

import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.JDialog;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.TestRig;
import org.antlr.v4.runtime.tree.gui.TreeViewer;
import org.junit.Test;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.ErrorLog.LogItem;
import edu.depauw.declan.common.ast.Program;
import io.github.H20man13.DeClan.common.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.ReaderSource;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.main.MyCodeGenerator;
import io.github.H20man13.DeClan.main.MyDeClanLexer;
import io.github.H20man13.DeClan.main.MyDeClanParser;
import io.github.H20man13.DeClan.main.MyICodeGenerator;
import io.github.H20man13.DeClan.main.MyOptimizer;
import io.github.H20man13.DeClan.main.MyStandardLibrary;
import io.github.H20man13.DeClan.main.assembler.ArmAssemblerLexer;
import io.github.H20man13.DeClan.main.assembler.ArmAssemblerParser;

public class CodeGeneratorTest {
    private void testDeclanFile(String fileName, String expectedResult){
        ErrorLog errLog = new ErrorLog();
        try{
            FileReader reader = new FileReader(fileName);
            ReaderSource source = new ReaderSource(reader);
            MyDeClanLexer lexer = new MyDeClanLexer(source, errLog);
            MyDeClanParser parser = new MyDeClanParser(lexer, errLog);
            MyStandardLibrary stdLib = new MyStandardLibrary(errLog);

            Program prog = parser.parseProgram();

            IrRegisterGenerator rGen = new IrRegisterGenerator();
            MyICodeGenerator gen = new MyICodeGenerator(errLog, rGen);
            stdLib.ioLibrary().accept(gen);
            stdLib.mathLibrary().accept(gen);
            prog.accept(gen);

            List<ICode> generatedICode = gen.getICode();

            MyOptimizer optimizer = new MyOptimizer(generatedICode, rGen);
            optimizer.runDataFlowAnalysis();
            optimizer.performDeadCodeElimination();

            List<ICode> optimizedICode = optimizer.getICode();

            MyCodeGenerator codeGenerator = new MyCodeGenerator(optimizer.getLiveVariableAnalysis(), optimizedICode, rGen, errLog); 

            StringWriter writer = new StringWriter();
            codeGenerator.codeGen(writer);


            for(LogItem item : errLog){
                assertTrue(item.toString(), false);
            }

            String outputString = writer.toString();

            assertTrue("Error expected -\n\n" + expectedResult + "\n\n but found -\n\n " + outputString, outputString.equals(expectedResult));

            StringReader outputStringReader = new StringReader(outputString);
            ANTLRInputStream inputString = new ANTLRInputStream(outputStringReader);
            ArmAssemblerLexer armLexer = new ArmAssemblerLexer(inputString);
            CommonTokenStream tokStream = new CommonTokenStream(armLexer);
            ArmAssemblerParser armParser = new ArmAssemblerParser(tokStream);

            armParser.program();
   
        } catch(Exception exp){
            assertTrue(exp.toString(), false);
        }
    }

    /* 
    @Test
    public void testConversions(){
        String expectedResult = "";
        testDeclanFile("test_source/conversions.dcl", expectedResult);
    }

    @Test
    public void testExpressions(){
        String expectedResult = "";
        testDeclanFile("test_source/expressions.dcl", expectedResult);
    }

    @Test
    public void testForLoopAdvanced(){
        String expectedResult = "";
        testDeclanFile("test_source/ForLoopAdvanced.dcl", expectedResult);
    }

    @Test
    public void testForLoopBasic(){
        String expectedResult = "LDR R13, totalBytes\r\n" + //
                                "B begin_0\r\n" + //
                                "h: .WORD 0\r\n" + //
                                "i: .WORD 0\r\n" + //
                                "j: .WORD 0\r\n" + //
                                "S: .WORD 1\r\n" + //
                                "M: .WORD 0\r\n" + //
                                "T: .WORD 1\r\n" + //
                                "N: .WORD 0\r\n" + //
                                "U: .WORD 23\r\n" + //
                                "V: .WORD 0\r\n" + //
                                "m6: .BYTE 1\r\n" + //
                                "m7: .BYTE 1\r\n" + //
                                "W: .WORD 0\r\n" + //
                                "X: .WORD 23\r\n" + //
                                "Y: .WORD 0\r\n" + //
                                "Z: .WORD 0\r\n" + //
                                "O: .WORD 0\r\n" + //
                                "a0: .WORD 1\r\n" + //
                                "a1: .WORD 0\r\n" + //
                                "O: .WORD 0\r\n" + //
                                "a2: .WORD 2\r\n" + //
                                "a3: .WORD 0\r\n" + //
                                "B: .WORD 12.0\r\n" + //
                                "z: .WORD 8\r\n" + //
                                "A: .WORD 4\r\n" + //
                                "a4: .WORD 0\r\n" + //
                                "a5: .WORD 0\r\n" + //
                                "P: .WORD 0\r\n" + //
                                "a6: .WORD 0\r\n" + //
                                "M: .WORD 0\r\n" + //
                                "a7: .WORD 1\r\n" + //
                                "a8: .WORD 0\r\n" + //
                                "N: .WORD 0\r\n" + //
                                "a9: .WORD 23\r\n" + //
                                "b0: .WORD 0\r\n" + //
                                "V: .WORD 0\r\n" + //
                                "b1: .WORD 23\r\n" + //
                                "b2: .WORD 0\r\n" + //
                                "b3: .WORD 255\r\n" + //
                                "b4: .WORD 0\r\n" + //
                                "Q: .WORD 0\r\n" + //
                                "b5: .WORD 2\r\n" + //
                                "b6: .WORD 127\r\n" + //
                                "b7: .WORD 0\r\n" + //
                                "E: .WORD 12.0\r\n" + //
                                "C: .WORD 8\r\n" + //
                                "D: .WORD 4\r\n" + //
                                "b8: .WORD 0\r\n" + //
                                "Q: .WORD 0\r\n" + //
                                "b9: .WORD 31\r\n" + //
                                "c0: .WORD 0\r\n" + //
                                "c1: .WORD 1\r\n" + //
                                "c2: .WORD 0\r\n" + //
                                "R: .WORD 0\r\n" + //
                                "c3: .WORD 1\r\n" + //
                                "c4: .WORD 0\r\n" + //
                                "n2: .BYTE 1\r\n" + //
                                "c5: .WORD 1\r\n" + //
                                "c6: .WORD 0\r\n" + //
                                "R: .WORD 0\r\n" + //
                                "c7: .WORD 0\r\n" + //
                                "c8: .WORD 0\r\n" + //
                                "L: .WORD 0\r\n" + //
                                "d1: .WORD 0\r\n" + //
                                "d3: .WORD 255\r\n" + //
                                "d4: .WORD 0\r\n" + //
                                "d0: .WORD 0\r\n" + //
                                "d5: .WORD 2\r\n" + //
                                "d6: .WORD 127\r\n" + //
                                "d7: .WORD 0\r\n" + //
                                "E: .WORD 12.0\r\n" + //
                                "C: .WORD 8\r\n" + //
                                "D: .WORD 4\r\n" + //
                                "d8: .WORD 0\r\n" + //
                                "c9: .WORD 0\r\n" + //
                                "e0: .WORD 0\r\n" + //
                                "e1: .WORD 0\r\n" + //
                                "e2: .WORD 0\r\n" + //
                                "n7: .BYTE 1\r\n" + //
                                "d9: .WORD 0\r\n" + //
                                "e3: .WORD 0\r\n" + //
                                "d9: .WORD 0\r\n" + //
                                "e6: .WORD 0\r\n" + //
                                "e4: .WORD 0\r\n" + //
                                "e7: .WORD 0\r\n" + //
                                "e5: .WORD 0\r\n" + //
                                "e8: .WORD 0\r\n" + //
                                "e9: .WORD 0\r\n" + //
                                "f0: .WORD 0\r\n" + //
                                "f1: .WORD 0\r\n" + //
                                "n9: .BYTE 1\r\n" + //
                                "o0: .BYTE 1\r\n" + //
                                "f2: .WORD 0\r\n" + //
                                "e4: .WORD 0\r\n" + //
                                "f3: .WORD 1\r\n" + //
                                "f4: .WORD 0\r\n" + //
                                "e5: .WORD 0\r\n" + //
                                "f5: .WORD 0\r\n" + //
                                "f6: .WORD 0\r\n" + //
                                "f7: .WORD 0\r\n" + //
                                "f1: .WORD 0\r\n" + //
                                "g0: .WORD 0\r\n" + //
                                "g1: .WORD 0\r\n" + //
                                "s: .WORD 12.0\r\n" + //
                                "q: .WORD 8\r\n" + //
                                "r: .WORD 4\r\n" + //
                                "g2: .WORD 0\r\n" + //
                                "f8: .WORD 0\r\n" + //
                                "l: .WORD 8.0\r\n" + //
                                "k: .WORD 4\r\n" + //
                                "g3: .WORD 0\r\n" + //
                                "f9: .WORD 0\r\n" + //
                                "g6: .WORD 0\r\n" + //
                                "g4: .WORD 0\r\n" + //
                                "g7: .WORD 0\r\n" + //
                                "g8: .WORD 0\r\n" + //
                                "g9: .WORD 0\r\n" + //
                                "h0: .WORD 0\r\n" + //
                                "o6: .BYTE 1\r\n" + //
                                "o7: .BYTE 1\r\n" + //
                                "h1: .WORD 0\r\n" + //
                                "g4: .WORD 0\r\n" + //
                                "h2: .WORD 0\r\n" + //
                                "h3: .WORD 0\r\n" + //
                                "h4: .WORD 0\r\n" + //
                                "h0: .WORD 0\r\n" + //
                                "h7: .WORD 1\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "h8: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "h9: .WORD 0\r\n" + //
                                "i0: .WORD 0\r\n" + //
                                "i1: .WORD 0\r\n" + //
                                "o9: .BYTE 1\r\n" + //
                                "i2: .WORD 0\r\n" + //
                                "p0: .BYTE 1\r\n" + //
                                "p1: .BYTE 1\r\n" + //
                                "i3: .WORD 0\r\n" + //
                                "i4: .WORD 0\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "i5: .WORD 1\r\n" + //
                                "i6: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "i7: .WORD 0\r\n" + //
                                "i2: .WORD 0\r\n" + //
                                "i8: .WORD 0\r\n" + //
                                "i9: .WORD 0\r\n" + //
                                "p2: .BYTE 1\r\n" + //
                                "j0: .WORD 0\r\n" + //
                                "p3: .BYTE 1\r\n" + //
                                "p4: .BYTE 1\r\n" + //
                                "j1: .WORD 0\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "j2: .WORD 1\r\n" + //
                                "j3: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "j4: .WORD 0\r\n" + //
                                "j0: .WORD 0\r\n" + //
                                "j7: .WORD 1\r\n" + //
                                "j5: .WORD 0\r\n" + //
                                "j8: .WORD 0\r\n" + //
                                "j6: .WORD 0\r\n" + //
                                "j9: .WORD 0\r\n" + //
                                "k0: .WORD 0\r\n" + //
                                "p6: .BYTE 1\r\n" + //
                                "p7: .BYTE 1\r\n" + //
                                "k1: .WORD 0\r\n" + //
                                "k2: .WORD 0\r\n" + //
                                "j5: .WORD 0\r\n" + //
                                "k3: .WORD 1\r\n" + //
                                "k4: .WORD 0\r\n" + //
                                "j6: .WORD 0\r\n" + //
                                "k5: .WORD 0\r\n" + //
                                "k0: .WORD 0\r\n" + //
                                "k6: .WORD 0\r\n" + //
                                "k7: .WORD 0\r\n" + //
                                "k8: .WORD 0.5\r\n" + //
                                "k9: .WORD 0\r\n" + //
                                "G: .WORD 8.0\r\n" + //
                                "F: .WORD 4\r\n" + //
                                "l0: .WORD 0\r\n" + //
                                "l1: .WORD 1\r\n" + //
                                "l2: .WORD 0\r\n" + //
                                "l3: .WORD 0\r\n" + //
                                "G: .WORD 8.0\r\n" + //
                                "F: .WORD 4\r\n" + //
                                "l4: .WORD 0\r\n" + //
                                "l5: .WORD 0\r\n" + //
                                "l6: .WORD 0\r\n" + //
                                "l8: .WORD 1\r\n" + //
                                "l7: .WORD 0\r\n" + //
                                "l9: .WORD 10\r\n" + //
                                "c: .WORD 4\r\n" + //
                                "m0: .WORD 1\r\n" + //
                                "m1: .WORD 0\r\n" + //
                                "l7: .WORD 0\r\n" + //
                                "totalBytes: .WORD 3668\r\n" + //
                                "begin_0: B begin_1\r\n" + //
                                "WriteLn: SWI 4\r\n" + //
                                "LDR R2, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "WriteInt: LDR R2, c\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, h\r\n" + //
                                "LDR R0, h\r\n" + //
                                "SWI 1\r\n" + //
                                "LDR R3, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "WriteReal: LDR R3, e\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, i\r\n" + //
                                "LDR R0, i\r\n" + //
                                "SWI 2\r\n" + //
                                "LDR R2, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "ReadInt: SWI 3\r\n" + //
                                "STR R0, j\r\n" + //
                                "LDR R2, j\r\n" + //
                                "LDR R3, g\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "begin_1: B begin_2\r\n" + //
                                "IntToReal: LDR R3, S\r\n" + //
                                "STR R3, M\r\n" + //
                                "LDR R2, T\r\n" + //
                                "STR R2, N\r\n" + //
                                "LDR R3, N\r\n" + //
                                "LDR R3, U\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLE R3, #1\r\n" + //
                                "MOVGT R3, #0\r\n" + //
                                "STR R3, V\r\n" + //
                                "LDR R3, V\r\n" + //
                                "LDR R3, m6\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_0_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_0_SEQ_0_LEVEL_0: LDR R3, V\r\n" + //
                                "LDR R3, m7\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_0_LEVEL_0\r\n" + //
                                "WHILESTAT_0_SEQ_0_LEVEL_0: LDR R3, k\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, W\r\n" + //
                                "LDR R3, X\r\n" + //
                                "LDR R3, N\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, Y\r\n" + //
                                "LDR R3, W\r\n" + //
                                "LDR R3, Y\r\n" + //
                                "MOV R3, R3, LSR R3\r\n" + //
                                "STR R3, Z\r\n" + //
                                "LDR R3, Z\r\n" + //
                                "STR R3, O\r\n" + //
                                "LDR R2, O\r\n" + //
                                "LDR R3, a0\r\n" + //
                                "AND R4, R2, R3\r\n" + //
                                "STR R4, a1\r\n" + //
                                "LDR R2, a1\r\n" + //
                                "STR R2, O\r\n" + //
                                "LDR R3, N\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R3, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL INeg\r\n" + //
                                "LDR R3, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R3, a3\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, z\r\n" + //
                                "LDR R4, a2\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, A\r\n" + //
                                "LDR R5, a3\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL RealExp\r\n" + //
                                "LDR R2, B\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, a4\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, O\r\n" + //
                                "LDR R3, a4\r\n" + //
                                "MUL R4, R2, R3\r\n" + //
                                "STR R4, a5\r\n" + //
                                "LDR R2, a5\r\n" + //
                                "STR R2, P\r\n" + //
                                "LDR R3, M\r\n" + //
                                "LDR R3, P\r\n" + //
                                "ADD R3, R3, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R3, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R3, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R3, a6\r\n" + //
                                "LDR R3, a6\r\n" + //
                                "STR R3, M\r\n" + //
                                "LDR R2, N\r\n" + //
                                "LDR R3, a7\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, a8\r\n" + //
                                "LDR R2, a8\r\n" + //
                                "STR R2, N\r\n" + //
                                "LDR R3, N\r\n" + //
                                "LDR R3, a9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLE R3, #1\r\n" + //
                                "MOVGT R3, #0\r\n" + //
                                "STR R3, b0\r\n" + //
                                "LDR R3, b0\r\n" + //
                                "STR R3, V\r\n" + //
                                "B WHILECOND_0_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_0_LEVEL_0: LDR R2, W\r\n" + //
                                "LDR R3, b1\r\n" + //
                                "MOV R4, R2, LSR R3\r\n" + //
                                "STR R4, b2\r\n" + //
                                "LDR R2, b2\r\n" + //
                                "LDR R2, b3\r\n" + //
                                "AND R2, R2, R2\r\n" + //
                                "STR R2, b4\r\n" + //
                                "LDR R2, b4\r\n" + //
                                "STR R2, Q\r\n" + //
                                "LDR R2, Q\r\n" + //
                                "LDR R3, b6\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, b7\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R2, C\r\n" + //
                                "LDR R3, b5\r\n" + //
                                "STR R3, [R13,-R2]\r\n" + //
                                "LDR R4, D\r\n" + //
                                "LDR R5, b7\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL IntExp\r\n" + //
                                "LDR R3, E\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, b8\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, b8\r\n" + //
                                "STR R2, Q\r\n" + //
                                "LDR R3, W\r\n" + //
                                "LDR R3, b9\r\n" + //
                                "MOV R3, R3, LSR R3\r\n" + //
                                "STR R3, c0\r\n" + //
                                "LDR R3, c0\r\n" + //
                                "LDR R3, c1\r\n" + //
                                "AND R3, R3, R3\r\n" + //
                                "STR R3, c2\r\n" + //
                                "LDR R3, c2\r\n" + //
                                "STR R3, R\r\n" + //
                                "LDR R2, R\r\n" + //
                                "LDR R3, c3\r\n" + //
                                "TEQ R2, R3\r\n" + //
                                "MOVEQ R4, #1\r\n" + //
                                "MOVNE R4, #0\r\n" + //
                                "STR R4, c4\r\n" + //
                                "LDR R2, c4\r\n" + //
                                "LDR R2, n2\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_0_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_0_SEQ_0_LEVEL_0: LDR R2, c5\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL INeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, c6\r\n" + //
                                "LDR R2, c6\r\n" + //
                                "STR R2, R\r\n" + //
                                "B IFEND_0_LEVEL_0\r\n" + //
                                "IFNEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_0_LEVEL_0: LDR R3, R\r\n" + //
                                "LDR R3, Q\r\n" + //
                                "MUL R3, R3, R3\r\n" + //
                                "STR R3, c7\r\n" + //
                                "LDR R3, c7\r\n" + //
                                "LDR R3, M\r\n" + //
                                "MUL R3, R3, R3\r\n" + //
                                "STR R3, c8\r\n" + //
                                "LDR R3, c8\r\n" + //
                                "STR R3, L\r\n" + //
                                "LDR R2, L\r\n" + //
                                "LDR R3, l\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "RealToInt: LDR R3, m\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, d1\r\n" + //
                                "LDR R2, d1\r\n" + //
                                "LDR R3, d3\r\n" + //
                                "AND R4, R2, R3\r\n" + //
                                "STR R4, d4\r\n" + //
                                "LDR R2, d4\r\n" + //
                                "STR R2, d0\r\n" + //
                                "LDR R3, d0\r\n" + //
                                "LDR R3, d6\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, d7\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, C\r\n" + //
                                "LDR R4, d5\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, D\r\n" + //
                                "LDR R5, d7\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL IntExp\r\n" + //
                                "LDR R2, E\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, d8\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, d8\r\n" + //
                                "STR R2, c9\r\n" + //
                                "LDR R3, c9\r\n" + //
                                "LDR R3, n\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Abs: LDR R3, o\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, e0\r\n" + //
                                "LDR R2, e0\r\n" + //
                                "LDR R3, e1\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVGE R4, #1\r\n" + //
                                "MOVLT R4, #0\r\n" + //
                                "STR R4, e2\r\n" + //
                                "LDR R2, e2\r\n" + //
                                "LDR R2, n7\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_1_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_1_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_1_SEQ_0_LEVEL_0: LDR R2, e0\r\n" + //
                                "STR R2, d9\r\n" + //
                                "B IFEND_1_LEVEL_0\r\n" + //
                                "IFNEXT_1_SEQ_0_LEVEL_0: LDR R2, e0\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, e3\r\n" + //
                                "LDR R2, e3\r\n" + //
                                "STR R2, d9\r\n" + //
                                "B IFEND_1_LEVEL_0\r\n" + //
                                "IFNEXT_1_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_1_LEVEL_0: LDR R3, d9\r\n" + //
                                "LDR R3, p\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Div: LDR R3, q\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, e6\r\n" + //
                                "LDR R2, e6\r\n" + //
                                "STR R2, e4\r\n" + //
                                "LDR R3, e7\r\n" + //
                                "STR R3, e5\r\n" + //
                                "LDR R2, r\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, e8\r\n" + //
                                "LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, e9\r\n" + //
                                "LDR R3, e9\r\n" + //
                                "LDR R3, f0\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, f1\r\n" + //
                                "LDR R3, f1\r\n" + //
                                "LDR R3, n9\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_2_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_2_SEQ_0_LEVEL_0: LDR R3, f1\r\n" + //
                                "LDR R3, o0\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_2_LEVEL_0\r\n" + //
                                "WHILESTAT_2_SEQ_0_LEVEL_0: LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, f2\r\n" + //
                                "LDR R3, f2\r\n" + //
                                "STR R3, e4\r\n" + //
                                "LDR R2, e5\r\n" + //
                                "LDR R3, f3\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, f4\r\n" + //
                                "LDR R2, f4\r\n" + //
                                "STR R2, e5\r\n" + //
                                "LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, f5\r\n" + //
                                "LDR R3, f5\r\n" + //
                                "LDR R3, f6\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, f7\r\n" + //
                                "LDR R3, f7\r\n" + //
                                "STR R3, f1\r\n" + //
                                "B WHILECOND_2_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_2_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_2_LEVEL_0: LDR R2, e5\r\n" + //
                                "LDR R3, s\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Divide: LDR R3, t\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g0\r\n" + //
                                "LDR R2, u\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g1\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, q\r\n" + //
                                "LDR R4, g0\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, r\r\n" + //
                                "LDR R5, g1\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL Div\r\n" + //
                                "LDR R2, s\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g2\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, g2\r\n" + //
                                "STR R2, f8\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R3, k\r\n" + //
                                "LDR R4, f8\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R2, l\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g3\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, g3\r\n" + //
                                "STR R2, f9\r\n" + //
                                "LDR R3, f9\r\n" + //
                                "LDR R3, v\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Mod: LDR R3, w\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g6\r\n" + //
                                "LDR R2, g6\r\n" + //
                                "STR R2, g4\r\n" + //
                                "LDR R3, x\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g7\r\n" + //
                                "LDR R2, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, g8\r\n" + //
                                "LDR R4, g8\r\n" + //
                                "LDR R5, g9\r\n" + //
                                "CMP R4, R5\r\n" + //
                                "MOVGT R6, #1\r\n" + //
                                "MOVLE R6, #0\r\n" + //
                                "STR R6, h0\r\n" + //
                                "LDR R2, h0\r\n" + //
                                "LDR R3, o6\r\n" + //
                                "TST R2, R3\r\n" + //
                                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_4_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_4_SEQ_0_LEVEL_0: LDR R2, h0\r\n" + //
                                "LDR R3, o7\r\n" + //
                                "TST R2, R3\r\n" + //
                                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_4_LEVEL_0\r\n" + //
                                "WHILESTAT_4_SEQ_0_LEVEL_0: LDR R3, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, h1\r\n" + //
                                "LDR R3, h1\r\n" + //
                                "STR R3, g4\r\n" + //
                                "LDR R2, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, h2\r\n" + //
                                "LDR R4, h2\r\n" + //
                                "LDR R5, h3\r\n" + //
                                "CMP R4, R5\r\n" + //
                                "MOVGT R6, #1\r\n" + //
                                "MOVLE R6, #0\r\n" + //
                                "STR R6, h4\r\n" + //
                                "LDR R2, h4\r\n" + //
                                "STR R2, h0\r\n" + //
                                "B WHILECOND_4_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_4_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_4_LEVEL_0: LDR R3, g4\r\n" + //
                                "LDR R3, y\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "RealExp: LDR R3, h7\r\n" + //
                                "STR R3, h5\r\n" + //
                                "LDR R2, h8\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, A\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, h9\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "LDR R3, i0\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVGT R4, #1\r\n" + //
                                "MOVLE R4, #0\r\n" + //
                                "STR R4, i1\r\n" + //
                                "LDR R2, i1\r\n" + //
                                "LDR R2, o9\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_2_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_2_SEQ_0_LEVEL_0: LDR R2, h6\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "CMP R2, R2\r\n" + //
                                "MOVLT R2, #1\r\n" + //
                                "MOVGE R2, #0\r\n" + //
                                "STR R2, i2\r\n" + //
                                "LDR R2, i2\r\n" + //
                                "LDR R2, p0\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_6_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_6_SEQ_0_LEVEL_0: LDR R2, i2\r\n" + //
                                "LDR R2, p1\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_6_LEVEL_0\r\n" + //
                                "WHILESTAT_6_SEQ_0_LEVEL_0: LDR R2, z\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, i3\r\n" + //
                                "LDR R2, h5\r\n" + //
                                "LDR R2, i3\r\n" + //
                                "MUL R2, R2, R2\r\n" + //
                                "STR R2, i4\r\n" + //
                                "LDR R2, i4\r\n" + //
                                "STR R2, h5\r\n" + //
                                "LDR R2, h6\r\n" + //
                                "LDR R3, i5\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, i6\r\n" + //
                                "LDR R2, i6\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, h6\r\n" + //
                                "LDR R3, h9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLT R3, #1\r\n" + //
                                "MOVGE R3, #0\r\n" + //
                                "STR R3, i7\r\n" + //
                                "LDR R3, i7\r\n" + //
                                "STR R3, i2\r\n" + //
                                "B WHILECOND_6_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_6_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_6_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                                "IFNEXT_2_SEQ_0_LEVEL_0: LDR R2, h9\r\n" + //
                                "LDR R3, i8\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVLT R4, #1\r\n" + //
                                "MOVGE R4, #0\r\n" + //
                                "STR R4, i9\r\n" + //
                                "LDR R2, i9\r\n" + //
                                "LDR R2, p2\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_2_SEQ_1_LEVEL_0\r\n" + //
                                "BNE IFNEXT_2_SEQ_1_LEVEL_0\r\n" + //
                                "IFSTAT_2_SEQ_1_LEVEL_0: LDR R2, h6\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "CMP R2, R2\r\n" + //
                                "MOVGT R2, #1\r\n" + //
                                "MOVLE R2, #0\r\n" + //
                                "STR R2, j0\r\n" + //
                                "LDR R2, j0\r\n" + //
                                "LDR R2, p3\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_8_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_8_SEQ_0_LEVEL_0: LDR R2, j0\r\n" + //
                                "LDR R2, p4\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_8_LEVEL_0\r\n" + //
                                "WHILESTAT_8_SEQ_0_LEVEL_0: LDR R2, h5\r\n" + //
                                "LDR R2, i3\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R2, [R13, #-8]\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "BL Divide\r\n" + //
                                "LDR R2, [R13, #-12]\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "STR R2, j1\r\n" + //
                                "LDR R2, j1\r\n" + //
                                "STR R2, h5\r\n" + //
                                "LDR R2, h6\r\n" + //
                                "LDR R3, j2\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, j3\r\n" + //
                                "LDR R2, j3\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, h6\r\n" + //
                                "LDR R3, h9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, j4\r\n" + //
                                "LDR R3, j4\r\n" + //
                                "STR R3, j0\r\n" + //
                                "B WHILECOND_8_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_8_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_8_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                                "IFNEXT_2_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_2_LEVEL_0: LDR R2, h5\r\n" + //
                                "LDR R3, B\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "IntExp: LDR R3, j7\r\n" + //
                                "STR R3, j5\r\n" + //
                                "LDR R2, j8\r\n" + //
                                "STR R2, j6\r\n" + //
                                "LDR R3, D\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, j9\r\n" + //
                                "LDR R2, j6\r\n" + //
                                "LDR R3, j9\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVLT R4, #1\r\n" + //
                                "MOVGE R4, #0\r\n" + //
                                "STR R4, k0\r\n" + //
                                "LDR R4, k0\r\n" + //
                                "LDR R5, p6\r\n" + //
                                "TST R4, R5\r\n" + //
                                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_10_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_10_SEQ_0_LEVEL_0: LDR R4, k0\r\n" + //
                                "LDR R5, p7\r\n" + //
                                "TST R4, R5\r\n" + //
                                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_10_LEVEL_0\r\n" + //
                                "WHILESTAT_10_SEQ_0_LEVEL_0: LDR R2, C\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, k1\r\n" + //
                                "LDR R2, j5\r\n" + //
                                "LDR R2, k1\r\n" + //
                                "MUL R2, R2, R2\r\n" + //
                                "STR R2, k2\r\n" + //
                                "LDR R2, k2\r\n" + //
                                "STR R2, j5\r\n" + //
                                "LDR R2, j6\r\n" + //
                                "LDR R3, k3\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R4, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R4, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R4, k4\r\n" + //
                                "LDR R2, k4\r\n" + //
                                "STR R2, j6\r\n" + //
                                "LDR R3, j6\r\n" + //
                                "LDR R3, j9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLT R3, #1\r\n" + //
                                "MOVGE R3, #0\r\n" + //
                                "STR R3, k5\r\n" + //
                                "LDR R3, k5\r\n" + //
                                "STR R3, k0\r\n" + //
                                "B WHILECOND_10_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_10_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_10_LEVEL_0: LDR R2, j5\r\n" + //
                                "LDR R3, E\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Floor: LDR R3, F\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, k6\r\n" + //
                                "LDR R2, k6\r\n" + //
                                "LDR R3, G\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Round: LDR R3, H\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, k7\r\n" + //
                                "LDR R2, k7\r\n" + //
                                "LDR R3, k8\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R4, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R4, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R4, k9\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R2, F\r\n" + //
                                "LDR R3, k9\r\n" + //
                                "STR R3, [R13,-R2]\r\n" + //
                                "BL Floor\r\n" + //
                                "LDR R4, G\r\n" + //
                                "LDR R4, [R13, -R4]\r\n" + //
                                "STR R4, l0\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, l0\r\n" + //
                                "LDR R2, I\r\n" + //
                                "STR R2, [R13, -R2]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Ceil: LDR R2, J\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, l2\r\n" + //
                                "LDR R2, l2\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, l3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R3, F\r\n" + //
                                "LDR R4, l3\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "BL Floor\r\n" + //
                                "LDR R2, G\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, l4\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, l1\r\n" + //
                                "LDR R3, l4\r\n" + //
                                "MUL R4, R2, R3\r\n" + //
                                "STR R4, l5\r\n" + //
                                "LDR R2, l5\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, l6\r\n" + //
                                "LDR R3, l6\r\n" + //
                                "LDR R3, K\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "begin_2: B begin_3\r\n" + //
                                "begin_3: LDR R3, l8\r\n" + //
                                "STR R3, l7\r\n" + //
                                "FORBEG_0_LEVEL_0: LDR R2, l7\r\n" + //
                                "LDR R3, l9\r\n" + //
                                "TST R2, R3\r\n" + //
                                "BNE FORLOOP_0_LEVEL_0\r\n" + //
                                "BEQ FOREND_0_LEVEL_0\r\n" + //
                                "FORLOOP_0_LEVEL_0: ADD R13, R13, #8\r\n" + //
                                "STR R14, [R13, #-8]\r\n" + //
                                "LDR R4, c\r\n" + //
                                "LDR R2, l7\r\n" + //
                                "STR R2, [R13,-R4]\r\n" + //
                                "BL WriteInt\r\n" + //
                                "LDR R14, [R13, #-8]\r\n" + //
                                "SUB R13, R13, #8\r\n" + //
                                "LDR R2, l7\r\n" + //
                                "LDR R4, m0\r\n" + //
                                "ADD R5, R2, R4\r\n" + //
                                "STR R5, m1\r\n" + //
                                "LDR R2, m1\r\n" + //
                                "STR R2, l7\r\n" + //
                                "B FORBEG_0_LEVEL_0\r\n" + //
                                "FOREND_0_LEVEL_0: ADD R13, R13, #4\r\n" + //
                                "STR R14, [R13, #-4]\r\n" + //
                                "BL WriteLn\r\n" + //
                                "LDR R14, [R13, #-4]\r\n" + //
                                "SUB R13, R13, #4\r\n" + //
                                "STP\r\n";
        testDeclanFile("test_source/ForLoopBasic.dcl", expectedResult);
    }

    @Test
    public void testForLoopBasic2(){
        String expectedResult = "LDR R13, totalBytes\r\n" + //
                                "B begin_0\r\n" + //
                                "h: .WORD 0\r\n" + //
                                "i: .WORD 0\r\n" + //
                                "j: .WORD 0\r\n" + //
                                "S: .WORD 1\r\n" + //
                                "M: .WORD 0\r\n" + //
                                "T: .WORD 1\r\n" + //
                                "N: .WORD 0\r\n" + //
                                "U: .WORD 23\r\n" + //
                                "V: .WORD 0\r\n" + //
                                "m6: .BYTE 1\r\n" + //
                                "m7: .BYTE 1\r\n" + //
                                "W: .WORD 0\r\n" + //
                                "X: .WORD 23\r\n" + //
                                "Y: .WORD 0\r\n" + //
                                "Z: .WORD 0\r\n" + //
                                "O: .WORD 0\r\n" + //
                                "a0: .WORD 1\r\n" + //
                                "a1: .WORD 0\r\n" + //
                                "O: .WORD 0\r\n" + //
                                "a2: .WORD 2\r\n" + //
                                "a3: .WORD 0\r\n" + //
                                "B: .WORD 12.0\r\n" + //
                                "z: .WORD 8\r\n" + //
                                "A: .WORD 4\r\n" + //
                                "a4: .WORD 0\r\n" + //
                                "a5: .WORD 0\r\n" + //
                                "P: .WORD 0\r\n" + //
                                "a6: .WORD 0\r\n" + //
                                "M: .WORD 0\r\n" + //
                                "a7: .WORD 1\r\n" + //
                                "a8: .WORD 0\r\n" + //
                                "N: .WORD 0\r\n" + //
                                "a9: .WORD 23\r\n" + //
                                "b0: .WORD 0\r\n" + //
                                "V: .WORD 0\r\n" + //
                                "b1: .WORD 23\r\n" + //
                                "b2: .WORD 0\r\n" + //
                                "b3: .WORD 255\r\n" + //
                                "b4: .WORD 0\r\n" + //
                                "Q: .WORD 0\r\n" + //
                                "b5: .WORD 2\r\n" + //
                                "b6: .WORD 127\r\n" + //
                                "b7: .WORD 0\r\n" + //
                                "E: .WORD 12.0\r\n" + //
                                "C: .WORD 8\r\n" + //
                                "D: .WORD 4\r\n" + //
                                "b8: .WORD 0\r\n" + //
                                "Q: .WORD 0\r\n" + //
                                "b9: .WORD 31\r\n" + //
                                "c0: .WORD 0\r\n" + //
                                "c1: .WORD 1\r\n" + //
                                "c2: .WORD 0\r\n" + //
                                "R: .WORD 0\r\n" + //
                                "c3: .WORD 1\r\n" + //
                                "c4: .WORD 0\r\n" + //
                                "n2: .BYTE 1\r\n" + //
                                "c5: .WORD 1\r\n" + //
                                "c6: .WORD 0\r\n" + //
                                "R: .WORD 0\r\n" + //
                                "c7: .WORD 0\r\n" + //
                                "c8: .WORD 0\r\n" + //
                                "L: .WORD 0\r\n" + //
                                "d1: .WORD 0\r\n" + //
                                "d3: .WORD 255\r\n" + //
                                "d4: .WORD 0\r\n" + //
                                "d0: .WORD 0\r\n" + //
                                "d5: .WORD 2\r\n" + //
                                "d6: .WORD 127\r\n" + //
                                "d7: .WORD 0\r\n" + //
                                "E: .WORD 12.0\r\n" + //
                                "C: .WORD 8\r\n" + //
                                "D: .WORD 4\r\n" + //
                                "d8: .WORD 0\r\n" + //
                                "c9: .WORD 0\r\n" + //
                                "e0: .WORD 0\r\n" + //
                                "e1: .WORD 0\r\n" + //
                                "e2: .WORD 0\r\n" + //
                                "n7: .BYTE 1\r\n" + //
                                "d9: .WORD 0\r\n" + //
                                "e3: .WORD 0\r\n" + //
                                "d9: .WORD 0\r\n" + //
                                "e6: .WORD 0\r\n" + //
                                "e4: .WORD 0\r\n" + //
                                "e7: .WORD 0\r\n" + //
                                "e5: .WORD 0\r\n" + //
                                "e8: .WORD 0\r\n" + //
                                "e9: .WORD 0\r\n" + //
                                "f0: .WORD 0\r\n" + //
                                "f1: .WORD 0\r\n" + //
                                "n9: .BYTE 1\r\n" + //
                                "o0: .BYTE 1\r\n" + //
                                "f2: .WORD 0\r\n" + //
                                "e4: .WORD 0\r\n" + //
                                "f3: .WORD 1\r\n" + //
                                "f4: .WORD 0\r\n" + //
                                "e5: .WORD 0\r\n" + //
                                "f5: .WORD 0\r\n" + //
                                "f6: .WORD 0\r\n" + //
                                "f7: .WORD 0\r\n" + //
                                "f1: .WORD 0\r\n" + //
                                "g0: .WORD 0\r\n" + //
                                "g1: .WORD 0\r\n" + //
                                "s: .WORD 12.0\r\n" + //
                                "q: .WORD 8\r\n" + //
                                "r: .WORD 4\r\n" + //
                                "g2: .WORD 0\r\n" + //
                                "f8: .WORD 0\r\n" + //
                                "l: .WORD 8.0\r\n" + //
                                "k: .WORD 4\r\n" + //
                                "g3: .WORD 0\r\n" + //
                                "f9: .WORD 0\r\n" + //
                                "g6: .WORD 0\r\n" + //
                                "g4: .WORD 0\r\n" + //
                                "g7: .WORD 0\r\n" + //
                                "g8: .WORD 0\r\n" + //
                                "g9: .WORD 0\r\n" + //
                                "h0: .WORD 0\r\n" + //
                                "o6: .BYTE 1\r\n" + //
                                "o7: .BYTE 1\r\n" + //
                                "h1: .WORD 0\r\n" + //
                                "g4: .WORD 0\r\n" + //
                                "h2: .WORD 0\r\n" + //
                                "h3: .WORD 0\r\n" + //
                                "h4: .WORD 0\r\n" + //
                                "h0: .WORD 0\r\n" + //
                                "h7: .WORD 1\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "h8: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "h9: .WORD 0\r\n" + //
                                "i0: .WORD 0\r\n" + //
                                "i1: .WORD 0\r\n" + //
                                "o9: .BYTE 1\r\n" + //
                                "i2: .WORD 0\r\n" + //
                                "p0: .BYTE 1\r\n" + //
                                "p1: .BYTE 1\r\n" + //
                                "i3: .WORD 0\r\n" + //
                                "i4: .WORD 0\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "i5: .WORD 1\r\n" + //
                                "i6: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "i7: .WORD 0\r\n" + //
                                "i2: .WORD 0\r\n" + //
                                "i8: .WORD 0\r\n" + //
                                "i9: .WORD 0\r\n" + //
                                "p2: .BYTE 1\r\n" + //
                                "j0: .WORD 0\r\n" + //
                                "p3: .BYTE 1\r\n" + //
                                "p4: .BYTE 1\r\n" + //
                                "j1: .WORD 0\r\n" + //
                                "h5: .WORD 0\r\n" + //
                                "j2: .WORD 1\r\n" + //
                                "j3: .WORD 0\r\n" + //
                                "h6: .WORD 0\r\n" + //
                                "j4: .WORD 0\r\n" + //
                                "j0: .WORD 0\r\n" + //
                                "j7: .WORD 1\r\n" + //
                                "j5: .WORD 0\r\n" + //
                                "j8: .WORD 0\r\n" + //
                                "j6: .WORD 0\r\n" + //
                                "j9: .WORD 0\r\n" + //
                                "k0: .WORD 0\r\n" + //
                                "p6: .BYTE 1\r\n" + //
                                "p7: .BYTE 1\r\n" + //
                                "k1: .WORD 0\r\n" + //
                                "k2: .WORD 0\r\n" + //
                                "j5: .WORD 0\r\n" + //
                                "k3: .WORD 1\r\n" + //
                                "k4: .WORD 0\r\n" + //
                                "j6: .WORD 0\r\n" + //
                                "k5: .WORD 0\r\n" + //
                                "k0: .WORD 0\r\n" + //
                                "k6: .WORD 0\r\n" + //
                                "k7: .WORD 0\r\n" + //
                                "k8: .WORD 0.5\r\n" + //
                                "k9: .WORD 0\r\n" + //
                                "G: .WORD 8.0\r\n" + //
                                "F: .WORD 4\r\n" + //
                                "l0: .WORD 0\r\n" + //
                                "l1: .WORD 1\r\n" + //
                                "l2: .WORD 0\r\n" + //
                                "l3: .WORD 0\r\n" + //
                                "G: .WORD 8.0\r\n" + //
                                "F: .WORD 4\r\n" + //
                                "l4: .WORD 0\r\n" + //
                                "l5: .WORD 0\r\n" + //
                                "l6: .WORD 0\r\n" + //
                                "l8: .WORD 1\r\n" + //
                                "l7: .WORD 0\r\n" + //
                                "l9: .WORD 10\r\n" + //
                                "c: .WORD 4\r\n" + //
                                "m0: .WORD 1\r\n" + //
                                "m1: .WORD 0\r\n" + //
                                "l7: .WORD 0\r\n" + //
                                "totalBytes: .WORD 3668\r\n" + //
                                "begin_0: B begin_1\r\n" + //
                                "WriteLn: SWI 4\r\n" + //
                                "LDR R2, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "WriteInt: LDR R2, c\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, h\r\n" + //
                                "LDR R0, h\r\n" + //
                                "SWI 1\r\n" + //
                                "LDR R3, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "WriteReal: LDR R3, e\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, i\r\n" + //
                                "LDR R0, i\r\n" + //
                                "SWI 2\r\n" + //
                                "LDR R2, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "ReadInt: SWI 3\r\n" + //
                                "STR R0, j\r\n" + //
                                "LDR R2, j\r\n" + //
                                "LDR R3, g\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "begin_1: B begin_2\r\n" + //
                                "IntToReal: LDR R3, S\r\n" + //
                                "STR R3, M\r\n" + //
                                "LDR R2, T\r\n" + //
                                "STR R2, N\r\n" + //
                                "LDR R3, N\r\n" + //
                                "LDR R3, U\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLE R3, #1\r\n" + //
                                "MOVGT R3, #0\r\n" + //
                                "STR R3, V\r\n" + //
                                "LDR R3, V\r\n" + //
                                "LDR R3, m6\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_0_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_0_SEQ_0_LEVEL_0: LDR R3, V\r\n" + //
                                "LDR R3, m7\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_0_LEVEL_0\r\n" + //
                                "WHILESTAT_0_SEQ_0_LEVEL_0: LDR R3, k\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, W\r\n" + //
                                "LDR R3, X\r\n" + //
                                "LDR R3, N\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, Y\r\n" + //
                                "LDR R3, W\r\n" + //
                                "LDR R3, Y\r\n" + //
                                "MOV R3, R3, LSR R3\r\n" + //
                                "STR R3, Z\r\n" + //
                                "LDR R3, Z\r\n" + //
                                "STR R3, O\r\n" + //
                                "LDR R2, O\r\n" + //
                                "LDR R3, a0\r\n" + //
                                "AND R4, R2, R3\r\n" + //
                                "STR R4, a1\r\n" + //
                                "LDR R2, a1\r\n" + //
                                "STR R2, O\r\n" + //
                                "LDR R3, N\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R3, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL INeg\r\n" + //
                                "LDR R3, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R3, a3\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, z\r\n" + //
                                "LDR R4, a2\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, A\r\n" + //
                                "LDR R5, a3\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL RealExp\r\n" + //
                                "LDR R2, B\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, a4\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, O\r\n" + //
                                "LDR R3, a4\r\n" + //
                                "MUL R4, R2, R3\r\n" + //
                                "STR R4, a5\r\n" + //
                                "LDR R2, a5\r\n" + //
                                "STR R2, P\r\n" + //
                                "LDR R3, M\r\n" + //
                                "LDR R3, P\r\n" + //
                                "ADD R3, R3, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R3, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R3, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R3, a6\r\n" + //
                                "LDR R3, a6\r\n" + //
                                "STR R3, M\r\n" + //
                                "LDR R2, N\r\n" + //
                                "LDR R3, a7\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, a8\r\n" + //
                                "LDR R2, a8\r\n" + //
                                "STR R2, N\r\n" + //
                                "LDR R3, N\r\n" + //
                                "LDR R3, a9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLE R3, #1\r\n" + //
                                "MOVGT R3, #0\r\n" + //
                                "STR R3, b0\r\n" + //
                                "LDR R3, b0\r\n" + //
                                "STR R3, V\r\n" + //
                                "B WHILECOND_0_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_0_LEVEL_0: LDR R2, W\r\n" + //
                                "LDR R3, b1\r\n" + //
                                "MOV R4, R2, LSR R3\r\n" + //
                                "STR R4, b2\r\n" + //
                                "LDR R2, b2\r\n" + //
                                "LDR R2, b3\r\n" + //
                                "AND R2, R2, R2\r\n" + //
                                "STR R2, b4\r\n" + //
                                "LDR R2, b4\r\n" + //
                                "STR R2, Q\r\n" + //
                                "LDR R2, Q\r\n" + //
                                "LDR R3, b6\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, b7\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R2, C\r\n" + //
                                "LDR R3, b5\r\n" + //
                                "STR R3, [R13,-R2]\r\n" + //
                                "LDR R4, D\r\n" + //
                                "LDR R5, b7\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL IntExp\r\n" + //
                                "LDR R3, E\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, b8\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, b8\r\n" + //
                                "STR R2, Q\r\n" + //
                                "LDR R3, W\r\n" + //
                                "LDR R3, b9\r\n" + //
                                "MOV R3, R3, LSR R3\r\n" + //
                                "STR R3, c0\r\n" + //
                                "LDR R3, c0\r\n" + //
                                "LDR R3, c1\r\n" + //
                                "AND R3, R3, R3\r\n" + //
                                "STR R3, c2\r\n" + //
                                "LDR R3, c2\r\n" + //
                                "STR R3, R\r\n" + //
                                "LDR R2, R\r\n" + //
                                "LDR R3, c3\r\n" + //
                                "TEQ R2, R3\r\n" + //
                                "MOVEQ R4, #1\r\n" + //
                                "MOVNE R4, #0\r\n" + //
                                "STR R4, c4\r\n" + //
                                "LDR R2, c4\r\n" + //
                                "LDR R2, n2\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_0_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_0_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_0_SEQ_0_LEVEL_0: LDR R2, c5\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL INeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, c6\r\n" + //
                                "LDR R2, c6\r\n" + //
                                "STR R2, R\r\n" + //
                                "B IFEND_0_LEVEL_0\r\n" + //
                                "IFNEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_0_LEVEL_0: LDR R3, R\r\n" + //
                                "LDR R3, Q\r\n" + //
                                "MUL R3, R3, R3\r\n" + //
                                "STR R3, c7\r\n" + //
                                "LDR R3, c7\r\n" + //
                                "LDR R3, M\r\n" + //
                                "MUL R3, R3, R3\r\n" + //
                                "STR R3, c8\r\n" + //
                                "LDR R3, c8\r\n" + //
                                "STR R3, L\r\n" + //
                                "LDR R2, L\r\n" + //
                                "LDR R3, l\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "RealToInt: LDR R3, m\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, d1\r\n" + //
                                "LDR R2, d1\r\n" + //
                                "LDR R3, d3\r\n" + //
                                "AND R4, R2, R3\r\n" + //
                                "STR R4, d4\r\n" + //
                                "LDR R2, d4\r\n" + //
                                "STR R2, d0\r\n" + //
                                "LDR R3, d0\r\n" + //
                                "LDR R3, d6\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, d7\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, C\r\n" + //
                                "LDR R4, d5\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, D\r\n" + //
                                "LDR R5, d7\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL IntExp\r\n" + //
                                "LDR R2, E\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, d8\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, d8\r\n" + //
                                "STR R2, c9\r\n" + //
                                "LDR R3, c9\r\n" + //
                                "LDR R3, n\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Abs: LDR R3, o\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, e0\r\n" + //
                                "LDR R2, e0\r\n" + //
                                "LDR R3, e1\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVGE R4, #1\r\n" + //
                                "MOVLT R4, #0\r\n" + //
                                "STR R4, e2\r\n" + //
                                "LDR R2, e2\r\n" + //
                                "LDR R2, n7\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_1_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_1_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_1_SEQ_0_LEVEL_0: LDR R2, e0\r\n" + //
                                "STR R2, d9\r\n" + //
                                "B IFEND_1_LEVEL_0\r\n" + //
                                "IFNEXT_1_SEQ_0_LEVEL_0: LDR R2, e0\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, e3\r\n" + //
                                "LDR R2, e3\r\n" + //
                                "STR R2, d9\r\n" + //
                                "B IFEND_1_LEVEL_0\r\n" + //
                                "IFNEXT_1_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_1_LEVEL_0: LDR R3, d9\r\n" + //
                                "LDR R3, p\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Div: LDR R3, q\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, e6\r\n" + //
                                "LDR R2, e6\r\n" + //
                                "STR R2, e4\r\n" + //
                                "LDR R3, e7\r\n" + //
                                "STR R3, e5\r\n" + //
                                "LDR R2, r\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, e8\r\n" + //
                                "LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, e9\r\n" + //
                                "LDR R3, e9\r\n" + //
                                "LDR R3, f0\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, f1\r\n" + //
                                "LDR R3, f1\r\n" + //
                                "LDR R3, n9\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_2_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_2_SEQ_0_LEVEL_0: LDR R3, f1\r\n" + //
                                "LDR R3, o0\r\n" + //
                                "TST R3, R3\r\n" + //
                                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_2_LEVEL_0\r\n" + //
                                "WHILESTAT_2_SEQ_0_LEVEL_0: LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, f2\r\n" + //
                                "LDR R3, f2\r\n" + //
                                "STR R3, e4\r\n" + //
                                "LDR R2, e5\r\n" + //
                                "LDR R3, f3\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, f4\r\n" + //
                                "LDR R2, f4\r\n" + //
                                "STR R2, e5\r\n" + //
                                "LDR R3, e4\r\n" + //
                                "LDR R3, e8\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, f5\r\n" + //
                                "LDR R3, f5\r\n" + //
                                "LDR R3, f6\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, f7\r\n" + //
                                "LDR R3, f7\r\n" + //
                                "STR R3, f1\r\n" + //
                                "B WHILECOND_2_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_2_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_2_LEVEL_0: LDR R2, e5\r\n" + //
                                "LDR R3, s\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Divide: LDR R3, t\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g0\r\n" + //
                                "LDR R2, u\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g1\r\n" + //
                                "\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "LDR R3, q\r\n" + //
                                "LDR R4, g0\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "LDR R4, r\r\n" + //
                                "LDR R5, g1\r\n" + //
                                "STR R5, [R13,-R4]\r\n" + //
                                "BL Div\r\n" + //
                                "LDR R2, s\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g2\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "LDR R2, g2\r\n" + //
                                "STR R2, f8\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R3, k\r\n" + //
                                "LDR R4, f8\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R2, l\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, g3\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, g3\r\n" + //
                                "STR R2, f9\r\n" + //
                                "LDR R3, f9\r\n" + //
                                "LDR R3, v\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Mod: LDR R3, w\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g6\r\n" + //
                                "LDR R2, g6\r\n" + //
                                "STR R2, g4\r\n" + //
                                "LDR R3, x\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, g7\r\n" + //
                                "LDR R2, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, g8\r\n" + //
                                "LDR R4, g8\r\n" + //
                                "LDR R5, g9\r\n" + //
                                "CMP R4, R5\r\n" + //
                                "MOVGT R6, #1\r\n" + //
                                "MOVLE R6, #0\r\n" + //
                                "STR R6, h0\r\n" + //
                                "LDR R2, h0\r\n" + //
                                "LDR R3, o6\r\n" + //
                                "TST R2, R3\r\n" + //
                                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_4_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_4_SEQ_0_LEVEL_0: LDR R2, h0\r\n" + //
                                "LDR R3, o7\r\n" + //
                                "TST R2, R3\r\n" + //
                                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_4_LEVEL_0\r\n" + //
                                "WHILESTAT_4_SEQ_0_LEVEL_0: LDR R3, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R3, R3, R3\r\n" + //
                                "STR R3, h1\r\n" + //
                                "LDR R3, h1\r\n" + //
                                "STR R3, g4\r\n" + //
                                "LDR R2, g4\r\n" + //
                                "LDR R3, g7\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, h2\r\n" + //
                                "LDR R4, h2\r\n" + //
                                "LDR R5, h3\r\n" + //
                                "CMP R4, R5\r\n" + //
                                "MOVGT R6, #1\r\n" + //
                                "MOVLE R6, #0\r\n" + //
                                "STR R6, h4\r\n" + //
                                "LDR R2, h4\r\n" + //
                                "STR R2, h0\r\n" + //
                                "B WHILECOND_4_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_4_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_4_LEVEL_0: LDR R3, g4\r\n" + //
                                "LDR R3, y\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "RealExp: LDR R3, h7\r\n" + //
                                "STR R3, h5\r\n" + //
                                "LDR R2, h8\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, A\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, h9\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "LDR R3, i0\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVGT R4, #1\r\n" + //
                                "MOVLE R4, #0\r\n" + //
                                "STR R4, i1\r\n" + //
                                "LDR R2, i1\r\n" + //
                                "LDR R2, o9\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_2_SEQ_0_LEVEL_0\r\n" + //
                                "BNE IFNEXT_2_SEQ_0_LEVEL_0\r\n" + //
                                "IFSTAT_2_SEQ_0_LEVEL_0: LDR R2, h6\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "CMP R2, R2\r\n" + //
                                "MOVLT R2, #1\r\n" + //
                                "MOVGE R2, #0\r\n" + //
                                "STR R2, i2\r\n" + //
                                "LDR R2, i2\r\n" + //
                                "LDR R2, p0\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_6_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_6_SEQ_0_LEVEL_0: LDR R2, i2\r\n" + //
                                "LDR R2, p1\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_6_LEVEL_0\r\n" + //
                                "WHILESTAT_6_SEQ_0_LEVEL_0: LDR R2, z\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, i3\r\n" + //
                                "LDR R2, h5\r\n" + //
                                "LDR R2, i3\r\n" + //
                                "MUL R2, R2, R2\r\n" + //
                                "STR R2, i4\r\n" + //
                                "LDR R2, i4\r\n" + //
                                "STR R2, h5\r\n" + //
                                "LDR R2, h6\r\n" + //
                                "LDR R3, i5\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "STR R4, i6\r\n" + //
                                "LDR R2, i6\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, h6\r\n" + //
                                "LDR R3, h9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLT R3, #1\r\n" + //
                                "MOVGE R3, #0\r\n" + //
                                "STR R3, i7\r\n" + //
                                "LDR R3, i7\r\n" + //
                                "STR R3, i2\r\n" + //
                                "B WHILECOND_6_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_6_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_6_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                                "IFNEXT_2_SEQ_0_LEVEL_0: LDR R2, h9\r\n" + //
                                "LDR R3, i8\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVLT R4, #1\r\n" + //
                                "MOVGE R4, #0\r\n" + //
                                "STR R4, i9\r\n" + //
                                "LDR R2, i9\r\n" + //
                                "LDR R2, p2\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ IFSTAT_2_SEQ_1_LEVEL_0\r\n" + //
                                "BNE IFNEXT_2_SEQ_1_LEVEL_0\r\n" + //
                                "IFSTAT_2_SEQ_1_LEVEL_0: LDR R2, h6\r\n" + //
                                "LDR R2, h9\r\n" + //
                                "CMP R2, R2\r\n" + //
                                "MOVGT R2, #1\r\n" + //
                                "MOVLE R2, #0\r\n" + //
                                "STR R2, j0\r\n" + //
                                "LDR R2, j0\r\n" + //
                                "LDR R2, p3\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_8_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_8_SEQ_0_LEVEL_0: LDR R2, j0\r\n" + //
                                "LDR R2, p4\r\n" + //
                                "TST R2, R2\r\n" + //
                                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_8_LEVEL_0\r\n" + //
                                "WHILESTAT_8_SEQ_0_LEVEL_0: LDR R2, h5\r\n" + //
                                "LDR R2, i3\r\n" + //
                                "ADD R13, R13, #16\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R2, [R13, #-8]\r\n" + //
                                "STR R14, [R13, #-16]\r\n" + //
                                "BL Divide\r\n" + //
                                "LDR R2, [R13, #-12]\r\n" + //
                                "LDR R14, [R13, #-16]\r\n" + //
                                "SUB R13, R13, #16\r\n" + //
                                "STR R2, j1\r\n" + //
                                "LDR R2, j1\r\n" + //
                                "STR R2, h5\r\n" + //
                                "LDR R2, h6\r\n" + //
                                "LDR R3, j2\r\n" + //
                                "SUB R4, R2, R3\r\n" + //
                                "STR R4, j3\r\n" + //
                                "LDR R2, j3\r\n" + //
                                "STR R2, h6\r\n" + //
                                "LDR R3, h6\r\n" + //
                                "LDR R3, h9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVGT R3, #1\r\n" + //
                                "MOVLE R3, #0\r\n" + //
                                "STR R3, j4\r\n" + //
                                "LDR R3, j4\r\n" + //
                                "STR R3, j0\r\n" + //
                                "B WHILECOND_8_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_8_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_8_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                                "IFNEXT_2_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "IFEND_2_LEVEL_0: LDR R2, h5\r\n" + //
                                "LDR R3, B\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "IntExp: LDR R3, j7\r\n" + //
                                "STR R3, j5\r\n" + //
                                "LDR R2, j8\r\n" + //
                                "STR R2, j6\r\n" + //
                                "LDR R3, D\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, j9\r\n" + //
                                "LDR R2, j6\r\n" + //
                                "LDR R3, j9\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "MOVLT R4, #1\r\n" + //
                                "MOVGE R4, #0\r\n" + //
                                "STR R4, k0\r\n" + //
                                "LDR R4, k0\r\n" + //
                                "LDR R5, p6\r\n" + //
                                "TST R4, R5\r\n" + //
                                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILENEXT_10_SEQ_0_LEVEL_0\r\n" + //
                                "WHILECOND_10_SEQ_0_LEVEL_0: LDR R4, k0\r\n" + //
                                "LDR R5, p7\r\n" + //
                                "TST R4, R5\r\n" + //
                                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                                "BNE WHILEEND_10_LEVEL_0\r\n" + //
                                "WHILESTAT_10_SEQ_0_LEVEL_0: LDR R2, C\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, k1\r\n" + //
                                "LDR R2, j5\r\n" + //
                                "LDR R2, k1\r\n" + //
                                "MUL R2, R2, R2\r\n" + //
                                "STR R2, k2\r\n" + //
                                "LDR R2, k2\r\n" + //
                                "STR R2, j5\r\n" + //
                                "LDR R2, j6\r\n" + //
                                "LDR R3, k3\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R4, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R4, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R4, k4\r\n" + //
                                "LDR R2, k4\r\n" + //
                                "STR R2, j6\r\n" + //
                                "LDR R3, j6\r\n" + //
                                "LDR R3, j9\r\n" + //
                                "CMP R3, R3\r\n" + //
                                "MOVLT R3, #1\r\n" + //
                                "MOVGE R3, #0\r\n" + //
                                "STR R3, k5\r\n" + //
                                "LDR R3, k5\r\n" + //
                                "STR R3, k0\r\n" + //
                                "B WHILECOND_10_SEQ_0_LEVEL_0\r\n" + //
                                "WHILENEXT_10_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                                "WHILEEND_10_LEVEL_0: LDR R2, j5\r\n" + //
                                "LDR R3, E\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Floor: LDR R3, F\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, k6\r\n" + //
                                "LDR R2, k6\r\n" + //
                                "LDR R3, G\r\n" + //
                                "STR R2, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Round: LDR R3, H\r\n" + //
                                "LDR R3, [R13, -R3]\r\n" + //
                                "STR R3, k7\r\n" + //
                                "LDR R2, k7\r\n" + //
                                "LDR R3, k8\r\n" + //
                                "ADD R4, R2, R3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R4, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL IntToReal\r\n" + //
                                "LDR R4, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R4, k9\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R2, F\r\n" + //
                                "LDR R3, k9\r\n" + //
                                "STR R3, [R13,-R2]\r\n" + //
                                "BL Floor\r\n" + //
                                "LDR R4, G\r\n" + //
                                "LDR R4, [R13, -R4]\r\n" + //
                                "STR R4, l0\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, l0\r\n" + //
                                "LDR R2, I\r\n" + //
                                "STR R2, [R13, -R2]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "Ceil: LDR R2, J\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, l2\r\n" + //
                                "LDR R2, l2\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, l3\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "LDR R3, F\r\n" + //
                                "LDR R4, l3\r\n" + //
                                "STR R4, [R13,-R3]\r\n" + //
                                "BL Floor\r\n" + //
                                "LDR R2, G\r\n" + //
                                "LDR R2, [R13, -R2]\r\n" + //
                                "STR R2, l4\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "LDR R2, l1\r\n" + //
                                "LDR R3, l4\r\n" + //
                                "MUL R4, R2, R3\r\n" + //
                                "STR R4, l5\r\n" + //
                                "LDR R2, l5\r\n" + //
                                "ADD R13, R13, #12\r\n" + //
                                "STR R2, [R13, #-4]\r\n" + //
                                "STR R14, [R13, #-12]\r\n" + //
                                "BL RNeg\r\n" + //
                                "LDR R2, [R13, #-8]\r\n" + //
                                "LDR R14, [R13, #-12]\r\n" + //
                                "SUB R13, R13, #12\r\n" + //
                                "STR R2, l6\r\n" + //
                                "LDR R3, l6\r\n" + //
                                "LDR R3, K\r\n" + //
                                "STR R3, [R13, -R3]\r\n" + //
                                "LDR R4, [R13]\r\n" + //
                                "SUB R13, R13, #2\r\n" + //
                                "MOV R15, R14\r\n" + //
                                "begin_2: B begin_3\r\n" + //
                                "begin_3: LDR R3, l8\r\n" + //
                                "STR R3, l7\r\n" + //
                                "FORBEG_0_LEVEL_0: LDR R2, l7\r\n" + //
                                "LDR R3, l9\r\n" + //
                                "CMP R2, R3\r\n" + //
                                "BLT FORLOOP_0_LEVEL_0\r\n" + //
                                "BGE FOREND_0_LEVEL_0\r\n" + //
                                "FORLOOP_0_LEVEL_0: ADD R13, R13, #8\r\n" + //
                                "STR R14, [R13, #-8]\r\n" + //
                                "LDR R4, c\r\n" + //
                                "LDR R2, l7\r\n" + //
                                "STR R2, [R13,-R4]\r\n" + //
                                "BL WriteInt\r\n" + //
                                "LDR R14, [R13, #-8]\r\n" + //
                                "SUB R13, R13, #8\r\n" + //
                                "LDR R2, l7\r\n" + //
                                "LDR R4, m0\r\n" + //
                                "ADD R5, R2, R4\r\n" + //
                                "STR R5, m1\r\n" + //
                                "LDR R2, m1\r\n" + //
                                "STR R2, l7\r\n" + //
                                "B FORBEG_0_LEVEL_0\r\n" + //
                                "FOREND_0_LEVEL_0: ADD R13, R13, #4\r\n" + //
                                "STR R14, [R13, #-4]\r\n" + //
                                "BL WriteLn\r\n" + //
                                "LDR R14, [R13, #-4]\r\n" + //
                                "SUB R13, R13, #4\r\n" + //
                                "STP\r\n";
        testDeclanFile("test_source/ForLoopBasic2.dcl", expectedResult);
    }

    @Test
    public void testForLoopBasic3(){
        String expectedResult = "";
        testDeclanFile("test_source/ForLoopBasic3.dcl", expectedResult);
    }

    @Test
    public void testIfStatementAdvanced(){
        String expectedResult = "";
        testDeclanFile("test_source/IfStatementAdvanced.dcl", expectedResult);
    }

    @Test
    public void testIfStatementBasic(){
        String expectedResult = "LDR R13, totalBytes\r\n" + //
                "B begin_0\r\n" + //
                "a7: .WORD 0\r\n" + //
                "a8: .WORD 0\r\n" + //
                "a9: .WORD 0\r\n" + //
                "e4: .WORD 1\r\n" + //
                "d8: .WORD 0\r\n" + //
                "e5: .WORD 1\r\n" + //
                "d9: .WORD 0\r\n" + //
                "e6: .WORD 23\r\n" + //
                "e7: .WORD 0\r\n" + //
                "s2: .BYTE 1\r\n" + //
                "s3: .BYTE 1\r\n" + //
                "e8: .WORD 0\r\n" + //
                "e9: .WORD 23\r\n" + //
                "f0: .WORD 0\r\n" + //
                "f1: .WORD 0\r\n" + //
                "e0: .WORD 0\r\n" + //
                "f2: .WORD 1\r\n" + //
                "f3: .WORD 0\r\n" + //
                "e0: .WORD 0\r\n" + //
                "f4: .WORD 2\r\n" + //
                "f5: .WORD 0\r\n" + //
                "c7: .WORD 12.0\r\n" + //
                "c5: .WORD 8\r\n" + //
                "c6: .WORD 4\r\n" + //
                "f6: .WORD 0\r\n" + //
                "f7: .WORD 0\r\n" + //
                "e1: .WORD 0\r\n" + //
                "f8: .WORD 0\r\n" + //
                "d8: .WORD 0\r\n" + //
                "f9: .WORD 1\r\n" + //
                "g0: .WORD 0\r\n" + //
                "d9: .WORD 0\r\n" + //
                "g1: .WORD 23\r\n" + //
                "g2: .WORD 0\r\n" + //
                "e7: .WORD 0\r\n" + //
                "g3: .WORD 23\r\n" + //
                "g4: .WORD 0\r\n" + //
                "g5: .WORD 255\r\n" + //
                "g6: .WORD 0\r\n" + //
                "e2: .WORD 0\r\n" + //
                "g7: .WORD 2\r\n" + //
                "g8: .WORD 127\r\n" + //
                "g9: .WORD 0\r\n" + //
                "d0: .WORD 12.0\r\n" + //
                "c8: .WORD 8\r\n" + //
                "c9: .WORD 4\r\n" + //
                "h0: .WORD 0\r\n" + //
                "e2: .WORD 0\r\n" + //
                "h1: .WORD 31\r\n" + //
                "h2: .WORD 0\r\n" + //
                "h3: .WORD 1\r\n" + //
                "h4: .WORD 0\r\n" + //
                "e3: .WORD 0\r\n" + //
                "h5: .WORD 1\r\n" + //
                "h6: .WORD 0\r\n" + //
                "s8: .BYTE 1\r\n" + //
                "h7: .WORD 1\r\n" + //
                "h8: .WORD 0\r\n" + //
                "e3: .WORD 0\r\n" + //
                "h9: .WORD 0\r\n" + //
                "i0: .WORD 0\r\n" + //
                "d7: .WORD 0\r\n" + //
                "i3: .WORD 0\r\n" + //
                "i5: .WORD 255\r\n" + //
                "i6: .WORD 0\r\n" + //
                "i2: .WORD 0\r\n" + //
                "i7: .WORD 2\r\n" + //
                "i8: .WORD 127\r\n" + //
                "i9: .WORD 0\r\n" + //
                "d0: .WORD 12.0\r\n" + //
                "c8: .WORD 8\r\n" + //
                "c9: .WORD 4\r\n" + //
                "j0: .WORD 0\r\n" + //
                "i1: .WORD 0\r\n" + //
                "j2: .WORD 0\r\n" + //
                "j3: .WORD 0\r\n" + //
                "j4: .WORD 0\r\n" + //
                "t3: .BYTE 1\r\n" + //
                "j1: .WORD 0\r\n" + //
                "j5: .WORD 0\r\n" + //
                "j1: .WORD 0\r\n" + //
                "j8: .WORD 0\r\n" + //
                "j6: .WORD 0\r\n" + //
                "j9: .WORD 0\r\n" + //
                "j7: .WORD 0\r\n" + //
                "k0: .WORD 0\r\n" + //
                "k1: .WORD 0\r\n" + //
                "k2: .WORD 0\r\n" + //
                "k3: .WORD 0\r\n" + //
                "t5: .BYTE 1\r\n" + //
                "t6: .BYTE 1\r\n" + //
                "k4: .WORD 0\r\n" + //
                "j6: .WORD 0\r\n" + //
                "k5: .WORD 1\r\n" + //
                "k6: .WORD 0\r\n" + //
                "j7: .WORD 0\r\n" + //
                "k7: .WORD 0\r\n" + //
                "k8: .WORD 0\r\n" + //
                "k9: .WORD 0\r\n" + //
                "k3: .WORD 0\r\n" + //
                "l2: .WORD 0\r\n" + //
                "l3: .WORD 0\r\n" + //
                "b8: .WORD 12.0\r\n" + //
                "b6: .WORD 8\r\n" + //
                "b7: .WORD 4\r\n" + //
                "l4: .WORD 0\r\n" + //
                "l0: .WORD 0\r\n" + //
                "b1: .WORD 8.0\r\n" + //
                "b0: .WORD 4\r\n" + //
                "l5: .WORD 0\r\n" + //
                "l1: .WORD 0\r\n" + //
                "l8: .WORD 0\r\n" + //
                "l6: .WORD 0\r\n" + //
                "l9: .WORD 0\r\n" + //
                "m0: .WORD 0\r\n" + //
                "m1: .WORD 0\r\n" + //
                "m2: .WORD 0\r\n" + //
                "u2: .BYTE 1\r\n" + //
                "u3: .BYTE 1\r\n" + //
                "m3: .WORD 0\r\n" + //
                "l6: .WORD 0\r\n" + //
                "m4: .WORD 0\r\n" + //
                "m5: .WORD 0\r\n" + //
                "m6: .WORD 0\r\n" + //
                "m2: .WORD 0\r\n" + //
                "m9: .WORD 1\r\n" + //
                "m7: .WORD 0\r\n" + //
                "n0: .WORD 0\r\n" + //
                "m8: .WORD 0\r\n" + //
                "n1: .WORD 0\r\n" + //
                "n2: .WORD 0\r\n" + //
                "n3: .WORD 0\r\n" + //
                "u5: .BYTE 1\r\n" + //
                "n4: .WORD 0\r\n" + //
                "u6: .BYTE 1\r\n" + //
                "u7: .BYTE 1\r\n" + //
                "n5: .WORD 0\r\n" + //
                "n6: .WORD 0\r\n" + //
                "m7: .WORD 0\r\n" + //
                "n7: .WORD 1\r\n" + //
                "n8: .WORD 0\r\n" + //
                "m8: .WORD 0\r\n" + //
                "n9: .WORD 0\r\n" + //
                "n4: .WORD 0\r\n" + //
                "o0: .WORD 0\r\n" + //
                "o1: .WORD 0\r\n" + //
                "u8: .BYTE 1\r\n" + //
                "o2: .WORD 0\r\n" + //
                "u9: .BYTE 1\r\n" + //
                "v0: .BYTE 1\r\n" + //
                "o3: .WORD 0\r\n" + //
                "m7: .WORD 0\r\n" + //
                "o4: .WORD 1\r\n" + //
                "o5: .WORD 0\r\n" + //
                "m8: .WORD 0\r\n" + //
                "o6: .WORD 0\r\n" + //
                "o2: .WORD 0\r\n" + //
                "o9: .WORD 1\r\n" + //
                "o7: .WORD 0\r\n" + //
                "p0: .WORD 0\r\n" + //
                "o8: .WORD 0\r\n" + //
                "p1: .WORD 0\r\n" + //
                "p2: .WORD 0\r\n" + //
                "v2: .BYTE 1\r\n" + //
                "v3: .BYTE 1\r\n" + //
                "p3: .WORD 0\r\n" + //
                "p4: .WORD 0\r\n" + //
                "o7: .WORD 0\r\n" + //
                "p5: .WORD 1\r\n" + //
                "p6: .WORD 0\r\n" + //
                "o8: .WORD 0\r\n" + //
                "p7: .WORD 0\r\n" + //
                "p2: .WORD 0\r\n" + //
                "p8: .WORD 0\r\n" + //
                "p9: .WORD 0\r\n" + //
                "q0: .WORD 0.5\r\n" + //
                "q1: .WORD 0\r\n" + //
                "d2: .WORD 8.0\r\n" + //
                "d1: .WORD 4\r\n" + //
                "q2: .WORD 0\r\n" + //
                "q3: .WORD 1\r\n" + //
                "q4: .WORD 0\r\n" + //
                "q5: .WORD 0\r\n" + //
                "d2: .WORD 8.0\r\n" + //
                "d1: .WORD 4\r\n" + //
                "q6: .WORD 0\r\n" + //
                "q7: .WORD 0\r\n" + //
                "q8: .WORD 0\r\n" + //
                "q9: .WORD 1\r\n" + //
                "r0: .WORD 0\r\n" + //
                "r1: .WORD 0\r\n" + //
                "r2: .WORD 0\r\n" + //
                "w0: .BYTE 1\r\n" + //
                "r3: .WORD 4\r\n" + //
                "a2: .WORD 4\r\n" + //
                "r4: .WORD 5\r\n" + //
                "a2: .WORD 4\r\n" + //
                "w3: .BYTE 1\r\n" + //
                "r5: .WORD 2\r\n" + //
                "a2: .WORD 4\r\n" + //
                "w5: .BYTE 1\r\n" + //
                "r6: .WORD 5\r\n" + //
                "a2: .WORD 4\r\n" + //
                "r7: .WORD 6\r\n" + //
                "a2: .WORD 4\r\n" + //
                "totalBytes: .WORD 3855\r\n" + //
                "begin_0: B begin_1\r\n" + //
                "WriteLn: SWI 4\r\n" + //
                "LDR R2, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "WriteInt: LDR R2, a2\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, a7\r\n" + //
                "LDR R0, a7\r\n" + //
                "SWI 1\r\n" + //
                "LDR R3, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "WriteReal: LDR R3, a4\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, a8\r\n" + //
                "LDR R0, a8\r\n" + //
                "SWI 2\r\n" + //
                "LDR R2, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "ReadInt: SWI 3\r\n" + //
                "STR R0, a9\r\n" + //
                "LDR R2, a9\r\n" + //
                "LDR R3, a6\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "begin_1: B begin_2\r\n" + //
                "IntToReal: LDR R3, e4\r\n" + //
                "STR R3, d8\r\n" + //
                "LDR R2, e5\r\n" + //
                "STR R2, d9\r\n" + //
                "LDR R3, d9\r\n" + //
                "LDR R3, e6\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVLE R3, #1\r\n" + //
                "MOVGT R3, #0\r\n" + //
                "STR R3, e7\r\n" + //
                "LDR R3, e7\r\n" + //
                "LDR R3, s2\r\n" + //
                "TST R3, R3\r\n" + //
                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_0_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_0_SEQ_0_LEVEL_0: LDR R3, e7\r\n" + //
                "LDR R3, s3\r\n" + //
                "TST R3, R3\r\n" + //
                "BEQ WHILESTAT_0_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_0_LEVEL_0\r\n" + //
                "WHILESTAT_0_SEQ_0_LEVEL_0: LDR R3, b0\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, e8\r\n" + //
                "LDR R3, e9\r\n" + //
                "LDR R3, d9\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, f0\r\n" + //
                "LDR R3, e8\r\n" + //
                "LDR R3, f0\r\n" + //
                "MOV R3, R3, LSR R3\r\n" + //
                "STR R3, f1\r\n" + //
                "LDR R3, f1\r\n" + //
                "STR R3, e0\r\n" + //
                "LDR R2, e0\r\n" + //
                "LDR R3, f2\r\n" + //
                "AND R4, R2, R3\r\n" + //
                "STR R4, f3\r\n" + //
                "LDR R2, f3\r\n" + //
                "STR R2, e0\r\n" + //
                "LDR R3, d9\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R3, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL INeg\r\n" + //
                "LDR R3, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R3, f5\r\n" + //
                "ADD R13, R13, #16\r\n" + //
                "STR R14, [R13, #-16]\r\n" + //
                "LDR R3, c5\r\n" + //
                "LDR R4, f4\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "LDR R4, c6\r\n" + //
                "LDR R5, f5\r\n" + //
                "STR R5, [R13,-R4]\r\n" + //
                "BL RealExp\r\n" + //
                "LDR R2, c7\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, f6\r\n" + //
                "LDR R14, [R13, #-16]\r\n" + //
                "SUB R13, R13, #16\r\n" + //
                "LDR R2, e0\r\n" + //
                "LDR R3, f6\r\n" + //
                "MUL R4, R2, R3\r\n" + //
                "STR R4, f7\r\n" + //
                "LDR R2, f7\r\n" + //
                "STR R2, e1\r\n" + //
                "LDR R3, d8\r\n" + //
                "LDR R3, e1\r\n" + //
                "ADD R3, R3, R3\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R3, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL IntToReal\r\n" + //
                "LDR R3, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R3, f8\r\n" + //
                "LDR R3, f8\r\n" + //
                "STR R3, d8\r\n" + //
                "LDR R2, d9\r\n" + //
                "LDR R3, f9\r\n" + //
                "ADD R4, R2, R3\r\n" + //
                "STR R4, g0\r\n" + //
                "LDR R2, g0\r\n" + //
                "STR R2, d9\r\n" + //
                "LDR R3, d9\r\n" + //
                "LDR R3, g1\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVLE R3, #1\r\n" + //
                "MOVGT R3, #0\r\n" + //
                "STR R3, g2\r\n" + //
                "LDR R3, g2\r\n" + //
                "STR R3, e7\r\n" + //
                "B WHILECOND_0_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_0_LEVEL_0: LDR R2, e8\r\n" + //
                "LDR R3, g3\r\n" + //
                "MOV R4, R2, LSR R3\r\n" + //
                "STR R4, g4\r\n" + //
                "LDR R2, g4\r\n" + //
                "LDR R2, g5\r\n" + //
                "AND R2, R2, R2\r\n" + //
                "STR R2, g6\r\n" + //
                "LDR R2, g6\r\n" + //
                "STR R2, e2\r\n" + //
                "LDR R2, e2\r\n" + //
                "LDR R3, g8\r\n" + //
                "SUB R4, R2, R3\r\n" + //
                "STR R4, g9\r\n" + //
                "ADD R13, R13, #16\r\n" + //
                "STR R14, [R13, #-16]\r\n" + //
                "LDR R2, c8\r\n" + //
                "LDR R3, g7\r\n" + //
                "STR R3, [R13,-R2]\r\n" + //
                "LDR R4, c9\r\n" + //
                "LDR R5, g9\r\n" + //
                "STR R5, [R13,-R4]\r\n" + //
                "BL IntExp\r\n" + //
                "LDR R3, d0\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, h0\r\n" + //
                "LDR R14, [R13, #-16]\r\n" + //
                "SUB R13, R13, #16\r\n" + //
                "LDR R2, h0\r\n" + //
                "STR R2, e2\r\n" + //
                "LDR R3, e8\r\n" + //
                "LDR R3, h1\r\n" + //
                "MOV R3, R3, LSR R3\r\n" + //
                "STR R3, h2\r\n" + //
                "LDR R3, h2\r\n" + //
                "LDR R3, h3\r\n" + //
                "AND R3, R3, R3\r\n" + //
                "STR R3, h4\r\n" + //
                "LDR R3, h4\r\n" + //
                "STR R3, e3\r\n" + //
                "LDR R2, e3\r\n" + //
                "LDR R3, h5\r\n" + //
                "TEQ R2, R3\r\n" + //
                "MOVEQ R4, #1\r\n" + //
                "MOVNE R4, #0\r\n" + //
                "STR R4, h6\r\n" + //
                "LDR R2, h6\r\n" + //
                "LDR R2, s8\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ IFSTAT_0_SEQ_0_LEVEL_0\r\n" + //
                "BNE IFNEXT_0_SEQ_0_LEVEL_0\r\n" + //
                "IFSTAT_0_SEQ_0_LEVEL_0: LDR R2, h7\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R2, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL INeg\r\n" + //
                "LDR R2, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R2, h8\r\n" + //
                "LDR R2, h8\r\n" + //
                "STR R2, e3\r\n" + //
                "B IFEND_0_LEVEL_0\r\n" + //
                "IFNEXT_0_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "IFEND_0_LEVEL_0: LDR R3, e3\r\n" + //
                "LDR R3, e2\r\n" + //
                "MUL R3, R3, R3\r\n" + //
                "STR R3, h9\r\n" + //
                "LDR R3, h9\r\n" + //
                "LDR R3, d8\r\n" + //
                "MUL R3, R3, R3\r\n" + //
                "STR R3, i0\r\n" + //
                "LDR R3, i0\r\n" + //
                "STR R3, d7\r\n" + //
                "LDR R2, d7\r\n" + //
                "LDR R3, b1\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "RealToInt: LDR R3, b2\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, i3\r\n" + //
                "LDR R2, i3\r\n" + //
                "LDR R3, i5\r\n" + //
                "AND R4, R2, R3\r\n" + //
                "STR R4, i6\r\n" + //
                "LDR R2, i6\r\n" + //
                "STR R2, i2\r\n" + //
                "LDR R3, i2\r\n" + //
                "LDR R3, i8\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, i9\r\n" + //
                "ADD R13, R13, #16\r\n" + //
                "STR R14, [R13, #-16]\r\n" + //
                "LDR R3, c8\r\n" + //
                "LDR R4, i7\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "LDR R4, c9\r\n" + //
                "LDR R5, i9\r\n" + //
                "STR R5, [R13,-R4]\r\n" + //
                "BL IntExp\r\n" + //
                "LDR R2, d0\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, j0\r\n" + //
                "LDR R14, [R13, #-16]\r\n" + //
                "SUB R13, R13, #16\r\n" + //
                "LDR R2, j0\r\n" + //
                "STR R2, i1\r\n" + //
                "LDR R3, i1\r\n" + //
                "LDR R3, b3\r\n" + //
                "STR R3, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Abs: LDR R3, b4\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, j2\r\n" + //
                "LDR R2, j2\r\n" + //
                "LDR R3, j3\r\n" + //
                "CMP R2, R3\r\n" + //
                "MOVGE R4, #1\r\n" + //
                "MOVLT R4, #0\r\n" + //
                "STR R4, j4\r\n" + //
                "LDR R2, j4\r\n" + //
                "LDR R2, t3\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ IFSTAT_1_SEQ_0_LEVEL_0\r\n" + //
                "BNE IFNEXT_1_SEQ_0_LEVEL_0\r\n" + //
                "IFSTAT_1_SEQ_0_LEVEL_0: LDR R2, j2\r\n" + //
                "STR R2, j1\r\n" + //
                "B IFEND_1_LEVEL_0\r\n" + //
                "IFNEXT_1_SEQ_0_LEVEL_0: LDR R2, j2\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R2, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL RNeg\r\n" + //
                "LDR R2, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R2, j5\r\n" + //
                "LDR R2, j5\r\n" + //
                "STR R2, j1\r\n" + //
                "B IFEND_1_LEVEL_0\r\n" + //
                "IFNEXT_1_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "IFEND_1_LEVEL_0: LDR R3, j1\r\n" + //
                "LDR R3, b5\r\n" + //
                "STR R3, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Div: LDR R3, b6\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, j8\r\n" + //
                "LDR R2, j8\r\n" + //
                "STR R2, j6\r\n" + //
                "LDR R3, j9\r\n" + //
                "STR R3, j7\r\n" + //
                "LDR R2, b7\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, k0\r\n" + //
                "LDR R3, j6\r\n" + //
                "LDR R3, k0\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, k1\r\n" + //
                "LDR R3, k1\r\n" + //
                "LDR R3, k2\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVGT R3, #1\r\n" + //
                "MOVLE R3, #0\r\n" + //
                "STR R3, k3\r\n" + //
                "LDR R3, k3\r\n" + //
                "LDR R3, t5\r\n" + //
                "TST R3, R3\r\n" + //
                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_2_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_2_SEQ_0_LEVEL_0: LDR R3, k3\r\n" + //
                "LDR R3, t6\r\n" + //
                "TST R3, R3\r\n" + //
                "BEQ WHILESTAT_2_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_2_LEVEL_0\r\n" + //
                "WHILESTAT_2_SEQ_0_LEVEL_0: LDR R3, j6\r\n" + //
                "LDR R3, k0\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, k4\r\n" + //
                "LDR R3, k4\r\n" + //
                "STR R3, j6\r\n" + //
                "LDR R2, j7\r\n" + //
                "LDR R3, k5\r\n" + //
                "ADD R4, R2, R3\r\n" + //
                "STR R4, k6\r\n" + //
                "LDR R2, k6\r\n" + //
                "STR R2, j7\r\n" + //
                "LDR R3, j6\r\n" + //
                "LDR R3, k0\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, k7\r\n" + //
                "LDR R3, k7\r\n" + //
                "LDR R3, k8\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVGT R3, #1\r\n" + //
                "MOVLE R3, #0\r\n" + //
                "STR R3, k9\r\n" + //
                "LDR R3, k9\r\n" + //
                "STR R3, k3\r\n" + //
                "B WHILECOND_2_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_2_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_2_LEVEL_0: LDR R2, j7\r\n" + //
                "LDR R3, b8\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Divide: LDR R3, b9\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, l2\r\n" + //
                "LDR R2, c0\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, l3\r\n" + //
                "ADD R13, R13, #16\r\n" + //
                "STR R14, [R13, #-16]\r\n" + //
                "LDR R3, b6\r\n" + //
                "LDR R4, l2\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "LDR R4, b7\r\n" + //
                "LDR R5, l3\r\n" + //
                "STR R5, [R13,-R4]\r\n" + //
                "BL Div\r\n" + //
                "LDR R2, b8\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, l4\r\n" + //
                "LDR R14, [R13, #-16]\r\n" + //
                "SUB R13, R13, #16\r\n" + //
                "LDR R2, l4\r\n" + //
                "STR R2, l0\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "LDR R3, b0\r\n" + //
                "LDR R4, l0\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "BL IntToReal\r\n" + //
                "LDR R2, b1\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, l5\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "LDR R2, l5\r\n" + //
                "STR R2, l1\r\n" + //
                "LDR R3, l1\r\n" + //
                "LDR R3, c1\r\n" + //
                "STR R3, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Mod: LDR R3, c2\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, l8\r\n" + //
                "LDR R2, l8\r\n" + //
                "STR R2, l6\r\n" + //
                "LDR R3, c3\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, l9\r\n" + //
                "LDR R2, l6\r\n" + //
                "LDR R3, l9\r\n" + //
                "SUB R4, R2, R3\r\n" + //
                "STR R4, m0\r\n" + //
                "LDR R4, m0\r\n" + //
                "LDR R5, m1\r\n" + //
                "CMP R4, R5\r\n" + //
                "MOVGT R6, #1\r\n" + //
                "MOVLE R6, #0\r\n" + //
                "STR R6, m2\r\n" + //
                "LDR R2, m2\r\n" + //
                "LDR R3, u2\r\n" + //
                "TST R2, R3\r\n" + //
                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_4_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_4_SEQ_0_LEVEL_0: LDR R2, m2\r\n" + //
                "LDR R3, u3\r\n" + //
                "TST R2, R3\r\n" + //
                "BEQ WHILESTAT_4_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_4_LEVEL_0\r\n" + //
                "WHILESTAT_4_SEQ_0_LEVEL_0: LDR R3, l6\r\n" + //
                "LDR R3, l9\r\n" + //
                "SUB R3, R3, R3\r\n" + //
                "STR R3, m3\r\n" + //
                "LDR R3, m3\r\n" + //
                "STR R3, l6\r\n" + //
                "LDR R2, l6\r\n" + //
                "LDR R3, l9\r\n" + //
                "SUB R4, R2, R3\r\n" + //
                "STR R4, m4\r\n" + //
                "LDR R4, m4\r\n" + //
                "LDR R5, m5\r\n" + //
                "CMP R4, R5\r\n" + //
                "MOVGT R6, #1\r\n" + //
                "MOVLE R6, #0\r\n" + //
                "STR R6, m6\r\n" + //
                "LDR R2, m6\r\n" + //
                "STR R2, m2\r\n" + //
                "B WHILECOND_4_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_4_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_4_LEVEL_0: LDR R3, l6\r\n" + //
                "LDR R3, c4\r\n" + //
                "STR R3, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "RealExp: LDR R3, m9\r\n" + //
                "STR R3, m7\r\n" + //
                "LDR R2, n0\r\n" + //
                "STR R2, m8\r\n" + //
                "LDR R3, c6\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, n1\r\n" + //
                "LDR R2, n1\r\n" + //
                "LDR R3, n2\r\n" + //
                "CMP R2, R3\r\n" + //
                "MOVGT R4, #1\r\n" + //
                "MOVLE R4, #0\r\n" + //
                "STR R4, n3\r\n" + //
                "LDR R2, n3\r\n" + //
                "LDR R2, u5\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ IFSTAT_2_SEQ_0_LEVEL_0\r\n" + //
                "BNE IFNEXT_2_SEQ_0_LEVEL_0\r\n" + //
                "IFSTAT_2_SEQ_0_LEVEL_0: LDR R2, m8\r\n" + //
                "LDR R2, n1\r\n" + //
                "CMP R2, R2\r\n" + //
                "MOVLT R2, #1\r\n" + //
                "MOVGE R2, #0\r\n" + //
                "STR R2, n4\r\n" + //
                "LDR R2, n4\r\n" + //
                "LDR R2, u6\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_6_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_6_SEQ_0_LEVEL_0: LDR R2, n4\r\n" + //
                "LDR R2, u7\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ WHILESTAT_6_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_6_LEVEL_0\r\n" + //
                "WHILESTAT_6_SEQ_0_LEVEL_0: LDR R2, c5\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, n5\r\n" + //
                "LDR R2, m7\r\n" + //
                "LDR R2, n5\r\n" + //
                "MUL R2, R2, R2\r\n" + //
                "STR R2, n6\r\n" + //
                "LDR R2, n6\r\n" + //
                "STR R2, m7\r\n" + //
                "LDR R2, m8\r\n" + //
                "LDR R3, n7\r\n" + //
                "ADD R4, R2, R3\r\n" + //
                "STR R4, n8\r\n" + //
                "LDR R2, n8\r\n" + //
                "STR R2, m8\r\n" + //
                "LDR R3, m8\r\n" + //
                "LDR R3, n1\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVLT R3, #1\r\n" + //
                "MOVGE R3, #0\r\n" + //
                "STR R3, n9\r\n" + //
                "LDR R3, n9\r\n" + //
                "STR R3, n4\r\n" + //
                "B WHILECOND_6_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_6_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_6_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                "IFNEXT_2_SEQ_0_LEVEL_0: LDR R2, n1\r\n" + //
                "LDR R3, o0\r\n" + //
                "CMP R2, R3\r\n" + //
                "MOVLT R4, #1\r\n" + //
                "MOVGE R4, #0\r\n" + //
                "STR R4, o1\r\n" + //
                "LDR R2, o1\r\n" + //
                "LDR R2, u8\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ IFSTAT_2_SEQ_1_LEVEL_0\r\n" + //
                "BNE IFNEXT_2_SEQ_1_LEVEL_0\r\n" + //
                "IFSTAT_2_SEQ_1_LEVEL_0: LDR R2, m8\r\n" + //
                "LDR R2, n1\r\n" + //
                "CMP R2, R2\r\n" + //
                "MOVGT R2, #1\r\n" + //
                "MOVLE R2, #0\r\n" + //
                "STR R2, o2\r\n" + //
                "LDR R2, o2\r\n" + //
                "LDR R2, u9\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_8_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_8_SEQ_0_LEVEL_0: LDR R2, o2\r\n" + //
                "LDR R2, v0\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ WHILESTAT_8_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_8_LEVEL_0\r\n" + //
                "WHILESTAT_8_SEQ_0_LEVEL_0: LDR R2, m7\r\n" + //
                "LDR R2, n5\r\n" + //
                "ADD R13, R13, #16\r\n" + //
                "STR R2, [R13, #-4]\r\n" + //
                "STR R2, [R13, #-8]\r\n" + //
                "STR R14, [R13, #-16]\r\n" + //
                "BL Divide\r\n" + //
                "LDR R2, [R13, #-12]\r\n" + //
                "LDR R14, [R13, #-16]\r\n" + //
                "SUB R13, R13, #16\r\n" + //
                "STR R2, o3\r\n" + //
                "LDR R2, o3\r\n" + //
                "STR R2, m7\r\n" + //
                "LDR R2, m8\r\n" + //
                "LDR R3, o4\r\n" + //
                "SUB R4, R2, R3\r\n" + //
                "STR R4, o5\r\n" + //
                "LDR R2, o5\r\n" + //
                "STR R2, m8\r\n" + //
                "LDR R3, m8\r\n" + //
                "LDR R3, n1\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVGT R3, #1\r\n" + //
                "MOVLE R3, #0\r\n" + //
                "STR R3, o6\r\n" + //
                "LDR R3, o6\r\n" + //
                "STR R3, o2\r\n" + //
                "B WHILECOND_8_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_8_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_8_LEVEL_0: B IFEND_2_LEVEL_0\r\n" + //
                "IFNEXT_2_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "IFEND_2_LEVEL_0: LDR R2, m7\r\n" + //
                "LDR R3, c7\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "IntExp: LDR R3, o9\r\n" + //
                "STR R3, o7\r\n" + //
                "LDR R2, p0\r\n" + //
                "STR R2, o8\r\n" + //
                "LDR R3, c9\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, p1\r\n" + //
                "LDR R2, o8\r\n" + //
                "LDR R3, p1\r\n" + //
                "CMP R2, R3\r\n" + //
                "MOVLT R4, #1\r\n" + //
                "MOVGE R4, #0\r\n" + //
                "STR R4, p2\r\n" + //
                "LDR R4, p2\r\n" + //
                "LDR R5, v2\r\n" + //
                "TST R4, R5\r\n" + //
                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILENEXT_10_SEQ_0_LEVEL_0\r\n" + //
                "WHILECOND_10_SEQ_0_LEVEL_0: LDR R4, p2\r\n" + //
                "LDR R5, v3\r\n" + //
                "TST R4, R5\r\n" + //
                "BEQ WHILESTAT_10_SEQ_0_LEVEL_0\r\n" + //
                "BNE WHILEEND_10_LEVEL_0\r\n" + //
                "WHILESTAT_10_SEQ_0_LEVEL_0: LDR R2, c8\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, p3\r\n" + //
                "LDR R2, o7\r\n" + //
                "LDR R2, p3\r\n" + //
                "MUL R2, R2, R2\r\n" + //
                "STR R2, p4\r\n" + //
                "LDR R2, p4\r\n" + //
                "STR R2, o7\r\n" + //
                "LDR R2, o8\r\n" + //
                "LDR R3, p5\r\n" + //
                "ADD R4, R2, R3\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R4, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL IntToReal\r\n" + //
                "LDR R4, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R4, p6\r\n" + //
                "LDR R2, p6\r\n" + //
                "STR R2, o8\r\n" + //
                "LDR R3, o8\r\n" + //
                "LDR R3, p1\r\n" + //
                "CMP R3, R3\r\n" + //
                "MOVLT R3, #1\r\n" + //
                "MOVGE R3, #0\r\n" + //
                "STR R3, p7\r\n" + //
                "LDR R3, p7\r\n" + //
                "STR R3, p2\r\n" + //
                "B WHILECOND_10_SEQ_0_LEVEL_0\r\n" + //
                "WHILENEXT_10_SEQ_0_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "WHILEEND_10_LEVEL_0: LDR R2, o7\r\n" + //
                "LDR R3, d0\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Floor: LDR R3, d1\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, p8\r\n" + //
                "LDR R2, p8\r\n" + //
                "LDR R3, d2\r\n" + //
                "STR R2, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Round: LDR R3, d3\r\n" + //
                "LDR R3, [R13, -R3]\r\n" + //
                "STR R3, p9\r\n" + //
                "LDR R2, p9\r\n" + //
                "LDR R3, q0\r\n" + //
                "ADD R4, R2, R3\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R4, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL IntToReal\r\n" + //
                "LDR R4, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R4, q1\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "LDR R2, d1\r\n" + //
                "LDR R3, q1\r\n" + //
                "STR R3, [R13,-R2]\r\n" + //
                "BL Floor\r\n" + //
                "LDR R4, d2\r\n" + //
                "LDR R4, [R13, -R4]\r\n" + //
                "STR R4, q2\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "LDR R2, q2\r\n" + //
                "LDR R2, d4\r\n" + //
                "STR R2, [R13, -R2]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "Ceil: LDR R2, d5\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, q4\r\n" + //
                "LDR R2, q4\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R2, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL RNeg\r\n" + //
                "LDR R2, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R2, q5\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "LDR R3, d1\r\n" + //
                "LDR R4, q5\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "BL Floor\r\n" + //
                "LDR R2, d2\r\n" + //
                "LDR R2, [R13, -R2]\r\n" + //
                "STR R2, q6\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "LDR R2, q3\r\n" + //
                "LDR R3, q6\r\n" + //
                "MUL R4, R2, R3\r\n" + //
                "STR R4, q7\r\n" + //
                "LDR R2, q7\r\n" + //
                "ADD R13, R13, #12\r\n" + //
                "STR R2, [R13, #-4]\r\n" + //
                "STR R14, [R13, #-12]\r\n" + //
                "BL RNeg\r\n" + //
                "LDR R2, [R13, #-8]\r\n" + //
                "LDR R14, [R13, #-12]\r\n" + //
                "SUB R13, R13, #12\r\n" + //
                "STR R2, q8\r\n" + //
                "LDR R3, q8\r\n" + //
                "LDR R3, d6\r\n" + //
                "STR R3, [R13, -R3]\r\n" + //
                "LDR R4, [R13]\r\n" + //
                "SUB R13, R13, #2\r\n" + //
                "MOV R15, R14\r\n" + //
                "begin_2: LDR R3, q9\r\n" + //
                "STR R3, r0\r\n" + //
                "LDR R2, r1\r\n" + //
                "STR R2, r2\r\n" + //
                "B begin_3\r\n" + //
                "begin_3: LDR R3, r0\r\n" + //
                "LDR R3, w0\r\n" + //
                "TST R3, R3\r\n" + //
                "BEQ IFSTAT_3_SEQ_0_LEVEL_0\r\n" + //
                "BNE IFNEXT_3_SEQ_0_LEVEL_0\r\n" + //
                "IFSTAT_3_SEQ_0_LEVEL_0: ADD R13, R13, #8\r\n" + //
                "STR R14, [R13, #-8]\r\n" + //
                "LDR R3, a2\r\n" + //
                "LDR R4, r3\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "BL WriteInt\r\n" + //
                "LDR R14, [R13, #-8]\r\n" + //
                "SUB R13, R13, #8\r\n" + //
                "B IFEND_3_LEVEL_0\r\n" + //
                "IFNEXT_3_SEQ_0_LEVEL_0: ADD R13, R13, #8\r\n" + //
                "STR R14, [R13, #-8]\r\n" + //
                "LDR R2, a2\r\n" + //
                "LDR R3, r4\r\n" + //
                "STR R3, [R13,-R2]\r\n" + //
                "BL WriteInt\r\n" + //
                "LDR R14, [R13, #-8]\r\n" + //
                "SUB R13, R13, #8\r\n" + //
                "B IFEND_3_LEVEL_0\r\n" + //
                "IFNEXT_3_SEQ_1_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "IFEND_3_LEVEL_0: LDR R2, r2\r\n" + //
                "LDR R2, w3\r\n" + //
                "TST R2, R2\r\n" + //
                "BEQ IFSTAT_4_SEQ_0_LEVEL_0\r\n" + //
                "BNE IFNEXT_4_SEQ_0_LEVEL_0\r\n" + //
                "IFSTAT_4_SEQ_0_LEVEL_0: ADD R13, R13, #8\r\n" + //
                "STR R14, [R13, #-8]\r\n" + //
                "LDR R2, a2\r\n" + //
                "LDR R4, r5\r\n" + //
                "STR R4, [R13,-R2]\r\n" + //
                "BL WriteInt\r\n" + //
                "LDR R14, [R13, #-8]\r\n" + //
                "SUB R13, R13, #8\r\n" + //
                "B IFEND_4_LEVEL_0\r\n" + //
                "IFNEXT_4_SEQ_0_LEVEL_0: LDR R2, r0\r\n" + //
                "LDR R3, w5\r\n" + //
                "TST R2, R3\r\n" + //
                "BEQ IFSTAT_4_SEQ_1_LEVEL_0\r\n" + //
                "BNE IFNEXT_4_SEQ_1_LEVEL_0\r\n" + //
                "IFSTAT_4_SEQ_1_LEVEL_0: ADD R13, R13, #8\r\n" + //
                "STR R14, [R13, #-8]\r\n" + //
                "LDR R3, a2\r\n" + //
                "LDR R4, r6\r\n" + //
                "STR R4, [R13,-R3]\r\n" + //
                "BL WriteInt\r\n" + //
                "LDR R14, [R13, #-8]\r\n" + //
                "SUB R13, R13, #8\r\n" + //
                "B IFEND_4_LEVEL_0\r\n" + //
                "IFNEXT_4_SEQ_1_LEVEL_0: ADD R13, R13, #8\r\n" + //
                "STR R14, [R13, #-8]\r\n" + //
                "LDR R2, a2\r\n" + //
                "LDR R3, r7\r\n" + //
                "STR R3, [R13,-R2]\r\n" + //
                "BL WriteInt\r\n" + //
                "LDR R14, [R13, #-8]\r\n" + //
                "SUB R13, R13, #8\r\n" + //
                "B IFEND_4_LEVEL_0\r\n" + //
                "IFNEXT_4_SEQ_2_LEVEL_0: ADD R0, R0, #0\r\n" + //
                "IFEND_4_LEVEL_0: STP\r\n";
        testDeclanFile("test_source/IfStatementBasic.dcl", expectedResult);
    }

    @Test
    public void testLoops(){
        String expectedResult = "";
        testDeclanFile("test_source/loops.dcl", expectedResult);
    }

    @Test
    public void testRepeatLoopBasic(){
        String expectedResult = "";
        testDeclanFile("test_source/RepeatLoopBasic.dcl", expectedResult);
    }

    @Test
    public void testSample(){
        String expectedResult = "";
        testDeclanFile("test_source/sample.dcl", expectedResult);
    }

    @Test
    public void testTest(){
        String expectedResult = "";
        testDeclanFile("test_source/test.dcl", expectedResult);
    }

    @Test
    public void testTest2(){
        String expectedResult = "";
        testDeclanFile("test_source/test2.dcl", expectedResult);
    }

    @Test
    public void testTest3(){
        String expectedResult = "";
        testDeclanFile("test_source/test3.dcl", expectedResult);
    }

    @Test
    public void testTest4(){
        String expectedResult = "";
        testDeclanFile("test_source/test4.dcl", expectedResult);
    }

    @Test
    public void testWhileLoopAdvanced(){
        String expectedResult = "";
        testDeclanFile("test_source/WhileLoopAdvanced.dcl", expectedResult);
    }

    @Test
    public void testWhileLoopBasic(){
        String expectedResult = "";
        testDeclanFile("test_source/WhileLoopBasic.dcl", expectedResult);
    }
    */
}

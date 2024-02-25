package io.github.H20man13.DeClan;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;

import org.junit.Test;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.Source;
import io.github.H20man13.DeClan.common.ReaderSource;
import io.github.H20man13.DeClan.main.MyDeClanLexer;
import io.github.H20man13.DeClan.main.MyDeClanParser;

public class MyParserBasicTest {
    public void runParserOnSource(String sourcePath){
        File sourceFile = new File(sourcePath);
        assertTrue("Error file " + sourceFile.getAbsolutePath() + " doesnt exist!!", sourceFile.exists());

        
        try {
            FileReader reader = new FileReader(sourceFile);
            Source mySource = new ReaderSource(reader);
            ErrorLog errLog = new ErrorLog();
            MyDeClanLexer lexer = new MyDeClanLexer(mySource, errLog);
            MyDeClanParser parser = new MyDeClanParser(lexer, errLog);
            parser.parseProgram();
            parser.close();
        } catch(Exception exp){
            assertTrue(exp.toString(), false);
        }
    }

    @Test
    public void testConversions(){
        runParserOnSource("test/declan/conversions.dcl");
    }

    @Test
    public void testExpressions(){
        runParserOnSource("test/declan/expressions.dcl");
    }

    @Test
    public void testLoops(){
        runParserOnSource("test/declan/loops.dcl");
    }

    @Test
    public void testSample(){
        runParserOnSource("test/declan/sample.dcl");
    }

    @Test
    public void testTest(){
        runParserOnSource("test/declan/test.dcl");
    }

    @Test
    public void testTest2(){
        runParserOnSource("test/declan/test2.dcl");
    }

    @Test
    public void testTest3(){
        runParserOnSource("test/declan/test3.dcl");
    }

    @Test
    public void testTest4(){
        runParserOnSource("test/declan/test4.dcl");
    }

    @Test
    public void testSingleConversion(){
        runParserOnSource("test/declan/SingleConversion.dcl");
    }

    @Test
    public void testSingleConversion2(){
        runParserOnSource("test/declan/SingleConversion2.dcl");
    }

    @Test
    public void testRealAddition(){
        runParserOnSource("test/declan/RealAddition.dcl");
    }

    @Test
    public void testRealAddition2(){
        runParserOnSource("test/declan/RealAddition2.dcl");
    }

    @Test
    public void testRealAddition3(){
        runParserOnSource("test/declan/RealMultiplication.dcl");
    }

    @Test
    public void testRealMultiplication(){
        runParserOnSource("test/declan/RealMultiplication.dcl");
    }

    @Test
    public void testRealMultiplication2(){
        runParserOnSource("test/declan/RealMultiplication2.dcl");
    }

    @Test
    public void testIntegerDiv(){
        runParserOnSource("test/declan/IntegerDiv.dcl");
    }

    @Test
    public void testIntegerDiv2(){
        runParserOnSource("test/declan/IntegerDiv2.dcl");
    }

    @Test
    public void testRealDivision(){
        runParserOnSource("test/declan/RealDivision.dcl");
    }

    @Test
    public void testRealDivision2(){
        runParserOnSource("test/declan/RealDivision2.dcl");
    }
}

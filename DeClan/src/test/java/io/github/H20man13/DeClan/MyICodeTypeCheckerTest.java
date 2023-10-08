package io.github.H20man13.DeClan;

import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import edu.depauw.declan.common.ErrorLog;
import io.github.H20man13.DeClan.common.ReaderSource;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.symboltable.entry.TypeCheckerQualities;
import io.github.H20man13.DeClan.main.MyICodeTypeChecker;
import io.github.H20man13.DeClan.main.MyIrLexer;
import io.github.H20man13.DeClan.main.MyIrParser;

public class MyICodeTypeCheckerTest {
    private MyICodeTypeChecker runTypeCheckerOnSource(String source){
        StringReader stringReader = new StringReader(source);
        ErrorLog errLog = new ErrorLog();
        ReaderSource typeCheckerSource = new ReaderSource(stringReader);

        MyIrLexer lexer = new MyIrLexer(typeCheckerSource, errLog);
        MyIrParser parser = new MyIrParser(lexer, errLog);

        List<ICode> prog = parser.parseProgram();

        MyICodeTypeChecker typeChecker = new MyICodeTypeChecker(prog, errLog);
        typeChecker.runTypeChecker();

        return typeChecker;
    }

    private void assertTypeCheckerQualities(MyICodeTypeChecker typeChecker, String identifier, Integer typeCheckerQualities){
        boolean typeCheckerContainsQualities = typeChecker.identContainsQualities(identifier, typeCheckerQualities);
        TypeCheckerQualities checkQualities = new TypeCheckerQualities(typeCheckerQualities);
        assertTrue("Error " + identifier + " does not contain qualities " + checkQualities, typeCheckerContainsQualities);
    }

    @Test
    public void testProg1(){
        String source = "a := 456\n"
                      + "b := 48393\n"
                      + "c := 8.23"
                      + "d := a IADD b\n"
                      + "e := b ISUB a\n"
                      + "f := d IMOD e\n"
                      + "g := a IMUL b\n"
                      + "h := c RADD f\n"
                      + "i := h RSUB c\n"
                      + "j := b RMUL i\n"
                      + "k := b LOR j\n"
                      + "l := b IOR g\n"
                      + "m := l IDIV a\n"
                      + "n := m RDIVIDE c\n"
                      + "o := m LAND a\n"
                      + "p := a IAND a\n"
                      + "q := p ILSHIFT b\n"
                      + "r := b IRSHIFT f\n"
                      + "s := r LT c\n"
                      + "t := i GT g\n"
                      + "u := p LE j\n"
                      + "v := r GE n\n"
                      + "w := n NE n\n"
                      + "x := j EQ h\n"
                      + "END\n";
        MyICodeTypeChecker tC = runTypeCheckerOnSource(source);
        assertTypeCheckerQualities(tC, "a", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "b", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "c", TypeCheckerQualities.REAL);
        assertTypeCheckerQualities(tC, "d", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "e", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "f", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "g", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "h", TypeCheckerQualities.REAL);
        assertTypeCheckerQualities(tC, "i", TypeCheckerQualities.REAL);
        assertTypeCheckerQualities(tC, "j", TypeCheckerQualities.REAL);
        assertTypeCheckerQualities(tC, "k", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "l", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "m", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "n", TypeCheckerQualities.REAL);
        assertTypeCheckerQualities(tC, "o", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "p", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "q", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "r", TypeCheckerQualities.INTEGER);
        assertTypeCheckerQualities(tC, "s", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "t", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "u", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "v", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "w", TypeCheckerQualities.BOOLEAN);
        assertTypeCheckerQualities(tC, "x", TypeCheckerQualities.BOOLEAN);
    }
}

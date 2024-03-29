package io.github.H20man13.DeClan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.ErrorLog.LogItem;
import edu.depauw.declan.common.ast.Library;
import io.github.H20man13.DeClan.main.MyStandardLibrary;
import io.github.H20man13.DeClan.main.MyTypeChecker;

public class MyStandardLibTest {
    @Test
    public void parseMathLib(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.mathLibrary();

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }

    @Test
    public void parseIoLib(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.ioLibrary();

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }

    @Test
    public void parseRealLibrary(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.realLibrary();

        for(LogItem err: errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }

    @Test
    public void parseIntegerLibrary(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.intLibrary();

        for(LogItem err: errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }

    @Test
    public void parseConversionsLibrary(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.conversionsLibrary();

        for(LogItem err: errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }

    @Test
    public void parseUtilsLibrary(){
        ErrorLog errLog = new ErrorLog();
        MyStandardLibrary stdLib = new MyStandardLibrary(errLog);
        Library lib = stdLib.utilsLibrary();

        for(LogItem err: errLog){
            assertTrue(err.toString(), false);
        }

        MyTypeChecker typeChecker = new MyTypeChecker(errLog);
        lib.accept(typeChecker);

        for(LogItem err : errLog){
            assertTrue(err.toString(), false);
        }
    }
}

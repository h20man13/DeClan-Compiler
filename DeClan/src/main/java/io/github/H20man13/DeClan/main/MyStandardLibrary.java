package io.github.H20man13.DeClan.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.Position;
import edu.depauw.declan.common.Source;
import edu.depauw.declan.common.ast.Library;
import io.github.H20man13.DeClan.common.ElaborateReaderSource;
import io.github.H20man13.DeClan.common.ReaderSource;
import io.github.H20man13.DeClan.common.gen.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.icode.ICode;

public class MyStandardLibrary {
    private ErrorLog errLog;
    private String libDir;
    private boolean libDirFound;

    public MyStandardLibrary(ErrorLog errLog){
        this.errLog = errLog;
        String locLib = System.getenv("DECLIB");
        if(locLib == null){
            errLog.add("Error: Cannot find environment variable DECLIB", new Position(0, 0));
            libDirFound = false;
        } else {
            libDir = locLib;
            libDirFound = true;
        }
    }

    public Library mathLibrary(){
        if(libDirFound){
            String libSource = libDir + "/Math.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "Math.declib";
            return parseLibrarySource(libSource);
        }
    }

    public Library ioLibrary(){
        if(libDirFound){
            String libSource = libDir + "/Io.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "Io.declib";
            return parseLibrarySource(libSource);
        }
    }

    public Library realLibrary(){
        if(libDirFound){
            String libSource = libDir + "/RealOperations.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "RealOperations.declib";
            return parseLibrarySource(libSource);
        }
    }

    public Library intLibrary(){
        if(libDirFound){
            String libSource = libDir + "/IntOperations.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "IntOperations.declib";
            return parseLibrarySource(libSource);
        }
    }

    public Library conversionsLibrary(){
        if(libDirFound){
            String libSource = libDir + "/Conversions.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "Conversions.declib";
            return parseLibrarySource(libSource);
        }
    }

    public Library utilsLibrary(){
        if(libDirFound){
            String libSource = libDir + "/Utils.declib";
            return parseLibrarySource(libSource);
        } else {
            String libSource = "Utils.declib";
            return parseLibrarySource(libSource);
        }
    }

    private Library parseLibrarySource(String sourceName){
        try{
            File file = new File(sourceName);
            if(file.exists()){
                FileReader reader = new FileReader(file);
                Source readerSource = new ElaborateReaderSource(sourceName, reader);
                MyDeClanLexer lexer = new MyDeClanLexer(readerSource, errLog);
                MyDeClanParser parser = new MyDeClanParser(lexer, errLog);
                return parser.parseLibrary();
            } else {
                errLog.add("Could not create file reader with source name " + sourceName, new Position(0, 0));
                return null;
            }
        } catch(FileNotFoundException exp) {
            errLog.add("Could not create file reader with source name " + sourceName, new Position(0, 0));
            return null;
        }
    }

}



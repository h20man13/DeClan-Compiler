package io.github.H20man13.DeClan.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import io.github.H20man13.DeClan.common.ErrorLog;
import io.github.H20man13.DeClan.common.position.Position;
import io.github.H20man13.DeClan.common.ast.Library;
import io.github.H20man13.DeClan.common.icode.Lib;
import io.github.H20man13.DeClan.common.position.Position;
import io.github.H20man13.DeClan.common.source.ElaborateReaderSource;
import io.github.H20man13.DeClan.common.source.Source;

public class MyStandardLibrary {
    private ErrorLog errLog;
    private String irDir;
    private String declanDir;
    private boolean declanDirFound;
    private boolean irDirFound;

    public MyStandardLibrary(ErrorLog errLog){
        this.errLog = errLog;
        String locLib = System.getenv("DECLIB");
        if(locLib == null){
            errLog.add("Error: Cannot find environment variable DECLIB", new Position(0, 0));
            declanDirFound = false;
            irDirFound = false;
        } else {
            irDir = locLib + "\\ir\\linkable";
            declanDir = locLib + "\\declan";
            File irDirAsFile = new File(irDir);
            File declanDirAsFile = new File(declanDir);
            
            if(irDirAsFile.exists()){
                irDirFound = true;
            } else {
                irDirFound = false;
            }

            if(declanDirAsFile.exists()){
                declanDirFound = true;
            } else{
                declanDirFound = false;
            }
        }
    }

    private Library parseDeclanSource(String fileName){
        if(declanDirFound){
            String declanSrcFile = declanDir + '\\' + fileName + ".declib";
            File file = new File(declanSrcFile);
            if(!file.exists())
                throw new RuntimeException("Error file at path-\n" + declanSrcFile + "\ndoes not exist!!!\n");

            try{
                FileReader reader = new FileReader(declanSrcFile);
                Source source = new ElaborateReaderSource(fileName, reader);
                MyDeClanLexer lexer = new MyDeClanLexer(source, null, errLog);
                MyDeClanParser parser = new MyDeClanParser(lexer, errLog);
                Library lib = parser.parseLibrary();
                parser.close();
                return lib;
            } catch(Exception exp){
                throw new RuntimeException(exp.toString());
            }
        } else {
            throw new RuntimeException("Error declan library src directory -\n" + declanDir + "\n not found!!!");
        }
    }

    private Lib parseIrSource(String fileName){
        if(!irDirFound && !declanDirFound)
            throw new RuntimeException("No library paths found");

        if(irDirFound){
            String irFileString = irDir + '\\' + fileName + ".ilib";
            File irFile = new File(irFileString);
            if(irFile.exists()){
                try{
                    FileReader reader = new FileReader(irFile);
                    Source source = new ElaborateReaderSource(fileName, reader);
                    MyIrLexer lexer = new MyIrLexer(source, errLog);
                    MyIrParser parser = new MyIrParser(lexer, errLog);
                    Lib toRet = parser.parseLibrary();
                    parser.close();
                    return toRet;
                } catch(FileNotFoundException exp){
                    throw new RuntimeException();
                }
            }
        }

        if(declanDirFound){
            Library lib = parseDeclanSource(fileName);
            MyICodeGenerator gen = new MyICodeGenerator(null, errLog);
            return gen.generateLibraryIr(lib);
        } else {
            throw new RuntimeException("Error directory at path-\n" + declanDir + "\n not found!!!");
        }
    }

    public Lib irMathLibrary(){
        return parseIrSource("Math");
    }

    public Library declanMathLibrary(){
        return parseDeclanSource("Math");
    }

    public Lib irIoLibrary(){
        return parseIrSource("Io");
    }

    public Library declanIoLibrary(){
        return parseDeclanSource("Io");
    }

    public Lib irRealLibrary(){
        return parseIrSource("RealOperations");
    }

    public Library declanRealLibrary(){
        return parseDeclanSource("RealOperations");
    } 

    public Lib irIntLibrary(){
        return parseIrSource("IntOperations");
    }

    public Library declanIntLibrary(){
        return parseDeclanSource("IntOperations");
    }

    public Lib irConversionsLibrary(){
        return parseIrSource("Conversions");
    }

    public Library declanConversionsLibrary(){
        return parseDeclanSource("Conversions");
    } 

    public Lib irUtilsLibrary(){
        return parseIrSource("Utils");
    }

    public Library declanUtilsLibrary(){
        return parseDeclanSource("Utils");
    }
}



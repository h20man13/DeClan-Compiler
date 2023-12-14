package io.github.H20man13.DeClan.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.Position;
import edu.depauw.declan.common.Source;
import edu.depauw.declan.common.ast.Library;
import edu.depauw.declan.common.ast.Program;
import edu.depauw.declan.model.SymbolTable;
import io.github.H20man13.DeClan.common.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.ReaderSource;
import io.github.H20man13.DeClan.common.Tuple;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.If;
import io.github.H20man13.DeClan.common.icode.Inline;
import io.github.H20man13.DeClan.common.icode.Lib;
import io.github.H20man13.DeClan.common.icode.Prog;
import io.github.H20man13.DeClan.common.icode.SymEntry;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.BoolExp;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;
import io.github.H20man13.DeClan.common.icode.label.ProcLabel;
import io.github.H20man13.DeClan.common.icode.procedure.Call;
import io.github.H20man13.DeClan.common.icode.procedure.ExternalCall;
import io.github.H20man13.DeClan.common.icode.procedure.ExternalPlace;
import io.github.H20man13.DeClan.common.icode.procedure.InternalPlace;
import io.github.H20man13.DeClan.common.icode.procedure.ParamAssign;
import io.github.H20man13.DeClan.common.icode.procedure.Proc;
import io.github.H20man13.DeClan.common.icode.section.CodeSec;
import io.github.H20man13.DeClan.common.icode.section.DataSec;
import io.github.H20man13.DeClan.common.icode.section.ProcSec;
import io.github.H20man13.DeClan.common.icode.section.SymSec;
import io.github.H20man13.DeClan.common.util.Utils;

public class MyIrLinker {
    private IrRegisterGenerator gen;
    private ErrorLog errLog;

    public MyIrLinker(ErrorLog errLog){
        this.errLog = errLog;
        this.gen = new IrRegisterGenerator();
    }

    private static Prog generateProgram(ErrorLog errorLog, Program prog){
        IrRegisterGenerator gen = new IrRegisterGenerator();
        MyICodeGenerator iGen = new MyICodeGenerator(errorLog, gen);
        return iGen.generateProgramIr(prog);
    }

    private static Lib[] generateLibraries(ErrorLog errLog, Library... libs){
        int size = libs.length;
        Lib[] toRet = new Lib[size];
        for(int i = 0; i < size; i++){
            toRet[i]  = generateLibrary(errLog, libs[i]);
        }
        return toRet;
    }

    private static Lib generateLibrary(ErrorLog errLog, Library lib){
        IrRegisterGenerator gen = new IrRegisterGenerator();
        MyICodeGenerator iGen = new MyICodeGenerator(errLog, gen);
        return iGen.generateLibraryIr(lib);
    }

    private void fetchExternalDependentInstructions(String identName, Prog program, Lib[] libraries, SymSec newTable, DataSec dataInstructions, CodeSec codeSec, ProcSec procSec, Lib... libsToIgnore){
        loop: for(int libIndex = 0; libIndex < libraries.length; libIndex++){
            Lib library = libraries[libIndex];
            if(!Utils.arrayContainsValue(library, libsToIgnore)){
                SymSec libSymbols = library.symbols;
                if(libSymbols.containsEntryWithIdentifier(identName, SymEntry.INTERNAL)){
                    SymEntry libEntry = libSymbols.getEntryByIdentifier(identName, SymEntry.INTERNAL);
                    DataSec libData = library.variables;
                    for(int z = 0; z <= libData.getLength(); z++){
                        Assign assignLib = libData.getInstruction(z);
                        if(assignLib.place.equals(libEntry.icodePlace)){
                            Exp exp = assignLib.value;
                            if(exp instanceof IdentExp){
                                IdentExp identExp = (IdentExp)exp;
                                if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                                    SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, library);
                                    if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                        if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, program, libraries, identExp.ident, newTable, dataInstructions, procSec);
                                }
                            } else if(exp instanceof UnExp){
                                UnExp unary = (UnExp)exp;
                                if(unary.right instanceof IdentExp){
                                    IdentExp identExp = (IdentExp)unary.right;
                                    if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, program, libraries, identExp.ident, newTable, dataInstructions, procSec);
                                    }
                                }
                            } else if(exp instanceof BinExp){
                                BinExp binary = (BinExp)exp;

                                if(binary.left instanceof IdentExp){
                                    IdentExp leftIdent = (IdentExp)binary.left;
                                    if(libSymbols.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, program, libraries, leftIdent.ident, newTable, dataInstructions, procSec);
                                    }
                                }

                                if(binary.right instanceof IdentExp){
                                    IdentExp rightIdent = (IdentExp)binary.right;
                                    if(libSymbols.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, program, libraries, rightIdent.ident, newTable, dataInstructions, procSec);
                                    }
                                }
                            }

                            if(!placeIsUniqueToProgramOrLibrary(assignLib.place, program, libraries, library)){
                                String place = null;    
                                do{
                                    place = gen.genNextRegister();
                                } while(!placeIsUniqueToProgramOrLibrary(place, program, libraries, library));

                                replacePlaceInLib(library, assignLib.place, place);
                            }

                            if(!instructionExistsInNewProgram(assignLib, dataInstructions)){
                                dataInstructions.addInstruction(assignLib);
                                newTable.addEntry(libEntry);
                            }
                            break loop;
                        }
                    }
                }
            }
        }
    }

    private void fetchExternalDependentInstructions(String identName, Lib single, Lib[] libraries, SymSec newTable, DataSec dataInstructions, ProcSec procSec, Lib... libsToIgnore){
        loop: for(int libIndex = 0; libIndex < libraries.length; libIndex++){
            Lib library = libraries[libIndex];
            if(!Utils.arrayContainsValue(library, libsToIgnore)){
                SymSec libSymbols = library.symbols;
                if(libSymbols.containsEntryWithIdentifier(identName, SymEntry.INTERNAL)){
                    SymEntry libEntry = libSymbols.getEntryByIdentifier(identName, SymEntry.INTERNAL);
                    DataSec libData = library.variables;
                    List<Assign> libICode = libData.intermediateCode;
                    for(int z = 0; z <= libICode.size(); z++){
                        Assign assignLib = libICode.get(z);
                        if(assignLib.place.equals(libEntry.icodePlace)){
                            Exp exp = assignLib.value;
                            if(exp instanceof IdentExp){
                                IdentExp identExp = (IdentExp)exp;
                                if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                                    SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, library);
                                    if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                        if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, single, libraries, identExp.ident, newTable, dataInstructions, procSec);
                                }
                            } else if(exp instanceof UnExp){
                                UnExp unary = (UnExp)exp;
                                if(unary.right instanceof IdentExp){
                                    IdentExp identExp = (IdentExp)unary.right;
                                    if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, single, libraries, identExp.ident, newTable, dataInstructions, procSec);
                                    }
                                }
                            } else if(exp instanceof BinExp){
                                BinExp binary = (BinExp)exp;

                                if(binary.left instanceof IdentExp){
                                    IdentExp leftIdent = (IdentExp)binary.left;
                                    if(libSymbols.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, single, libraries, leftIdent.ident, newTable, dataInstructions, procSec);
                                    }
                                }

                                if(binary.right instanceof IdentExp){
                                    IdentExp rightIdent = (IdentExp)binary.right;
                                    if(libSymbols.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                                        SymEntry symEntry = libSymbols.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, library);
                                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, symEntry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, single, libraries, rightIdent.ident, newTable, dataInstructions, procSec);
                                    }
                                }
                            }

                            if(!placeIsUniqueToLibrary(assignLib.place, single, libraries, library)){
                                String place = null;    
                                do{
                                    place = gen.genNextRegister();
                                } while(!placeIsUniqueToLibrary(place, single, libraries, library));

                                replacePlaceInLib(library, assignLib.place, place);
                            }

                            if(!instructionExistsInNewProgram(assignLib, dataInstructions)){
                                dataInstructions.addInstruction(assignLib);
                                newTable.addEntry(libEntry);
                            }
                            break loop;
                        }
                    }
                }
            }
        }
    }

    private void fetchInternalDependentInstructions(Lib currentLib, Lib single, Lib[] libraries, String labelName, SymSec newTable, DataSec dataInstructions, ProcSec procSec){
        DataSec data = currentLib.variables;
        SymSec libSymbols = currentLib.symbols;
        for(int i = 0; i < data.intermediateCode.size(); i++){
            Assign assign = (Assign)data.intermediateCode.get(i);
            if(assign.place.equals(labelName)){
                Exp exp = assign.value;
                if(exp instanceof IdentExp){
                    IdentExp identExp = (IdentExp)exp;
                    if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                        SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, currentLib);
                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                        }
                    } else {
                        fetchInternalDependentInstructions(currentLib, single, libraries, identExp.ident, newTable, dataInstructions, procSec);
                    }
                } else if(exp instanceof UnExp){
                    UnExp unary = (UnExp)exp;
                    if(unary.right instanceof IdentExp){
                        IdentExp identExp = (IdentExp)unary.right;
                        if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, single, libraries, identExp.ident, newTable, dataInstructions, procSec);
                        }
                    }
                } else if(exp instanceof BinExp){
                    BinExp binary = (BinExp)exp;

                    if(binary.left instanceof IdentExp){
                        IdentExp leftIdent = (IdentExp)binary.left;
                        if(libSymbols.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, single, libraries, leftIdent.ident, newTable, dataInstructions, procSec);
                        }
                    }

                    if(binary.right instanceof IdentExp){
                        IdentExp rightIdent = (IdentExp)binary.right;
                        if(libSymbols.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, single, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, single, libraries, rightIdent.ident, newTable, dataInstructions, procSec);
                        }
                    }
                }
                
                if(!placeIsUniqueToLibrary(assign.place, single, libraries, currentLib)){
                    String place = null;    
                    do{
                        place = gen.genNextRegister();
                    } while(!placeIsUniqueToLibrary(place, single, libraries, currentLib));

                    replacePlaceInLib(currentLib, assign.place, place);
                }

                if(!instructionExistsInNewProgram(assign, dataInstructions)){
                    dataInstructions.addInstruction(assign);
                    if(libSymbols.containsEntryWithICodePlace(assign.place, SymEntry.INTERNAL)){
                        SymEntry entry = libSymbols.getEntryByICodePlace(assign.place, SymEntry.INTERNAL);
                        newTable.addEntry(entry);
                    }
                }
                
                break;
            }
        }
    }

     private void fetchInternalDependentInstructions(Lib currentLib, Prog program, Lib[] libraries, String labelName, SymSec newTable, DataSec dataInstructions, CodeSec codeSec,  ProcSec procSec){
        DataSec data = currentLib.variables;
        SymSec libSymbols = currentLib.symbols;
        for(int i = 0; i < data.intermediateCode.size(); i++){
            Assign assign = (Assign)data.intermediateCode.get(i);
            if(assign.place.equals(labelName)){
                Exp exp = assign.value;
                if(exp instanceof IdentExp){
                    IdentExp identExp = (IdentExp)exp;
                    if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                        SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, currentLib);
                        if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                            if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                        }
                    } else {
                        fetchInternalDependentInstructions(currentLib, program, libraries, identExp.ident, newTable, dataInstructions, procSec);
                    }
                } else if(exp instanceof UnExp){
                    UnExp unary = (UnExp)exp;
                    if(unary.right instanceof IdentExp){
                        IdentExp identExp = (IdentExp)unary.right;
                        if(libSymbols.containsEntryWithICodePlace(identExp.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(identExp.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, program, libraries, identExp.ident, newTable, dataInstructions, procSec);
                        }
                    }
                } else if(exp instanceof BinExp){
                    BinExp binary = (BinExp)exp;

                    if(binary.left instanceof IdentExp){
                        IdentExp leftIdent = (IdentExp)binary.left;
                        if(libSymbols.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, program, libraries, leftIdent.ident, newTable, dataInstructions, procSec);
                        }
                    }

                    if(binary.right instanceof IdentExp){
                        IdentExp rightIdent = (IdentExp)binary.right;
                        if(libSymbols.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                            SymEntry symEntry = libSymbols.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(symEntry.declanIdent, program, libraries, newTable, dataInstructions, procSec, currentLib);
                            if(newTable.containsEntryWithIdentifier(symEntry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = newTable.getEntryByIdentifier(symEntry.declanIdent, SymEntry.INTERNAL);
                                if(!symEntry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(currentLib, symEntry.icodePlace, newEntry.icodePlace);
                            }
                        } else {
                            fetchInternalDependentInstructions(currentLib, program, libraries, rightIdent.ident, newTable, dataInstructions, procSec);
                        }
                    }
                }
                
                if(!placeIsUniqueToProgramOrLibrary(assign.place, program, libraries, currentLib)){
                    String place = null;    
                    do{
                        place = gen.genNextRegister();
                    } while(!placeIsUniqueToProgramOrLibrary(place, program, libraries, currentLib));

                    replacePlaceInLib(currentLib, assign.place, place);
                }

                if(!instructionExistsInNewProgram(assign, dataInstructions)){
                    dataInstructions.addInstruction(assign);
                    if(libSymbols.containsEntryWithICodePlace(assign.place, SymEntry.INTERNAL)){
                        SymEntry entry = libSymbols.getEntryByICodePlace(assign.place, SymEntry.INTERNAL);
                        newTable.addEntry(entry);
                    }
                }
                
                break;
            }
        }
    }

    private void fetchExternalProcedure(String procName, Prog prog, Lib[] libraries, SymSec symbolTable, DataSec dataSection, CodeSec codeSection, ProcSec procedureSec, Lib... libsToIgnore){
        ProcLabel newProcLabel = new ProcLabel(procName);
        Proc newProcedure = new Proc(newProcLabel);
        loop: for(int i = 0; i < libraries.length; i++){
            Lib library = libraries[i];
            if(!Utils.arrayContainsValue(library, libsToIgnore)){
                ProcSec libProcSec = library.procedures;
                SymSec libSymbols = library.symbols;
                if(libProcSec.containsProcedure(procName)){
                    Proc procedure = libProcSec.getProcedureByName(procName);

                    for(int assignIndex = 0; assignIndex < procedure.paramAssign.size(); assignIndex++){
                        ParamAssign assign = procedure.paramAssign.get(assignIndex);

                        if(!placeIsUniqueToProgramOrLibrary(assign.paramPlace, prog, libraries, library)){
                            String place = null;
                            do{
                                place = gen.genNextRegister();
                            }while(!placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));

                            replacePlaceInLib(library, assign.paramPlace, place);
                        }

                        if(!placeIsUniqueToProgramOrLibrary(assign.newPlace, prog, libraries, library)){
                            String place = null;
                            do{
                                place = gen.genNextRegister();
                            } while(!placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));

                            replacePlaceInLib(library, assign.newPlace, place);
                        }

                        newProcedure.addParamater(assign);
                    }

                    for(int instructionIndex = 0; instructionIndex < procedure.instructions.size(); instructionIndex++){
                        ICode icode = procedure.instructions.get(instructionIndex);

                        if(icode instanceof Assign){
                            Assign assignment = (Assign)icode;
                            
                            if(!placeIsUniqueToProgramOrLibrary(assignment.place, prog, libraries, library)){
                                String newPlace = null;
                                do{
                                    newPlace = gen.genNextRegister();
                                } while(!placeIsUniqueToProgramOrLibrary(newPlace, prog, libraries, library));
                                replacePlaceInLib(library, assignment.place, newPlace);
                            }

                            Exp assignExp = assignment.value;
                            if(assignExp instanceof IdentExp){
                                IdentExp ident = (IdentExp)assignExp;
                                if(libSymbols.containsEntryWithICodePlace(ident.ident, SymEntry.EXTERNAL)){
                                    SymEntry entry = libSymbols.getEntryByICodePlace(ident.ident, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, prog, libraries, ident.ident, symbolTable, dataSection, codeSection, procedureSec);
                                }
                            } else if(assignExp instanceof UnExp){
                                UnExp unExp = (UnExp)assignExp;
                                
                                if(unExp.right instanceof IdentExp){
                                    IdentExp ident = (IdentExp)unExp.right;
                                    if(libSymbols.containsEntryWithICodePlace(ident.ident, SymEntry.EXTERNAL)){
                                        SymEntry entry = libSymbols.getEntryByICodePlace(ident.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, prog, libraries, ident.ident, symbolTable, dataSection, codeSection, procedureSec);
                                    }
                                }
                            } else if(assignExp instanceof BinExp){
                                BinExp binExp = (BinExp)assignExp;

                                if(binExp.left instanceof IdentExp){
                                    IdentExp leftExp = (IdentExp)binExp.left;
                                    if(libSymbols.containsEntryWithICodePlace(leftExp.ident, SymEntry.EXTERNAL)){
                                        SymEntry entry = libSymbols.getEntryByICodePlace(leftExp.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, prog, libraries, leftExp.ident, symbolTable, dataSection, codeSection, procedureSec);
                                    }
                                }

                                if(binExp.right instanceof IdentExp){
                                    IdentExp rightExp = (IdentExp)binExp.right;
                                    if(libSymbols.containsEntryWithICodePlace(rightExp.ident, SymEntry.EXTERNAL)){
                                        SymEntry entry = libSymbols.getEntryByICodePlace(rightExp.ident, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, prog, libraries, rightExp.ident, symbolTable, dataSection, codeSection, procedureSec);
                                    }
                                }
                            }
                        } else if(icode instanceof If){
                            If ifStat = (If)icode;

                            BinExp exp = ifStat.exp;
                            if(exp.left instanceof IdentExp){
                                IdentExp leftExp = (IdentExp)exp.left;
                                if(libSymbols.containsEntryWithICodePlace(leftExp.ident, SymEntry.EXTERNAL)){
                                    SymEntry entry = libSymbols.getEntryByICodePlace(leftExp.ident, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, prog, libraries, leftExp.ident, symbolTable, dataSection, codeSection, procedureSec);
                                }
                            }

                            if(exp.right instanceof IdentExp){
                                IdentExp rightExp = (IdentExp)exp.right;
                                if(libSymbols.containsEntryWithICodePlace(rightExp.ident, SymEntry.EXTERNAL)){
                                    SymEntry entry = libSymbols.getEntryByICodePlace(rightExp.ident, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, prog, libraries, rightExp.ident, symbolTable, dataSection, codeSection, procedureSec);
                                }
                            }
                        } else if(icode instanceof ExternalCall){
                            ExternalCall call = (ExternalCall)icode;
                            
                            if(!procedureSec.containsProcedure(call.procedureName))
                                fetchExternalProcedure(call.procedureName, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                            Proc fetchedProcedure = procedureSec.getProcedureByName(call.procedureName);

                            int numberOfArgsInProc = fetchedProcedure.paramAssign.size();
                            int numberOfArgsInCall = call.arguments.size();

                            if(numberOfArgsInCall != numberOfArgsInProc){
                                errLog.add("In call " + call.toString() + " expected " + numberOfArgsInCall + " but found procedure with " + numberOfArgsInProc + " arguments", new Position(i, 0));
                            } else if(fetchedProcedure.placement == null && call.toRet != null){
                                errLog.add("In call " + call.toString() + " function found does not have a return value and is VOID", new Position(i, 0));
                            } else {
                                List<Tuple<String, String>> newArgs = new LinkedList<Tuple<String, String>>();
                                for(int argIndex = 0; argIndex < numberOfArgsInCall; argIndex++){
                                    String place = call.arguments.get(argIndex);
                                    Tuple<String, String> newArg = new Tuple<String,String>("", "");
                                    
                                    if(libSymbols.containsEntryWithICodePlace(place, SymEntry.EXTERNAL)){
                                        SymEntry entry = libSymbols.getEntryByICodePlace(place, SymEntry.EXTERNAL);
                                        fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                                replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                        }
                                    } else {
                                        fetchInternalDependentInstructions(library, prog, libraries, place, symbolTable, dataSection, codeSection, procedureSec);
                                    }

                                    newArg.source = place;
                                    newArg.dest = fetchedProcedure.paramAssign.get(argIndex).paramPlace;
                                    newArgs.add(newArg);
                                }
                                
                                newProcedure.addInstruction(new Call(call.procedureName, newArgs));

                                if(call.toRet != null){
                                    String toRetFrom = fetchedProcedure.placement.place;
                                    String toRetTo = call.toRet;
                                    newProcedure.addInstruction(new ExternalPlace(toRetTo, toRetFrom));
                                }

                                continue;
                            }
                        } else if(icode instanceof Call){
                            Call call = (Call)icode;
                            for(Tuple<String, String> arg : call.params){
                                String place = arg.source;

                                if(libSymbols.containsEntryWithICodePlace(place, SymEntry.EXTERNAL)){
                                    SymEntry entry = libSymbols.getEntryByICodePlace(place, SymEntry.EXTERNAL);
                                    fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                                            replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                    }
                                } else {
                                    fetchInternalDependentInstructions(library, prog, libraries, place, symbolTable, dataSection, codeSection, procedureSec);
                                }
                            }

                            if(!procedureSec.containsProcedure(call.pname)){
                                Proc procedureEntry = libProcSec.getProcedureByName(call.pname);
                                procedureSec.addProcedure(procedureEntry);
                            }
                        } else if(icode instanceof ExternalPlace){
                            ExternalPlace placement = (ExternalPlace)icode;

                            if(!placeIsUniqueToProgramOrLibrary(placement.place, prog, libraries, library)){
                                String place = null;
                                do{
                                    place = gen.genNextRegister();
                                }while(!placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));
                                
                                replacePlaceInLib(library, placement.place, place);
                            }

                            if(placeIsUniqueToProgramOrLibrary(placement.retPlace, prog, libraries, library)){
                                String place = null;
                                do{
                                    place = gen.genNextRegister();
                                } while(placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));

                                replacePlaceInLib(library, placement.retPlace, place);
                            }
                            
                            if(libSymbols.containsEntryWithICodePlace(placement.retPlace, SymEntry.EXTERNAL)){
                                SymEntry entry = libSymbols.getEntryByICodePlace(placement.retPlace, SymEntry.EXTERNAL);
                                fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                                if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                    SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                    if(!entry.icodePlace.equals(newEntry.icodePlace))
                                        replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                                }
                            } else {
                                fetchInternalDependentInstructions(library, prog, libraries, placement.retPlace, symbolTable, dataSection, codeSection, procedureSec);
                            }
                        }
                        newProcedure.addInstruction(icode);
                    }

                    if(procedure.placement != null){
                        InternalPlace placement = procedure.placement;
                        if(!placeIsUniqueToProgramOrLibrary(placement.retPlace, prog, libraries, library)){
                            String place = null;
                            do{
                                place = gen.genNextRegister();
                            } while(!placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));

                            replacePlaceInLib(library, placement.retPlace, place);
                        }

                        if(!placeIsUniqueToProgramOrLibrary(placement.place, prog, libraries, library)){
                            String place = null;
                            do{
                                place = gen.genNextRegister();
                            } while(!placeIsUniqueToProgramOrLibrary(place, prog, libraries, library));

                            replacePlaceInLib(library, placement.place, place);
                        }

                        if(libSymbols.containsEntryWithICodePlace(placement.retPlace, SymEntry.EXTERNAL)){
                            SymEntry entry = libSymbols.getEntryByICodePlace(procedure.placement.retPlace, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(entry.declanIdent, prog, libraries, symbolTable, dataSection, codeSection, procedureSec, library);
                            if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                if(!entry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInLib(library, entry.icodePlace, newEntry.icodePlace);
                            }
                        }

                        newProcedure.placement = placement;
                    }

                    procedureSec.addProcedure(newProcedure);
                    break loop;
                }
            }
        }
    }

    private static void replacePlaceInICode(ICode icode, String oldPlace, String newPlace){
        if(icode instanceof Assign){
            Assign icodeAssign = (Assign)icode;

            if(icodeAssign.place.equals(oldPlace))
                icodeAssign.place = newPlace;

            Exp exp = icodeAssign.value;
            if(exp instanceof BinExp){
                BinExp binExp = (BinExp)exp;

                if(binExp.left instanceof IdentExp){
                    IdentExp leftExp = (IdentExp)binExp.left;

                    if(leftExp.ident.equals(oldPlace))
                        leftExp.ident = newPlace;
                }

                if(binExp.right instanceof IdentExp){
                    IdentExp rightExp = (IdentExp)binExp.right;

                    if(rightExp.ident.equals(oldPlace))
                        rightExp.ident = newPlace;
                }
            } else if(exp instanceof UnExp){
                UnExp unExp = (UnExp)exp;

                if(unExp.right instanceof IdentExp){
                    IdentExp rightExp = (IdentExp)unExp.right;

                    if(rightExp.ident.equals(oldPlace))
                        rightExp.ident = newPlace;
                }
            } else if(exp instanceof IdentExp){
                IdentExp iExp = (IdentExp)exp;

                if(iExp.ident.equals(oldPlace))
                    iExp.ident = newPlace;
            }
        } else if(icode instanceof If){
            If ifICode = (If)icode;

            BinExp exp = ifICode.exp;
            if(exp.left instanceof IdentExp){
                IdentExp leftIdent = (IdentExp)exp.left;
                
                if(leftIdent.ident.equals(oldPlace))
                    leftIdent.ident = newPlace;
            }

            if(exp.right instanceof IdentExp){
                IdentExp rightIdent = (IdentExp)exp.right;

                if(rightIdent.ident.equals(oldPlace))
                    rightIdent.ident = newPlace;
            }
        } else if(icode instanceof Inline){
            Inline inlineICode = (Inline)icode;

            List<String> newParam = new ArrayList<>();
            for(String param : inlineICode.param){
                if(param.equals(oldPlace))
                    newParam.add(newPlace);
                else
                    newParam.add(param);
            }
            
            inlineICode.param = newParam;
        } else if(icode instanceof ExternalPlace){
            ExternalPlace place = (ExternalPlace)icode;

            if(place.place.equals(oldPlace))
                place.place = newPlace;

            if(place.retPlace.equals(oldPlace))
                place.retPlace = newPlace;
        } else if(icode instanceof InternalPlace){
            InternalPlace place = (InternalPlace)icode;

            if(place.place.equals(oldPlace))
                place.place = newPlace;

            if(place.retPlace.equals(oldPlace))
                place.retPlace = newPlace;
        } else if(icode instanceof ParamAssign){
            ParamAssign assign = (ParamAssign)icode;
            if(assign.newPlace.equals(oldPlace))
                assign.newPlace = newPlace;

            if(assign.paramPlace.equals(oldPlace))
                assign.paramPlace = newPlace;
        } else if(icode instanceof ExternalCall){
            ExternalCall call = (ExternalCall)icode;

            List<String> newArgs = new LinkedList<String>();
            for(String arg : call.arguments){
                if(arg.equals(oldPlace))
                    newArgs.add(newPlace);
                else
                    newArgs.add(arg);
            }

            if(call.toRet != null){
                if(call.toRet.equals(oldPlace))
                    call.toRet = newPlace;
            }

            call.arguments = newArgs;
        } else if(icode instanceof Call){
            Call call = (Call)icode;
            
            List<Tuple<String, String>> newArgs = new LinkedList<Tuple<String, String>>();
            for(Tuple<String, String> arg : call.params){
                Tuple<String, String> newArg = new Tuple<String,String>("", "");
                if(arg.dest.equals(oldPlace))
                    newArg.dest = newPlace;
                else
                    newArg.dest = arg.dest;

                if(arg.source.equals(oldPlace))
                    newArg.source = newPlace;
                else
                    newArg.source = arg.source;
                newArgs.add(newArg);
            }

            call.params = newArgs;
        }
    }

    private static void replacePlaceInLib(Lib library, String oldPlace, String newPlace){
        SymSec symbols = library.symbols;
        for(SymEntry entry : symbols.entries)
            if(entry.icodePlace.equals(oldPlace))
                entry.icodePlace = newPlace;
        
        DataSec dataSection = library.variables;
        for(ICode icode : dataSection.intermediateCode)
            replacePlaceInICode(icode, oldPlace, newPlace);

        ProcSec procSection = library.procedures;
        for(Proc proc : procSection.procedures){
            for(ParamAssign assign : proc.paramAssign)
                replacePlaceInICode(assign, oldPlace, newPlace);
            for(ICode icode : proc.instructions)
                replacePlaceInICode(icode, oldPlace, newPlace);
            if(proc.placement != null)
                replacePlaceInICode(proc.placement, oldPlace, newPlace);
        }
    }

    private static void replacePlaceInProgram(Prog program, String oldPlace, String newPlace){
        replacePlaceInLib(program, oldPlace, newPlace);

        CodeSec cSec = program.code;
        for(int i = 0; i < cSec.getLength(); i++){
            ICode instruction = cSec.getInstruction(i);
            replacePlaceInICode(instruction, oldPlace, newPlace);
        }
    }

    private static boolean placeExistsInICode(String place, ICode dataCode){
        if(dataCode instanceof Assign){
            Assign assign = (Assign)dataCode;
            
            if(assign.place.equals(place))
                return true;

            Exp value = assign.value;
            if(value instanceof IdentExp){
                IdentExp identVal = (IdentExp)value;
                if(identVal.ident.equals(place))
                    return true;
            } else if(value instanceof UnExp){
                UnExp unaryVal = (UnExp)value;
                if(unaryVal.right instanceof IdentExp){
                    IdentExp rightVal = (IdentExp)unaryVal.right;
                    if(rightVal.ident.equals(place))
                        return true;
                }
            } else if(value instanceof BinExp){
                BinExp binVal = (BinExp)value;
                
                if(binVal.left instanceof IdentExp){
                    IdentExp leftVal = (IdentExp)binVal.left;
                    if(leftVal.ident.equals(place))
                        return true;
                }

                if(binVal.right instanceof IdentExp){
                    IdentExp rightVal = (IdentExp)binVal.right;
                    if(rightVal.ident.equals(place))
                        return true;
                }
            }
        } else if(dataCode instanceof If){
            If ifStat = (If)dataCode;
            BinExp expression = ifStat.exp;

            if(expression.left instanceof IdentExp){
                IdentExp leftIdent = (IdentExp)expression.left;
                if(leftIdent.ident.equals(place))
                    return true;
            }

            if(expression.right instanceof IdentExp){
                IdentExp rightIdent = (IdentExp)expression.right;
                if(rightIdent.ident.equals(place))
                    return true;
            }
        } else if(dataCode instanceof ExternalPlace){
            ExternalPlace placement = (ExternalPlace)dataCode;
            if(placement.place.equals(place))
                return true;

            if(placement.retPlace.equals(place))
                return true;
        } else if(dataCode instanceof InternalPlace){
            InternalPlace placement = (InternalPlace)dataCode;
            if(placement.place.equals(place))
                return true;
            
            if(placement.retPlace.equals(place))
                return true;
        } else if(dataCode instanceof Inline){
            Inline inlineAssembly = (Inline)dataCode;
            for(String arg : inlineAssembly.param){
                if(arg.equals(place))
                    return true;
            }
        } else if(dataCode instanceof ExternalCall){
            ExternalCall call = (ExternalCall)dataCode;
            for(String arg: call.arguments){
                if(arg.equals(place))
                    return true;
            }
        } else if(dataCode instanceof Call){
            Call call = (Call)dataCode;
            for(Tuple<String, String> arg : call.params){
                if(arg.source.equals(place))
                    return true;

                if(arg.dest.equals(place))
                    return true;
            }
        } else if(dataCode instanceof ParamAssign){
            ParamAssign assign = (ParamAssign)dataCode;
            if(assign.newPlace.equals(place))
                return true;

            if(assign.paramPlace.equals(place))
                return true;
        } else {
            return false;
        }
        return false;
    }

    private static boolean placeExistsInProgram(String place, Prog program){
        if(placeExistsInLibrary(place, program))
            return true;

        CodeSec codeSec = program.code;
        for(int i = 0; i < codeSec.getLength(); i++){
            ICode instr = codeSec.getInstruction(i);
            if(placeExistsInICode(place, instr))
                return true;
        }

        return false;
    }

    private static boolean placeExistsInLibrary(String place, Lib lib){
        SymSec symbols = lib.symbols;
        for(int i = 0; i < symbols.getLength(); i++){
            SymEntry entry = symbols.getEntryByIndex(i);
            if(entry.icodePlace.equals(place))
                return true;
        }

        DataSec dataCodes = lib.variables;
        for(int i = 0; i < dataCodes.getLength(); i++){
            ICode dataCode = dataCodes.getInstruction(i);
            if(placeExistsInICode(place, dataCode))
                return true;
        }

        ProcSec procedures = lib.procedures;
        for(int i = 0; i < procedures.getLength(); i++){
            Proc procedure = procedures.getProcedureByIndex(i);
            if(placeExistsInProcedure(place, procedure))
                return true;
        }

        return false;
    }

    private static boolean placeExistsInProcedure(String place, Proc procedure){
        for(ParamAssign assign: procedure.paramAssign){
            if(placeExistsInICode(place, assign))
                return true;
        }

        for(ICode icode : procedure.instructions){
            if(placeExistsInICode(place, icode))
                return true;
        }

        if(procedure.placement != null)
            if(placeExistsInICode(place, procedure.placement))
                return true;

        return false;
    }

    private static boolean placeIsUniqueToProgramOrLibrary(String place, Prog program, Lib[] libraries, Lib libraryToIgnore){
        if(placeExistsInProgram(place, program))
            return false;

        for(Lib library : libraries){
            if(!library.equals(libraryToIgnore))
                if(placeExistsInLibrary(place, library))
                    return false;
        }

        return true;
    }

    private static boolean placeIsUniqueToProgramOrLibrary(String place, Lib[] libraries){
        for(Lib library : libraries){
            if(placeExistsInLibrary(place, library))
                return false;
        }

        return true;
    }

    private static boolean placeIsUniqueToLibrary(String place, Lib library, Lib[] libraries, Lib libToIgnore){
        if(!library.equals(libToIgnore))
            if(placeExistsInLibrary(place, library))
                return false;

        for(Lib lib : libraries){
            if(!lib.equals(libToIgnore))
                if(placeExistsInLibrary(place, lib))
                    return false;
        }

        return true;
    }

    private static boolean instructionExistsInNewProgram(ICode codeToSearch, DataSec dataSec){
        for(int i = 0; i < dataSec.getLength(); i++){
            ICode icode = dataSec.getInstruction(i);
            if(icode.equals(codeToSearch))
                return true;
        }

        return false;
    }

    private void linkDataSections(Prog startingProgram, Lib[] libraries, SymSec symbolTable, DataSec dataSec, CodeSec codeSec, ProcSec procedures){
        SymSec programSymbolTable = startingProgram.symbols;
        DataSec programDataSec = startingProgram.variables;
        for(int i = 0; i < programDataSec.getLength(); i++){
            Assign assign = programDataSec.getInstruction(i);
            Exp assignExp = assign.value;
            if(assignExp instanceof BinExp){
                BinExp assignBinExp = (BinExp)assignExp;

                if(assignBinExp.left instanceof IdentExp){
                    IdentExp leftIdent = (IdentExp)assignBinExp.left;
                    if(programSymbolTable.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programSymbolTable.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingProgram, libraries, symbolTable, dataSec, codeSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(startingProgram, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }

                if(assignBinExp.right instanceof IdentExp){
                    IdentExp rightIdent = (IdentExp)assignBinExp.right;
                    if(programSymbolTable.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programSymbolTable.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingProgram, libraries, symbolTable, dataSec, codeSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(startingProgram, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }
            } else if(assignExp instanceof UnExp){
                UnExp assignUnExp = (UnExp)assignExp;
                if(assignUnExp.right instanceof IdentExp){
                    IdentExp rightIdent = (IdentExp)assignUnExp.right;
                    if(programSymbolTable.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = symbolTable.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingProgram, libraries, symbolTable, dataSec, codeSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(startingProgram, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }
            } else if(assignExp instanceof IdentExp){
                IdentExp assignIdentExp = (IdentExp)assignExp;
                if(programSymbolTable.containsEntryWithICodePlace(assignIdentExp.ident, SymEntry.EXTERNAL)){
                    SymEntry entry = symbolTable.getEntryByICodePlace(assignIdentExp.ident, SymEntry.EXTERNAL);
                    fetchExternalDependentInstructions(entry.declanIdent, startingProgram, libraries, symbolTable, dataSec, codeSec, procedures);
                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                            replacePlaceInProgram(startingProgram, entry.icodePlace, newEntry.icodePlace);
                    }
                }
            }
            dataSec.addInstruction(assign);
        }
    }

    private void linkDataSections(Lib startingLibrary, Lib[] libraries, SymSec symbolTable, DataSec dataSec, ProcSec procedures){
        SymSec programSymbolTable = startingLibrary.symbols;
        DataSec programDataSec = startingLibrary.variables;
        for(int i = 0; i < programDataSec.getLength(); i++){
            Assign assign = programDataSec.getInstruction(i);
            Exp assignExp = assign.value;
            if(assignExp instanceof BinExp){
                BinExp assignBinExp = (BinExp)assignExp;

                if(assignBinExp.left instanceof IdentExp){
                    IdentExp leftIdent = (IdentExp)assignBinExp.left;
                    if(programSymbolTable.containsEntryWithICodePlace(leftIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programSymbolTable.getEntryByICodePlace(leftIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingLibrary, libraries, symbolTable, dataSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInLib(startingLibrary, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }

                if(assignBinExp.right instanceof IdentExp){
                    IdentExp rightIdent = (IdentExp)assignBinExp.right;
                    if(programSymbolTable.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programSymbolTable.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingLibrary, libraries, symbolTable, dataSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInLib(startingLibrary, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }
            } else if(assignExp instanceof UnExp){
                UnExp assignUnExp = (UnExp)assignExp;
                if(assignUnExp.right instanceof IdentExp){
                    IdentExp rightIdent = (IdentExp)assignUnExp.right;
                    if(programSymbolTable.containsEntryWithICodePlace(rightIdent.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = symbolTable.getEntryByICodePlace(rightIdent.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, startingLibrary, libraries, symbolTable, dataSec, procedures);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInLib(startingLibrary, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }
            } else if(assignExp instanceof IdentExp){
                IdentExp assignIdentExp = (IdentExp)assignExp;
                if(programSymbolTable.containsEntryWithICodePlace(assignIdentExp.ident, SymEntry.EXTERNAL)){
                    SymEntry entry = symbolTable.getEntryByICodePlace(assignIdentExp.ident, SymEntry.EXTERNAL);
                    fetchExternalDependentInstructions(entry.declanIdent, startingLibrary, libraries, symbolTable, dataSec, procedures);
                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                            replacePlaceInLib(startingLibrary, entry.icodePlace, newEntry.icodePlace);
                    }
                }
            }
            dataSec.addInstruction(assign);
        }
    }

    private void linkCodeSection(Prog program, Lib[] libraries, SymSec symbolTable, DataSec dataSection, CodeSec codeSection, ProcSec procedureSec){
        SymSec programTable = program.symbols;
        CodeSec codeSec = program.code;
        ProcSec programProcSec = program.procedures;

        for(int i = 0; i < codeSec.getLength(); i++){
            ICode icode = codeSec.getInstruction(i);
            if(icode instanceof Assign){
                Assign assignment = (Assign)icode;
                Exp assignExp = assignment.value;

                if(assignExp instanceof IdentExp){
                    IdentExp ident = (IdentExp)assignExp;
                    if(programTable.containsEntryWithICodePlace(ident.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programTable.getEntryByICodePlace(ident.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                } else if(assignExp instanceof UnExp){
                    UnExp unExp = (UnExp)assignExp;
                    
                    if(unExp.right instanceof IdentExp){
                        IdentExp ident = (IdentExp)unExp.right;
                        if(programTable.containsEntryWithICodePlace(ident.ident, SymEntry.EXTERNAL)){
                            SymEntry entry = programTable.getEntryByICodePlace(ident.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                            if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                if(!entry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                            }
                        }
                    }
                } else if(assignExp instanceof BinExp){
                    BinExp binExp = (BinExp)assignExp;

                    if(binExp.left instanceof IdentExp){
                        IdentExp leftExp = (IdentExp)binExp.left;
                        if(programTable.containsEntryWithICodePlace(leftExp.ident, SymEntry.EXTERNAL)){
                            SymEntry entry = programTable.getEntryByICodePlace(leftExp.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                            if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                if(!entry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                            }
                        }
                    }

                    if(binExp.right instanceof IdentExp){
                        IdentExp rightExp = (IdentExp)binExp.right;
                        if(programTable.containsEntryWithICodePlace(rightExp.ident, SymEntry.EXTERNAL)){
                            SymEntry entry = programTable.getEntryByICodePlace(rightExp.ident, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                            if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                if(!entry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                            }
                        }
                    }
                }
            } else if(icode instanceof If){
                If ifStat = (If)icode;

                BinExp exp = ifStat.exp;
                if(exp.left instanceof IdentExp){
                    IdentExp leftExp = (IdentExp)exp.left;
                    if(programTable.containsEntryWithICodePlace(leftExp.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programTable.getEntryByICodePlace(leftExp.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }

                if(exp.right instanceof IdentExp){
                    IdentExp rightExp = (IdentExp)exp.right;
                    if(programTable.containsEntryWithICodePlace(rightExp.ident, SymEntry.EXTERNAL)){
                        SymEntry entry = programTable.getEntryByICodePlace(rightExp.ident, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }
            } else if(icode instanceof ExternalCall){
                ExternalCall call = (ExternalCall)icode;

                if(call.toRet != null){
                    if(!placeIsUniqueToProgramOrLibrary(call.toRet, program, libraries, program)){
                        String place = null;
                        do{
                            place = gen.genNextRegister();
                        } while(!placeIsUniqueToProgramOrLibrary(place, program, libraries, program));

                        replacePlaceInProgram(program, call.toRet, place);
                    }
                }
                
                if(!procedureSec.containsProcedure(call.procedureName))
                    fetchExternalProcedure(call.procedureName, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                Proc procedure = procedureSec.getProcedureByName(call.procedureName);

                int numberOfArgsInProc = procedure.paramAssign.size();
                int numberOfArgsInCall = call.arguments.size();

                if(numberOfArgsInCall != numberOfArgsInProc){
                    errLog.add("In call " + call.toString() + " expected " + numberOfArgsInCall + " but found procedure with " + numberOfArgsInProc + " arguments", new Position(i, 0));
                } else if(procedure.placement == null && call.toRet != null){
                    errLog.add("In call " + call.toString() + " function found does not have a return value and is VOID", new Position(i, 0));
                } else {
                    List<Tuple<String, String>> newArgs = new LinkedList<Tuple<String, String>>();
                    for(int argIndex = 0; argIndex < numberOfArgsInCall; argIndex++){
                        String place = call.arguments.get(argIndex);
                        Tuple<String, String> newArg = new Tuple<String,String>("", "");
                        
                        if(programTable.containsEntryWithICodePlace(place, SymEntry.EXTERNAL)){
                            SymEntry entry = programTable.getEntryByICodePlace(place, SymEntry.EXTERNAL);
                            fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                            if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                                SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                                if(!entry.icodePlace.equals(newEntry.icodePlace))
                                    replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                            }
                        }

                        newArg.source = place;
                        newArg.dest = procedure.paramAssign.get(argIndex).paramPlace;
                        newArgs.add(newArg);
                    }
                    codeSection.addInstruction(new Call(call.procedureName, newArgs));

                    if(call.toRet != null){
                        String toRetFrom = procedure.placement.place;
                        String toRetTo = call.toRet;
                        codeSection.addInstruction(new ExternalPlace(toRetTo, toRetFrom));
                    }

                    continue;
                }
            } else if(icode instanceof Call){
                Call call = (Call)icode;
                for(Tuple<String, String> arg : call.params){
                    String place = arg.source;

                    if(programTable.containsEntryWithICodePlace(place, SymEntry.EXTERNAL)){
                        SymEntry entry = programTable.getEntryByICodePlace(place, SymEntry.EXTERNAL);
                        fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                        if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                            SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                            if(!entry.icodePlace.equals(newEntry.icodePlace))
                                replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                        }
                    }
                }

                if(!procedureSec.containsProcedure(call.pname)){
                    Proc procedure = programProcSec.getProcedureByName(call.pname);
                    procedureSec.addProcedure(procedure);
                }
            } else if(icode instanceof ExternalPlace){
                ExternalPlace placement = (ExternalPlace)icode;
                
                if(programTable.containsEntryWithICodePlace(placement.retPlace, SymEntry.EXTERNAL)){
                    SymEntry entry = programTable.getEntryByICodePlace(placement.retPlace, SymEntry.EXTERNAL);
                    fetchExternalDependentInstructions(entry.declanIdent, program, libraries, symbolTable, dataSection, codeSection, procedureSec);
                    if(symbolTable.containsEntryWithIdentifier(entry.declanIdent, SymEntry.INTERNAL)){
                        SymEntry newEntry = symbolTable.getEntryByIdentifier(entry.declanIdent, SymEntry.INTERNAL);
                        if(!entry.icodePlace.equals(newEntry.icodePlace))
                            replacePlaceInProgram(program, entry.icodePlace, newEntry.icodePlace);
                    }
                }
            }

            codeSection.addInstruction(icode);
        }
    }

    private void linkProcedureSections(Lib library, Lib[] libraries, SymSec symbolTable, DataSec variables, ProcSec procedures){
        ProcSec libProcSec = library.procedures;
        // Finish tthis another day   
    }

    public Prog performLinkage(Prog program, Lib... libraries){
        SymSec symbolTable = new SymSec();
        DataSec dataSec = new DataSec();
        CodeSec codeSec = new CodeSec();
        ProcSec procSec = new ProcSec();
        linkDataSections(program, libraries, symbolTable, dataSec, codeSec, procSec);
        linkCodeSection(program, libraries, symbolTable, dataSec, codeSec, procSec);
        return new Prog(symbolTable, dataSec, codeSec, procSec);
    }

    public Prog performLinkage(Program prog, Library... libraries){
        Prog generatedProgram = generateProgram(errLog, prog);
        Lib[] libs = generateLibraries(errLog, libraries);
        return performLinkage(generatedProgram, libs);
    }

    public Lib performLinkage(Lib library, Lib... libraries){
        SymSec symbolTable = new SymSec();
        DataSec dataSec = new DataSec();
        ProcSec procSec = new ProcSec();
        linkDataSections(library, libraries, symbolTable, dataSec, procSec);
        linkProcedureSections(library, libraries, symbolTable, dataSec, procSec);
        return new Lib(symbolTable, dataSec, procSec);
    }

    public Lib performLinkage(Library library, Library... libraries){
        Lib lib = generateLibrary(errLog, library);
        Lib[] libs = generateLibraries(errLog, libraries);
        return performLinkage(lib, libs);
    }
}

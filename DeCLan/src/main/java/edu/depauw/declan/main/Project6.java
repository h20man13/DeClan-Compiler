package edu.depauw.declan.main;

import java.util.List;
import java.util.Properties;

import edu.depauw.declan.common.ErrorLog;
import edu.depauw.declan.common.ParseException;
import edu.depauw.declan.common.Parser;
import edu.depauw.declan.common.ast.Program;
import edu.depauw.declan.common.icode.ICode;
import edu.depauw.declan.model.ReferenceChecker;
import edu.depauw.declan.model.ReferenceGenerator;
import edu.depauw.declan.model.ReferenceInterpreter;

/**
 * Main class for Project 6 -- Code generator for larger subset of DeCLan (Fall 2020).
 * Parse a program, check for type errors, then generate intermediate code.
 * 
 * @author bhoward
 */
public class Project6 {
	public static void main(String[] args) {
		String demoSource =
				  "(* Declare some constants and a global variable *)\n"
				+ "CONST six = 6; seven = 7;\n"
				+ "VAR answer, temp : INTEGER;\n"
				+ "(* Define a proper procedure *)\n"
				+ "PROCEDURE Display(answer: INTEGER; a, b: INTEGER; x: REAL);\n"
				+ "  VAR i, temp : INTEGER;\n"
				+ "  BEGIN\n"
				+ "    temp := answer;\n"
				+ "    FOR i := a TO b BY -1 DO\n"
				+ "      PrintInt(temp); PrintLn();\n"
				+ "      WHILE temp > i DO temp := temp - 1\n"
				+ "      ELSIF temp < i DO temp := temp + 1\n"
				+ "      END\n"
				+ "    END;\n"
				+ "    ASSERT(temp = b, \"something went wrong\");\n"
				+ "    PrintReal(x / 2.); PrintLn()\n"
				+ "  END Display;\n"
				+ "(*********** Main Program ***********)\n"
				+ "BEGIN\n"
				+ "  answer := 0;\n"
				+ "  temp := 0;\n"
				+ "  REPEAT\n"
				+ "    answer := answer + temp;\n"
				+ "    temp := temp + seven;\n"
				+ "  UNTIL answer >= six * seven;\n"
				+ "  PrintString(\"The answer is \");\n"
				+ "  Display(answer, seven, six, 3.14159265);\n"
				+ "  PrintInt(answer); PrintLn();\n"
				+ "  PrintInt(temp); PrintLn();\n"
				+ "END.\n";

		Properties props = new Properties();
		props.setProperty("useModelLexer", "false");
		props.setProperty("useModelParser", "false");
		props.setProperty("sourceFile", "");
		props.setProperty("demoSource", demoSource);

		Config config = new Config(args, props);

		try (Parser parser = config.getParser()) {
			Program program = parser.parseProgram();
program.accept(new ReferenceInterpreter(config.getErrorLog()));			
			// Type-check the program, recording discovered type info in the checker object
			MyTypeChecker checker = new MyTypeChecker(config.getErrorLog());
			program.accept(checker);
			
			// Generate intermediate code assuming the type checker succeeded
			MyCodeGenerator generator = new MyCodeGenerator(config.getErrorLog(), checker);
			program.accept(generator);
			List<ICode> code = generator.getCode();
			// Print out the intermediate code
			for (ICode instr : code) {
				System.out.println(instr);
			}
		} catch (ParseException pe) {
			System.err.println(pe.getMessage());
		}

		for (ErrorLog.LogItem item : config.getErrorLog()) {
			System.err.println(item);
		}

		System.out.println("DONE");
	}
}

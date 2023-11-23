package io.github.H20man13.DeClan.common.builder;

import java.util.LinkedList;
import java.util.List;

import edu.depauw.declan.common.ErrorLog;
import io.github.H20man13.DeClan.common.IrRegisterGenerator;
import io.github.H20man13.DeClan.common.Tuple;
import io.github.H20man13.DeClan.common.builder.template.CompletableBuilder;
import io.github.H20man13.DeClan.common.icode.Assign;
import io.github.H20man13.DeClan.common.icode.ICode;
import io.github.H20man13.DeClan.common.icode.exp.BinExp;
import io.github.H20man13.DeClan.common.icode.exp.Exp;
import io.github.H20man13.DeClan.common.icode.exp.IdentExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp;
import io.github.H20man13.DeClan.common.icode.exp.UnExp.Operator;
import io.github.H20man13.DeClan.common.icode.section.CodeSec;
import io.github.H20man13.DeClan.main.MyIrFactory;

public class CodeSectionBuilder extends StatementBuilder implements CompletableBuilder<CodeSec> {
    private MyIrFactory factory;
    private IrRegisterGenerator gen;
    public CodeSectionBuilder(IrBuilderContext ctx, IrRegisterGenerator gen, ErrorLog errLog){
        super(ctx, gen, errLog);
        this.factory = new MyIrFactory(errLog);
        this.gen = gen;
        resetBuilder();
    }

    @Override
    public CodeSec completeBuild() {
        CodeSec section = new CodeSec(intermediateCode);
        resetBuilder();
        return section;
    }

    @Override
    public String buildParamaterAssignment(String place) {
        String gened = gen.genNextRegister();
        this.intermediateCode.add(factory.produceParamAssignment(gened, place));
        return gened;
    }
}

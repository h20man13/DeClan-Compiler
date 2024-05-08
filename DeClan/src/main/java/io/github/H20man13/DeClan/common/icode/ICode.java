package io.github.H20man13.DeClan.common.icode;

import java.util.List;
import java.util.Set;

import io.github.H20man13.DeClan.common.pat.P;

public interface ICode {
    public String toString();
    public boolean isConstant();
    public boolean isBranch();
    public P asPattern();
    public boolean equals(Object object);
    public List<ICode> genFlatCode();
    public boolean containsPlace(String place);
    public boolean containsParamater(String place);
    public Set<String> paramaterForFunctions(String place);
    public Set<String> argumentInFunctions(String place);
    public Set<String> internalReturnForFunctions(String place);
    public Set<String> externalReturnForFunctions(String place);
    public boolean containsArgument(String place);
    public boolean containsInternalReturn(String place);
    public boolean containsExternalReturn(String place);
    public boolean containsLabel(String label);
    public void replacePlace(String from, String to);
    public void replaceLabel(String from, String to);
}

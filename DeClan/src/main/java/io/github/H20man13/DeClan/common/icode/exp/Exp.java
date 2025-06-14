package io.github.H20man13.DeClan.common.icode.exp;

import io.github.H20man13.DeClan.common.Copyable;
import io.github.H20man13.DeClan.common.pat.P;

public interface Exp extends NullableExp{
    @Override
    public boolean equals(Object exp);
    @Override
    public String toString();

    public boolean isBranch();

    public boolean isConstant();
    public boolean containsPlace(String place);
    public void replacePlace(String from, String to);

    public P asPattern(boolean hasContainder);
    
    @Override
    public int hashCode();
    
    @Override
    public NullableExp copy();
}

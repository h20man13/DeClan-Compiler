package io.github.H20man13.DeClan.common.region;

import io.github.H20man13.DeClan.common.icode.ICode;

public interface RegionBase {
	@Override
	public String toString();
	@Override
	public int hashCode();
	public ICode getFirstInstruction();
	public ICode getLastInstruction();
}

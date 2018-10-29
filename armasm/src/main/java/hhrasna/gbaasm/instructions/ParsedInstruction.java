package hhrasna.gbaasm.instructions;

import java.util.*;

public class ParsedInstruction {
    
	private Class instructionHandler;
	private int lineNum;
	private int address;
	private String mneumonic;
	private Vector opcodes;

	public ParsedInstruction(Class instructionHandler, int lineNum, int address, String mneumonic, Vector opcodes)  {
		this.instructionHandler = instructionHandler;
		this.lineNum = lineNum;
		this.address = address;
		this.mneumonic = mneumonic;
		this.opcodes = opcodes;
	}
	
	public Class getHandler() {
		return instructionHandler;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public int getAddress() {
		return address;
	}
	
	public String getMneumonic() {
		return mneumonic;
	}
	
	public Vector getOpcodes() {
		return opcodes;
	}
}
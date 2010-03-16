package asm.instructions;


public class ARM7MVN extends ARM7MOV {


	public ARM7MVN(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    set(new int [] {24,23,22,21}); // MVN 1111 operand2 (operand1 is ignored)
	}
}
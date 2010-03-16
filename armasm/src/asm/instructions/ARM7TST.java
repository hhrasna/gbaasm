package asm.instructions;


public class ARM7TST extends ARM7CMP {

	public ARM7TST(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    set(24); // TST 1000
	    clear(23);
	    clear(22);
	    clear(21);
	}
}
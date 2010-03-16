package asm.instructions;


public class ARM7TEQ extends ARM7CMP {

	public ARM7TEQ(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    set(new int [] {24,21}); // TEQ 1001
	    clear(23);
	    clear(22);
	}
}
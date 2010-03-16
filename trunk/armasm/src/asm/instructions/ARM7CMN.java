package asm.instructions;


public class ARM7CMN extends ARM7CMP {

	public ARM7CMN(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    set(new int [] {24,22,21}); // CMN 1011
	    clear(23);
	}
}
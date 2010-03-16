package asm.instructions;


public class ARM7BIC extends ARM7AND {


	public ARM7BIC(ParsedInstruction pi) {
	    super(pi);
	    set(24); // BIC 1110 operand1 AND NOT operand2 (Bit clear)
	    set(23);
	    set(22);
	    clear(21);
	}
}
package asm.instructions;


public class ARM7EOR extends ARM7AND {


	public ARM7EOR(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // EOR 0001 operand1 EOR operand2
	    clear(23);
	    clear(22);
	    set(21);
	}
}
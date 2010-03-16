package asm.instructions;


public class ARM7ORR extends ARM7AND {


	public ARM7ORR(ParsedInstruction pi) {
	    super(pi);
	    set(24); // ORR 1100 operand1 OR operand2
	    set(23);
	    clear(22);
	    clear(21);
	}
}
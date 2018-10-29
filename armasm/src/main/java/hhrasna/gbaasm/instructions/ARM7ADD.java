package hhrasna.gbaasm.instructions;


public class ARM7ADD extends ARM7AND {


	public ARM7ADD(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // ADD 0100 operand1 + operand2
	    set(23);
	    clear(22);
	    clear(21);
	}
}
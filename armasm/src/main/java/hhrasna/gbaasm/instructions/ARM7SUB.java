package hhrasna.gbaasm.instructions;


public class ARM7SUB extends ARM7AND {


	public ARM7SUB(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // SUB 0010 operand1 - operand2
	    clear(23);
	    set(22);
	    clear(21);
	}
}
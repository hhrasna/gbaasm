package hhrasna.gbaasm.instructions;


public class ARM7SBC extends ARM7AND {


	public ARM7SBC(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // SBC 0110 operand1 - operand2 + carry - 1
	    set(23);
	    set(22);
	    set(21);
	}
}
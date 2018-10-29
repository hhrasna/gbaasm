package hhrasna.gbaasm.instructions;


public class ARM7RSC extends ARM7AND {


	public ARM7RSC(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // RSC 0111 operand2 - operand1 + carry - 1
	    set(23);
	    set(22);
	    set(21);
	}
}
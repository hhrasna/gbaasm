package asm.instructions;


public class ARM7RSB extends ARM7AND {


	public ARM7RSB(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // RSB 0011 operand2 - operand1
	    clear(23);
	    set(22);
	    set(21);
	}
}
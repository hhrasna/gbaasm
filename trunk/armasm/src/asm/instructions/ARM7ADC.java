package asm.instructions;

public class ARM7ADC extends ARM7AND {


	public ARM7ADC(ParsedInstruction pi) {
	    super(pi);
	    clear(24); // ADC 0101 operand1 + operand2 + carry
	    set(23);
	    clear(22);
	    set(21);
	}
}
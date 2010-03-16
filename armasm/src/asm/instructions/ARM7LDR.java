package asm.instructions;


/* <LDR|STR>{cond}{B}{T} Rd,<Address> */
public class ARM7LDR extends ARM7SingleDataXfer {

	public ARM7LDR(ParsedInstruction pi) {	
		super(pi);
	    set(20); // set load/store bit (load)
	}
}
package asm.instructions;


public class ARM7STR extends ARM7SingleDataXfer {

	public ARM7STR(ParsedInstruction pi) {
		super(pi);
		
		clear(20); // clear load/store bit (store)
			
	}
}
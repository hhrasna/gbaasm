package asm.instructions;


public class ARM7CMP extends ARM7DataProcessing {

	public ARM7CMP(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    set(new int [] {24,22}); // CMP 1010
	    clear(23);
	    clear(21);
	    setRn((String)opcodes.elementAt(1));
	    setOp2((String)opcodes.elementAt(2));
	    if(opcodes.size() > 3) {
	    	setShift((String)opcodes.elementAt(3));
		}
	}
}
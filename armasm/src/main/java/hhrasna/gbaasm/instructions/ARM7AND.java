package hhrasna.gbaasm.instructions;


public class ARM7AND extends ARM7DataProcessing {

	public ARM7AND(ParsedInstruction pi) {
	    super(pi);
	    java.util.Vector opcodes = pi.getOpcodes();
	    clear(24); // AND 0000 operand1 AND operand2
	    clear(23);
	    clear(22);
	    clear(21);
	    setRd((String)opcodes.elementAt(1));
	    setRn((String)opcodes.elementAt(2));
	    setOp2((String)opcodes.elementAt(3));
	    if(opcodes.size() > 4) {
	    	setShift((String)opcodes.elementAt(4));
		}
	}
}
package asm.instructions;


public class ARM7MOV extends ARM7DataProcessing {
    
    
    public ARM7MOV(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        set(new int [] {24,23,21}); // MOV 1101 operand2 (operand1 is ignored)
        clear(22);
        setRd((String)opcodes.elementAt(1));
        if(opcodes.size() > 3) {
            setShift((String)opcodes.elementAt(3));
        }
        setOp2((String)opcodes.elementAt(2));
    }
}
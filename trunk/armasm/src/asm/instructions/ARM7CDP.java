/*
 * ARM7CDP.java
 *
 * Created on December 4, 2002, 3:34 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7CDP extends ARM7Coprocessor {
    
    /** Creates a new instance of ARM7CDP */
    public ARM7CDP(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        set(new int[] {27,26,25});
        setCPNumber((String)opcodes.elementAt(1));
        
        //set Coprocessor opcode bits 23-20 CPOpc
        int r = parseExpression((String)opcodes.elementAt(2));
        System.out.println(mneumonic + ": CPOpc = " + r);
        int i = 1;
        for ( int bit = 20; bit <= 23; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
        
        setCRd((String)opcodes.elementAt(3));
        setCRn((String)opcodes.elementAt(4));
        setCRm((String)opcodes.elementAt(5));
        if (opcodes.size() > 6) {
            setCPInfo((String)opcodes.elementAt(6));
        }
    }
    
}

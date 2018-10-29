/*
 * ARM7MLA.java
 *
 * Created on December 3, 2002, 4:38 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7MLA extends ARM7Multiply {
    
    /** Creates a new instance of ARM7MUL */
    public ARM7MLA(ParsedInstruction pi) {
        super(pi);
        set(21);
        java.util.Vector opcodes = pi.getOpcodes();
        
        //set Rn
        String reg = (String)opcodes.elementAt(1);
        int r = getRegisterNumber(reg);
        int i = 1;
	for ( int bit = 12; bit <= 15; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        } 
    }
    
}

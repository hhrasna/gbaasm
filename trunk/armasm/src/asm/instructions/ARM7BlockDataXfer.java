/*
 * ARM7BlockDataXfer.java
 *
 * Created on November 22, 2002, 11:05 AM
  Block Data Transfer (LDM, STM)
  The instruction is only executed if the condition is true.
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7BlockDataXfer extends ARM7Instruction {
    
    /** Creates a new instance of ARM7BlockDataXfer */
    public ARM7BlockDataXfer(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        // unconditional bits
        set(27);
        clear(26);
        clear(25);
        
        // set Rn
        String rn = ((String)opcodes.elementAt( 1 )).toUpperCase();
        int r = getRegisterNumber( rn );
	System.out.println(mneumonic + ": setRn = " + r);
	int i = 1;
	for ( int bit = 16; bit <= 19; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        }
        
        // set write back bit
        if ( rn.endsWith("!") ) {
            set(21);
        }
        
        // set register list
        if (((String)opcodes.elementAt(2)).startsWith("{") && ((String)opcodes.lastElement()).indexOf("}") >= 0) {
            for ( i = 2; i < opcodes.size(); i++ ) {
                String s = (String)opcodes.elementAt( i );
                if ( s.indexOf("-") >= 0 ) { // range ie. R1-R9
                    String s1 = s.substring(0, s.indexOf("-"));
                    String s2 = s.substring(s.indexOf("-"), s.length());
                    int start = getRegisterNumber( s1 );
                    int end = getRegisterNumber( s2 );
                    for ( ; start <= end; start++ ) {
                        set(start);
                    }
                } else {
                    r = getRegisterNumber( s );
                    set( r );
                }
            }
        } else {
            error("Syntax error ");
        }
            
        // set S bit
        if ( ((String)opcodes.lastElement()).endsWith("^") ) {
            set(22);
        }
    }
    
}

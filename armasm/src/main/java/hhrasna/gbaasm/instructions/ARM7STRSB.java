/*
 * ARM7STRSB.java
 *
 * Created on November 21, 2002, 5:30 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7STRSB extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRSB */
    public ARM7STRSB(ParsedInstruction pi) {
        super(pi);
        set(6); //set S bit 6
        clear(5);
    }
    
}

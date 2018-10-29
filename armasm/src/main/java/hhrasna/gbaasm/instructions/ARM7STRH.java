/*
 * ARM7STRH.java
 *
 * Created on November 21, 2002, 1:45 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7STRH extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRH */
    public ARM7STRH( ParsedInstruction pi ) {
        super(pi);
        set(5); //set H bit 5
    }
    
}

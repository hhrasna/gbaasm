/*
 * ARM7STRSH.java
 *
 * Created on November 21, 2002, 5:26 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7STRSH extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRSH */
    public ARM7STRSH(ParsedInstruction pi) {
        super(pi);
        set(6); //set S bit 6
        set(5); //set H bit 5
    }
    
}

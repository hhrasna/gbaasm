/*
 * ARM7SWI.java
 *
 * Created on December 4, 2002, 10:04 AM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7SWI extends ARM7Instruction {
    
    /** Creates a new instance of ARM7SWI */
    public ARM7SWI(ParsedInstruction pi) {
        super(pi);
        set(new int [] {27,26,25,24});
    }
    
}

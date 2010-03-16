/*
 * ARM7SMULL.java
 *
 * Created on November 25, 2002, 4:09 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7SMULL extends ARM7MultiplyLong {
    
    /** Creates a new instance of ARM7SMULL */
    public ARM7SMULL( ParsedInstruction pi ) {
        super(pi);
        set(22); // Unsigned bit 1=signed
    }
    
}

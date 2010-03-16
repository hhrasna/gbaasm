/*
 * ARM7SMLAL.java
 *
 * Created on December 2, 2002, 4:09 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7SMLAL extends ARM7MultiplyLong {
    
    /** Creates a new instance of ARM7SMULL */
    public ARM7SMLAL( ParsedInstruction pi ) {
        super(pi);
        set(22); // Unsigned bit 1=signed
        set(21); // 1 = multiply and accumulate
    }
    
}

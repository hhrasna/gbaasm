/*
 * ARM7UMLAL.java
 *
 * Created on December 2, 2002, 4:09 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7UMLAL extends ARM7MultiplyLong {
    
    /** Creates a new instance of ARM7SMULL */
    public ARM7UMLAL( ParsedInstruction pi ) {
        super(pi);
        set(21); // 1 = multiply and accumulate
    }
    
}

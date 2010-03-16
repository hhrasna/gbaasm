/*
 * ARM7SWPB.java
 *
 * Created on December 3, 2002, 10:00 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7SWPB extends ARM7SingleDataSwap {
    
    /** Creates a new instance of ARM7SWPB */
    public ARM7SWPB(ParsedInstruction pi) {
        super(pi);
        set(22);
    }
    
}

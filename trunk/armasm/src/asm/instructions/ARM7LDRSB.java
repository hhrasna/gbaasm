package asm.instructions;

/*
 * ARM7LDRSB.java
 *
 * Created on November 21, 2002, 5:30 PM
 */

/**
 *
 * @author  hans
 */
public class ARM7LDRSB extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRSB */
    public ARM7LDRSB(ParsedInstruction pi) {
        super(pi);
        set(6); //set S bit 6
        set(20); //set L bit = load
        clear(5); //clear H bit
    }
    
}
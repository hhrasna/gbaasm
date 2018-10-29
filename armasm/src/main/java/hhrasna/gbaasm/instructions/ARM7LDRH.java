package hhrasna.gbaasm.instructions;

/*
 * ARM7LDRH.java
 *
 * Created on November 21, 2002, 8:29 PM
 */

/**
 *
 * @author  hans
 */
public class ARM7LDRH extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRH */
    public ARM7LDRH(ParsedInstruction pi) {
        super(pi);
        set(5); //set H bit 5
        set(20); //set L bit = load
    }
    
}

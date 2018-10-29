package hhrasna.gbaasm.instructions;

/*
 * ARM7STRSH.java
 *
 * Created on November 21, 2002, 5:26 PM
 */

/**
 *
 * @author  hans
 */
public class ARM7LDRSH extends ARM7HalfwordXfer {
    
    /** Creates a new instance of ARM7STRSH */
    public ARM7LDRSH(ParsedInstruction pi) {
        super(pi);
        set(6); //set S bit 6
        set(5); //set H bit 5
        set(20); //set L bit = load
    }
    
}

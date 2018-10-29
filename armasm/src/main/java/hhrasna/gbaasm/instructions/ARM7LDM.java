/*
 * ARM7LDM.java
 *
 * Created on November 25, 2002, 2:35 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7LDM extends ARM7BlockDataXfer {
    
    /** Creates a new instance of ARM7LDM */
    public ARM7LDM( ParsedInstruction pi ) {
        super(pi);
        set(20); //Load/Store 1 = load
        String cmd = ((String)(pi.getOpcodes()).firstElement()).toUpperCase();
        if ( cmd.endsWith("ED") || cmd.endsWith("IB") ) {
            set(24); // P bit
            set(23); // U bit
        } else
        if ( cmd.endsWith("FD") || cmd.endsWith("IA") ) {
            clear(24);
            set(23);
        } else
        if ( cmd.endsWith("EA") || cmd.endsWith("DB") ) {
           set(24);
           clear(23);
        } else
        if ( cmd.endsWith("FA") || cmd.endsWith("DA") ) {
            clear(24);
            clear(23);
        } else {
            error("Syntax error: " + cmd);
        }
    }
    
}

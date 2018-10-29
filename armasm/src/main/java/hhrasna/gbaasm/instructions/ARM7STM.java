/*
 * ARM7STM.java
 *
 * Created on November 25, 2002, 3:34 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 */
public class ARM7STM extends ARM7BlockDataXfer {
    
    /** Creates a new instance of ARM7STM */
    public ARM7STM( ParsedInstruction pi ) {
        super(pi);
        clear(20); //Load/Store 0 = load
        String cmd = ((String)(pi.getOpcodes()).firstElement()).toUpperCase();
        if ( cmd.endsWith("FA") || cmd.endsWith("IB") ) {
            set(24); // P bit
            set(23); // U bit
        } else
        if ( cmd.endsWith("EA") || cmd.endsWith("IA") ) {
            clear(24);
            set(23);
        } else
        if ( cmd.endsWith("FD") || cmd.endsWith("DB") ) {
           set(24);
           clear(23);
        } else
        if ( cmd.endsWith("ED") || cmd.endsWith("DA") ) {
            clear(24);
            clear(23);
        } else {
            error("Syntax error: " + cmd);
        }
    }
    
}

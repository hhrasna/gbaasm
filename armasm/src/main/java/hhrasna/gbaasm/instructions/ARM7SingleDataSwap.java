/*
 * ARM7SingleDataSwap.java
 *
 * Created on December 3, 2002, 9:41 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 *
 Syntax
 <SWP>{cond}{B} Rd,Rm,[Rn]

 {cond} two-character condition mnemonic.

 {B} if B is present then byte transfer, otherwise word transfer

 Rd,Rm,Rn are expressions evaluating to valid register numbers
 */
public class ARM7SingleDataSwap extends ARM7DataProcessing {
    
    /** Creates a new instance of ARM7SingleDataSwap */
    public ARM7SingleDataSwap(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        set(new int[] {24,7,4});
        setRd((String)opcodes.elementAt(1));
        setRm((String)opcodes.elementAt(2));
        if(opcodes.size() > 3) {
            setRn((String)opcodes.elementAt(3));
        }
    }
}

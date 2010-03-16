/*
 * ARM7MRS.java
 *
 * Created on December 3, 2002, 5:37 PM
 */

package asm.instructions;

/**
 *
 * @author  hans
 *
 Syntax
    MRS - transfer PSR contents to a register
        MRS{cond} Rd,<psr>
Key:
{cond} two-character condition mnemonic. 

 Rd and Rm are expressions evaluating to a register number other than
R15

 <psr> is CPSR, CPSR_all, SPSR or SPSR_all. (CPSR and
CPSR_all are synonyms as are SPSR and SPSR_all) 
 */
public class ARM7MRS extends ARM7DataProcessing {
    
    /** Creates a new instance of ARM7MRS */
    public ARM7MRS(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
	set(new int [] {24,19,18,17,16}); // unconditional bits
        
        //set Rd
        setRd((String)opcodes.elementAt(1));
                
        //set Ps
        String ps = (String)opcodes.elementAt(2);
        if ( ps.equals("CPSR") || ps.equals("CPSR_all") ) {
            clear(22);
        } else if ( ps.equals("SPSR") || ps.equals("SPSR_all") ) {
            set(22);
        } else {
            error("Syntax error, Ps must be CPSR, CPSR_all, SPSR, or SPSR_all -> " + ps);
        }
    }
    
}

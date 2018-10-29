/*
 * ARM7MSR.java
 *
 * Created on December 3, 2002, 5:38 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 
 Syntax
 MSR - transfer register contents to PSR
MSR{cond} <psr>,Rm

 MSR - transfer register contents to PSR flag bits only
MSR{cond} <psrf>,Rm
The most significant four bits of the register contents are written to the N,Z,C
& V flags respectively.

 MSR - transfer immediate value to PSR flag bits only
MSR{cond} <psrf>,<#expression>
The expression should symbolise a 32 bit value of which the most significant
four bits are written to the N,Z,C and V flags respectively.

 Key:
{cond} two-character condition mnemonic. 

 Rd and Rm are expressions evaluating to a register number other than
R15

 <psr> is CPSR, CPSR_all, SPSR or SPSR_all. (CPSR and
CPSR_all are synonyms as are SPSR and SPSR_all)

 <psrf> is CPSR_flg or SPSR_flg

 <#expression> where this is used, the assembler will attempt to generate a
shifted immediate 8-bit field to match the expression. If this is
impossible, it will give an error.
 */
public class ARM7MSR extends ARM7DataProcessing {
    
    /** Creates a new instance of ARM7MSR */
    public ARM7MSR(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        set(new int [] {24,21,19,15,14,13,12}); // unconditional bits
    
        //set Pd
        String ps = (String)opcodes.elementAt(1);
        if ( ps.equals("CPSR") || ps.equals("CPSR_all") ) {
            set(16);
            clear(22);
        } else if ( ps.equals("SPSR") || ps.equals("SPSR_all") ) {
            set(16);
            set(22);
        } else if ( ps.equals("CPSR_flg") || ps.equals("SPSR_flg") ) {
            //this space intentionally blank
        } else {
            error("Syntax error, Ps must be CPSR, CPSR_all, SPSR, or SPSR_all -> " + ps);
        }
        
        //set Rm or #expression
        setOp2((String)opcodes.elementAt(2));
    }
}

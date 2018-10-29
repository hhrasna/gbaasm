/*
 * ARM7MultiplyLong.java
 *
 * Created on November 25, 2002, 3:54 PM
 */

package hhrasna.gbaasm.instructions;

/**
 *
 * @author  hans
 *

Syntax

Mnemonic                       Description
UMULL{cond}{S} RdLo,RdHi,Rm,Rs Unsigned Multiply Long 32 x 32 = 64
UMLAL{cond}{S} RdLo,RdHi,Rm,Rs Unsigned Multiply & Accumulate Long 32 x 32 + 64 = 64
SMULL{cond}{S} RdLo,RdHi,Rm,Rs Signed Multiply Long 32 x 32 = 64
SMLAL{cond}{S} RdLo,RdHi,Rm,Rs Signed Multiply & Accumulate Long 32 x 32 + 64 = 64

where:
{cond} two-character condition mnemonic. 

{S} set condition codes if S present
 
RdLo, RdHi, Rm, Rs are expressions evaluating to a register number other
than R15.
 
 */
public class ARM7MultiplyLong extends ARM7Instruction {
    
    /** Creates a new instance of ARM7MultiplyLong */
    public ARM7MultiplyLong( ParsedInstruction pi ) {
        super(pi);
        // unconditional bits
        set(23);
        set(7);
        set(4);
        
        java.util.Vector opcodes = pi.getOpcodes();
        
        //set S bit (condition code set)
        String cmd = (String)opcodes.elementAt(0);
        if ( cmd.endsWith("S") ) {
            set(20);
        }
        
        //set RdLo
        String reg = (String)opcodes.elementAt(1);
        int r = getRegisterNumber(reg);
        int i = 1;
	for ( int bit = 12; bit <= 15; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        }
        
        //set RdHi
        reg = (String)opcodes.elementAt(2);
        r = getRegisterNumber(reg);
        i = 1;
        for ( int bit = 16; bit <= 19; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        }
        
        //set Rm
        reg = (String)opcodes.elementAt(3);
        r = getRegisterNumber(reg);
        i = 1;
        for ( int bit = 0; bit <= 3; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        }
        
        //set Rs
        reg = (String)opcodes.elementAt(4);
        r = getRegisterNumber(reg);
        i = 1;
        for ( int bit = 8; bit <= 11; bit++ ) {
            if ( ( r & i ) == i ) {
	        set(bit);
	    }
	    i = i * 2;
        }
    }
    
}

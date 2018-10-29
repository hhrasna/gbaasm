package hhrasna.gbaasm.instructions;

import java.util.*;

/** Branch and Exchange (BX)

This instruction is only executed if the condition is true. 

This instruction performs a branch by copying the contents of a general register, Rn,
into the program counter, PC. The branch causes a pipeline flush and refill from the
address specified by Rn. This instruction also permits the instruction set to be
exchanged. When the instruction is executed, the value of Rn[0] determines whether
the instruction stream will be decoded as ARM or THUMB instructions.

Syntax:

BX - branch and exchange.

BX{cond} Rn

{cond} Two character condition mnemonic.

Rn is an expression evaluating to a valid register number.

Using R15 as an operand
If R15 is used as an operand, the behaviour is undefined.

*/

public class ARM7BX extends ARM7Instruction {

    /* 31-28      24      20      16        11   9 8 7 6 5 4 3-0
        Cond 0 0 0 1 0 0 1 0 1 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 Rn
    */
    
    public ARM7BX(ParsedInstruction pi) {
        super(pi);
        
        Vector opcodes = pi.getOpcodes();
        int instaddr = pi.getAddress();
        
        // unconditional bits
        set( new int [] {24,21,19,18,17,16,15,14,13,12,11,10,9,8,4});
        
        String cmd = (String)opcodes.firstElement();
        String rn = ((String)opcodes.lastElement());
        int r = getRegisterNumber(rn);
		int i = 1;
		for ( int bit = 0; bit <= 3; bit++ ) {
			if ( ( r & i ) == i ) {
				set(bit);
			}
			i = i * 2;
		}              
    }        
}
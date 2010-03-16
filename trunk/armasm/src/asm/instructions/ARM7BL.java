package asm.instructions;

import asm.Assembler;

/** Branch with Link (BL)

The instruction is only executed if the condition is true. 

Branch instructions contain a signed 2's complement 24 bit offset. This is shifted left
two bits, sign extended to 32 bits, and added to the PC. The instruction can therefore
specify a branch of +/- 32Mbytes. The branch offset must take account of the prefetch
operation, which causes the PC to be 2 words (8 bytes) ahead of the current
instruction.

Branches beyond +/- 32Mbytes must use an offset or absolute destination which has
been previously loaded into a register. In this case the PC should be manually saved
in R14 if a Branch with Link type operation is required.

The link bit

Branch with Link (BL) writes the old PC into the link register (R14) of the current bank.
The PC value written into R14 is adjusted to allow for the prefetch, and contains the
address of the instruction following the branch and link instruction. Note that the CPSR
is not saved with the PC and R14[1:0] are always cleared.
To return from a routine called by Branch with Link use MOV PC,R14 if the link register
is still valid or LDM Rn!,{..PC} if the link register has been saved onto a stack pointed
to by Rn.

Syntax:

Items in {} are optional. Items in <> must be present.

B{L}{cond} <expression>

{L} is used to request the Branch with Link form of the instruction.
If absent, R14 will not be affected by the instruction.

{cond} is a two-character mnemonic
If absent then AL (ALways) will be used.

<expression> is the destination. The assembler calculates the offset.

*/

public class ARM7BL extends ARM7B {

    /*
      31-28  Condition
      27 = 1
      26 = 0
      25 = 1
      24      Link bit 0 = Branch 1 = Branch with Link (BL)
      23-0    Signed 2's complement 24 bit offset
    */
     
    private String destination = null;
    
    public ARM7BL(ParsedInstruction pi) {
        super(pi);
        
        set(24); // Branch with Link
	}
	
}
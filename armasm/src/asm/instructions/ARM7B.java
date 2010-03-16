package asm.instructions;

import java.util.*;

/** Branch and Branch with Link (B, BL)
 *
 * The instruction is only executed if the condition is true.
 *
 * Branch instructions contain a signed 2's complement 24 bit offset. This is shifted left
 * two bits, sign extended to 32 bits, and added to the PC. The instruction can therefore
 * specify a branch of +/- 32Mbytes. The branch offset must take account of the prefetch
 * operation, which causes the PC to be 2 words (8 bytes) ahead of the current
 * instruction.
 *
 * Branches beyond +/- 32Mbytes must use an offset or absolute destination which has
 * been previously loaded into a register. In this case the PC should be manually saved
 * in R14 if a Branch with Link type operation is required.
 *
 * The link bit
 *
 * Branch with Link (BL) writes the old PC into the link register (R14) of the current bank.
 * The PC value written into R14 is adjusted to allow for the prefetch, and contains the
 * address of the instruction following the branch and link instruction. Note that the CPSR
 * is not saved with the PC and R14[1:0] are always cleared.
 * To return from a routine called by Branch with Link use MOV PC,R14 if the link register
 * is still valid or LDM Rn!,{..PC} if the link register has been saved onto a stack pointed
 * to by Rn.
 *
 * Syntax:
 *
 * Items in {} are optional. Items in <> must be present.
 *
 * B{L}{cond} <expression>
 *
 * {L} is used to request the Branch with Link form of the instruction.
 * If absent, R14 will not be affected by the instruction.
 *
 * {cond} is a two-character mnemonic
 * If absent then AL (ALways) will be used.
 *
 * <expression> is the destination. The assembler calculates the offset.
 *
 */

public class ARM7B extends ARM7Instruction {
    
    /*
      31-28  Condition
      27 = 1
      26 = 0
      25 = 1
      24      Link bit 0 = Branch 1 = Branch with Link (BL)
      23-0    Signed 2's complement 24 bit offset
     */
    
    private String destination = null;
    
    public ARM7B(ParsedInstruction pi) {
        super(pi);
        
        Vector opcodes = pi.getOpcodes();
        int instaddr = pi.getAddress();
        
        // unconditional bits
        set(27);
        set(25);
        clear(26);
        
        String cmd = (String)opcodes.firstElement();
        destination = ((String)opcodes.lastElement());
        
        // if specified as number: interpret as constant, else calculate offset to label
        try {
            Integer c = Integer.decode(destination);
            System.out.println("Integer.decode( " + destination + " ) = " + c);
        }
        catch ( NumberFormatException ne ) {
            System.out.println("B destination: " + destination);
            int offset;
            String destnum = getLabel(destination);
            System.out.println( destination + " = " + destnum );
            if ( destnum == null ) {
                error("Can't find label " + destination);
                return;
            }
            try {
                int destaddr = Integer.parseInt(destnum);
                if ( instaddr < destaddr ) { // foward branch
                    offset = (destaddr - (instaddr + 8)) / 4; //PC 2 words ahead of inst due to prefetch
                } else {
                    offset = ((instaddr + 8) - destaddr) / 4;
                    offset =~ offset;
                    offset++;        // 2's complement
                }
                System.out.println("B calculated offset = " + Integer.toHexString(offset));
                int i = 1;
                for ( int bit = 0;bit <= 23 ; bit++ ) {
                    if ( ( offset & i ) == i ) {
                        set(bit);
                    }
                    i = i * 2;
                }
            } catch (NumberFormatException nfe) {
                error("Can't process address " + destnum);
            }
        }
    }
    
/*
    public void setOffset(java.util.Hashtable labels, int instaddr) {
        if ( destination == null ) {
            System.out.println( "Error: BL with null destination");
            return;
        }
        int offset;
        String destnum = getLabel(destination);
        int destaddr = Integer.parseInt(destnum);
        if ( instaddr < destaddr ) { // foward branch
            offset = (destaddr - (instaddr + 2)); //PC 2 words ahead of inst due to prefetch
        } else {
            offset = ((instaddr + 2) - destaddr);
            offset =~ offset;
            offset++;        // 2's complement
        }
        System.out.println("BL calculated offset = " + Integer.toHexString(offset));
        int i = 1;
        for ( int bit = 0;bit <= 23 ; bit++ ) {
            if ( ( offset & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
 */
}
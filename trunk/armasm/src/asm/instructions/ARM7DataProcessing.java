package asm.instructions;

import java.util.*;


/** Data Processing
 * The data processing instruction is only executed if the condition is true.
 *
 * The instruction produces a result by performing a specified arithmetic or logical
 * operation on one or two operands. The first operand is always a register (Rn).
 *
 * The second operand may be a shifted register (Rm) or a rotated 8 bit immediate value
 * (Imm) according to the value of the I bit in the instruction. The condition codes in the
 * CPSR may be preserved or updated as a result of this instruction, according to the
 * value of the S bit in the instruction.
 * Certain operations (TST, TEQ, CMP, CMN) do not write the result to Rd. They are used
 * only to perform tests and to set the condition codes on the result and always have the
 * S bit set.
 *
 * 4.5.1 CPSR flags
 * The data processing operations may be classified as logical or arithmetic. The logical
 * operations (AND, EOR, TST, TEQ, ORR, MOV, BIC, MVN) perform the logical action
 * on all corresponding bits of the operand or operands to produce the result. If the S bit
 * is set (and Rd is not R15, see below) the V flag in the CPSR will be unaffected, the C
 * flag will be set to the carry out from the barrel shifter (or preserved when the shift
 * operation is LSL #0), the Z flag will be set if and only if the result is all zeros, and the
 * N flag will be set to the logical value of bit 31 of the result.
 *
 * Assembler
 * Mnemonic OpCode  Action
 * -------- ------ ------
 * AND 0000 operand1 AND operand2
 * EOR 0001 operand1 EOR operand2
 * SUB 0010 operand1 - operand2
 * RSB 0011 operand2 - operand1
 * ADD 0100 operand1 + operand2
 * ADC 0101 operand1 + operand2 + carry
 * SBC 0110 operand1 - operand2 + carry - 1
 * RSC 0111 operand2 - operand1 + carry - 1
 * TST 1000 as AND, but result is not written
 * TEQ 1001 as EOR, but result is not written
 * CMP 1010 as SUB, but result is not written
 * CMN 1011 as ADD, but result is not written
 * ORR 1100 operand1 OR operand2
 * MOV 1101 operand2 (operand1 is ignored)
 * BIC 1110 operand1 AND NOT operand2 (Bit clear)
 * MVN 1111 NOT operand2 (operand1 is ignored)
 *
 *
 * Syntax:
 *
 * 1 MOV,MVN (single operand instructions.)
 * <opcode>{cond}{S} Rd,<Op2>
 *
 * 2 CMP,CMN,TEQ,TST (instructions which do not produce a result.)
 * <opcode>{cond} Rn,<Op2>
 *
 * 3 AND,EOR,SUB,RSB,ADD,ADC,SBC,RSC,ORR,BIC
 * <opcode>{cond}{S} Rd,Rn,<Op2>
 *
 * where:
 * <Op2> is Rm{,<shift>} or,<#expression>
 *
 * {cond} is a two-character condition mnemonic.
 *
 * {S} set condition codes if S present (implied for CMP, CMN, TEQ, TST).
 *
 * Rd, Rn and Rm are expressions evaluating to a register number.
 *
 * <#expression> if this is used, the assembler will attempt to generate a shifted
 * immediate 8-bit field to match the expression. If this is
 * impossible, it will give an error.
 *
 * <shift> is <shiftname> <register> or <shiftname> #expression, or
 * RRX (rotate right one bit with extend).
 *
 * <shiftname>s are: ASL, LSL, LSR, ASR, ROR. (ASL is a synonym for LSL,
 * they assemble to the same code.)
 *
 */

public class ARM7DataProcessing extends ARM7Instruction {
    
    /*
      31-28  Condition
      27 = 0
      26 = 0
      25     0 = offset is an immediate value 1 = offset is a register
      24-21  Opcode
      20     0 = don not alter condition code 1 = set condition code
      19-16  1st operand register (Rn)
      15-12  Destination register (Rd)
      11-0   Operand 2
      11-8   Rotate ( immediate mode )
       7-0   immediate value ( immediate mode )
      11-4   Shift applied to offset register ( register mode )
      3-0    Offset register (Rm) ( register mode )
     */
    
    private Vector opcodes;
    
    protected ARM7DataProcessing( ParsedInstruction pi ) {
        super(pi);
        opcodes = pi.getOpcodes();
         /* Certain operations (TST, TEQ, CMP, CMN) do not write the result to Rd. They are used
          * only to perform tests and to set the condition codes on the result and always have the
          * S bit set.
          */
        if ( mneumonic.equals("TST") | mneumonic.equals("TEQ") |
        mneumonic.equals("CMP") | mneumonic.equals("CMN") ) {
            set(20);
        }
        
        if ( ((String)opcodes.firstElement()).endsWith("S") ) {
            set(20);
        }
    }
    
    //<Op2> is Rm{,<shift>} or,<#expression>
    protected void setOp2( String operand ) {
        int r;
        if ( operand.startsWith("#") ) {
            set(25);
            r = parseExpression(operand.substring(1,operand.length()));
            int ror = 0;
            
            if ( ( (r & 0x000000FF) != r ) && ( (~r & 0x000000FF) != ~r ) ) {
                if ( ((r & 0x7FFFFFFF) != r) && mneumonic.startsWith("M") ) {
                    set(new int [] {24,23,22,21}); // MVN 1111 NOT operand2 (operand1 is ignored)
                    r = ~r;
                }
                int shift = 0;
                while( ( r & 0x1 ) == 0 ) {
                    shift++;
                    r = r >>> 1;
                    if( ( shift & 1 ) == 0 ) {
                        if( ( r & 0xFF ) == r ) {
                            break;
                        }
                    }
                }
                if( ( r & 0xFF ) != r ) {
                    error("Immediate " + operand + " out of range for this operation");
                    return;
                }
                ror = ( 32 - shift ) >>> 1;
                if( ( ( shift & 0x1 ) == 1 ) ) {
                    error("Immediate " + operand + " out of range for this operation");
                    return;
                }
            }
            if( (~r & 0x000000FF) == ~r ) {
                set(new int [] {24,23,22,21}); // MVN 1111 NOT operand2 (operand1 is ignored)
                r = ~r;
            }
            // set rotation
            int i = 1;
            for ( int bit = 8; bit <= 11; bit++ ) {
                if ( ( ror & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }          
            
            // set immediate offset
            i = 1;
            for ( int bit = 0; bit <= 7; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
            System.out.println(mneumonic + ": Op2 = " + r);
        } else {
            setRm(operand);
            /*
             try {
                r = getRegisterNumber( operand );
                System.out.println(mneumonic + ": Rm = " + r);
            } catch (NumberFormatException nfe) {
                error(lineNum + " Syntax error " + operand);
                return;
            }
            int i = 1;
            for ( int bit = 0; bit <= 3; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
             */
        }
    }

    protected void setRm(String operand) {
        int r;
        try {
            r = getRegisterNumber( operand );
            System.out.println(mneumonic + ": Rm = " + r);
        } catch (NumberFormatException nfe) {
            error(lineNum + " Syntax error " + operand);
            return;
        }
        int i = 1;
        for ( int bit = 0; bit <= 3; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
    
    /*
     protected void setOp2( String operand ) {
        int r;
        if ( operand.startsWith("#") ) {
            set(25);
            r = parseExpression(operand.substring(1,operand.length()));
            int ror = -1;
            if ( ( r & 0x000000FF ) != r ) {
                //try to create a 8 bit value that can be rotated to equal r
                int n = r;
                ror = -1;
                for ( int rot = 2; rot < 32; rot = rot + 2 ) {
                    int carry = n & 0xC0000000; // grab bits 32 and 31
                    n <<= 2;
                    carry >>>= 30; // move 32 and 31 to 1 and 0
                    n = n | carry;
                    if ( ( n & 0x000000FF ) == n ) {
                        int test = n; // our left rotated number, ROR * rot should yeild original number
                        for ( int rt = 0; rt < rot; rt++ ) {
                            int c = test & 0x1; // grab bit 0
                            test >>>= 1;
                            c <<= 31;
                            test = test | c;
                        }
                        if ( test == r ) {
                            System.out.println( r + " = " + n + " ror " + rot );
                            ror = rot;
                            r = n;
                            break;
                        }
                    }
                }
            }
     
            if ( ror < 0 ) {
                if ( mneumonic.equals("MOV") ) {
                    error("Immediate " + operand + " out of range for this operation");
                    return;
     
                     The following has a bug because it doesn't add the bytes for the
                    operation into the address space so I return with the error for now.
                    Also, per ADS 1.2 Assembler 2.6.1:
     *
     * This a TO DO... NEED TO ADD THIS FUNCTIONALITY
     *
                     You do not need to decide whether to use MOV or MVN. The assembler
                    uses whichever is appropriate. This is useful if the value is an assembly-time variable.
     
     
                    warning("Immediate " + operand + " out of range for this operation");
                    //synthesize a LDR Rd,=op2
                    java.util.Vector newops = new java.util.Vector();
                    newops.addElement("LDR");
                    newops.addElement((String)opcodes.elementAt(1));
                    String l = "=" + operand.substring(1,operand.length());
                    newops.addElement(l);
                    ParsedInstruction pi = new ParsedInstruction(null, lineNum, address, "LDR", newops);
                    java.util.Vector pools = asm.Assembler.getPools();
                    ParsedInstruction pool = (ParsedInstruction)pools.lastElement();
                    java.util.Vector lits = pool.getOpcodes();
                    lits.addElement(operand.substring(1,operand.length()));
                    ARM7LDR ldr = new ARM7LDR(pi);
                    ldr.setPoolAddress(asm.Assembler.getPools(), address);
                    code = ldr.getBitSet();
                    return;
     
} else {
    error("Immediate " + operand + " out of range for this operation");
}
            } else {
                // set rotation
                int i = 1;
                ror = ror/2;
                for ( int bit = 8; bit <= 11; bit++ ) {
                    if ( ( ror & i ) == i ) {
                        set(bit);
                    }
                    i = i * 2;
                }
     
            }
     
            // set immediate offset
            int i = 1;
            for ( int bit = 0; bit <= 7; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
            System.out.println(mneumonic + ": Op2 = " + r);
        } else {
            try {
                r = getRegisterNumber( operand );
                System.out.println(mneumonic + ": Rm = " + r);
            } catch (NumberFormatException nfe) {
                error(lineNum + " Syntax error " + operand);
                return;
            }
            int i = 1;
            for ( int bit = 0; bit <= 3; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
        }
    }
     */
    
    protected void setRd( String rd ) {
        int r = getRegisterNumber( rd );
        System.out.println(mneumonic + ": setRd = " + r);
        int i = 1;
        for ( int bit = 12; bit <= 15; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
    
    protected void setRn( String rn ) {
        int r = getRegisterNumber( rn );
        System.out.println(mneumonic + ": setRn = " + r);
        int i = 1;
        for ( int bit = 16; bit <= 19; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
    
    /*
     
    When the second operand is specified to be a shifted register, the operation of the
    barrel shifter is controlled by the Shift field in the instruction. This field indicates the
    type of shift to be performed (logical left or right, arithmetic right or rotate right). The
    amount by which the register should be shifted may be contained in an immediate field
    in the instruction, or in the bottom byte of another register (other than R15).
     
    Shift amount specified by 5 bit unsigned integer
     
    4=0
     
    6-5 Shift type
        00 = logical left
        01 = logical right
        10 = arithmetic right
        11 = rotate right
     
    11-7  Shift amount (5 bit unsigned integer)
     
     
    Shift amount specified in bottom byte of Rs
     
    4=1
     
    6-5 Shift type
     
    7=0
     
    11-8 Shift register (Rs)
     
     
     */
    protected void setShift( String shift ) {
        shift = shift.toUpperCase();
        if ( shift.startsWith("ASL") || shift.startsWith("LSL") ) {
            clear(6);
            clear(5);
        } else
            if ( shift.startsWith("LSR") ) {
                clear(6);
                set(5);
            } else
                if ( shift.startsWith("ASR") ) {
                    set(6);
                    clear(5);
                } else
                    if ( shift.startsWith("ROR") ) {
                        set(6);
                        set(5);
                    } else {
                        error(lineNum + " Syntax error: " + shift);
                    }
        
        String amount = shift.substring(3,shift.length());
        if ( amount.startsWith("#") ) {
            clear(4);
            int s = parseExpression(amount.substring(1,amount.length()));
            int i = 1;
            for ( int bit = 7; bit <= 11; bit++ ) {
                if ( ( s & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
        } else {
            set(4);
            clear(7);
            int s = getRegisterNumber( amount );
            int i = 1;
            for ( int bit = 8; bit <= 11; bit++ ) {
                if ( ( s & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
        }
    }
    
}
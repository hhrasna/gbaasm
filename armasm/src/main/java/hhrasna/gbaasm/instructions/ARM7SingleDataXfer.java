package hhrasna.gbaasm.instructions;

import java.util.*;


/** Single Data Transfer (LDR,STR)
 *
 * The instruction is only executed if the condition is true.
 *
 * The single data transfer instructions are used to load or store single bytes or words of
 * data. The memory address used in the transfer is calculated by adding an offset to or
 * subtracting an offset from a base register.
 *
 * The result of this calculation may be written back into the base register if auto-indexing
 * is required.
 *
 * Syntax:
 *
 * <LDR|STR>{cond}{B}{T} Rd,<Address>
 *
 * LDR load from memory into a register
 * STR store from a register into memory
 * {cond} two-character condition mnemonic.
 *
 * {B} if B is present then byte transfer, otherwise word transfer
 * {T} if T is present the W bit will be set in a post-indexed instruction, forcing
 * non-privileged mode for the transfer cycle. T is not allowed when a
 * pre-indexed addressing mode is specified or implied.
 *
 * Rd is an expression evaluating to a valid register number.
 * Rn and Rm are expressions evaluating to a register number. If Rn is R15 then the
 * assembler will subtract 8 from the offset value to allow for ARM7TDMI
 * pipelining. In this case base write-back should not be specified.
 *
 * <Address> can be:
 *
 * An expression which generates an address:
 * <expression>
 * The assembler will attempt to generate an instruction using
 * the PC as a base and a corrected immediate offset to address
 * the location given by evaluating the expression. This will be a
 * PC relative, pre-indexed address. If the address is out of
 * range, an error will be generated.
 *
 * A pre-indexed addressing specification:
 * [Rn] offset of zero
 * [Rn,<#expression>]{!} offset of <expression>
 * bytes
 * [Rn,{+/-}Rm{,<shift>}]{!} offset of +/- contents of
 * index register, shifted
 * by <shift>
 *
 * A post-indexed addressing specification:
 * [Rn],<#expression> offset of <expression>
 * bytes
 * [Rn],{+/-}Rm{,<shift>} offset of +/- contents of
 * index register, shifted
 * as by <shift>.
 *
 */
public class ARM7SingleDataXfer extends ARM7Instruction {
    
    /*
      31-28  Condition
      27 = 0
      26 = 1
      25     0 = offset is an immediate value 1 = offset is a register
      24     0 = post; add offset after transfer 1 = pre; add offset before transfer
      23     0 = down; subtract offset from base 1 = up; add offset to base
      22     0 = transfer word 1 = transfer byte
      21     0 = no write-back 1 = write address into base
      20     0 = store into memory 1 = load from memory
      19-16  Base register
      15-12  Source/Destination register
      11-0   Immediate Offset ( immediate mode )
      11-4   Shift applied to offset register
      3-0    Offset register
     */
    
    private String literal = null;
    private int offset = 0; // offset of literal in it's pool. the pool address
    // is added to this during the second pass in setPoolAddress()
    // to calculate the actual address of the literal
    
    protected ARM7SingleDataXfer( ParsedInstruction pi ) {
        super(pi);
        Vector opcodes = pi.getOpcodes();
        
        // unconditional bits
        set(26);
        clear(27);
        
        String cmd = (String)opcodes.firstElement();
        String rd = ((String)opcodes.elementAt( 1 )).toUpperCase();
        String rn = ((String)opcodes.elementAt( 2 ));
        
        String last = ((String)opcodes.lastElement()).toUpperCase();
        
        boolean preindexed = (last.endsWith( "]") || last.endsWith("!") || rn.startsWith("="));
        boolean immediate = false;
        
        // set Rm;
        try {
            int r;
            String rm = ((String)opcodes.elementAt( 3 ));
            //set Up/Down
            if ( rm.startsWith("-") ) {
                clear(23);
            } else {
                set(23);
            }
            if ( rm.startsWith("#") ) {
                immediate = true; 
                String expr = rm.substring(1,rm.length());
                r = parseExpression(expr);
            } else {
                r = getRegisterNumber(rm);
                System.out.println("Rm = " + r);
            }
            System.out.println(mneumonic + ": setRm = " + r);
            int i = 1;
            for ( int bit = 0; bit <= 11; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            immediate = true;
            set(23);
        }
        catch (Exception e) {
            error(lineNum + ": expected Rm or #expression: " + (String)opcodes.elementAt( 3 ) );
        }
        
        //check for B or T
        if ( cmd.endsWith("B") ) {
            set(22); //set B bit 22
        } else if ( cmd.endsWith("T") ) {
            if( immediate || preindexed || opcodes.size() < 3) {
                throw new java.lang.RuntimeException("T only allowed with post indexed intstruction");
            } else {
                set(21); // set W bit 21
            }
        }
        
        // set Rd;
        try {
            
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
        catch (Exception e) {
            System.out.println("ARM7SingleDataXfer: " + e );
        }
        
        if ( preindexed ) {
            set(24);
        }
        
        if ( !immediate ) {
            set(25);
        }
        // set Rn;
        try {
            
            int r = getRegisterNumber( rn );
            System.out.println(mneumonic + ": setRn = " + r);
            int i = 1;
            for ( int bit = 16; bit <= 19; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
            
        } catch (NumberFormatException e) {
            // set Rn to PC;
            set( new int [] {16,17,18,19} );
            
            //parse Rn as literal or label
            System.out.println("Base address " + rn);
            if ( rn.startsWith("=") ) {
                //set literal
                literal = rn.substring( 1, rn.length() );
                
            } else {
                int pc = pi.getAddress() + 8;
                int destaddr = parseExpression( rn );
                if ( destaddr  > pc ) {
                    setOffset( destaddr - pc );
                    set(23);
                } else {
                    setOffset( pc - destaddr );
                    clear(23);
                }
            }
        }
        
        //set shift
        if ( opcodes.size() >= 5 ) {
            String shift = (String)opcodes.elementAt(4);
            if ( shift.indexOf("]") >= 0 ) {
                String tmp = shift;
                shift = tmp.substring(0,tmp.indexOf("]"));
            }
            System.out.println(cmd + " shift = " + shift);
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
                            error("Syntax error: " + shift);
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
                error("Shift amount must be an immediate value or expression: " + shift);
            }
        }
        
    }
    
    public void setPoolAddress(Vector pools, int instaddr) {
        if ( literal == null ) {
            return;
        }
        int pooladdr = 0;
        Enumeration en = pools.elements();
        while ( en.hasMoreElements() ) {
            ParsedInstruction pi = (ParsedInstruction)en.nextElement();
            Vector lp = pi.getOpcodes();
            if ( lp.contains(literal) ) {
                pooladdr = pi.getAddress();
                offset = (lp.indexOf(literal) * 4);
                break;
            }
        }
        if ( pooladdr == 0 ) {
            error( "Literal not found " + literal );
        }
        int litaddr = pooladdr + offset;
        
        if ( litaddr < instaddr ) {
            clear(23);
            offset = instaddr - litaddr;
        } else {
            set(23);
            offset = litaddr - instaddr - 8;
        }
        setOffset(offset);
    }
    
    private void setOffset( int offset ) {
        int i = 1;
        for ( int bit = 0;bit <= 11 ; bit++ ) {
            if ( ( offset & i ) == i ) {
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
    in the instruction.
         
    Shift amount specified by 5 bit unsigned integer
         
    4=0
         
    6-5 Shift type
        00 = logical left
        01 = logical right
        10 = arithmetic right
        11 = rotate right
         
    11-7  Shift amount (5 bit unsigned integer)
         
         
         */
    protected void setShift( String shift ) {
        shift = shift.toUpperCase();
        System.out.println("SingleDataXfer shift = " + shift);
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
                        error("Syntax error: " + shift);
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
            error("Shift amount must be an immediate value or expression: " + shift);
        }
    }
}
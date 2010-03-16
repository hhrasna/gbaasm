package asm.instructions;

import java.util.*;


/** Halfword and Signed Data Transfer
 * (LDRH/STRH/LDRSB/LDRSH)
 * The instruction is only executed if the condition is true.
 *
 * These instructions are used to load or store half-words of data and also load
 * sign-extended bytes or half-words of data. The memory address used in the transfer
 * is calculated by adding an offset to or subtracting an offset from a base register. The
 * result of this calculation may be written back into the base register if auto-indexing is
 * required.
 *
 *
 * Syntax:
 *
 * <LDR|STR>{cond}<H|SH|SB> Rd,<address>
 *
 * LDR load from memory into a register
 * STR Store from a register into memory
 *
 * {cond} two-character condition mnemonic.
 *
 * H Transfer halfword quantity
 *
 * SB Load sign extended byte (Only valid for LDR)
 *
 * SH Load sign extended halfword (Only valid for LDR)
 *
 * Rd is an expression evaluating to a valid register number.
 *
 * <address> can be:
 * 1 An expression which generates an address:
 *
 * <expression>
 * The assembler will attempt to generate an instruction using
 * the PC as a base and a corrected immediate offset to address
 * the location given by evaluating the expression. This will be a
 * PC relative, pre-indexed address. If the address is out of
 * range, an error will be generated.
 *
 * 2 A pre-indexed addressing specification:
 *
 * [Rn] offset of zero
 * [Rn,<#expression>]{!} offset of <expression> bytes
 * [Rn,{+/-}Rm]{!} offset of +/- contents of
 * index register
 *
 * 3 A post-indexed addressing specification:
 *
 * [Rn],<#expression> offset of <expression> bytes
 * [Rn],{+/-}Rm offset of +/- contents of index register.
 *
 * Rn and Rm are expressions evaluating to a register number.
 * If Rn is R15 then the assembler will subtract 8 from the offset
 * value to allow for ARM7TDMI pipelining. In this case base
 * write-back should not be specified.
 *
 * {!} writes back the base register (set the W bit) if ! is present.
 *
 */
public class ARM7HalfwordXfer extends ARM7Instruction {
    
    /*
      31-28  Condition
      27 = 0
      26 = 0
      25 = 0
      24     0 = post; add offset after transfer 1 = pre; add offset before transfer
      23     0 = down; subtract offset from base 1 = up; add offset to base
      22     0 = register offset 1 = immediate offset
      21     0 = no write-back 1 = write address into base
      20     0 = store into memory 1 = load from memory
      19-16  Base register
      15-12  Source/Destination register
      11-8   Immediate Offset high nibble ( immediate mode )
      11-8 = 0 (register mode)
      7 = 1
      6-5    S,H 00 = SWP instruction, 01 = Unsigned halfwords,
                 10 = Signed byte,     11 = Signed halfwords
      4 = 1
      3-0    Offset register (register mode)
      3-0    Immediate offset low nibble (immediate mode)
     */
    
    protected ARM7HalfwordXfer( ParsedInstruction pi ) {
        super(pi);
        Vector opcodes = pi.getOpcodes();
        
        // unconditional bits
        clear(27);
        clear(26);
        clear(25);
        set(7);
        set(4);
        
        String cmd = (String)opcodes.firstElement();
        String rd = ((String)opcodes.elementAt( 1 )).toUpperCase();
        String rn = ((String)opcodes.elementAt( 2 ));
        
        String last = ((String)opcodes.lastElement()).toUpperCase();
        
        boolean preindexed = (last.endsWith( "]") || last.endsWith("!"));
        boolean immediate = false;
        
        // set Rm;
        try {
            int r;
            String rm = ((String)opcodes.elementAt( 3 ));
            clear(22);
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
            for ( int bit = 0; bit <= 3; bit++ ) {
                if ( ( r & i ) == i ) {
                    set(bit);
                }
                i = i * 2;
            }
            if ( immediate ) {
                //high bit
                for ( int bit = 8;bit <= 11 ; bit++ ) {
                    if ( ( r & i ) == i ) {
                        set(bit);
                    }
                    i = i * 2;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException ae) {
            immediate = true;
            set(23);
        }
        catch (Exception e) {
            error(lineNum + ": expected Rm or #expression: " + (String)opcodes.elementAt( 3 ) );
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
        
        if ( immediate ) {
            set(22);
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
            
        } catch (Exception e) {
            // set Rn to PC;
            set( new int [] {16,17,18,19} );
            
            //parse Rn as literal or label
            System.out.println("Base literal " + rn);
            setOffset(parseExpression( rn ));
        }
    }
    
    private void setOffset( int offset ) {
        int i = 1;
        //low nibble
        for ( int bit = 0;bit <= 3 ; bit++ ) {
            if ( ( offset & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
        //high nibble
        for ( int bit = 8;bit <= 11 ; bit++ ) {
            if ( ( offset & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
}
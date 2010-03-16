package asm.instructions;

import java.util.*;
import asm.Assembler;


/** Encapsulates the fields common to all ARM7 Instructions
 * @author Hans Hrasna
 */
public class ARM7Instruction implements Instruction {
    
    protected BitSet code = new BitSet(32); //the machine code for this instruction
    protected String condition;
    protected String mneumonic;
    protected int address;
    protected int lineNum;
    
    public ARM7Instruction( ParsedInstruction pi ) {
        Vector opcodes = pi.getOpcodes();
        mneumonic = pi.getMneumonic();
        address = pi.getAddress();
        lineNum = pi.getLineNum();
        
        // Set the condition code for this Instruction (bits 31 - 28)
        String command = (String)opcodes.firstElement();
        if ( mneumonic.length() == command.length() ) {
            set(new int [] {31,30,29}); // Always
            return;
        }
        
        try {
            condition = command.substring(mneumonic.length(), mneumonic.length() + 2).toUpperCase();
        } catch (StringIndexOutOfBoundsException e) {
            condition = "AL"; //default to always
        }
        
        if ( condition.equals("EQ") ) { // Z set - equal
            // 0000
        } else
            
            if ( condition.equals("NE") ) { // Z clear - not equal
                set(28);
            } else
                
                if ( condition.equals("CS") ) { // C set - unsigned higher or same
                    set(29);
                } else
                    
                    if ( condition.equals("CC") ) { // C clear - unsigned lower
                        set(new int [] {29,28});
                    } else
                        
                        if ( condition.equals("MI") ) { // N set - negative
                            set(30);
                        } else
                            
                            if ( condition.equals("PL") ) { // N clear - positive or zero
                                set(new int [] {30,28});
                            } else
                                
                                if ( condition.equals("VS") ) { // V set - overflow
                                    set(new int [] {30, 29});
                                } else
                                    
                                    if ( condition.equals("VC") ) { // V clear - no overflow
                                        set(new int [] {30,29,28});
                                    } else
                                        
                                        if ( condition.equals("HI") ) { // C set and Z clear - unsigned higher
                                            set(31);
                                        } else
                                            
                                            if ( condition.equals("LS") ) { // C clear and Z set - unsigned lower or same
                                                set(new int [] {31,28});
                                            } else
                                                
                                                if ( condition.equals("GE") ) { // N equals V - greater or equal
                                                    set(new int [] {31,29});
                                                } else
                                                    
                                                    if ( condition.equals("LT") ) { // N not equal to V - less than
                                                        set(new int [] {31,29,28});
                                                    } else
                                                        
                                                        if ( condition.equals("GT") ) { // Z clear AND (N equals V) - greater than
                                                            set(new int [] {31,30});
                                                        } else
                                                            
                                                            if ( condition.equals("LE") ) { // Z set OR (N not equal to V) - less than or equal
                                                                set(new int [] {31,30,28});
                                                            } else
                                                                
                                                                if ( condition.equals("AL") ) { // (ignored) - always
                                                                    set(new int [] {31,30,29});
                                                                } else
                                                                    
                                                                    if ( condition.equals("NE") ) { // (ignored) - never
                                                                        set(new int [] {31,30,29,28});
                                                                    } else
                                                                        
                                                                    {
                                                                        set(new int [] {31,30,29}); // Default (ignored) - always
                                                                    }
    }
    
    public int getMachineCode() {
        int bits2int = 0;
        for( int i=0; i<32;i++) {
            bits2int = bits2int | (code.get(i) ? 1:0) << i;
        }
        return bits2int;
    }
    
    public BitSet getBitSet() {
        return code;
    }
    
    protected void set(int [] bits) {
        for ( int i=0; i< bits.length; i++ ) {
            code.set(bits[i]);
        }
    }
    
    protected void set( int bit ) {
        code.set(bit);
    }
    
    protected void clear( int bit ) {
        code.clear(bit);
    }
    
    protected int getRegisterNumber(String opcode) throws NumberFormatException {
       return Util.getRegisterNumber(opcode);
    }
    
    protected int parseExpression( String expr ) {
        return Assembler.parseExpression( expr );
    }
    
    protected void error(String msg) {
        Assembler.error(msg);
    }
    
    protected void warning(String msg) {
        Assembler.warning(msg);
    }
    
    protected int addLiteral(String l) {
        return Assembler.addLiteral(l);
    }
    
    protected String getLabel(String label) {
        return Assembler.getLabel(label);
    }
    
}
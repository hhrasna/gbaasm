/*
 * ARM7Coprocessor.java
 *
 * Created on December 4, 2002, 10:29 AM
 */

package asm.instructions;

/**
 *
 * @author  hans
 *
 * Implements the Coprocessor settings common to all coprocessor instructions
 */
public class ARM7Coprocessor extends ARM7Instruction {
    
    /** Creates a new instance of ARM7Coprocessor */
    public ARM7Coprocessor(ParsedInstruction pi) {
        super(pi);
    }
    
    protected void setCRm(String operand) {
        int r;
        try {
            r = getRegisterNumber( "R" + operand.substring(1,operand.length()) );
            System.out.println(mneumonic + ": CRm = " + r);
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
    
    //set CP#
    public void setCPNumber(String operand) {
        if( ! (operand.startsWith("p")) ) {
            error("Coprocessor number must be specified with 'p#' -> " + operand);
            return;
        }
        int r = getRegisterNumber("R" + operand.substring(1,operand.length()));
        int i = 1;
        for ( int bit = 8; bit <= 11; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
    
    //set CPInfo
    public void setCPInfo(String operand) {
        int r = parseExpression(operand);
        int i = 1;
        for ( int bit = 5; bit <= 7; bit++ ) {
            if ( ( r & i ) == i ) {
                set(bit);
            }
            i = i * 2;
        }
    }
    
    protected void setCRd( String crd ) {
        setRd("R" + crd.substring(1,crd.length()));
    }
    
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
    
    protected void setCRn( String crn ) {
        setRd("R" + crn.substring(1,crn.length()));
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
}

/* Copyright 2001 Sun Microsystems, Inc. All Rights Reserved. */

package hhrasna.gbaasm.instructions;

public class Util {

    static int getRegisterNumber(String opcode) throws NumberFormatException {
        String numbers = "0123456789";
        
        int rindex = opcode.toUpperCase().indexOf("R");
        if ( rindex < 0 ) {
            throw new NumberFormatException();
        }
        String rnum = null; 
        
        try {
            rnum = opcode.substring(rindex + 1, rindex + 2);
            if (numbers.indexOf(opcode.charAt(rindex + 2)) >= 0 ) {
                rnum = rnum + opcode.substring(rindex + 2, rindex + 3);
            }
        } catch (StringIndexOutOfBoundsException e ) {
        }    
        return Integer.parseInt(rnum);
    }

}

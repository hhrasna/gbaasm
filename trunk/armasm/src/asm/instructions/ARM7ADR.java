package asm.instructions;

import java.util.Vector;

/**
ADR ARM pseudo-instruction
Load a program-relative or register-relative address into a register.

Syntax
ADR{cond} register,expr

where:
cond is an optional condition code.

register is the register to load.

expr is a program-relative or register-relative expression that evaluates to:
¥ a non word-aligned address within ±255 bytes
¥ a word-aligned address within ±1020 bytes.

More distant addresses can be used if the alignment is 16 bytes or more.
The address can be either before or after the address of the instruction or
the base register
**/

public class ARM7ADR extends ARM7Instruction {

    private Instruction i; 
    public ARM7ADR(ParsedInstruction pi) {
        super(pi);
        Vector opcodes = pi.getOpcodes();
        String rd = (String)opcodes.elementAt(1);
        int destaddr = parseExpression( (String)opcodes.elementAt(2) );
        int pc = pi.getAddress() + 8;
        /* 
            start MOV r0,#10
            ADR r4,start ; => SUB r4,pc,#0xc
         */
        String offset;
        String cmd;
        String mneumonic;
        if ( destaddr < pc ) {
            offset = "#" + String.valueOf(pc - destaddr);
            mneumonic = "SUB";
        } else {
            offset = "#" + String.valueOf(destaddr - pc);
            mneumonic = "ADD";
        }
        cmd = mneumonic + condition;
        opcodes = new Vector();
		opcodes.addElement(cmd);
		opcodes.addElement(rd);
		opcodes.addElement("r15");
		opcodes.addElement(offset);
		if ( mneumonic.equals("SUB") ) {
		    i = new ARM7SUB(new ParsedInstruction(pi.getHandler(), pi.getLineNum(), 
			    pi.getAddress(), mneumonic, opcodes));
		} else {
		    i = new ARM7ADD(new ParsedInstruction(pi.getHandler(), pi.getLineNum(), 
			    pi.getAddress(), mneumonic, opcodes));
		}
            
	}

    public int getMachineCode() {
        return i.getMachineCode();
	}
}        
            
        
package hhrasna.gbaasm.instructions;

import java.util.BitSet;
import hhrasna.gbaasm.Assembler;

public class Thumb7Instruction implements Instruction {
    protected BitSet code;

    public Thumb7Instruction(ParsedInstruction pi) {
        code = new BitSet(16);
    }

    public int getMachineCode() {
        int bits2int = 0;
        for (int i = 0; i < 16; i++)
            bits2int |= (code.get(i) ? 1 : 0) << i;
        return bits2int;
    }

    public BitSet getBitSet() {
        return code;
    }

    protected void set(int[] bits) {
        for (int i = 0; i < bits.length; i++)
            code.set(bits[i]);
    }

    protected void set(int bit) {
        code.set(bit);
    }

    protected void clear(int bit) {
        code.clear(bit);
    }

    protected int getRegisterNumber(String opcode) throws NumberFormatException {
        return Util.getRegisterNumber(opcode);
    }

    protected int parseExpression(String expr) {
        return Assembler.parseExpression(expr);
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

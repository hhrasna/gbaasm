package hhrasna.gbaasm.instructions;

public class Thumb7Format1 extends Thumb7Instruction {
    public Thumb7Format1(ParsedInstruction pi) {
        super(pi);
        java.util.Vector opcodes = pi.getOpcodes();
        int rd = getRegisterNumber((String) opcodes.elementAt(1));
        int rs = getRegisterNumber((String) opcodes.elementAt(2));
        int offset;
        try {
            offset = parseExpression(((String) opcodes.elementAt(3)).substring(1));
        } catch (ArrayIndexOutOfBoundsException  ae) {
            offset = 0;
        } catch (Exception e) {
            error(e.toString());
        }
    }
}

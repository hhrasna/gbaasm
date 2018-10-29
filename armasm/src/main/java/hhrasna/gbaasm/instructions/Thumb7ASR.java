package hhrasna.gbaasm.instructions;

public class Thumb7ASR extends Thumb7Format1 {
    public Thumb7ASR(ParsedInstruction pi) {
        super(pi);
        set(12);
        set(11);
    }
}

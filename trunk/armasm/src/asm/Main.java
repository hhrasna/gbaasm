package asm;

import java.io.File;
import java.util.Hashtable;

public class Main {

    public static void main(String [] args) {
         
        if ( args.length < 1 ) {
            System.out.println("usage: asm <filename>");
            System.exit(0);
        }
        try {
            File file = new File(args[0]);
            Assembler as = new Assembler(file);
            as.assemble();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
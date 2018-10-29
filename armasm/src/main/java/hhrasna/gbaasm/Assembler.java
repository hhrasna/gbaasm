package hhrasna.gbaasm;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import hhrasna.gbaasm.instructions.Instruction;
import hhrasna.gbaasm.instructions.ARM7SingleDataXfer;
import hhrasna.gbaasm.instructions.ParsedInstruction;
import org.nfunk.jep.JEP;

public class Assembler {
    
    private BufferedReader fileReader;
    private static Hashtable labels = new Hashtable();    //key = String name, value = Long value
    private static Vector literalPool = new Vector();     //the current pool
    private static Vector pools = new Vector();           //vector of ParsedInstructions which are LTORG
    
    private Vector instructions = new Vector();
    
    private File file;
    private static int errors = 0;
    private static int warnings = 0;
    private String [] cc =
    new String [] {"EQ","NE","CS","CC","MI","PL","VS","VC","HI","LS","GE","LT","GT","LE","AL"};
    private static JEP myParser = new JEP();

    public Assembler(File file) throws FileNotFoundException {
        fileReader = new BufferedReader(new FileReader(file));
        this.file = file;
    }
    
    public static int addLiteral(String literal) {
        if ( ! literalPool.contains(literal) ) {
            literalPool.addElement(literal);
        }
        return literalPool.indexOf(literal) * 4;
    }
    
    public static void error(String msg) {
        errors++;
        System.out.println("ERROR: " + msg);
    }
    
    public static void warning(String msg) {
        warnings++;
        System.out.println("WARNING: " + msg);
    }
    
    public static String getLabel(String label) {
        return (String)labels.get(label);
    }
    
    public static Vector getPools() {
        return pools;
    }
    
    /*
     parse a program relative or register relative (MAP) expression
     */
    public static int parseExpression( String expr ) {
        System.out.println("parseExpression " + expr );
        try {
            return (Integer.decode( expr )).intValue();
        } catch ( NumberFormatException nfe ) {
        }

        String delimeters = "[]()+-*/%~&|^";
        StringTokenizer st = new StringTokenizer(expr, delimeters, true);
        StringBuffer eval = new StringBuffer();
        while ( st.hasMoreTokens() ) {
            String operand = st.nextToken();
            if ( delimeters.indexOf(operand) >= 0 ) {
                if ( !operand.equals("[") && !operand.equals("]") ) {
                    System.out.println("operand: " + operand);
                    eval.append(operand);
                }
                continue;
            }
            String value = getLabel( operand );
            if ( value != null ) {
                
                try {
                    Integer i = Integer.decode( value );
                    System.out.println("parseExpression variable : " + operand + " = " + i);
                    myParser.addVariable( operand , i.intValue() );
                    eval.append(operand);
                    
                } catch( NumberFormatException nfe ) {
                    int i = parseExpression(value);
                    System.out.println("parseExpression variable : " + operand + " = " + i);
                    myParser.addVariable( operand , i );
                    eval.append(operand);
                    //error("parseExpression: " + nfe.toString());
                }
            } else {
                System.out.print(operand + " -> ");
                if ( operand.startsWith("$")) {
                    operand = "0x" + operand.substring(1,operand.length());
                } else if ( operand.startsWith("0") && !( operand.startsWith("0x") || operand.startsWith("0X") ) ) {
                    operand = "0x" + operand;
                }
                System.out.println(operand);
                try {
                    int i = (Long.decode( operand )).intValue();
                    eval.append(i);
                } catch ( NumberFormatException fe ) {
                    error("Unable to expand expression: " + expr);
                }
            }
        }
        
        myParser.parseExpression( eval.toString() );
        int val = (int)myParser.getValue();
        System.out.println( "parseExpression: " + eval + "=" + val);
        return val;
        
    }
    
    void assembleLiteralPool(Vector lp, DataOutputStream dos) throws IOException {
        Enumeration le = lp.elements();
        while (le.hasMoreElements() ) {
            String label = (String)le.nextElement();
            System.out.println( "Processing literal " + label );
            String n = getLabel(label);
            int i;
            if ( n != null ) {
                i = parseExpression( n );
            } else {
                i = parseExpression( label );
            }
            System.out.println( label + " = " + i );
            dos.write(i);
            dos.write(i >> 8);
            dos.write(i >> 16);
            dos.write(i >> 24);
        }
    }
    
    public synchronized void assemble() {
        int lineNum = 1;
        int address = 0;
        Stack labelStack = new Stack();
        String label = null;
        
        try {
            while (fileReader.ready()) {
                String currentLine = fileReader.readLine();
                
                if ( currentLine.startsWith("\n") || currentLine.startsWith(";") ) {
                    lineNum++;
                    continue;
                }
                
                StringTokenizer st = new StringTokenizer(currentLine);
                if ((!(currentLine.startsWith(" ") || currentLine.startsWith("\t"))) && st.hasMoreTokens() ) {
                    label = st.nextToken();
                    labelStack.push(label);
                    if (labels.containsKey(label) ) {
                        warning(lineNum + " Warning: label redefined -> " + label);
                    }
                }
                
                if ( st.hasMoreTokens() ) {
                    Vector opcodes = new Vector();
                    StringBuffer sb = new StringBuffer();
                    String cmd = st.nextToken().toUpperCase();
                    
                    if ( cmd.startsWith(";") ) {
                        lineNum++;
                        continue;
                    }
                    
                    if ( cmd.equals("EQU") || cmd.equals("*") ) {
                        String value = "";
                        while( st.hasMoreTokens() ) {
                            String t = st.nextToken();
                            if ( t.startsWith(";") ) {
                                break;
                            }
                            value = value + t;
                        }
                        labels.put( (String)labelStack.pop(), value );
                        lineNum++;
                        continue;
                    } else
                        if ( cmd.equals("LTORG") ) {
                            ParsedInstruction pi = new ParsedInstruction(null, lineNum, address, cmd, literalPool);
                            instructions.addElement(pi);
                            pools.addElement(pi);
                            address = address + (literalPool.size() * 4);
                            literalPool = new Vector();
                            lineNum++;
                            continue;
                        }
                    
                    opcodes.addElement(cmd);
                    while ( !labelStack.empty() ) {
                        String a = Integer.toString(address);
                        labels.put( (String)labelStack.pop(), a );
                    }
                    
                    // remove remaining spaces
                    String tmp;
                    while( st.hasMoreTokens() ) {
                        tmp = st.nextToken();
                        if ( tmp.startsWith(";") ) {
                            break;
                        }
                        sb.append(tmp);
                    }
                    
                    String s = sb.toString();
                    if (s.indexOf(",") < 0 ) {
                        opcodes.addElement( s );
                    } else {
                        st = new StringTokenizer(s,",");
                        while(st.hasMoreTokens()) {
                            tmp = st.nextToken();
                            opcodes.addElement( tmp );
                        }
                    }
                    
                    //process LDR Rd,=constant pseudo instruction
                    if ( cmd.startsWith("LDR") && ((String)opcodes.lastElement()).startsWith("=") ) {
                        String lit = (String)opcodes.lastElement();
                        addLiteral( lit.substring( 1, lit.length() ) );
                    }
                    
                    //DCD
                    if ( cmd.equals("DCD") || cmd.equals("&") ) {
                        instructions.addElement(new ParsedInstruction(null, lineNum, address, cmd, opcodes));
                        System.out.println(Integer.toHexString(address) + ": " +
                        "    " + currentLine + "   " + opcodes);
                        address = address + ((opcodes.size() -1) * 4);
                        lineNum++;
                        continue;
                    }
                    
                    //Set InstructionHandler for ARM7 Instructions
                    Class instructionHandler = null;
                    try {
                        //strip condition code
                        for( int x=0; x < cc.length; x++ ) {
                            try {
                                if( cmd.substring( 3, 5 ).equals( cc[x] ) ) {
                                    tmp = cmd;
                                    cmd = tmp.substring(0,3) + tmp.substring(5,tmp.length());
                                    break;
                                }
                            } catch (StringIndexOutOfBoundsException sob) {
                            }
                        }
                        int c = cmd.length();
                        //System.out.println("Lookup InstructionHandler for " + cmd);
                        String mneumonic;
                        while(c > 0) {
                            mneumonic = cmd.substring(0,c--).toUpperCase();
                            try {
                                instructionHandler = Class.forName("hhrasna.asm.instructions.ARM7" + mneumonic );
                            }
                            catch (ClassNotFoundException cnf) {
                                //System.out.println(cnf);
                            }
                            catch (Exception e1) {
                                System.out.println(e1);
                            }
                            if ( instructionHandler != null ) {
                                System.out.println(Integer.toHexString(address) + ": " +
                                "    " + currentLine + "   " + opcodes);
                                instructions.addElement(new ParsedInstruction(instructionHandler, lineNum, address, mneumonic, opcodes));
                                address = address + 4;
                                break;
                            }
                        }
                        if ( instructionHandler == null ) {
                            //didn't find a class to handle the cmd
                            error("line: " + lineNum + " Syntax error, unknown command:" + currentLine);
                        }
                        
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                lineNum++;
            }
            if ( errors > 0 ) {
                System.out.println( errors + " Errors");
                return;
            }
            // if there are any unprocessed literals, put them at the end.
            if ( !literalPool.isEmpty() ) {
                ParsedInstruction pi = new ParsedInstruction(null, lineNum, address, "LTORG", literalPool);
                instructions.addElement(pi);
                pools.addElement(pi);
            }
            
            // ******** Second pass *********
            
            Enumeration e = instructions.elements();
            int ia = 0;
            String origfile = file.getName();
            String assemfile = origfile.substring(0,origfile.indexOf(".")) + ".gba";
            System.out.println( "Writing to: " + assemfile );
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(assemfile)));
            while ( e.hasMoreElements() ) {
                Instruction a7i;
                ParsedInstruction pi = (ParsedInstruction)e.nextElement();
                if ( pi.getMneumonic().equals("LTORG") ) {
                    Vector lp = pi.getOpcodes();
                    assembleLiteralPool( lp, dos );
                    ia = ia + ( lp.size() * 4 );
                    continue;
                } else
                    if ( pi.getMneumonic().equalsIgnoreCase("DCD") || pi.getMneumonic().equals("&") ) {
                        Vector data = pi.getOpcodes();
                        Enumeration en = data.elements();
                        en.nextElement(); // skip cmd
                        int radix = 10;
                        while ( en.hasMoreElements() ) {
                            String operand = (String)en.nextElement();
                            if ( operand.startsWith("$") ) {
                                System.out.print(ia + ": ");
                                operand = operand.substring(1,operand.length());
                                radix = 16;
                                System.out.println(operand);
                            } else if (operand.startsWith("0x")) {
                                System.out.print(ia + ": ");
                                operand = operand.substring(2,operand.length());
                                radix = 16;
                                System.out.println(operand);
                            } else if (operand.startsWith("0")) {
                                System.out.print(ia + ": ");
                                //operand = operand.substring(1,operand.length());
                                radix = 8;
                                System.out.println(operand);
                            }

                            try {
                                int i = (Long.valueOf( operand, radix )).intValue();
                                dos.write(i);
                                dos.write(i >> 8);
                                dos.write(i >> 16);
                                dos.write(i >> 24);
                                ia = ia + 4;
                            } catch ( NumberFormatException fe ) {
                                error("Unable to expand expression: " + operand);
                            }
                        }
                        continue;
                    }
                
                Class instructionHandler = pi.getHandler();
                System.out.println("Handler: " + instructionHandler);
                try {
                    Class [] parameterTypes = new Class [] {pi.getClass()};
                    Constructor constructor = instructionHandler.getConstructor(parameterTypes);
                    a7i = (Instruction)constructor.newInstance(new Object [] { pi });
                } catch (Exception ex) {
                    error("Assem: " + ex);
                    break;
                }
                
                if ( a7i instanceof ARM7SingleDataXfer ) {
                    //set literal pool address
                    ((ARM7SingleDataXfer)a7i).setPoolAddress(pools, ia);
                }
                
                System.out.println(Integer.toHexString(ia) + ": " +
                Integer.toHexString(a7i.getMachineCode()));
                int ic = (a7i.getMachineCode());
                dos.write(ic);
                dos.write(ic >> 8);
                dos.write(ic >> 16);
                dos.write(ic >> 24);
                ia = ia + 4;
            }
            dos.close();
        } catch (Exception e) {
            error(e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println( errors + " Errors" );
        System.out.println( warnings + " Warnings" );
        System.out.println( "Done." );
    }
    
}







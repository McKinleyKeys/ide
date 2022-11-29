

package Languages;

import Compiler.Compiler;
import Languages.Util.Method;
import Languages.Util.Operation;
import Languages.Util.Variable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Redstone extends Language {
    
    public static final String name = "Redstone";
    
    private ArrayList<String> wordTypes = new ArrayList<>(Arrays.asList("int", "type", "var", "method"));
    private ArrayList<String> valTypes = new ArrayList<>(Arrays.asList("int"));
    
    private ArrayList<Operation> operations = new ArrayList<Operation>() {{
        add(Operation.getAddOp()); add(Operation.getPosOp()); add(Operation.getSubOp()); add(Operation.getNegOp());
        add(Operation.getMulOp()); add(Operation.getPowOp()); add(Operation.getDivOp()); add(Operation.getModOp());
        add(Operation.getBitwiseNotOp()); add(Operation.getBitwiseOrOp()); add(Operation.getBitwiseAndOp()); add(Operation.getBitwiseXorOp());
        add(Operation.getBitwiseNorOp()); add(Operation.getBitwiseNandOp()); add(Operation.getBitwiseXnorOp());
        add(Operation.getShiftLeftOp()); add(Operation.getShiftRightOp()); add(Operation.getShiftRightUnsignedOp());
    }};
    private HashMap<String, Method> methods = new HashMap<String, Method>() {{
        put("print", new Method("print", new ArrayList<>(Arrays.asList("int")), "void"));
        put("println", new Method("println", new ArrayList<>(Arrays.asList("int")), "void"));
        put("exit", new Method("exit", new ArrayList<>(Arrays.asList("int")), "void"));
//        put("pow", new Method("pow", new ArrayList<>(Arrays.asList("int", "int")), "int"));
    }};
    
    private Variable ramCell0 = new Variable("com.mem.ramCell0", "int", "0");
    private Variable ramCell1 = new Variable("com.mem.ramCell1", "int", "0");
    private Variable ramCell2 = new Variable("com.mem.ramCell2", "int", "0");
    private Variable ramCell3 = new Variable("com.mem.ramCell3", "int", "0");
    private Variable ramCell4 = new Variable("com.mem.ramCell4", "int", "0");
    private Variable ramCell5 = new Variable("com.mem.ramCell5", "int", "0");
    private Variable ramCell6 = new Variable("com.mem.ramCell6", "int", "0");
    private Variable ramCell7 = new Variable("com.mem.ramCell7", "int", "0");
    private final ArrayList<Variable> ramCells = new ArrayList<Variable>() {{
        add(ramCell0);    add(ramCell1);    add(ramCell2);    add(ramCell3);
        add(ramCell4);    add(ramCell5);    add(ramCell6);    add(ramCell7);
    }};
    
    private final Variable romCell0 = new Variable("com.mem.romCell0", "int", "0");
    private final Variable romCell1 = new Variable("com.mem.romCell1", "int", "1");
    private final Variable romCell2 = new Variable("com.mem.romCell2", "int", "9");
    private final Variable romCell3 = new Variable("com.mem.romCell3", "int", "314159265");
    private final Variable romCell4 = new Variable("com.mem.romCell4", "int", "271828183");
    private final Variable romCell5 = new Variable("com.mem.romCell5", "int", "100");
    private final Variable romCell6 = new Variable("com.mem.romCell6", "int", "0");
    private final Variable romCell7 = new Variable("com.mem.romCell7", "int", "0");
    private final ArrayList<Variable> romCells = new ArrayList<Variable>() {{
        add(romCell0);    add(romCell1);    add(romCell2);    add(romCell3);
        add(romCell4);    add(romCell5);    add(romCell6);    add(romCell7);
    }};
    
    private Variable mmCell0 = new Variable("com.mem.mmCell0", "int", "0");
    private Variable mmCell1 = new Variable("com.mem.mmCell1", "int", "0");
    private Variable mmCell2 = new Variable("com.mem.mmCell2", "int", "0");
    private Variable mmCell3 = new Variable("com.mem.mmCell3", "int", "0");
    private Variable mmCell4 = new Variable("com.mem.mmCell4", "int", "0");
    private Variable mmCell5 = new Variable("com.mem.mmCell5", "int", "0");
    private Variable mmCell6 = new Variable("com.mem.mmCell6", "int", "0");
    private Variable mmCell7 = new Variable("com.mem.mmCell7", "int", "0");
    private final ArrayList<Variable> mmCells = new ArrayList<Variable>() {{
        add(mmCell0);    add(mmCell1);    add(mmCell2);    add(mmCell3);
        add(mmCell4);    add(mmCell5);    add(mmCell6);    add(mmCell7);
    }};
    
    private Variable register0 = new Variable("com.cpu.alu.register0", "int", "0");
    private Variable register1 = new Variable("com.cpu.alu.register1", "int", "0");
    private Variable register2 = new Variable("com.cpu.alu.register2", "int", "0");
    private Variable register3 = new Variable("com.cpu.alu.register3", "int", "0");
    private Variable register4 = new Variable("com.cpu.alu.register4", "int", "0");
    private Variable register5 = new Variable("com.cpu.alu.register5", "int", "0");
    private Variable register6 = new Variable("com.cpu.alu.register6", "int", "0");
    private Variable register7 = new Variable("com.cpu.alu.register7", "int", "0");
    private final ArrayList<Variable> registers = new ArrayList<Variable>() {{
        add(register0);    add(register1);    add(register2);    add(register3);
        add(register4);    add(register5);    add(register6);    add(register7);
    }};
    
    private HashMap<String, Variable> variables = new HashMap<String, Variable>() {{
        put("com.mem.ramCell0", ramCells.get(0)); put("com.mem.ramCell1", ramCells.get(1)); put("com.mem.ramCell2", ramCells.get(2)); put("com.mem.ramCell3", ramCells.get(3));
        put("com.mem.ramCell4", ramCells.get(4)); put("com.mem.ramCell5", ramCells.get(5)); put("com.mem.ramCell6", ramCells.get(6)); put("com.mem.ramCell7", ramCells.get(7));
        put("com.mem.romCell0", romCells.get(0)); put("com.mem.romCell1", romCells.get(1)); put("com.mem.romCell2", romCells.get(2)); put("com.mem.romCell3", romCells.get(3));
        put("com.mem.romCell4", romCells.get(4)); put("com.mem.romCell5", romCells.get(5)); put("com.mem.romCell6", romCells.get(6)); put("com.mem.romCell7", romCells.get(7));
        put("com.mem.mmCell0", mmCells.get(0)); put("com.mem.mmCell1", mmCells.get(1)); put("com.mem.mmCell2", mmCells.get(2)); put("com.mem.mmCell3", mmCells.get(3));
        put("com.mem.mmCell4", mmCells.get(4)); put("com.mem.mmCell5", mmCells.get(5)); put("com.mem.mmCell6", mmCells.get(6)); put("com.mem.mmCell7", mmCells.get(7));
        put("com.cpu.alu.register0", registers.get(0)); put("com.cpu.alu.register1", registers.get(1)); put("com.cpu.alu.register2", registers.get(2)); put("com.cpu.alu.register3", registers.get(3));
        put("com.cpu.alu.register4", registers.get(4)); put("com.cpu.alu.register5", registers.get(5)); put("com.cpu.alu.register6", registers.get(6)); put("com.cpu.alu.register7", registers.get(7));
    }};
    
//    private HashMap<ArrayList<String>, ArrayList<String>> aliases = new HashMap<ArrayList<String>, ArrayList<String>>() {{
//        put("ramCell0", "com.mem.ramCell0"); put("ramCell1", "com.mem.ramCell1"); put("ramCell2", "com.mem.ramCell2"); put("ramCell3", "com.mem.ramCell3");
//        put("ramCell4", "com.mem.ramCell4"); put("ramCell5", "com.mem.ramCell5"); put("ramCell6", "com.mem.ramCell6"); put("ramCell7", "com.mem.ramCell7");
//        put("romCell0", "com.mem.romCell0"); put("romCell1", "com.mem.romCell1"); put("romCell2", "com.mem.romCell2"); put("romCell3", "com.mem.romCell3");
//        put("romCell4", "com.mem.romCell4"); put("romCell5", "com.mem.romCell5"); put("romCell6", "com.mem.romCell6"); put("romCell7", "com.mem.romCell7");
//        put("mmCell0", "com.mem.mmCell0"); put("mmCell1", "com.mem.mmCell1"); put("mmCell2", "com.mem.mmCell2"); put("mmCell3", "com.mem.mmCell3");
//        put("mmCell4", "com.mem.mmCell4"); put("mmCell5", "com.mem.mmCell5"); put("mmCell6", "com.mem.mmCell6"); put("mmCell7", "com.mem.mmCell7");
//        put("regiter0", "com.cpu.alu.register0"); put("register1", "com.cpu.alu.register1"); put("register2", "com.cpu.alu.register2"); put("register3", "com.cpu.alu.register3");
//        put("register4", "com.cpu.alu.register4"); put("register5", "com.cpu.alu.register5"); put("register6", "com.cpu.alu.register6"); put("register7", "com.cpu.alu.register7");
//    }};
    
    private HashMap<ArrayList<String>,  ArrayList<String>> aliases = new HashMap<ArrayList<String>,  ArrayList<String>>() {{
        put(new ArrayList<>(Arrays.asList("ramCell0")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell0"))); put(new ArrayList<>(Arrays.asList("ramCell1")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell1"))); put(new ArrayList<>(Arrays.asList("ramCell2")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell2"))); put(new ArrayList<>(Arrays.asList("ramCell3")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell3")));
        put(new ArrayList<>(Arrays.asList("ramCell4")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell4"))); put(new ArrayList<>(Arrays.asList("ramCell5")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell5"))); put(new ArrayList<>(Arrays.asList("ramCell6")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell6"))); put(new ArrayList<>(Arrays.asList("ramCell7")), new ArrayList<>(Arrays.asList("com",".","mem",".","ramCell7")));
        put(new ArrayList<>(Arrays.asList("romCell0")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell0"))); put(new ArrayList<>(Arrays.asList("romCell1")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell1"))); put(new ArrayList<>(Arrays.asList("romCell2")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell2"))); put(new ArrayList<>(Arrays.asList("romCell3")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell3")));
        put(new ArrayList<>(Arrays.asList("romCell4")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell4"))); put(new ArrayList<>(Arrays.asList("romCell5")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell5"))); put(new ArrayList<>(Arrays.asList("romCell6")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell6"))); put(new ArrayList<>(Arrays.asList("romCell7")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell7")));
        put(new ArrayList<>(Arrays.asList("mmCell0")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell0"))); put(new ArrayList<>(Arrays.asList("mmCell1")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell1"))); put(new ArrayList<>(Arrays.asList("mmCell2")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell2"))); put(new ArrayList<>(Arrays.asList("mmCell3")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell3")));
        put(new ArrayList<>(Arrays.asList("mmCell4")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell4"))); put(new ArrayList<>(Arrays.asList("mmCell5")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell5"))); put(new ArrayList<>(Arrays.asList("mmCell6")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell6"))); put(new ArrayList<>(Arrays.asList("mmCell7")), new ArrayList<>(Arrays.asList("com",".","mem",".","mmCell7")));
        put(new ArrayList<>(Arrays.asList("register0")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register0"))); put(new ArrayList<>(Arrays.asList("register1")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register1"))); put(new ArrayList<>(Arrays.asList("register2")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register2"))); put(new ArrayList<>(Arrays.asList("register3")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register3")));
        put(new ArrayList<>(Arrays.asList("register4")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register4"))); put(new ArrayList<>(Arrays.asList("register5")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register5"))); put(new ArrayList<>(Arrays.asList("register6")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register6"))); put(new ArrayList<>(Arrays.asList("register7")), new ArrayList<>(Arrays.asList("com",".","cpu",".","alu",".","register7")));
        put(new ArrayList<>(Arrays.asList("addIdent")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell0"))); put(new ArrayList<>(Arrays.asList("mulIdent")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell1"))); put(new ArrayList<>(Arrays.asList("accuracy")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell2"))); put(new ArrayList<>(Arrays.asList("pi")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell3")));
        put(new ArrayList<>(Arrays.asList("e")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell4"))); put(new ArrayList<>(Arrays.asList("version")), new ArrayList<>(Arrays.asList("com",".","mem",".","romCell5")));
    }};
    
    private ArrayList<ArrayList<String>> currInfoTransport = new ArrayList<>();
    private int nextLine;
    private boolean exitingProgram = false;
    private int exitCode;
    
    private static final int ALUAddOp = 0;
    private static final int ALUSubOp = 1;
    private static final int ALUMulOp = 2;
    private static final int ALUDivOp = 3;
    private static final int ALUNotOp = 4;
    private static final int ALUOrOp = 5;
    private static final int ALUAndOp = 6;
    private static final int ALUXorOp = 7;
    private static final int ALUNorOp = 8;
    private static final int ALUNandOp = 9;
    private static final int ALUXnorOp = 10;
    private static final int ALUShiftLeftOp = 11;
    private static final int ALUShiftRightOp = 12;
    private static final int ALUShiftRightUnsignedOp = 13;
    
    private static final ArrayList<Integer> oneRegOps = new ArrayList<Integer>() {{
        add(ALUNotOp);
        add(ALUShiftLeftOp);    add(ALUShiftRightOp);    add(ALUShiftRightUnsignedOp);
    }};
    private static final ArrayList<Integer> twoRegOps = new ArrayList<Integer>() {{
        add(ALUAddOp);    add(ALUSubOp);    add(ALUMulOp);    add(ALUDivOp);
        add(ALUOrOp);    add(ALUAndOp);    add(ALUXorOp);
        add(ALUNorOp);    add(ALUNandOp);    add(ALUXnorOp);
    }};
    
    private static final String inputPrompt = ">>> ";
    
    public Redstone() {
        
        super.wordTypes = wordTypes;
        super.valTypes = valTypes;
        super.variables = variables;
        super.methods = methods;
        super.operations = operations;
    }
    
    @Override
    public void execLine(int lineNumber) {
        
        currentLine = lineNumber;
        nextLine = currentLine + 1;
        
        int realLine = getRealLine(lineNumber);
        if (realLine < 0) {
            
            //ERROR
            compiler.exitProgram(Compiler.buildFinishedCode);
            return;
        }
        
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        
        //put all the lines we need in lines
        int curlybraces = 0;
        mainloop:
        for (int i = realLine; i < program.size(); i++) {
            
            for (String str : program.get(i)) {
                
                if (str.equals("{"))
                    curlybraces++;
                
                else if (str.equals("}"))
                    curlybraces--;
                
                else if (str.equals(";") && curlybraces == 0) {
                    
                    if (i == realLine)
                        //this is the first and last line
                        lines.add(subArray(program.get(i), getFirstIndexOf(new ArrayList<>(Arrays.asList("{")), program.get(i)) + 1, getLastIndexOf(new ArrayList<>(Arrays.asList("}")), program.get(i)) - 1));
                    
                    else
                        //add line, only things before "}"
                        lines.add(subArray(program.get(i), 0, getLastIndexOf(new ArrayList<>(Arrays.asList("}")), program.get(i)) - 1));
                    break mainloop;
                }
            }
            
            if (i == realLine)
                //this is the first line
                lines.add(subArray(program.get(i), getFirstIndexOf(new ArrayList<>(Arrays.asList("{")), program.get(i)) + 1, program.get(i).size() - 1));
            
            else
            lines.add(program.get(i));
        }
        
        //sort lines into an array of each command
        ArrayList<ArrayList<String>> commands = new ArrayList<>();
        for (ArrayList<String> line : lines) {
            
            curlybraces = 0;
            
            int lastSemicolon = -1;
            
            for (int i = 0; i < line.size(); i++) {
                
                if (line.get(i).equals("{"))
                    curlybraces++;
                
                else if (line.get(i).equals("}"))
                    curlybraces--;
                
                else if (line.get(i).equals(";") && curlybraces <= 0) {
                    
                    commands.add(subArray(line, lastSemicolon + 1, i - 1));
                    lastSemicolon = i;
                }
            }
        }
        
        //execute each command
        for (ArrayList<String> command : commands)
            execCommand(command);
        
        clock();
    }
    
    public void execCommand(ArrayList<String> command) {
        
        //comments
        if (command.get(0).equals("/") && command.get(1).equals("/"))
            return;
        
        //aliases
        for (int i = 0; i < command.size(); i++)
            for (int a = command.size() - 1; a >= i; a--)
                if (aliases.containsKey(subArray(command, i, a))) { 
                    
                    ArrayList<String> alias = aliases.get(subArray(command, i, a));
                    command = trimArray(command, i, a);
                    //add alias in
                    for (int e = 0; e < alias.size(); e++)
                        command.add(i + e, alias.get(e));
                    
                    i += alias.size() - 1;
                    break;
                }
        
        System.out.println("Command: " + command);
        
        //input
        ArrayList<String> inputs = new ArrayList<>();
        for (int i = 0; i < command.size(); i++)
            if (command.get(i).substring(0, command.get(i).length() - 1).equals("getInput")) {
                
                int inputNumber = 0;
                
                try {
                    
                    inputNumber = Integer.parseInt(command.get(i).substring(command.get(i).length() - 1));
                    
                    while (inputNumber >= inputs.size())
                        inputs.add(String.valueOf(Integer.parseInt(compiler.getInput(inputPrompt))));
                    
                    command.set(i, inputs.get(inputNumber));
                }
                
                catch (NumberFormatException e) {compiler.exitProgram(Compiler.numberParsingError);}
            }
        
        ArrayList<String> form;
        
        form = new ArrayList(Arrays.asList("load", "@int", ",", "@var"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            currInfoTransport.add(new ArrayList<>(Arrays.asList(toString(unknowns.get(0)), toString(unknowns.get(1)))));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("send", "@int"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            nextLine = Integer.parseInt(evaluateSeq(unknowns.get(0)));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("send", "_", "@int"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            nextLine = currentLine + Integer.parseInt(evaluateSeq(unknowns.get(0)));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("continue"));
        if (isForm(command, form)) {
            
            nextLine = currentLine + 1;
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("branch", "(", "@int", ")", ":", "@any"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            if (!evaluateSeq(unknowns.get(0)).equals("0")) {
                
                execCommand(unknowns.get(1));
            }
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("branch", "!", "(", "@int", ")", ":", "@any"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            if (evaluateSeq(unknowns.get(0)).equals("0")) {
                
                execCommand(unknowns.get(1));
            }
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("@method", "(", "@any", ")"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            Method method = methods.get(unknowns.get(0).get(0));
            
            evaluateMethod(method, new ArrayList<>(Arrays.asList(evaluateSeq(unknowns.get(1)))));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("calc", "@int", ",", "@var"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            String res = evaluateSeq(unknowns.get(0));
            
            currInfoTransport.add(new ArrayList<>(Arrays.asList(res, shortenNameWrapped(unknowns.get(1)))));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("alias", "@any", ",", "@any"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenNameWrapped(unknowns.get(0)) != null/* && shortenName(unknowns.get(1)).size() == 1*/)
                aliases.put(unknowns.get(0), unknowns.get(1));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("print", "@int"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            evaluateMethod(methods.get("print"), new ArrayList<>(Arrays.asList(evaluateSeq(unknowns.get(0)))));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("println", "@int"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            evaluateMethod(methods.get("println"), new ArrayList<>(Arrays.asList(evaluateSeq(unknowns.get(0)))));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("print", "@any"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            compiler.print(toString(unknowns.get(0)));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("println", "@any"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            compiler.println(toString(unknowns.get(0)));
            
            return;
        }
        
        form = new ArrayList(Arrays.asList("exit", "@int"));
        if (isForm(command, form)) {
            
            ArrayList<ArrayList<String>> unknowns = getUnknowns(command, form);
            
            if (shortenName(shortenNum(unknowns.get(0))).size() > 1)
                return;
            
            evaluateMethod(methods.get("exit"), new ArrayList<>(Arrays.asList(evaluateSeq(unknowns.get(0)))));
            
            return;
        }
    }
    
    private void clock() {
        
        //clone ram and registers
        ArrayList<Variable> ramCellsClone = new ArrayList<>();
        ArrayList<Variable> mmCellsClone = new ArrayList<>();
        ArrayList<Variable> registersClone = new ArrayList<>();
        
        for (Variable ramCell : ramCells)
            ramCellsClone.add((Variable)ramCell.clone());
        
        for (Variable mmCell : mmCells)
            mmCellsClone.add((Variable)mmCell.clone());
        
        for (Variable register : registers)
            registersClone.add((Variable)register.clone());
        
        //transport information
        for (ArrayList<String> transport : currInfoTransport) {
            
            String information = null;
            
            switch (transport.get(0).substring(0, transport.get(0).length() - 1)) {
                
                case "ramCell": case "com.mem.ramCell":
                    try {
                        
                        information = ramCellsClone.get(Integer.parseInt(transport.get(0).substring(transport.get(0).length() - 1))).getVal();
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                case "romCell": case "com.mem.romCell":
                    try {
                        
                        information = romCells.get(Integer.parseInt(transport.get(0).substring(transport.get(0).length() - 1))).getVal();
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                case "mmCell": case "com.mem.mmCell":
                    try {
                        
                        information = mmCellsClone.get(Integer.parseInt(transport.get(0).substring(transport.get(0).length() - 1))).getVal();
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                case "register": case "com.cpu.alu.register":
                    try {
                        
                        information = registersClone.get(Integer.parseInt(transport.get(0).substring(transport.get(0).length() - 1))).getVal();
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                //numbers
                default:
                    try {
                        
                        information = transport.get(0);
                    }
                    catch (NumberFormatException e) {}
                    break;
            }
            
            switch (transport.get(1).substring(0, transport.get(1).length() - 1)) {
                
                case "ramCell": case "com.mem.ramCell":
                    try {
                        
                        ramCells.get(Integer.parseInt(transport.get(1).substring(transport.get(1).length() - 1))).setVal(information);
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                case "mmCell": case "com.mem.mmCell":
                    try {
                        
                        mmCells.get(Integer.parseInt(transport.get(1).substring(transport.get(1).length() - 1))).setVal(information);
                    }
                    catch (NumberFormatException e) {}
                    break;
                
                case "register": case "com.cpu.alu.register":
                    try {
                        
                        registers.get(Integer.parseInt(transport.get(1).substring(transport.get(1).length() - 1))).setVal(information);
                    }
                    catch (NumberFormatException e) {}
                    break;
            }
        }
        
        currInfoTransport.clear();
        
        //exit program if needed
        if (exitingProgram)
            compiler.exitProgram(exitCode);
    }
    
//    private static int ALUCalc(int operation, int val) {
//        
//        switch (operation) {
//            
//            case ALUNotOp:
//                try {
//                    
//                    return ~val;
//                }
//                catch (NumberFormatException e) {}
//                break;
//        }
//        
//        return 0;
//    }
//    
//    private static int ALUCalc(int operation, int val1, int val2) {
//        
//        switch (operation) {
//            
//            case ALUAddOp:
//                try {
//                    
//                    return val1 + val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUSubOp:
//                try {
//                    
//                    return val1 - val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUMulOp:
//                try {
//                    
//                    return val1 * val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUDivOp:
//                try {
//                    
//                    return val1 / val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUOrOp:
//                try {
//                    
//                    return val1 | val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUAndOp:
//                try {
//                    
//                    return val1 & val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUXorOp:
//                try {
//                    
//                    return val1 ^ val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUNorOp:
//                try {
//                    
//                    return ~(val1 | val2);
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUNandOp:
//                try {
//                    
//                    return ~(val1 & val2);
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUXnorOp:
//                try {
//                    
//                    return ~(val1 ^ val2);
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUShiftLeftOp:
//                try {
//                    
//                    return val1 << val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUShiftRightOp:
//                try {
//                    
//                    return val1 >> val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//            case ALUShiftRightUnsignedOp:
//                try {
//                    
//                    return val1 >>> val2;
//                }
//                catch (NumberFormatException e) {}
//                break;
//        }
//        
//        return 0;
//    }
//    
//    private static int getOpNumber(String operation) {
//        
//        switch (operation) {
//            
//            case "+":
//                return ALUAddOp;
//            case "-":
//                return ALUSubOp;
//            case "*":
//                return ALUMulOp;
//            case "/":
//                return ALUDivOp;
//            case "~":
//                return ALUNotOp;
//            case "|":
//                return ALUOrOp;
//            case "&":
//                return ALUAndOp;
//            case "^":
//                return ALUXorOp;
//            case "~|":
//                return ALUNorOp;
//            case "~&":
//                return ALUNandOp;
//            case "~^":
//                return ALUXnorOp;
//            case "<<":
//                return ALUShiftLeftOp;
//            case ">>":
//                return ALUShiftRightOp;
//            case ">>>":
//                return ALUShiftRightUnsignedOp;
//        }
//        
//        return 0;
//    }
    
    @Override
    protected String evaluateMethod(Method method, ArrayList<String> parameters) {
        
        String superEval = super.evaluateMethod(method, parameters);
        
        if (superEval == null) {
            
            switch (method.getName()) {
                
                case "print":
                    compiler.print(Integer.parseInt(parameters.get(0)));
                    break;
                case "println":
                    compiler.println(Integer.parseInt(parameters.get(0)));
                    break;
                case "exit":
                    exitingProgram = true;
                    exitCode = Integer.parseInt(parameters.get(0));
                    break;
                
//                case "pow":
//                    return String.valueOf((int)Math.pow(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1))));
            }
        }
        
        return superEval;
    }
    
    @Override
    protected String evaluateOp(Operation operation, String value) {
        
        int val = Integer.parseInt(value);
        
        switch (operation.getSymbol()) {
            
            case "~":
                return String.valueOf(~val);
            
            case "+":
                return String.valueOf(val);
            
            case "-":
                return String.valueOf(-val);
        }
        
        return null;
    }
    
    @Override
    protected String evaluateOp(Operation operation, String value1, String value2) {
        
        int val1 = Integer.parseInt(value1);
        int val2 = Integer.parseInt(value2);
        
        switch (operation.getSymbol()) {
            
            case "+":
                return String.valueOf(val1 + val2);
                
            case "-":  
                return String.valueOf(val1 - val2);
                
            case "*":
                return String.valueOf(val1 * val2);
            
            case "**":
                return String.valueOf((int)Math.pow(val1, val2));
                
            case "/":
                return String.valueOf(val1 / val2);
            
            case "%":
                return String.valueOf(val1 % val2);
                
            case "|":
                return String.valueOf(val1 | val2);
                
            case "&":
                return String.valueOf(val1 & val2);
                
            case "^":
                return String.valueOf(val1 ^ val2);
                
            case "~|":
                return String.valueOf(~(val1 | val2));
                
            case "~&":
                return String.valueOf(~(val1 & val2));
                
            case "~^":
                return String.valueOf(~(val1 ^ val2));
                
            case "<<":
                return String.valueOf(val1 << val2);
                
            case ">>":
                return String.valueOf(val1 >> val2);
                
            case ">>>":
                return String.valueOf(val1 >>> val2);
        }
        
        return null;
    }
    
    @Override
    public void initProgram() {
        
        for (int i = 0; i < program.size(); i++)
            if (getProgramLine(i) == -1)
                execLine(-1);
    }
    
    @Override
    public int findStartLine() {
        
        return 0;
    }
    
    private int getProgramLine(int realLine) {
        
        if (program.get(realLine).size() < 2)
            return -2;
        
        if (program.get(realLine).get(1).equals(":")) {
            
            try {
                
                return Integer.parseInt(program.get(realLine).get(0));
            }
            
            catch (NumberFormatException e) {}
        }
        
        if (program.get(realLine).size() < 3)
            return -2;
        
        if (program.get(realLine).get(2).equals(":")) {
            
            try {
                
                if (program.get(realLine).get(0).equals("-"))
                    return -Integer.parseInt(program.get(realLine).get(1));
            }
            
            catch (NumberFormatException e) {}
        }
        
        return -2;
    }
    
    private int getRealLine(int programLine) {
        
        for (int i = 0; i < program.size(); i++)
            if (getProgramLine(i) == programLine)
                return i;
        
        return -1;
    }
    
    @Override
    public int getNextLine() {
        
        return nextLine;
    }
    
    public static String getName() {
        
        return name;
    }
}



package TextEditor;

import Compiler.Compiler;
import Languages.Redstone;
import TextDisplay.TextDisplay;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    
    private static ArrayList<TextDisplay> displays = new ArrayList<>(Arrays.asList(new TextDisplay(), new TextDisplay(), new TextDisplay()/*, new TextDisplay()*/));
    private static int currentDisplay;
    
    public static void main(String[] args) {
        
        Redstone redstone = new Redstone();
        Compiler compiler = new Compiler();
        
//        System.out.println(redstone.getUnknowns(new ArrayList<>(Arrays.asList("calc", "pow", "(", "10", ",", "2", ")", ",", "register0")), new ArrayList<>(Arrays.asList("calc", "@int", ",", "@var"))));
        
//        System.out.println(redstone.getSeqType(new ArrayList<>(Arrays.asList("(", "int", "$", "bool", ")", "+", "int"))));
//        System.out.println(redstone.getSeqType(new ArrayList<>(Arrays.asList("int"))));
//        System.out.println(redstone.getWordType("int"));
//        System.out.println(redstone.getSeqType(new ArrayList<>(Arrays.asList("(", "int", "==", "int", ")", "||", "bool"))));
        
        //boolean var = (1 == 1) || true;
        
//        redstone.execLine(16);
        
//        ArrayList<String> program = new ArrayList<String>() {{
//            add("#lang Redstone");
//            add("");
//            add("0: {");
//            add("branch (true || false): send 3;");
//            add("print(1);");
//            add("};");
//        }};
//        
//        ArrayList<ArrayList<String>> programArray = new ArrayList<ArrayList<String>>() {{
//            add(new ArrayList<>(Arrays.asList("0", ":", "{")));
//            add(new ArrayList<>(Arrays.asList("branch", "(", "true", "||", "false", ")", ":", "send", "3", ";")));
//            add(new ArrayList<>(Arrays.asList("print", "(", "1", ")", ";")));
//            add(new ArrayList<>(Arrays.asList("}", ";")));
//        }};
//        
////        redstone.setProgram(programArray);
//        
//        compiler.runProgram(program);
        
//        redstone.execLine(0);
        
//        System.out.println(redstone.getNextLine());
        
//        System.out.println(redstone.isForm(new ArrayList<>(Arrays.asList("branch", "(", "true", ")", ":", "send", "3", ";")), new ArrayList<>(Arrays.asList("branch", "(", "@bool", ")", ":", "@any", ";"))));
        
//        System.out.println(redstone.isForm(new ArrayList<>(Arrays.asList("1", "=", "2")), new ArrayList<>(Arrays.asList("@int", "=", "@any"))));
        
//        compiler.runProgram(program);
        
//        redstone.execCommand(new ArrayList<>(Arrays.asList("print", "(", "1", ")")));
        
//        System.out.println(redstone.evaluateSeq(new ArrayList<>(Arrays.asList("testMethod", "(", "1", "+", "1", ",", "true", "&&", "false", ")", "||", "false"))));
        
//        System.out.println(redstone.evaluateSeq(new ArrayList<>(Arrays.asList("testMethod", "(", "2", ",", "false", ")"))));
        
//        System.out.println(redstone.getSeqType(new ArrayList<>(Arrays.asList("true", "&&", "true", "||", "testMethod", "(", "1", "+", "1", ",", "false", ")"))));
        
//        System.out.println(redstone.getSeqType(new ArrayList<>(Arrays.asList("(", "1", "+", "1", ")", "-", "1"))));
        
//        System.out.println(redstone.isForm(new ArrayList<>(Arrays.asList("int", "=", "1", ";")), new ArrayList<>(Arrays.asList("@any", "=", "@int", ";"))));
        
//        redstone.clock();
        
//        System.out.println(redstone.isForm(new ArrayList<>(Arrays.asList("boolean", "var", "=", "(", "1", "==", "1", ")", "||", "true", ";")), new ArrayList<>(Arrays.asList("@type", "@any", "=", "@bool", ";"))));
//        System.out.println(redstone.getUnknowns(new ArrayList<>(Arrays.asList("boolean", "var", "=", "(", "1", "==", "1", ")", "||", "true", ";")), new ArrayList<>(Arrays.asList("@type", "var", "=", "@bool", ";"))));
//        System.out.println(redstone.getUnknowns(new ArrayList<>(Arrays.asList("int", "var", "=", "1", "+", "1", ";")), new ArrayList<>(Arrays.asList("@type", "@any", "=", "@int", ";"))));
//        System.out.println(redstone.splitSeq(new ArrayList<>(Arrays.asList("int", "var")), new ArrayList<>(Arrays.asList("@type", "@any"))));
        
//        System.out.println(new Redstone().isEqual(new ArrayList(Arrays.asList("opTest")), new ArrayList(Arrays.asList("@ops"))));
        
        TextDisplay.initDisplay();
        
        for (TextDisplay display : displays)
            display.initialise();
        
        while (true) {
            
            displays.get(currentDisplay).getInput();
            displays.get(currentDisplay).render();
        }
    }
    
    public static void switchDisplay(int display) {
        
        if (displays.size() <= display) {
            
            System.out.println("Display " + display + " does not exist.");
            return;
        }
        
        currentDisplay = display;
        
        System.out.println("Switched to: " + currentDisplay);
    }
    
    public static void addDisplay() {
        
        addDisplay(new TextDisplay());
    }
    
    public static void addDisplay(TextDisplay display) {
        
        displays.add(display);
        switchDisplay(displays.indexOf(display));
    }
    
    public static void addDisplay(TextDisplay display, int index) {
        
        displays.add(index, display);
        switchDisplay(index);
    }
    
    public static void removeDisplay(int display) {
        
        displays.remove(display);
        
        if (currentDisplay >= displays.size())
            currentDisplay--;
    }
    
    public static ArrayList<TextDisplay> getDisplays() {
        
        return displays;
    }
    
    public static TextDisplay getCurrentDisplay() {
        
        return displays.get(currentDisplay);
    }
}

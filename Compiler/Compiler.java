

package Compiler;

import Languages.*;
import TextDisplay.TextDisplay;
import TextEditor.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;


public class Compiler {
    
    private ArrayList<ArrayList<String>> program = new ArrayList<>();
    private Language language;
    //public static Class<? extends Language> language = (Class<? extends Language>)(Class)Redstone.class;
    private boolean programRunning;
    private int currentLine;
    private static final HashMap<String, Language> languages = new HashMap<String, Language>() {{
        put("Redstone", new Redstone());
    }};
    
    private TextDisplay programDisplay;
    
    public static final int nullError = 0;
    public static final int buildFinishedCode = 1;
    public static final int languageNotFoundError = 2;
    public static final int numberParsingError = 3;
    
    private static final int whitespaceCharCode = 0;
    private static final int letterCharCode = 1;
    private static final int numberCharCode = 2;
    private static final int letterNumberCharCode = 3;
    private static final int symbolCharCode = 4;
    private static final int opSymbolCharCode = 5;
    private static final int miscSymbolCharCode = 6;
    
    private static final ArrayList<Character> opSymbolChars = new ArrayList<>(Arrays.asList('+', '-', '*', '/', '=', '!', '|', '&', '<', '>', '^'));
    
    public Compiler() {}
    
    public Compiler(TextDisplay programDisplay) {
        
        this.programDisplay = programDisplay;
    }
    
    public void runProgram(ArrayList<String> inputText) {
        
        ArrayList<String> text = (ArrayList)inputText.clone();
        
        println("run:");
        
        trim(text);
        program = simplify(text);
        
        language = findLanguage(program);
        
        if (!(language instanceof Language)) {
            //exit program
            return;
        }
        
        if (program.isEmpty()) {
            //end program
            exitProgram(buildFinishedCode);
            return;
        }
        
        language.setProgram(program);
        language.setCompiler(this);
        language.initProgram();
        currentLine = language.findStartLine();
        
        programRunning = true;
        while (programRunning) {
            
            execLine(currentLine);
            currentLine = language.getNextLine();
            
            Main.getCurrentDisplay().getInput();
            Main.getCurrentDisplay().render();
        }
    }
    
    public void execLine(int line) {
        
        language.execLine(line);
    }
    
    public void exitProgram(int cause) {
        
        programRunning = false;
        
        switch (cause) {
            
            case languageNotFoundError:
                println("Error: Compiler was unable to determine language");
            
            case numberParsingError:
                println("Error: NumberFormatException on line " + language.getCurrentLine());
        }
        
        println("Build Finished. Exit Code: " + cause);
    }
    
    public String getInput() {
        
        return getInput("");
    }
    
    public String getInput(String prompt) {
        
        print(prompt);
        
        boolean returnWasDown = Keyboard.isKeyDown(Keyboard.KEY_RETURN);
        
        while (true) {
            
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                
                if (!returnWasDown) {
                    
                    return Main.getCurrentDisplay().getPlainText().get(Main.getCurrentDisplay().cursorLine).substring(prompt.length());
                }
            }
            
            else
                returnWasDown = false;
            
            Main.getCurrentDisplay().getInput();
            Main.getCurrentDisplay().render();
        }
    }
    
    public void print(String string) {
        
        if (programDisplay.getPlainText().get(programDisplay.text.size() - 1).length() > 0)
            programDisplay.enter(programDisplay.text.size() - 1, programDisplay.getPlainText().get(programDisplay.text.size() - 1).length());
        programDisplay.write(string);
    }
    
    public void print(int string) {
        
        print(String.valueOf(string));
    }
    
    public void println(String string) {
        
        print(string);
        programDisplay.enter(programDisplay.text.size() - 1, programDisplay.getPlainText().get(programDisplay.text.size() - 1).length());
    }
    
    public void println(int string) {
        
        println(String.valueOf(string));
    }
    
    public void println() {
        
        programDisplay.enter(programDisplay.text.size() - 1, programDisplay.getPlainText().get(programDisplay.text.size() - 1).length());
    }
    
    public Language findLanguage(ArrayList<ArrayList<String>> program) {
        
        for (ArrayList<String> line : program) {
            
            if (line.size() < 3)
                continue;
            
            if ((line.get(0) + line.get(1)).equals("#lang")) {
                program.remove(line);
                return languages.get(line.get(2));
            }
            
            if (!line.equals(new ArrayList<>())) {
                //language not found
                exitProgram(languageNotFoundError);
                return null;
            }
        }
        
        //language not found
        exitProgram(languageNotFoundError);
        return null;
    }
    
    public static ArrayList<String> trim(ArrayList<String> text) {
        
        //get rid of empty lines
        for (int i = 0; i < text.size(); i++) {
            
            text.set(i, text.get(i).trim());
            
            if (text.get(i).length() == 0)
                text.remove(i);
        }
        
        return text;
    }
    
    public static ArrayList<ArrayList<String>> simplify(ArrayList<String> text) {
        
        ArrayList<ArrayList<String>> simplifiedText = new ArrayList<>(text.size());
        
        for (String line : text)
            simplifiedText.add(simplify(line));
        
        return simplifiedText;
    }
    
    public static ArrayList<String> simplify(String string) {
        
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(""));
        
        //separate string into words
        for (Character ch : string.toCharArray()) {
            
            if (charType(ch, letterNumberCharCode)) {
                
                if (charType(stringArray.get(stringArray.size() - 1).toCharArray(), letterNumberCharCode))
                    stringArray.set(stringArray.size() - 1, stringArray.get(stringArray.size() - 1) + ch);
                else
                    stringArray.add(Character.toString(ch));
            }
            
            else if (charType(ch, whitespaceCharCode)) {
                
                if (!stringArray.get(stringArray.size() - 1).equals(""))
                    stringArray.add("");
            }
            
            else if (charType(ch, opSymbolCharCode)) {
                
                if (charType(stringArray.get(stringArray.size() - 1).toCharArray(), opSymbolCharCode))
                    stringArray.set(stringArray.size() - 1, stringArray.get(stringArray.size() - 1) + ch);
                else
                    stringArray.add(Character.toString(ch));
            }
            
            else if (charType(ch, miscSymbolCharCode)) {
                
                if (stringArray.get(stringArray.size() - 1).equals(""))
                    stringArray.set(stringArray.size() - 1, Character.toString(ch));
                else
                    stringArray.add(Character.toString(ch));
            }
        }
        
        //remove empty index at start
        if (stringArray.get(0).isEmpty())
            stringArray.remove(0);
        
        return stringArray;
    }
    
    public static boolean charType(char ch, int type) {
        
        switch (type) {
            
            case whitespaceCharCode:
                return Character.isWhitespace(ch);
            case letterCharCode:
                return Character.isLetter(ch);
            case numberCharCode:
                return Character.isDigit(ch);
            case letterNumberCharCode:
                return Character.isLetterOrDigit(ch);
            case symbolCharCode:
                return !(Character.isWhitespace(ch) || Character.isLetterOrDigit(ch));
            case opSymbolCharCode:
                return opSymbolChars.contains(ch);
            case miscSymbolCharCode:
                return !(Character.isWhitespace(ch) || Character.isLetterOrDigit(ch) || opSymbolChars.contains(ch));
        }
        
        return false;
    }
    
    public static boolean charType(char[] chars, int type) {
        
        switch (type) {
            
            case whitespaceCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case letterCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case numberCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case letterNumberCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case symbolCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case opSymbolCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
            
            case miscSymbolCharCode:
                for (Character ch : chars)
                    if (!charType(ch, type))
                        return false;
                return true;
        }
        
        return false;
    }
}

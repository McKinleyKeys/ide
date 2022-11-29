

package Languages;

import java.util.ArrayList;
import Compiler.Compiler;
import Languages.Util.Method;
import Languages.Util.Operation;
import Languages.Util.Variable;
import java.util.Arrays;
import java.util.HashMap;


public class Language {
    
    protected static final String name = null;
    protected ArrayList<ArrayList<String>> program;
    protected Compiler compiler;
    protected int currentLine;
    protected int nextLine;
    
    protected ArrayList<String> wordTypes = new ArrayList<>(Arrays.asList("int", "float", "bool", "type", "var", "method"));
    protected ArrayList<Operation> operations = Operation.getStandardOps();
    protected HashMap<String, Variable> variables = new HashMap<String, Variable>() {{}};
    protected HashMap<String, Method> methods = new HashMap<String, Method>() {{}};
    
    protected ArrayList<String> valTypes = new ArrayList<>(Arrays.asList("int", "float", "boolean"));
    
    public void execLine(int index) {
        
        
    }
    
    public void initProgram() {
        
        
    }
    
    protected boolean isWordType(String word, String type) {
        
        //variables
        String varName = word;
        if (variables.containsKey(word))
            word = variables.get(word).getVal();
        
        switch (type) {
            
            case "@any":
                return !word.isEmpty();
            case "@var":
                return variables.containsKey(varName);
            case "@method":
                return methods.containsKey(word);
            case "@type":
                return valTypes.contains(word);
            case "@val":
                return isValue(word, "val");
            case "@int": case "@float": case"@bool":
                return isValue(word, type.substring(1));
            case "@op":
                for (Operation op : operations)
                    if (op.getSymbol().equals(word))
                        return true;
                return false;
            
            default:
                return false;
        }
    }
    
    protected String getWordType(String word) {
        
        for (String type : wordTypes)
            if (isWordType(word, "@" + type))
                return type;
        
        return null;
    }
    
    public String getSeqType(ArrayList<String> sequence) {
        
        ArrayList<String> seq = (ArrayList)sequence.clone();
        
        //replace words with their type
        for (int i = 0; i < seq.size(); i++) {
            
            String wordType;
            
            if ((wordType = getWordType(seq.get(i))) != null)
                seq.set(i, wordType);
        }
        
        return getSeqTypeWrapped(seq);
    }
    
    public String getSeqTypeWrapped(ArrayList<String> sequence) {
        
        ArrayList<String> seq = (ArrayList)sequence.clone();
        
        //evaluate variables first
        for (int i = 0; i < seq.size(); i++) {
            
            aLoop:
            for (int a = seq.size() - 1; a >= i; a--) {
                
                ArrayList<String> subArray = subArray(seq, i, a);
                String subString = "";
                
                //check if subArray is proper variable name
                for (String string : subArray) {
                    
                    if (subString.length() == 0) {
                        
                        if (!string.equals("."))
                            subString += string;
                        
                        else
                            continue aLoop;
                    }
                    
                    else if (string.equals(".")) {
                        
                        if (subString.charAt(subString.length() - 1) != '.')
                            subString += string;
                        
                        else
                            continue aLoop;
                    }
                    
                    else {
                        
                        if (subString.charAt(subString.length() - 1) == '.')
                            subString += string;
                        
                        else
                            continue aLoop;
                    }
                }
                
                if (!variables.containsKey(subString))
                    continue;
                
                //remove between i and a
                for (int e = a; e >= i; e--)
                    seq.remove(e);
                
                seq.add(i, variables.get(subString).getType());
                break;
            }
        }
        
        //evalutate methods
        for (int i = 1; i < seq.size() - 1; i++)
            if (seq.get(i).equals("(")) {
                
                if (methods.containsKey(seq.get(i - 1))) {
                    
                    Method method = methods.get(seq.get(i - 1));
                    
                    ArrayList<Integer> commaPos = new ArrayList<>();
                    int brackets = 0;
                    int curlyBraces = 0;
                    int finalBracketPos = 0;
                    
                    for (int a = i + 1; a < seq.size(); a++) {
                        
                        if (seq.get(a).equals("("))
                            brackets++;
                        else if (seq.get(a).equals(")")) {
                            
                            if (brackets == 0 && curlyBraces == 0) {
                                
                                finalBracketPos = a;
                                break;
                            }
                            brackets--;
                        }
                        else if (seq.get(a).equals("{"))
                            curlyBraces++;
                        else if (seq.get(a).equals("}"))
                            curlyBraces--;
                        
                        else if (seq.get(a).equals(",") && brackets == 0 && curlyBraces == 0)
                            commaPos.add(a);
                    }
                    
                    ArrayList<ArrayList<String>> parameters = new ArrayList<>();
                    
                    int lastCommaPos = i;
                    for (int comma : commaPos) {
                        
                        parameters.add(subArray(seq, lastCommaPos + 1, comma - 1));
                        lastCommaPos = comma;
                    }
                    
                    //add final parameter
                    parameters.add(subArray(seq, lastCommaPos + 1, finalBracketPos - 1));
                    
                    //get parameter types
                    ArrayList<String> paramTypes = new ArrayList<>();
                    for (ArrayList<String> parameter : parameters) {
                        if (parameter.isEmpty())
                            continue;
                        paramTypes.add(getSeqTypeWrapped(parameter));
                    }
                    
                    if (paramTypes.equals(method.getParamTypes())) {
                        
                        seq = trimArray(seq, i - 1, finalBracketPos);
                        seq.add(i - 1, method.getReturnType());
                    }
                    
                    else {
                        
                        //ERROR
                    }
                }
            }
        
        //evaluate brackets
        while (seq.contains("(")) {
            
            int openBracketIndex = seq.indexOf("(");
            int closeBracketIndex = 0;
            //find endBracketIndex
            int a = 0;
            for (int i = openBracketIndex + 1; i < seq.size(); i++) {
                
                if (seq.get(i).equals("("))
                    a++;
                
                else if (seq.get(i).equals(")")) {
                    
                    if (a == 0) {
                        
                        closeBracketIndex = i;
                        break;
                    }
                    
                    a--;
                }
            }
            
            if (closeBracketIndex == 0) {
                
                //ERROR
                return null;
            }
            
            ArrayList<String> insideBrackets = subArray(seq, openBracketIndex + 1, closeBracketIndex - 1);
            
            //remove everything inside brackets
            for (int i = closeBracketIndex; i >= openBracketIndex; i--)
                seq.remove(i);
            
            seq.add(openBracketIndex, getSeqTypeWrapped(insideBrackets));
        }
        
        //evaluate
        for (int i = Operation.topPriority; i >= 0; i--) {
            
            mainloop:
            for (int a = 0; a < seq.size() - 1; a++) {
                
                for (Operation op : operations) {
                    
                    if (op.getSymbol().equals(seq.get(a)) && op.priority == i) {
                        
                        String returnType;
                        
                        switch (op.opType) {
                            
                            case Operation.oneWordOpType:
                                
                                boolean doOperation = false;
                                
                                if (a == 0)
                                    doOperation = true;
                                
                                else if (isOperation(seq.get(a - 1)))
                                    doOperation = true;
                                
                                if (doOperation) {
                                    
                                    if ((returnType = op.getReturnType(seq.get(a + 1))) != null) {
                                        
                                        seq.remove(a + 1);
                                        seq.remove(a);
                                        seq.add(a, returnType);
                                        
                                        continue mainloop;
                                    }
                                    
                                    else {
                                        
                                        //ERROR
                                    }
                                }
                                break;
                            
                            case Operation.twoWordOpType:
                                
                                if (a == 0) {
                                    
                                    //ERROR
                                }
                                
                                if ((returnType = op.getReturnType(seq.get(a - 1), seq.get(a + 1))) != null) {
                                    
                                    seq.remove(a + 1);
                                    seq.remove(a);
                                    seq.remove(a - 1);
                                    seq.add(a - 1, returnType);
                                    
                                    a--;
                                    
                                    continue mainloop;
                                }
                                
                                else {
                                    
                                    //ERROR
                                }
                                break;
                        }
                    }
                }
            }
        }
        
        //one word operations first
//        mainloop:
//        for (int i = 0; i < seq.size() - 1; i++) {
//            
//            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i)) && op.opType == Operation.oneWordOpType) {
//                    
//                    boolean doOperation = false;
//                    
//                    if (i == 0)
//                        doOperation = true;
//                    
//                    else if (isOperation(seq.get(i - 1)))
//                        doOperation = true;
//                    
//                    if (doOperation) {
//                        
//                        String returnType;
//                        if ((returnType = op.getReturnType(seq.get(i + 1))) != null) {
//                            
//                            seq.remove(i + 1);
//                            seq.remove(i);
//                            seq.add(i, returnType);
//                            
//                            continue mainloop;
//                        }
//                        
//                        else {
//                            
//                            //ERROR
//                        }
//                    }
//                }
//            }
//        }
//        
//        //two word operations
//        mainloop:
//        for (int i = 1; i < seq.size() - 1; i++) {
//            
//            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i)) && op.opType == Operation.twoWordOpType) {
//                    
//                    String returnType;
//                    if ((returnType = op.getReturnType(seq.get(i - 1), seq.get(i + 1))) != null) {
//                        
//                        seq.remove(i + 1);
//                        seq.remove(i);
//                        seq.remove(i - 1);
//                        seq.add(i - 1, returnType);
//                        
//                        i--;
//                        
//                        continue mainloop;
//                    }
//                    
//                    else {
//                        
//                        //ERROR
//                    }
//                }
//            }
//        }
//        mainloop:
//        for (int i = 0; i < seq.size() - 1; i++) {
            
            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i))) {
//                    
//                    String returnType;
//                    
//                    switch (op.opType) {
//                        
//                        case Operation.twoWordOpType:
//                        {
//                            if ((returnType = op.getReturnType(seq.get(i - 1), seq.get(i + 1))) != null) {
//                                
//                                seq.remove(i + 1);
//                                seq.remove(i);
//                                seq.remove(i - 1);
//                                seq.add(i - 1, returnType);
//                                
//                                i--;
//                                continue mainloop;
//                            }
//                            
//                            else {
//                                
//                                //ERROR
//                            }
//                        }
//                        case Operation.oneWordOpType:
//                        {
//                            if ((returnType = op.getReturnType(seq.get(i + 1))) != null) {
//                                
//                                seq.remove(i + 1);
//                                seq.remove(i);
//                                seq.add(i, returnType);
//                                
//                                i--;
//                                continue mainloop;
//                            }
//                            
//                            else {
//                                
//                                //ERROR
//                            }
//                        }
//                    }
//                    
//                    continue mainloop;
//                }
//            }
//        }
        
        if (seq.size() > 1) {
            
            //ERROR
            return null;
        }
        
        return seq.get(0);
    }
    
    protected boolean isSeqType(ArrayList<String> seq, String type) {
        
        if (type.equals("@any"))
            return !seq.isEmpty();
        
        String seqType = getSeqType(seq);
        
        if (seqType == null)
            return false;
        
        return ("@" + seqType).equals(type);
    }
    
    public String evaluateSeq(ArrayList<String> sequence) {
        
        ArrayList<String> seq = (ArrayList)sequence.clone();
        
        //evaluate variables first
//        for (int i = 0; i < seq.size(); i++)
//            if (variables.containsKey(seq.get(i)))
//                seq.set(i, variables.get(seq.get(i)).getVal());
        
        //evaluate variables first
        for (int i = 0; i < seq.size(); i++) {
            
            aLoop:
            for (int a = seq.size() - 1; a >= i; a--) {
                
                ArrayList<String> subArray = subArray(seq, i, a);
                String subString = "";
                
                //check if subArray is proper variable name
                for (String string : subArray) {
                    
                    if (subString.length() == 0 && !string.equals("."))
                        subString += string;
                    
                    else if (string.equals(".")) {
                        
                        if (subString.charAt(subString.length() - 1) != '.')
                            subString += string;
                        
                        else
                            continue aLoop;
                    }
                    
                    else {
                        
                        if (subString.charAt(subString.length() - 1) == '.')
                            subString += string;
                        
                        else
                            continue aLoop;
                    }
                }
                
                if (!variables.containsKey(subString))
                    continue;
                
                //remove between i and a
                for (int e = a; e >= i; e--)
                    seq.remove(e);
                
                seq.add(i, variables.get(subString).getVal());
                break;
            }
        }
        
        //evaluate methods
        for (int i = 1; i < seq.size() - 1; i++)
            if (seq.get(i).equals("(")) {
                
                if (methods.containsKey(seq.get(i - 1))) {
                    
                    Method method = methods.get(seq.get(i - 1));
                    
                    ArrayList<Integer> commaPos = new ArrayList<>();
                    int brackets = 0;
                    int curlyBraces = 0;
                    int finalBracketPos = 0;
                    
                    for (int a = i + 1; a < seq.size(); a++) {
                        
                        if (seq.get(a).equals("("))
                            brackets++;
                        else if (seq.get(a).equals(")")) {
                            
                            if (brackets == 0 && curlyBraces == 0) {
                                
                                finalBracketPos = a;
                                break;
                            }
                            brackets--;
                        }
                        else if (seq.get(a).equals("{"))
                            curlyBraces++;
                        else if (seq.get(a).equals("}"))
                            curlyBraces--;
                        
                        else if (seq.get(a).equals(",") && brackets == 0 && curlyBraces == 0)
                            commaPos.add(a);
                    }
                    
                    ArrayList<ArrayList<String>> parameters = new ArrayList<>();
                    
                    int lastCommaPos = i;
                    for (int comma : commaPos) {
                        
                        parameters.add(subArray(seq, lastCommaPos + 1, comma - 1));
                        lastCommaPos = comma;
                    }
                    
                    //add final parameter
                    parameters.add(subArray(seq, lastCommaPos + 1, finalBracketPos - 1));
                    
                    //get parameter values
                    ArrayList<String> paramValues = new ArrayList<>();
                    for (ArrayList<String> parameter : parameters)
                        paramValues.add(evaluateSeq(parameter));
                    
                    //get parameter types
                    ArrayList<String> paramTypes = new ArrayList<>();
                    for (ArrayList<String> parameter : parameters)
                        paramTypes.add(getSeqType(parameter));
                    
                    if (paramTypes.equals(method.getParamTypes())) {
                        
                        seq = trimArray(seq, i - 1, finalBracketPos);
                        seq.add(i - 1, evaluateMethod(method, paramValues));
                    }
                    
                    else {
                        
                        //ERROR
                    }
                }
            }
        
        //evaluate brackets
        while (seq.contains("(")) {
            
            int openBracketIndex = seq.indexOf("(");
            int closeBracketIndex = 0;
            //find endBracketIndex
            int a = 0;
            for (int i = openBracketIndex + 1; i < seq.size(); i++) {
                
                if (seq.get(i).equals("("))
                    a++;
                
                else if (seq.get(i).equals(")")) {
                    
                    if (a == 0) {
                        
                        closeBracketIndex = i;
                        break;
                    }
                    
                    a--;
                }
            }
            
            if (closeBracketIndex == 0) {
                
                //ERROR
                return null;
            }
            
            ArrayList<String> insideBrackets = subArray(seq, openBracketIndex + 1, closeBracketIndex - 1);
            
            //remove everything inside brackets
            for (int i = closeBracketIndex; i >= openBracketIndex; i--)
                seq.remove(i);
            
            seq.add(openBracketIndex, evaluateSeq(insideBrackets));
        }
        
        //evaluate
        for (int i = Operation.topPriority; i >= 0; i--) {
            
            mainloop:
            for (int a = 0; a < seq.size() - 1; a++) {
                
                for (Operation op : operations) {
                    
                    if (op.getSymbol().equals(seq.get(a)) && op.priority == i) {
                        
                        switch (op.opType) {
                            
                            case Operation.oneWordOpType:
                                
                                boolean doOperation = false;
                                
                                if (a == 0)
                                    doOperation = true;
                                
                                else if (isOperation(seq.get(a - 1)))
                                    doOperation = true;
                                
                                if (doOperation) {
                                    
                                    if (op.getReturnType(getWordType(seq.get(a + 1))) != null) {
                                        
                                        String eval = evaluateOp(op, seq.get(a + 1));
                                        
                                        seq.remove(a + 1);
                                        seq.remove(a);
                                        seq.add(a, eval);
                                        
                                        continue mainloop;
                                    }
                                    
                                    else {
                                        
                                        //ERROR
                                    }
                                }
                                break;
                            
                            case Operation.twoWordOpType:
                                
                                if (a == 0) {
                                    
                                    //ERROR
                                }
                                
                                if (op.getReturnType(getWordType(seq.get(a - 1)), getWordType(seq.get(a + 1))) != null) {
                                    
                                    String eval = evaluateOp(op, seq.get(a - 1), seq.get(a + 1));
                                    
                                    seq.remove(a + 1);
                                    seq.remove(a);
                                    seq.remove(a - 1);
                                    seq.add(a - 1, eval);
                                    
                                    a--;
                                    
                                    continue mainloop;
                                }
                                
                                else {
                                    
                                    //ERROR
                                }
                                break;
                        }
                    }
                }
            }
        }
        
        //one word operations first
//        mainloop:
//        for (int i = 0; i < seq.size() - 1; i++) {
//            
//            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i)) && op.opType == Operation.oneWordOpType) {
//                    
//                    boolean doOperation = false;
//                    
//                    if (i == 0)
//                        doOperation = true;
//                    
//                    else if (isOperation(seq.get(i - 1)))
//                        doOperation = true;
//                    
//                    if (doOperation) {
//                        
//                        if (op.getReturnType(getWordType(seq.get(i + 1))) != null) {
//                            
//                            String eval = evaluateOp(op, seq.get(i + 1));
//                            
//                            seq.remove(i + 1);
//                            seq.remove(i);
//                            seq.add(i, eval);
//                            
//                            continue mainloop;
//                        }
//                        
//                        else {
//                            
//                            //ERROR
//                        }
//                    }
//                }
//            }
//        }
//        
//        //two word operations
//        mainloop:
//        for (int i = 1; i < seq.size() - 1; i++) {
//            
//            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i)) && op.opType == Operation.twoWordOpType) {
//                    
//                    if (op.getReturnType(getWordType(seq.get(i - 1)), getWordType(seq.get(i + 1))) != null) {
//                        
//                        String eval = evaluateOp(op, seq.get(i - 1), seq.get(i + 1));
//                        
//                        seq.remove(i + 1);
//                        seq.remove(i);
//                        seq.remove(i - 1);
//                        seq.add(i - 1, eval);
//                        
//                        i--;
//                        
//                        continue mainloop;
//                    }
//                    
//                    else {
//                        
//                        //ERROR
//                    }
//                }
//            }
//        }
//        mainloop:
//        for (int i = 0; i < seq.size() - 1; i++) {
//            
//            //check if index is an operation
//            for (Operation op : operations) {
//                
//                if (op.getSymbol().equals(seq.get(i))) {
//                    
//                    switch (op.opType) {
//                        
//                        case Operation.twoWordOpType:
//                        {
//                            if (i == 0)
//                                continue;
//                            
//                            if (op.getReturnType(getWordType(seq.get(i - 1)), getWordType(seq.get(i + 1))) != null) {
//                                
//                                String eval = evaluateOp(op, seq.get(i - 1), seq.get(i + 1));
//                                
//                                seq.remove(i + 1);
//                                seq.remove(i);
//                                seq.remove(i - 1);
//                                seq.add(i - 1, eval);
//                                
//                                i--;
//                                continue mainloop;
//                            }
//                            
//                            else {
//                                
//                                //ERROR
//                            }
//                        }
//                        case Operation.oneWordOpType:
//                        {
//                            if (op.getReturnType(getWordType(seq.get(i + 1))) != null) {
//                                
//                                String eval = evaluateOp(op, seq.get(i + 1));
//                                
//                                seq.remove(i + 1);
//                                seq.remove(i);
//                                seq.add(i, eval);
//                                
//                                i--;
//                                continue mainloop;
//                            }
//                            
//                            else {
//                                
//                                //ERROR
//                            }
//                        }
//                    }
//                    
//                    continue mainloop;
//                }
//            }
//        }
        
        if (seq.size() > 1) {
            
            //ERROR
            return null;
        }
        
        return seq.get(0);
    }
    
    protected String evaluateMethod(Method method, ArrayList<String> parameters) {
        
        switch (method.getName()) {
            
            case "testMethod":
                try {
                    
                    int i = Integer.parseInt(parameters.get(0));
                    boolean a = Boolean.parseBoolean(parameters.get(1));
                    
                    if (a) {
                        
                        return String.valueOf(i == 1 || i == 3);
                    }
                    
                    else {
                        
                        return String.valueOf(i == 2);
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
        }
        
        return null;
    }
    
    protected String evaluateOp(Operation operation, String parameter) {
        
        String parameterType = getWordType(parameter);
        
        switch (operation.getSymbol()) {
            
            case "!":
                return String.valueOf(!Boolean.parseBoolean(parameter));
            
            case "+":
                return String.valueOf(Integer.parseInt(parameter));
            
            case "-":
                return String.valueOf(-Integer.parseInt(parameter));
        }
        
        return null;
    }
    
    protected String evaluateOp(Operation operation, String parameter1, String parameter2) {
        
        String param1Type = getWordType(parameter1);
        String param2Type = getWordType(parameter2);
        
        switch (operation.getSymbol()) {
            
            case "+":
                try {
                    
                    if (param1Type.equals("int") && param2Type.equals("int")) {
                        
                        return String.valueOf(Integer.parseInt(parameter1) + Integer.parseInt(parameter2));
                    }
                    
                    if (param1Type.equals("float") && param2Type.equals("float")) {
                        
                        return String.valueOf(Float.parseFloat(parameter1) + Float.parseFloat(parameter2));
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
                break;
            case "-":
                try {
                    
                    if (param1Type.equals("int") && param2Type.equals("int")) {
                        
                        return String.valueOf(Integer.parseInt(parameter1) - Integer.parseInt(parameter2));
                    }
                    
                    if (param1Type.equals("float") && param2Type.equals("float")) {
                        
                        return String.valueOf(Float.parseFloat(parameter1) - Float.parseFloat(parameter2));
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
                break;
            case "*":
                try {
                    
                    if (param1Type.equals("int") && param2Type.equals("int")) {
                        
                        return String.valueOf(Integer.parseInt(parameter1) * Integer.parseInt(parameter2));
                    }
                    
                    if (param1Type.equals("float") && param2Type.equals("float")) {
                        
                        return String.valueOf(Float.parseFloat(parameter1) * Float.parseFloat(parameter2));
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
                break;
            case "/":
                try {
                    
                    if (param1Type.equals("int") && param2Type.equals("int")) {
                        
                        return String.valueOf(Integer.parseInt(parameter1) / Integer.parseInt(parameter2));
                    }
                    
                    if (param1Type.equals("float") && param2Type.equals("float")) {
                        
                        return String.valueOf(Float.parseFloat(parameter1) / Float.parseFloat(parameter2));
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
                break;
            case "%":
                try {
                    
                    if (param1Type.equals("int") && param2Type.equals("int")) {
                        
                        return String.valueOf(Integer.parseInt(parameter1) % Integer.parseInt(parameter2));
                    }
                    
                    if (param1Type.equals("float") && param2Type.equals("float")) {
                        
                        return String.valueOf(Float.parseFloat(parameter1) % Float.parseFloat(parameter2));
                    }
                }
                catch (NumberFormatException | NullPointerException e) {}
                break;
            case "||":
                return String.valueOf(Boolean.parseBoolean(parameter1) || Boolean.parseBoolean(parameter2));
            case "&&":
                return String.valueOf(Boolean.parseBoolean(parameter1) && Boolean.parseBoolean(parameter2));
            case "==":
                return String.valueOf(parameter1.equals(parameter2));
        }
        
        return null;
    }
    
    protected boolean isOperation(String string) {
        
        for (Operation op : operations) {
            
            if (op.getSymbol().equals(string))
                return true;
        }
        
        return false;
    }
    
    protected boolean isValue(String string, String valType) {
        
        switch (valType) {
            
            case "val":
                for (String type : valTypes)
                    if (isValue(string, type))
                        return true;
                return false;
            case "int":
                try {
                    Integer.parseInt(string);
                    return true;
                } catch (NumberFormatException | NullPointerException e) {return false;}
            case "float":
                try {
                    Float.parseFloat(string);
                    return true;
                } catch (NumberFormatException | NullPointerException e) {return false;}
            case "bool":
                if (string == null)
                    return false;
                return string.equals("true") || string.equals("false");
        }
        
        return false;
    }
    
    public boolean isForm(ArrayList<String> inputLine, ArrayList<String> form) {
        
        ArrayList<String> line = (ArrayList)inputLine.clone();
        
        line = shortenName(line);
        
        //current index in line
        int i = 0;
        
        mainloop:
        for (int a = 0; a < form.size(); a++) {
            
            String str = form.get(a);
            
            if (i >= line.size())
                return false;
            
            if (str.startsWith("@")) {
                
                int eStartingIndex;
                
                if (a == form.size() - 1)
                    eStartingIndex = line.size() - 1;
                
                else {
                    
                    eStartingIndex = getLastIndexOf(new ArrayList<>(Arrays.asList(form.get(a + 1))), line) - 1;
                }
                
                for (int e = eStartingIndex; e > i; e--) {
                    
                    if (isSeqType(subArray(line, i, e), str)) {
                        
                        i = e + 1;
                        continue mainloop;
                    }
                }
                
                if (isWordType(line.get(i), str)) {
                    
                    i++;
                    continue;
                }
                
                return false;
            }
            
            else {
                
                if (!line.get(i).equals(str))
                    return false;
                
                i++;
            }
        }
        
        return true;
    }
    
    protected ArrayList<ArrayList<String>> getUnknowns(ArrayList<String> line, ArrayList<String> form) {
        
        ArrayList<String> seq = (ArrayList)line.clone();
        
        ArrayList<ArrayList<String>> splitForm = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        
        //create splitForm and indices
        for (int i = 0; i < form.size(); i++) {
            
            if (i == 0) {
                
                splitForm.add(new ArrayList<>(Arrays.asList(form.get(i))));
                
                if (form.get(i).startsWith("@"))
                    indices.add(i);
                
                continue;
            }
            
            if (form.get(i).startsWith("@")) {
                
                if (splitForm.get(splitForm.size() - 1).get(0).startsWith("@"))
                    splitForm.get(splitForm.size() - 1).add(form.get(i));
                
                else {
                    
                    splitForm.add(new ArrayList<>(Arrays.asList(form.get(i))));
                    indices.add(splitForm.size() - 1);
                }
            }
            
            else {
                
                if (!splitForm.get(splitForm.size() - 1).get(0).startsWith("@"))
                    splitForm.get(splitForm.size() - 1).add(form.get(i));
                
                else
                    splitForm.add(new ArrayList<>(Arrays.asList(form.get(i))));
            }
        }
        
        ArrayList<ArrayList<String>> stringArray = new ArrayList<>();
        
        for (int i : indices) {
            
            if (i == 0) {
                
                if (i == splitForm.size() - 1) {
                    
                    stringArray.add((ArrayList)seq.clone());
                    seq.clear();
                    break;
                }
                
                int endIndex = getFirstIndexOf(splitForm.get(i + 1), seq) - 1;
                
                stringArray.add(subArray(seq, 0, endIndex));
                
                seq = trimArray(seq, 0, endIndex);
            }
            
            else if (i == splitForm.size() - 1) {
                
                int startIndex = getFirstIndexOf(splitForm.get(i - 1), seq) + splitForm.get(i - 1).size();
                
                stringArray.add(subArray(seq, startIndex, seq.size() - 1));
                
                seq.clear();
            }
            
            else if (i == splitForm.size() - 2) {
                
                int startIndex = getFirstIndexOf(splitForm.get(i - 1), seq) + splitForm.get(i - 1).size();
                int endIndex = getLastIndexOf(splitForm.get(i + 1), seq) - 1;
                
                stringArray.add(subArray(seq, startIndex, endIndex));
                
                seq = trimArray(seq, startIndex - splitForm.get(i - 1).size(), endIndex + splitForm.get(i + 1).size());
            }
            
            else {
                
                int startIndex = getFirstIndexOf(splitForm.get(i - 1), seq) + splitForm.get(i - 1).size();
                int endIndex = getLastIndexOf(splitForm.get(i + 1), seq) - 1;
                
                stringArray.add(subArray(seq, startIndex, endIndex));
                
                seq = trimArray(seq, startIndex - splitForm.get(i - 1).size(), endIndex);
            }
        }
        
        //splitSeq
        for (int i : indices) {
            
            int size;
            
            if ((size = splitForm.get(i).size()) > 1) {
                
                int stringArrayIndex = indices.indexOf(i);
                
                ArrayList<ArrayList<String>> splitSeq = splitSeq(stringArray.get(stringArrayIndex), splitForm.get(i));
                
                stringArray.remove(stringArrayIndex);
                for (int a = 0; a < splitSeq.size(); a++)
                    stringArray.add(stringArrayIndex + a, splitSeq.get(a));
            }
        }
        
        return stringArray;
    }
    
    protected ArrayList<String> shortenName(ArrayList<String> sequence) {
        
        ArrayList<String> seq = (ArrayList)sequence.clone();
        
        //multi-string variables and methods
        for (int i = 0; i < seq.size(); i++) {
            
            for (int a = seq.size() - 1; a >= i; a--) {
                
                ArrayList<String> subArray = subArray(seq, i, a);
                String subString;
                
                //check if subArray is proper name format
                subString = shortenNameWrapped(subArray);
                if (subString == null)
                    continue;
                
                if (!(variables.containsKey(subString) || methods.containsKey(subString)))
                    continue;
                
                //remove between i and a
                for (int e = a; e >= i; e--)
                    seq.remove(e);
                
                seq.add(i, subString);
                break;
            }
        }
        
        return seq;
    }
    
    protected String shortenNameWrapped(ArrayList<String> seq) {
        
        String subString = "";
        
        for (String string : seq) {
            
            if (subString.length() == 0) {
                
                if (!string.equals("."))
                    subString += string;
                
                else
                    return null;
            }
            
            else if (string.equals(".")) {
                
                if (subString.charAt(subString.length() - 1) != '.')
                    subString += string;
                
                else
                    return null;
            }
            
            else {
                
                if (subString.charAt(subString.length() - 1) == '.')
                    subString += string;
                
                else
                    return null;
            }
        }
        
        return subString;
    }
    
    protected ArrayList<String> shortenNum(ArrayList<String> sequence) {
        
        ArrayList<String> seq = (ArrayList)sequence.clone();
        
        //get rid of positive and negative signs
        for (int i = 0; i < seq.size() - 1; i++) {
            
            for (Operation op : operations) {
                
                if (op.getSymbol().equals(seq.get(i)) && op.opType == Operation.oneWordOpType) {
                    
                    if (op.opName.equals("pos") || op.opName.equals("neg")) {
                        
                        String eval = evaluateOp(op, seq.get(i + 1));
                        
                        seq.remove(i + 1);
                        seq.remove(i);
                        seq.add(eval);
                    }
                }
            }
        }
        
        return seq;
    }
    
    protected ArrayList<ArrayList<String>> splitSeq(ArrayList<String> string, ArrayList<String> form) {
        
        ArrayList<String> seq = (ArrayList)string.clone();
        
        ArrayList<ArrayList<String>> stringArray = new ArrayList<>();
        
        for (String str : form) {
            
            if (isSeqType(subArray(string, 0, string.size() - 1), str)) {
                
                stringArray.add(new ArrayList<>(string));
                string.clear();
                
                break;
            }
            
            for (int i = string.size() - 1; i >= 0; i--) {
                
                if (isSeqType(subArray(string, 0, i), str)) {
                    
                    stringArray.add(subArray(string, 0, i));
                    string = subArray(string, i + 1, string.size() - 1);
                    
                    break;
                }
            }
        }
        
        return stringArray;
    }
    
    public void setProgram(ArrayList<ArrayList<String>> program) {
        
        this.program = program;
    }
    
    public void setCompiler(Compiler compiler) {
        
        this.compiler = compiler;
    }
    
    public int findStartLine() {
        
        return 0;
    }
    
    public int getCurrentLine() {
        
        return currentLine;
    }
    
    public int getNextLine() {
        
        return nextLine;
    }
    
    public static String getName() {
        
        return name;
    }
    
    protected static ArrayList<String> subArray(ArrayList<String> list, int index1, int index2) {
        
        ArrayList<String> between = new ArrayList<>();
        
        for (int i = index1; i <= index2; i++)
            between.add(list.get(i));
        
        return between;
    }
    
    protected static ArrayList<String> trimArray(ArrayList<String> list, int index1, int index2) {
        
        ArrayList<String> array = (ArrayList)list.clone();
        
        for (int i = index2; i >= index1; i--)
            array.remove(i);
        
        return array;
    }
    
    protected static int getFirstIndexOf(ArrayList<String> seq, ArrayList<String> line) {
        
        mainloop:
        for (int i = 0; i < line.size(); i++) {
            
            for (int a = 0; a < seq.size(); a++) {
                
                if (!line.get(i + a).equals(seq.get(a)))
                    continue mainloop;
            }
            
            return i;
        }
        
        return 0;
    }
    
    protected static int getLastIndexOf(ArrayList<String> seq, ArrayList<String> line) {
        
        mainloop:
        for (int i = line.size() - 1 - (seq.size() - 1); i >= 0; i--) {
            
            for (int a = 0; a < seq.size(); a++) {
                
                if (!line.get(i + a).equals(seq.get(a)))
                    continue mainloop;
            }
            
            return i;
        }
        
        return 0;
    }
    
    protected static String toString(ArrayList<String> seq) {
        
        String returnValue = "";
        
        for (String str : seq)
            returnValue += str;
        
        return returnValue;
    }
}

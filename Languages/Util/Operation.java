

package Languages.Util;

import java.util.ArrayList;
import java.util.Arrays;


public class Operation {
    
    public String opName;
    public String opSymbol;
    public int priority;
    public static final int twoWordOpType = 0;
    public static final int oneWordOpType = 1;
    //0: two value operation (1 + 1), 1: one value operation before (!true), 2: one value operation after (true!)
    public int opType;
    public ArrayList<String> wordTypeBefore;
    public ArrayList<String> wordTypeAfter;
    public ArrayList<String> returnType;
    
    public static final int topPriority = 11;
    
    public Operation(String opName, String opSymbol, int priority, int opType, ArrayList<String> valTypeBefore, ArrayList<String> valTypeAfter, ArrayList<String> returnType) {
        
        this.opName = opName;
        this.opSymbol = opSymbol;
        this.priority = priority;
        this.opType = opType;
        this.wordTypeBefore = valTypeBefore;
        this.wordTypeAfter = valTypeAfter;
        this.returnType = returnType;
    }
    
    public String getSymbol() {
        
        return this.opSymbol;
    }
    
    public String getReturnType(String wordTypeBefore, String wordTypeAfter) {
        
        for (int i = 0; i < this.wordTypeBefore.size(); i++) {
            
            if (this.wordTypeBefore.get(i).equals(wordTypeBefore) && this.wordTypeAfter.get(i).equals(wordTypeAfter))
                return returnType.get(i);
        }
        
        return null;
    }
    
    public String getReturnType(String wordTypeAfter) {
        
        for (int i = 0; i < this.wordTypeAfter.size(); i++) {
            
            if (this.wordTypeAfter.get(i).equals(wordTypeAfter))
                return returnType.get(i);
        }
        
        return null;
    }
    
    public static ArrayList<Operation> getStandardOps() {
        
        return new ArrayList<>(Arrays.asList(getAddOp(), getPosOp(), getSubOp(), getNegOp(), getMulOp(), getDivOp(), getModOp(), getNotOp(), getOrOp(), getAndOp(), getEqualsOp(), getNotEqualsOp(), getGreaterThanOp(), getLessThanOp(), getGreaterEqualsOp(), getLessEqualsOp(), getBitwiseNotOp(), getBitwiseOrOp(), getBitwiseAndOp(), getBitwiseXorOp(), getShiftLeftOp(), getShiftRightOp(), getShiftRightUnsignedOp()));
    }
    
    public static Operation getAddOp() {
        
        return new Operation("add", "+", 9, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getPosOp() {
        
        return new Operation("pos", "+", 11, oneWordOpType, null, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getSubOp() {
        
        return new Operation("sub", "-", 9, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getNegOp() {
        
        return new Operation("neg", "-", 11, oneWordOpType, null, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getMulOp() {
        
        return new Operation("mul", "*", 10, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getPowOp() {
        
        return new Operation("pow", "**", 10, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getDivOp() {
        
        return new Operation("div", "/", 10, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getModOp() {
        
        return new Operation("mod", "%", 10, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")), new ArrayList<>(Arrays.asList("int", "float")));
    }
    
    public static Operation getNotOp() {
        
        return new Operation("not", "!", 11, oneWordOpType, null, new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")));
    }
    
    public static Operation getOrOp() {
        
        return new Operation("or", "||", 0, twoWordOpType, new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")));
    }
    
    public static Operation getAndOp() {
        
        return new Operation("and", "&&", 2, twoWordOpType, new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")));
    }
    
    public static Operation getXorOp() {
        
        return new Operation("xor", "^^", 1, twoWordOpType, new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")), new ArrayList<>(Arrays.asList("bool")));
    }
    
    public static Operation getEqualsOp() {
        
        return new Operation("equals", "==", 6, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getNotEqualsOp() {
        
        return new Operation("notEquals", "!=", 6, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getGreaterThanOp() {
        
        return new Operation("greaterThan", ">", 7, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getLessThanOp() {
        
        return new Operation("lessThan", "<", 7, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getGreaterEqualsOp() {
        
        return new Operation("greaterEquals", ">=", 7, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getLessEqualsOp() {
        
        return new Operation("lessEquals", "<=", 7, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("bool", "bool", "bool")));
    }
    
    public static Operation getBitwiseNotOp() {
        
        return new Operation("bitwiseNot", "~", 11, oneWordOpType, null, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseOrOp() {
        
        return new Operation("bitwiseOr", "|", 3, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseAndOp() {
        
        return new Operation("bitwiseAnd", "&", 5, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseXorOp() {
        
        return new Operation("bitwiseXor", "^", 4, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseNorOp() {
        
        return new Operation("bitwiseNor", "~|", 3, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseNandOp() {
        
        return new Operation("bitwiseNand", "~&", 5, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getBitwiseXnorOp() {
        
        return new Operation("bitwiseXnor", "~^", 4, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getShiftLeftOp() {
        
        return new Operation("shiftLeft", "<<", 8, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getShiftRightOp() {
        
        return new Operation("shiftRight", ">>", 8, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getShiftRightUnsignedOp() {
        
        return new Operation("shiftRightUnsigned", ">>>", 8, twoWordOpType, new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")), new ArrayList<>(Arrays.asList("int", "float", "bool")));
    }
    
    public static Operation getTestOp() {
        
        return new Operation("test", "$", 0, twoWordOpType, new ArrayList<>(Arrays.asList("int", "bool")), new ArrayList<>(Arrays.asList("bool", "int")), new ArrayList<>(Arrays.asList("int", "bool")));
    }
}

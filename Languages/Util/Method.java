

package Languages.Util;

import java.util.ArrayList;


public class Method {
    
    private String name;
    private ArrayList<String> paramTypes;
    private String returnType;
    
    public Method(String name, ArrayList<String> paramTypes, String returnType) {
        
        this.name = name;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
    }

    public String getName() {
        
        return name;
    }

    public ArrayList<String> getParamTypes() {
        
        return paramTypes;
    }

    public String getReturnType() {
        
        return returnType;
    }
}

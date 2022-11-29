

package Languages.Util;


public class Variable {
    
    private String name;
    private String type;
    private String val;
    
    public Variable(String name, String type) {
        
        this(name, type, null);
    }
    
    public Variable(String name, String type, String val) {
        
        this.name = name;
        this.type = type;
        this.val = val;
    }

    public String getName() {
        
        return name;
    }

    public String getType() {
        
        return type;
    }

    public String getVal() {
        
        return val;
    }

    public void setName(String name) {
        
        this.name = name;
    }

    public void setType(String type) {
        
        this.type = type;
    }

    public void setVal(String val) {
        
        this.val = val;
    }
    
    @Override
    public Variable clone() {
        
//        try {
//            
//            return (Variable)super.clone();
//        }
//        
//        catch (CloneNotSupportedException e) {
//            
//            throw new InternalError(e);
//        }
        
        return new Variable(getName(), getType(), getVal());
    }
}

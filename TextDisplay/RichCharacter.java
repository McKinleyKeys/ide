

package TextDisplay;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class RichCharacter {
    
    private char ch;
    private int font;
    private int color;
    
    public RichCharacter(char ch, int font, int color) {
        
        this.ch = ch;
        this.font = font;
        this.color = color;
    }
    
    public char getChar() {
        
        return ch;
    }
    
    public void setChar(char ch) {
        
        this.ch = ch;
    }
    
    public TrueTypeFont getFont() {
        
        return TextDisplay.getFont(font);
    }
    
    public int getFontIndex() {
        
        return font;
    }
    
    public void setFont(int font) {
        
        this.font = font;
    }
    
    public Color getColor() {
        
        return TextDisplay.getColor(color);
    }
    
    public int getColorIndex() {
        
        return color;
    }
    
    public void setColor(int color) {
        
        this.color = color;
    }
    
    public int getWidth() {
        
        return getFont().getWidth(Character.toString(ch));
    }
    
    public int getHeight() {
        
        return getFont().getHeight(Character.toString(ch));
    }
}

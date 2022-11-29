

package TextDisplay;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import Compiler.Compiler;
import TextEditor.Main;
import java.io.BufferedReader;
import java.io.FileReader;


public class TextDisplay {
    
    public static final String[] fontNames = {"Monospaced", "Calibri", "Apple Chancery"};
    public static final TrueTypeFont[] fonts = new TrueTypeFont[fontNames.length];
    public static final int fontSize = 20;
    public int selectedFont = 0;
    
    public static final Color[] colors = {Color.black, Color.blue, Color.cyan, Color.green, Color.magenta, Color.orange,
                                          Color.pink, Color.red, Color.white, Color.yellow};
    public int selectedColor = 0;
    
    public ArrayList<ArrayList<RichCharacter>> text = new ArrayList<>(Arrays.asList(new ArrayList<>()));
    
    public int scrollX = 0;
    public int scrollY = 0;
    
    public static final int marginX = 20;
    public static final int marginY = 10;
    
    public static final int lineSpacing = 5;
    
    public int cursorLine;
    public int cursorChar;
    /* store the x and y positions of the cursor and update them whenever it moves
     * these value ignore scrolling */
    public int cursorPosX = marginX;
    public int cursorPosY = marginY;
    public final float cursorWidth = 2;
    
    public final boolean cursorFlashing = true;
    public final long cursorFlashingInterval = 500000000;
    public long cursorLastFlash;
    
    public static boolean drawCustomMouse = true;
    public static final float mouseSizeX = 10;
    public static final float mouseSizeY = 20;
    //if the previous mouse click was inside the margins
    public boolean mouseClickInMargins;
    
    public int highlight1PosX;
    public int highlight1PosY;
    public int highlight2PosX;
    public int highlight2PosY;
    public int highlight1Line;
    public int highlight1Char;
    public int highlight2Line;
    public int highlight2Char;
    
    public static final float highlightTransparency = 0.4f;
    public static final float[] highlightColor = {0f, 0f, 1f};
    
    public ArrayList<ArrayList<RichCharacter>> clipboard = new ArrayList<>();
    
    public Integer runScreen;
    
    public static final String saveFileName = "PiProgram";
    public static final String loadFileName = "PiProgram";
    public static final String fileExtension = ".txt";
    
    public static HashMap<String, Character> charKeyNames = new HashMap<String, Character>() {{
        put("GRAVE", '`');  put("MINUS", '-');  put("EQUALS", '='); put("LBRACKET", '[');
        put("RBRACKET", ']');   put("BACKSLASH", '\\'); put("SEMICOLON", ';');  put("APOSTROPHE", '\'');
        put("COMMA", ',');  put("PERIOD", '.'); put("SLASH", '/');
        put("SPACE", ' ');  put("TAB", '\t');
    }};
    
    public static HashMap<Character, Character> charCapital = new HashMap<Character, Character>() {{
        put('`', '~');  put('1', '!');  put('2', '@');  put('3', '#');  put('4', '$');  put('5', '%');
        put('6', '^');  put('7', '&');  put('8', '*');  put('9', '(');  put('0', ')');  put('-', '_');
        put('=', '+');  put('[', '{');  put(']', '}');  put('\\', '|'); put(';', ':');  put('\'', '"');
        put(',', '<');  put('.', '>');  put('/', '?');  put(' ', ' ');
    }};

    public TextDisplay() {
        
    }
    
    public void initialise() {
        
        //should be called after initDisplay
        
        setCursorPos(0, 0);
    }
    
    public static void initDisplay() {
        
        try {
            
            Display.setDisplayMode(Display.getAvailableDisplayModes()[0]);
            Display.setFullscreen(true);
            Display.setVSyncEnabled(true);
            Display.create();
            Mouse.create();
            Keyboard.create();
        }
        
        catch (LWJGLException e) {
            
            e.printStackTrace();
            System.exit(0);
        }
        
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        
        glClearColor(1, 1, 1, 0.0f);
        glClearDepth(1);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        
        for (int a = 0; a < fonts.length; a++)
            fonts[a] = new TrueTypeFont(new Font(fontNames[a], Font.PLAIN, fontSize), true);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        Mouse.setGrabbed(drawCustomMouse);
        Keyboard.enableRepeatEvents(true);
    }
    
    public void render() {
        
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();
        
        drawText();
        drawCursor();
        drawMargins();
        drawMouse();
        
        Display.update();
        
        Display.sync(60);
    }
    
    public void drawText() {
        
        int lastLinePosY = Display.getHeight() - marginY + scrollY;
        for (ArrayList<RichCharacter> line : text) {
            
            //get the height of this line
            int lineHeight = line.isEmpty() ? getSelectedFont().getHeight() : 0;
            for (RichCharacter ch : line) {
                int charHeight = ch.getHeight();
                if (charHeight > lineHeight)
                    lineHeight = charHeight;
            }
            
            int linePosY = lastLinePosY - lineHeight;
            
            if (linePosY < Display.getHeight() - marginY && lastLinePosY > marginY) {
                
                //draw each visible character
                int charPosX = marginX - scrollX;
                for (RichCharacter ch : line) {
                    
                    int charWidth = ch.getWidth();
                    if (charPosX + charWidth > marginX)
                        drawString(ch.getFont(), charPosX, linePosY + lineHeight / 2f - ch.getHeight() / 2f, Character.toString(ch.getChar()), ch.getColor());
                    
//                    drawLine(charPosX, linePosY + lineHeight / 2f - ch.getHeight() / 2f, charPosX + ch.getWidth(), linePosY + lineHeight / 2f - ch.getHeight() / 2f, 1, 0, 1, 0);
//                    drawLine(charPosX, linePosY + lineHeight / 2f - ch.getHeight() / 2f, charPosX, linePosY + lineHeight / 2f + ch.getHeight() / 2f, 1, 0, 1, 0);
//                    drawLine(charPosX, linePosY + lineHeight / 2f + ch.getHeight() / 2f, charPosX + ch.getWidth(), linePosY + lineHeight / 2f + ch.getHeight() / 2f, 1, 0, 1, 0);
//                    drawLine(charPosX + ch.getWidth(), linePosY + lineHeight / 2f - ch.getHeight() / 2f, charPosX + ch.getWidth(), linePosY + lineHeight / 2f + ch.getHeight() / 2f, 1, 0, 1, 0);
//                    drawLine(charPosX + ch.getWidth() / 2f, linePosY + lineHeight / 2f - ch.getHeight() / 2f, charPosX + ch.getWidth() / 2f, linePosY + lineHeight / 2f + ch.getHeight() / 2f, 1, 1, 0, 0);
                    
                    charPosX += charWidth;
                    
                    if (charPosX >= Display.getWidth() - marginX)
                        break;
                }
            }
            
            lastLinePosY = linePosY - lineSpacing;
        }
        
        //draw highlights
        if (highlight1Line == highlight2Line) {
            if (highlight1Char != highlight2Char)
                drawTransparentQuad(getHighlightPosX(0), getHighlightPosY(0), getHighlightPosX(1), getHighlightPosY(1) + getLineHeight(highlight2Line),
                                    highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
        }
        
        else {
            
            int firstLine = (highlight1Line < highlight2Line) ? highlight1Line : highlight2Line;
            int firstChar = (highlight1Line < highlight2Line) ? highlight1Char : highlight2Char;
            int lastLine = (highlight1Line < highlight2Line) ? highlight2Line : highlight1Line;
            int lastChar = (highlight1Line < highlight2Line) ? highlight2Char : highlight1Char;
            
            int firstLinePosY = getLinePosY(firstLine);
            drawTransparentQuad(getCharPosX(firstChar, firstLine), firstLinePosY, Display.getWidth() - marginX, firstLinePosY + getLineHeight(firstLine),
                                highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
            
            for (int line = firstLine + 1; line <= lastLine - 1; line++) {
                int linePosY = getLinePosY(line);
                drawTransparentQuad(getCharPosX(0, line), linePosY, Display.getWidth() - marginX, linePosY + getLineHeight(line),
                                    highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
            }
            
            int finalLinePosY = getLinePosY(lastLine);
            drawTransparentQuad(getCharPosX(0, lastLine), finalLinePosY, getCharPosX(lastChar, lastLine), finalLinePosY + getLineHeight(lastLine),
                                highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
        }
    }
    
    public void drawCursor() {
        
        boolean drawCursor = true;
        
        if (System.nanoTime() - cursorLastFlash >= cursorFlashingInterval && cursorFlashing) {
            
            if (System.nanoTime() - cursorLastFlash >= 2 * cursorFlashingInterval)
                cursorLastFlash = System.nanoTime();
            else
                drawCursor = false;
        }
        
        if (drawCursor)
            drawQuad(getCursorPosX(), getCursorPosY(), getCursorPosX() + cursorWidth, getCursorPosY() + getSelectedFont().getHeight(), 0f, 0f, 0f);
    }
    
    public void drawMouse() {
        
        if (!drawCustomMouse)
            return;
        
        int posX = Mouse.getX();
        int posY = Mouse.getY();
        
        //horizontal lines
        drawLine(posX - mouseSizeX / 2, posY - mouseSizeY / 2, posX - mouseSizeX * 0.4f / 2, posY - mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX + mouseSizeX / 2, posY - mouseSizeY / 2, posX + mouseSizeX * 0.4f / 2, posY - mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX - mouseSizeX / 2, posY + mouseSizeY / 2, posX - mouseSizeX * 0.4f / 2, posY + mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX + mouseSizeX / 2, posY + mouseSizeY / 2, posX + mouseSizeX * 0.4f / 2, posY + mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX - mouseSizeX / 6 - 1, posY, posX + mouseSizeX / 6, posY, 1, 0f, 0f, 0f);
        
        //vertical line
        drawLine(posX, posY - mouseSizeY * 0.8f / 2, posX, posY + mouseSizeY * 0.8f / 2, 1, 0f, 0f, 0f);
        
        //diagonal lines
        drawLine(posX, posY + mouseSizeY * 0.8f / 2, posX - mouseSizeX * 0.4f / 2, posY + mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX, posY + mouseSizeY * 0.8f / 2, posX + mouseSizeX * 0.4f / 2, posY + mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX, posY - mouseSizeY * 0.8f / 2, posX - mouseSizeX * 0.4f / 2, posY - mouseSizeY / 2, 1, 0f, 0f, 0f);
        drawLine(posX, posY - mouseSizeY * 0.8f / 2, posX + mouseSizeX * 0.4f / 2, posY - mouseSizeY / 2, 1, 0f, 0f, 0f);
    }
    
    public void drawMargins() {
        
        drawQuad(0, 0, marginX, Display.getHeight(), 1f, 1f, 1f);
        drawQuad(0, Display.getHeight(), Display.getWidth(), Display.getHeight() - marginY, 1f, 1f, 1f);
        drawQuad(Display.getWidth(), Display.getHeight(), Display.getWidth() - marginX, 0, 1f, 1f, 1f);
        drawQuad(Display.getWidth(), 0, 0, marginY, 1f, 1f, 1f);
        
//        drawLine(marginX, marginY, Display.getWidth() - marginX, marginY, 1, 0f, 0f, 0f);
//        drawLine(marginX, marginY, marginX, Display.getHeight() - marginY, 1, 0f, 0f, 0f);
//        drawLine(marginX, Display.getHeight() - marginY, Display.getWidth() - marginX, Display.getHeight() - marginY, 1, 0f, 0f, 0f);
//        drawLine(Display.getWidth() - marginX, marginY, Display.getWidth() - marginX, Display.getHeight() - marginY, 1, 0f, 0f, 0f);
    }
    
    public void getInput() {

        //mouse inputs
        Mouse.poll();
        
        while (Mouse.next()) {
            
            if (Mouse.getEventButton() == 0) {
                
                if (Mouse.getX() > marginX && Mouse.getX() < Display.getWidth() - marginX &&
                    Mouse.getY() > marginY && Mouse.getY() < Display.getHeight() - marginY) {
                    
                    boolean buttonState = Mouse.getEventButtonState();
                    
                    if (buttonState) {
                        mouseClickInMargins = true;
                        cursorLastFlash = System.nanoTime();
                    }
                    
                    if (mouseClickInMargins) {
                        
                        int line = getLineAt(Mouse.getY());
                        int ch = getCharAt(Mouse.getX(), line);
                        
                        setCursorPos(ch, line);
                        
                        if (buttonState)
                            setHighlightPos(0, ch, line);
                        else
                            setHighlightPos(1, ch, line);
                    }
                }
                
                else
                    mouseClickInMargins = false;
            }
        }
        
        if (Mouse.isButtonDown(0) && mouseClickInMargins) {
            
            int line = getLineAt(Mouse.getY());
            int ch = getCharAt(Mouse.getX(), line);
            
            setCursorPos(ch, line);
            setHighlightPos(1, ch, line);
        }
        
        int scroll = Mouse.getDWheel();
        
        if (scroll != 0) {
            
            //scroll up
            if (scroll > 0)
                scrollY -= 20;
            
            //scroll down
            else
                scrollY += 20;
        }

        //keyboard inputs
        Keyboard.poll();

        while (Keyboard.next()) {

            if (Keyboard.getEventKeyState()) {

                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {

                    try {
                        
                        if (Display.isFullscreen()) {
                            Display.setFullscreen(false);
                            Mouse.setGrabbed(false);
                            drawCustomMouse = false;
                        }
                        
                        else {
                            Display.setFullscreen(true);
                            Mouse.setGrabbed(true);
                            drawCustomMouse = true;
                        }
                        
                        render();
                        continue;
                    }
                    
                    catch (LWJGLException ex) {

                        Logger.getLogger(TextEditor.Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                String keyName = Keyboard.getKeyName(Keyboard.getEventKey());
                System.out.println(keyName);
                
                cursorLastFlash = System.nanoTime();
                
                switch (keyName) {
                    
                    case "ESCAPE":
                        continue;
                    case "BACK":
                        delete();
                        continue;
                    case "RETURN":
                        enter();
                        continue;
                    case "LEFT":    case "RIGHT":   case "UP":  case "DOWN":
                        moveCursor(keyName, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
                        continue;
                    case "S":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            save();
                            continue;
                        }
                        break;
                    case "L":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            load();
                            continue;
                        }
                        break;
                    case "A":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            selectAll();
                            continue;
                        }
                        break;
                    case "C":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            copy();
                            continue;
                        }
                        break;
                    case "V":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            paste();
                            continue;
                        }
                        break;
                    case "X":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA)) {
                            cut();
                            continue;
                        }
                        break;
                    case "PERIOD":
                        scrollX += 5;
                        continue;
                    case "COMMA":
                        scrollX -= 5;
                        continue;
                    case "F1":
                        switchDisplay(0);
                        continue;
                    case "F2":
                        switchDisplay(1);
                        continue;
                    case "F3":
                        switchDisplay(2);
                        continue;
                    case "F4":
                        switchDisplay(3);
                        continue;
                    case "F5":
                        switchDisplay(4);
                        continue;
                    case "F6":
                        if (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA))
                            switchDisplay(5);
                        else
                            runProgram();
                        continue;
                    case "F7":
                        switchDisplay(6);
                        continue;
                    case "F8":
                        switchDisplay(7);
                        continue;
                }
                
                //if key is a character
                boolean containsKey = charKeyNames.containsKey(keyName);
                if (containsKey || keyName.length() == 1) {
                    
                    char keyChar = containsKey ? charKeyNames.get(keyName) : keyName.charAt(0);
                    
                    boolean LShiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                    boolean RShiftDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                    boolean capitalDown = Keyboard.isKeyDown(Keyboard.KEY_CAPITAL);
                    
                    write(getChar(keyChar, LShiftDown || RShiftDown, capitalDown));
                }
            }
        }
    }
    
    public void write(RichCharacter ch) {
        
        text.get(cursorLine).add(cursorChar, ch);
        setCursorPos(cursorChar + 1, cursorLine);
    }
    
    public void write(char ch, int font, int color) {
        
        write(new RichCharacter(ch, font, color));
    }
    
    public void write(char ch) {
        
        write(new RichCharacter(ch, selectedFont, selectedColor));
    }
    
    public void write(String string) {
        
        write(string, selectedFont, selectedColor);
    }
    
    public void write(String string, int font, int color) {
        
        for (char ch : string.toCharArray())
            write(new RichCharacter(ch, font, color));
    }
    
    public void write(String string, int font, int color, int line, int ch) {
        
        for (int a = 0; a < string.length(); a++)
            text.get(line).add(ch + a, new RichCharacter(string.charAt(a), font, color));
    }
    
    public char getChar(char keyChar, boolean shiftDown, boolean capsLockDown) {
        
        if (shiftDown) {
            
            if (charCapital.containsKey(keyChar))
                return charCapital.get(keyChar);
            
            return Character.toUpperCase(keyChar);
        }
        
        else if (capsLockDown)
            return Character.toUpperCase(keyChar);
        
        else
            return Character.toLowerCase(keyChar);
    }
    
    public void enter() {
        
        enter(cursorLine, cursorChar);
    }
    
    public void enter(int line, int ch) {
        
        text.add(line + 1, new ArrayList<>());
        
        for (int a = ch; a < text.get(line).size();) {
            text.get(line + 1).add(text.get(line).get(a));
            text.get(line).remove(a);
        }
        
        setCursorPos(0, line + 1);
    }
    
    public void moveCursor(String directionName, boolean shiftDown) {
        
        switch (directionName) {
            
            case "LEFT":
                
                if (highlight1Line == highlight2Line && highlight1Char == highlight2Char) {
                    if (cursorChar > 0)
                        setCursorPos(cursorChar - 1, cursorLine);
                    else if (cursorLine > 0)
                        setCursorPos(text.get(cursorLine - 1).size(), cursorLine - 1);
                }
                
                else {
                    
                    if (highlight1Line == highlight2Line) {
                        
                        int firstChar = (highlight1Char < highlight2Char) ? highlight1Char : highlight2Char;
                        
                        setCursorPos(firstChar, highlight1Line);
                        
                        setHighlightPos(0, firstChar, highlight1Line);
                        setHighlightPos(1, firstChar, highlight2Line);
                    }
                    
                    else {
                        
                        int firstLine = (highlight1Line < highlight2Line) ? highlight1Line : highlight2Line;
                        int firstChar = (highlight1Line < highlight2Line) ? highlight1Char : highlight2Char;
                        
                        setCursorPos(firstChar, firstLine);
                        
                        setHighlightPos(0, firstChar, firstLine);
                        setHighlightPos(1, firstChar, firstLine);
                    }
                }
                
                break;
            
            case "RIGHT":
                
                if (highlight1Line == highlight2Line && highlight1Char == highlight2Char) {
                    if (cursorChar < text.get(cursorLine).size())
                        setCursorPos(cursorChar + 1, cursorLine);
                    else if (cursorLine < text.size() - 1)
                        setCursorPos(0, cursorLine + 1);
                }
                
                else {
                    
                    if (highlight1Line == highlight2Line) {
                        
                        int lastChar = (highlight1Char > highlight2Char) ? highlight1Char : highlight2Char;
                        
                        setCursorPos(lastChar, highlight1Line);
                        
                        setHighlightPos(0, lastChar, highlight1Line);
                        setHighlightPos(1, lastChar, highlight2Line);
                    }
                    
                    else {
                        
                        int lastLine = (highlight1Line > highlight2Line) ? highlight1Line : highlight2Line;
                        int lastChar = (highlight1Line > highlight2Line) ? highlight1Char : highlight2Char;
                        
                        setCursorPos(lastChar, lastLine);
                        
                        setHighlightPos(0, lastChar, lastLine);
                        setHighlightPos(1, lastChar, lastLine);
                    }
                }
                
                break;
            
            case "UP":
                
                if (cursorLine > 0)
                    setCursorPos(getCharAt(cursorPosX, cursorLine - 1), cursorLine - 1);
                
                setHighlightPos(0, cursorChar, cursorLine);
                setHighlightPos(1, cursorChar, cursorLine);
                
                break;
            
            case "DOWN":
                
                if (cursorLine < text.size() - 1)
                    setCursorPos(getCharAt(cursorPosX, cursorLine + 1), cursorLine + 1);
                
                setHighlightPos(0, cursorChar, cursorLine);
                setHighlightPos(1, cursorChar, cursorLine);
                
                break;
        }
        
        if (shiftDown)
            setHighlightPos(1, cursorChar, cursorLine);
        else {
            setHighlightPos(0, cursorChar, cursorLine);
            setHighlightPos(1, cursorChar, cursorLine);
        }
    }
    
    public void delete() {
        
        if (highlight1Line == highlight2Line && highlight1Char == highlight2Char) {
            
            if (cursorChar > 0) {
                text.get(cursorLine).remove(cursorChar - 1);
                setCursorPos(cursorChar - 1, cursorLine);
            }
            
            else if (cursorLine > 0) {
                int previousLineLength = text.get(cursorLine - 1).size();
                for (RichCharacter ch : text.get(cursorLine))
                    text.get(cursorLine - 1).add(ch);
                text.remove(cursorLine);
                setCursorPos(previousLineLength, cursorLine - 1);
            }
        }
        
        else {
            
            if (highlight1Line == highlight2Line) {
                
                int firstChar = (highlight1Char < highlight2Char) ? highlight1Char : highlight2Char;
                int lastChar = (highlight1Char < highlight2Char) ? highlight2Char : highlight1Char;
                
                for (; lastChar > firstChar; lastChar--)
                    text.get(highlight1Line).remove(firstChar);
                
                setHighlightPos((highlight1Char < highlight2Char) ? 1 : 0, firstChar, highlight1Line);
                setCursorPos(firstChar, highlight1Line);
            }
            
            else {
                
                int firstLine = (highlight1Line < highlight2Line) ? highlight1Line : highlight2Line;
                int firstChar = (highlight1Line < highlight2Line) ? highlight1Char : highlight2Char;
                int lastLine = (highlight1Line < highlight2Line) ? highlight2Line : highlight1Line;
                int lastChar = (highlight1Line < highlight2Line) ? highlight2Char : highlight1Char;
                
                //trim the first line
                while (firstChar < text.get(firstLine).size())
                    text.get(firstLine).remove(firstChar);
                
                //remove all lines inbetween the first and last lines
                for (int a = firstLine + 1; a < lastLine; a++)
                    text.remove(firstLine + 1);
                
                //add the end of the last line to the first line
                for (int ch = lastChar; ch < text.get(firstLine + 1).size(); ch++)
                    text.get(firstLine).add(text.get(firstLine + 1).get(ch));
                
                text.remove(firstLine + 1);
                
                setHighlightPos((highlight1Line < highlight2Line) ? 1 : 0, firstChar, firstLine);
                setCursorPos(firstChar, firstLine);
            }
        }
    }
    
    public void copy() {
        
        if (highlight1Line == highlight2Line && highlight1Char == highlight2Char)
            return;
        
        clipboard.clear();
        
        if (highlight1Line == highlight2Line) {
            
            if (highlight1Char == highlight2Char)
                return;
            
            int firstChar = (highlight1Char < highlight2Char) ? highlight1Char : highlight2Char;
            int lastChar = (highlight1Char < highlight2Char) ? highlight2Char : highlight1Char;
            
            clipboard.add(new ArrayList<>());
            
            for (int ch = firstChar; ch < lastChar; ch++)
                clipboard.get(0).add(text.get(highlight1Line).get(ch));
        }
        
        else {
            
            int firstLine = (highlight1Line < highlight2Line) ? highlight1Line : highlight2Line;
            int firstChar = (highlight1Line < highlight2Line) ? highlight1Char : highlight2Char;
            int lastLine = (highlight1Line < highlight2Line) ? highlight2Line : highlight1Line;
            int lastChar = (highlight1Line < highlight2Line) ? highlight2Char : highlight1Char;
            
            //copy first line
            clipboard.add(new ArrayList<>());
            for (int ch = firstChar; ch < text.get(firstLine).size(); ch++)
                clipboard.get(0).add(text.get(firstLine).get(ch));
            
            //copy middle lines
            for (int line = firstLine + 1; line < lastLine; line++) {
                
                clipboard.add(new ArrayList<>());
                for (int ch = 0; ch < text.get(line).size(); ch++)
                    clipboard.get(line - firstLine).add(text.get(line).get(ch));
            }
            
            //copy last line
            clipboard.add(new ArrayList<>());
            for (int ch = 0; ch < lastChar; ch++)
                clipboard.get(clipboard.size() - 1).add(text.get(lastLine).get(ch));
        }
    }
    
    public void paste() {
        
        if (!(highlight1Line == highlight2Line && highlight1Char == highlight2Char))
            delete();
        
        for (int line = 0; line < clipboard.size(); line++) {
            
            if (line > 0)
                enter();
            
            for (RichCharacter ch : clipboard.get(line)) {
                text.get(cursorLine).add(cursorChar, ch);
                setCursorPos(cursorChar + 1, cursorLine);
            }
        }
    }
    
    public void cut() {
        
        copy();
        delete();
    }
    
    public void selectAll() {
        
        setHighlightPos(0, 0, 0);
        setHighlightPos(1, text.get(text.size() - 1).size(), text.size() - 1);
    }
    
    public void save() {
        
        try {
            
            FileWriter fileWriter = new FileWriter("/Users/MaccaMac/Desktop/McKinley/Programming/Java/Programs/" + saveFileName + fileExtension);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            for (String line : getPlainText()) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            
            bufferedWriter.close();
            
            System.out.println("File has been saved as \"" + saveFileName + fileExtension + "\".");
        }
        
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void load() {
        
        text.clear();
        text.add(new ArrayList<>());
        
        try {
            
            FileReader fileReader = new FileReader("/Users/MaccaMac/Desktop/McKinley/Programming/Java/Programs/" + loadFileName + fileExtension);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                
                for (char ch : line.toCharArray())
                    text.get(text.size() - 1).add(new RichCharacter(ch, selectedFont, selectedColor));
                
                text.add(new ArrayList<>());
            }
            
            bufferedReader.close();
            
            System.out.println(loadFileName + fileExtension + " has been loaded.");
        }
        
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        scrollX = 0;
        scrollY = 0;
        setCursorPos(0, 0);
        setHighlightPos(0, 0, 0);
        setHighlightPos(1, 0, 0);
    }
    
    public void switchDisplay(int display) {
        
        Main.switchDisplay(display);
    }
    
    public void runProgram() {
        
        TextDisplay programDisplay = new TextDisplay();
        
        if (runScreen == null) {
            
            Main.addDisplay(programDisplay);
            runScreen = Main.getDisplays().size() - 1;
        }
        
        else {
            
            Main.removeDisplay(runScreen);
            Main.addDisplay(programDisplay, runScreen);
        }
        
        Compiler compiler = new Compiler(programDisplay);
        compiler.runProgram(getPlainText());
    }
    
    public ArrayList<String> getPlainText() {
        
        ArrayList<String> plainText = new ArrayList<>();
        for (ArrayList<RichCharacter> line : text) {
            plainText.add(new String());
            for (RichCharacter ch : line)
                plainText.set(plainText.size() - 1, plainText.get(plainText.size() - 1) + Character.toString(ch.getChar()));
        }
        
        return plainText;
    }
    
    public int getCharAt(int x, int line) {
        
        int lastCharPos = marginX - scrollX;
        for (int ch = 0; ch < text.get(line).size(); ch++) {
            
            int charWidth = text.get(line).get(ch).getWidth();
            
            if (x <= lastCharPos + charWidth / 2f)
                return ch;
            
            lastCharPos += charWidth;
        }
        
        return text.get(line).size();
    }
    
    public int getLineAt(int y) {
        
        int lastLinePos = Display.getHeight() - marginY + scrollY;
        for (int line = 0; line < text.size(); line++) {
            
            if (line > 0)
                lastLinePos -= lineSpacing;
            
            int lineHeight = text.get(line).isEmpty() ? getSelectedFont().getHeight() : 0;
            for (RichCharacter ch : text.get(line)) {
                
                int charHeight = ch.getHeight();
                if (charHeight > lineHeight)
                    lineHeight = charHeight;
            }
            
            if (y >= lastLinePos - lineHeight - lineSpacing / 2f)
                return line;
            
            lastLinePos -= lineHeight;
        }
        
        return text.size() - 1;
    }
    
    public int getCharPosX(int ch, int line) {
        
        int xPos = marginX - scrollX;
        for (int a = 0; a < ch; a++)
            xPos += text.get(line).get(a).getWidth();
        
        return xPos;
    }
    
    public int getLinePosY(int line) {
        
        int yPos = Display.getHeight() - marginY + scrollY;
        for (int a = 0; a <= line; a++) {
            
            if (a > 0)
                yPos -= lineSpacing;
            
            int lineHeight = text.get(a).isEmpty() ? getSelectedFont().getHeight() : 0;
            for (RichCharacter richChar : text.get(a)) {
                
                int charHeight = richChar.getHeight();
                if (charHeight > lineHeight)
                    lineHeight = charHeight;
            }
            
            yPos -= lineHeight;
        }
        
        return yPos;
    }
    
    public int getLineHeight(int line) {
        
        int lineHeight = text.get(line).isEmpty() ? getSelectedFont().getHeight() : 0;
        for (RichCharacter ch : text.get(line)) {
            
            int charHeight = ch.getHeight();
            if (charHeight > lineHeight)
                lineHeight = charHeight;
        }
        
        return lineHeight;
    }
    
    public int getCursorPosX() {
        
        return cursorPosX - scrollX;
    }
    
    public int getCursorPosY() {
        
        return cursorPosY + scrollY;
    }
    
    public int getHighlightPosX(int highlightNumber) {
        
        int highlightPosX = (highlightNumber == 0) ? highlight1PosX : highlight2PosX;
        
        return highlightPosX - scrollX;
    }
    
    public int getHighlightPosY(int highlightNumber) {
        
        int highlightPosY = (highlightNumber == 0) ? highlight1PosY : highlight2PosY;
        
        return highlightPosY + scrollY;
    }
    
    public void setCursorPosX(int screenPosX) {
        
        cursorPosX = screenPosX + scrollX;
    }
    
    public void setCursorPosY(int screenPosY) {
        
        cursorPosY = screenPosY - scrollY;
    }
    
    public void setHighlightPosX(int highlightNumber, int screenPosX) {
        
        int highlightPosX = screenPosX + scrollX;
        
        if (highlightNumber == 0)
            highlight1PosX = highlightPosX;
        else
            highlight2PosX = highlightPosX;
    }
    
    public void setHighlightPosY(int highlightNumber, int screenPosY) {
        
        int highlightPosY = screenPosY - scrollY;
        
        if (highlightNumber == 0)
            highlight1PosY = highlightPosY;
        else
            highlight2PosY = highlightPosY;
    }
    
    public void setCursorPos(int ch, int line) {
        
        cursorChar = ch;
        cursorLine = line;
        
        setCursorPosX(getCharPosX(ch, line));
        setCursorPosY((int)(getLinePosY(line) + getLineHeight(line) / 2f - getSelectedFont().getHeight() / 2f));
        
        adjustScreen(cursorChar, cursorLine);
    }
    
    public void setHighlightPos(int highlightNumber, int ch, int line) {
        
        if (highlightNumber == 0) {
            highlight1Char = ch;
            highlight1Line = line;
        }
        
        else {
            highlight2Char = ch;
            highlight2Line = line;
        }
        
        setHighlightPosX(highlightNumber, getCharPosX(ch, line));
        setHighlightPosY(highlightNumber, getLinePosY(line));
    }
    
    public void adjustScreen(int ch, int line) {
        
        //moves the screen so that it includes the given character
        
        //correct scrollX
        int posX = getCharPosX(ch, line);
        
        if (posX > Display.getWidth() - marginX - cursorWidth)
            scrollX += posX - (Display.getWidth() - marginX - cursorWidth);
        else if (posX < marginX)
            scrollX -= marginX - posX;
        
        //correct scrollY
        int posY = getLinePosY(line);
        int lineHeight = getLineHeight(line);
        
        if (posY < marginY)
            scrollY += marginY - posY;
        else if (posY + lineHeight > Display.getHeight() - marginY)
            scrollY -= posY + lineHeight - (Display.getHeight() - marginY);
    }
    
    public TrueTypeFont getSelectedFont() {
        
        return getFont(selectedFont);
    }
    
    public static TrueTypeFont getFont(int fontIndex) {
        
        return fonts[fontIndex];
    }
    
    public static TrueTypeFont getFont(String fontName) {
        
        for (int a = 0; a < fontNames.length; a++)
            if (fontNames[a].equals(fontName))
                return fonts[a];
        
        return null;
    }
    
    public Color getSelectedColor() {
        
        return getColor(selectedColor);
    }
    
    public static Color getColor(int colorIndex) {
        
        return colors[colorIndex];
    }
    
    public static Color getColor(String colorName) {
        
        for (Color color : colors)
            if (color.toString().equals(colorName.toLowerCase()))
                return color;
        
        return null;
    }
    
    public static void drawLine(float x1, float y1, float x2, float y2, float width, float r, float g, float b) {
        
        glColor3f(r, g, b);
        
        glLineWidth(width);
        
        glBegin(GL_LINE_STRIP);
        {
            glVertex2f(x1, Display.getHeight() - y1);
            glVertex2f(x2, Display.getHeight() - y2);
        }
        
        glEnd();
    }

    public static void drawQuad(float x1, float y1, float x2, float y2, float r, float g, float b) {
        
        glColor3f(r, g, b);
        
        glBegin(GL_QUADS);
        {
            glVertex2f(x1, Display.getHeight() - y1);
            glVertex2f(x2, Display.getHeight() - y1);
            glVertex2f(x2, Display.getHeight() - y2);
            glVertex2f(x1, Display.getHeight() - y2);
        }
        
        glEnd();
    }
    
    public static void drawTransparentQuad(float x1, float y1, float x2, float y2, float transparency, float r, float g, float b) {
        
        //enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glColor4f(r, g, b, transparency);
        
        glBegin(GL_QUADS);
        {
            glVertex2f(x1, Display.getHeight() - y1);
            glVertex2f(x2, Display.getHeight() - y1);
            glVertex2f(x2, Display.getHeight() - y2);
            glVertex2f(x1, Display.getHeight() - y2);
        }
        
        glEnd();
        
        //disable transparency
        glDisable(GL_BLEND);
    }
    
    public void drawString(TrueTypeFont font, float x, float y, String text, Color color) {
        
        glEnable(GL_TEXTURE_2D);
        
        //enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        font.drawString(x, Display.getHeight() - (y + font.getHeight(text)), text, color);
        
        //disable transparency
        glDisable(GL_BLEND);
        
        glDisable(GL_TEXTURE_2D);
    }
}

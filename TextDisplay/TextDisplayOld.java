

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


public class TextDisplayOld {

    public static final float[] clearColor = {1f, 1f, 1f};
    public static final Color clearColorName = new Color(clearColor[0], clearColor[1], clearColor[2]);
    public static final float[] textColor = {0f, 0f, 0f};
    public static final Color textColorName = new Color(textColor[0], textColor[1], textColor[2]);
    public static TrueTypeFont font24;
    public static Font awtFont24;
    public static TrueTypeFont font;
    public static Font awtFont;
    
    public static final float fontSize = 20;
    public static final float fontWidth = fontSize * 3f / 5;
    public static final String fontName = "Monospaced";
    
    public int cursorLine;
    public int cursorChar;
    
    public final boolean cursorFlashing = true;
    public final long cursorFlashingInterval = 500000000;
    public long cursorLastFlash;
    
    public int highlightPos1Line;
    public int highlightPos1Char;
    public int highlightPos2Line;
    public int highlightPos2Char;
    public int highlightLine1;
    public int highlightLine2;
    public int highlightChar1;
    public int highlightChar2;
    
    public static final float highlightTransparency = 0.4f;
    public static final float[] highlightColor = {0f, 0f, 1f};
    
    public static final int marginX = 20;
    public static final int marginY = 10;
    
    public int topScreenLine = 0;
    public int leftScreenChar = 0;
    
    public float cursorPosX;
    public float cursorPosY;
    public final float cursorSizeX = 2;
    public final float cursorSizeY = fontSize * 1.15f;
    
    public static boolean drawCustomMouse = true;
    public static final float mouseSizeX = fontSize * 0.48f;
    public static final float mouseSizeY = fontSize;
    
    public static ArrayList<String> clipboard = new ArrayList<>();
    
    public ArrayList<String> text = new ArrayList<>(Arrays.asList("hello", "color", "blue"));
    public ArrayList<int[]> textColorPositions = new ArrayList<>(Arrays.asList(new int[] {0, 0}, new int[] {2, 0}, new int[] {0, 2}));
    public ArrayList<float[]> textColors = new ArrayList<>(Arrays.asList(new float[] {0, 0, 0}, new float[] {0, 1, 0}, new float[] {0, 0, 1}));
    
    public Integer runScreen;
    
    public String fileName;
    public String saveFileName = "Program3";
    public String loadFileName = "PiProgram";
    public static final String fileExtension = ".txt";
    
    
    public static final ArrayList<String> numbers = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"));
    public static final ArrayList<String> nonCharKeys = new ArrayList<>(Arrays.asList("NONE", "RETURN", "BACK", "DELETE", "CAPITAL", "LSHIFT", "RSHIFT", "LMETA", "RMETA", "LCONTROL", "RCONTROL", "LMENU", "RMENU", "LEFT", "RIGHT", "UP", "DOWN", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12", "F13", "F14", "F15", "F16", "F17", "F18", "F19"));
    
    public static HashMap<String, String> keyNameChar = new HashMap<String, String>() {{
        put("GRAVE", "`");  put("MINUS", "-");  put("EQUALS", "="); put("LBRACKET", "[");
        put("RBRACKET", "]");   put("BACKSLASH", "\\"); put("SEMICOLON", ";");  put("APOSTROPHE", "'");
        put("COMMA", ",");  put("PERIOD", "."); put("SLASH", "/");
        put("SPACE", " ");  put("TAB", "    ");
    }};
    
    public static HashMap<String, String> charCapital = new HashMap<String, String>() {{
        put("`", "~");  put("1", "!");  put("2", "@");  put("3", "#");  put("4", "$");  put("5", "%");
        put("6", "^");  put("7", "&");  put("8", "*");  put("9", "(");  put("0", ")");  put("-", "_");
        put("=", "+");  put("[", "{");  put("]", "}");  put("\\", "|"); put(";", ":");  put("'", "\"");
        put(",", "<");  put(".", ">");  put("/", "?");  put(" ", " ");
    }};
    
    public TextDisplayOld() {
        
        
    }
    
    public TextDisplayOld(String saveFileName, String loadFileName) {
        
        this.saveFileName = saveFileName;
        this.loadFileName = loadFileName;
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
        
        glClearColor(clearColor[0], clearColor[1], clearColor[2], 0.0f);
        glClearDepth(1);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        
        awtFont = new Font(fontName, Font.PLAIN, (int)fontSize);
        font = new TrueTypeFont(awtFont, true);
        awtFont24 = new Font(fontName, Font.PLAIN, 24);
        font24 = new TrueTypeFont(awtFont24, true);
        
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
        
        int bottomScreenLine = topScreenLine + (int)Math.floor((Display.getHeight() - 2 * marginY) / (fontSize * 1.5f));
        
        int color = 0;
        for (int i = topScreenLine; i < text.size() && i <= bottomScreenLine; i++) {
            
            if (textColorPositions.size() > color + 1 && textColorPositions.get(color + 1)[1] == i)
                while (true) {
                    
                    int thisColorChar = textColorPositions.get(color)[0];
                    int nextColorChar = textColorPositions.get(color + 1)[0];
                    
                    drawString(font, getPosX(thisColorChar, i), Display.getHeight() - marginY - fontSize * 1.15f - (i - topScreenLine) * fontSize * 1.5f, text.get(i).substring(thisColorChar, nextColorChar), new Color(textColors.get(color)[0], textColors.get(color)[1], textColors.get(color)[2]));
                    
                    color++;
                    
                    if (!(textColorPositions.size() > color + 1 && textColorPositions.get(color + 1)[1] == i)) {
                        
                        drawString(font, getPosX(nextColorChar, i), Display.getHeight() - marginY - fontSize * 1.15f - (i - topScreenLine) * fontSize * 1.5f, text.get(i).substring(nextColorChar), new Color(textColors.get(color)[0], textColors.get(color)[1], textColors.get(color)[2]));
                        color++;
                        break;
                    }
                }
            
            else if (!(leftScreenChar > text.get(i).length()))
                drawString(font, marginX, Display.getHeight() - marginY - fontSize * 1.15f - (i - topScreenLine) * fontSize * 1.5f, text.get(i).substring(leftScreenChar), textColorName);
        }
        
        //highlight
        if (highlightPos1Line == highlightPos2Line) {
            
            drawTransparentQuad(getPosX(highlightPos1Char, highlightPos1Line), getPosY(highlightPos1Line), getPosX(highlightPos2Char, highlightPos2Line), getPosY(highlightPos1Line) + fontSize * 1.15f, highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
        }
        
        else {
            
            calcHighlightPos();
            
            //draw first highlight
            drawTransparentQuad(getPosX(highlightChar1, highlightLine1), getPosY(highlightLine1) + fontSize * 1.15f, Display.getWidth() - marginX, getPosY(highlightLine1) - fontSize * (1.5f - 1.15f) / 2, highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
            
            for (int i = highlightLine1 + 1; i < highlightLine2; i++) {
                
                drawTransparentQuad(marginX, getPosY(i) - fontSize * (1.5f - 1.15f) / 2, Display.getWidth() - marginX, getPosY(i) + fontSize * (1.5f + 1.15f) / 2, highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
            }
            
            drawTransparentQuad(marginX, getPosY(highlightLine2), getPosX(highlightChar2, highlightLine2), getPosY(highlightLine2) + fontSize * (1.5f + 1.15f) / 2, highlightTransparency, highlightColor[0], highlightColor[1], highlightColor[2]);
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
        
        if (drawCursor) {
        
            findCursorPosX();
            findCursorPosY();

            drawQuad(cursorPosX, cursorPosY, cursorPosX + cursorSizeX, cursorPosY + cursorSizeY, 0f, 0f, 0f);
        }
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
        drawLine(posX - mouseSizeX / 6, posY, posX + mouseSizeX / 6 - 1, posY, 1, 0f, 0f, 0f);
        
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
    }

    public void getInput() {

        //mouse inputs
        Mouse.poll();
        
        while (Mouse.next()) {
            
            if (Mouse.getEventButtonState()) {
                
                if (Mouse.getEventButton() == 0) {
                    
                    if (Mouse.getX() >= marginX && Mouse.getX() <= Display.getWidth() - marginX &&
                        Mouse.getY() >= marginY && Mouse.getY() <= Display.getHeight() - marginY) {
                        
                        //cursorChar = leftScreenChar + Math.round((Mouse.getX() - marginX) / fontWidth);
                        cursorLine = (int)Math.ceil((Display.getHeight() - marginY - Mouse.getY() - fontSize * (1.5f + 1.15f) / 2) / (fontSize * 1.5f) + topScreenLine);
                        
                        if (cursorLine >= text.size())
                            cursorLine = text.size() - 1;
                        if (cursorLine < 0)
                            cursorLine = 0;
                        if (cursorLine < topScreenLine)
                            cursorLine = topScreenLine;
                        
                        for (int a = leftScreenChar; a < text.get(cursorLine).length(); a++) {
                            
                            if (Mouse.getX() <= marginX + font.getWidth(text.get(cursorLine).substring(leftScreenChar, a)) + font.getWidth(Character.toString(text.get(cursorLine).charAt(a))) / 2f) {
                                cursorChar = a;
                                break;
                            }
                            else if (a == text.get(cursorLine).length() - 1)
                                cursorChar = text.get(cursorLine).length();
                        }
                        
                        if (cursorChar >= text.get(cursorLine).length())
                            cursorChar = text.get(cursorLine).length();
                        if (cursorChar < 0)
                            cursorChar = 0;
                        if (cursorChar < leftScreenChar)
                            cursorChar = leftScreenChar;
                        
                        cursorLastFlash = System.nanoTime();
                        
                        highlightPos1Line = cursorLine;
                        highlightPos1Char = cursorChar;
                    }
                }
            }
            
            else {
                
                if (Mouse.getEventButton() == 0) {
                    
                    //cursorChar = leftScreenChar + Math.round((Mouse.getX() - marginX) / fontWidth);
                    cursorLine = (int)Math.ceil((Display.getHeight() - marginY - Mouse.getY() - fontSize * (1.5f + 1.15f) / 2) / (fontSize * 1.5f) + topScreenLine);
                    
                    if (cursorLine >= text.size())
                        cursorLine = text.size() - 1;
                    if (cursorLine < 0)
                        cursorLine = 0;
                    if (cursorLine < topScreenLine)
                        cursorLine = topScreenLine;
                    
                    for (int a = leftScreenChar; a < text.get(cursorLine).length(); a++) {
                        
                        if (Mouse.getX() <= marginX + font.getWidth(text.get(cursorLine).substring(leftScreenChar, a)) + font.getWidth(Character.toString(text.get(cursorLine).charAt(a))) / 2f) {
                            cursorChar = a;
                            break;
                        }
                        else if (a == text.get(cursorLine).length() - 1)
                            cursorChar = text.get(cursorLine).length();
                    }
                    
                    if (cursorChar >= text.get(cursorLine).length())
                        cursorChar = text.get(cursorLine).length();
                    if (cursorChar < 0)
                        cursorChar = 0;
                    if (cursorChar < leftScreenChar)
                        cursorChar = leftScreenChar;
                    
                    highlightPos2Char = cursorChar;
                    highlightPos2Line = cursorLine;
                }
            }
        }
        
        if (Mouse.isButtonDown(0)) {
            
            //int cursorChar = leftScreenChar + Math.round((Mouse.getX() - marginX) / fontWidth);
            cursorLine = (int)Math.ceil((Display.getHeight() - marginY - Mouse.getY() - fontSize * (1.5f + 1.15f) / 2) / (fontSize * 1.5f) + topScreenLine);
            
            if (cursorLine >= text.size())
                cursorLine = text.size() - 1;
            if (cursorLine < 0)
                cursorLine = 0;
            if (cursorLine < topScreenLine)
                cursorLine = topScreenLine;
            
            for (int a = leftScreenChar; a < text.get(cursorLine).length(); a++) {
                
                if (Mouse.getX() <= marginX + font.getWidth(text.get(cursorLine).substring(leftScreenChar, a)) + font.getWidth(Character.toString(text.get(cursorLine).charAt(a))) / 2f) {
                    cursorChar = a;
                    break;
                }
                else if (a == text.get(cursorLine).length() - 1)
                    cursorChar = text.get(cursorLine).length();
            }
            
            if (cursorChar >= text.get(cursorLine).length())
                cursorChar = text.get(cursorLine).length();
            if (cursorChar < 0)
                cursorChar = 0;
            if (cursorChar < leftScreenChar)
                cursorChar = leftScreenChar;
            
            highlightPos2Char = cursorChar;
            highlightPos2Line = cursorLine;
        }
        
        int scroll = Mouse.getDWheel();
        
        if (scroll != 0) {
            
            //scroll up
            if (scroll > 0) {
                
                if (topScreenLine > 0)
                    topScreenLine--;
            }
            
            //scroll down
            else {
                
                int bottomScreenLine = topScreenLine + (int)Math.floor((Display.getHeight() - 2 * marginY) / (fontSize * 1.5f));
                
                if (bottomScreenLine < text.size() - 1 + (1d / 3d * (bottomScreenLine - topScreenLine)))
                    topScreenLine++;
            }
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
                        moveCursor(keyName);
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
                
                if (nonCharKeys.contains(keyName))
                    return;
                
                boolean LShiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                boolean RShiftDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                boolean capitalDown = Keyboard.isKeyDown(Keyboard.KEY_CAPITAL);
                
                if (LShiftDown || RShiftDown || capitalDown)
                    write(getChar(Keyboard.getKeyName(Keyboard.getEventKey()), true, !(LShiftDown || RShiftDown)));
                else
                    write(getChar(Keyboard.getKeyName(Keyboard.getEventKey()), false, false));
            }
        }
    }
    
    public static String getChar(String keyName, boolean caps, boolean capsLock) {
        
        if (caps) {
            
            if (numbers.contains(keyName)) {
                
                if (capsLock)
                    return keyName;
                
                else
                    return charCapital.get(keyName);
            }
            
            if (keyNameChar.containsKey(keyName))
                return charCapital.get(keyNameChar.get(keyName));
            
            else
                return keyName.toUpperCase();
        }
        
        else {
            
            if (keyNameChar.containsKey(keyName))
                return keyNameChar.get(keyName);
            
            else
                return keyName.toLowerCase();
        }
    }
    
    public void write(String string) {
        
        if (!(highlightPos1Line == highlightPos2Line && highlightPos1Char == highlightPos2Char))
            delete();
        
        text.set(cursorLine, insertString(text.get(cursorLine), string, cursorChar));
        
        cursorChar += string.length();
        
        int rightScreenChar = calcRightScreenChar();
        
        if (cursorChar > rightScreenChar)
            leftScreenChar += cursorChar - rightScreenChar;
    }
    
    public void write(String string, int linePos, int charPos) {
        
        text.set(cursorLine, insertString(text.get(cursorLine), string, cursorChar));
        
        if (cursorLine == linePos && cursorChar >= charPos)
            cursorChar += string.length();
        
        int rightScreenChar = calcRightScreenChar();
        
        if (cursorChar > rightScreenChar)
            leftScreenChar += cursorChar - rightScreenChar;
    }
    
    public void delete() {
        
        if (!(highlightPos1Line == highlightPos2Line && highlightPos1Char == highlightPos2Char)) {
            
            calcHighlightPos();
            
            if (highlightLine1 == highlightLine2) {
                
                if (highlightChar1 > highlightChar2) {
                    
                    int difference = highlightChar1 - highlightChar2;
                    highlightChar1 -= difference;
                    highlightChar2 += difference;
                }
                
                text.set(highlightLine1, text.get(highlightLine1).substring(0, highlightChar1) + text.get(highlightLine1).substring(highlightChar2));
                
                cursorChar = highlightChar1;
                highlightPos1Char = cursorChar;
                highlightPos2Char = cursorChar;
                
                return;
            }
            
            text.set(highlightLine1, text.get(highlightLine1).substring(0, highlightChar1));
            
            for (int i = highlightLine1 + 1; i < highlightLine2; i++)
                text.remove(highlightLine1 + 1);
            
            text.set(highlightLine1, text.get(highlightLine1) + text.get(highlightLine1 + 1).substring(highlightChar2));
            
            text.remove(highlightLine1 + 1);
            
            cursorLine = highlightLine1;
            cursorChar = highlightChar1;
            
            highlightPos2Line = highlightPos1Line;
            highlightPos2Char = highlightPos1Char;
            
            return;
        }
        
        char[] charArray = text.get(cursorLine).toCharArray();
        String replacementString = "";
        
        if (cursorChar == 0) {
            
            if (cursorLine != 0) {
                
                cursorChar = text.get(cursorLine - 1).length();
            
                text.set(cursorLine - 1, text.get(cursorLine - 1) + text.get(cursorLine));
                text.remove(cursorLine);
                
                cursorLine--;
            }
        }
        
        else {
            
            for (int i = 0; i < charArray.length; i++)
                if (i != cursorChar - 1)
                    replacementString += charArray[i];

            text.set(cursorLine, replacementString);
            cursorChar--;
        }
    }
    
    public void enter() {
        
        text.add(cursorLine + 1, text.get(cursorLine).substring(cursorChar));
        
        text.set(cursorLine, text.get(cursorLine).substring(0, cursorChar));
        
        cursorLine++;
        cursorChar = 0;
        
        //scroll down if needed
        int bottomScreenLine = calcBottomScreenLine();
        
        if (cursorLine > bottomScreenLine)
            topScreenLine++;
        
        leftScreenChar = 0;
    }
    
    public void enter(int linePos, int charPos) {
        
        text.add(linePos + 1, text.get(linePos).substring(charPos));
        
        text.set(linePos, text.get(linePos).substring(0, charPos));
        
        if (cursorLine > linePos || (cursorLine == linePos && cursorChar >= charPos)) {
            
            cursorLine++;
            cursorChar -= charPos;
        }
        
        //scroll down if needed
        int bottomScreenLine = calcBottomScreenLine();
        
        if (cursorLine > bottomScreenLine)
            topScreenLine++;
        
        leftScreenChar = 0;
    }
    
    public void save() {
        
        try {
            
            FileWriter fileWriter = new FileWriter("/Users/MaccaMac/Desktop/McKinley/Programming/Java/Programs/" + saveFileName + fileExtension);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            for (String line : text) {
                
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
        cursorLine = 0;
        cursorChar = 0;
        topScreenLine = 0;
        leftScreenChar = 0;
        highlightPos1Line = 0;
        highlightPos1Char = 0;
        highlightPos2Line = 0;
        highlightPos2Char = 0;
        highlightLine1 = 0;
        highlightLine2 = 0;
        highlightChar1 = 0;
        highlightChar2 = 0;
        
        try {
            
            FileReader fileReader = new FileReader("/Users/MaccaMac/Desktop/McKinley/Programming/Java/Programs/" + loadFileName + fileExtension);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
                
                text.add(line);
            }
            
            bufferedReader.close();
            
            fileName = loadFileName;
            
            System.out.println(loadFileName + fileExtension + " has been loaded.");
        }
        
        catch (IOException ex) {
            
            ex.printStackTrace();
        }
    }
    
    public void setSaveFile(String saveFileName) {
        
        this.saveFileName = saveFileName;
    }
    
    public void setLoadFile(String loadFileName) {
        
        this.loadFileName = loadFileName;
    }
    
    public void selectAll() {
        
        highlightPos1Line = 0;
        highlightPos1Char = 0;
        highlightPos2Line = text.size() - 1;
        highlightPos2Char = text.get(text.size() - 1).length();
    }
    
    public void copy() {
        
        if (!(highlightPos1Line == highlightPos2Line && highlightPos1Char == highlightPos2Char)) {
            
            clipboard.clear();
            
            calcHighlightPos();
            
            if (highlightLine1 == highlightLine2) {
                
                if (highlightChar1 > highlightChar2) {
                    
                    int difference = highlightChar1 - highlightChar2;
                    highlightChar1 -= difference;
                    highlightChar2 += difference;
                }
                
                clipboard.add(text.get(highlightLine1).substring(highlightChar1, highlightChar2));
                
                return;
            }
            
            clipboard.add(text.get(highlightLine1).substring(highlightChar1));
            
            for (int i = highlightLine1 + 1; i < highlightLine2; i++)
                clipboard.add(text.get(i));
            
            clipboard.add(text.get(highlightLine2).substring(0, highlightChar2));
        }
    }
    
    public void paste() {
        
        if (!clipboard.isEmpty()) {
            
            String string = text.get(cursorLine).substring(cursorChar);
            
            text.set(cursorLine, text.get(cursorLine).substring(0, cursorChar) + clipboard.get(0));
            
            for (int i = 1; i < clipboard.size(); i++)
                text.add(cursorLine + i, clipboard.get(i));
            
            cursorLine += clipboard.size() - 1;
            cursorChar = text.get(cursorLine).length();
            
            text.set(cursorLine, text.get(cursorLine) + string);
            
            
            int bottomScreenLine = calcBottomScreenLine();
            
            if (cursorLine > bottomScreenLine)
                topScreenLine += cursorLine - bottomScreenLine;
            
            int rightScreenChar = calcRightScreenChar();
            
            if (cursorChar > rightScreenChar)
                leftScreenChar += cursorChar - rightScreenChar;
        }
    }
    
    public void cut() {
        
        copy();
        delete();
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
        compiler.runProgram(text);
    }
    
    public static void switchDisplay(int display) {
        
        Main.switchDisplay(display);
    }
    
    public void moveCursor(String direction) {
        
        if (direction.equals("UP")) {
            
            if (cursorLine != 0) {
                
                cursorLine--;
                
                if (cursorChar > text.get(cursorLine).length())
                    cursorChar = text.get(cursorLine).length();
                
                if (topScreenLine > cursorLine)
                    topScreenLine = cursorLine;
            }
        }
        
        else if (direction.equals("DOWN")) {
            
            if (cursorLine != text.size() - 1) {
                
                cursorLine++;
                
                if (cursorChar > text.get(cursorLine).length())
                    cursorChar = text.get(cursorLine).length();
                
                int bottomScreenLine = topScreenLine + (int)Math.floor((Display.getHeight() - 2 * marginY) / (fontSize * 1.5f));
                
                if (bottomScreenLine < cursorLine)
                    topScreenLine += cursorLine - bottomScreenLine;
            }
        }
        
        else if (direction.equals("LEFT")) {
            
            if (cursorChar == 0) {
                
                if (cursorLine != 0) {
                    
                    cursorLine--;
                    cursorChar = text.get(cursorLine).length();
                }
            }
            
            else
                cursorChar--;
            
            if (cursorChar < leftScreenChar)
                leftScreenChar = cursorChar;
        }
        
        else if (direction.equals("RIGHT")) {
            
            if (cursorChar == text.get(cursorLine).length()) {
                
                if (cursorLine != text.size() - 1) {
                    
                    cursorLine++;
                    cursorChar = 0;
                }
            }
            
            else
                cursorChar++;
            
            int rightScreenChar = calcRightScreenChar();
            
            if (cursorChar > rightScreenChar)
                leftScreenChar += cursorChar - rightScreenChar;
        }
    }
    
    public void findCursorPosX() {
        
        cursorPosX = getPosX(cursorChar, cursorLine) - cursorSizeX / 2f;
    }
    
    public void findCursorPosY() {
        
        cursorPosY = getPosY(cursorLine);
    }
    
    public float getPosX(int character, int line) {
        
        if (character < leftScreenChar)
            return -1;
        
        return marginX + font.getWidth(text.get(line).substring(leftScreenChar, character));
    }
    
    public float getPosY(int line) {
        
        return Display.getHeight() - marginY - fontSize * 1.15f + 1 - fontSize * 1.5f * (line - topScreenLine);
    }
    
    public static String insertString(String string, String subString, int index) {
        
        String stringStart = string.substring(0, index);
        String stringEnd = string.substring(index, string.length());
        
        return stringStart + subString + stringEnd;
    }
    
    public void calcHighlightPos() {
        
        if (highlightPos1Line > highlightPos2Line) {
            
            highlightLine2 = highlightPos1Line;
            highlightLine1 = highlightPos2Line;
            highlightChar2 = highlightPos1Char;
            highlightChar1 = highlightPos2Char;
        }
        
        else {
            
            highlightLine2 = highlightPos2Line;
            highlightLine1 = highlightPos1Line;
            highlightChar2 = highlightPos2Char;
            highlightChar1 = highlightPos1Char;
        }
    }
    
    public int calcBottomScreenLine() {
        
        return topScreenLine + (int)Math.floor((Display.getHeight() - 2 * marginY) / (fontSize * 1.5f));
    }
    
    public int calcRightScreenChar() {
        
        return leftScreenChar + (int)Math.floor((Display.getWidth() - 2 * marginX) / (fontWidth));
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
        
        drawStringFunc(font, x, y + fontSize * 1.15f + 1, text, color);
    }

    public static void drawStringFunc(TrueTypeFont font, float x, float y, String text, Color color) {

        glEnable(GL_TEXTURE_2D);

        //enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        font.drawString(x, Display.getHeight() - y, text, color);

        //disable transparency
        glDisable(GL_BLEND);

        glDisable(GL_TEXTURE_2D);
    }
}

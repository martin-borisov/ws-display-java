package mb.iot.display.ws;

import static java.text.MessageFormat.format;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.jna.Library;
import com.sun.jna.Native;

import mb.iot.display.DisplayException;
import mb.iot.display.Utils;

public class DisplayController {

    private static final String LIB_NAME = "LCD_2in_test";
    public static final short MAX_BACKLIGHT = 1023;
    private static final short CLEAR_COLOR = 0;
    
    public interface WSDisplayLib extends Library {
        WSDisplayLib INSTANCE = (WSDisplayLib) Native.load(LIB_NAME, WSDisplayLib.class); 
        
        byte DEV_ModuleInit();
        void DEV_ModuleExit();
        void DEV_SetBacklight(short val);
        void LCD_2IN_Init();
        void LCD_2IN_Clear(short col);
        void LCD_2IN_Display(byte[] image);
        void LCD_2IN_test();
    }
    private static final WSDisplayLib LIB = WSDisplayLib.INSTANCE;
    
    protected int rotation, width, height;
    private boolean initialized;
    
    public DisplayController(int width, int height) {
        this(width, height, 0);
    }

    public DisplayController(int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
    
    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void setBacklight(short val) throws DisplayException {
        
        if(val < 0 || val > MAX_BACKLIGHT) {
            throw new IllegalArgumentException("Backlight value out of range");
        }
        
        init();
        LIB.DEV_SetBacklight(val);
    }
    
    public void clear() throws DisplayException {
        init();
        LIB.LCD_2IN_Clear(CLEAR_COLOR);
    }
    
    public void drawText(String text, int size, Color color) throws DisplayException {
        init();
        LIB.LCD_2IN_Display(Utils.drawText(text, size, color, width, height, rotation));
    }
    
    public void drawImage(InputStream is) throws DisplayException {
        init();
        
        try {
            LIB.LCD_2IN_Display(Utils.imageToRGB565(ImageIO.read(is), width, height, rotation));
        } catch (IOException e) {
            throw new DisplayException("Converting image failed", e);
        }
    }
    
    public void close() {
        if(initialized) {
            LIB.DEV_ModuleExit();
        }
    }
    
    private void init() throws DisplayException {
        if (!initialized) {
            int status = LIB.DEV_ModuleInit();
            if (status == 0) {
                LIB.LCD_2IN_Init();
                initialized = true;
                clear();
            } else {
                throw new DisplayException(
                        format("Failed to initialize display module with status: {0, number, #}", status));
            }
        }
    }
}

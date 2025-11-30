package mb.iot.display.ws;

import static java.text.MessageFormat.format;

import java.awt.Color;

import com.sun.jna.Library;
import com.sun.jna.Native;

import mb.iot.display.DisplayException;
import mb.iot.display.Utils;

public class DisplayController {
    public static final short MAX_BACKLIGHT = 1023;
    private static final String LIB_NAME = "LCD_2in_test";
    
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
    private boolean initialized;
    
    public void setBacklight(short val) throws DisplayException {
        init();
        
        // TODO Validate val
        LIB.DEV_SetBacklight(val);
    }
    
    public void clear() throws DisplayException {
        init();
        
        // TODO Should be constant
        LIB.LCD_2IN_Clear((short) 0);
    }
    
    public void drawText(String text, int size, Color color) throws DisplayException {
        init();
        LIB.LCD_2IN_Display(Utils.drawText(text, size, color, 320, 240));
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

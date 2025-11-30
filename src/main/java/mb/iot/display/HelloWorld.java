package mb.iot.display;

import java.awt.Color;

import mb.iot.display.ws.DisplayController;

public class HelloWorld {

    public static void main(String[] args) throws InterruptedException {
        DisplayController display = new DisplayController();
        
        try {
            display.setBacklight(DisplayController.MAX_BACKLIGHT);
            display.drawText("Well, this is just a simple text", 42, Color.ORANGE);
        } catch (DisplayException e) {
            e.printStackTrace();
        }
        
        Thread.sleep(10000);
        
        try {
            display.clear();
            display.setBacklight((short) 0);
        } catch (DisplayException e) {
            e.printStackTrace();
        }
        
        display.close();
    }
}

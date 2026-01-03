package mb.iot.display;

import com.beust.jcommander.Parameter;

public class Args {
    
    @Parameter(names = "--port")
    private int port;
    
    @Parameter(names = "--rotation")
    private int rotation;
    
    @Parameter(names = "--dimming")
    private boolean dimming = true;
    
    @Parameter(names = "--width")
    private int width = 320;
    
    @Parameter(names = "--height")
    private int height = 240;
    
    @Parameter(names = "--dimming-delay")
    private int dimmingDelay = 15;

    public int getPort() {
        return port;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isDimming() {
        return dimming;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDimmingDelay() {
        return dimmingDelay;
    }
}

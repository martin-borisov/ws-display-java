package mb.iot.display.ws;

import java.awt.Color;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import mb.iot.display.DisplayException;

public class DimmingDisplayController extends DisplayController {
    
    private static final Logger LOG = 
            Logger.getLogger(DimmingDisplayController.class.getName());
    private static final int DEFAULT_DIMMING_DELAY_MS = 15000;
    
    private int dimmingDelay;
    private Timer dimmingTimer;
    private TimerTask dimmingTask;

    public DimmingDisplayController(int width, int height) {
        this(width, height, 0, DEFAULT_DIMMING_DELAY_MS);
    }

    public DimmingDisplayController(int width, int height, int rotation, int dimmingDelaySec) {
        super(width, height, rotation);
        this.dimmingDelay = dimmingDelaySec * 1000;
        dimmingTimer = new Timer(true);
    }
    
    @Override
    public void drawText(String text, int size, Color color) throws DisplayException {
        super.drawText(text, size, color);
        resetDimmingTimer();
    }

    @Override
    public void drawImage(InputStream is) throws DisplayException {
        super.drawImage(is);
        resetDimmingTimer();
    }

    private void resetDimmingTimer() throws DisplayException {
        
        // Reset display brightness
        // TODO Default brightness should be configurable rather than the actual max
        setBacklight(MAX_BACKLIGHT);
        
        // Cancel any pending tasks
        if(dimmingTask != null) {
            dimmingTask.cancel();
            dimmingTimer.purge();
        }
        
        // Create and add a new task
        dimmingTimer.schedule(dimmingTask = new TimerTask() {
            public void run() {
                try {
                    setBacklight((short) 0);
                } catch (DisplayException e) {
                    LOG.log(Level.WARNING, "Setting brightness from dimming task failed", e);
                }
                
            }
            
        }, dimmingDelay);
        
    }

}

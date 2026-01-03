package mb.iot.display;

import com.beust.jcommander.JCommander;

import mb.iot.display.api.ApiServer;
import mb.iot.display.ws.DimmingDisplayController;
import mb.iot.display.ws.DisplayController;

public class WsDisplayWeb {
    
    private static final int DEFAULT_PORT = 9090;
    
    public static void main(String[] args) {
        
        Args arg = new Args();
        JCommander.newBuilder().addObject(arg).build().parse(args);
        
        // Initialize display
        DisplayController display = arg.isDimming() ? 
                new DimmingDisplayController(arg.getWidth(), arg.getHeight(), arg.getRotation(), arg.getDimmingDelay()) : 
                    new DisplayController(arg.getWidth(), arg.getHeight(), arg.getRotation());

        // Start server
        ApiServer server = new ApiServer();
        server.start(arg.getPort() > 0 ? arg.getPort() : DEFAULT_PORT, display); 
    }
}

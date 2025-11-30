package mb.iot.display;

import com.beust.jcommander.Parameter;

public class Args {
    
    @Parameter(names = "--port")
    private int port;

    public int getPort() {
        return port;
    }
}

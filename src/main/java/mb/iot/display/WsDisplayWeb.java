package mb.iot.display;

import mb.iot.display.api.ApiServer;

public class WsDisplayWeb {
    public static void main(String[] args) {
        ApiServer server = new ApiServer();
        server.start(9090); 
    }
}

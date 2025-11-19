package mb.iot.display.api;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import mb.iot.display.ws.DisplayController;

public class ApiServer {
    
    private static final Logger LOG = Logger.getLogger(ApiServer.class.getName());
    private Server server;
    
    public void start(int serverPort) {
        
        // Initialize display
        DisplayController display = new DisplayController();
        
        // Create and start the http container
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(serverPort).build();
        ResourceConfig config = new ResourceConfig(DisplayControllerWrapper.class);
        config.register(new AbstractBinder() {
            protected void configure() {
                bind(display).to(DisplayController.class);
            }
        });
        server = JettyHttpContainerFactory.createServer(baseUri, config);
        
        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down http container...");
                server.stop();
                server.destroy();
                System.out.println("Done.");
                System.out.println("Closing display...");
                display.close();
                System.out.println("Done, exit.");
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }));
        
        try {
            server.join();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

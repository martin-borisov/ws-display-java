package mb.iot.display.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import mb.iot.display.DisplayException;
import mb.iot.display.ws.DisplayController;

@Path("api/display")
public class DisplayControllerWrapper {
    
    @Inject
    private DisplayController display;
    
    @GET
    @Path("/text")
    @Produces(MediaType.TEXT_PLAIN)
    public String drawText(
            @QueryParam("content") String text, 
            @QueryParam("size") int size) {
        
        try {
            display.drawText(text, size);
        } catch (DisplayException e) {
            throw new RuntimeException(e);
        }
        
        return "OK";
    }

}

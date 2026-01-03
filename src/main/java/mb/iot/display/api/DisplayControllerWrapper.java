package mb.iot.display.api;

import static java.text.MessageFormat.format;

import java.awt.Color;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
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
            @QueryParam("size") int size,
            @QueryParam("color") int color) {
        
        try {
            display.drawText(text, size, new Color(color));
        } catch (DisplayException e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }
        
        return "OK";
    }
    
    @GET
    @Path("/backlight")
    @Produces(MediaType.TEXT_PLAIN)
    public String setBacklight(
            @QueryParam("value") short value) {
        
        if (value > 0 && value <= DisplayController.MAX_BACKLIGHT) {
            
            try {
                display.setBacklight(value);
            } catch (DisplayException e) {
                throw new WebApplicationException(e.getMessage(), 500);
            }
            
        } else {
            throw new WebApplicationException(
                    format("Value must be from 0 to {0}", DisplayController.MAX_BACKLIGHT), 400);
        }
        
        return "OK";
    }
    
    
    @PUT
    @Path("/image")
    @Consumes("application/octet-stream")
    public String drawImage(InputStream is) {
        
        try(is) {
            display.drawImage(is);
        } catch (Exception e) {
            throw new WebApplicationException("Error drawing image", 500);
        }
        
        return "OK";
    }

}

package mb.iot.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import mb.iot.display.text.TextAlignment;
import mb.iot.display.text.TextFormat;
import mb.iot.display.text.TextRenderer;

public class Utils {
    
    public static byte[] drawImage(String path, int width, int height) {
        
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return imageToRGB565(image, width, height);
    }
    
    public static byte[] drawText(String text, int size, Color color, int dispWidth, int dispHeight) {
        BufferedImage image = new BufferedImage(dispWidth, dispHeight, BufferedImage.TYPE_USHORT_565_RGB);
        Graphics g = image.getGraphics();

        TextRenderer.drawString(
                g,
                text,
                new Font("TimesRoman", Font.PLAIN, size),
                color,
                new Rectangle(dispWidth, dispHeight),
                TextAlignment.MIDDLE,
                TextFormat.FIRST_LINE_VISIBLE
            );
        
        return imageToRGB565(image, 320, 240);
    }
    
    public static byte[] imageToRGB565(BufferedImage image, int width, int height) {
        
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
        scaledImage.getGraphics().drawImage(image, 0, 0, width, height, null); // TODO Is this necessary?
        
        byte[] bytes = new byte[width*height*2];
        int byteIdx=0;
        
        // NB: Normal vs mirrored
        //for (int i = 0; i < width; i++) {
        for (int i = width - 1; i >= 0; i--) {
            for (int j = 0; j < height; j++) {

                int aRGBpix = scaledImage.getRGB(i, j);;

                //RGB888
                int red = (aRGBpix >> 16) & 0x0FF;
                int green = (aRGBpix >> 8) & 0x0FF;
                int blue = (aRGBpix >> 0) & 0x0FF; 
                // int alpha = (aRGBpix >> 24) & 0x0FF;

                //RGB565
                red = red >> 3;
                green = green >> 2;
                blue = blue >> 3;

                //A pixel is represented by a 4-byte (32 bit) integer, like so:
                //00000000 00000000 00000000 11111111
                //^ Alpha  ^Red     ^Green   ^Blue
                //Converting to RGB565

                short pixel_to_send = 0;
                int pixel_to_send_int = 0;
                pixel_to_send_int = (red << 11) | (green << 5) | (blue);
                pixel_to_send = (short) pixel_to_send_int;


                //dividing into bytes
                byte byteH=(byte)((pixel_to_send >> 8) & 0x0FF);
                byte byteL=(byte)(pixel_to_send & 0x0FF);

                //Writing it to array - High-byte is second
                bytes[byteIdx]=byteH;
                bytes[byteIdx+1]=byteL;

                byteIdx+=2;
            }
        }
        return bytes;
    }
    
    public static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.setTitle("Sample Image");
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

}

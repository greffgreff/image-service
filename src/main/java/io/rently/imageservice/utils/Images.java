package io.rently.imageservice.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Images {

    private Images() {
        try {
            File file = new File("src/main/java/io/rently/imageservice/assets/Ubuntu-Bold.ttf");
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file));
        } catch (Exception exception) {
            Broadcaster.error(exception);
        }
    }

    public static byte[] addRentlyWatermark(byte[] imageBytes) {
        try {
            return addTextWatermark("Rently.io", "Ubuntu", imageBytes);
        } catch (Exception exception) {
            Broadcaster.error(exception);
            return imageBytes;
        }
    }

    public static byte[] addTextWatermark(String text, String fontName, byte[] imageBytes) throws Exception {
        BufferedImage sourceImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

        int fontSize = (int) (sourceImage.getHeight() * 0.045);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(fontName, Font.BOLD, fontSize));
        int x = fontSize;
        int y = (sourceImage.getHeight() - fontSize);
        g2d.drawString(text, x, y);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(sourceImage, "png", outputStream);
        byte[] watermarkedImage = outputStream.toByteArray();

        outputStream.close();
        g2d.dispose();

        return watermarkedImage;
    }
}

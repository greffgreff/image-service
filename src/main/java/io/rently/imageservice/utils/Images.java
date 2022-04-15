package io.rently.searchservice.utils;

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
    public static File RENTLY_FONT;

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
        int x = 25;
        int y = (sourceImage.getHeight() - 25);
        g2d.drawString(text, x, y);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(sourceImage, "png", outputStream);
        byte[] watermarkedImage = outputStream.toByteArray();

        outputStream.close();
        g2d.dispose();

        return watermarkedImage;
    }

    static {
        try {
            URL url = new URL("https://fonts.googleapis.com/css?family=Ubuntu:Bold");
            RENTLY_FONT = new File(url.toURI());
        } catch (Exception ignore) { }
    }
}

package util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class ColorCalculator {
    public static Color averageColor(BufferedImage image) {
        final Raster raster = image.getRaster();
        double array[] = new double[4];
        double r = 0;
        double g = 0;
        double b = 0;

        for (int x = 0; x < raster.getWidth(); x+=1000) {
            for (int y = 0; y < raster.getHeight(); y+=1000) {
                raster.getPixel(x, y, array);
                r += array[0];
                g += array[1];
                b += array[2];
              }
        }

        final double total = raster.getHeight() * raster.getWidth();
        r /= total;
        g /= total;
        b /= total;
        return new Color((int)r, (int)g, (int)b);
    }
}

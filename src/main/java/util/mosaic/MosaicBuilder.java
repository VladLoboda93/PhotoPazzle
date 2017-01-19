package util.mosaic;

import model.entity.JDBCPhotoManager;
import model.entity.PhotoManager;
import model.entity.RGB;
import model.logic.ColorCalculator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MosaicBuilder {
    private final double RESERVE_KOEFF = 0.7;
    private PhotoManager photoManager = JDBCPhotoManager.getInstanse();

    private int countWidth;
    private int countHeight;
    private int puzzleWidth;
    private int puzzleHeight;


    private BufferedImage getImageByPath(String path) {
        BufferedImage image = null;
        try (InputStream stream = new FileInputStream(path)) {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public BufferedImage createMosaic(RGB[][] rgbs) {
        BufferedImage puzzle = null;

        BufferedImage mosaic = new BufferedImage(puzzleWidth * countWidth, puzzleHeight * countHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = mosaic.getGraphics();
        for (int h = 0; h < rgbs.length; h++)
            for (int w = 0; w < rgbs[h].length; w++) {
                photoManager.findNearestPhoto(rgbs[h][w]);
                if (puzzle == null) continue;
                graphics.drawImage(puzzle, w * puzzleWidth, h * puzzleHeight, puzzleWidth, puzzleHeight, null);
            }
        graphics.dispose();
        return mosaic;
    }

    public BufferedImage createMosaic(BufferedImage masterPhoto) {

        int height = masterPhoto.getHeight();
        int width = masterPhoto.getWidth();
        int count = (int) (photoManager.count() * RESERVE_KOEFF);

        double relation = width / height;
        if (relation > 1) {
            countWidth = (int) (count / relation);
        } else {
            countWidth = (int) (count * relation);
        }
        countHeight = count - countWidth;

        puzzleWidth = masterPhoto.getWidth() / countWidth;
        puzzleHeight = masterPhoto.getHeight() / countHeight;
        RGB rgb[][] = new RGB[countHeight][countWidth];
        for (int h = 0; h < countHeight; h++) {
            for (int w = 0; w < countWidth; w++) {
                rgb[h][w] = ColorCalculator.averageColor(masterPhoto.getSubimage(h * puzzleWidth, w * puzzleHeight, puzzleWidth, puzzleHeight));
            }
        }
        BufferedImage mosaic = createMosaic(rgb);
        return mosaic;
    }
}

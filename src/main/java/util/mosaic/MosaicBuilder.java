package util.mosaic;

import model.logic.service.PhotoService;
import util.ColorCalculator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MosaicBuilder {
    private final double RESERVE_KOEFF = 0.7;
    private PhotoService service;
    private int MIN_VALUE = 4000;

    public MosaicBuilder(PhotoService service) {
        this.service = service;
    }

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

    public BufferedImage createMosaic(Color[][] colors) {
        BufferedImage puzzle = null;

        BufferedImage mosaic = new BufferedImage(puzzleWidth * countWidth, puzzleHeight * countHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = mosaic.getGraphics();
        for (int h = 0; h < colors.length; h++) {
            for (int w = 0; w < colors[h].length; w++) {
                puzzle = service.findPathNearestPhoto(colors[h][w]);
                if (puzzle != null)
                    graphics.drawImage(puzzle, w * puzzleWidth, h * puzzleHeight, puzzleWidth, puzzleHeight, null);
            }
        }
        graphics.dispose();

        return mosaic;
    }

    public BufferedImage createMosaic(BufferedImage masterPhoto) {

        int height = masterPhoto.getHeight();
        int width = masterPhoto.getWidth();
        //int count = service.count();

        int count = 2000;
        if (count > MIN_VALUE) {
            count = (int) (count * RESERVE_KOEFF);
        }

        double relation;
        if (width < height) {
            relation = (double) width / height;
        } else {
            relation = (double) height / width;
        }

        puzzleWidth = (int) Math.sqrt(masterPhoto.getWidth() * masterPhoto.getHeight() / (count * relation)) + 1;
        puzzleHeight = (int) (relation * puzzleWidth) + 1;


        countHeight = masterPhoto.getHeight() / puzzleHeight;
        countWidth = masterPhoto.getWidth() / puzzleWidth;
        System.out.println(countHeight + "высота n");
        System.out.println(countWidth + " ширина n");
        System.out.println(puzzleWidth + "ширина картинки");
        System.out.println(puzzleHeight + " высота картинки");

        Color colors[][] = new Color[countHeight][countWidth];
        for (int h = 0; h < countHeight; h++) {
            for (int w = 0; w < countWidth; w++) {
                try {
                    colors[h][w] = ColorCalculator.averageColor(masterPhoto.getSubimage(h * puzzleWidth, w * puzzleHeight, puzzleWidth, puzzleHeight));
                } catch (Exception e) {
//TODO придумать решение
                    colors[h][w] = new Color(0, 0, 0);
                }
            }
        }
        BufferedImage mosaic = createMosaic(colors);
        return mosaic;
    }
}

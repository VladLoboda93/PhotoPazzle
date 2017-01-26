package util.mosaic;

import model.logic.service.PhotoService;
import util.ColorCalculator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MosaicBuilder {
    private final double RESERVE_KOEFF = 0.7;
    private PhotoService service;
    private String photoPath;
    private int MIN_VALUE = 4000;

    public MosaicBuilder(PhotoService service, String photoPath) {
        this.photoPath = photoPath;
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

    public String createMosaic(Color[][] colors) {
        BufferedImage puzzle = null;

        BufferedImage mosaic = new BufferedImage(puzzleWidth * countWidth, puzzleHeight * countHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = mosaic.getGraphics();

        for (int w = 0; w < colors.length; w++) {
            for (int h = 0; h < colors[w].length; h++) {
                puzzle = service.findPathNearestPhoto(colors[w][h]);
                if (puzzle != null)
                    graphics.drawImage(puzzle, w * puzzleWidth, h * puzzleHeight, puzzleWidth, puzzleHeight, null);
            }
        }
        graphics.dispose();
//TODO доделать
        File file = new File( "\\res\\done1.png");
        try {
            ImageIO.write(mosaic, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file.getAbsolutePath();
    }

    public String createMosaic() {

        BufferedImage masterPhoto = getImageByPath(photoPath);
        int height = masterPhoto.getHeight();
        int width = masterPhoto.getWidth();
        int count = service.count();

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

        Color colors[][] = new Color[countWidth][countHeight];
        for (int h = 0; h < countHeight; h++) {
            for (int w = 0; w < countWidth; w++) {
                try {
                    colors[w][h] = ColorCalculator.averageColor(masterPhoto.getSubimage(w * puzzleWidth, h * puzzleHeight, puzzleWidth, puzzleHeight));
                } catch (Exception e) {
//TODO придумать решение
                    colors[w][h] = new Color(0, 0, 0);
                }
            }
        }
        String mosaicPath = createMosaic(colors);
        return mosaicPath;
    }
}

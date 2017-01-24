package controllers;


import model.logic.service.MySQLPhotoService;
import model.logic.service.PhotoService;
import util.mosaic.MosaicBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Executor {
    private String pathToImage;
    private List<String> pathToTiles;
    private String uniqueFolder;

    public Executor(String pathToImage, List<String> pathToTiles, String uniqueFolder) {
        this.pathToImage = pathToImage;
        this.pathToTiles = pathToTiles;
        this.uniqueFolder = uniqueFolder;
    }

    public void execute(){
        PhotoService service = new MySQLPhotoService(uniqueFolder);
        for(String currentPath: pathToTiles){
            File file = new File(currentPath);
            service.addPhoto(file);
        }
        MosaicBuilder builder = new MosaicBuilder(service, pathToImage);
        String mosaic = builder.createMosaic();

        //TODO Дима, ты знаешь что делать)
    }
}

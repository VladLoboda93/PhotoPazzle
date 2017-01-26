package model.logic.service;


import util.mosaic.MosaicBuilder;

import java.io.File;
import java.util.List;

public class MainClass {
    private String pathToImage;
    private List<String> pathToTiles;
    private String uniqueFolder;

    public MainClass(String pathToImage, List<String> pathToTiles, String uniqueFolder) {
        this.pathToImage = pathToImage;
        this.pathToTiles = pathToTiles;
        uniqueFolder = uniqueFolder.replaceAll("\\W", "");
        this.uniqueFolder = uniqueFolder.substring(uniqueFolder.length() / 2);
    }

    public String getPathToReadyPuzzle() {
        PhotoService service = new MySQLPhotoService(uniqueFolder);
        for (String currentPath : pathToTiles) {
            File file = new File(currentPath);
            service.addPhoto(file);
        }
        MosaicBuilder builder = new MosaicBuilder(service, pathToImage);
        String pathToPuzzle = builder.createMosaic();
        service.destroyStorage();
        return pathToPuzzle;
    }
}

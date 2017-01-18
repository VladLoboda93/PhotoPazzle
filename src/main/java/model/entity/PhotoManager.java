package model.entity;

import java.awt.image.BufferedImage;
import java.io.File;

public interface PhotoManager {

    void addPhoto(File file);
    BufferedImage findNearestPhoto(RGB rgb);
    void destroyStorage();
    int count();
}

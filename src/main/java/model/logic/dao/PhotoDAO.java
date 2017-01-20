package model.logic.dao;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public interface PhotoDAO {

    //void addPhoto(File file);
    void addPhoto(String path, Color color);

    //   BufferedImage findPathNearestPhoto(Color color);
    String findPathNearestPhoto(Color color);

    void destroyStorage();

    void deletePhotoByPath(String path);

    int count();
}

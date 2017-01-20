package model.logic.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public interface PhotoService {
    void addPhoto(File file);

    BufferedImage findPathNearestPhoto(Color color);

    void destroyStorage();

    int count();

}

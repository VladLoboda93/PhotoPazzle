package model.logic.service;

import model.logic.dao.PhotoDAO;
import model.logic.mysql.MySqlDaoFactory;
import util.ColorCalculator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MySQLPhotoService implements PhotoService {
    private MySqlDaoFactory factory;
    private PhotoDAO dao;

    public MySQLPhotoService(String customerTable) {
        factory = MySqlDaoFactory.getInstanse();
        this.dao = factory.getDao(customerTable);
    }

    @Override
    public void addPhoto(File file) {
        BufferedImage image;
        Color color = null;
        try {
            image = castToImage(file);
            color = ColorCalculator.averageColor(image);
            dao.addPhoto(file.getAbsolutePath(), color);
        } catch (Exception e) {
            //TODO прикрутить логер
            System.out.println(file.getName() + " not exist or not to be a image " + e);
            //return;
        }

        //dao.addPhoto(file.getAbsolutePath(), color);
    }

    private BufferedImage castToImage(File file) throws IOException {
        BufferedImage image;
        try (InputStream stream = new FileInputStream(file)) {
            image = ImageIO.read(stream);
        }
        return image;
    }


    @Override
    public BufferedImage findPathNearestPhoto(Color color) {
        String photoPath = null;
        BufferedImage image = null;
        boolean isPhotoExist = false;
        while (!isPhotoExist) {
            try {
                photoPath = dao.findPathNearestPhoto(color);
                image = castToImage(new File(photoPath));
                isPhotoExist = true;
            } catch (IOException e) {
                //TODO прикрутить логер
                System.out.println("File " + photoPath + " not found " + e);
                findPathNearestPhoto(color);
            }catch (Exception e) {
                //TODO прикрутить логер
                System.out.println("File " + photoPath + " WTF??? " + e);
                findPathNearestPhoto(color);
            } finally {
                dao.deletePhotoByPath(photoPath);
            }
        }
        return image;
    }

    @Override
    public void destroyStorage() {
        dao.destroyStorage();
    }

    @Override
    public int count() {
        return dao.count();
    }
}

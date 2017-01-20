package model.logic.mysql;

import model.logic.dao.DaoFactory;
import model.logic.dao.PhotoDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class MySqlDaoFactory implements DaoFactory {
    public static final String DATABASE_PROPERTIES_FILE_PATH = "/DB.properties";
    private String url;
    private String login;
    private String password;

    private static MySqlDaoFactory instance;

    private MySqlDaoFactory() {
        readProperties();
    }

    public static MySqlDaoFactory getInstanse() {
        if (instance == null) {
            instance = new MySqlDaoFactory();
        }
        return instance;
    }

    private void readProperties() {
        try (InputStream stream = MySqlDaoFactory.class.getResourceAsStream(DATABASE_PROPERTIES_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(stream);
            url = properties.getProperty("url");
            login = properties.getProperty("login");
            password = properties.getProperty("password");
        } catch (IOException e) {
        }
    }

    @Override
    public PhotoDAO getDao(String customTable) {
        try {
            return new MySqlPhotoDao(customTable,  DriverManager.getConnection(url, login, password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

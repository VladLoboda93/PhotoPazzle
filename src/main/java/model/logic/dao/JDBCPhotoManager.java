//package model.logic.dao;
//
//import util.ColorCalculator;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.sql.*;
//import java.util.Properties;
//
//
//public class JDBCPhotoManager implements PhotoDAO {
//    private static final String DATABASE_PROPERTIES_FILE_PATH = "/DB.properties";
//    private String url;
//    private String login;
//    private String password;
//    private String photoPath;
//    private Connection connection;
//    private String userTableName;
//
//    private final String ADD_PHOTO_QUERY = "INSERT INTO `temp`.? (R,G,B,path) values (?,?,?,?);";
//    private final String DELETE_PHOTO_QUERY = "DELETE FROM `temp`.? WHERE `path` = ? ;";
//    private final String DROP_TABLE = "DROP TABLE `temp`.?;";
//    private final String CREATE_TABLE = "CREATE TABLE `temp`.? (`path` VARCHAR(300) NOT NULL," +
//            "`R` INT NOT NULL,`G` INT NOT NULL,`B` INT NOT NULL, PRIMARY KEY (`path`));";
//    private final String FIND_QUERY = "SELECT path,MIN((30*pow((R - ?),2)+59*pow((G - ?),2)+11*pow((B - ?),2)))" +
//            " FROM `temp`.?";
//    private final String COUNT_QUERY = "SELECT count(path)as count FROM `temp`.?;";
//
//    private JDBCPhotoManager(String userTable) {
//        readProperties();
//        userTableName = userTable;
//        try {
//            connection = DriverManager.getConnection(url, login, password);
//            eqecuteSimpleSql(CREATE_TABLE);
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("Database connection is failed " + e);
//        }
//    }
//
//    private BufferedImage castToImage(File file) throws IOException {
//        BufferedImage image;
//        try (InputStream stream = new FileInputStream(file)) {
//            image = ImageIO.read(stream);
//        }
//        return image;
//    }
//
//    public void addPhoto(File file) {
//        BufferedImage image;
//        try {
//            image = castToImage(file);
//        } catch (IOException e) {
//            //TODO прикрутить логер
//            System.out.println(file.getName() + " not exist or not to be a image " + e);
//            return;
//        }
//        Color color = ColorCalculator.averageColor(image);
//        try (PreparedStatement statement = connection.prepareStatement(ADD_PHOTO_QUERY)) {
//            setStatementRGB(statement, color);
//            statement.setString(4, file.getAbsolutePath());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("SQL isn' t correct " + e);
//        }
//    }
//
//    public BufferedImage findPathNearestPhoto(Color color) {
//        BufferedImage image = null;
//        boolean isPhotoExist = false;
//        try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY)) {
//            while (!isPhotoExist) {
//                setStatementRGB(statement, color);
//                ResultSet set = statement.executeQuery();
//                try {
//                    if (set.next()) {
//                        photoPath = set.getString("path");
//                        image = castToImage(new File(photoPath));
//                        isPhotoExist = true;
//                    }
//                } catch (IOException e) {
//                    //TODO прикрутить логер
//                    System.out.println("File " + photoPath + " not found " + e);
//                } finally {
//                    deletePhoto(photoPath);
//                }
//            }
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("Oops, something wrong  " + e);
//        }
//        return image;
//    }
//
//    private void setStatementRGB(PreparedStatement statement, Color color) throws SQLException {
//        statement.setDouble(1, color.getRed());
//        statement.setDouble(2, color.getGreen());
//        statement.setDouble(3, color.getBlue());
//    }
//
//    private void deletePhoto(String photoPath) {
//        try (PreparedStatement statement = connection.prepareStatement(DELETE_PHOTO_QUERY)) {
//            statement.setString(1, photoPath);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("Photo " + photoPath + " wasn't deleted" + e);
//        }
//    }
//
//    public void destroyStorage() {
//        try {
//            eqecuteSimpleSql(DROP_TABLE);
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("SQL exception " + e);
//        }
//    }
//
//    @Override
//    public int count() {
//        try (Statement statement = connection.createStatement()) {
//            statement.execute(COUNT_QUERY);
//            ResultSet set = statement.getResultSet();
//            if (set.next()) {
//                return set.getInt("count");
//            }
//        } catch (SQLException e) {
//            //TODO прикрутить логер
//            System.out.println("Oops, something wrong  " + e);
//        }
//        return 0;
//    }
//
//    private ResultSet eqecuteSimpleSql(String sql) throws SQLException {
//        try (Statement statement = connection.createStatement()) {
//            statement.execute(sql);
//            return statement.getResultSet();
//        }
//    }
//
//    private void readProperties() {
//        try (InputStream stream = JDBCPhotoManager.class.getResourceAsStream(DATABASE_PROPERTIES_FILE_PATH)) {
//            Properties properties = new Properties();
//            properties.load(stream);
//            url = properties.getProperty("url");
//            login = properties.getProperty("login");
//            password = properties.getProperty("password");
//        } catch (IOException e) {
//        }
//    }
//
//    public static void main(String[] args) {
//        Color color = new Color(0,0,0);
//        System.out.println(color.getBlue());
//    }
//
//}

package model.logic.mysql;

import model.logic.dao.PhotoDAO;

import java.awt.*;
import java.sql.*;


public class MySqlPhotoDao implements PhotoDAO {
    private Connection connection;
    private String customTable;

    private String ADD_PHOTO_QUERY;
    private String DELETE_PHOTO_QUERY;
    private String DROP_TABLE;
    private String CREATE_TABLE;
    private String FIND_QUERY;
    private String COUNT_QUERY;


    public MySqlPhotoDao(String customTable, Connection connection) {
        this.customTable = customTable;
        this.connection = connection;
        initSQLQuery();
        try {
            executeSimpleSql(CREATE_TABLE);
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("Database connection is failed " + e);
        }
    }

    private void initSQLQuery() {
        ADD_PHOTO_QUERY = "INSERT INTO `temp`.`" + customTable + "` (R,G,B,path) values (?,?,?,?);";
        DELETE_PHOTO_QUERY = "DELETE FROM `temp`.`" + customTable + "` WHERE `path` = ? ;";
        DROP_TABLE = "DROP TABLE `temp`.`" + customTable + "`;";
        CREATE_TABLE = "CREATE TABLE `temp`.`" + customTable + "` (`path` VARCHAR(300) NOT NULL," +
                "`R` INT NOT NULL,`G` INT NOT NULL,`B` INT NOT NULL, PRIMARY KEY (`path`));";
        FIND_QUERY = "SELECT path FROM `temp`.`" + customTable + "` WHERE ((30*pow((R - ?),2)+59*pow((G - ?),2)+11*pow((B - ?),2)))" +
                "= (SELECT MIN((30*pow((R - ?),2)+59*pow((G - ?),2)+11*pow((B - ?),2))) FROM `temp`.`" + customTable + "`);";
//       " SELECT path`temp`.`" + customTable + "` WHERE (30*pow((R - ?),2)+59*pow((G - ?),2)+11*pow((B - ?),2))  " +
//               "= (SELECT MIN((30*pow((R - ?),2)+59*pow((G - ?),2)+11*pow((B - ?),2)))FROM `temp`.`" + customTable + "`;";
//
        COUNT_QUERY = "SELECT count(path)as count FROM `temp`.`" + customTable + "`;";
    }


    @Override
    public void addPhoto(String path, Color color) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_PHOTO_QUERY)) {
            setStatementRGB(statement, color);
            statement.setString(4, path);
            statement.executeUpdate();
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("SQL isn' t correct " + e);
        }
    }

    private void setStatementRGB(PreparedStatement statement, Color color, int shift) throws SQLException {
        statement.setDouble(1 + shift, color.getRed());
        statement.setDouble(2 + shift, color.getGreen());
        statement.setDouble(3 + shift, color.getBlue());
    }

    private void setStatementRGB(PreparedStatement statement, Color color) throws SQLException {
        setStatementRGB(statement, color, 0);
    }

    @Override
    public String findPathNearestPhoto(Color color) {
        String path = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_QUERY)) {
            setStatementRGB(statement, color);
            setStatementRGB(statement,color,3);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                path = set.getString("path");
            }
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("Oops, something wrong  " + e);
        }
        return path;
    }

    @Override
    public void destroyStorage() {
        try {
            executeSimpleSql(DROP_TABLE);
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("SQL exception " + e);
        }
    }

    @Override
    public void deletePhotoByPath(String path) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PHOTO_QUERY)) {
            statement.setString(1, path);
            statement.executeUpdate();
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("Photo " + path + " wasn't deleted" + e);
        }
    }

    private void executeSimpleSql(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Override
    public int count() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(COUNT_QUERY);
            ResultSet set = statement.getResultSet();
            if (set.next()) {
                return set.getInt("count");
            }
        } catch (SQLException e) {
            //TODO прикрутить логер
            System.out.println("Oops, something wrong  " + e);
        }
        return 0;
    }
}

package model.logic.dao;

/**
 * Created by Vlad on 19.01.2017.
 */
public interface DaoFactory {
    PhotoDAO getDao(String customTable);
}

package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}

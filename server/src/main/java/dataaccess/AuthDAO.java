package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(String userName) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAllAuth();
}

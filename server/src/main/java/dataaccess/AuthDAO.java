package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String userName) throws DataAccessException;

    AuthData createAuth(String userName) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}

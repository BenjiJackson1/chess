package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuthData(String userName) throws DataAccessException;

    AuthData createAuthData(String userName) throws DataAccessException;
}

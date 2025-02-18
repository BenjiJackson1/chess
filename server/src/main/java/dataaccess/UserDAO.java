package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserData(String username) throws DataAccessException;

    UserData addUserData(UserData userData) throws DataAccessException;
}

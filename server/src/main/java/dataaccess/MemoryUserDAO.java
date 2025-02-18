package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> allUsers = new HashMap<>();

    public UserData getUser(String username) throws DataAccessException {
        return allUsers.get(username);
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        UserData user = new UserData(userData.username(), userData.password(), userData.password());
        allUsers.put(user.username(), user);
        return user;
    }

    public void deleteAllUsers() throws DataAccessException {
        allUsers.clear();
    }


}

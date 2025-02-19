package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> allUsers = new HashMap<>();

    public UserData getUser(String username, String password) throws DataAccessException {
        if (!allUsers.containsKey(username) || (!allUsers.get(username).password().equals(password))){
            throw new DataAccessException("Error: unauthorized");
        }
        return allUsers.get(username);
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        UserData user = new UserData(userData.username(), userData.password(), userData.email());
        if (allUsers.containsKey(userData.username())){
            throw new DataAccessException("Error: already taken");
        }
        allUsers.put(user.username(), user);
        return user;
    }

    public void deleteAllUsers() throws DataAccessException {
        allUsers.clear();
    }


}

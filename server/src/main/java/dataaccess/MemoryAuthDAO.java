package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> allAuth = new HashMap<>();

    public AuthData getAuth(String authToken) throws DataAccessException {
        if (!allAuth.containsKey(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        return allAuth.get(authToken);
    }

    public AuthData createAuth(String userName) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userName);
        allAuth.put(authToken, authData);
        return authData;
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        if (!allAuth.containsKey(authToken)){
            throw new DataAccessException("Error: unauthorized");
        }
        allAuth.remove(authToken);
    }

    public void deleteAllAuth(){
        allAuth.clear();
    }
}

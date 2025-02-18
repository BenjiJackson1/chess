package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> allAuth = new HashMap<>();

    public AuthData getAuth(String userName) throws DataAccessException {
        return allAuth.get(userName);
    }

    public AuthData createAuth(String userName) throws DataAccessException {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), userName);
        return authData;
    }
}

package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.request.RegisterRequest;
import model.result.RegisterResult;

public class UserService {
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public RegisterResult register(RegisterRequest registerRequest){
        UserData userData;
        try {
            userData = userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, e.getMessage());
        }
        AuthData authData;
        try {
            authData = authDAO.createAuth(registerRequest.username());
        } catch (DataAccessException e) {
            return new RegisterResult(null, null, e.getMessage());
        }
        return new RegisterResult(userData.username(), authData.authToken(), null);
    }
}

package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.LogoutResult;
import model.result.RegisterResult;

public class UserService {
    private final UserDAO userDAO;

    {
        try {
            userDAO = new MySQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final AuthDAO authDAO;

    {
        try {
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData getAuth(String authToken){
        AuthData authData;
        try {
            authData = authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            return new AuthData(null, null);
        }
        return authData;
    }

    public RegisterResult register(RegisterRequest registerRequest){
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            return new RegisterResult(null, null, "Error: bad request");
        }
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

    public LoginResult login(LoginRequest loginRequest){
        UserData userData;
        try {
            userData = userDAO.getUser(loginRequest.username(), loginRequest.password());
        } catch (DataAccessException e) {
            return new LoginResult(null, null, e.getMessage());
        }
        AuthData authData;
        try {
            authData = authDAO.createAuth(loginRequest.username());
        } catch (DataAccessException e) {
            return new LoginResult(null, null, e.getMessage());
        }
        return new LoginResult(userData.username(), authData.authToken(), null);
    }

    public LogoutResult logout(LogoutRequest logoutRequest){
        try {
            authDAO.deleteAuth(logoutRequest.authToken());
        } catch (DataAccessException e) {
            return new LogoutResult(e.getMessage());
        }
        return new LogoutResult(null);
    }

    public void clear(){
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuth();
    }
}

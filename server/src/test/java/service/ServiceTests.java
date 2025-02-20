package service;

import chess.ChessGame;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.LogoutResult;
import model.result.RegisterResult;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() {
        UserService userService = new UserService();
        RegisterResult registerResult = userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        Assertions.assertNull(registerResult.message(), "Bad result returned an error message");
        Assertions.assertNotNull(registerResult.authToken(), "Auth Token was not generated in registration");
        Assertions.assertNotNull(registerResult.username(), "RegisterResult was not formatted correctly");
    }
    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        RegisterResult registerResultCopy = userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        Assertions.assertNotNull(registerResultCopy.message(), "RegisterResult did not return an error message");
        Assertions.assertEquals(registerResultCopy.message(), "Error: already taken", "Incorrect error message was returned");
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        LoginResult loginResult = userService.login(new LoginRequest("benji55", "passw0rd"));
        Assertions.assertNull(loginResult.message(), "Login Result returned an error message");
        Assertions.assertEquals(loginResult.username(), "benji55", "Incorrect username was fetched");
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        LoginResult loginResult = userService.login(new LoginRequest("benji55", "passw0r"));
        Assertions.assertNotNull(loginResult.message(), "Login Result did not return an error message");
        Assertions.assertEquals(loginResult.message(), "Error: unauthorized", "Incorrect error message returned");
    }

    @Test
    @Order(5)
    @DisplayName("Clear Test")
    public void clearTest() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        userService.login(new LoginRequest("benji55", "passw0rd"));
        userService.clear();
        LoginResult loginResult = userService.login(new LoginRequest("benji55", "passw0rd"));
        Assertions.assertNotNull(userService, "Login Result did not return an error message");
        Assertions.assertEquals(loginResult.message(), "Error: unauthorized", "Incorrect error message returned");
    }

    @Test
    @Order(6)
    @DisplayName("Login Success")
    public void logoutSuccess() {
        UserService userService = new UserService();
        userService.register(new RegisterRequest("benji55", "passw0rd", "benji55@byu.edu"));
        LoginResult loginResult = userService.login(new LoginRequest("benji55", "passw0rd"));
        LogoutResult logoutResult = userService.logout(new LogoutRequest(loginResult.authToken()));
        Assertions.assertNull(logoutResult.message(), "Logout Result returned an error message");
    }

    @Test
    @Order(7)
    @DisplayName("Login Failure For Non-Existing User")
    public void logoutFailure() {
        UserService userService = new UserService();
        LogoutResult logoutResult = userService.logout(new LogoutRequest("benji55"));
        Assertions.assertNotNull(logoutResult.message(), "Login Result did not return an error message");
        Assertions.assertEquals(logoutResult.message(), "Error: unauthorized", "Incorrect error message returned");
    }
}

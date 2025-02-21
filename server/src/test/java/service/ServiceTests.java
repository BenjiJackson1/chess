package service;

import model.request.*;
import model.result.*;
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

    @Test
    @Order(8)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        GameService gameService = new GameService();
        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest("Game1"));
        Assertions.assertNull(createGameResult.message(), "Logout Result returned an error message");
    }

    @Test
    @Order(9)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        GameService gameService = new GameService();
        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(null));
        Assertions.assertEquals(createGameResult.message(), "Error: bad request", "Logout Result returned an error message");
    }

    @Test
    @Order(10)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        GameService gameService = new GameService();
        gameService.createGame(new CreateGameRequest("Game1"));
        JoinGameResult joinGameResult = gameService.joinGame(new JoinGameRequest("WHITE", 1), "Benji");
        Assertions.assertNull(joinGameResult.message(), "Join Game Result returned an error message");
    }

    @Test
    @Order(11)
    @DisplayName("Join Game Failure - Nonexistent Color")
    public void joinGameFailure() {
        GameService gameService = new GameService();
        gameService.createGame(new CreateGameRequest("Game1"));
        JoinGameResult joinGameResult = gameService.joinGame(new JoinGameRequest("ORANGE", 1), "Benji");
        Assertions.assertEquals(joinGameResult.message(), "Error: bad request", "Join Game Result returned wrong error message");
    }

    @Test
    @Order(12)
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
        GameService gameService = new GameService();
        gameService.createGame(new CreateGameRequest("Game1"));
        ListGamesResult listGamesResult = gameService.listGames();
        Assertions.assertNull(listGamesResult.message(), "Join Game Result returned an error message");
    }

    @Test
    @Order(13)
    @DisplayName("List Games Failure - Unauthorized")
    public void listGamesFailure() {
        GameService gameService = new GameService();
        gameService.createGame(new CreateGameRequest("Game1"));
        gameService.clear();
        ListGamesResult listGamesResult = gameService.listGames();
        Assertions.assertNull(listGamesResult.message(), "Join Game Result returned an error message");
    }
}

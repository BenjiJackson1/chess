package service;

import chess.ChessGame;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

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
}

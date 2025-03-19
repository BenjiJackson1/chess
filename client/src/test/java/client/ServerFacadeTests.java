package client;

import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Good Register")
    void register() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @Order(2)
    @DisplayName("Failed Register - Multiple users with same username")
    void register_fails() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try {
            facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        } catch (Exception ex){
            assertTrue(ex.getMessage() == "Error: already taken");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Good Login")
    void loginPositive() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var userData = facade.login(new LoginRequest("p3", "password"));
        assertTrue(userData.authToken().length() > 9);
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login")
    void loginFailure() throws Exception {
        try{
            facade.login(new LoginRequest("p3", "password"));
        } catch (Exception ex){
            assertTrue(ex.getMessage() == "Error: unauthorized");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    void logoutPositive() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var userData = facade.login(new LoginRequest("p3", "password"));
        var result = facade.logout(new LogoutRequest(userData.authToken()));
        assertTrue(result.message() == null);
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout - Wrong Auth")
    void logoutFailure() throws Exception {
        try{
            facade.logout(new LogoutRequest("SurelyWon'tWork"));
        }catch (Exception ex){
            assertTrue(ex.getMessage()== "Error: unauthorized");
        }

    }
}

package client;

import model.request.*;
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

    @Test
    @Order(7)
    @DisplayName("Good Game Creation")
    void gameCreationPositive() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var userData = facade.login(new LoginRequest("p3", "password"));
        var result = facade.createGame(new CreateGameRequest("JUEGO UNO"), userData.authToken());
        assertTrue(result.message() == null);
    }

    @Test
    @Order(8)
    @DisplayName("Game Creation Failure - Not Authorized")
    void gameCreationFailure() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try{
            facade.createGame(new CreateGameRequest("JUEGO UNO"), "Wrong Auth");
        } catch (Exception e){
            assertTrue(e.getMessage() == "Error: unauthorized");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Join Game Success")
    void joinGameGood() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var gameData = facade.createGame(new CreateGameRequest("Bob's Game"), authData.authToken());
        var result = facade.joinGame(new JoinGameRequest("WHITE", gameData.gameID()), authData.authToken());
        assertTrue(result.message()== null);
    }

    @Test
    @Order(10)
    @DisplayName("Join Game Failure - Game doesn't exist")
    void joinGameFailure() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try{
            facade.joinGame(new JoinGameRequest("WHITE", 18), authData.authToken());
        } catch (Exception ex){
            assertTrue(ex.getMessage() == "Error: unauthorized");
        }
    }

    @Test
    @Order(11)
    @DisplayName("List Game Success")
    void listGamesGood() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest("Bob's Game"), authData.authToken());
        facade.createGame(new CreateGameRequest("Bob's Game 2"), authData.authToken());
        facade.createGame(new CreateGameRequest("Ender's Game"), authData.authToken());
        var allGames = facade.listGames(authData.authToken());
        assertTrue(allGames.games().size() == 3);
    }

    @Test
    @Order(12)
    @DisplayName("List Game Failure - Bad Auth")
    void listGamesFailure() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        facade.createGame(new CreateGameRequest("Bob's Game"), authData.authToken());
        facade.createGame(new CreateGameRequest("Bob's Game 2"), authData.authToken());
        facade.createGame(new CreateGameRequest("Ender's Game"), authData.authToken());
        try{
            facade.listGames("Bad");
        } catch (Exception ex){
            assertTrue(ex.getMessage() == "Error: unauthorized");
        }
    }
}

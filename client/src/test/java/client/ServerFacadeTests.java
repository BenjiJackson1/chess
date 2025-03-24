package client;

import model.request.*;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


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
    void registerFailure() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try {
            facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        } catch (Exception ex){
            assertSame("Error: already taken", ex.getMessage());
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
    void loginFailure() {
        try{
            facade.login(new LoginRequest("p3", "password"));
        } catch (Exception ex){
            assertSame("Error: unauthorized", ex.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    void logoutPositive() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var userData = facade.login(new LoginRequest("p3", "password"));
        var result = facade.logout(new LogoutRequest(userData.authToken()));
        assertNull(result.message());
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout - Wrong Auth")
    void logoutFailure() {
        try{
            facade.logout(new LogoutRequest("SurelyWon'tWork"));
        }catch (Exception ex){
            assertSame("Error: unauthorized", ex.getMessage());
        }

    }

    @Test
    @Order(7)
    @DisplayName("Good Game Creation")
    void gameCreationPositive() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var userData = facade.login(new LoginRequest("p3", "password"));
        var result = facade.createGame(new CreateGameRequest("JUEGO UNO"), userData.authToken());
        assertNull(result.message());
    }

    @Test
    @Order(8)
    @DisplayName("Game Creation Failure - Not Authorized")
    void gameCreationFailure() throws Exception {
        facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try{
            facade.createGame(new CreateGameRequest("JUEGO UNO"), "Wrong Auth");
        } catch (Exception e){
            assertSame("Error: unauthorized", e.getMessage());
        }
    }

    @Test
    @Order(9)
    @DisplayName("Join Game Success")
    void joinGameGood() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        var gameData = facade.createGame(new CreateGameRequest("Bob's Game"), authData.authToken());
        var result = facade.joinGame(new JoinGameRequest("WHITE", gameData.gameID()), authData.authToken());
        assertNull(result.message());
    }

    @Test
    @Order(10)
    @DisplayName("Join Game Failure - Game doesn't exist")
    void joinGameFailure() throws Exception {
        var authData = facade.register(new RegisterRequest("p3", "password", "p1@email.com"));
        try{
            facade.joinGame(new JoinGameRequest("WHITE", 18), authData.authToken());
        } catch (Exception ex){
            assertSame("Error: unauthorized", ex.getMessage());
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
        assertEquals(3, allGames.games().size());
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
            assertSame("Error: unauthorized", ex.getMessage());
        }
    }
}

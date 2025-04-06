package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Timer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests {

    @Test
    @Order(1)
    @DisplayName("Get Auth Success")
    public void getAuthSuccess(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            AuthData authData = authDAO.createAuth("benji");
            AuthData myAuthData = authDAO.getAuth(authData.authToken());
            Assertions.assertEquals(authData.authToken(), myAuthData.authToken(), "AuthTokens do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Get Auth Failure")
    public void getAuthFailure(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            authDAO.getAuth("benji");
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Create Auth Success")
    public void createAuthSuccess(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            AuthData authData = authDAO.createAuth("benji");
            Assertions.assertEquals(authData.username(), "benji", "Usernames do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Create Auth Failure")
    public void createAuthFailure(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            authDAO.createAuth(null);
        } catch (DataAccessException e){
            Assertions.assertNotNull(e.getMessage(), "No error message");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Delete Auth Success")
    public void deleteAuthSuccess(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            AuthData authData = authDAO.createAuth("benji");
            authDAO.deleteAuth(authData.authToken());
            authDAO.getAuth(authData.authToken());
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(6)
    @DisplayName("Delete Auth Failure")
    public void deleteAuthFailure(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            authDAO.deleteAuth("bingo");
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Delete All Auth Success")
    public void deleteAllAuthTest(){
        AuthDAO authDAO;
        try{
            authDAO = new MySQLAuthDAO();
            AuthData authData = authDAO.createAuth("benji");
            authDAO.createAuth("jess");
            authDAO.deleteAllAuth();
            authDAO.getAuth(authData.authToken());
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Get User Success")
    public void getUserSuccess(){
        UserDAO userDAO;
        try{
            userDAO = new MySQLUserDAO();
            UserData userData = userDAO.createUser(new UserData("benji", "jackson", "benji@jackson.com"));
            UserData myUserdata = userDAO.getUser(userData.username(), userData.password());
            Assertions.assertEquals(userData.username(), myUserdata.username(), "Usernames do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(9)
    @DisplayName("Get User Failure")
    public void getUserFailure(){
        UserDAO userDAO;
        try{
            userDAO = new MySQLUserDAO();
            userDAO.getUser("benji", "jackson");
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Create User Success")
    public void createUserSuccess(){
        UserDAO userDAO;
        try{
            userDAO = new MySQLUserDAO();
            UserData userData = userDAO.createUser(new UserData("bin", "jackson", "benji@jackson.com"));
            Assertions.assertEquals(userData.username(), "bin", "Usernames do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(11)
    @DisplayName("Create User Failure")
    public void createUserFailure(){
        UserDAO userDAO;
        try{
            userDAO = new MySQLUserDAO();
            userDAO.getUser(null, null);
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(12)
    @DisplayName("Delete All Users Success")
    public void deleteAllUsersTest(){
        UserDAO userDAO;
        try{
            userDAO = new MySQLUserDAO();
            userDAO.createUser(new UserData("j", "j@", "johnson@gmail.com"));
            userDAO.createUser(new UserData("b", "b@", "bin@gmail.com"));
            userDAO.deleteAllUsers();
            userDAO.getUser("j", "j@");
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Incorrect error message");
        }
    }

    @Test
    @Order(13)
    @DisplayName("Create Game Success")
    public void createGameSuccess(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            GameData gameData = gameDAO.createGame("Jegor Uno");
            Assertions.assertEquals(gameData.gameName(), "Jegor Uno", "Game Names do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(14)
    @DisplayName("Create Game Failure")
    public void createGameFailure(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.createGame(null);
        } catch (DataAccessException e){
            Assertions.assertEquals("Error: bad request", e.getMessage(), "Incorrect error message");
        }
    }

    @Test
    @Order(15)
    @DisplayName("Get Game Success")
    public void getGameSuccess(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            GameData gameData = gameDAO.createGame("Jegor Three");
            GameData gameData1 = gameDAO.getGame(gameData.gameID());
            Assertions.assertEquals(gameData.gameID(), gameData1.gameID(), "Game IDs do not match.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(16)
    @DisplayName("Get Game Failure")
    public void getGameFailure(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.deleteAllGames();
            gameDAO.createGame(null);
        } catch (DataAccessException e){
            Assertions.assertEquals("Error: bad request", e.getMessage(), "Incorrect error message");
        }
    }

    @Test
    @Order(17)
    @DisplayName("List Games Success")
    public void listGamesSuccess(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.createGame("Jegor");
            gameDAO.createGame("Here");
            List<GameData> gamesList = gameDAO.listGames();
            Assertions.assertEquals(2, gamesList.size(), "Not the correct size.");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(18)
    @DisplayName("List Games Failure")
    public void listGamesFailure(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.deleteAllGames();
            gameDAO.listGames();
        } catch (DataAccessException e){
            Assertions.assertEquals("Error: bad request", e.getMessage(), "Incorrect error message");
        }
    }

    @Test
    @Order(19)
    @DisplayName("Delete All Games Success")
    public void deleteAllGamesTest(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.deleteAllGames();
            gameDAO.getGame(1);
        } catch (DataAccessException e){
            Assertions.assertEquals(e.getMessage(), "Error: bad request", "Incorrect error message");
        }
    }

    @Test
    @Order(20)
    @DisplayName("Update Game Success")
    public void updateGameSuccess(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            GameData gameData = gameDAO.createGame("Kasparov");
            ChessGame newChessGame = gameData.game();
            try{
                newChessGame.makeMove(new ChessMove(new ChessPosition(2,2), new ChessPosition(3,2), null));
            } catch (InvalidMoveException e){
                Assertions.fail("Invalid move");
            }
            gameDAO.updateGame(gameData.gameID(), new GameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), newChessGame));
            Assertions.assertEquals( "BLACK", gameDAO.getGame(gameData.gameID()).game().getTeamTurn().toString(), "Move was not made");
        } catch (DataAccessException e){
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(21)
    @DisplayName("Update Game Failure")
    public void updateGameFailure(){
        GameDAO gameDAO;
        try{
            gameDAO = new MySQLGameDAO();
            gameDAO.deleteAllGames();
            ChessGame chessGame = new ChessGame();
            try{
                chessGame.makeMove(new ChessMove(new ChessPosition(2,2), new ChessPosition(3,2), null));
            } catch (InvalidMoveException e){
                Assertions.fail("Invalid move");
            }
            gameDAO.updateGame(1, new GameData(1, null, null, "New", chessGame));
        } catch (DataAccessException e){
            Assertions.assertEquals("Error: bad request", e.getMessage(), "Incorrect error message");
        }
    }

}

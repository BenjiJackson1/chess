package service;

import dataaccess.*;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;

public class GameService {
    private final GameDAO gameDAO;

    {
        try {
            gameDAO = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        GameData gameData;
        try {
            gameData = gameDAO.createGame(createGameRequest.gameName());
        } catch (DataAccessException e) {
            return new CreateGameResult(null, "Error: bad request");
        }
        return new CreateGameResult(gameData.gameID(), null);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String userName){
        GameData gameData;
        try {
            gameData = gameDAO.getGame(joinGameRequest.gameID());
        }catch (DataAccessException e){
            return new JoinGameResult(e.getMessage());
        }
        if (joinGameRequest.playerColor() == null){
            return new JoinGameResult("Error: bad request");
        }
        if (!joinGameRequest.playerColor().equals("WHITE") && !joinGameRequest.playerColor().equals("BLACK")){
            return new JoinGameResult("Error: bad request");
        }
        if (joinGameRequest.playerColor().equals("WHITE") && gameData.whiteUsername() == null){
            try{
                gameDAO.updateGame(gameData.gameID(), new GameData(gameData.gameID(), userName, gameData.blackUsername(),
                        gameData.gameName(), gameData.game()));
                return new JoinGameResult(null);
            }catch (DataAccessException e){
                return new JoinGameResult(e.getMessage());
            }
        }
        if (joinGameRequest.playerColor().equals("BLACK") && gameData.blackUsername() == null){
            try{
                gameDAO.updateGame(gameData.gameID(), new GameData(gameData.gameID(), gameData.whiteUsername(), userName,
                        gameData.gameName(), gameData.game()));
                return new JoinGameResult(null);
            }catch (DataAccessException e){
                return new JoinGameResult(e.getMessage());
            }
        }
        return new JoinGameResult("Error: already taken");
    }

    public ListGamesResult listGames(){
        return new ListGamesResult(gameDAO.listGames(), null);
    }

    public GameData getGame(int gameID){
        try{
            return gameDAO.getGame(gameID);
        } catch (Exception e){
            return new GameData(-1, null, null, null, null);
        }
    }

    public void clear(){
        gameDAO.deleteAllGames();
    }
}

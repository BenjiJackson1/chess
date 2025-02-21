package service;

import dataaccess.*;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.result.CreateGameResult;
import model.result.JoinGameResult;
import model.result.ListGamesResult;

import java.awt.*;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();

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
        ListGamesResult listGamesResult = new ListGamesResult(gameDAO.listGames());
        return listGamesResult;
    }

    public void clear(){
        gameDAO.deleteAllGames();
    }
}

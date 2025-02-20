package service;

import dataaccess.*;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.RegisterRequest;
import model.result.CreateGameResult;
import model.result.RegisterResult;

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
}

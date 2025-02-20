package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> allGames = new HashMap<>();

    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null){
            throw new DataAccessException("Error: bad request");
        }
        GameData gameData = new GameData(allGames.size()+1, null, null, gameName, new ChessGame());
        allGames.put(allGames.size()+1, gameData);
        return gameData;
    }
}

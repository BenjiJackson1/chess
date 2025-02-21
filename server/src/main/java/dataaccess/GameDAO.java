package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    GameData updateGame(Integer gameID, GameData updatedGameData) throws DataAccessException;

    ArrayList<GameData> listGames();

    void deleteAllGames();
}

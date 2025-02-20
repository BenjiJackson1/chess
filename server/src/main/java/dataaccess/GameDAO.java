package dataaccess;

import model.GameData;

public interface GameDAO {
    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    GameData updateGame(Integer gameID, GameData updatedGameData) throws DataAccessException;

    void deleteAllGames();
}

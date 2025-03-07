package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO extends MySQLDAO implements AuthDAO{

    public MySQLAuthDAO() throws DataAccessException{
        configureDatabase();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public AuthData createAuth(String userName) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try{
            executeUpdate(statement, authToken, userName);
            return new AuthData(authToken, userName);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: already taken");
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        getAuth(authToken);
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void deleteAllAuth() {
        var statement = "TRUNCATE auth";
        try{
            executeUpdate(statement);
        }
        catch (DataAccessException ignored){
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("authToken");
        return new AuthData(authToken, username);
    }

    @Override
    String[] getStatements() {
        return new String[]{
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
    }
}

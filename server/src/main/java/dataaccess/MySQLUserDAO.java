package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class MySQLUserDAO extends MySQLDAO implements UserDAO{

    public MySQLUserDAO() throws DataAccessException{
        configureDatabase();
    }

    public UserData getUser(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?");
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserData user = readUser(rs);
                    if (!BCrypt.checkpw(password, user.password())){
                        throw new DataAccessException("Error: unauthorized");
                    }
                    return new UserData(user.username(), password, user.email());
                }
            }
        } catch (Exception e) {
                throw new DataAccessException("Error: unauthorized");
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try{
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            executeUpdate(statement, userData.username(), hashedPassword, userData.email());
            return userData;
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: already taken");
        }
    }

    public void deleteAllUsers(){
        var statement = "TRUNCATE users";
        try{
            executeUpdate(statement);
        }
        catch (DataAccessException ignored){
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    String[] getStatements() {
        return new String[]{
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256),
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
    }
}

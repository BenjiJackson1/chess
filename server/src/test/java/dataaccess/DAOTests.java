package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests {

    @Test
    @Order(1)
    @DisplayName("Get Auth Success")
    public void getAuthSuccess(){
        AuthDAO authDAO = null;
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
}

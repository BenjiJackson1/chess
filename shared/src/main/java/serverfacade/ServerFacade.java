package serverfacade;

import exception.ResponseException;
import com.google.gson.Gson;
import model.request.*;
import model.result.*;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException{
        var path = "/user";
        try {
            return this.makeRequest("POST", path, registerRequest, RegisterResult.class, null);
        } catch (ResponseException ex){
            throw new ResponseException(403, "Error: already taken");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException{
        var path = "/session";
        try {
            return this.makeRequest("POST", path, loginRequest, LoginResult.class, null);
        } catch (ResponseException ex){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ResponseException{
        var path = "/session";
        try{
            return this.makeRequest("DELETE", path, logoutRequest, LogoutResult.class, logoutRequest.authToken());
        } catch (ResponseException ex){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws ResponseException{
        var path = "/game";
        try{
            return this.makeRequest("POST", path, createGameRequest, CreateGameResult.class, authToken);
        } catch (ResponseException ex){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest, String authToken) throws ResponseException{
        var path = "/game";
        try{
            return this.makeRequest("PUT", path, joinGameRequest, JoinGameResult.class, authToken);
        } catch (ResponseException ex){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public ListGamesResult listGames(String authToken) throws ResponseException{
        var path = "/game";
        try{
            return this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
        } catch (ResponseException ex){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public LogoutResult clear() throws ResponseException{
        var path = "/db";
        return this.makeRequest("DELETE", path, null, LogoutResult.class, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String header) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeHeader(header, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String header, HttpURLConnection http) throws IOException {
        if (header != null) {
            http.addRequestProperty("Authorization", header);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

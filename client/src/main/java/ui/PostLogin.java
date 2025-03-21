package ui;

import model.request.CreateGameRequest;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import server.ServerFacade;

import java.util.Arrays;

public class PostLogin implements Client{
    private final ServerFacade server;
    private final String serverUrl;
    private final String authToken;

    public PostLogin(String serverUrl, String authToken){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> new ReplResponse("quit", State.POSTLOGIN, authToken);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "logout" -> logout(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.POSTLOGIN, authToken);
        }
    }

    public ReplResponse logout(String ... params){
        try{
            server.logout(new LogoutRequest(authToken));
            return new ReplResponse("You logged out!", State.PRELOGIN, authToken);
        } catch (Exception e){
            return new ReplResponse("Unable to log out. Try quitting.", State.POSTLOGIN, authToken);
        }
    }

    public ReplResponse createGame(String ... params){
        try{
            server.createGame(new CreateGameRequest(params[0]), authToken);
            return new ReplResponse("Game was created!", State.POSTLOGIN, authToken);
        } catch (Exception e){
            return new ReplResponse("Expected: <GAME_NAME>", State.POSTLOGIN, authToken);
        }
    }

    public ReplResponse listGames(){
        try{
            var result = server.listGames(authToken);
            String gameInfo = "";
            int index = 1;
            for (var game: result.games()) {
                gameInfo = gameInfo + index + ". " + game.gameName() + " WHITE: " + game.whiteUsername() + " BLACK: " + game.blackUsername() + "\n";
                index += 1;
            }       
            return new ReplResponse(gameInfo, State.POSTLOGIN, authToken);
        } catch (Exception e){
            return new ReplResponse("Expected: <GAME_NAME>", State.POSTLOGIN, authToken);
        }
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - logout - to logout of this account
                - create <GAME_NAME> - to create a chess game
                - list - to list all current games
                - play <ID> [WHITE|BLACK] - to join a game with specified team color
                - observe <ID> - to watch a game
                - quit - playing chess
                - help - with possible commands
                """, State.POSTLOGIN, authToken);
    }
}

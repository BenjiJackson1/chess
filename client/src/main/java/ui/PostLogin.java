package ui;

import chess.ChessGame;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LogoutRequest;
import serverFacade.ServerFacade;

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
                case "play" -> joinGame(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.POSTLOGIN, authToken);
        }
    }

    public ReplResponse logout(){
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

    public ReplResponse joinGame(String ... params){
        try{
            var gameList = server.listGames(authToken);
            int num = Integer.parseInt(params[0]);
            int gameID = gameList.games().get(num-1).gameID();
            ChessGame game = gameList.games().get(num-1).game();
            System.out.println(gameList.games().get(num-1).gameName());
            server.joinGame(new JoinGameRequest(params[1].toUpperCase(), gameID), authToken);
            return new ReplResponse(game.getBoard().toString(), State.POSTLOGIN, authToken);
        } catch (Exception e){
            return new ReplResponse("Expected: <ID> [WHITE|BLACK]", State.POSTLOGIN, authToken);
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

package ui;

import chess.ChessGame;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LogoutRequest;
import serverfacade.ServerFacade;
import static ui.ChessBoardPrinter.*;

import java.util.Arrays;

import static ui.EscapeSequences.*;

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
                case "quit" -> new ReplResponse("quit", State.POSTLOGIN, authToken, -1);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> joinGame(params);
                case "observe" -> observerGame(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.POSTLOGIN, authToken, -1);
        }
    }

    public ReplResponse logout(){
        try{
            server.logout(new LogoutRequest(authToken));
            return new ReplResponse("You logged out!", State.PRELOGIN, authToken, -1);
        } catch (Exception e){
            return new ReplResponse("Unable to log out. Try quitting.", State.POSTLOGIN, authToken, -1);
        }
    }

    public ReplResponse createGame(String ... params){
        try{
            server.createGame(new CreateGameRequest(params[0]), authToken);
            return new ReplResponse("Game was created!", State.POSTLOGIN, authToken, -1);
        } catch (Exception e){
            return new ReplResponse("Expected: <GAME_NAME>", State.POSTLOGIN, authToken, -1);
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
            return new ReplResponse(gameInfo, State.POSTLOGIN, authToken, -1);
        } catch (Exception e){
            return new ReplResponse("Unable to list the games!", State.POSTLOGIN, authToken, -1);
        }
    }

    public ReplResponse joinGame(String ... params){
        int numGames = 0;
        try{
            var gameList = server.listGames(authToken);
            numGames = gameList.games().size();
            int num = Integer.parseInt(params[0]);
            int gameID = gameList.games().get(num-1).gameID();
            ChessGame game = gameList.games().get(num-1).game();
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            server.joinGame(new JoinGameRequest(params[1].toUpperCase(), gameID), authToken);
            printGame(game, params[1]);
            return new ReplResponse("Game: " + gameList.games().get(num-1).gameName(), State.GAMEPLAY, authToken, gameID);
        } catch (Exception e){
            if (params.length == 2){
                System.out.print(SET_TEXT_COLOR_WHITE);
                try{
                    if (Integer.parseInt(params[0]) > numGames || Integer.parseInt(params[0]) < 0){
                        return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken, -1);
                    }
                } catch (NumberFormatException ex){
                    return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken, -1);
                }
                if (params[1].equalsIgnoreCase("white") || params[1].equalsIgnoreCase("black")){
                    return new ReplResponse("Chosen color is already taken!", State.POSTLOGIN, authToken, -1);
                }
            }
            System.out.print(SET_TEXT_COLOR_WHITE);
            return new ReplResponse("Expected: <ID> [WHITE|BLACK]", State.POSTLOGIN, authToken, -1);
        }
    }

    public ReplResponse observerGame(String ... params){
        try{
            var gameList = server.listGames(authToken);
            int num = Integer.parseInt(params[0]);
            ChessGame game = gameList.games().get(num-1).game();
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            printGame(game, null);
            return new ReplResponse("Game: " + gameList.games().get(num-1).gameName(), State.POSTLOGIN, authToken, -1);
        } catch (Exception e){
            System.out.print(SET_TEXT_COLOR_WHITE);
            return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken, -1);
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
                """, State.POSTLOGIN, authToken, -1);
    }
}

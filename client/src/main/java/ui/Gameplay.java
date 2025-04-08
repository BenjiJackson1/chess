package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import serverfacade.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static ui.ChessBoardPrinter.*;
import static ui.EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gameplay implements Client{
    private final ServerFacade server;
    private final String authToken;
    private final int gameID;
    private WebSocketFacade ws;
    private final String teamColor;
    private final NotificationHandler notificationHandler;

    public Gameplay(String serverUrl, String authToken, int gameID, String teamColor, NotificationHandler notificationHandler){
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameID = gameID;
        this.teamColor = teamColor;
        this.notificationHandler = notificationHandler;
        try{
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.connect(authToken, gameID);
        } catch (ResponseException e){}
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "resign" -> resign();
                case "leave" -> leave();
                case "highlight" -> highlight(params);
                case "redraw" -> redraw();
                case "move" -> move(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.GAMEPLAY, authToken, gameID, teamColor);
        }
    }

    public ReplResponse resign(){
        try{
            ws.resign(authToken, gameID);
            return new ReplResponse("", State.GAMEPLAY, authToken, gameID, teamColor);
        } catch (Exception e){
            return new ReplResponse("Unable to resign.", State.GAMEPLAY, authToken, gameID, teamColor);
        }
    }

    public ReplResponse leave(){
        try{
            ws.leave(authToken, gameID);
            return new ReplResponse("You left the game.", State.POSTLOGIN, authToken, -1, null);
        } catch (Exception e){
            return new ReplResponse("Unable to leave the game.", State.GAMEPLAY, authToken, gameID, teamColor);
        }
    }

    public ReplResponse move(String ... params){
        if (params.length < 1 || params[0].length() != 2 || params[1].length() != 2) {
            return new ReplResponse("Expected: <START> <END>", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        char c = Character.toLowerCase(params[0].charAt(0));
        char r = params[0].charAt(1);
        char c1 = Character.toLowerCase(params[1].charAt(0));
        char r1 = params[1].charAt(1);
        if (c < 'a' || c > 'h' || r < '1' || r > '8' || c1 < 'a' || c1 > 'h' || r1 < '1' || r1 > '8') {
            return new ReplResponse("Not a valid move!", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        int row = Character.getNumericValue(params[0].charAt(1));
        int col = c - 'a' +1;
        int row1 = Character.getNumericValue(params[1].charAt(1));
        int col1 = c1 - 'a' +1;
        try{
            ws.move(authToken, gameID, new ChessMove(new ChessPosition(row,col), new ChessPosition(row1,col1), null));
        }catch (ResponseException e){

        }
        return new ReplResponse("", State.GAMEPLAY, authToken, gameID, teamColor);
    }

    public ReplResponse redraw(){
        try{
            var gameList = server.listGames(authToken);
            ChessGame game = gameList.games().get(gameID-1).game();
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            printGame(game, teamColor, new ArrayList<>());
            return new ReplResponse("Game: " + gameList.games().get(gameID-1).gameName(), State.GAMEPLAY,
                    authToken, gameID, teamColor);

        } catch (ResponseException e){
            return new ReplResponse("Unable to draw the board.", State.GAMEPLAY, authToken, gameID, teamColor);
        }

    }

    public ReplResponse highlight(String ... params){
        if (params.length < 1 || params[0].length() != 2) {
            return new ReplResponse("Expected: <PIECE> (ex, e4)", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        char c = Character.toLowerCase(params[0].charAt(0));
        char r = params[0].charAt(1);
        if (c < 'a' || c > 'h' || r < '1' || r > '8') {
            return new ReplResponse("Expected: <PIECE> (ex, e4)", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        int row = Character.getNumericValue(params[0].charAt(1));
        int col = c - 'a' +1;
        try{
            var gameList = server.listGames(authToken);
            ChessGame game = gameList.games().get(gameID-1).game();
            var gameMoves = game.validMoves(new ChessPosition(row, col));
            List<ChessPosition> highlightedMoves = new ArrayList<>();
            for (var gameMove: gameMoves) {
                highlightedMoves.add(gameMove.getEndPosition());
            }
            highlightedMoves.add(new ChessPosition(row, col));
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            printGame(game, teamColor, highlightedMoves);
            return new ReplResponse("Game: " + gameList.games().get(gameID-1).gameName(), State.GAMEPLAY,
                    authToken, gameID, teamColor);
        }catch (Exception e){}
        return new ReplResponse("Expected: <PIECE> (ex, e4)", State.GAMEPLAY, authToken, gameID, teamColor);
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - redraw - to redraw the board
                - highlight <PIECE> - to highlight all the legal moves
                - move <START> <END> <PROMOTE_PIECE?> - to move a piece
                - resign - forfeit the current game
                - leave - to leave the current game
                - help - with possible commands
                """, State.GAMEPLAY, authToken, gameID, teamColor);
    }
}

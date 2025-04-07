package ui;

import chess.ChessGame;
import chess.ChessPosition;
import serverfacade.ServerFacade;
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
    private final String teamColor;

    public Gameplay(String serverUrl, String authToken, int gameID, String teamColor){
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameID = gameID;
        this.teamColor = teamColor;
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> new ReplResponse("quit", State.POSTLOGIN, authToken, -1, null);
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.GAMEPLAY, authToken, gameID, teamColor);
        }
    }

    public ReplResponse highlight(String ... params){
        if (params.length < 1 || params[0].length() != 2) {
            return new ReplResponse("Expected: <PIECE> (ex, e4)", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        char file = Character.toLowerCase(params[0].charAt(0));
        char rankChar = params[0].charAt(1);
        if (file < 'a' || file > 'h' || rankChar < '1' || rankChar > '8') {
            return new ReplResponse("Expected: <PIECE> (ex, e4)", State.GAMEPLAY, authToken, gameID, teamColor);
        }
        int row = Character.getNumericValue(params[0].charAt(1));
        int col = file - 'a' +1;
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
                - move <START> <END> - to move a piece
                - resign - forfeit the current game
                - leave - to leave the current game
                - help - with possible commands
                """, State.GAMEPLAY, authToken, gameID, teamColor);
    }
}

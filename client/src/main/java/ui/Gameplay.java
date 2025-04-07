package ui;

import serverfacade.ServerFacade;

import java.util.Arrays;

public class Gameplay implements Client{
    private final ServerFacade server;
    private final String authToken;
    private final int gameID;

    public Gameplay(String serverUrl, String authToken, int gameID){
        server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> new ReplResponse("quit", State.POSTLOGIN, authToken, -1);
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.GAMEPLAY, authToken, gameID);
        }
    }

    public ReplResponse highlight(String ... params){
        return new ReplResponse("FIX MEE", State.GAMEPLAY, authToken, gameID);
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - redraw - to redraw the board
                - highlight <PIECE> - to highlight all the legal moves
                - move <START> <END> - to move a piece
                - resign - forfeit the current game
                - leave - to leave the current game
                - help - with possible commands
                """, State.GAMEPLAY, authToken, gameID);
    }
}

package ui;

import java.util.Arrays;

public class Gameplay implements Client{
    private final String authToken;

    public Gameplay(String authToken){
        this.authToken = authToken;
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> new ReplResponse("quit", State.POSTLOGIN, authToken);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.GAMEPLAY, authToken);
        }
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - redraw - to redraw the board
                - highlight <PIECE> - to highlight all the legal moves
                - move <START> <END> - to move a piece
                - resign - forfeit the current game
                - leave - to leave the current game
                - help - with possible commands
                """, State.GAMEPLAY, authToken);
    }
}

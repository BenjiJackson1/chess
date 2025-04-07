package ui;

import chess.ChessGame;

public record ReplResponse (String output, State newState, String authToken, int gameID, String teamColor){}
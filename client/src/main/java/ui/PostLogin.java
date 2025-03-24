package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LogoutRequest;
import serverfacade.ServerFacade;

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
                case "quit" -> new ReplResponse("quit", State.POSTLOGIN, authToken);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> joinGame(params);
                case "observe" -> observerGame(params);
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
            return new ReplResponse("Unable to list the games!", State.POSTLOGIN, authToken);
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
            return new ReplResponse("Game: " + gameList.games().get(num-1).gameName(), State.POSTLOGIN, authToken);
        } catch (Exception e){
            if (params.length == 2){
                System.out.print(SET_TEXT_COLOR_WHITE);
                try{
                    if (Integer.parseInt(params[0]) > numGames || Integer.parseInt(params[0]) < 0){
                        return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken);
                    }
                } catch (NumberFormatException ex){
                    return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken);
                }
                if (params[1].equalsIgnoreCase("white") || params[1].equalsIgnoreCase("black")){
                    return new ReplResponse("Chosen color is already taken!", State.POSTLOGIN, authToken);
                }
            }
            System.out.print(SET_TEXT_COLOR_WHITE);
            return new ReplResponse("Expected: <ID> [WHITE|BLACK]", State.POSTLOGIN, authToken);
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
            return new ReplResponse("Game: " + gameList.games().get(num-1).gameName(), State.POSTLOGIN, authToken);
        } catch (Exception e){
            System.out.print(SET_TEXT_COLOR_WHITE);
            return new ReplResponse("Not a valid game ID!", State.POSTLOGIN, authToken);
        }
    }

    private void printGame(ChessGame chessGame, String teamColor){
        if (teamColor == null || teamColor.equalsIgnoreCase("white")){
            System.out.print("   ");
            System.out.print(" a ");
            System.out.print(" b ");
            System.out.print(" c ");
            System.out.print(" d ");
            System.out.print(" e ");
            System.out.print(" f ");
            System.out.print(" g ");
            System.out.print(" h ");
            System.out.print("   \n");
            for (int j = 8; j > 0; j--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ j +" ");
                for (int i = 1; i < 9; i++) {
                    if ((i+j) % 2 == 0){
                        System.out.print(SET_BG_COLOR_BLUE);
                    }else{
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                    System.out.print(pieceGetter(chessGame.getBoard().getPiece(new ChessPosition(j,i))));
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ j +" \n");
            }
            System.out.print("   ");
            System.out.print(" a ");
            System.out.print(" b ");
            System.out.print(" c ");
            System.out.print(" d ");
            System.out.print(" e ");
            System.out.print(" f ");
            System.out.print(" g ");
            System.out.print(" h ");
            System.out.print("   \n");
        }
        else{
            System.out.print("   ");
            System.out.print(" h ");
            System.out.print(" g ");
            System.out.print(" f ");
            System.out.print(" e ");
            System.out.print(" d ");
            System.out.print(" c ");
            System.out.print(" b ");
            System.out.print(" a ");
            System.out.print("   \n");
            for (int j = 8; j > 0; j--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                int col = 9 - j;
                System.out.print(" "+ col +" ");
                for (int i = 1; i < 9; i++) {
                    if ((i+j) % 2 == 0){
                        System.out.print(SET_BG_COLOR_BLUE);
                    }else{
                        System.out.print(SET_BG_COLOR_WHITE);
                    }
                    System.out.print(pieceGetter(chessGame.getBoard().getPiece(new ChessPosition(9-j,9-i))));
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ col +" \n");
            }
            System.out.print("   ");
            System.out.print(" h ");
            System.out.print(" g ");
            System.out.print(" f ");
            System.out.print(" e ");
            System.out.print(" d ");
            System.out.print(" c ");
            System.out.print(" b ");
            System.out.print(" a ");
            System.out.print("   \n");
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private String pieceGetter(ChessPiece chessPiece){
        if (chessPiece == null){
            return EMPTY;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_ROOK;
            } return BLACK_ROOK;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_BISHOP;
            } return BLACK_BISHOP;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_PAWN;
            } return BLACK_PAWN;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.KING){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_KING;
            } return BLACK_KING;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_QUEEN;
            } return BLACK_QUEEN;
        }
        else{
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_KNIGHT;
            }
            return BLACK_KNIGHT;
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

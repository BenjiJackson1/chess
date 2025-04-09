package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import service.GameService;
import service.UserService;
import websocket.messages.*;
import websocket.commands.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections;
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(ConnectionManager connections, UserService userService, GameService gameService) {
        this.connections = connections;
        this.gameService = gameService;
        this.userService = userService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        AuthData authData = userService.getAuth(command.getAuthToken());
        if (authData.authToken() == null){
            connections.sendSession(session, new ErrorMessage("Error: unauthorized"));
        }
        else{
            connections.add(authData.username(), session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(authData.username(), session, command);
                case LEAVE -> leave(authData.username(), command);
                case MAKE_MOVE -> move(authData.username(), new Gson().fromJson(message, MakeMoveCommand.class));
                case RESIGN -> resign(authData.username(), command);
            }
        }
    }

    public void connect(String visitorName, Session session, UserGameCommand command) throws IOException {
        connections.add(visitorName, session);
        GameData gameData = gameService.getGame(command.getGameID());
        if (gameData.gameID() == -1){
            connections.send(visitorName, new ErrorMessage("Error: invalid game"));
        }
        else{
            connections.joinGame(command.getGameID(), visitorName);
            var message = String.format("%s is in the game.", visitorName);
            connections.broadcastToGame(command.getGameID(),visitorName, new NotificationMessage(message));
            var notification = new LoadGameMessage(gameData);
            connections.send(visitorName, notification);
        }

    }

    public void move(String visitorName, MakeMoveCommand command){
        try{
            GameData gameData = gameService.getGame(command.getGameID());
            if (gameData.game().isOver()){
                connections.send(visitorName, new ErrorMessage("Error: game is over"));
            }
            else if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) ||
                    gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                connections.send(visitorName, new ErrorMessage("Error: game is over"));
            }
            else if (gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE && !gameData.whiteUsername().equals(visitorName)){
                connections.send(visitorName, new ErrorMessage("Error: not your turn"));
            } else if (gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK && !gameData.blackUsername().equals(visitorName)) {
                connections.send(visitorName, new ErrorMessage("Error: not your turn"));
            } else{
                gameData.game().makeMove(command.getChessMove());
                gameService.makeMove(gameData.gameID(), gameData);
                connections.send(visitorName, new LoadGameMessage(gameData));
                connections.broadcastToGame(command.getGameID(),visitorName, new NotificationMessage(String.format("%s made a move.", visitorName)));
                connections.broadcastToGame(command.getGameID(),visitorName, new LoadGameMessage(gameData));
                if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                    gameData.game().setOver(true);
                    gameService.makeMove(gameData.gameID(), gameData);
                    connections.send(visitorName, new NotificationMessage("BLACK is in checkmate, WHITE wins!"));
                    connections.broadcastToGame(gameData.gameID() ,visitorName, new NotificationMessage("BLACK is in checkmate, WHITE wins!"));
                }
                else if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                    gameData.game().setOver(true);
                    gameService.makeMove(gameData.gameID(), gameData);
                    connections.send(visitorName, new NotificationMessage("WHITE is in checkmate, BLACK wins!"));
                    connections.broadcastToGame(gameData.gameID() ,visitorName, new NotificationMessage("WHITE is in checkmate, BLACK wins!"));
                }
                else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
                    connections.send(visitorName, new NotificationMessage("BLACK is in check."));
                    connections.broadcastToGame(gameData.gameID() ,visitorName, new NotificationMessage("BLACK is in check."));
                }
                else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
                    connections.send(visitorName, new NotificationMessage("WHITE is in check."));
                    connections.broadcastToGame(gameData.gameID() ,visitorName, new NotificationMessage("WHITE is in check."));
                }
            }
        } catch (Exception e){
            try{
                connections.send(visitorName, new ErrorMessage("Error: invalid move"));
            } catch (IOException x){}
        }
    }

    private void leave(String visitorName, UserGameCommand command) throws IOException {
        GameData gameData = gameService.getGame(command.getGameID());
        if (visitorName.equals(gameData.whiteUsername())){
            gameService.makeMove(gameData.gameID(), new GameData(gameData.gameID(), null, gameData.blackUsername(),
                    gameData.gameName(), gameData.game()));

        }
        if (visitorName.equals(gameData.blackUsername())){
            gameService.makeMove(gameData.gameID(), new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                    gameData.gameName(), gameData.game()));

        }
        connections.remove(visitorName);
        var message = String.format("%s left the game.", visitorName);
        var notification = new NotificationMessage(message);
        connections.send(visitorName, notification);
        connections.broadcastToGame(command.getGameID(), visitorName, notification);
    }

    public void resign(String visitorName, UserGameCommand command) throws IOException{
        GameData gameData = gameService.getGame(command.getGameID());
        if (gameData.game().isOver()) {
            connections.send(visitorName, new ErrorMessage("Error: game is already over"));
            return;
        }
        if (!visitorName.equals(gameData.whiteUsername()) && !visitorName.equals(gameData.blackUsername())) {
            connections.send(visitorName, new ErrorMessage("Error: only players can resign"));
            return;
        }
        gameData.game().setOver(true);
        gameService.makeMove(command.getGameID(), gameData);
        connections.setGameOver(command.getGameID());
        connections.send(visitorName, new NotificationMessage("You resigned from the match. Loser."));
        connections.broadcastToGame(command.getGameID() ,visitorName, new NotificationMessage(String.format("%s resigned.", visitorName)));
    }
}
package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
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
            connections.send(session.getRemote().toString(), new ErrorMessage("Error: unauthorized"));
        }
        else{
            connections.add(authData.username(), session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(authData.username(), session, command.getGameID());
                case LEAVE -> leave(authData.username());
            }
        }
    }

    public void connect(String visitorName, Session session, int gameID) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the game.", visitorName);
        connections.broadcast(visitorName, new NotificationMessage(message));
        var notification = new LoadGameMessage(new GameData(1, null, null, "test", new ChessGame()));
        connections.send(visitorName, notification);
    }

    private void leave(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the game.", visitorName);
        var notification = new NotificationMessage(message);
        connections.send(visitorName, notification);
    }
}
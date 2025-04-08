package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.ResponseException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.net.URI;

import javax.websocket.*;
import java.io.IOException;
import java.net.URISyntaxException;

import ui.PostLogin;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
                    String type = jsonObject.get("serverMessageType").getAsString();
                    ServerMessage notification;
                    if (type.equals("NOTIFICATION")){
                        notification = gson.fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    }
                    else if (type.equals("ERROR")){
                        notification = gson.fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(notification);
                    }else if (type.equals("LOAD_GAME")){
                        notification = gson.fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void move(String authToken, int gameID, ChessMove chessMove) throws ResponseException{
        try{
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, chessMove);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException{
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        }catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void connect(String authToken, int gameID) throws ResponseException{
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e){
            throw new ResponseException(500, e.getMessage());
        }
    }
}

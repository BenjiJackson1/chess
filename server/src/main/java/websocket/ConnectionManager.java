package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap.KeySetView<String, Boolean>> gamePlayers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Boolean> gameResigned = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void send(String visitorName, ServerMessage message) throws IOException {
        Connection connection = connections.get(visitorName);
        if (connection != null && connection.session.isOpen()) {
            connection.send(message.toString());
        } else {
            connections.remove(visitorName);
        }
    }

    public void sendSession(Session session, ServerMessage message) throws IOException {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString(message.toString());
        }
    }

    public void joinGame(int gameID, String visitorName) {
        gamePlayers.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(visitorName);
        gameResigned.putIfAbsent(gameID, false);
    }

    public void setGameOver(int gameID) {
        gameResigned.put(gameID, true);
    }

    public void broadcastToGame(int gameID, String excludeVisitorName, ServerMessage message) throws IOException {
        var players = gamePlayers.get(gameID);
        if (players == null) {
            return;
        }

        var removeList = new ArrayList<String>();
        for (var player : players) {
            if (!player.equals(excludeVisitorName)) {
                var conn = connections.get(player);
                if (conn != null && conn.session.isOpen()) {
                    conn.send(message.toString());
                } else {
                    removeList.add(player);
                }
            }
        }
        for (var player : removeList) {
            connections.remove(player);
            players.remove(player);
        }
    }

}
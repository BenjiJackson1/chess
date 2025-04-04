package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

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
}
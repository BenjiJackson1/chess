package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public boolean isOver;

    public Connection(String visitorName, Session session) {
        this.visitorName = visitorName;
        this.session = session;
        this.isOver = false;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public void setConnectionState(boolean over) {
        isOver = over;
    }
}
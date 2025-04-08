package ui;

import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import static ui.ChessBoardPrinter.*;

import java.util.ArrayList;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final PreLogin preLoginClient;
    private Client currentClient;
    private String url;
    private State state;
    private String teamColor;

    public Repl(String serverUrl){
        url = serverUrl;
        preLoginClient = new PreLogin(serverUrl);
        currentClient = preLoginClient;
        state = State.PRELOGIN;
        teamColor = null;
    }

    public void run() {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("♕ 240 Chess Client: ");
        System.out.print(preLoginClient.help().output());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                ReplResponse replResponse = currentClient.eval(line);
                result = replResponse.output();
                if (replResponse.newState() == State.PRELOGIN){
                    currentClient = preLoginClient;
                    state = State.PRELOGIN;
                } else if (replResponse.newState() == State.POSTLOGIN) {
                    currentClient = new PostLogin(url, replResponse.authToken());
                    state = State.POSTLOGIN;
                } else if (replResponse.newState() == State.GAMEPLAY){
                    if (state != State.GAMEPLAY) {
                        currentClient = new Gameplay(url, replResponse.authToken(),
                                replResponse.gameID(), replResponse.teamColor(), this);
                        teamColor = replResponse.teamColor();
                    }
                    state = State.GAMEPLAY;
                }
                System.out.print(SET_BG_COLOR_MAGENTA + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage notification) {
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            NotificationMessage notificationMessage = (NotificationMessage) notification;
            System.out.println(SET_BG_COLOR_MAGENTA);
            System.out.println(SET_TEXT_COLOR_WHITE + notificationMessage.getMessage());
        }
        else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ErrorMessage notificationMessage = (ErrorMessage) notification;
            System.out.println(SET_BG_COLOR_MAGENTA);
            System.out.println(SET_TEXT_COLOR_WHITE + notificationMessage.getErrorMessage());
        }
        else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadGameMessage notificationMessage = (LoadGameMessage) notification;
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.println("\n");
            printGame(notificationMessage.getGame().game(), teamColor, new ArrayList<>());
        }else{
            System.out.println(SET_TEXT_COLOR_RED + notification);
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
        printPrompt();
    }

    private void printPrompt() {
        String termCommand = "";
        if (state.name() == State.PRELOGIN.name()){
            termCommand = "[LOGGED_OUT] ";
        }
        if (state.name() == State.POSTLOGIN.name()){
            termCommand = "[LOGGED_IN] ";
        }
        if (state.name() == State.GAMEPLAY.name()){
            termCommand = "[IN_GAME] ";
        }
        System.out.print("\n" + RESET_BG_COLOR + termCommand +">>> " + SET_BG_COLOR_GREEN);
    }
}

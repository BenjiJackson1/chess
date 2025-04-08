package ui;

import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final PreLogin preLoginClient;
    private Client currentClient;
    private String url;
    private State state;

    public Repl(String serverUrl){
        url = serverUrl;
        preLoginClient = new PreLogin(serverUrl);
        currentClient = preLoginClient;
        state = State.PRELOGIN;
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
                    currentClient = new Gameplay(url, replResponse.authToken(),
                            replResponse.gameID(), replResponse.teamColor(), this);
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
        System.out.println(SET_TEXT_COLOR_RED + notification.toString());
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

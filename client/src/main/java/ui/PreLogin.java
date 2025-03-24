package ui;

import model.request.LoginRequest;
import model.request.RegisterRequest;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class PreLogin implements Client {
    private final ServerFacade server;
    private final String serverUrl;

    public PreLogin(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public ReplResponse eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> new ReplResponse("quit", State.PRELOGIN, null);
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.PRELOGIN, null);
        }
    }

    public ReplResponse login(String ... params){
        try{
            var response = server.login(new LoginRequest(params[0], params[1]));
            return new ReplResponse("You signed in!", State.POSTLOGIN, response.authToken());
        } catch (Exception e){
            return new ReplResponse("Expected: <USERNAME> <PASSWORD>", State.PRELOGIN, null);
        }
    }

    public ReplResponse register(String ... params){
        try{
            var response = server.register(new RegisterRequest(params[0], params[1], params[2]));
            return new ReplResponse("You registered!", State.POSTLOGIN, response.authToken());
        } catch (Exception e){
            return new ReplResponse("Expected: <USERNAME> <PASSWORD> <EMAIL>", State.PRELOGIN, null);
        }
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands
                """, State.PRELOGIN, null);
    }
}


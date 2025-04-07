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
                case "quit" -> new ReplResponse("quit", State.PRELOGIN, null, -1, null);
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        } catch (Exception e) {
            return new ReplResponse(e.getMessage(), State.PRELOGIN, null, -1, null);
        }
    }

    public ReplResponse login(String ... params){
        try{
            var response = server.login(new LoginRequest(params[0], params[1]));
            return new ReplResponse("You signed in!", State.POSTLOGIN, response.authToken(), -1, null);
        } catch (Exception e){
            if (params.length == 2){
                return new ReplResponse("Incorrect username and password!", State.PRELOGIN, null, -1, null);
            }
            return new ReplResponse("Expected: <USERNAME> <PASSWORD>", State.PRELOGIN, null, -1, null);
        }
    }

    public ReplResponse register(String ... params){
        try{
            var response = server.register(new RegisterRequest(params[0], params[1], params[2]));
            return new ReplResponse("You registered!", State.POSTLOGIN, response.authToken(), -1, null);
        } catch (Exception e){
            if (params.length == 3){
                return new ReplResponse("A user with this username already exists!", State.PRELOGIN, null, -1, null);
            }
            return new ReplResponse("Expected: <USERNAME> <PASSWORD> <EMAIL>", State.PRELOGIN, null, -1, null);
        }
    }

    public ReplResponse help() {
        return new ReplResponse("""
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands
                """, State.PRELOGIN, null, -1, null);
    }
}


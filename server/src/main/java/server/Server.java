package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.GameData;
import model.request.*;
import model.result.*;
import model.result.RegisterResult;
import service.GameService;
import service.UserService;
import websocket.*;
import spark.*;


public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final WebSocketHandler webSocketHandler = new WebSocketHandler(connectionManager, userService, gameService);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clear);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        var user = new Gson().fromJson(req.body(), UserData.class);
        RegisterResult thisUser = userService.register(new RegisterRequest(user.username(), user.password(), user.email()));
        if (thisUser.message() != null && thisUser.message().equals("Error: already taken")){
            res.status(403);
            return new Gson().toJson(thisUser);
        }
        if (thisUser.message() != null && thisUser.message().equals("Error: bad request")){
            res.status(400);
            return new Gson().toJson(thisUser);
        }
        return new Gson().toJson(thisUser);
    }

    private Object login(Request req, Response res){
        var user = new Gson().fromJson(req.body(), UserData.class);
        LoginResult loginUser = userService.login(new LoginRequest(user.username(), user.password()));
        if (loginUser.message() != null && loginUser.message().equals("Error: unauthorized")){
            res.status(401);
            return new Gson().toJson(loginUser);
        }
        return new Gson().toJson(loginUser);
    }

    private Object logout(Request req, Response res){
        var auth = req.headers("Authorization");
        LogoutResult logoutResult = userService.logout(new LogoutRequest(auth));
        if (logoutResult.message() != null && logoutResult.message().equals("Error: unauthorized")){
            res.status(401);
            return new Gson().toJson(logoutResult);
        }
        return new Gson().toJson(logoutResult);
    }

    private Object clear(Request req, Response res){
        userService.clear();
        gameService.clear();
        return new Gson().toJson(new LogoutResult(null));
    }

    private Object createGame(Request req, Response res){
        var auth = req.headers("Authorization");
        AuthData authorized = userService.getAuth(auth);
        if (authorized.authToken() == null){
            res.status(401);
            return new Gson().toJson(new CreateGameResult(null, "Error: unauthorized"));
        }
        var game = new Gson().fromJson(req.body(), GameData.class);
        if (game.gameName() == null){
            res.status(400);
            return new Gson().toJson(new CreateGameResult(null, "Error: bad request"));
        }
        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(game.gameName()));
        return new Gson().toJson(createGameResult);
    }

    private Object joinGame(Request req, Response res){
        var auth = req.headers("Authorization");
        AuthData authorized = userService.getAuth(auth);
        if (authorized.authToken() == null){
            res.status(401);
            return new Gson().toJson(new CreateGameResult(null, "Error: unauthorized"));
        }
        var game = new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult joinGameResult = gameService.joinGame(new JoinGameRequest(game.playerColor(), game.gameID()),
                authorized.username());
        if (joinGameResult.message() != null && joinGameResult.message().equals("Error: bad request")){
            res.status(400);
            return new Gson().toJson(joinGameResult);
        }
        if (joinGameResult.message() != null && joinGameResult.message().equals("Error: already taken")){
            res.status(403);
            return new Gson().toJson(joinGameResult);
        }
        return new Gson().toJson(joinGameResult);
    }

    private Object listGames(Request req, Response res){
        var auth = req.headers("Authorization");
        AuthData authorized = userService.getAuth(auth);
        if (authorized.authToken() == null){
            res.status(401);
            return new Gson().toJson(new ListGamesResult(null, "Error: unauthorized"));
        }
        ListGamesResult listGamesResult = gameService.listGames();
        return new Gson().toJson(listGamesResult);
    }
}

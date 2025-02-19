package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService;

    public Server() {
        this.userService = new UserService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/db", this::clear);
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
        if (thisUser.message() != null && thisUser.message() == "Error: already taken"){
            res.status(403);
            return new Gson().toJson(thisUser);
        }
        if (thisUser.message() != null && thisUser.message() == "Error: bad request"){
            res.status(400);
            return new Gson().toJson(thisUser);
        }
        return new Gson().toJson(thisUser);
    }

    private Object login(Request req, Response res){
        var user = new Gson().fromJson(req.body(), UserData.class);
        LoginResult loginUser = userService.login(new LoginRequest(user.username(), user.password()));
        if (loginUser.message() != null && loginUser.message() == "Error: unauthorized"){
            res.status(401);
            return new Gson().toJson(loginUser);
        }
        return new Gson().toJson(loginUser);
    }

    private Object clear(Request req, Response res){
        userService.clear();
        return new Gson().toJson(null);
    }
}

package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
import model.request.RegisterRequest;
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
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        RegisterResult thisUser = userService.register(new RegisterRequest(user.username(), user.password(), user.email()));
        return new Gson().toJson(thisUser);
    }
}

package ui;

public interface Client {

    ReplResponse eval(String input);

    ReplResponse help();
}

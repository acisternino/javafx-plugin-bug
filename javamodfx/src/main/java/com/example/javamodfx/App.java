package com.example.javamodfx;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        var javaVersion = System.getProperty("java.version");
        var javafxVersion = System.getProperty("javafx.version");

        var l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(l), 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

        System.out.println("Program args:");
        var args = getParameters();
        List<String> rawArgs = args.getRaw();
        for (String arg : rawArgs) {
            System.out.println(arg);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

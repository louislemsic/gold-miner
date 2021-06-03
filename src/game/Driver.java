package game;

import javafx.application.Application;
import javafx.stage.Stage;
import game.controller.MainController;

/**
 * This class is just created to be the kicker of the program. It contains the public static void main and calls launch(args)
 * @author James Lemsic
 * @author Jordan Sibug
 */
public class Driver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the whole program to create a GUI. Creates the ControllerMain also.
     * @param primaryStage the stage (JavaFX)
     * @throws Exception throws the exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        new MainController(primaryStage);
    }
}

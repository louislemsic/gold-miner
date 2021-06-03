package game.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import game.model.*;

/**
 * The Main Controller of the whole game. This Controller is also responsible in creating a new GoldMiner game.
 * Every Controller is dependent to the leadership of the main controller. It contains the file locations and the
 * function changeScene where it allows the Stage to change scenes.
 * @author James Lemsic
 * @author Jordan Sibug
 * @version 1.0
 */
public class MainController {

    /** The Title of the Game */
    public static final String TITLE = "Gold Miner by Lemsic & Sibug";

    /** Value that corresponds to the Main Menu */
    public static final int PAGE_MAIN = 1;

    /** Value that corresponds to the Initial Configuration of the Game */
    public static final int PAGE_MINER = 2;

    /** Value that corresponds to the Board of the Game */
    public static final int PAGE_SMART = 3;
    public static final int PAGE_RANDOM = 4;

    /**
     * Value that corresponds to the Credits of the Game
     */
    public static final int CREDITS = 5;

    /** The Primary Stage of the Game */
    private final Stage primaryStage;
    /** The Game itself */
    private GoldMiner game;

    /**
     * From the driver, it passes the stage to this controller. This constructor also sets the stage, its title and icon.
     * @param stage the stage from the Driver.
     * @throws IOException throws the exception
     */
    public MainController(Stage stage) throws IOException {

        primaryStage = stage;
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/game/view/resources/icon.png")));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        changeScene(PAGE_MAIN);
    }

    /**
     * One of the most important functions. This function allows the changing of scenes of the stage. It receives a parameter of integer
     * that corresponds to the indicated final attributes above.
     * @param view the number passed by the controllers to indicate what scene to transfer to.
     * @throws IOException throws the exception
     */
    public void changeScene(int view) throws IOException {

        FXMLLoader loader = new FXMLLoader();

        switch(view){
            case PAGE_MAIN : loader = new FXMLLoader(getClass().getResource("/game/view/MainMenu.fxml"));
                             game = new GoldMiner();
                break;
            case PAGE_MINER : loader = new FXMLLoader(getClass().getResource("/game/view/MinerMenu.fxml"));
                break;
            case PAGE_SMART : loader = new FXMLLoader(getClass().getResource("/game/view/SmartBoard.fxml"));
                break;
            case PAGE_RANDOM : loader = new FXMLLoader(getClass().getResource("/game/view/RandomBoard.fxml"));
                break;
            case CREDITS: loader = new FXMLLoader(getClass().getResource("/game/view/Credits.fxml"));
                break;
        }

        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setMainController(this);

        primaryStage.setScene(new Scene(root));

        switch(view){
            case PAGE_MAIN :
            case PAGE_SMART :
            case PAGE_RANDOM :
                primaryStage.centerOnScreen();
                break;
        }

        primaryStage.show();
    }

    /**
     * Allows every controller to get the primary stage.
     * @return primaryStage the stage of the GUI
     */
    public Stage getStage(){
        return primaryStage;
    }

    /**
     * Allows every controller to get the current game.
     * @return game The GoldMiner being played
     */
    public GoldMiner getGame(){
        return game;
    }
}

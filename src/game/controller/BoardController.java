package game.controller;

import game.model.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;


public class BoardController extends Controller{

    /**
     * Moves available for the miner.
     */
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_UP = 3;
    public static final int MOVE_DOWN = 4;
    public static final int ROTATE_RIGHT = 5;
    public static final int SCAN = 6;

    @FXML
    private Label movesLabel;

    @FXML
    private Label scansLabel;

    @FXML
    private Label rotationsLabel;

    @FXML
    private Button loadButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button skipButton;

    @FXML
    private StackPane stackPane;

    private GridPane board;

    private ImageView imageView;

    private Pair<Integer, Integer> coordinates;

    private ArrayList<Integer> actions;
    private int actionIndex;

    private Timeline timeline;

    private char minerFace;

    public BoardController(){
        timeline = new Timeline();
        actions = new ArrayList<>();
        actionIndex = 0;
        minerFace = 'E';
    }

    @FXML
    public void exitButtonAction() throws IOException {
        timeline.stop();
        controller.changeScene(MainController.PAGE_MAIN);
    }

    public void changeStats(int index){

        if(index == 1) {
            movesLabel.setText(Integer.toString(Integer.parseInt(movesLabel.getText()) + 1));
        }
        else if (index == 2){
            scansLabel.setText(Integer.toString(Integer.parseInt(scansLabel.getText()) + 1));
        }
        else if (index == 3) {
            rotationsLabel.setText(Integer.toString(Integer.parseInt(rotationsLabel.getText()) + 1));
        }
    }

    @FXML
    public void skipButtonAction(){

        timeline.stop();

        int x = controller.getGame().getFinalCoordinates().getKey();
        int y = controller.getGame().getFinalCoordinates().getValue();

        board.getChildren().remove(imageView);
        changeDirection('E');
        board.add(imageView, x, y);

        finalReport();
    }

    /**
     * The Action being done when the button "Start" is pressed
     */
    @FXML
    public void initBoard() {

        loadButton.setDisable(true);
        loadButton.setVisible(false);

        int boardSize = controller.getGame().getBoardSize();
        board = createGrid(boardSize, 100);

        board.setGridLinesVisible(true);
//        board.getStyleClass().add("GridLines");

        load(); //load the elements

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(board);

        stackPane.getChildren().add(scrollPane);
        scrollPane.setStyle("-fx-font-size: 18px;");
    }

    /**
     * loads the necessary elements in the GUI.
     */
    public void load() {
        imageView = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/avatar_E.gif")));
        board.add(imageView, 0, 0);
        coordinates = new Pair<>(0, 0);

        addGold(controller.getGame().getBoard().getGold().getPosX(), controller.getGame().getBoard().getGold().getPosY());
        ArrayList<Cell> beacons = controller.getGame().getBoard().getBeacons();
        ArrayList<Cell> pits = controller.getGame().getBoard().getPits();

        int i;

        for(i = 0; i < pits.size(); i++){
            addPit(pits.get(i).getPosX(), pits.get(i).getPosY());
        }

        for(i = 0; i < beacons.size(); i++){
            addBeacon(beacons.get(i).getPosX(), beacons.get(i).getPosY());
        }

        actions = controller.getGame().calculate(); //calculate it

        if (actions.size() != 0) { //error control
            timeline = new Timeline(new KeyFrame(Duration.millis(300), e -> moveMiner(actions.get(actionIndex))));
            timeline.setCycleCount(actions.size());

            skipButton.setDisable(false);
        }
        else {
            System.out.println("Internal Error: There are no actions");
        }

        timeline.play();
        exitButton.setDisable(false);
        exitButton.setVisible(true);
        skipButton.setVisible(true);
        timeline.setOnFinished(e -> finalReport());
    }

    /**
     * The method that is responsible for the pop-up to appear.
     */
    private void finalReport(){
        skipButton.setDisable(true);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.initStyle(StageStyle.UTILITY);
        ArrayList<Integer> stats = controller.getGame().getStats();

        if (controller.getGame().getImpoExit()){
            info.setTitle("MINER IS CONFUSED");
            info.setHeaderText("There is no impossible way to the gold.");
        }
        else if (controller.getGame().getWinResult()){
            info.setTitle("WE HAVE FOUND GOLD");
            info.setHeaderText("All of it was honest and hard work.");
        }
        else {
            info.setTitle("MINER FELL IN A PIT");
            info.setHeaderText("Accidents happen.");
        }
        info.setContentText("Total Moves: " + stats.get(0) +
                            "\nTotal Scans: " + stats.get(1) +
                            "\nTotal Rotations: " + stats.get(2));

        movesLabel.setText(Integer.toString(stats.get(0)));
        scansLabel.setText(Integer.toString(stats.get(1)));
        rotationsLabel.setText(Integer.toString(stats.get(2)));

        info.show();
    }

    /**
     * the method responsible in creating the grid lines.
     * @param boardSize the NxN integer number
     * @param size the space of each square in the grid
     * @return gridPane a GridPane ready to be used for the game.
     */
    private GridPane createGrid(int boardSize, int size){

        int i;
        GridPane gridPane = new GridPane();

        for (i = 0; i < boardSize; i++){
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        }

        for (i = 0; i < boardSize; i++){
            gridPane.getRowConstraints().add(new RowConstraints(size));
        }

        return gridPane;
    }

    private void addPit(int x, int y){
        board.add(new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/p.png"))), x, y);
    }

    private void addBeacon(int x, int y){
        board.add(new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/b.png"))), x, y);
    }

    private void addGold(int x, int y){
        board.add(new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/g.png"))), x, y);
    }

    /**
     * all of the methods below are for animations.
     *
     */
    public void changeDirection(char direction){

        switch (direction){
            case 'N': imageView = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/avatar_N.gif")));
                minerFace = 'N';
                break;
            case 'S': imageView = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/avatar_S.gif")));
                minerFace = 'S';
                break;
            case 'W': imageView = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/avatar_W.gif")));
                minerFace = 'W';
                break;
            case 'E': imageView = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/avatar_E.gif")));
                minerFace = 'E';
                break;
        }
    }

    public void moveMiner(int directionIndex){

        board.getChildren().remove(imageView);

        int x = coordinates.getKey();
        int y = coordinates.getValue();

        switch (directionIndex){
            case SCAN: changeStats(2);
                break;
            case MOVE_UP: y = y - 1; changeStats(1);
                break;
            case MOVE_DOWN: y = y + 1; changeStats(1);
                break;
            case MOVE_LEFT: x = x - 1; changeStats(1);
                break;
            case MOVE_RIGHT: x = x + 1; changeStats(1);
                break;
            case ROTATE_RIGHT:
                if (minerFace == 'N') changeDirection('E');
                else if (minerFace == 'W') changeDirection('N');
                else if (minerFace == 'S') changeDirection('W');
                else if (minerFace == 'E') changeDirection('S');

                changeStats(3);
                break;
        }

        board.add(imageView, x, y);
        coordinates = new Pair<>(x, y);
        actionIndex++;
    }

    public void initialize(){
        exitButton.setDisable(true);
        exitButton.setVisible(false);
        skipButton.setDisable(true);
        skipButton.setVisible(false);
    }
}

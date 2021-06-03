package game.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;


/**
 * The Controller for the Main Menu, this checks also if the player clicked Start Game, New Player, or Exit Button
 */
public class MainMenuController extends Controller{

    @FXML
    private TextField gridTextBox;

    /**
     * The Constructor is made to create the controller for the first page.
     */
    public MainMenuController(){
    }

    @FXML
    public void creditsButtonAction() throws IOException{
        controller.changeScene(MainController.CREDITS);
    }

    /**
     * The Function that corresponds if the Exit button is pressed. It should close the whole program.
     * @param e The event mouse click
     */
    @FXML
    public void exitButtonAction(Event e){
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    /**
     * The function that corresponds if the Start Game is pressed.
     * This checks if there enough players in the game, if there are, it should ask the main controller to change scenes.
     */
    @FXML
    public void updateGoldButton() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("UPDATE GOLD LOCATION");
        dialog.setHeaderText("To where will the treasure be located?");
        dialog.initStyle(StageStyle.UTILITY);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/gold.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);

        dialog.setGraphic(icon);

        ButtonType addLocationButton = new ButtonType("Add Location", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addLocationButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField x_coord = new TextField();
        x_coord.setPromptText("X");
        TextField y_coord = new TextField();
        y_coord.setPromptText("Y");

        grid.add(new Label("X-Coordinate:"), 0, 0);
        grid.add(x_coord, 1, 0);
        grid.add(new Label("Y-Coordinate:"), 0, 1);
        grid.add(y_coord, 1, 1);

        Node nodeButton = dialog.getDialogPane().lookupButton(addLocationButton);
        nodeButton.setDisable(true);

        y_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!x_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        x_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!y_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(x_coord::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addLocationButton) {
                return new Pair<>(x_coord.getText(), y_coord.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(goldCoordinates -> {
            boolean judgment = controller.getGame().setGold(Integer.parseInt(goldCoordinates.getKey()), Integer.parseInt(goldCoordinates.getValue()));

            if (judgment){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.initStyle(StageStyle.UTILITY);
                info.setTitle("UPDATE GOLD LOCATION");
                info.setHeaderText("Success!");
                info.setContentText("There will be a gold at coordinate: (" + goldCoordinates.getKey() + ", " + goldCoordinates.getValue() +").\n" + "Please take note due to this, all beacons will reset.");
                info.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid Input");
                alert.setContentText("The provided coordinates are either out of bounds or occupied with another element.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void startGameButton() throws IOException {
        controller.changeScene(MainController.PAGE_MINER);
    }

    @FXML
    public void addPitButton() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("ADD PIT LOCATION");
        dialog.setHeaderText("What coordinate the danger lies upon?");
        dialog.initStyle(StageStyle.UTILITY);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/pit.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);

        dialog.setGraphic(icon);

        ButtonType addLocationButton = new ButtonType("Add Location", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addLocationButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField x_coord = new TextField();
        x_coord.setPromptText("X");
        TextField y_coord = new TextField();
        y_coord.setPromptText("Y");

        grid.add(new Label("X-Coordinate:"), 0, 0);
        grid.add(x_coord, 1, 0);
        grid.add(new Label("Y-Coordinate:"), 0, 1);
        grid.add(y_coord, 1, 1);

        Node nodeButton = dialog.getDialogPane().lookupButton(addLocationButton);
        nodeButton.setDisable(true);

        y_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!x_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        x_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!y_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(x_coord::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addLocationButton) {
                return new Pair<>(x_coord.getText(), y_coord.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pitCoordinates -> {
            boolean judgment = controller.getGame().addPit(Integer.parseInt(pitCoordinates.getKey()), Integer.parseInt(pitCoordinates.getValue()));

            if (judgment){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.initStyle(StageStyle.UTILITY);
                info.setTitle("ADD PIT LOCATION");
                info.setHeaderText("Success!");
                info.setContentText("There will be a pit at coordinate: (" + pitCoordinates.getKey() + ", " + pitCoordinates.getValue() +").");
                info.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid Input");
                alert.setContentText("The provided coordinates are either out of bounds or occupied with another element.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void addBeaconButton() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("ADD BEACON LOCATION");
        dialog.setHeaderText("Where is the guiding light?");
        dialog.initStyle(StageStyle.UTILITY);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/game/view/resources/beacon.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);

        dialog.setGraphic(icon);

        ButtonType addLocationButton = new ButtonType("Add Location", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addLocationButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField x_coord = new TextField();
        x_coord.setPromptText("X");
        TextField y_coord = new TextField();
        y_coord.setPromptText("Y");

        grid.add(new Label("X-Coordinate:"), 0, 0);
        grid.add(x_coord, 1, 0);
        grid.add(new Label("Y-Coordinate:"), 0, 1);
        grid.add(y_coord, 1, 1);

        Node nodeButton = dialog.getDialogPane().lookupButton(addLocationButton);
        nodeButton.setDisable(true);

        y_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!x_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        x_coord.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!y_coord.getText().isEmpty())
                nodeButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(x_coord::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addLocationButton) {
                return new Pair<>(x_coord.getText(), y_coord.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(beaconCoordinates -> {
            boolean judgment = controller.getGame().addBeacon(Integer.parseInt(beaconCoordinates.getKey()), Integer.parseInt(beaconCoordinates.getValue()));

            if (judgment){
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.initStyle(StageStyle.UTILITY);
                info.setTitle("ADD BEACON LOCATION");
                info.setHeaderText("Success!");
                info.setContentText("There will be a beacon at coordinate: (" + beaconCoordinates.getKey() + ", " + beaconCoordinates.getValue() +").");
                info.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid Input");
                alert.setContentText("The provided coordinates are either out of bounds or occupied with another element.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void resetButton(){

        controller.getGame().reset();
        gridTextBox.setText("8");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Reset Stats");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("All inputs have now been erased.");
        alert.showAndWait();
    }

    /**
     * Updates the size of the Grid.
     */
    @FXML
    public void updateGridButton(){

        int grid = Integer.parseInt(gridTextBox.getText());


        if (grid < 8 || grid > 20){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);

            alert.setTitle("GRID SIZE");
            alert.setContentText("Number entered is invalid. [8-20 only].");
            gridTextBox.setText("");

            alert.showAndWait();
        }
        else {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.initStyle(StageStyle.UTILITY);

            controller.getGame().setGridSize(grid);
            info.setHeaderText("Success!");
            info.setTitle("GRID SIZE");
            info.setContentText("Grid Size is now updated to " + grid + "x" + grid + ". Because of this, all inputs have been erased.");

            controller.getGame().setGridSize(grid);

            info.showAndWait();
        }

    }

    public void initialize(){
        gridTextBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                gridTextBox.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}

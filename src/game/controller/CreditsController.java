package game.controller;

import javafx.fxml.FXML;

import java.io.IOException;

public class CreditsController extends Controller{

    @FXML
    public void goBackAction() throws IOException {
        controller.changeScene(MainController.PAGE_MAIN);
    }
}

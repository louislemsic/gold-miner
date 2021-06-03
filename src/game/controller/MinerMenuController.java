package game.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class MinerMenuController extends Controller {

    @FXML
    private AnchorPane randomMiner;

    @FXML
    private AnchorPane smartMiner;

    @FXML
    public void randomMinerAction() throws IOException {
        controller.getGame().setRationality(false);   //miner is not smart
        controller.changeScene(MainController.PAGE_RANDOM);
    }

    @FXML
    public void smartMinerAction() throws IOException {
        controller.getGame().setRationality(true);   //miner is smart
        controller.changeScene(MainController.PAGE_SMART);
    }

    @FXML
    public void mouseInSmart(){
        smartMiner.setBorder(new Border(new BorderStroke(Color.BROWN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    @FXML
    public void mouseOutSmart(){
        smartMiner.setBorder(null);
    }

    @FXML
    public void mouseInRandom(){
        randomMiner.setBorder(new Border(new BorderStroke(Color.BROWN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    @FXML
    public void mouseOutRandom(){
        randomMiner.setBorder(null);
    }
}

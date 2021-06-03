module MC01 {
    requires javafx.fxml;
    requires javafx.controls;

    opens game;
    opens game.model;
    opens game.controller;
    opens game.view;
}
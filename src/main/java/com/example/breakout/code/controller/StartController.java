package com.example.breakout.code.controller;

import com.example.breakout.code.model.GameModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controls the start screen UI interaction.
 * Changes the theme based on the theme buttons and loads the Game scene when the Start button is pressed.
 */
public class StartController implements Initializable {
    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    /**
     * Themes for the game
     */
    public enum Theme {T1, T2, T3}

    private Theme currentTheme;
    private HashMap<Theme, String> themeStyleMap;

    private static final String gameScenePath = "/fxmls/game_scene.fxml";
    private static final String startMusicPath = "/audio/start_music.mp3";
    private MediaPlayer player;

    @FXML
    private Pane root;

    /**
     * Default constructor, just initializes the styleMap and sets the default theme.
     */
    public StartController() {
        themeStyleMap = new HashMap<>();
        themeStyleMap.put(Theme.T1, getClass().getResource("/css/style1.css").toExternalForm());
        themeStyleMap.put(Theme.T2, getClass().getResource("/css/style2.css").toExternalForm());
        themeStyleMap.put(Theme.T3, getClass().getResource("/css/style3.css").toExternalForm());
        currentTheme = Theme.T1;
    }

    /**
     * Loads the game scene.
     * @param event generated by the FXML node which calls this function
     * @throws IOException in case the loader fails to load the game scene
     */
    @FXML
    private void loadGameScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(gameScenePath));

        Rectangle drawArea = new Rectangle(0, 0, DEF_WIDTH, DEF_HEIGHT);
        int brickCount = 30;
        int lineCount = 3;
        double brickDimensionRatio = 6 / 2;
        Point2D ballPos = new Point2D(300, 430);
        GameModel gameModel = new GameModel(drawArea, brickCount, lineCount, brickDimensionRatio, ballPos);

        Parent root = loader.load();
        root.getStylesheets().add(themeStyleMap.get(currentTheme));
        GameController controller = loader.getController();
        controller.setGameModel(gameModel);
        controller.start();

        player.stop();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Media sound = new Media(Objects.requireNonNull(getClass().getResource(startMusicPath)).toExternalForm());
        setTheme(currentTheme);
        player = new MediaPlayer(sound);
        player.play();
    }

    /**
     * Called when theme1 button is clicked
     */
    @FXML
    private void onClickT1() {
        setTheme(Theme.T1);
    }

    /**
     * Called when theme2 button is clicked
     */
    @FXML
    private void onClickT2() {
        setTheme(Theme.T2);
    }

    /**
     * Called when theme3 button is clicked
     */
    @FXML
    private void onClickT3() {
        setTheme(Theme.T3);
    }

    /**
     * Sets the current color theme
     *
     * @param theme the theme to be set
     */
    private void setTheme(Theme theme) {
        root.getStylesheets().remove(themeStyleMap.get(currentTheme));
        root.getStylesheets().add(themeStyleMap.get(theme));
        currentTheme = theme;
    }
}

package ooga.view.gameDisplay;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.view.gameDisplay.bottom.*;
import ooga.view.gameDisplay.center.*;
import javafx.scene.Scene;
import ooga.view.UINodeBuilder.UINodeBuilder;
import ooga.view.gameDisplay.top.GameStats;

public class GameDisplay {
    private Stage myStage;
    private Scene myScene;
    private BorderPane root;
    private GameButtons myGameButtons;
    private BoardView myBoardView;
    private GameStats myGameStats;
    private static final String DEFAULT_RESOURCE_PACKAGE = "ooga.view.resources.";
    private static final String DEFAULT_STYLESHEET =
            "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/") + "Default.css";
    private Controller myController;

    public GameDisplay(Stage stage, int width, int height, String viewMode, String language,  String gameType, Controller controller) {
        myController = controller;
        myGameStats = new GameStats(myController);
        myStage = stage;
        root = new BorderPane();
        myScene = new Scene(root, width, height);
        myScene.getStylesheets().add(getClass().getResource(DEFAULT_STYLESHEET).toExternalForm());
        myGameButtons = new GameButtons(stage, width, height, myController);
        myBoardView = new BoardView();
    }

    /**
     * Sets the new scene which will show the actual pacman games
     * @param title The title for the stage
     */
    public void setMainDisplay(String title) {
        setupScene();
        myStage.setTitle(title);
        myStage.setScene(myScene);
    }


    private void setupScene(){
        root.setTop(myGameStats.makeStatLabels());
        root.setCenter(myBoardView.makeBoard());
        root.setBottom(myGameButtons.makeButtonBox());
    }

}

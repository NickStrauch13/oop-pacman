package ooga.controller;

import javafx.stage.Stage;
import java.lang.Integer;

import ooga.models.game.Board;
import ooga.models.game.CollisionManager;
import ooga.models.game.Game;
import ooga.view.gameDisplay.center.BoardView;
import ooga.view.home.HomeScreen;
import org.json.simple.parser.ParseException;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import ooga.view.gameDisplay.gamePieces.MovingPiece;

public class Controller {

    // TODO: Constant values should be in a file probably - enum?
    public static final Dimension DEFAULT_SIZE = new Dimension(1000, 600);
    public static final String TITLE = "Start Screen";
    public static final String gameType = "Pacman"; //TODO update gameType variable
    public static final int CELL_SIZE = 25;

    private Game myGame;
    private Board myBoard;
    private BoardView myBoardView;
    private double animationSpeed;
    private ArrayList<MovingPiece> myMovingPieces;
    private HomeScreen myStartScreen;
    private CollisionManager collisionManager;


    // TODO: Probably bad design to mix stage and board initialization at the same time. Will talk to my TA about this.
    // TODO: Maybe let the controller do readFile by moving readFile() from HomeScreen to Controller?
    /**
     * The constructor of the game controller that starts and controls the overall communication between the frontend and backend
     * @param stage the Stage object for the view
     * @throws IOException
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Controller(Stage stage) throws IOException, ParseException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        myStartScreen = new HomeScreen(stage, DEFAULT_SIZE.width, DEFAULT_SIZE.height, this);
        collisionManager = new CollisionManager();
        stage.setTitle(TITLE);
        stage.setScene(myStartScreen.createScene());
        stage.show();
        animationSpeed = 0.3;
    }

    // TODO: I think this should be private, and I definitely need to refactor this as well
    // TODO: Throw vs. try/catch here
    /**
     * Initialize a Pacman game
     * @param path The directory of a layout file
     */
    public void initializeGame(String path) {
        int numOfRows, numOfCols;
        try {
            JSONReader reader = new JSONReader(path);
            JSONContainer container = reader.readJSONConfig();
            numOfRows = container.getMyNumOfRows();
            numOfCols = container.getMyNumOfCols();
            Map<Integer, String> gameObjectMap = container.getMyConversionMap();

            //TODO: Currently creatureMap is never accessed
            Map<Integer, String> creatureMap = container.getMyCreatureMap();
            List<List<String>> stringBoard = container.getMyStringBoard();

            myBoard = new Board(numOfRows, numOfCols);
            initializeBoard(numOfRows, numOfCols, gameObjectMap, stringBoard);

            myBoardView = new BoardView(this);
            initializeBoardView(numOfRows, numOfCols, gameObjectMap, stringBoard);

            myGame = new Game(myBoard,myBoard.getNumPickupsAtStart(), myBoard.getMyUser(),myBoard.getMyCPUCreatures() ,CELL_SIZE); //TODO assigning pickups manually assign from file!!


            //TODO get lives from JSON file
        }
        catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | IOException | ParseException | InstantiationException e) {
            e.printStackTrace();     // TODO: Need better exception handling if we are going with try/catch
        }
    }

    /*
    Initialize all game objects within the Board object
     */
    private void initializeBoard(int numOfRows, int numOfCols, Map<Integer, String> gameObjectMap, List<List<String>> stringBoard) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfCols; col ++) {
                String objectName = stringBoard.get(row).get(col);
                if (gameObjectMap.containsValue(objectName)) {
                    myBoard.createGameObject(row, col, objectName);
                }
                else {
                    myBoard.createCreature(col*CELL_SIZE+3, row*CELL_SIZE+3, objectName,CELL_SIZE-5);
                }
            }
        }
    }

    /*
    Initialize all pieces within the BoardView object
     */
    private void initializeBoardView(int numOfRows, int numOfCols, Map<Integer, String> gameObjectMap, List<List<String>> stringBoard) {
        myBoardView.makeBoard(numOfRows, numOfCols);
        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfCols; col ++) {
                String objectName = stringBoard.get(row).get(col);
                if (gameObjectMap.containsValue(objectName)) {
                    myBoardView.addBoardPiece(row, col, objectName);
                }
                else {
                    if(objectName.equals("PACMAN")) { //TODO I added this in as a temporary fix. We need a way to tell if the creature is user controlled or CPU controlled. Maybe have the user specify what piece they want to control in the json file?
                        myBoardView.addUserCreature(row, col, objectName);
                    }
                    else{
                        myBoardView.addCPUCreature(row, col, objectName);
                    }
                }
            }
        }
    }

    public int getCellCoordinate(double pixels){
        return ((int)pixels)/CELL_SIZE;
    }

    /**
     * Get the number of lives remained
     * @return the number of lives remained
     */
    public int getLives() {
        return myGame.getLives(); //TODO change this to the model's get lives
    }

    /**
     * Get the current game scores
     * @return the current game scores
     */
    public int getScore() {
        return myGame.getScore();
    }

    /**
     * Get the game category
     * @return the game category
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * Get the BoardView object of the game
     * @return the Boardview object
     */
    public BoardView getBoardView() {
        return myBoardView;
    }

    /**
     * Get the dimension of each cell
     * @return the size of a cell in the board
     */
    public int getCellSize() {
        return CELL_SIZE;
    }


//    public int getRows() {
//        return myBoard.getRows();
//    }
//
//    public int getCols() {
//        return myBoard.getCols();
//    }
//
//    public double getAnimationSpeed() {
//        return animationSpeed;
//    }


    /**
     * Returns the hashmap containing the moving game objects "creatures"
     * @return the creature map
     */
//    public Map getCreatureMap(){
//        return creatureMap;
//    }

    /**
     * Update and sync each frame of the game with the last direction used
     * @param direction the string value for the direction
     */
    public void step(String direction) {
        myGame.setLastDirection(direction);
        myGame.step();
    }

    /**
     * Access the current coordinates of the user
     * @return (x,y) of the current position
     */
    public int[] getUserPosition() {
        int [] newPosition = {myBoard.getMyUser().getXpos(), myBoard.getMyUser().getYpos()};
        return newPosition;
    }

    /**
     * Gets the new ghost position of the ghost identified by the given ID
     * @param nodeID
     * @return
     */
    public int[] getGhostPosition(String nodeID) {
        if (myBoard.getMyCPU(nodeID) != null) {
            int[] newPosition = {myBoard.getMyCPU(nodeID).getXpos(), myBoard.getMyCPU(nodeID).getYpos()};
            return newPosition;
        }
        //System.out.print("NOT FOUND IN CREATURE ARRAY");
        return null;
    }

    /**
     * METHOD ONLY FOR TESTFX TESTS. Needed some way to load in a file into the file chooser.
     */
    public void changeToGameScreen(String filePath){
        myStartScreen.startNewGameForViewTests(filePath);
    }

    /**
     * Used by frontend to report the most recent node collision.
     * @param nodeID The ID of the most recently collided node.
     */
    public void setCollision(String nodeID){
        //TODO pass to backend to handle collision action depending on the node type
        collisionManager.setCollision(nodeID);
        myGame.dealWithCollision(collisionManager);
    }

    /**
     * Used by the frontend to get the ID of a node that should be removed from the view. If nothing
     * is to be removed, this method returns null.
     * @return ID of node that should be removed from the view on the current step.
     */
    public String getRemovedNodeID() {
        //return (some call to backend that gets the node ID that should be removed on this step. If nothing
        //should be removed this step, rust return null.
        return collisionManager.getCurrentCollision(); //Temporary placeholder for the return.
    }

    /**
     * Sends information about the collision to the backend
     * @param nodeID
     * @return
     */
    public boolean handleCollision(String nodeID){
        collisionManager.setCollision(nodeID);
        return myGame.dealWithCollision(collisionManager);
    }

    public void resetGame() {
        myGame.resetGame();;
    }

}

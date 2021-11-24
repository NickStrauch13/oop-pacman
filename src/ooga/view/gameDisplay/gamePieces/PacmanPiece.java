package ooga.view.gameDisplay.gamePieces;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PacmanPiece extends MovingPiece {
    private static final String CSS_ID = "pacmanPiece";
    private static final int CREATURE_SLOP = 5;

    public PacmanPiece(int cellSize){
        super(cellSize);
    }

    @Override
    protected Node makeNode(){
        ImageView pacman = new ImageView("ooga/view/resources/viewIcons/yellow square.png");
        pacman.setFitWidth(getCellSize()-CREATURE_SLOP);
        pacman.setFitHeight(getCellSize()-CREATURE_SLOP);
        setIDs(pacman, CSS_ID, getCellIndexID());
        System.out.println(getCellSize());
        return pacman;
    }


}

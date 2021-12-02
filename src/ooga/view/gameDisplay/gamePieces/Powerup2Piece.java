package ooga.view.gameDisplay.gamePieces;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that represents the dot pickup pieces in the view board.
 */
public class Powerup2Piece extends GamePiece{
  public static final int DOT_RAD = 8;
  private static final String CSS_ID = "dotPiece";

  public Powerup2Piece(Integer cellSize){
    super(cellSize);
  }

  @Override
  protected Circle makeNode(){
    Circle dot = new Circle(DOT_RAD);
    dot.setId(getCellIndexID());
    dot.setFill(Color.rgb(184,134, 11));
    return dot;
  }
}
package ooga.view.UINodeBuilder;


import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class UINodeBuilder {
  private static final String DEFAULT_RESOURCE_PACKAGE = "ooga.view.resources.";
  private static final String DEFAULT_STYLESHEET =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/") + "Default.css";
  private ResourceBundle myResources;
  public UINodeBuilder(){
    myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "English"); //TODO add language here

  }

  /**
   * Creates buttons for the UI.
   * Adapted from previous project.
   * @param property
   * @param response
   * @return Button node
   */
  public Button makeButton(String property, EventHandler<ActionEvent> response) {
    Button result = new Button();
    result.setText(property);
    result.setOnAction(response);
    result.getStyleClass().add("button");
    return (Button)setID(property, result);
  }

  private Node setID(String id, Node node) {
    node.setId(id);
    return node;
  }

}
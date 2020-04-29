/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2020
 * Instructor: Prof. Chris Dancy
 *
 * Name: Christopher Asbrock
 * Section: 11am
 * Date: 4/29/20
 * Time: 6:20 AM
 *
 * Project: csci205_final_project_sp2020
 * Package: main
 * Class: SnakeMenu
 *
 * Description:
 *
 * ****************************************
 */
package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SnakeMenu extends GridPane
{
    private Button clientButton;
    private Button hostButton;
    private TextField hostText;
    private TextField portText;
    private TextField hostPortField;
    private TextField numOfPlayerField;
    private TextField setHeightField;
    private TextField setWidthField;
    private TextField displayMessage;

    /**
     * initializes and add the menu items to the grid menu
     * general layout (so the grid coordinates are not as confusing to look at)
     *
     *  TOP: join game menu, with host and port field
     *  MIDDLE: host game menu, with port, number of players, width and height fields
     *  BOTTOM: a message display, for errors or updates
     */
    public SnakeMenu()
    {
        super();
        setDisplayProperties();
        initMenusFields();
        setUpJoinSection();
        setUpHostSection();

        this.add(this.displayMessage, 1, 13,4,1);
    }

    private void initMenusFields()
    {
        this.hostText = new TextField();
        this.portText = new TextField();
        this.hostPortField = new TextField();
        this.numOfPlayerField = new TextField();
        this.setHeightField = new TextField();
        this.setWidthField = new TextField();
        this.clientButton = new Button();
        this.hostButton = new Button();
        this.displayMessage = new TextField();

        this.setUpDefaultAttributes();
        this.setDefaultValues("1", "800", "600");
    }

    private void setUpDefaultAttributes()
    {
        this.clientButton.setText("JOIN");
        this.hostButton.setText("HOST");

        this.displayMessage.setMouseTransparent(true);
        this.displayMessage.setEditable(false);
        this.displayMessage.setStyle("-fx-background-color: #c8c8c8; -fx-text-fill: red");
        this.displayMessage.setAlignment(Pos.CENTER);
    }

    private void setDefaultValues(String players, String width, String height)
    {
        this.numOfPlayerField.setText(players);
        this.setWidthField.setText(width);
        this.setHeightField.setText(height);
    }

    private void setDisplayProperties()
    {
        this.setPrefSize(400,300);
        //this.startMenu.setGridLinesVisible(true);
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
    }

    private void setUpHostSection()
    {
        this.add(new Label("HOST GAME"), 2, 6,3,1);
        this.add(new Label("PORT:"), 1,7);
        this.add(this.hostPortField, 2,7);
        this.add(this.hostButton,3,7,1,4);
        this.add(new Label("PLAYERS:"), 1,8);
        this.add(this.numOfPlayerField,2,8);
        this.add(new Label("PANE HEIGHT:"), 1,9);
        this.add(this.setHeightField,2,9);
        this.add(new Label("PANE WIDTH:"), 1,10);
        this.add(this.setWidthField,2,10);
    }

    private void setUpJoinSection()
    {
        this.add(new Label("JOIN GAME"), 2, 1,3,1);
        this.add(new Label("HOST:"), 1,2);
        this.add(this.hostText, 2,2);
        this.add(new Label("PORT:"), 1,3);
        this.add(this.portText,2,3);
        this.add(this.clientButton,3,2,1,2);
    }

    public void deactivateMenu()
    {
        this.getChildren().forEach((node)->node.setDisable(true));
        this.displayMessage.setDisable(false);
    }

    public void activateMenu()
    {
        this.getChildren().forEach((node)->node.setDisable(false));
    }

    public void onClientButtonClick(EventHandler<ActionEvent> event)
    {
        this.clientButton.setOnAction(event);
    }

    public void onHostButtonClick(EventHandler<ActionEvent> event)
    {
        this.hostButton.setOnAction(event);
    }

    public void hostStartAction(MVCSnakeController controller)
    {
        controller.setHost(this.hostPortField.getText(),
                this.numOfPlayerField.getText(),
                this.setWidthField.getText(),
                this.setHeightField.getText());
    }

    public void joinStartAction(MVCSnakeController controller)
    {
        controller.setJoin(this.hostText.getText(), this.portText.getText());
    }

    public TextField getDisplayMessage()
    {
        return displayMessage;
    }
}
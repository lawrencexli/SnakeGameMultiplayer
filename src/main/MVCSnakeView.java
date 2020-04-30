package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MVCSnakeView extends Application
{
    /**game main root*/
    private Pane root;

    /**games start menu*/
    private SnakeMenu startMenu;

    /**A reference to the game controller*/
    private MVCSnakeController controller;

    /**
     * the scene init,
     * sets up the menu, controller, and the main pane
     */
    @Override
    public void init()
    {
        root = new Pane();
        controller = new MVCSnakeController(this);
        root.setPrefSize(this.controller.WIDTH, this.controller.HEIGHT);

        this.startMenu = new SnakeMenu();

        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(this.controller.WIDTH/4.0);
        this.startMenu.setTranslateY(this.controller.HEIGHT/4.0);
    }

    /**
     * javafx start method, sets and shows the stage,
     * and handles the user input before and after the scene
     *
     * @param primaryStage - the primary stage
     */
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setScene(new Scene(this.root));
        if (this.controller.gameGoing)
            this.userInputButtonPress(primaryStage);
        else
        {
            this.startMenu.onHostButtonClick((event) ->
                    this.startMenu.hostStartAction(this.controller));

            this.startMenu.onClientButtonClick((event) ->
                    this.startMenu.joinStartAction(this.controller));
        }
        primaryStage.show();
    }

    /**
     * the call to update the view,
     * if the controller has any changes it will make a call to the view to update with any new information
     */
    public void updateView()
    {
        if (this.controller.gameGoing)
        {
            //set the write flag (so no changes are made while this is being used)
            this.controller.dataWrite = true;

            //remove the no longer needed nodes, and clear the list
            while (!this.controller.getTrash().isEmpty())
                this.root.getChildren().remove(this.controller.getTrash().remove(0));

            //goes through and updates any new items that just game in
            for (Circle part : this.controller.getITEM_POSITIONING())
                if (part != null && !this.root.getChildren().contains(part))
                    this.root.getChildren().add(part);

            //uses the controller positioning reference to update the snakes on scene
            for (int i = 0; i < this.controller.getNumOfPlayers(); i++)
                for (Circle part : this.controller.getSNAKE_PARTS_POSITIONING().get(i))
                    if (part != null && !this.root.getChildren().contains(part))
                        this.root.getChildren().add(part);

            //view update is done, the data can be changed again
            this.controller.dataWrite = false;
        }
        else
        {
            //if the game is not running its just because the menu is up, any update is basically only
            //going to change what the message says
            this.startMenu.getDisplayMessage().setText(this.controller.menuMessage);
        }
    }

    /**
     * user input handle,
     * on a keystroke it makes a call to the controller who will handle it
     *
     * @param stage - the primary stage
     */
    private void userInputButtonPress(Stage stage)
    {
        stage.getScene().setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.leftTurn(true);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.leftTurn(true);
        });

        stage.getScene().setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.rightTurn(false);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.rightTurn(false);
        });
    }

    /**
     * menu getter, need the reference to add to the scrap node when done with it
     *
     * @return - reference to snakeMenu
     */
    public SnakeMenu getStartMenu()
    {
        return startMenu;
    }

    /**
     * main program launch
     *
     * @param args - command line arguments - NOT USED
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
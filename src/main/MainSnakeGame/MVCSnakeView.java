package main.MainSnakeGame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.SnakeGameAssets.SnakeMenu;

public class MVCSnakeView extends Application
{
    /**game main root*/
    private Pane root;

    /**games start menu*/
    private SnakeMenu startMenu;

    private Label snakeAlert;

    /**A reference to the game controller*/
    private MVCSnakeController controller;

    private final int WALL_SIZE = 30;

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

        this.addWalls();
        this.addSnakeAlertTextBox();

        this.startMenu = new SnakeMenu();

        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(this.controller.WIDTH/4.0);
        this.startMenu.setTranslateY(this.controller.HEIGHT/4.0);
    }

    private void addSnakeAlertTextBox()
    {
        this.snakeAlert = new Label("WELCOME");
        this.snakeAlert.setStyle("-fx-background-color: #f2f2f2");
        this.snakeAlert.setAlignment(Pos.CENTER);
        this.snakeAlert.setPrefWidth(this.controller.WIDTH/2.0);
        this.snakeAlert.setMouseTransparent(true);
        this.snakeAlert.setTranslateX(this.controller.WIDTH/2.0 - this.snakeAlert.getPrefWidth()/2);
        this.snakeAlert.setTranslateY(this.controller.HEIGHT - 24);
        this.root.getChildren().add(snakeAlert);
    }

    private void addWalls()
    {
        this.root.getChildren().add(this.setWall(0,0, this.WALL_SIZE, this.controller.HEIGHT, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(this.controller.WIDTH - this.WALL_SIZE, 0,this.WALL_SIZE, this.controller.HEIGHT, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(0,0, this.controller.WIDTH, this.WALL_SIZE, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(0, this.controller.HEIGHT - this.WALL_SIZE,this.controller.WIDTH, 30, Color.DARKGRAY));
    }

    private Rectangle setWall(int posX, int posY, int width, int height, Color color)
    {
        Rectangle tempTangle = new Rectangle(width, height, color);
        tempTangle.setTranslateX(posX);
        tempTangle.setTranslateY(posY);

        return tempTangle;
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

        this.userInputGameGoing(primaryStage);
        this.userInputMainMenu();

        primaryStage.show();
    }

    private void userInputMainMenu() {
        this.startMenu.onHostButtonClick((event) ->
                this.startMenu.hostStartAction(this.controller));

        this.startMenu.onClientButtonClick((event) ->
                this.startMenu.joinStartAction(this.controller));
    }

    /**
     * the call to update the view,
     * if the controller has any changes it will make a call to the view to update with any new information
     */
    public void updateView()
    {
        if (this.controller.gameGoing)
        {
            this.snakeAlert.setText(this.controller.gameMessage);

            //remove the no longer needed nodes, and clear the list
            while (!this.controller.getTrash().isEmpty())
                this.root.getChildren().remove(this.controller.getTrash().remove(0));

            //goes through and updates any new items that just game in
            for (Circle part : this.controller.getITEM_POSITIONING())
                if (part != null && !this.root.getChildren().contains(part))
                    this.root.getChildren().add(part);

            //uses the controller positioning reference to update the snakes on scene
            for (int i = 0; i < this.controller.getNumOfPlayers(); i++)
                for (Circle part : this.controller.getSNAKE_PARTS_POSITIONING()[i])
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
    private void userInputGameGoing(Stage stage)
    {
        stage.getScene().setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.leftTurn(true);
            if (event.getCode() == KeyCode.RIGHT)
                this.controller.rightTurn(true);
        });

        stage.getScene().setOnKeyReleased(event ->
        {
            if (event.getCode() == KeyCode.LEFT)
                this.controller.leftTurn(false);
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

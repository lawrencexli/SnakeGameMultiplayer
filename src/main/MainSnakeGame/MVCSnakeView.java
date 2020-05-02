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
import main.CommonInterfaces.GameGlobalValues;
import main.SnakeGameAssets.SnakeMenu;
import main.SnakeGameAssets.SnakePlayerText;

public class MVCSnakeView extends Application implements GameGlobalValues
{
    /**game main root*/
    private Pane root;

    /**games start menu*/
    private SnakeMenu startMenu;

    private Label snakeAlert;

    private SnakePlayerText playerInfo;

    /**A reference to the game controller*/
    private MVCSnakeController controller;

    private Circle[][] snakeParts;
    private Circle[] items;

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

        initSnakes();
        initItems();

        this.addWalls();
        this.addSnakeAlertTextBox();

        this.startMenu = new SnakeMenu();

        this.root.getChildren().add(this.startMenu);
        this.startMenu.setTranslateX(this.controller.WIDTH/4.0);
        this.startMenu.setTranslateY(this.controller.HEIGHT/4.0);

        this.playerInfo = new SnakePlayerText();
        this.root.getChildren().add(this.playerInfo);
        this.playerInfo.setTranslateX(WALL_THICKNESS);
        this.playerInfo.setTranslateY(this.controller.HEIGHT - 26);
    }

    private void initSnakes()
    {
        this.snakeParts = new Circle[MAX_PLAYERS][MAX_SNAKE_PIECES];

        for (int i = 0; i <this.snakeParts.length; i++)
        {
            for (int j = 0; j < this.snakeParts[i].length; j++)
            {
                Circle part = new Circle(SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE,SNAKE_PIECE_SIZE, Color.DARKGRAY);
                part.setTranslateX(-30);
                part.setTranslateY(-30);
                this.snakeParts[i][this.snakeParts[i].length - 1 -j] = part;
                this.root.getChildren().add(part);
            }
        }
    }

    private void initItems()
    {
        this.items = new Circle[MAX_ITEMS];

        for (int i = 0; i < this.items.length; i++)
        {
            Circle part = new Circle(ITEM_SIZE,ITEM_SIZE,ITEM_SIZE, Color.DARKGRAY);
            part.setTranslateX(-30);
            part.setTranslateY(-30);
            this.items[i] = part;
            this.root.getChildren().add(part);
        }
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
        this.root.getChildren().add(this.setWall(0,0, WALL_THICKNESS, this.controller.HEIGHT, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(this.controller.WIDTH - WALL_THICKNESS, 0,WALL_THICKNESS, this.controller.HEIGHT, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(0,0, this.controller.WIDTH, WALL_THICKNESS, Color.DARKGRAY));
        this.root.getChildren().add(this.setWall(0, this.controller.HEIGHT - WALL_THICKNESS,this.controller.WIDTH, WALL_THICKNESS, Color.DARKGRAY));
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
            this.playerInfo.setPlayerField(this.controller.playerInfo, this.controller.playerColor);

            updateAllNodesBeta();

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

    private void updateAllNodesBeta()
    {
        //remove the no longer needed nodes, and clear the list
        while (!this.controller.getTrash().isEmpty())
            this.root.getChildren().remove(this.controller.getTrash().remove(0));

        updateItems();
        updateSnake();
    }

    private void updateItems()
    {
        for (int i = 0; i < this.items.length ; i++)
        {
            if (i < this.controller.getItemPositions().size())
            {
                this.items[i].setVisible(true);
                this.items[i].setTranslateX(this.controller.getItemPositions().get(i).getTranslateX());
                this.items[i].setTranslateY(this.controller.getItemPositions().get(i).getTranslateY());
                this.items[i].setFill(this.controller.getItemPositions().get(i).getFill());
            }
            else
                this.items[i].setVisible(false);
        }
    }

    private void updateSnake()
    {
        for (int i = 0; i < this.controller.getSnakePostions().length ; i++)
        {
            for (int j = 0; j < this.snakeParts[i].length ; j++)
            {
                if (j < this.controller.getSnakePostions()[i].size() && this.controller.getSnakePostions()[i].get(j) != null)
                {
                    this.snakeParts[i][j].setVisible(true);
                    this.snakeParts[i][j].setScaleX(this.controller.getSnakePostions()[i].get(j).getScaleX());
                    this.snakeParts[i][j].setScaleY(this.controller.getSnakePostions()[i].get(j).getScaleY());
                    this.snakeParts[i][j].setTranslateX(this.controller.getSnakePostions()[i].get(j).getTranslateX());
                    this.snakeParts[i][j].setTranslateY(this.controller.getSnakePostions()[i].get(j).getTranslateY());
                    this.snakeParts[i][j].setRotate(this.controller.getSnakePostions()[i].get(j).getRotate());
                    this.snakeParts[i][j].setFill(this.controller.getSnakePostions()[i].get(j).getFill());
                }
                else
                    this.snakeParts[i][j].setVisible(false);
            }
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

    public SnakePlayerText getPlayerInfo()
    {
        return playerInfo;
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
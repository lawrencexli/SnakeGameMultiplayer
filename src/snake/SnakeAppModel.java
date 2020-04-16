package snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class SnakeAppModel extends Application implements SnakeDriver, PlayerDriver
{
    private Pane root;

    private final ArrayList<GameObject> foodList = new ArrayList<>();
    private final ArrayList<Wall> walls = new ArrayList<>();

    protected GameObject player;
    static GameObject snakeTest = null;
    //private SnakeAppController controller;

    //private GameObject user;

    @Override
    public void start(Stage stage)
    {
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e ->
        {
            /*
                if (e.getCode() == KeyCode.DOWN)
                    ((User)user).down();
                if (e.getCode() == KeyCode.UP)
                    ((User)user).up();
                if (e.getCode() == KeyCode.LEFT)
                    ((User)user).left();
                if (e.getCode() == KeyCode.RIGHT)
                    ((User)user).right();
*/
            if (e.getCode() == KeyCode.LEFT)
                ((Snake)player).turnLeft = true;
            if (e.getCode() == KeyCode.RIGHT)
                ((Snake)player).turnRight = true;
            /*
            if (((User) user).getSwordStatus() == null)
            {
                if (e.getCode() == KeyCode.SPACE)
                {
                    addGameObject(((User) user).createSword(user),
                            (user.view.getTranslateX() + user.view.getScaleX()) / 2,
                            (user.view.getTranslateY() + user.view.getScaleY()) / 2);
                }
            }*/
        });


        stage.getScene().setOnKeyReleased(e ->
        {
            /*
            if (e.getCode() == KeyCode.RIGHT)
                ((User)user).stillLeft();
            else if (e.getCode() == KeyCode.LEFT)
                ((User)user).stillLeft();
            else if (e.getCode() == KeyCode.UP)
                ((User)user).stillUp();
            else if (e.getCode() == KeyCode.DOWN)
                ((User)user).stillUp();
*/
            if (e.getCode() == KeyCode.RIGHT)
                ((Snake)player).turnRight = false;
            else if (e.getCode() == KeyCode.LEFT)
                ((Snake)player).turnLeft = false;;
        });

        stage.show();
    }

    private Parent createContent()
    {
        root = new Pane();
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: black;");

        Wall wall = new Wall(new Rectangle(30, root.getPrefHeight(), Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,0);

        wall = new Wall(new Rectangle(30, root.getPrefHeight(), Color.GRAY));
        walls.add(wall);
        addGameObject(wall, root.getPrefWidth() - 30 ,0);

        wall = new Wall(new Rectangle(root.getPrefWidth(), 30, Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,0);

        wall = new Wall(new Rectangle(root.getPrefWidth(), 30, Color.GRAY));
        walls.add(wall);
        addGameObject(wall, 0 ,root.getPrefHeight() - 30);


        player = new Snake(root);
        snakeTest = player;
        player.setVelocity(new Point2D(1,0));
        addGameObject(player, root.getPrefWidth()/2, root.getPrefHeight()/2);

        /*
        user = new User(root);
        user.setVelocity(new Point2D(1,0));
        addGameObject(user, root.getPrefWidth()/2, root.getPrefHeight()/2);

         */
/*
        for(int i = 0; i < 30; i++)
        {
            SnakePart newSnakePart = new SnakePart(null, player);
            ((Player) player).addPart(newSnakePart);
            addGameObject(newSnakePart , player.getView().getTranslateX(), player.getView().getTranslateY());
        }
*/

        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                updateView();
            }
        };

        timer.start();
        return root;
    }


    private void addFood(GameObject food, double x, double y)
    {
        int randomNum =  new Random().nextInt(3);
        switch (randomNum)
        {
            case 0: ((Circle) food.view).setFill(new ImagePattern(new Image("apple.png")));
                break;
            case 1: ((Circle) food.view).setFill(new ImagePattern(new Image("hamburger.png")));
                break;
            case 2: ((Circle) food.view).setFill(new ImagePattern(new Image("fries.png")));
                break;
        }

        foodList.add(food);
        addGameObject(food, x, y);
    }

    private void addGameObject(GameObject object, double x, double y)
    {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    public ArrayList<double[]> getSnakePositions()
    {
        if(!((Snake) this.player).bodyParts.isEmpty())
        {
            ArrayList<double[]> partList = new ArrayList<>();

            for (GameObject snake : ((Snake) this.player).bodyParts) {
                double[] temp = new double[3];
                temp[0] = snake.getView().getTranslateX();
                temp[1] = snake.getView().getTranslateY();
                temp[2] = snake.getView().getRotate();
                partList.add(temp);
            }

            return partList;
        }

        return null;
    }

    private void updateView()
    {
        for (GameObject food : foodList)
        {
            if (player.isColliding(food))
            {
                food.setAlive(false);
                root.getChildren().removeAll(food.getView());

                SnakePart newSnakePart = new SnakePart(null, player);
                ((Snake) player).addPart(newSnakePart);
                addGameObject(newSnakePart , player.getView().getTranslateX(), player.getView().getTranslateY());
            }

            /*
            if (((User) user).getSwordStatus() != null)
                if (((User) user).swordColliding(food))
                {
                    food.setAlive(false);
                    root.getChildren().removeAll(food.getView());
                }

             */
        }

        for (GameObject wall : walls)
        {
            if (player.isColliding(wall))
            {
                //player.reverse();
                player.setAlive(false);
            }
        }

        if (((Snake)player).checkForTail())
        {
            player.setAlive(false);
        }

        if (((Snake) player).checkForTail())
        {
            player.setAlive(false);
        }

        foodList.removeIf(GameObject::isEaten);

        walls.forEach(GameObject::update);

        foodList.forEach(GameObject::update);
        player.update();
        ((Snake) player).stackForEach(GameObject::update);

        //if (this.controller != null)
        {
            //System.out.println("nnn");
            //controller.snakePartPositions.clear();
            //controller.snakePartPositions.addAll(this.getSnakePositions());
        }

        //this.controller.controllerUpdate();

        /*
        ((User) user).userUpdate();

        if (((User) user).getSwordStatus() != null)
            ((User) user).swordUpdate();
*/
        int rand;
        Random randomizer = new Random();

        if (Math.random() < 0.02)
        {
            rand = randomizer.nextInt(5) + 10;
            /*
            if (rand % 2 == 0)
                for (int i = 0; i < rand; i++)
                    player.rotateLeft();
            else
                for (int i = 0; i < rand; i++)
                    player.rotateRight();
*/
            /*
            if (rand % 2 == 0)
            {
                player.turnRight = false;
                player.turnLeft = !player.turnLeft;
            }
            else
            {
                player.turnRight = !player.turnRight;
                player.turnLeft = false;
            }
*/

            if (foodList.size() < 30)
            addFood(new Food(),
                    30 + ((Math.random() * (root.getPrefWidth() - 90))),
                    30 + ((Math.random() * (root.getPrefHeight() - 90))));
        }
    }

    private static class Wall extends GameObject
    {
        Wall(Rectangle shape)
        {
            super(shape);
        }
    }



    private static class Food extends GameObject
    {
        Food()
        {
            super(new Circle(10, 10, 10, Color.RED));
        }
    }

    public void launchMain(String[] args)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                new SnakeAppModel().start(new Stage());
            }
        });
        //this.launch(args);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

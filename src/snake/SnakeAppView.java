package snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SnakeAppView extends Application
{
    private Pane root;
    private final ArrayList<GameObject> snakeParts = new ArrayList<>();
    private SnakeAppController controller;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.controller = new SnakeAppController(this);
        this.controller.launch(new String[3]);
        this.root = new Pane();
        root.setPrefSize(800, 600);
        root.setStyle("-fx-background-color: black;");

        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                update();
            }
        };

        timer.start();

        primaryStage.setScene(new Scene(this.root));
        primaryStage.show();

    }

    private void addGameObject(GameObject object, double x, double y)
    {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    public void update()
    {
        //System.out.println(this.controller);
        this.controller.controllerUpdate();

        String[] info = this.controller.snakePartPositions.split(";");

        //System.out.println(info.length);
        //System.out.println(info[0]);
        if (info.length > this.snakeParts.size())
        {
            while (info.length > this.snakeParts.size())
            {
                GameObject part = new GameObject(new Circle(15,15,15, Color.RED));
                this.snakeParts.add(part);
                this.addGameObject(part,-1,-1);
            }
        }

        for (int i = 0; i < info.length ; i++)
        {
            String[] positioning = info[i].split(",");
            this.snakeParts.get(i).getView().setTranslateX(Double.parseDouble(positioning[0]));
            this.snakeParts.get(i).getView().setTranslateY(Double.parseDouble(positioning[1]));
            this.snakeParts.get(i).getView().setRotate(Double.parseDouble(positioning[2]));
        }

        //System.out.println(this.snakeParts.size());
        snakeParts.forEach(GameObject::update);
    }
}

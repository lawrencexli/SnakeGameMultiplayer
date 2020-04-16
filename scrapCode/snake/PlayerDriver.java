package snake;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public interface PlayerDriver
{
    class User extends GameObject
    {
        private GameObject sword = null;
        private final Pane root;
        Rotate rotate = new Rotate();

        int horizontalSpeed = 0;
        int verticalSpeed = 0;

        int timer = 0;
        double swing = -90;

        User(Pane root)
        {
            super(new Rectangle(30, 30, Color.BLUE));
            ((Rectangle) this.view).setFill(new ImagePattern(new Image("scrapCode/snakeHead.png")));
            this.root = root;

        }

        public GameObject getSwordStatus()
        {
            return this.sword;
        }

        public boolean swordColliding(GameObject object)
        {
            return sword.isColliding(object);
        }

        public void swordUpdate()
        {
            sword.update();
        }

        public GameObject createSword(GameObject user)
        {
            sword = new PlayerDriver.Sword(user);
            sword.view.getTransforms().add(rotate);
            rotate.setAngle(user.view.getRotate() + 180);

            return sword;
        }

        public void userUpdate()
        {
            setVelocity(new Point2D(horizontalSpeed, verticalSpeed));

            if (horizontalSpeed > 0)
                view.setRotate(0);
            if (horizontalSpeed < 0)
                view.setRotate(180);
            if (verticalSpeed > 0)
                view.setRotate(90);
            if (verticalSpeed < 0)
                view.setRotate(270);

            view.setTranslateX(view.getTranslateX() + velocity.getX());
            view.setTranslateY(view.getTranslateY() + velocity.getY());

            timer++;

            if (timer > 15)
                swing += 5;

            if (swing > 20)
            {
                root.getChildren().removeAll(sword.view);
                sword = null;
            }

            if (sword != null)
            {
                rotate.setPivotX(((Rectangle) this.view).getX());
                rotate.setPivotY(((Rectangle) this.view).getY());
                rotate.setAngle(rotate.getAngle() + 10);
                //sword.view.setRotate(this.view.getRotate() - 90 + swing);
            }
            else
            {
                rotate.setAngle(0);
                swing = -90;
            }
        }

        public void left()
        {
            horizontalSpeed = -1;
        }

        public void right()
        {
            horizontalSpeed = 1;
        }

        public void up()
        {
            verticalSpeed = -1;
        }

        public void down()
        {
            verticalSpeed = 1;
        }

        public void stillLeft()
        {
            horizontalSpeed = 0;
        }

        public void stillUp()
        {
            verticalSpeed = 0;
        }
    }



    class Sword extends GameObject
    {
        GameObject parent;

        Sword(GameObject parent)
        {
            super(new Rectangle(15, 60, Color.ORANGE));
            ((Rectangle)this.view).setFill(new ImagePattern(new Image("scrapCode/sword2.png")));
            this.parent = parent;


            //this.view.setRotate(parent.view.getRotate());
        }

        @Override
        public void update()
        {
            //view.setTranslateX((user.view.getTranslateX() - user.view.getScaleX()) / 2);
            //view.setTranslateY((user.view.getTranslateY() - user.view.getScaleY()) / 2);
            view.setTranslateX(parent.view.getTranslateX() + 15);
            view.setTranslateY(parent.view.getTranslateY() + 15);
        }
    }
}

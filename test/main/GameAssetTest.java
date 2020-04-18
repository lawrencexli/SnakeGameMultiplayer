package main;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static main.GameAsset.MyRotate.LEFT;
import static org.junit.jupiter.api.Assertions.*;
import static main.GameAsset.MyRotate.RIGHT;

class GameAssetTest
{
    private static final double EPSILON = 1.0E-12;
    private Item testAsset;

    @BeforeEach
    void setUp()
    {
        this.testAsset = new Item(15, Color.RED, "Test");
    }

    @Test
    void rotate()
    {
        this.cordPrintOut();
        this.testAsset.setVelocity(1,0);

        assertEquals(this.testAsset.getTranslateX(), 0, EPSILON);
        this.testAsset.updateAsset();
        assertEquals(this.testAsset.getTranslateX(), 1, EPSILON);
        this.cordPrintOut();
        setUp();
        this.cordPrintOut();
        this.testAsset.setVelocity(2,0);
        this.testAsset.updateAsset();
        assertEquals(this.testAsset.getTranslateX(), 2, EPSILON);
        this.cordPrintOut();
        setUp();
        this.testAsset.setVelocity(1,0);
        for (int i = 0; i < 15; i++)
        {
            this.testAsset.rotate(LEFT);
        }
        this.testAsset.updateAsset();
        assertTrue(this.testAsset.getTranslateX() > 0);
        assertTrue(this.testAsset.getTranslateY() > 0);
        assertEquals(this.testAsset.getTranslateX(),this.testAsset.getTranslateY(), EPSILON );
        this.cordPrintOut();
    }

    @Test
    void rotateReverse()
    {
        this.testAsset.setVelocity(1,0);
        for (int i = 0; i < 15; i++)
        {
            this.testAsset.rotate(RIGHT);
        }
        this.testAsset.updateAsset();
        assertTrue(this.testAsset.getTranslateX() > 0);
        assertTrue(this.testAsset.getTranslateY() < 0);
        assertNotEquals(this.testAsset.getTranslateX(),this.testAsset.getTranslateY(), EPSILON );
        this.cordPrintOut();
    }

    private void cordPrintOut()
    {
        System.out.println(this.testAsset.getTranslateX() + " " + this.testAsset.getTranslateY() + " " + this.testAsset.getRotate());
    }
}
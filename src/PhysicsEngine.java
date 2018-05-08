/*
   PhysicsEngine.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Where the main is located along
   with setup of the window with java fx.

   Known Bugs:
   	everything
 */


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.ArrayList;

public class PhysicsEngine extends Application {

    @Override
    public void start(Stage theStage)
    {
        theStage.setTitle( "L.A.G.'s Physics Engine" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
        Canvas canvas = new Canvas( 1280, 720 );

        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();


        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {


            }
        }.start();


        theStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
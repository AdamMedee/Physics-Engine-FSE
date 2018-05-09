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
    public void start(Stage stage)
    {
        stage.setTitle( "L.A.G.'s Physics Engine" ); //Sets window title screen

        //Sets up window and canvas
        Group root = new Group();
        Scene scene = new Scene( root );
        stage.setScene( scene );
        final int WIDTH = 1280;
        final int HEIGHT = 720;
        Canvas canvas = new Canvas( WIDTH, HEIGHT);
        root.getChildren().add( canvas );

        //For displaying stuff to screen
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        String currentScreen = "MainMenu"; //Current menu being used

        //Initializing the differen menues
        MainMenu mainMenu = new MainMenu();
        SelectMenu selectMenu = new SelectMenu();
        CreditsMenu creditsMenu = new CreditsMenu();


        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                switch(currentScreen) {
                    case "MainMenu":
                        mainMenu.run();
                        mainMenu.update();
                        break;

                    case "Credits":
                        break;

                    case "SelectMenu":
                        break;

                    default:
                        break;

                }
            }
        }.start();


        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
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
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class PhysicsEngine extends Application {

    //Start method which contains the entire program
    @Override
    public void start(Stage stage)
    {
        stage.setTitle( "L.A.G.'s Physics Engine" ); //Sets window title screen

        //Sets up java fx essentials
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        //Stops the program when the windows closed.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        //Making the canvas
        final int WIDTH = 1280;
        final int HEIGHT = 720;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas); //Adds canvas to program

        //For displaying stuff to screen
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        String currentScreen = "SystemMenu"; //Current menu being used

        //Puts all the environement data from txt into arraylist of environemnt objects
        //(currently hardcoded for testing)
        ArrayList<Environment> environmentList = new ArrayList<Environment>();
        Environment a = new Environment(root);
        environmentList.add(a);

        //Initializing the different menus
        MainMenu mainMenu = new MainMenu();
        SelectMenu selectMenu = new SelectMenu();
        CreditsMenu creditsMenu = new CreditsMenu();
        SystemMenu systemMenu = new SystemMenu(environmentList.get(0), root);

        //Gets user input
        InputHandler inputHandler = new InputHandler(scene);



        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {

                inputHandler.prepareActionHandlers();
                System.out.println(inputHandler.getMousePos());

                //Runs the menu the user has selected
                switch(currentScreen) {
                    case "MainMenu":
                        mainMenu.run();
                        mainMenu.update(graphics);
                        break;

                    case "CreditsMenu":
                        creditsMenu.run();
                        creditsMenu.update(graphics);
                        break;

                    case "SelectMenu":
                        selectMenu.run();
                        selectMenu.update(graphics);
                        break;

                    case "SystemMenu":
                        systemMenu.run();
                        systemMenu.update(graphics);
                        break;

                    default:
                        break;
                }
        stage.show(); //Displays everything onto the screen
    }
}.start();
    }


    //Main method
    public static void main(String[] args) {
        launch(args);
    }
}
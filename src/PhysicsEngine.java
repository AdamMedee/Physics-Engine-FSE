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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.util.ArrayList;

public class PhysicsEngine extends Application {
    public static Stage window;
    static Scene CreditsScene;
    Group PhysicsLayout = new Group(), CreditsLayout = new Group();



    // -------- Menu Initialization
    public static MainMenu mainMenu;
    public static SystemMenu systemMenu;


    //--------------------------------------
    static String currentScreen = "MainMenu"; //Current menu being used
    //Start method which contains the entire program

    @Override
    public void start(Stage stage)
    {

        mainMenu = new MainMenu();
        //systemMenu = new SystemMenu(); // Will be initialized in the start method
        window = stage;


        stage.setTitle( "L.A.G.'s Physics Engine" ); //Sets window title screen


        //Stops the program when the windows closed.
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        // ----- Main Menu Initialization
        //STEPS:
        //Make scene menu and group in contructor
        //Clicking button changes which scene is set
        //Method to return scene object
        //Set scene in menu classes with button clicks
        //.run() returns string
        //currentScreen = mainMenu.run();


        mainMenu.update();





        // --------------------------
        //Making the canvas
        final int WIDTH = 1280;
        final int HEIGHT = 720;
        //Canvas canvas = new Canvas(WIDTH, HEIGHT);
        //root.getChildren().add(canvas); //Adds canvas to program


        // We will add canvas to each individual Group/layout

        //For displaying stuff to screen
        //GraphicsContext graphics = canvas.getGraphicsContext2D();



        //Puts all the environement data from txt into arraylist of environemnt objects
        //(currently hardcoded for testing)
        ArrayList<Environment> environmentList = new ArrayList<Environment>();
        Environment a = new Environment(PhysicsLayout);
        environmentList.add(a);


        CreditsScene = new Scene(CreditsLayout, 1280, 720);

        //Initializing the different menus
        SelectMenu selectMenu = new SelectMenu();
        CreditsMenu creditsMenu = new CreditsMenu(CreditsLayout);
        systemMenu = new SystemMenu(environmentList.get(0));



        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {

                //Runs the menu the user has selected
                switch(currentScreen) {
                    case "MainMenu":
                        //mainMenu.run();
                        //mainMenu.update();
                        break;

                    case "CreditsMenu":
                        creditsMenu.run();
                        creditsMenu.update(CreditsLayout);
                        break;

                    case "SelectMenu":
                        //selectMenu.run();
                        //selectMenu.update(root,graphics);
                        break;

                    case "SystemMenu":
                        //systemMenu.run();
                        //systemMenu.update(root,graphics);
                        break;

                    default:
                        break;
                }





    }

}.start();

        //Sets up java fx essentials
        window.setScene(mainMenu.MainMenuScene);
        window.show(); //Displays everything onto the screen
    }


    //Main method
    public static void main(String[] args) {
        launch(args);
    }
}
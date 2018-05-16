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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class PhysicsEngine extends Application {
    public static Stage window;
    static Scene CreditsScene;
    Group PhysicsLayout = new Group(), CreditsLayout = new Group();



    // -------- Menu Initialization
    public static MainMenu mainMenu;
    public static SystemMenu systemMenu;
    public static CreditsMenu creditsMenu;
    public static SelectMenu selectMenu;


    //--------------------------------------
    static String currentScreen = "MainMenu"; //Current menu being used

    //Start method which contains the entire program
    @Override
    public void start(Stage stage)
    {
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
        //mainMenu.update();



        // --------------------------
        //Making the canvas
        final int WIDTH = 1280;
        final int HEIGHT = 720;


        //Puts all the environement data from txt into arraylist of environemnt objects
        //(currently hardcoded for testing)
        ArrayList<Environment> environmentList = new ArrayList<Environment>();
        Environment a = new Environment();
        environmentList.add(a);




        //Initializing the different menus
        mainMenu = new MainMenu();
        selectMenu = new SelectMenu();
        creditsMenu = new CreditsMenu();
        systemMenu = new SystemMenu(environmentList.get(0));

        //Sets up java fx essentials
        window.setScene(mainMenu.mainMenuScene);
        window.show(); //Displays everything onto the screen

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {

                //Runs the menu the user has selected
                switch(currentScreen) {
                    case "MainMenu":
                        currentScreen = mainMenu.run();
                        mainMenu.update();
                        break;

                    case "CreditsMenu":
                        currentScreen = creditsMenu.run();
                        creditsMenu.update();
                        break;

                    case "SelectMenu":
                        currentScreen = selectMenu.run();
                        selectMenu.update();
                        break;

                    case "SystemMenu":
                        currentScreen = systemMenu.run();
                        systemMenu.update();
                        break;

                    default:
                        break;
                }





    }

}.start();


    }


    //Main method
    public static void main(String[] args) {
        launch(args);
    }
}
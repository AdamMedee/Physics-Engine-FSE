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
//test comment
public class PhysicsEngine extends Application {
    public static Stage window;                         //This is where you see the program

    // -------- Menu Initialization
    public static MainMenu mainMenu;               //Initialize each menu
    public static SystemMenu systemMenu;
    public static CreditsMenu creditsMenu;
    public static SelectMenu selectMenu;


    //--------------------------------------
    String currentScreen = "MainMenu";          //Current menu being used

    //Start method which contains the entire program
    @Override
    public void start(Stage stage)
    {
        window = stage;                                         //Creates window
        stage.setTitle( "L.A.G.'s Physics Engine" ); //Sets window title screen
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

        //Puts all the environment data from txt into arraylist of environemnt objects (currently hardcoded for testing)
        ArrayList<Environment> environmentList = new ArrayList<Environment>();
        Environment a = new Environment();
        environmentList.add(a);

        //Initializing the different menus
        mainMenu = new MainMenu();
        creditsMenu = new CreditsMenu();
        selectMenu = new SelectMenu();
        systemMenu = new SystemMenu(environmentList.get(0));

        //Sets default scene to main menu
        window.setScene(mainMenu.mainMenuScene);
        window.show(); //Displays everything onto the screen

        //Start of the game loop
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
}.start(); }

    //Main method
    public static void main(String[] args) {
        launch(args);
    }
}
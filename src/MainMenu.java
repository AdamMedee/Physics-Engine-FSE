/*
   MainMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class for the main menu
   which is opened when the program
   first runs.

   Known Bugs:
   	everything
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class MainMenu {

    public Image background = new Image("resources/images/LEOO.png");


    // Graphics
    public Scene mainMenuScene;
    public Group mainMenuLayout;
    public Button button1,button2;

    // Keeps track of switching between scenes
    public String newScene;

    //Constructor for the menu
    public MainMenu(){
        newScene = "MainMenu";
        mainMenuLayout = new Group();

       //mainMenuScene.getStylesheets().add("resources/Garu.css");

        //Making and placing buttons
        button1 = new Button("Demo");
        button1.setLayoutX(640);
        button1.setLayoutY(360);


        button2 = new Button("Credits");
        button2.setLayoutX(640);
        button2.setLayoutY(540);


        //Setting button actions
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PhysicsEngine.window.setScene(PhysicsEngine.systemMenu.systemScene);
                newScene = "SystemMenu";
            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PhysicsEngine.window.setScene(PhysicsEngine.creditsMenu.creditsScene);
                newScene = "CreditsMenu";
            }
        });

        //Canvas and graphics context
        Canvas canvas = new Canvas(1280,720);
        GraphicsContext graphics = canvas.getGraphicsContext2D();

        //Title and background
        Font ourFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/modern.ttf"),72);
        graphics.setFont(ourFont);
        graphics.strokeText("L.A.G Physics Engine",250,100);
        graphics.drawImage(background, 0, 0);

        //Adds all the nodes to the layout
        mainMenuLayout.getChildren().add(canvas);
        mainMenuLayout.getChildren().add(button1);
        mainMenuLayout.getChildren().add(button2);

        //Makes a new scene with the constructed group
        mainMenuScene = new Scene(mainMenuLayout,1280,720);
    }


    //Goes through the actions inputted and acts accordingly
    public String run(){
        return newScene;
    }


    //Displayes menu to the screen
    public void update(){

    }


}

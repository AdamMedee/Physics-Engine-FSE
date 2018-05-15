/*
   MainMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class for the main menu
   which is opened when the program
   first runs.

   Known Bugs:
   	everything
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;

public class MainMenu {

    public Image background = new Image("resources/images/LEOO.png");


    // Graphics
    public Scene MainMenuScene;
    public Group MainMenuLayout;
    public Button button1,button2;

    public String temp = "MainMenu";

    //Constructor for the menu
    public MainMenu(){

        MainMenuLayout = new Group();

       // MainMenuScene.getStylesheets().add("resources/Garu.css");

        button1 = new Button("Demo");
        button1.setLayoutX(640);
        button1.setLayoutY(360);


        button2 = new Button("Credits");
        button2.setLayoutX(640);
        button2.setLayoutY(540);



        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("I'm running");
                //temp = "SystemMenu";
                PhysicsEngine.window.setScene(PhysicsEngine.systemMenu.PhysicsScene);
            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //temp = "CreditsMenu";
            }
        });
        MainMenuLayout.getChildren().add(button1);
        MainMenuLayout.getChildren().add(button2);
        MainMenuScene = new Scene(MainMenuLayout,1280,720);
        //MainMenuLayout.getChildren().addAll(button1,button2);





    }


    //Goes through the actions inputted and acts accordingly
    public void run(){




    }


    //Displayes menu to the screen
    public void update(){
        Group src = MainMenuLayout;
        Canvas canvas = new Canvas(1280,720);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Font ourFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/modern.ttf"),72);

        graphics.setFont(ourFont);
        graphics.strokeText("L.A.G Physics Engine",250,100);
        
        graphics.drawImage(background, 0, 0);
        src.getChildren().add(canvas);

    }


}

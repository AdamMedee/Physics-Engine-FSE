/*
   SystemMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   This si where the user will be
   playing around with the physics
   engine, will run a single environment
   and allow user input.

   Known Bugs:
   	everything
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SystemMenu {

    Environment environment; //The environment being used
    String selected; //System, Object, or Information are the three options

    //Shape graphics
    Rectangle BGrect;
    Rectangle BGborder;

    //Buttons on the screen


    //Constructor for the menu
    public SystemMenu(Environment environment, Group root){
        //Environment being run
        this.environment = environment;

        //Buttons to change the selected var
        Button systemB = new Button("System");
        systemB.setPrefSize(150, 30);
        systemB.setLayoutX(980);
        systemB.setLayoutY(0);
        root.getChildren().add(systemB);

        Button objectB = new Button("Objects");
        objectB.setPrefSize(150, 30);
        objectB.setLayoutX(1130);
        objectB.setLayoutY(0);
        root.getChildren().add(objectB);

        //Adds graphics and buttons to root
        //Background and background border
        BGrect = new Rectangle(980, 28, 300, 692);
        BGrect.setFill(javafx.scene.paint.Color.rgb(200, 76, 76));
        //BGrect.setOpacity(0.3);
        BGborder = new Rectangle(979, 0, 2, 720);
        BGborder.setFill(javafx.scene.paint.Color.BLACK);
        root.getChildren().add(BGrect);
        root.getChildren().add(BGborder);

        Button back = new Button("Back");
        back.setTranslateX(20);
        back.setTranslateY(20);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.MainMenuScene);
                PhysicsEngine.currentScreen = "MainMenu";
            }
        });
        root.getChildren().add(back);

    }


    //Goes through the actions inputted and acts accordingly
    public void run(){
        environment.run();
    }


    //Displayes menu to the screen
    public void update(Group src){
        Canvas canvas = new Canvas(1280,720);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        //Draws user input features

        //Draws environment objects
        environment.update(graphics);
    }
}

/*
   CreditsMenu.java                  2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Shows content creators and everyone
   who helped make this program possible.

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

public class CreditsMenu {

    public Button back;
    public String newScene;
    public Scene creditsScene;
    public Group creditsLayout;

    public CreditsMenu(){
        back = new Button("Back");
        back.setLayoutX(20);
        back.setLayoutY(20);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                newScene = "MainMenu";

            }
        });
        newScene = "CreditsMenu";
        creditsLayout = new Group();
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext g = canvas.getGraphicsContext2D();
        Font txtFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/modern.ttf"),20);
        g.setFont(txtFont);

        g.strokeText("Thanks to ma boi Green for the math", 600, 40);
        g.strokeText("Adam, Gary, Leo ", 620, 100);

        creditsLayout.getChildren().add(canvas);






        creditsLayout.getStylesheets().add("resources/Garu.css");
        creditsLayout.getChildren().add(back);

        creditsScene = new Scene(creditsLayout, 1280, 720);
    }

//    public String run(){
//
//        back.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                newScene = "MainMenu";
//
//            }
//        });
//
//        creditsLayout.getStylesheets().add("resources/Garu.css");
//        creditsLayout.getChildren().add(back);
//    }

    public String run(){



        return newScene;

    }

    public void update(Group root){



    }
}

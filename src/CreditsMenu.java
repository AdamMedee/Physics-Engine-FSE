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
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CreditsMenu {

    public Image background = new Image("resources/images/GREEN.png");

    public Button back;
    public String newScene;
    public Scene creditsScene;
    public Group creditsLayout;

    private Environment bg;
    private Pane bgSim;

    public CreditsMenu(){
        newScene = "CreditsMenu";
        creditsLayout = new Group();
        creditsScene = new Scene(creditsLayout, 1280, 720);

        bgSim = new Pane();
        this.startSim();


        Text creditsTitle = new Text("Thank you for using this semi-functional Physics Engine!");
        creditsTitle.setX(350); creditsTitle.setY(300);
        creditsTitle.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        creditsTitle.setFill(Color.RED);
        Text contributors = new Text("Created by: Leo Chen, Adam Mehdi, and Gary Sun");
        contributors.setX(350);  contributors.setY(340);
        contributors.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        contributors.setFill(Color.RED);
        Text description1 = new Text("This is an attempt to simulate the laws that govern the universe.");
        description1.setX(350); description1.setY(380);
        description1.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        description1.setFill(Color.RED);

        int a = 0;
        int b = 0;

        Text description2 = new Text("It's so accurate that we even incorporated quantum tunneling :D");
        description2.setX(350); description2.setY(420);
        description2.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        description2.setFill(Color.RED);
        Text description3 = new Text("Also a thanks to dead mathmetician George Green for supplying us");
        description3.setX(350); description3.setY(460);
        description3.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        description3.setFill(Color.RED);
        Text description4 = new Text("with more than half of the math we used <3");
        description4.setX(350); description4.setY(500);
        description4.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        description4.setFill(Color.RED);


        back = new Button("Back");
        back.setLayoutX(20); back.setLayoutY(20);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                PhysicsEngine.mainMenu.startSim();
                newScene = "MainMenu";
            }
        });

        creditsLayout.getStylesheets().add("resources/Garu.css");
        creditsLayout.getChildren().addAll(back, creditsTitle, description1, description2, description3, description4, contributors);
    }

    public void startSim(){
        bg = new Environment();
        creditsLayout.getChildren().remove(bgSim);
        bgSim = new Pane();
        bg.setSimulationSpeed(0.05);
        bg.setScale(0.5);
        bg.setGravity(new Point2D(-0.56, 1.27));
        bg.creditsBG(bgSim);
        creditsLayout.getChildren().add(bgSim);
        bgSim.toBack();
    }

    public String run(){
        bg.run();
        return newScene;
    }

    public void update(){



    }
}

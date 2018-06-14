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
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CreditsMenu {
    public Button back;                 //Button to return to main menu
    public String newScene;          //Screen that will be displayed next frame
    public Scene creditsScene;      //Stores and displays all objects
    public Group creditsLayout;

    private Environment bg;           //The background of the screen
    private Pane bgSim;

    private Image adam = new Image("resources/images/Adam.png");
    private Image gary = new Image("resources/images/Gary.png");
    private Image leo = new Image("resources/images/Leo.png");
    private Image green = new Image("resources/images/GREEN.png");

    public CreditsMenu(){
        //Init scene and group
        newScene = "CreditsMenu";
        creditsLayout = new Group();
        creditsScene = new Scene(creditsLayout, 1280, 720);

        //Init background
        bgSim = new Pane();
        this.startSim();

        //Text for credits
        Text creditsTitle = new Text("L.A.G.'s Hall of Fame");
        creditsTitle.setX(500); creditsTitle.setY(250);
        creditsTitle.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),30));
        creditsTitle.setFill(Color.RED);

        ImageView iv1 = new ImageView();
        iv1.setImage(leo);
        iv1.setX(350); iv1.setY(300);

        ImageView iv2 = new ImageView();
        iv2.setImage(adam);
        iv2.setX(500); iv2.setY(300);

        ImageView iv3 = new ImageView();
        iv3.setImage(gary);
        iv3.setX(650); iv3.setY(300);

        ImageView iv4 = new ImageView();
        iv4.setImage(green);
        iv4.setX(800); iv4.setY(300);

        Rectangle cover = new Rectangle(500,300, 300,200);
        cover.setFill(Color.BLACK);

        Text leoCaption = new Text("Leo Chen");
        leoCaption.setX(350); leoCaption.setY(450);
        leoCaption.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        leoCaption.setFill(Color.RED);

        Text adamCaption = new Text("Adam Mehdi");
        adamCaption.setX(500); adamCaption.setY(450);
        adamCaption.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        adamCaption.setFill(Color.RED);

        Text garyCaption = new Text("Gary Sun");
        garyCaption.setX(650); garyCaption.setY(450);
        garyCaption.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        garyCaption.setFill(Color.RED);

        Text greenCaption = new Text("George Green");
        greenCaption.setX(800); greenCaption.setY(450);
        greenCaption.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),15));
        greenCaption.setFill(Color.RED);



        //Init back button
        back = new Button("Back");
        back.setLayoutX(20); back.setLayoutY(20);
        back.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),40));
        back.setMinSize(100, 50);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                PhysicsEngine.mainMenu.startSim();
                newScene = "MainMenu";
            }
        });

        //Add everything to the layout so it is displayed to the screen
        creditsLayout.getChildren().addAll(back, creditsTitle, cover, iv1, iv2, iv3, iv4, leoCaption, adamCaption, garyCaption, greenCaption);
    }

    //Init environment variables
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

    //Run the screen
    public String run(){
        bg.run();
        return newScene;
    }

    public void update(){



    }
}

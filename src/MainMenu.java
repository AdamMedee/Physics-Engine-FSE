/*
   MainMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Class for the main menu
   which is opened when the program
   first runs.

   Known Bugs:
   	everything
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainMenu {

    public Image background = new Image("resources/images/LEOO.png");


    // Graphics
    public Scene mainMenuScene;      //Shows all the objects on screen
    public Group mainMenuLayout;    //Group of all objects in the main menu
    public Button startBtn,creditsBtn;

    // Keeps track of switching between scenes
    public String newScene;             //Represents the screen to be shown next frame

    private Environment bg;             //The background simulation
    private Pane boxSim;

    //Constructor for the menu
    public MainMenu(){
        newScene = "MainMenu";
        mainMenuLayout = new Group();
        boxSim = new Pane();
        this.startSim();

        //Making and placing buttons
        startBtn = new Button("START");
        startBtn.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),40));
        startBtn.setMinSize(200, 100);  startBtn.setLayoutX(540); startBtn.setLayoutY(300);

        //Making and placing buttons
        creditsBtn = new Button("CREDITS");
        creditsBtn.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),30));
        creditsBtn.setMinSize(160, 80);  creditsBtn.setLayoutX(560); creditsBtn.setLayoutY(440);

        //Setting button actions
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PhysicsEngine.window.setScene(PhysicsEngine.systemMenu.systemScene);
                newScene = "SystemMenu";
            }
        });

        creditsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PhysicsEngine.window.setScene(PhysicsEngine.creditsMenu.creditsScene);
                PhysicsEngine.creditsMenu.startSim();
                newScene = "CreditsMenu";
            }
        });

        //Create the text for the title
        Text t = new Text();

        //Create a light source for shadow effect
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(10.0);
        lighting.setDiffuseConstant(0.67);
        lighting.setSpecularConstant(10);
        lighting.setSpecularExponent(40);

        //Back Shadow
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.3, 0.3, 0.3));
        dropShadow.setInput(lighting);

        //Small bloom effect
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.02);
        bloom.setInput(dropShadow);

        t.setX(70);
        t.setY(200);
        t.setText("L.A.G.'s Physics Engine");
        t.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),110));
        t.setFill(Color.GHOSTWHITE);
        t.setEffect(dropShadow);



        //Adds all the nodes to the layout
        mainMenuLayout.getChildren().add(t);
        mainMenuLayout.getChildren().add(startBtn);
        mainMenuLayout.getChildren().add(creditsBtn);

        //Makes a new scene with the constructed group
        mainMenuScene = new Scene(mainMenuLayout,1280,720);
    }

    //Initialize the background environment variables
    public void startSim(){
        bg = new Environment();
        mainMenuLayout.getChildren().remove(boxSim);
        boxSim = new Pane();
        bg.setSimulationSpeed(0.05);
        bg.setScale(0.5);
        bg.BackGroundMenu(boxSim);
        mainMenuLayout.getChildren().add(boxSim);
        boxSim.toBack();
    }

    //Goes through the actions inputted and acts accordingly
    public String run(){
        bg.run();
        return newScene;
    }

    //Displays menu to the screen
    public void update(){
    }
}

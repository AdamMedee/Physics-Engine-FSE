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
    public Scene mainMenuScene;
    public Group mainMenuLayout;
    public Button button1,button2;

    // Keeps track of switching between scenes
    public String newScene;

    private Environment bg;
    private Pane boxSim;

    //Constructor for the menu
    public MainMenu(){
        newScene = "MainMenu";
        mainMenuLayout = new Group();

       //mainMenuScene.getStylesheets().add("resources/Garu.css");
        boxSim = new Pane();
        this.startSim();


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
                PhysicsEngine.creditsMenu.startSim();
                newScene = "CreditsMenu";
            }
        });

        //Canvas and graphics context

        Canvas canvas = new Canvas(1280,720);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.drawImage(background, 640 - background.getWidth()/2, 360 - background.getHeight()/2);

        Text t = new Text();

        //Create a light source
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(10.0);
        lighting.setDiffuseConstant(0.67);
        lighting.setSpecularConstant(10);
        lighting.setSpecularExponent(40);

        //Shadow
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.3, 0.3, 0.3));
        dropShadow.setInput(lighting);

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
        mainMenuLayout.getChildren().add(canvas);
        mainMenuLayout.getChildren().add(t);
        mainMenuLayout.getChildren().add(button1);
        mainMenuLayout.getChildren().add(button2);

        //Makes a new scene with the constructed group
        mainMenuScene = new Scene(mainMenuLayout,1280,720);
    }

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


    //Displayes menu to the screen
    public void update(){
    }
}

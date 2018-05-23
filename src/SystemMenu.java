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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;

import java.awt.*;

public class SystemMenu {

    Environment environment; //The environment being used
    String selected; //System, Object, or Information are the three options

    //Shape graphics
    Rectangle BGrect;
    Rectangle BGborder;

    // Inputs we're keeping track of

    double OriginX,OriginY;
    double ScaleVal;
    double gravityVal;
    double sideForceVal;
    double speedVal;
    boolean running; //Whether the environment's been paused


    // Graphics Initialization
    public Scene systemScene;
    public BorderPane SystemLayout = new BorderPane();
    public Button systemB,objectB,back;

    private String newScene;


    //Constructor for the menu
    public SystemMenu(Environment environment){
        newScene = "SystemMenu";
        systemScene = new Scene(SystemLayout,1280,720);
        running = true;

        gravityVal = 9.81;
        sideForceVal = 0;
        speedVal = 1;

        environment.setGravity(new Point2D(sideForceVal, gravityVal));
        environment.setSimulationSpeed(speedVal);

        Pane leftPane = new Pane();

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        GridPane SystemPane = new GridPane();
        //SystemPane.setPrefSize();
        Pane ObjectPane = new Pane();

        Tab SystemTab = new Tab();
        SystemTab.setText("System");
        SystemTab.setStyle("-fx-pref-width: 125");


        Tab ObjectTab = new Tab();
        ObjectTab.setText("Objects");
        ObjectTab.setStyle("-fx-pref-width: 125");

        tabs.getTabs().addAll(SystemTab,ObjectTab);

        // ---------SystemPane Initialization
        SystemPane.setPadding(new Insets(10, 10, 10, 10));
        SystemPane.setVgap(30);
        SystemPane.setHgap(10);



        // ----- First row
        HBox OriginBox = new HBox();

        Label orgLabel = new Label("Origin");

        orgLabel.setPrefWidth(100);
        //orgLabel.setMaxWidth(60);

        Label xLabel = new Label("x:");
        xLabel.setMaxWidth(60);
        xLabel.setStyle("-fx-font-size: 15");

        TextField xInput = new TextField("0");
        xInput.setMaxWidth(60);

        Label blkLabel = new Label("");
        blkLabel.setStyle("-fx-pref-width: 40;");
        //blkLabel.setPrefWidth(20);


        Label yLabel = new Label("y:");
        yLabel.setMaxWidth(60);
        yLabel.setStyle("-fx-font-size: 15");


        TextField yInput = new TextField("0");

        yInput.setMaxWidth(60);
        OriginBox.getChildren().addAll(orgLabel,xLabel,xInput,blkLabel,yLabel,yInput);


        GridPane.setConstraints(OriginBox,0,0,2,1);

        SystemPane.getChildren().add(OriginBox);

        SystemTab.setContent(SystemPane);

        // --- -Second row

        Label scaleLabel = new Label("Scale:");
        scaleLabel.setPrefWidth(80);

        TextField scaleInput = new TextField();
        scaleInput.setPrefWidth(100);

        GridPane.setConstraints(scaleLabel,0,1);
        GridPane.setConstraints(scaleInput,1,1);

        SystemPane.getChildren().addAll(scaleLabel,scaleInput);
        //---------------------

        //-----Third Row (Gravity)---------

        Label gravityLabel = new Label("Ver. Gravity:");
        gravityLabel.setPrefWidth(80);

        TextField gravityInput = new TextField();
        gravityInput.setPrefWidth(100);

        GridPane.setConstraints(gravityLabel,0,2);
        GridPane.setConstraints(gravityInput,1,2);

        SystemPane.getChildren().addAll(gravityLabel,gravityInput);

        // -------------------------------

        //----- Fourth Row (SideForce)

        Label sideForceLabel = new Label("Hor. Gravity:");
        sideForceLabel.setPrefWidth(80);

        TextField sideForceInput = new TextField();
        sideForceInput.setPrefWidth(100);

        GridPane.setConstraints(sideForceLabel,0,3);
        GridPane.setConstraints(sideForceInput,1,3);

        SystemPane.getChildren().addAll(sideForceLabel,sideForceInput);

        //------Fifth Row (SimulationSpeed)-----

        Label speedLabel = new Label("Simulation Speed:");

        TextField speedInput = new TextField();
        speedInput.setPrefWidth(100);

        GridPane.setConstraints(speedLabel,0,4);
        GridPane.setConstraints(speedInput,1,4);

        SystemPane.getChildren().addAll(speedLabel,speedInput);

        // -------------------------------
        Label Disclaimer1 = new Label("                               *Lower = More Accurate");
        Disclaimer1.setPrefWidth(300);
        Disclaimer1.setStyle("-fx-font-size: 10;");

        GridPane.setConstraints(Disclaimer1,0,5,2,1);
        SystemPane.getChildren().add(Disclaimer1);
        // ------------------------

        //Three buttons at the bottom (run, reset, clear)
        HBox bottomrow = new HBox(30);

        //Pause button for the system
        Button runBtn  = new Button(" Run ");
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(running){
                    runBtn.setText("Pause");
                }
                else{
                    runBtn.setText(" Run ");
                }
                running = !running;
            }

        });

        //Puts all rigid bodies back to default settings
        //Also updates environment settings
        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Takes all the text box input for environment settings and applies them
                if (isDouble(xInput.getText())) OriginX = Double.parseDouble(xInput.getText());
                if (isDouble(yInput.getText())) OriginY = Double.parseDouble(yInput.getText());
                if (isDouble(scaleInput.getText())) ScaleVal = Double.parseDouble(scaleInput.getText());
                if (isDouble(sideForceInput.getText())) sideForceVal = Double.parseDouble(sideForceInput.getText());
                if (isDouble(gravityInput.getText())) gravityVal = Double.parseDouble(gravityInput.getText());
                if (isDouble(speedInput.getText())) speedVal = Double.parseDouble(speedInput.getText());

                //Updates environment
                environment.setGravity(new Point2D(sideForceVal, gravityVal));
                environment.setSimulationSpeed(speedVal);
                environment.setScale(ScaleVal);

                environment.reset();
            }
        });

        //Resets environment to default settings
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                xInput.setText("0");
                yInput.setText("0");
                scaleInput.setText("1");
                gravityInput.setText("1");
                sideForceInput.setText("0");
                speedInput.setText("1");
            }
        });
        bottomrow.getChildren().addAll(runBtn,resetBtn,clearBtn);

        GridPane.setConstraints(bottomrow,0,6,2,4);

        SystemPane.getChildren().add(bottomrow);






//
//        Label blkLabel2 = new Label();
//        speedLabel.setPrefWidth(100);
//
//        Label speedDisclaimer = new Label("*Lower = More Accurate");
//        speedDisclaimer.setStyle("-fx-font-size: 5;");
//
//        GridPane.setConstraints(blkLabel2,0,5);
//        GridPane.setConstraints(speedDisclaimer,1,5);
//
//        SystemPane.getChildren().addAll(blkLabel2,speedDisclaimer);
        //-------Final Buttons Row----------------
















        leftPane.setPrefSize(980,720);
        tabs.setPrefSize(300,720);

        //Environment being run
        this.environment = environment;
        this.environment.setGroup(leftPane);

        SystemLayout.setLeft(leftPane);

        //Buttons to change the selected varBGborderBGborder
        systemB = new Button("System");
        systemB.setOnAction(e -> { });
        systemB.setPrefSize(150, 30);
        systemB.setLayoutX(0);
        systemB.setLayoutY(0);


        objectB = new Button("Objects");
        objectB.setPrefSize(150, 30);
        objectB.setLayoutX(150);
        objectB.setLayoutY(0);




        back = new Button("Back");
        back.setLayoutX(20);
        back.setLayoutY(20);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                newScene = "MainMenu";
            }
        });

        // Adds nodes to group
//        SystemLayout.getChildren().add(systemB);
//        SystemLayout.getChildren().add(objectB);
        leftPane.getChildren().add(back);
        //rightPane.getChildren().addAll(systemB,objectB);

        SystemLayout.setRight(tabs);
    }

    public static boolean isDouble(String s)
    {
        try
        {
            double temp = Double.parseDouble(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    //Goes through the actions inputted and acts accordingly
    public String run(){
        if(running) {
            environment.run();
        }
        return newScene;
    }


    //Displayes menu to the screen
    public void update(){
        environment.update();
    }
}
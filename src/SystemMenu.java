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
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;

public class SystemMenu {

    Environment environment; //The environment being used
    String selected; //System, Object, or Information are the three options

    //Shape graphics
    Rectangle BGrect;
    Rectangle BGborder;

    //Buttons on the screen



    // Graphics Initialization
    public Scene systemScene;
    public Group SystemLayout;
    public Button systemB,objectB,back;

    private String newScene;


    //Constructor for the menu
    public SystemMenu(Environment environment){
        newScene = "SystemMenu";
        SystemLayout = new Group();
        systemScene = new Scene(SystemLayout,1280,720);

        //Environment being run
        this.environment = environment;
        this.environment.setGroup(SystemLayout);

        //Buttons to change the selected var
        systemB = new Button("System");
        systemB.setOnAction(e -> {

        });

        systemB.setPrefSize(150, 30);
        systemB.setLayoutX(980);
        systemB.setLayoutY(0);
        //systemB.setStyle();
        SystemLayout.getChildren().add(systemB);

        objectB = new Button("Objects");
        objectB.setPrefSize(150, 30);
        objectB.setLayoutX(1130);
        objectB.setLayoutY(0);
        SystemLayout.getChildren().add(objectB);

        //Adds graphics and buttons to root
        //Background and background border
        BGrect = new Rectangle(980, 28, 300, 692);
        BGrect.setFill(javafx.scene.paint.Color.rgb(100, 46, 0));
        //BGrect.setOpacity(0.3);
        BGborder = new Rectangle(979, 0, 2, 720);
        BGborder.setFill(javafx.scene.paint.Color.BLACK);
        SystemLayout.getChildren().add(BGrect);
        SystemLayout.getChildren().add(BGborder);

        back = new Button("Back");
        back.setLayoutX(20);
        back.setLayoutY(20);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                PhysicsEngine.currentScreen = "MainMenu";
                newScene = "MainMenu";
            }
        });

        SystemLayout.getChildren().add(back);
    }


    //Goes through the actions inputted and acts accordingly
    public String run(){
        environment.run();
        System.out.println(1);

        return newScene;
    }


    //Displayes menu to the screen
    public void update(){
        environment.update();
    }
}

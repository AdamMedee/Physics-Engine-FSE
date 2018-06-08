/*
   SelectMenu.java                  2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th

   Previous saved files are selected here
   along with the option to create a new
   file. Works with a page flipping system.

   Known Bugs:
   	everything
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class SelectMenu {

    private String newScene;
    public Scene selectScene;
    public BorderPane selectLayout = new BorderPane();
    private ScrollPane selectionUI = new ScrollPane();
    private Button back;

    public SelectMenu(){
        newScene = "SelectMenu";
        selectScene = new Scene(selectLayout, 1280, 720);


        // Button to go back to the main menu
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
        selectLayout.getChildren().add(back);

        //Gets information of environments from txt file
        try(BufferedReader file = new BufferedReader(new FileReader("resources//savefiles/savetxt.txt"))) {
            ArrayList<String> sb = new ArrayList<>();
            String line = file.readLine();
            while (line != null) {
                sb.add(line);
                line = file.readLine();
            }
        }
        catch(Exception e){}
        int environmentCount = 2;
/*
        //Goes through environments and makes buttons for each one in scroll fashion
        for (int i = 0; i < environmentCount; i++)
        {
            Pane temp = new Pane();
            temp.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");

            temp.setPrefSize(600,150);

            String enviroName = "Temp";
            int enviroBodyCount = 5;
            double enviroScale = 1;
            double enviroHorVel = 2;
            double enviroVerVel = 3;
            double enviroSimSpeed = 4;


            GridPane.setConstraints(temp,0,i*4,4,4);
            Label NameInfo = new Label(String.format("NAME: %s",enviroName));
            Label SidesInfo = new Label(String.format("RIGIDBODY COUNT: %d",enviroBodyCount));
            Button EditBtn = new Button("Edit");
            Button SelectBtn = new Button("Select");

            SelectBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PhysicsEngine.window.setScene(PhysicsEngine.systemMenu.systemScene);
                    newScene = "SystemMenu";
                }
            });



            GridPane.setConstraints(NameInfo,4,i*4);
            GridPane.setConstraints(SidesInfo,4,i*4+1);
            GridPane.setConstraints(EditBtn,4,i*4+3);

            temp.getChildren().addAll(temp,NameInfo,SidesInfo,EditBtn, SelectBtn);
            selectionUI.setContent(temp);
        }
*/

        selectLayout.setCenter(selectionUI);



    }

    public String run(){
        return newScene;
    }

    public void update()
    {


    }
}

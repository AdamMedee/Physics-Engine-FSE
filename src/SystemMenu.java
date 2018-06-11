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

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.util.ArrayList;


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


    //------
    //Scroll Pane----------
    ScrollPane objectsUI =  new ScrollPane();

    private String newScene;


    //Constructor for the menu
    public SystemMenu(Environment environment){
        newScene = "SystemMenu";
        systemScene = new Scene(SystemLayout,1280,720);
        running = true;

        gravityVal = 1;
        sideForceVal = 0;
        speedVal = 0.05;

        environment.setGravity(new Point2D(sideForceVal, gravityVal));
        environment.setSimulationSpeed(speedVal);

        Pane leftPane = new Pane();

        TabPane tabs = new TabPane();

        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        GridPane SystemPane = new GridPane();
        //SystemPane.setPrefSize();

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
        //xLabel.setStyle("-fx-font-size: 15");

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

        TextField scaleInput = new TextField("1");
        scaleInput.setPrefWidth(100);

        GridPane.setConstraints(scaleLabel,0,1);
        GridPane.setConstraints(scaleInput,1,1);

        SystemPane.getChildren().addAll(scaleLabel,scaleInput);
        //---------------------

        //-----Third Row (Gravity)---------

        Label gravityLabel = new Label("Ver. Gravity:");
        gravityLabel.setPrefWidth(80);

        TextField gravityInput = new TextField("1");
        gravityInput.setPrefWidth(100);

        GridPane.setConstraints(gravityLabel,0,2);
        GridPane.setConstraints(gravityInput,1,2);

        SystemPane.getChildren().addAll(gravityLabel,gravityInput);

        // -------------------------------

        //----- Fourth Row (SideForce)

        Label sideForceLabel = new Label("Hor. Gravity:");
        sideForceLabel.setPrefWidth(80);

        TextField sideForceInput = new TextField("0");
        sideForceInput.setPrefWidth(100);

        GridPane.setConstraints(sideForceLabel,0,3);
        GridPane.setConstraints(sideForceInput,1,3);

        SystemPane.getChildren().addAll(sideForceLabel,sideForceInput);

        //------Fifth Row (SimulationSpeed)-----

        Label speedLabel = new Label("Simulation Speed:");

        TextField speedInput = new TextField("0.05");
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


        // CheckBox for ratation

        CheckBox rotateCB = new CheckBox();
        rotateCB.setText("Rotation");
        rotateCB.setSelected(false);
        rotateCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(rotateCB.selectedProperty().get()){
                    environment.setRotate(true);
                }
                else{
                    environment.setRotate(false);
                }
            }
        });

        GridPane.setConstraints(rotateCB,0,5);

        SystemPane.getChildren().add(rotateCB);

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

                environment.reset(true);
            }
        });

        //Resets environment to default settings
        Button clearBtn = new Button("Apply");
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (isDouble(xInput.getText())) OriginX = Double.parseDouble(xInput.getText());
                if (isDouble(yInput.getText())) OriginY = Double.parseDouble(yInput.getText());
                if (isDouble(scaleInput.getText())) ScaleVal = Double.parseDouble(scaleInput.getText());
                if (isDouble(sideForceInput.getText())) sideForceVal = Double.parseDouble(sideForceInput.getText());
                if (isDouble(gravityInput.getText())) gravityVal = Double.parseDouble(gravityInput.getText());
                if (isDouble(speedInput.getText())) speedVal = Double.parseDouble(speedInput.getText());

                //Updates environment
                environment.setGravity(new Point2D(sideForceVal, gravityVal));
                environment.setSimulationSpeed(speedVal);
                environment.setScale(ScaleVal == 0 ? Integer.MAX_VALUE : 1/ScaleVal);

                environment.reset(false);
            }
        });
        bottomrow.getChildren().addAll(runBtn,resetBtn,clearBtn);

        GridPane.setConstraints(bottomrow,0,6,2,4);

        SystemPane.getChildren().add(bottomrow);

        leftPane.setPrefSize(900,720);
        leftPane.setMaxWidth(980);

        tabs.setPrefSize(300,720);

        //Environment being run
        this.environment = environment;
        this.environment.setGroup(leftPane);

        leftPane.toBack();


        //ObjectUI Initialization
        objectsUI.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        objectsUI.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

        GridPane objectPane = new GridPane();
        objectPane.setPadding(new Insets(10,10,10,10));
        objectPane.setVgap(8);
        objectPane.setVgap(10);




        //--- Iterating through every rigid body



//        for (int i = 0;i<1;i++)
        // Buttons will be affected by fixed object. Only the bottom few would right now.
        // Uncomment the for loop on top and the comment out the for loop at the bottom to test the object customization page

        // --- Create new object
        Menu createObject = new Menu("New");
        MenuItem newRigidBody = new MenuItem("Rigid Body");
        newRigidBody.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newObjectWindow = new Stage();
                newObjectWindow.setTitle("Create a New Object");

                GridPane createPane = new GridPane();
                createPane.setPadding(new Insets(10,10,10,10));

                Pane selectionPane = new Pane();
                selectionPane.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");
                selectionPane.setPrefSize(400,400);


                ArrayList<Double> x = new ArrayList<Double>();
                ArrayList<Double> y = new ArrayList<Double>();

                ArrayList<Double> prevX = new ArrayList<Double>();
                ArrayList<Double> prevY = new ArrayList<Double>();

                ArrayList<Line> lines = new ArrayList<Line>();
                //---------------Information-------------
                Label currentPoint = new Label("Current Point");
                Label xLabel = new Label("X:");

                TextField xInput = new TextField();
                Label yLabel = new Label("Y:");
                TextField yInput = new TextField();


                Button clickMe = new Button("Print Lines(BUG TESTING)");
                clickMe.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        for (Line tmp : lines)
                        {
                            System.out.println(tmp);
                        }
                    }
                });
                //---------------------------------

                Scene createScene = new Scene(createPane,400,720);

                selectionPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        createScene.setCursor(Cursor.CROSSHAIR);

                        selectionPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                Double oldX = mouseEvent.getX();
                                Double oldY = mouseEvent.getY();

                                prevX.add(oldX);
                                prevY.add(oldY);

                                Double newX = oldX*2.45;
                                Double newY = oldY*1.8;

                                x.add(newX);
                                y.add(newY);


                                xInput.setText(String.format("%.2f",newX));
                                yInput.setText(String.format("%.2f",newY));

                                Circle tmpPoint = new Circle(1,Color.BLACK);
                                tmpPoint.setLayoutX(oldX);
                                tmpPoint.setLayoutY(oldY);

                                int n = x.size();
                                if (n>=2)
                                {
                                    if (n>3)
                                    {
                                        selectionPane.getChildren().remove(lines.get(lines.size()-1));
                                        lines.remove(lines.size()-1);

                                        Line tmpline = new Line(prevX.get(n-1),prevY.get(n-1),prevX.get(0),prevY.get(0));
                                        lines.add(tmpline);
                                        selectionPane.getChildren().add(tmpline);
                                    }

                                    Line line = new Line(prevX.get(n-2),prevY.get(n-2),prevX.get(n-1),prevY.get(n-1));
                                    lines.add(line);
                                    selectionPane.getChildren().add(line);
                                    if (n==3)
                                    {
                                        Line tmpline = new Line(prevX.get(0),prevY.get(0),prevX.get(n-1),prevY.get(n-1));
                                        lines.add(tmpline);
                                        selectionPane.getChildren().add(tmpline);

                                    }


                                }

                                selectionPane.getChildren().add(tmpPoint);



                            }
                        });
                    }
                });

                selectionPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        createScene.setCursor(Cursor.DEFAULT);
                    }
                });

                GridPane.setConstraints(selectionPane,0,0,10,10);
                GridPane.setConstraints(currentPoint,0,10);
                GridPane.setConstraints(xLabel,0,12);
                GridPane.setConstraints(xInput,2,12);
                GridPane.setConstraints(yLabel,0,14);
                GridPane.setConstraints(yInput,2,14);
                GridPane.setConstraints(clickMe,0,16);


                createPane.getChildren().addAll(selectionPane,currentPoint,xLabel,xInput,yLabel,yInput,clickMe);

                newObjectWindow.setScene(createScene);

                newObjectWindow.show();

            }
        });
        MenuItem newCircle = new MenuItem("Circle");
        newCircle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        createObject.getItems().addAll(newRigidBody,newCircle);

        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(createObject);

        GridPane.setConstraints(menubar,0,0);

        objectPane.getChildren().add(menubar);

        for (int i = 0;i<environment.rigidBodies.size();i++)
        {
            if(environment.rigidBodies.get(i) instanceof CircleBody){
                continue;
            }
            Pane temp = new Pane();
            temp.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");

            // Garu is only a shallow copy. Changes made to Garu will affect the original object.
            // DeepGaru is a deep copy. Changes made to DeepGaru will not affect the original object
            RigidBody Garu = environment.getRigidBodies().get(i);
            RigidBody DeepGaru = Garu.copy(temp,Garu.getColour());
            Point2D size = DeepGaru.getSize();
            Point2D min = DeepGaru.getMin();
            DeepGaru.setScale(Math.max(size.getX()/100, size.getY()/100));
            DeepGaru.translate((-size.getX()/2-min.getX()), ((-size.getY()/2-min.getY())));
            DeepGaru.translate(64*DeepGaru.getScale(), 64*DeepGaru.getScale());



            temp.setPrefSize(128,128);




            GridPane.setConstraints(temp,0,i*4+1,4,4);
            Label MassInfo = new Label(String.format("Mass: %f",Garu.getMass()));
            Label SidesInfo = new Label(String.format("Number of sides: %d",Garu.getSides()));
            Label CMInfo = new Label(String.format("X: %.2f\nY: %.2f",Garu.getCenter().getX(),Garu.getCenter().getY()));

            Button EditBtn = new Button("Edit");

            EditBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(running) runBtn.fire();
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Edit");
                    GridPane EditPane = new GridPane();
                    EditPane.setPadding(new Insets(10,10,10,10));
                    EditPane.setVgap(8);
                    EditPane.setVgap(10);

                    Label massLabel = new Label("Mass");
                    TextField massInput = new TextField(String.format("%.2f",Garu.getMass()));

                    //Where the shape currently is
                    Label CMLabel = new Label("Current Center of Mass:");

                    Label CMLabelx = new Label("X:");
                    TextField CMxInput = new TextField(String.format("%.2f",Garu.getCenter().getX()));

                    Label CMLabely = new Label("Y:");
                    TextField CMyInput = new TextField(String.format("%.2f",Garu.getCenter().getY()));

                    //Where the shape starts
                    Label SCMLabel = new Label("Starting Center of Mass:");

                    Label SCMLabelx = new Label("X:");
                    TextField SCMxInput = new TextField(String.format("%.2f",Garu.getStartCenter().getX()));

                    Label SCMLabely = new Label("Y:");
                    TextField SCMyInput = new TextField(String.format("%.2f",Garu.getStartCenter().getY()));

                    //How bouncy the shape is
                    Label RestLabel = new Label("Restitution:");
                    TextField RestInput = new TextField(String.format("%.3f",Garu.getRestitution()));

                    //For editing shape colour
                    ColorPicker colorPicker = new ColorPicker();
                    colorPicker.setValue(Garu.colour);
                    colorPicker.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                        }
                    });

                    Button ApplyBtn = new Button("Apply");
                    ApplyBtn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            // Only half done here
                            Garu.colour = colorPicker.getValue();
                            Garu.removeShape();
                            RigidBody tmp = new RigidBody(Garu.getXPoints(), Garu.getYPoints(), Garu.getMass(), Garu.getFixed(),leftPane, Garu.getColour());

                            tmp.setSerialNum(Garu.getSerialNum());

                            environment.rigidBodies.set(Garu.getSerialNum(),tmp);

                            //---- Updating colour in the selection menu for objects
                            Pane newPane = new Pane();
                            newPane.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");
                            newPane.setPrefSize(128,128);


                            RigidBody Deeptmp = tmp.copy(newPane,tmp.colour);
                            Point2D size0 = Deeptmp.getSize();
                            Point2D min0 = Deeptmp.getMin();
                            Deeptmp.setScale(Math.max(size0.getX()/100, size0.getY()/100));
                            Deeptmp.translate((-size0.getX()/2-min0.getX()), ((-size0.getY()/2-min0.getY())));
                            Deeptmp.translate(64*Deeptmp.getScale(), 64*Deeptmp.getScale());


                            GridPane.setConstraints(newPane,0,tmp.getSerialNum()*4,4,4);



                            objectPane.getChildren().remove(getNodeByRowColumnIndex(tmp.getSerialNum()*4,0,objectPane));

                            objectPane.getChildren().add(newPane);

                            System.out.println(tmp.getSerialNum());
                            System.out.println(tmp.getColour());
                            runBtn.fire();

//                            if (isDouble(massInput.getText())) Garu.setMass(Double.parseDouble(massInput.getText()));
//                            if (isDouble(xInput.getText()))  Garu.translate(Double.parseDouble(xInput.getText())-Garu.getCenter().getX(),0);
//                            if (isDouble(yInput.getText()))  Garu.translate(Double.parseDouble(yInput.getText())-Garu.getCenter().getY(),0);

                            //Edits mass, current and start position, colour, and restitution
                            if (isDouble(massInput.getText())) Garu.setMass(Double.parseDouble(massInput.getText()));
                            if (isDouble(CMxInput.getText()))  Garu.translate(Double.parseDouble(CMxInput.getText())-Garu.getCenter().getX(),0);
                            if (isDouble(CMyInput.getText()))  Garu.translate(0,Double.parseDouble(CMyInput.getText())-Garu.getCenter().getY());
                            if (isDouble(SCMxInput.getText())) Garu.setStartCenter(Double.parseDouble(SCMxInput.getText()), Garu.getStartCenter().getY());
                            if (isDouble(SCMyInput.getText())) Garu.setStartCenter(Garu.getStartCenter().getX(), Double.parseDouble(SCMyInput.getText()));
                            if (isDouble(RestInput.getText())) Garu.setRestitution(Double.parseDouble(RestInput.getText()));
                            MassInfo.setText(String.format("Mass: %f",Garu.getMass()));


//                            int n = Garu.SerialNumber;
//                            Label MassInfo = new Label(String.format("Mass: %f",Garu.getMass()));
//                            Label SidesInfo = new Label(String.format("Number of sides: %d",Garu.getSides()));
//                            Label CMInfo = new Label(String.format("X: %.2f\nY: %.2f",Garu.getCenter().getX(),Garu.getCenter().getY()));
//
//                            GridPane.setConstraints(MassInfo,4,n*4);
//                            GridPane.setConstraints(SidesInfo,4,n*4+1);
//                            GridPane.setConstraints(CMInfo,4,n*4+2);
//                            GridPane.setConstraints(EditBtn,4,n*4+3);

                            newWindow.close();
                        }
                    });

                    GridPane.setConstraints(massLabel,0,0);
                    GridPane.setConstraints(massInput,1,0);
                    GridPane.setConstraints(CMLabel,0,3);
                    GridPane.setConstraints(CMLabelx,0,4);
                    GridPane.setConstraints(CMxInput,1,4);
                    GridPane.setConstraints(CMLabely,0,5);
                    GridPane.setConstraints(CMyInput,1,5);

                    GridPane.setConstraints(SCMLabel,0,8);
                    GridPane.setConstraints(SCMLabelx,0,9);
                    GridPane.setConstraints(SCMxInput,1,9);
                    GridPane.setConstraints(SCMLabely,0,10);
                    GridPane.setConstraints(SCMyInput,1,10);

                    GridPane.setConstraints(RestLabel,0,13);
                    GridPane.setConstraints(RestInput,1,13);

                    GridPane.setConstraints(colorPicker,0,15);
                    GridPane.setConstraints(ApplyBtn,0,17);

                    EditPane.getChildren().addAll(massLabel,massInput,CMLabel,CMLabelx,CMxInput,CMLabely,CMyInput,colorPicker,ApplyBtn, SCMLabel, SCMLabelx, SCMLabely, SCMxInput, SCMyInput, RestLabel, RestInput);


                    Scene editScene = new Scene(EditPane,400,720);
                    newWindow.setScene(editScene);

                    newWindow.show();
                }
            });
            GridPane.setConstraints(MassInfo,4,i*4+1);
            GridPane.setConstraints(SidesInfo,4,i*4+2);
            GridPane.setConstraints(CMInfo,4,i*4+3);
            GridPane.setConstraints(EditBtn,4,i*4+4);

            objectPane.getChildren().addAll(temp,MassInfo,SidesInfo,CMInfo,EditBtn);
        }




        objectsUI.setContent(objectPane);

        ObjectTab.setContent(objectsUI);

        SystemLayout.setLeft(leftPane);





        back = new Button("Back");
        back.setLayoutX(20);
        back.setLayoutY(20);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                PhysicsEngine.mainMenu.startSim();
                newScene = "MainMenu";
            }
        });

        // Adds nodes to group

        leftPane.getChildren().add(back);
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


    public static Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    //Displayes menu to the screen
    public void update(){
        environment.update();
    }
}
/*
   SystemMenu.java                 2018 May 7th
   Adam Mehdi, Gary Sun, Leo Chen   2018 May 7th
    Ryan was also here

   This is where the user will be
   playing around with the physics
   engine, will run a single environment
   and allow user input.

 */

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Optional;


public class SystemMenu {


    Environment environment; //The environment being used

    // Inputs we're keeping track of
    double ScaleVal;               //Scale of simulation
    double gravityVal;            //Verical gravity
    double sideForceVal;        //Horizontal gravity
    double speedVal;              //Simulation speed
    boolean running;               //Whether the environment's been paused


    // Graphics Initialization
    public Scene systemScene;
    public BorderPane SystemLayout = new BorderPane();
    public Button systemB,objectB,back;
    public Pane leftPane;
    public GridPane objectPane;
    public GridPane SystemPane;

    //Images
    private Image adam = new Image("resources/images/ADAMM.png");
    private Image leo = new Image("resources/images/LEOO.png");
    private Image gary = new Image("resources/images/GARU.png");
    private Image green = new Image("resources/images/GREENN.jpg");


    //------
    //Scroll Pane----------
    ScrollPane objectsUI =  new ScrollPane();
    ScrollPane systemUI = new ScrollPane();

    Tab SystemTab;              //Where the environment variables can be modified

    //Buttons
    Button runBtn;

    private String newScene;    //The next scene to be displayed
    private int updateWait;     //How many frames until the next systemTab update


    //Constructor for the menu
    public SystemMenu(Environment environment){
        //Init environment variables
        newScene = "SystemMenu";
        systemScene = new Scene(SystemLayout,1280,720);
        running = true;
        gravityVal = 1;
        sideForceVal = 0;
        speedVal = 0.2;

        environment.setGravity(new Point2D(sideForceVal, gravityVal));
        environment.setSimulationSpeed(speedVal);

        leftPane = new Pane();
        SystemPane = new GridPane();

        //Tab system to switch between objects and systems
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        SystemTab = new Tab();
        SystemTab.setText("System");
        SystemTab.setStyle("-fx-pref-width: 125;");

        Tab ObjectTab = new Tab();
        ObjectTab.setText("Objects");
        ObjectTab.setStyle("-fx-pref-width: 125");

        tabs.getTabs().addAll(SystemTab, ObjectTab);

        leftPane.setPrefSize(900,720);
        leftPane.setMaxWidth(980);

        tabs.setPrefSize(300,720);

        //Environment being run
        this.environment = environment;

        leftPane.toBack();

        //ObjectUI Initialization
        objectsUI.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        objectsUI.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        systemUI.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        systemUI.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);


        objectPane = new GridPane();
        objectPane.setPadding(new Insets(10,10,10,10));
        objectPane.setVgap(8);
        objectPane.setVgap(10);


        //Create and show elements
        createBodyInfoBoxes();
        createBackButton();

        createSystemMenu();

        objectsUI.setContent(objectPane);
        systemUI.setContent(SystemPane);

        ObjectTab.setContent(objectsUI);
        SystemTab.setContent(systemUI);

        SystemLayout.setLeft(leftPane);
        SystemLayout.setRight(tabs);

        updateWait = 30;    //Default wait time for updating the object pane
    }

    //Creates the system editing menu
    private void createSystemMenu(){

        // ---------SystemPane Initialization
        SystemPane.setPadding(new Insets(10, 10, 10, 10));
        SystemPane.setVgap(30);
        SystemPane.setHgap(10);
        SystemTab.setContent(SystemPane);


        // --- -First row (Scale)

        Label scaleLabel = new Label("Scale:");
        scaleLabel.setPrefWidth(80);

        TextField scaleInput = new TextField("1");
        scaleInput.setPrefWidth(100);

        GridPane.setConstraints(scaleLabel,0,1);
        GridPane.setConstraints(scaleInput,1,1);

        SystemPane.getChildren().addAll(scaleLabel,scaleInput);
        //---------------------

        //-----Second Row (Gravity)---------

        Label gravityLabel = new Label("Ver. Gravity:");
        gravityLabel.setPrefWidth(80);

        TextField gravityInput = new TextField("1");
        gravityInput.setPrefWidth(100);

        GridPane.setConstraints(gravityLabel,0,2);
        GridPane.setConstraints(gravityInput,1,2);

        SystemPane.getChildren().addAll(gravityLabel,gravityInput);

        // -------------------------------

        //----- Third Row (SideForce)

        Label sideForceLabel = new Label("Hor. Gravity:");
        sideForceLabel.setPrefWidth(80);

        TextField sideForceInput = new TextField("0");
        sideForceInput.setPrefWidth(100);

        GridPane.setConstraints(sideForceLabel,0,3);
        GridPane.setConstraints(sideForceInput,1,3);

        SystemPane.getChildren().addAll(sideForceLabel,sideForceInput);

        //------Fourth Row (SimulationSpeed)-----

        Label speedLabel = new Label("Simulation Speed:");

        TextField speedInput = new TextField("0.2");
        speedInput.setPrefWidth(100);

        GridPane.setConstraints(speedLabel,0,4);
        GridPane.setConstraints(speedInput,1,4);

        SystemPane.getChildren().addAll(speedLabel,speedInput);

        // ------------------------


        // CheckBox for rotation
        createRotationBox();

        //Three buttons at the bottom (run, reset, clear)
        HBox bottomrow = new HBox(30);

        //Pause button for the system
        runBtn  = new Button(" Run ");  //Pause/Run button
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Toggle between running and not running
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

                //Warns the user about possible lag
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setHeaderText("If multiple objects have the same starting position, the simulation may lag. Are you sure you want to reset?");

                ImageView alertPic = new ImageView(adam);
                alertPic.setFitHeight(100);
                alertPic.setFitWidth(100);
                alert.setGraphic(alertPic);


                ButtonType OKBtn = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
                ButtonType CancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(OKBtn,CancelBtn);

                Optional<ButtonType> result = alert.showAndWait();  //Gets which button is pressed
                //Define what the button presses do
                if (result.get() == OKBtn)
                {
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
                else if (result.get() == CancelBtn)
                {
                    alert.close();
                }
            }
        });

        //Resets environment to default settings
        Button applyBtn = new Button("Apply");
        applyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Get the new variables and apply them accordingly
                if (isDouble(scaleInput.getText())) ScaleVal = Double.parseDouble(scaleInput.getText());
                if (isDouble(sideForceInput.getText())) sideForceVal = Double.parseDouble(sideForceInput.getText());
                if (isDouble(gravityInput.getText())) gravityVal = Double.parseDouble(gravityInput.getText());
                if (isDouble(speedInput.getText())) speedVal = Double.parseDouble(speedInput.getText());

                //Updates environment
                environment.setGravity(new Point2D(sideForceVal, gravityVal));
                environment.setSimulationSpeed(speedVal);
                environment.setScale(ScaleVal);

                environment.reset(false);
            }
        });
        bottomrow.getChildren().addAll(runBtn,resetBtn,applyBtn);   //Add buttons to group

        GridPane.setConstraints(bottomrow,0,6,2,4);

        SystemPane.getChildren().add(bottomrow);

        createClear(); //Create the clear button
    }



    //Empties the environments of all rigidbodies
    private void createClear(){
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Warn the user about the action
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setHeaderText("Are you sure you want to clear?\nThere is no way of undoing this action!");

                ImageView alertPic = new ImageView(adam);
                alertPic.setFitHeight(100);
                alertPic.setFitWidth(100);
                alert.setGraphic(alertPic);


                ButtonType OKBtn = new ButtonType("Clear", ButtonBar.ButtonData.OK_DONE);
                ButtonType CancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(OKBtn,CancelBtn);

                Optional<ButtonType> result = alert.showAndWait();
                //Gets the result and applies effects
                if (result.get() == OKBtn)
                {
                    //Remove rigidBody from environment and re-creates the side bar
                    for(RigidBody body : environment.rigidBodies){
                        body.removeShape();
                    }
                    environment.rigidBodies.clear();
                    createBodyInfoBoxes();
                }
                else if (result.get() == CancelBtn)
                {
                    alert.close();
                }

            }
        });
        GridPane.setConstraints(clearBtn,1,0);
        objectPane.getChildren().add(clearBtn);
    }



    //Creates the object info pane for a given rigidbody with index "i"
    private void createBodyPane(int i){
        Pane temp = new Pane();
        temp.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");

        // RigidBodyTmp is only a shallow copy. Changes made to RigidBodyTmp will affect the original object.
        // Deeptmp is a deep copy. Changes made to Deeptmp will not affect the original object
        RigidBody RigidBodyTmp = environment.getRigidBodies().get(i);
        RigidBody Deeptmp = RigidBodyTmp.copy(temp,RigidBodyTmp.getColour());
        Point2D size = Deeptmp.getSize();
        Point2D min = Deeptmp.getMin();
        Deeptmp.setScale(Math.max(size.getX()/100, size.getY()/100));
        Deeptmp.translate((-size.getX()/2-min.getX()), ((-size.getY()/2-min.getY())));
        Deeptmp.translate(64*Deeptmp.getScale(), 64*Deeptmp.getScale());



        temp.setPrefSize(128,128);


        //Create labels for textBoxes
        GridPane.setConstraints(temp,0,i*4+1,4,4);
        Label MassInfo = new Label(String.format("Mass: %f",RigidBodyTmp.getMass()));
        Label SidesInfo = new Label(String.format("Number of sides: %d",RigidBodyTmp.getSides()));
        Label CMInfo = new Label(String.format("X: %.2f\nY: %.2f",RigidBodyTmp.getCenter().getX(),RigidBodyTmp.getCenter().getY()));


        Button DeleteBtn = new Button("Delete");    //Init the deleteBtn
        DeleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(running) {runBtn.fire();}    //Pause the simulation
                //Warn the user about the action
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setHeaderText("Are you sure you want to delete this object?");

                ImageView alertPic = new ImageView(leo);
                alertPic.setFitHeight(100);
                alertPic.setFitWidth(100);
                alert.setGraphic(alertPic);


                ButtonType OKBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                ButtonType CancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(OKBtn,CancelBtn);

                Optional<ButtonType> result = alert.showAndWait();
                //Gets the user decison and executes accordingly
                if (result.get() == OKBtn)
                {
                    RigidBodyTmp.removeShape();
                    environment.rigidBodies.remove(RigidBodyTmp);
                    for(int i = 0; i < environment.rigidBodies.size(); i++){
                        environment.rigidBodies.get(i).setSerialNum(i);
                    }
                    createBodyInfoBoxes();
                    runBtn.fire();
                }
                else if (result.get() == CancelBtn) { alert.close(); }
            }
        });

        //Defines the edit rigidbody window
        Button EditBtn = new Button("Edit");
        EditBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(running) runBtn.fire();      //Pause the simulation while editing
                //Create a new window for the editing window
                Stage newWindow = new Stage();
                newWindow.setTitle("Edit");
                GridPane EditPane = new GridPane();
                EditPane.setPadding(new Insets(10,10,10,10));
                EditPane.setVgap(8);
                EditPane.setVgap(10);


                //Create all the labels and textBoxes for the rigidBody
                Label massLabel = new Label("Mass");
                TextField massInput = new TextField(String.format("%.2f",RigidBodyTmp.getMass()));

                //Where the shape currently is
                Label CMLabel = new Label("Current Center of Mass:");

                Label CMLabelx = new Label("X:");
                TextField CMxInput = new TextField(String.format("%.2f",RigidBodyTmp.getCenter().getX()));

                Label CMLabely = new Label("Y:");
                TextField CMyInput = new TextField(String.format("%.2f",RigidBodyTmp.getCenter().getY()));

                //Where the shape starts
                Label SCMLabel = new Label("Starting Center of Mass:");

                Label SCMLabelx = new Label("X:");
                TextField SCMxInput = new TextField(String.format("%.2f",RigidBodyTmp.getStartCenter().getX()));

                Label SCMLabely = new Label("Y:");
                TextField SCMyInput = new TextField(String.format("%.2f",RigidBodyTmp.getStartCenter().getY()));

                //How bouncy the shape is
                Label RestLabel = new Label("Restitution:");
                TextField RestInput = new TextField(String.format("%.3f",RigidBodyTmp.getRestitution()));

                //For editing shape colour
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setValue(RigidBodyTmp.colour);  // Show the color of

                Button ApplyBtn = new Button("Apply");

                ApplyBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //Get the new values and apply them to the rigidbody
                        RigidBodyTmp.colour = colorPicker.getValue();
                        //Edits mass, current and start position, colour, and restitution
                        RigidBody tmp = new RigidBody(RigidBodyTmp.getXPoints(), RigidBodyTmp.getYPoints(), RigidBodyTmp.getMass(), RigidBodyTmp.getFixed(),leftPane, RigidBodyTmp.getColour());

                        if (isDouble(massInput.getText())) tmp.setMass(Double.parseDouble(massInput.getText()));
                        if (isDouble(CMxInput.getText()))  tmp.translate(Double.parseDouble(CMxInput.getText())-tmp.getCenter().getX(),0);
                        if (isDouble(CMyInput.getText()))  tmp.translate(0,Double.parseDouble(CMyInput.getText())-tmp.getCenter().getY());
                        if (isDouble(SCMxInput.getText())) tmp.setStartCenter(Double.parseDouble(SCMxInput.getText()), tmp.getStartCenter().getY());
                        if (isDouble(SCMyInput.getText())) tmp.setStartCenter(tmp.getStartCenter().getX(), Double.parseDouble(SCMyInput.getText()));
                        if (isDouble(RestInput.getText())) tmp.setRestitution(Double.parseDouble(RestInput.getText()));


                        RigidBodyTmp.removeShape(); // Remove the old shape of the rigidbody from the pane

                        environment.rigidBodies.set(RigidBodyTmp.getSerialNum(),tmp); //Replace the old rigidbody to the new rigidbody that the user made

                        tmp.setSerialNum(RigidBodyTmp.getSerialNum());//Gets the index of the rigidbody in arraylist

                        objectPane.getChildren().removeAll(temp,MassInfo,SidesInfo,CMInfo,EditBtn, DeleteBtn);

                        createBodyPane(tmp.getSerialNum());

                        newWindow.close();

                        runBtn.fire(); // Resume the simulation
                    }
                });

                //Assign position to labels relative to the grid pane
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

                newWindow.show();   //Show the window on screen
            }
        });

        //Assign positions
        GridPane.setConstraints(DeleteBtn,5,i*4+4);

        GridPane.setConstraints(MassInfo,4,i*4+1);
        GridPane.setConstraints(SidesInfo,4,i*4+2);
        GridPane.setConstraints(CMInfo,4,i*4+3);
        GridPane.setConstraints(EditBtn,4,i*4+4);

        DeleteBtn.setTranslateX(-40);

        objectPane.getChildren().addAll(temp,MassInfo,SidesInfo,CMInfo,EditBtn, DeleteBtn); //Add all buttons to the group
    }


    //Create the list of info boxes on the right
    public  void createBodyInfoBoxes(){
        //Clear the objectPane and re-add rigidBodies to the objectPane in order to update positions
        objectPane.getChildren().clear();
        addShape();
        createClear();
        for (int i = 0; i<environment.rigidBodies.size(); i++)
        {
            createBodyPane(i);
        }
    }

    //Check box and warning screen to turn on W.I.P. rotation
    private void createRotationBox(){
        CheckBox rotateCB = new CheckBox();
        rotateCB.setText("Rotation");
        rotateCB.setSelected(false);    //By default no rotation in the simulation
        rotateCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                rotateCB.setSelected(!rotateCB.isSelected());
                // This ensures that the checkbox won't change state while the dialog is opened


                if (!rotateCB.isSelected())
                {
                    //Warns the user since rotation is not completely functional
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Are you sure?");
                    alert.setHeaderText("Are you sure you want to add rotation?");

                    ImageView alertPic = new ImageView(this.getClass().getResource("resources/images/GARU.png").toString());
                    alertPic.setFitHeight(100);
                    alertPic.setFitWidth(100);
                    alert.setGraphic(alertPic);

                    ButtonType OKBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    ButtonType CancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(OKBtn,CancelBtn);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == OKBtn)
                    {
                        rotateCB.setSelected(!rotateCB.isSelected());
                        environment.setRotate(true);
                    }
                    else if (result.get() == CancelBtn)
                    {
                        alert.close();
                        rotateCB.setSelected(false);
                    }
                }

                else // If switching off rotation
                {
                    rotateCB.setSelected(false);
                    environment.setRotate(false);

                }
            }
        });
        GridPane.setConstraints(rotateCB,0,5);
        SystemPane.getChildren().add(rotateCB);
    }

    //Button to go back to the main menu
    private void createBackButton(){
        back = new Button("Back");
        back.setLayoutX(15);
        back.setLayoutY(10);
        back.setFont(Font.loadFont(getClass().getResourceAsStream("resources/fonts/GiantRobotArmy-Medium.ttf"),16));
        back.setMinSize(50, 30);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Are you sure?");
                alert.setHeaderText("Is it really in your best interest to leave the beautiful world of physics??\nYour simulation will NOT be saved!");

                ImageView alertPic = new ImageView(green);
                alertPic.setFitHeight(100);
                alertPic.setFitWidth(100);
                alert.setGraphic(alertPic);

                ButtonType OKBtn = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType CancelBtn = new ButtonType("No I was just kidding", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(OKBtn,CancelBtn);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == OKBtn)
                {
                    PhysicsEngine.window.setScene(PhysicsEngine.mainMenu.mainMenuScene);
                    PhysicsEngine.mainMenu.startSim();
                    newScene = "MainMenu";
                }
                else if (result.get() == CancelBtn)
                {
                    alert.close();
                }


            }
        });
        // Adds nodes to group
        leftPane.getChildren().add(back);
    }

    //Allows you to create and add a circle or rigidbody to the environment
    private void addShape(){
        // --- Create new object

        Menu createObject = new Menu("New");
        MenuItem newRigidBody = new MenuItem("Rigid Body");
        newRigidBody.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newObjectWindow = new Stage();
                // Creates a new window
                newObjectWindow.setResizable(false);
                newObjectWindow.setTitle("Create a New Object");

                GridPane createPane = new GridPane();
                createPane.setPadding(new Insets(10,10,10,10));

                createPane.setVgap(8);
                createPane.setHgap(10);

                Pane selectionPane = new Pane();
                selectionPane.setStyle("-fx-border-color: black;-fx-border-insets: 10,10,10,10;");
                selectionPane.setPrefSize(400,400);


                ArrayList<Double> x = new ArrayList<Double>();
                // x-coordinates of the point on the simulation pane
                ArrayList<Double> y = new ArrayList<Double>();
                // y-coordinates of the point on the simulation pane


                ArrayList<Double> prevX = new ArrayList<Double>();
                // x-coordinates of the point relative to the point selection pane

                ArrayList<Double> prevY = new ArrayList<Double>();
                // y-coordinates of the point relative to the point selection pane


                ArrayList<Line> lines = new ArrayList<Line>();
                //Stores the lines that the user has created

                ArrayList<Circle> points = new ArrayList<Circle>();
                //Points that the user clicked

                ArrayList<Circle> highlights = new ArrayList<Circle>(1);
                // Telling the user what point is selected



                //---------------Information && Buttons && Text Entries-------------
                ComboBox comboBox = new ComboBox(); //Allows user to choose what point is selected
                comboBox.setPrefWidth(200);


                HBox hbox = new HBox();             //Stores the comboBox
                hbox.getChildren().add(comboBox);


                Label xLabel = new Label("X:");

                TextField xInput = new TextField();
                TextField yInput = new TextField();

                Label yLabel = new Label("Y:");


                Label massLbl = new Label("Mass:");
                TextField massInput = new TextField("1");


                CheckBox fixed = new CheckBox("Fixed");
                fixed.setSelected(false);   //Default fixed value

                ColorPicker colorPicker = new ColorPicker();    //Create a colorPicker
                colorPicker.setValue(Color.BLACK);              //Set default value
                colorPicker.setPrefWidth(60);


                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (x.size() == 0)
                        {
                            return;
                        }
                        int index = getPointIndex(comboBox,comboBox.getValue());
                        if (index == -1)
                        {
                            return;
                        }

                        if (highlights.size()==1)
                        {
                            selectionPane.getChildren().remove(highlights.get(0));
                            highlights.remove(0);
                        }

                        Circle hltPoint = new Circle(6,Color.YELLOW);
                        hltPoint.setLayoutX(prevX.get(index));
                        hltPoint.setLayoutY(prevY.get(index));

                        highlights.add(hltPoint);
                        selectionPane.getChildren().add(hltPoint);

                        xInput.setText(String.format("%.2f",x.get(index)));
                        yInput.setText(String.format("%.2f",y.get(index)));
                    }
                });

                Button deletePoint = new Button("Delete Point");
                deletePoint.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                       try
                       {
                           int index = getPointIndex(comboBox,comboBox.getValue());

                           selectionPane.getChildren().remove(points.get(index));
                           // Remove the point from the pane

                           points.remove(index);

                           x.remove(index);
                           y.remove(index);

                           prevX.remove(index);
                           prevY.remove(index);

                           selectionPane.getChildren().remove(highlights.get(0));

                           highlights.remove(0);


                           if (x.size()== 0) // If there are no points left
                           {

                               comboBox.getItems().remove(index);
                               comboBox.setValue("");

                               return;
                           }
                           else if (x.size()==1) // If there is one point left
                           {
                               comboBox.getItems().remove(index);
                               comboBox.setValue(comboBox.getItems().get(comboBox.getItems().size()-1));

                               selectionPane.getChildren().remove(lines.get(0));
                               lines.remove(0);

                               return;
                           }
                           else
                           {
                               if (index == 0)
                               {
                                   selectionPane.getChildren().remove(0);
                                   selectionPane.getChildren().remove(lines.get(lines.size()-1));

                                   lines.remove(0);
                                   lines.remove(lines.size()-1);

                                   if (comboBox.getItems().size()>0) // If there is a point allowed to be selected
                                   {
                                       comboBox.getItems().remove(index);
                                       if (comboBox.getItems().size()!=0) {
                                           comboBox.setValue(comboBox.getItems().get(comboBox.getItems().size() - 1));
                                       }
                                       else
                                       {
                                           comboBox.setValue("");
                                       }
                                   }


                                   if (x.size()!=2)
                                   {


                                       Line tmpLine = new Line(prevX.get(0),prevY.get(0),prevX.get(prevX.size()-1),prevY.get(prevY.size()-1));

                                       selectionPane.getChildren().add(tmpLine);
                                   }
                                   return;

                               }
                               else
                               {

                                   selectionPane.getChildren().remove(lines.get(index-1));
                                   selectionPane.getChildren().remove(lines.get(index));


                                   lines.remove(index-1);
                                   //lines.remove(index-1);

                                   if (x.size()==2)
                                   {
                                       comboBox.getItems().remove(index);
                                       comboBox.setValue(comboBox.getItems().get(comboBox.getItems().size()-1));

                                       return;
                                   }

                                   Line tmpLine = new Line(prevX.get(index-1),prevY.get(index-1), prevX.get(index%x.size()),prevY.get(index%x.size()));
                                   selectionPane.getChildren().add(tmpLine);
                                   lines.add(index-1,tmpLine);

                               }

                           }


                           comboBox.getItems().remove(index);
                           comboBox.setValue(comboBox.getItems().get(comboBox.getItems().size()-1));

                       }
                       catch (IndexOutOfBoundsException ex)
                       {
                           Alert alert = new Alert(Alert.AlertType.WARNING);
                           alert.setTitle("Error");
                           alert.setHeaderText("Delete operation cannot be completed!");
                           alert.setContentText("We recommend you to reconstruct your object");
                           alert.show();
                           return;

                       }
                       }

                });

                Button AddBtn = new Button("Add");

                AddBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //Stores the points of the to-be-created polygon

                        double [] tempX =  new double[x.size()];
                        double [] tempY =  new double[x.size()];

                        for (int i=0;i<x.size();i++)
                        {
                            //Gets the coordinates
                            tempX[i] = x.get(i);
                            tempY[i] = y.get(i);
                        }

                        double mass;
                        try
                        {
                            mass = Double.parseDouble(massInput.getText());
                            //Tries to get mass
                        }
                        catch(Exception ex)
                        {
                            //Shows alert if mass cant be processed
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("Mass input is invalid!");
                            alert.show();
                            return;
                        }
                        //Add the new rigidBody to the system
                        environment.rigidBodies.add(new RigidBody(tempX,tempY,mass,fixed.isSelected(),leftPane,colorPicker.getValue()));
                        createBodyPane(environment.rigidBodies.size()-1);
                    }
                });

                //---------------------------------

                Scene createScene = new Scene(createPane,400,720);  //Holds canvas for selecting points

                selectionPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    // When the user is in the pane ready to add a point
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        createScene.setCursor(Cursor.CROSSHAIR);
                        selectionPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                //Gets mouse position
                                Double oldX = mouseEvent.getX();
                                Double oldY = mouseEvent.getY();

                                //oldX and oldY are positions of x and y relative to the pane
                                prevX.add(oldX);
                                prevY.add(oldY);

                                //Accounts for aspect ratio
                                Double newX = oldX*2.45;
                                Double newY = oldY*1.8;

                                // newX and newY are positions on the physics simulation pane
                                x.add(newX);
                                y.add(newY);


                                xInput.setText(String.format("%.2f",newX));
                                yInput.setText(String.format("%.2f",newY));

                                //Center of mass for the new polygon
                                Circle tmpPoint = new Circle(1,Color.BLACK);
                                tmpPoint.setLayoutX(oldX);
                                tmpPoint.setLayoutY(oldY);

                                // Showing the new point the user added
                                int n = x.size();

                                comboBox.getItems().add(String.format("Point %d",n));
                                comboBox.setValue(comboBox.getItems().get(n-1));


                                if (n>=2)
                                {
                                    if (n>3)
                                    {
                                        selectionPane.getChildren().remove(lines.get(lines.size()-1));
                                        lines.remove(lines.size()-1);
                                    }

                                    Line line = new Line(prevX.get(n-2),prevY.get(n-2),prevX.get(n-1),prevY.get(n-1));
                                    // Added a line between the current last point and the new point added
                                    lines.add(line);
                                    selectionPane.getChildren().add(line);

                                    // ---- Order matters if you're wondering why I split the two if-statements apart
                                    if (n>3)
                                    {
                                        Line tmpline = new Line(prevX.get(n-1),prevY.get(n-1),prevX.get(0),prevY.get(0));
                                        lines.add(tmpline);
                                        selectionPane.getChildren().add(tmpline);
                                    }

                                    if (n==3)
                                    {
                                        Line tmpline = new Line(prevX.get(n-1),prevY.get(n-1),prevX.get(0),prevY.get(0));
                                        lines.add(tmpline);
                                        selectionPane.getChildren().add(tmpline);

                                    }
                                }

                                selectionPane.getChildren().add(tmpPoint);
                                points.add(tmpPoint);
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

                // Setting the position of each component
                GridPane.setConstraints(selectionPane,0,0,10,10);
                GridPane.setConstraints(hbox,0,10);
                GridPane.setConstraints(xLabel,0,12,4,4);
                GridPane.setConstraints(xInput,4,12,4,4);
                GridPane.setConstraints(yLabel,0,16,4,4);
                GridPane.setConstraints(yInput,4,16,4,4);
                GridPane.setConstraints(deletePoint,0,20,4,4);
                GridPane.setConstraints(massLbl,0,24,4,4);
                GridPane.setConstraints(massInput,4,24,4,4);
                GridPane.setConstraints(fixed,0,28,4,4);
                GridPane.setConstraints(colorPicker,4,28,4,4);
                GridPane.setConstraints(AddBtn,0,32,4,4);


                //Adds all labels into the pane
                createPane.getChildren().addAll(selectionPane,hbox,xLabel,xInput,yLabel,yInput,fixed,massLbl,massInput,colorPicker,AddBtn,deletePoint);

                newObjectWindow.setScene(createScene);

                newObjectWindow.show();
                //Show the window

            }
        });
        MenuItem newCircle = new MenuItem("Circle");    //Add a circle to the simulation
        newCircle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newObjectWindow = new Stage();       //New window for circle creation
                newObjectWindow.setTitle("Create a New Object");

                GridPane createPane = new GridPane();
                createPane.setPadding(new Insets(10,10,10,10));
                createPane.setVgap(20);


                //Create the labels and text boxes
                Label xLabel = new Label("X:");
                TextField xInput = new TextField();

                Label yLabel = new Label("Y:");
                TextField yInput = new TextField();

                Label radLabel = new Label("Radius:");
                TextField radInput = new TextField();

                CheckBox fixed = new CheckBox("Fixed");
                fixed.setSelected(false);

                Label massLbl = new Label("Mass:");
                TextField massInput = new TextField("1");

                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setValue(Color.BLACK);
                colorPicker.setPrefWidth(60);

                Button AddBtn = new Button("Add");
                AddBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //Init default values
                        double x = 0;
                        double y = 0;
                        double radius = 0;
                        if (isDouble(xInput.getText())) x = Double.parseDouble(xInput.getText());
                        if (isDouble(yInput.getText())) y = Double.parseDouble(yInput.getText());
                        if (isDouble(radInput.getText())) radius = Double.parseDouble(radInput.getText());

                        double[] xTmp = new double[40]; //Circle approx with 40 sided polygons
                        double[] yTmp = new double[40];

                        for(int i=0; i<40; i++){
                            //Roots of unity coords
                            xTmp[i] = x + radius * Math.cos(2 * i * Math.PI / 40);
                            yTmp[i] = y + radius * Math.sin(2 * i * Math.PI / 40);
                        }

                        double mass;
                        try {
                            mass = Double.parseDouble(massInput.getText());
                        } catch (Exception ex) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("Mass input is invalid!");
                            alert.show();
                            return;
                        }

                        RigidBody tmp = new RigidBody(xTmp, yTmp, mass, fixed.isSelected(), leftPane, colorPicker.getValue());
                        tmp.setSerialNum(environment.rigidBodies.size());
                        environment.rigidBodies.add(tmp);
                        createBodyPane(environment.rigidBodies.size() - 1);

                    }
                });


                // Setting the position of each component
                GridPane.setConstraints(xLabel,0,2,4,4);
                GridPane.setConstraints(xInput,4,2,4,4);
                GridPane.setConstraints(yLabel,0,4,4,4);
                GridPane.setConstraints(yInput,4,4,4,4);
                GridPane.setConstraints(radLabel,0,6,4,4);
                GridPane.setConstraints(radInput,4,6,4,4);
                GridPane.setConstraints(massLbl,0,8,4,4);
                GridPane.setConstraints(massInput,4,8,4,4);
                GridPane.setConstraints(fixed,0,10,4,4);
                GridPane.setConstraints(colorPicker,0,12,4,4);
                GridPane.setConstraints(AddBtn,0,16,4,4);



                createPane.getChildren().addAll(xLabel,xInput,yLabel,yInput,radLabel,radInput,fixed,AddBtn, colorPicker, massInput, massLbl);


                Scene createScene = new Scene(createPane,400,500);
                newObjectWindow.setScene(createScene);
                newObjectWindow.show();
            }
        });
        createObject.getItems().addAll(newRigidBody,newCircle); //Add the creation buttons

        //Add the menu into the window
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(createObject);

        GridPane.setConstraints(menubar,0,0);

        objectPane.getChildren().add(menubar);
    }

    public void removeOffscreen(){
        //Removes rigidBodies from the simulation if it goes ofscreen
        for(int i=environment.rigidBodies.size()-1; i>=0; i--){
            RigidBody check = environment.rigidBodies.get(i);
            if(check.getPolygon().getBoundsInLocal().getMaxX() < 0 || check.getPolygon().getBoundsInLocal().getMinX() > 1280  || check.getPolygon().getBoundsInLocal().getMaxY() < 0  || check.getPolygon().getBoundsInLocal().getMinY() > 720 ){
                //Check if the object is completely ofscreen
                check.removeShape();
                environment.rigidBodies.remove(check);  //Remove shape from list and scene
                for(int j = 0; j < environment.rigidBodies.size(); j++){
                    environment.rigidBodies.get(j).setSerialNum(j);
                }
                createBodyInfoBoxes();  //Re-draw the info boxes
            }
        }
    }


    //Checks if a given string can be converted to a double
    public static boolean isDouble(String s)
    {
        //Checks to see if a string can be interpreted as a double
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
            environment.run();  //Update polygons
            removeOffscreen();  //Remove Polygons off screen
            if(updateWait-- <= 0){
                createBodyInfoBoxes();  //Update InfoBoxes every half second
                updateWait = 30;
            }
        }
        return newScene;
    }


    public static int getPointIndex (ComboBox temp,Object s)
    {
        //Gets the index of a point in the comboBox
        int index = -1; // If value is not found

        for (int i=0;i<temp.getItems().size();i++)
        {
            if (temp.getItems().get(i) == s)
            {
                index = i;
            }
        }
        return index;
    }

}
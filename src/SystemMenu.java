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
import javafx.scene.shape.Rectangle;
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

    Tab SystemTab;

    //Buttons
    Button runBtn;

    private String newScene;
    private int updateWait;


    //Constructor for the menu
    public SystemMenu(Environment environment){
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
        SystemTab.setStyle("-fx-pref-width: 125");

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

        createBodyInfoBoxes();
        createBackButton();

        createSystemMenu();

        objectsUI.setContent(objectPane);
        systemUI.setContent(SystemPane);

        ObjectTab.setContent(objectsUI);
        SystemTab.setContent(systemUI);

        SystemLayout.setLeft(leftPane);
        SystemLayout.setRight(tabs);

        updateWait = 60;
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
        runBtn  = new Button(" Run ");
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

                Optional<ButtonType> result = alert.showAndWait();
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
        bottomrow.getChildren().addAll(runBtn,resetBtn,applyBtn);

        GridPane.setConstraints(bottomrow,0,6,2,4);

        SystemPane.getChildren().add(bottomrow);

        createClear();
    }



    //Empties the environments of all rigidbodies
    private void createClear(){
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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
                if (result.get() == OKBtn)
                {
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


        Button DeleteBtn = new Button("Delete");
        //Defines the edit rigidbody window
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
                        Garu.colour = colorPicker.getValue();
                        //Edits mass, current and start position, colour, and restitution
                        RigidBody tmp = new RigidBody(Garu.getXPoints(), Garu.getYPoints(), Garu.getMass(), Garu.getFixed(),leftPane, Garu.getColour());

                        if (isDouble(massInput.getText())) tmp.setMass(Double.parseDouble(massInput.getText()));
                        if (isDouble(CMxInput.getText()))  tmp.translate(Double.parseDouble(CMxInput.getText())-tmp.getCenter().getX(),0);
                        if (isDouble(CMyInput.getText()))  tmp.translate(0,Double.parseDouble(CMyInput.getText())-tmp.getCenter().getY());
                        if (isDouble(SCMxInput.getText())) tmp.setStartCenter(Double.parseDouble(SCMxInput.getText()), tmp.getStartCenter().getY());
                        if (isDouble(SCMyInput.getText())) tmp.setStartCenter(tmp.getStartCenter().getX(), Double.parseDouble(SCMyInput.getText()));
                        if (isDouble(RestInput.getText())) tmp.setRestitution(Double.parseDouble(RestInput.getText()));

                        Garu.removeShape();
                        environment.rigidBodies.set(Garu.getSerialNum(),tmp);
                        tmp.setSerialNum(Garu.getSerialNum());
                        objectPane.getChildren().removeAll(temp,MassInfo,SidesInfo,CMInfo,EditBtn, DeleteBtn);
                        createBodyPane(tmp.getSerialNum());
                        newWindow.close();
                        runBtn.fire();
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


        //Deleting a selected rigidbody
        DeleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(running) {runBtn.fire();}
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
                if (result.get() == OKBtn)
                {
                    Garu.removeShape();
                    environment.rigidBodies.remove(Garu);
                    for(int i = 0; i < environment.rigidBodies.size(); i++){
                        environment.rigidBodies.get(i).setSerialNum(i);
                    }
                    createBodyInfoBoxes();
                    runBtn.fire();
                }
                else if (result.get() == CancelBtn) { alert.close(); }
            }
        });
        GridPane.setConstraints(DeleteBtn,5,i*4+4);

        GridPane.setConstraints(MassInfo,4,i*4+1);
        GridPane.setConstraints(SidesInfo,4,i*4+2);
        GridPane.setConstraints(CMInfo,4,i*4+3);
        GridPane.setConstraints(EditBtn,4,i*4+4);

        DeleteBtn.setTranslateX(-40);

        objectPane.getChildren().addAll(temp,MassInfo,SidesInfo,CMInfo,EditBtn, DeleteBtn);
    }

    //Opens a screen to edit a given rigidbody


    //Create the list of info boxes on the right
    public  void createBodyInfoBoxes(){
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
        rotateCB.setSelected(false);
        rotateCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                rotateCB.setSelected(!rotateCB.isSelected());
                // This ensures that the checkbox won't change state while the dialog is opened


                if (!rotateCB.isSelected())
                {
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
                ArrayList<Double> y = new ArrayList<Double>();

                ArrayList<Double> prevX = new ArrayList<Double>();
                ArrayList<Double> prevY = new ArrayList<Double>();

                ArrayList<Line> lines = new ArrayList<Line>();

                ArrayList<Circle> points = new ArrayList<Circle>();

                ArrayList<Circle> highlights = new ArrayList<Circle>(1);



                //---------------Information-------------
                ComboBox comboBox = new ComboBox();
                comboBox.setPrefWidth(200);


                HBox hbox = new HBox();
                hbox.getChildren().add(comboBox);


                Label xLabel = new Label("X:");

                TextField xInput = new TextField();
                Label yLabel = new Label("Y:");
                TextField yInput = new TextField();

                Label massLbl = new Label("Mass:");
                TextField massInput = new TextField("1");


                CheckBox fixed = new CheckBox("Fixed");
                fixed.setSelected(false);

                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setValue(Color.BLACK);
                colorPicker.setPrefWidth(60);


                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (x.size() == 0)
                        {
                            return;
                        }
                        int index = -1; // Initialize to zero b/c java
                        for (int i=0;i<comboBox.getItems().size();i++)
                        {
                            if (comboBox.getItems().get(i)==comboBox.getValue())
                            {
                                index = i;
                            }
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
                        int index = getPointIndex(comboBox,comboBox.getValue());




                        selectionPane.getChildren().remove(points.get(index));

                        points.remove(index);

                        x.remove(index);
                        y.remove(index);

                        prevX.remove(index);
                        prevY.remove(index);

                        selectionPane.getChildren().remove(highlights.get(0));

                        highlights.remove(0);


                        if (x.size()== 0)
                        {

                            comboBox.getItems().remove(index);
                            comboBox.setValue("");

                            return;
                        }
                        else if (x.size()==1)
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

                                if (x.size()!=2)
                                {

                                    Line tmpLine = new Line(prevX.get(0),prevY.get(0),prevX.get(lines.size()-1),prevY.get(lines.size()-1));
                                    selectionPane.getChildren().add(tmpLine);
                                }
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

                });

                Button clickMe = new Button("Add");

                clickMe.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        double [] tempX =  new double[x.size()];
                        double [] tempY =  new double[x.size()];

                        for (int i=0;i<x.size();i++)
                        {
                            tempX[i] = x.get(i);
                            tempY[i] = y.get(i);
                        }

                        double mass;
                        try
                        {
                            mass = Double.parseDouble(massInput.getText());
                        }
                        catch(Exception ex)
                        {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("WARNING!");
                            alert.setHeaderText("Mass input is invalid!");
                            alert.show();
                            return;
                        }
                        RigidBody tmp = new RigidBody(tempX,tempY,mass,fixed.isSelected(),leftPane,colorPicker.getValue());
                        tmp.setSerialNum(environment.rigidBodies.size());
                        environment.rigidBodies.add(tmp);
                        createBodyPane(environment.rigidBodies.size()-1);
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
                GridPane.setConstraints(clickMe,0,32,4,4);



                createPane.getChildren().addAll(selectionPane,hbox,xLabel,xInput,yLabel,yInput,fixed,massLbl,massInput,colorPicker,clickMe,deletePoint);

                newObjectWindow.setScene(createScene);

                newObjectWindow.show();

            }
        });
        MenuItem newCircle = new MenuItem("Circle");
        newCircle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newObjectWindow = new Stage();
                newObjectWindow.setTitle("Create a New Object");

                GridPane createPane = new GridPane();
                createPane.setPadding(new Insets(10,10,10,10));
                createPane.setVgap(20);



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

                Button clickMe = new Button("Add");
                clickMe.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        double x = -999;
                        double y = -999;
                        double radius = 1;
                        if (isDouble(xInput.getText())) x = Double.parseDouble(xInput.getText());
                        if (isDouble(yInput.getText())) y = Double.parseDouble(yInput.getText());
                        if (isDouble(radInput.getText())) radius = Double.parseDouble(radInput.getText());

                        double[] xTmp = new double[40];
                        double[] yTmp = new double[40];

                        for (int i = 0; i < 40; i++) {
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
                GridPane.setConstraints(clickMe,0,16,4,4);



                createPane.getChildren().addAll(xLabel,xInput,yLabel,yInput,radLabel,radInput,fixed,clickMe, colorPicker, massInput, massLbl);


                Scene createScene = new Scene(createPane,400,500);
                newObjectWindow.setScene(createScene);
                newObjectWindow.show();
            }
        });
        createObject.getItems().addAll(newRigidBody,newCircle);

        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(createObject);

        GridPane.setConstraints(menubar,0,0);

        objectPane.getChildren().add(menubar);
    }

    public void removeOffscreen(){
        for(int i=environment.rigidBodies.size()-1; i>=0; i--){
            RigidBody check = environment.rigidBodies.get(i);
            if(check.getPolygon().getBoundsInLocal().getMaxX() < 0 || check.getPolygon().getBoundsInLocal().getMinX() > 1280  || check.getPolygon().getBoundsInLocal().getMaxY() < 0  || check.getPolygon().getBoundsInLocal().getMinY() > 720 ){
                check.removeShape();
                environment.rigidBodies.remove(check);
                for(int j = 0; j < environment.rigidBodies.size(); j++){
                    environment.rigidBodies.get(j).setSerialNum(j);
                }
                createBodyInfoBoxes();
            }
        }
    }


    //Checks if a given string can be converted to a double
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
            removeOffscreen();
            if(updateWait-- <= 0){
                createBodyInfoBoxes();
                updateWait = 30;
            }
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

    public static int getPointIndex (ComboBox temp,Object s)
    {
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
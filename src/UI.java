import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Created by Deviltech on 13.12.2015.
 */
public class UI extends Application{

    // for rotation
    double originX;
    double originY;

    // contains shapes
    Group drawRoot;

    PerspectiveCamera camera;

    // indicates if shift key is pressed
    private boolean isShiftPressed;


    @Override
    public void start(Stage primaryStage) throws Exception {

        drawRoot = new Group();

        // UI elements
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(vBox, 800, 800);

        SubScene drawSubScene = new SubScene(drawRoot, 800, 700,true, SceneAntialiasing.BALANCED);

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");

        MenuItem openItem = new MenuItem("Open");

        MenuItem clearItem = new MenuItem("Clear");

        camera = new PerspectiveCamera(true);

        // for camera and rotation
        scene.setOnMousePressed(sceneOnMousePressedEventHandler);
        scene.setOnMouseDragged(sceneOnMouseDraggedEventHandler);

        // scene key pressed
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                isShiftPressed = true;
            }
        });

        // scene key released
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                isShiftPressed = false;
            }
        });



        // Set open Menu handler
        openItem.setOnAction((value) -> {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter
            ArrayList<String> filters = new ArrayList<String>();
            filters.add("*.pdb");
            filters.add("*.ent");
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("PDB files (*.pdb), ENT files (*.ent)", filters);
            fileChooser.getExtensionFilters().add(extFilter);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                   ArrayList<Atom> myAtoms =  new PDB_Reader().readInFile(file);
                   setAtomCoordinates(myAtoms, drawRoot);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            }
        );

        // Set clearHandler
        clearItem.setOnAction((value) -> {
            drawRoot.getChildren().clear();
        });


        // Prepace scene
        menuFile.getItems().addAll(openItem, clearItem);
        menuBar.getMenus().addAll(menuFile);

        stackPane.getChildren().addAll(drawSubScene);

        vBox.getChildren().addAll(menuBar, stackPane);


        // set camera properties
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-100);

        drawSubScene.setCamera(camera);


        primaryStage.setScene(scene);
        primaryStage.setTitle("3D Viewer");

        // show scene
        primaryStage.show();
    }




    /**
     * Sets the Atom Coordinates according ro bdp file Atoms
     * @param myAtoms
     * @return
     */
    private void setAtomCoordinates(ArrayList<Atom> myAtoms, Group myGroup){

        // List of all Residues
        ArrayList<Residue> myResidues = new ArrayList<>();

        // Current Residue Index for while loop
        int currentResidueIndex = myAtoms.get(0).getAtomResidueIndex();
        Residue currentResidue = constructResidueWithType(myAtoms.get(0));

        for(int i = 0; i < myAtoms.size(); i++){

            // As long as the atom belongs to the same residue, add it to current residue
            while(i < myAtoms.size() -1 && currentResidueIndex == myAtoms.get(i).getAtomResidueIndex()){
                currentResidue.addAtom(myAtoms.get(i));
                i++;
            }

            // save completed residue
            myResidues.add(currentResidue);

            if(i < myAtoms.size()){
                // generate new Residue based on the Residue type
                currentResidue = constructResidueWithType(myAtoms.get(i));
                currentResidue.addAtom(myAtoms.get(i));
                currentResidueIndex = myAtoms.get(i).getAtomResidueIndex();
            }
        }

        // material blue
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        // material orange
        PhongMaterial orangeMaterial = new PhongMaterial();
        orangeMaterial.setDiffuseColor(Color.ORANGE);
        orangeMaterial.setSpecularColor(Color.YELLOW);

        // material black
        PhongMaterial blackMaterial = new PhongMaterial();
        blackMaterial.setDiffuseColor(Color.BLACK);
        blackMaterial.setSpecularColor(Color.DARKGRAY);


        // iterate over residues and draw them
        for (Residue myResidue: myResidues) {
            // generate base
            MeshView currentBaseView = new MeshView(myResidue.generateBaseMesh());
            currentBaseView.setMaterial(redMaterial);
            currentBaseView.setDrawMode(DrawMode.FILL);
            //generate sugar
            MeshView currentSugarView = new MeshView(myResidue.generateSugarMesh());
            currentSugarView.setMaterial(orangeMaterial);
            currentSugarView.setDrawMode(DrawMode.FILL);
            Shape3D currentPhosphorus = new Sphere();
            // handle phosphorus
            if(myResidue.generatePhosphorusMesh(currentPhosphorus)){
                myGroup.getChildren().add(currentPhosphorus);
            }
            // generate line
            Shape3D currentLine = myResidue.generateLine();
            currentLine.setMaterial(blackMaterial);

            myGroup.getChildren().addAll(currentBaseView, currentSugarView, currentLine);


        }

        centerGroup(drawRoot);



    }


    /**
     * generate Residue according to char
     * @param a
     * @return
     */
    private Residue constructResidueWithType(Atom a){

        switch (Character.toLowerCase(a.getAtomResidue())){
            case 'a': return new Purine();
            case 'u': return new Pyrimidine();
            case 'g': return new Purine();
            case 'c': return new Pyrimidine();
            default: return null;
        }
    }

    /**
     * Center Group elements
     * @param g
     */
    private void centerGroup(Group g){
        ObservableList<Node> nodes = g.getChildren();
        double meanX = 0;
        double meanY = 0;
        double meanZ = 0;

        for(Node currentNode: nodes){
            meanX += currentNode.getTranslateX();
            meanY += currentNode.getTranslateY();
            meanZ += currentNode.getTranslateZ();
        }
        meanX = meanX/nodes.size();
        meanY = meanY/nodes.size();
        meanZ = meanZ/nodes.size();


        for(Node currentNode: g.getChildren()){
            currentNode.setTranslateX(currentNode.getTranslateX()- meanX);
            currentNode.setTranslateY(currentNode.getTranslateY()- meanY);
            currentNode.setTranslateZ(currentNode.getTranslateZ()- meanZ);
        }



    }

    /**
     * Eventhandler for mouse pressed for circle drag
     */
    EventHandler<MouseEvent> sceneOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    // set origin coordinates
                    originX = t.getSceneX();
                    originY = t.getSceneY();
                }




            };

    /**
     * Eventhandler for mouse follow on drag
     */
    EventHandler<MouseEvent> sceneOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    // calculate offset
                    double offsetX = t.getSceneX() - originX;
                    double offsetY = t.getSceneY() - originY;
                    if (isShiftPressed) {
                        // zoom while shift pressed
                        camera.setTranslateZ(camera.getTranslateZ() + (offsetX + offsetY) / 2);
                    } else {
                        // follow mouse
                        drawRoot.getTransforms().add(new Rotate(offsetX, 0, 0, 0, Rotate.Y_AXIS));
                        drawRoot.getTransforms().add(new Rotate(offsetY, 0, 0, 0, Rotate.Z_AXIS));
                    }

                    originX += offsetX;
                    originY += offsetY;


                }


            };


}

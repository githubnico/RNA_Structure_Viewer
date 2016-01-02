package Viewer;

import Residues.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Created by Deviltech on 13.12.2015.
 */
public class UI extends Application {

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

        // Viewer.UI elements
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(vBox, 800, 800);
        Text text = new Text(myValues.NO_FILE_SELECTED);

        SubScene drawSubScene = new SubScene(drawRoot, 800, 700, true, SceneAntialiasing.BALANCED);
        drawSubScene.heightProperty().bind(scene.heightProperty());
        drawSubScene.widthProperty().bind(scene.widthProperty());


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
                            ArrayList<Atom> myAtoms = new PDB_Reader().readInFile(file);
                            setAtomCoordinates(myAtoms, drawRoot);
                            text.setText(file.getName());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
        );

        // Set clearHandler
        clearItem.setOnAction((value) -> {
            drawRoot.getChildren().clear();
            text.setText(myValues.NO_FILE_SELECTED);
        });


        // Prepace scene
        menuFile.getItems().addAll(openItem, clearItem);
        menuBar.getMenus().addAll(menuFile);

        stackPane.getChildren().addAll(drawSubScene, text);
        stackPane.setAlignment(text, Pos.BOTTOM_LEFT);
        stackPane.setMargin(text, new Insets(30));


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
     * Sets the Residues.Atom Coordinates according ro bdp file Atoms
     *
     * @param myAtoms
     * @return
     */
    private void setAtomCoordinates(ArrayList<Atom> myAtoms, Group myGroup) {

        // List of all Residues
        ArrayList<AResidue> myResidues = new ArrayList<>();

        // Current Residue Index for while loop
        int currentResidueIndex = myAtoms.get(0).getAtomResidueIndex();
        AResidue currentResidue = constructResidueWithType(myAtoms.get(0));

        for (int i = 0; i < myAtoms.size(); i++) {

            // As long as the atom belongs to the same residue, add it to current residue
            while (i < myAtoms.size() - 1 && currentResidueIndex == myAtoms.get(i).getAtomResidueIndex()) {
                currentResidue.addAtom(myAtoms.get(i));
                i++;
            }

            // save completed residue
            myResidues.add(currentResidue);

            if (i < myAtoms.size()) {
                // generate new Residue based on the Residue type
                currentResidue = constructResidueWithType(myAtoms.get(i));
                currentResidue.addAtom(myAtoms.get(i));
                currentResidueIndex = myAtoms.get(i).getAtomResidueIndex();
            }
        }


        // used for lines between phosphorus
        int residueIndex = -1;
        Atom oldAtom = new Atom();

        // iterate over residues and draw them
        for (AResidue myResidue : myResidues) {

            // draw lines between phosphorus
            if (residueIndex == -1 && (myResidue.getAtom("P") != null)) {
                // initialize, if first residue of file
                oldAtom = myResidue.getAtom("P");
                residueIndex = oldAtom.getAtomResidueIndex();
            } else {
                // compare for sequencial index
                Atom newAtom = myResidue.getAtom("P");
                if ((newAtom != null) && residueIndex == newAtom.getAtomResidueIndex() - 1) {
                    // draw line, if sequencial
                    Shape3D line = myResidue.generateLine(oldAtom, newAtom, myValues.LINE_WIDTH_MEDIUM);
                    line.setMaterial(myValues.MATERIAL_DARK_GREEN);
                    myGroup.getChildren().add(line);
                    oldAtom = newAtom;
                    residueIndex = newAtom.getAtomResidueIndex();
                } else {
                    // if not sequencial, reset
                    if (myResidue.getAtom("P") != null) {
                        oldAtom = myResidue.getAtom("P");
                        residueIndex = oldAtom.getAtomResidueIndex();
                    }
                }
            }
            // generate base
            MeshView currentBaseView = myResidue.generateBaseMesh();
            currentBaseView.setMaterial(myValues.MATERIAL_RED);
            currentBaseView.setDrawMode(DrawMode.FILL);
            //generate sugar
            MeshView currentSugarView = myResidue.generateSugarMesh();
            currentSugarView.setMaterial(myValues.MATERIAL_ORANGE);
            currentSugarView.setDrawMode(DrawMode.FILL);
            Shape3D currentPhosphorus = new Sphere(2);
            // handle phosphorus
            if (myResidue.generatePhosphorusMesh(currentPhosphorus)) {
                myGroup.getChildren().add(currentPhosphorus);
            }
            // generate line
            Shape3D currentLine = myResidue.generateLine();
            currentLine.setMaterial(myValues.MATERIAL_BLACK);

            myGroup.getChildren().addAll(currentBaseView, currentSugarView, currentLine);


        }

        centerGroup(drawRoot);

    }


    /**
     * generate Residue according to char
     *
     * @param a
     * @return
     */
    private AResidue constructResidueWithType(Atom a) {

        switch (Character.toLowerCase(a.getAtomResidue())) {
            case 'a':
                return new Adenine();
            case 'u':
                return new Uracil();
            case 'g':
                return new Guanine();
            case 'c':
                return new Cytosine();
            default:
                return null;
        }
    }

    /**
     * Center Group elements
     *
     * @param g
     */
    private void centerGroup(Group g) {
        ObservableList<Node> nodes = g.getChildren();
        double meanX = 0;
        double meanY = 0;
        double meanZ = 0;

        for (Node currentNode : nodes) {
            meanX += currentNode.getTranslateX();
            meanY += currentNode.getTranslateY();
            meanZ += currentNode.getTranslateZ();
        }
        meanX = meanX / nodes.size();
        meanY = meanY / nodes.size();
        meanZ = meanZ / nodes.size();


        for (Node currentNode : g.getChildren()) {
            currentNode.setTranslateX(currentNode.getTranslateX() - meanX);
            currentNode.setTranslateY(currentNode.getTranslateY() - meanY);
            currentNode.setTranslateZ(currentNode.getTranslateZ() - meanZ);
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

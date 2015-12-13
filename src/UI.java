import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Deviltech on 13.12.2015.
 */
public class UI extends Application{


    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBox = new VBox();
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(vBox, 800, 800);

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");

        MenuItem add = new MenuItem("Open");
        add.setOnAction((value) -> {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("PDB files (*.pdb)", "*.pdb");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                System.out.println(file);
            }

            }
        );

        menuFile.getItems().addAll(add);
        menuBar.getMenus().addAll(menuFile);

        vBox.getChildren().addAll(menuBar, stackPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("3D Viewer");




        // show scene
        primaryStage.show();
    }
}

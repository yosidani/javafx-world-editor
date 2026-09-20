package mvh.app;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;


/**
 * @author Yosias Demoz
 * @since April 4, 2026
 */


/**
 * The main entry point for the Monsters vs Heroes World Editor.
 * Handles the initialization of the JavaFX and sets up the primary stage.
 */
public class MvHApp extends Application {

    /**
     * The current version of the application.
     */
    public static final String version = "1.0";

    /**
     * Standard main method to launch app.
     * @param args command line arguments passed to the program.
     */
    static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the primary stage, loads the fxml view, and sets up the controller.
     * Also configures a shutdown to ensure all processes stop when the window is closed.
     * @param stage the primary stage.
     * @throws IOException if the fxml cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Monster vs Heroes World Editor v1.0");
        //Arguments to program
        List<String> args = getParameters().getRaw();
        FXMLLoader fxmlLoader = new FXMLLoader(MvHApp.class.getResource("MvHAppView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        MvHAppController controller = fxmlLoader.getController();
        controller.initData(args);
        stage.setScene(scene);
        stage.show();
        //if main closed then close everthing
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}

package mvh.app;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class MvHApp extends Application {

    public static final String version = "1.0";

    static void main(String[] args) {
        launch(args);
    }

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

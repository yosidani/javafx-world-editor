package mvh.app;

import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import mvh.enums.*;
import mvh.util.*;
import mvh.world.*;

import java.io.*;
import java.util.*;


public class MvHAppController {

    //Store the data of editor
    private World world;

    //Has to be run yourself to modify controller data after FXMLLoader .load()
    public void initData(List<String> args) {
    }

    //Runs on FXMLLoader .load()
    @FXML
    public void initialize() {
    }

    @FXML
    public void Load() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open World File");

        File file = chooser.showOpenDialog(null);
        if (file == null) return;

        world = MvHReader.loadWorld(file);

        printWorld();
    }

    @FXML
    private AnchorPane WorldMap;

    private void printWorld() {
        WorldMap.getChildren().clear();

        GridPane grid = new GridPane();


        int row = world.getRows();
        int col = world.getColumns();
        for (int i = 0; i < row + 2; i++) {
            for (int j = 0; j < col + 2; j++) {
                String symbol;
                if (i == 0 || j == 0 || i == row + 1 || j == col + 1) {
                    symbol = "#";
                }
                else {
                    Object entity = world.getEntity(i - 1, j - 1);

                    if (entity == null) {
                        symbol = ".";
                    } else if (entity instanceof Wall) {
                        symbol = "#";
                    } else if (entity instanceof Monster && ((Monster) entity).isAlive()) {
                        symbol = "M";
                    } else if (entity instanceof Hero && ((Hero) entity).isAlive()) {
                        symbol = "H";
                    } else {
                        symbol = "?";
                    }
                }
                    Label cell = new Label(symbol);
                    cell.setPrefSize(30, 30);
                    cell.setAlignment(Pos.CENTER);
                    cell.setStyle("-fx-border-color: black;");

                    grid.add(cell, j, i);

                }
            }
        WorldMap.getChildren().add(grid);
        }
}


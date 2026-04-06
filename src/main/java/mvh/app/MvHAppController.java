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
        // clean the anchorpane
        WorldMap.getChildren().clear();
        //create grid
        GridPane grid = new GridPane();
        AnchorPane.setTopAnchor(grid, 75.0);
        AnchorPane.setLeftAnchor(grid, 75.0);

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
                    //identify entity
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
                //add the symbol to a label and then add the label to the grid
                Label cell = new Label(symbol);
                cell.setPrefSize(40, 40);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: black;");
                grid.add(cell, j, i);
                }
            }
        //add grid to anchorpane
        WorldMap.getChildren().add(grid);
        }
}


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
    private File currentFile;
    @FXML
    private AnchorPane WorldMap;
    @FXML
    private Label LeftStatus;
    @FXML
    private Label RightStatus;
    @FXML
    private TextField column;
    @FXML
    private TextField row;
    @FXML
    private Button AddWall;

    //Has to be run yourself to modify controller data after FXMLLoader .load()
    public void initData(List<String> args) {
    }

    //Runs on FXMLLoader .load()
    @FXML
    public void initialize() {
        LeftStatus.setText("Nothing Currently.");
        LeftStatus.setTextFill(Color.BLACK);
        RightStatus.setText("Load or Create a World.");
        RightStatus.setTextFill(Color.BLACK);
    }

    @FXML
    public void Load() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open World File");

        File file = chooser.showOpenDialog(null);
        if (file == null) return;

        world = MvHReader.loadWorld(file);
        // remember file after loading
        currentFile = file;

        printWorld();
        LeftStatus.setText("World Loaded");
        LeftStatus.setTextFill(Color.GREEN);
        RightStatus.setText("Showing World.");
        RightStatus.setTextFill(Color.BLACK);
    }
    @FXML
    public void Save() {
        if (world == null) {
            LeftStatus.setText("No world loaded or created!");
            LeftStatus.setTextFill(Color.RED);
            return;
        }
        //if no file loaded from earlier, then go to save as
        if (currentFile == null) {
            SaveAs();
            return;
        }
        //else save on the currnt file
        MvHWriter.saveWorld(currentFile, world);
        LeftStatus.setText("World Saved!");
        LeftStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void SaveAs() {
        if (world == null) {
            LeftStatus.setText("No world loaded or created!");
            LeftStatus.setTextFill(Color.RED);
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save World As");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        Stage stage = (Stage) WorldMap.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);

        if (file == null) return;
        MvHWriter.saveWorld(file, world);
        currentFile = file;
        LeftStatus.setText("World Saved!");
        LeftStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void Quit() {
        LeftStatus.setText("Leaving?");
        LeftStatus.setTextFill(Color.RED);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Quit World?");
        alert.setContentText("Are you sure you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
        LeftStatus.setText("You Stayed");
        LeftStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void About() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Message");
        alert.setContentText("This is a World editor/creator for a Monster vs Hero game.\nAuthor: Yosias Demoz\nEmail: yosias.demoz@ucalgary.ca\nVersion: v1.0");
        alert.showAndWait();
    }

    @FXML
    public void Create() {
        try {
            int World_columns = Integer.parseInt(column.getText());
            int World_rows = Integer.parseInt(row.getText());
            world = new World(World_rows, World_columns);
        } catch (NumberFormatException e) {
            LeftStatus.setText("Please enter valid integers in both fields to create a World.");
            LeftStatus.setTextFill(Color.RED);
        }
        printWorld();
        LeftStatus.setText("World Created!");
        LeftStatus.setTextFill(Color.GREEN);
        RightStatus.setText("Showing World.");
        RightStatus.setTextFill(Color.BLACK);
    }

    private boolean addWallMode = false;
    @FXML
    private void AddWall() {
        if  (addWallMode) {
            addWallMode = false;
            AddWall.setStyle("");
            return;
        }
        if (world == null) {
            LeftStatus.setText("No world loaded or created!");
            LeftStatus.setTextFill(Color.RED);
            return;
        }
        addWallMode = true;
        AddWall.setStyle("-fx-border-color: green; -fx-border-width: 2;");
        LeftStatus.setText("Adding wall! Click on a cell to add a wall.");
        LeftStatus.setTextFill(Color.RED);
    }

    public enum AddType {
        HERO,
        MONSTER
    }
    private Stage stage;
    private AddController controller;

    @FXML
    private void AddHero() {
        forStage(AddType.HERO);
    }
    @FXML
    private void AddMonster() {
        forStage(AddType.MONSTER);
    }

    private void forStage(AddType type) {
        try {
            //if the stage doesn't exist yet
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AddView.fxml"));
                Parent root = loader.load();
                controller = loader.getController();
                stage = new Stage();
                stage.setScene(new Scene(root));
                // if stage closed null
                stage.setOnCloseRequest(e -> {
                    stage = null;
                });
            }
            controller.addEntity(type);
            stage.setTitle("Add " + (type == AddType.HERO ? "Hero" : "Monster"));
            if (!stage.isShowing()) {
                stage.show();
            } else {
                stage.toFront();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                final int r = i;
                final int c = j;
                //add the symbol to a label and then add the label to the grid
                Label cell = new Label(symbol);
                cell.setOnMouseEntered(e -> {
                    if (addWallMode) {
                        cell.setStyle("-fx-border-color: red; -fx-font-size: 22px;");
                    }
                });
                cell.setOnMouseExited(e -> {
                    cell.setStyle("-fx-border-color: black; -fx-font-size: 18px;");
                });
                cell.setOnMouseClicked(e -> {
                    if (!addWallMode) return;
                    if (r == 0 || c == 0 || r == row + 1 || c == col + 1) {
                        return;
                    }
                    //update the world by adding a wall
                    if (e.getClickCount() == 2) {
                        world.addEntity(r - 1, c - 1, null);
                        printWorld();
                        LeftStatus.setText("Wall removed!");
                        LeftStatus.setTextFill(Color.RED);
                        return;
                    }
                    if (addWallMode && e.getClickCount() == 1) {
                        world.addEntity(r - 1, c - 1, Wall.getWall());
                        printWorld();
                        LeftStatus.setText("Wall added!");
                        LeftStatus.setTextFill(Color.GREEN);
                    }

                });
                cell.setPrefSize(40, 40);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: black; -fx-font-size: 18px;");
                grid.add(cell, j, i);
                }
            }
        //add grid to anchorpane
        WorldMap.getChildren().add(grid);
        }
}


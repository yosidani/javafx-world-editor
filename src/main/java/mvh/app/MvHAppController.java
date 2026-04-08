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
    @FXML
    private Button AddHero;
    @FXML
    private Button AddMonster;
    @FXML
    private Label detailsInfoLabel;

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
        WorldMap.setOnDragOver(event -> {
            // We only care if the drag contains files
            if (event.getGestureSource() != WorldMap && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        // 2. Handle the "Drop" part
        WorldMap.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                // Get the first file dropped
                File file = db.getFiles().get(0);


                if (file == null) return;

                world = MvHReader.loadWorld(file);
                currentFile = file;

                printWorld();
                LeftStatus.setText("World Loaded by drag and drop");
                LeftStatus.setTextFill(Color.GREEN);
                RightStatus.setText("Showing World.");
                RightStatus.setTextFill(Color.BLACK);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
    private boolean addHeroMode = false;
    private boolean addMonsterMode = false;
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
        if (stage != null) {
            stage.close();
        }
        addHeroMode = false;
        addMonsterMode = false;
        AddMonster.setStyle("");
        AddHero.setStyle("");
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
        if (world == null) {
            LeftStatus.setText("Please create or load a world first!");
            LeftStatus.setTextFill(Color.RED);
            return;
        }
        forStage(AddType.HERO);
        addHeroMode = true;
        addMonsterMode = false;
        AddMonster.setStyle("");
        addWallMode = false;
        AddWall.setStyle("");
        LeftStatus.setText("Adding hero.");
        LeftStatus.setTextFill(Color.BLACK);
    }
    @FXML
    private void AddMonster() {
        if (world == null) {
            LeftStatus.setText("Please create or load a world first!");
            LeftStatus.setTextFill(Color.RED);
            return;
        }
        forStage(AddType.MONSTER);
        addMonsterMode = true;
        addWallMode = false;
        AddWall.setStyle("");
        addHeroMode = false;
        AddHero.setStyle("");
        LeftStatus.setText("Adding monster.");
        LeftStatus.setTextFill(Color.BLACK);
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
            if (type == AddType.MONSTER) {
                AddMonster.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            } else if (type == AddType.HERO) {
                AddHero.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            }
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
                        symbol = String.valueOf(((Monster) entity).getSymbol());
                    } else if (entity instanceof Hero && ((Hero) entity).isAlive()) {
                        symbol = String.valueOf(((Hero) entity).getSymbol());
                    } else {
                        symbol = "$";
                    }
                }
                final int r = i;
                final int c = j;
                //add the symbol to a label and then add the label to the grid
                Label cell = new Label(symbol);
                cell.setOnMouseEntered(e -> {
                    if (addWallMode || addHeroMode || addMonsterMode) {
                        cell.setStyle("-fx-border-color: red; -fx-font-size: 22px;");
                    } else {
                        cell.setStyle("-fx-border-color: green; -fx-border-width: 1.5px; -fx-font-size: 22px;");
                    }

                    if (r == 0 || c == 0 || r == row + 1 || c == col + 1) {
                        detailsInfoLabel.setText("Border Wall\nCannot be removed.");
                        return;
                    }

                    // Get the entity at this specific cell
                    Object entity = world.getEntity(r - 1, c - 1);

                    // Check what the entity is and format the text
                    if (entity == null) {
                        detailsInfoLabel.setText("Empty Space");
                    }
                    else if (entity instanceof Wall) {
                        detailsInfoLabel.setText("It is a Wall.");
                    }
                    else if (entity instanceof Hero) {
                        Hero h = (Hero) entity;
                        // NOTE: Adjust these getter methods to match the ones in your Hero class!
                        detailsInfoLabel.setText(
                                "--- HERO ---\n" +
                                        "Symbol: " + h.getSymbol() + "\n" +
                                        "Health: " + h.getHealth() + "\n" +
                                        "Attack: " + h.weaponStrength() + "\n" +
                                        "Armor: " + h.armourStrength()
                        );
                    }
                    else if (entity instanceof Monster) {
                        Monster m = (Monster) entity;
                        // NOTE: Adjust these getter methods to match the ones in your Monster class!
                        detailsInfoLabel.setText(
                                "--- MONSTER ---\n" +
                                        "Symbol: " + m.getSymbol() + "\n" +
                                        "Health: " + m.getHealth() + "\n" +
                                        "Weapon: " + m.getWeaponType()
                        );
                    }
                });
                cell.setOnMouseExited(e -> {
                    cell.setStyle("-fx-border-color: black; -fx-font-size: 18px;");
                });
                cell.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        if (r == 0 || c == 0 || r == row + 1 || c == col + 1) return;
                        if(world.isHero(r - 1, c - 1)){
                            LeftStatus.setText("Hero removed!");
                            LeftStatus.setTextFill(Color.RED);
                        } else if(world.isMonster(r - 1, c - 1)){
                            LeftStatus.setText("Monster removed!");
                            LeftStatus.setTextFill(Color.RED);
                        }else {
                            LeftStatus.setText("Wall removed!");
                            LeftStatus.setTextFill(Color.RED);
                        }
                        world.addEntity(r - 1, c - 1, null);
                        printWorld();
                        return;
                    }

                    if (e.getClickCount() == 1) {
                        if (r == 0 || c == 0 || r == row + 1 || c == col + 1) return;

                        if (addWallMode) {
                            world.addEntity(r - 1, c - 1, Wall.getWall());
                            LeftStatus.setText("Wall added!");
                        }
                        else if (addHeroMode && controller != null) {
                            // data from stage
                            if (controller.getSymbol() == '?'){
                                LeftStatus.setText("Please enter a symbol.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            char HeroSymbol = controller.getSymbol();
                            if (controller.getHealth() == "?"){
                                LeftStatus.setText("Please enter valid Health.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            int health = Integer.parseInt(controller.getHealth());
                            if (controller.getHeroAttack() == "?"){
                                LeftStatus.setText("Please enter valid attack strength.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            int weapon = Integer.parseInt(controller.getHeroAttack());
                            if (controller.getHeroArmor() == "?"){
                                LeftStatus.setText("Please enter valid armor strength.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            int armor = Integer.parseInt(controller.getHeroArmor());
                            // create hero
                            Hero h = new Hero(HeroSymbol, health, weapon, armor);
                            world.addEntity(r - 1, c - 1, h);
                            LeftStatus.setText("Hero added!");
                        }
                        else if (addMonsterMode && controller != null) {
                            // data from stage
                            if (controller.getSymbol() == '?'){
                                LeftStatus.setText("Please enter a symbol.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            char MonsterSymbol = controller.getSymbol();
                            if (controller.getHealth() == "?"){
                                LeftStatus.setText("Please enter valid Health.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            int health = Integer.parseInt(controller.getHealth());
                            if (controller.getMonsterWeapon() == '?'){
                                LeftStatus.setText("Please enter a weapon.");
                                LeftStatus.setTextFill(Color.RED);
                                return;
                            }
                            WeaponType weapon = WeaponType.getWeaponType(controller.getMonsterWeapon());

                            // create monster
                            Monster m = new Monster(MonsterSymbol, health, weapon);
                            world.addEntity(r - 1, c - 1, m);

                            LeftStatus.setText("Monster added!");
                        }

                        printWorld();
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


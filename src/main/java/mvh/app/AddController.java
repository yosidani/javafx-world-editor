package mvh.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mvh.world.World;
import mvh.app.MvHAppController.AddType;

import java.io.IOException;
import java.util.List;


/**
 * @author Yosias Demoz
 * @since April 4, 2026
 */

/**
 * Controller for the AddView popup window.
 * Manages the state for creating both Hero and Monster entities,
 * including field visibility and data validation.
 */
public class AddController {

    @FXML
    private Label titleLabel;
    @FXML
    private VBox weaponBox;
    @FXML
    private VBox heroBox;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField symbolField;
    @FXML
    private TextField healthField;
    @FXML
    private TextField attackField;
    @FXML
    private TextField armorField;
    @FXML
    private ComboBox<String> weaponComboBox;


    /**
     * Adjusts the visibility of fxml elements based on the entity type being added.
     * Shows armor/attack fields for Heroes and the weapon ComboBox for Monsters.
     * @param type The type of entity to configure the view for.
     */
    public void addEntity(AddType type) {
        if (type == AddType.HERO) {
            titleLabel.setText("Hero");
            heroBox.setVisible(true);
            heroBox.setManaged(true);
            weaponBox.setVisible(false);
            weaponBox.setManaged(false);
            statusLabel.setText("Add a Hero.");
            statusLabel.setTextFill(Color.BLACK);
        } else {
            titleLabel.setText("Monster");
            weaponBox.setVisible(true);
            weaponBox.setManaged(true);
            heroBox.setVisible(false);
            heroBox.setManaged(false);
            statusLabel.setText("Add a Monster.");
            statusLabel.setTextFill(Color.BLACK);
        }
    }

    /**
     * Validates and gets the symbol from the input field.
     * @return The character in the symbolField, or '?' if the input is empty or invalid.
     */
    public char getSymbol() {
        if  (symbolField.getText().equals("")) {
            statusLabel.setText("Please enter a symbol.");
            symbolField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            statusLabel.setTextFill(Color.RED);
            return '?';
        }
        if (symbolField.getText().length() > 1){
            statusLabel.setText("Please enter a single character.");
            symbolField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            statusLabel.setTextFill(Color.RED);
            return '?';
        }
        statusLabel.setText("Continue adding.");
        statusLabel.setTextFill(Color.BLACK);
        symbolField.setStyle("");
        return symbolField.getText().charAt(0);
    }

    /**
     * Validates that the health input is a valid integer.
     * @return The health value as a String for parsing, or "?" if invalid.
     */
    public String getHealth() {
        try {
            Integer.parseInt(healthField.getText());
            statusLabel.setText("Continue adding.");
            statusLabel.setTextFill(Color.BLACK);
            healthField.setStyle("");
            return healthField.getText();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid health.");
            statusLabel.setTextFill(Color.RED);
            healthField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            return "?";
        }
    }

    /**
     * Validates that the hero's attack strength input is a valid integer.
     * @return The attack value as a String, or "?" if invalid.
     */
    public String getHeroAttack() {
        try {
            Integer.parseInt(attackField.getText());
            statusLabel.setText("Continue adding a hero.");
            statusLabel.setTextFill(Color.BLACK);
            attackField.setStyle("");
            return attackField.getText();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid attack strength.");
            statusLabel.setTextFill(Color.RED);
            attackField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            return "?";
        }
    }

    /**
     * Validates that the hero's armor strength input is a valid integer.
     * @return The armor value as a String, or "?" if invalid.
     */
    public String getHeroArmor() {
        try {
            Integer.parseInt(armorField.getText());
            statusLabel.setText("Continue adding a hero.");
            statusLabel.setTextFill(Color.BLACK);
            armorField.setStyle("");
            return armorField.getText();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid armor strength.");
            statusLabel.setTextFill(Color.RED);
            armorField.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            return "?";
        }
    }

    /**
     * Validates that a weapon has been selected from the ComboBox.
     * @return The first character of the selected weapon string,
     * or '?' if no selection is made.
     */
    public char getMonsterWeapon() {
        if (weaponComboBox.getValue() == null) {
            statusLabel.setText("Please enter a weapon.");
            weaponComboBox.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 3px;");
            statusLabel.setTextFill(Color.RED);
            return '?';
        }
        statusLabel.setText("Continue adding a monster.");
        statusLabel.setTextFill(Color.BLACK);
        weaponComboBox.setStyle("");
        return weaponComboBox.getValue().charAt(0);
    }
}

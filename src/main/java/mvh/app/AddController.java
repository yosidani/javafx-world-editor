package mvh.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mvh.world.World;
import mvh.app.MvHAppController.AddType;

import java.io.IOException;
import java.util.List;

public class AddController {

    @FXML
    private Label titleLabel;
    @FXML
    private VBox weaponBox;
    @FXML
    private VBox heroBox;
    @FXML
    private Label statusLabel;

    public void addEntity(AddType type) {
        if (type == AddType.HERO) {
            titleLabel.setText("Hero");
            heroBox.setVisible(true);
            heroBox.setManaged(true);
            weaponBox.setVisible(false);
            weaponBox.setManaged(false);

            statusLabel.setText("Add a Hero.");
        } else {
            titleLabel.setText("Monster");
            weaponBox.setVisible(true);
            weaponBox.setManaged(true);
            heroBox.setVisible(false);
            heroBox.setManaged(false);
            statusLabel.setText("Add a Monster.");
        }
    }
}

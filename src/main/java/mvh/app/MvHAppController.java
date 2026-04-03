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

}

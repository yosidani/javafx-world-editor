module mvh.app {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens mvh.app to javafx.fxml;
    exports mvh.app;
}
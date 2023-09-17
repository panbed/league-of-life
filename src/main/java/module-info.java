module com.ianbed.leagueoflife.leagueoflife {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;


    opens com.ianbed.leagueoflife to javafx.fxml;
    exports com.ianbed.leagueoflife;
}
module com.ianbed.leagueoflife.leagueoflife {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.ianbed.leagueoflife to javafx.fxml;
    exports com.ianbed.leagueoflife;
}
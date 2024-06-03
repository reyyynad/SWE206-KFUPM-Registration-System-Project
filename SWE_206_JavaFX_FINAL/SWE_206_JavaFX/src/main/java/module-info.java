module com.example.swe_206_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.swe_206_javafx to javafx.fxml;
    exports com.example.swe_206_javafx;


}
module com.mycompany.prueba {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.prueba to javafx.fxml;
    exports com.mycompany.prueba;
}

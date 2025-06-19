package com.mycompany.prueba;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class CreateContactController {

    @FXML
    private void switchToCreateNaturalPerson() throws IOException {
        App.setRoot("createNaturalPerson");
    }

    @FXML
    private void switchToCreateEmpresa() throws IOException {
        App.setRoot("createEmpresa");
    }
    
    @FXML
    private void switchAtras() throws IOException {
        App.setRoot("mainView");
    }
    
}

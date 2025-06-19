/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
public class MainViewController {

    @FXML
    private void switchCreateContac() throws IOException {
        App.setRoot("createContact");
    }

    @FXML
    private void switchToContacts() throws IOException {
        App.setRoot("viewContact");
    }
    
}

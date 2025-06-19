package com.mycompany.prueba;

import Modelo.Contacto;
import Modelo.ListaDobleCircular;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
//        ListaDobleCircular<Contacto> contactos = Contacto.cargarContactos();
//
//        // Verificar que se hayan cargado correctamente (impresión opcional)
//        if (!contactos.estaVacia()) {
//            System.out.println("Contactos cargados exitosamente:");
//            contactos.mostrarAdelante(); // Asumiendo que tienes un método imprimir en ListaDobleCircular
//        } else {
//            System.out.println("No se encontraron contactos.");
//        }
        scene = new Scene(loadFXML("mainView"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
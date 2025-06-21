/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.prueba;

import Modelo.ArrayList;
import Modelo.Contacto;
import Modelo.Empresa;
import Modelo.Foto;
import Modelo.ListaDobleCircular;
import Modelo.NodoDobleCircular;
import Modelo.PersonaNatural;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class AsociadosViewController implements Initializable {

    @FXML
    private VBox vboxContactos;
    @FXML
    private Button btnAtras, btnSiguiente;
    @FXML
    private Button btnFotoAnterior;
    @FXML
    private Button btnFotoSiguiente;

    private ListaDobleCircular<Contacto> asociados;
    private NodoDobleCircular<Contacto> nodoActual;
    private NodoDobleCircular<Foto> nodoFotoActual;
    private ImageView imgFoto; 
    private ArrayList<TextField> camposActuales = new ArrayList<>(10);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSiguiente.setOnAction(e -> siguienteContacto());
        btnAtras.setOnAction(e -> anteriorContacto());
        btnFotoSiguiente.setOnAction(e -> mostrarFotoSiguiente());
        btnFotoAnterior.setOnAction(e -> mostrarFotoAnterior());
    }    
    
    public void setContactosAsociados(ListaDobleCircular<Contacto> asociados) {
        this.asociados = asociados;
        if (!asociados.estaVacia()) {
            nodoActual = asociados.cabeza;
            mostrarContacto(nodoActual.dato);
        }
    }
    
    private void mostrarFotoSiguiente() {
        if (nodoFotoActual != null && nodoFotoActual.siguiente != null) {
            nodoFotoActual = nodoFotoActual.siguiente;
            cargarImagen(nodoFotoActual.dato.getRuta());
        }
    }
    
    private void mostrarFotoAnterior() {
        if (nodoFotoActual != null && nodoFotoActual.anterior != null) {
            nodoFotoActual = nodoFotoActual.anterior;
            cargarImagen(nodoFotoActual.dato.getRuta());
        }
    }
    
    private void cargarImagen(String rutaRelativa) {
        try {
            // Ruta relativa al directorio externo "imagenes"
            File archivo = new File("imagenes", new File(rutaRelativa).getName());
            if (archivo.exists()) {
                Image imagen = new Image(archivo.toURI().toString());
                imgFoto.setImage(imagen);
            } else {
                System.out.println("Archivo de imagen no encontrado: " + archivo.getPath());
                imgFoto.setImage(null);
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + rutaRelativa);
            imgFoto.setImage(null);
        }
    }
    
    private void mostrarContacto(Contacto contacto) {
        System.out.println("Contacto actual:");
        System.out.println("Mostrando: " + contacto);
        ListaDobleCircular<Foto> fotos = contacto.getFotos();
        if (fotos.estaVacia()) {
            System.out.println("Este contacto no tiene fotos.");
        } else {
            System.out.println("Fotos asociadas:");
            NodoDobleCircular<Foto> nodoFoto = fotos.cabeza;
            do {
                System.out.println("  - " + nodoFoto.dato);
                nodoFoto = nodoFoto.siguiente;
            } while (nodoFoto != fotos.cabeza);
        }
        vboxContactos.getChildren().clear();

        VBox contenedor = new VBox(10);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(10));
   
        // Imagen + flechas laterales
        HBox hboxImagen = new HBox(10);
        hboxImagen.setAlignment(Pos.CENTER);
        
        VBox vboxImagenYBoton = new VBox(5);
        vboxImagenYBoton.setAlignment(Pos.CENTER);

        Button btnIzquierda = btnFotoAnterior;
        Button btnDerecha = btnFotoSiguiente;

        imgFoto = new ImageView();
        imgFoto.setFitHeight(120);
        imgFoto.setFitWidth(120);
        imgFoto.setPreserveRatio(true);

        if (!fotos.estaVacia()) {
            nodoFotoActual = fotos.cabeza;
            cargarImagen(nodoFotoActual.dato.getRuta());
        } else {
            // Usar una imagen por defecto ubicada en la carpeta externa "imagenes/"
            String rutaDefault = "imagenes/default.png";  // ruta relativa al ejecutable (no dentro del proyecto)
            cargarImagen(rutaDefault);
            nodoFotoActual = null;
        }
        vboxImagenYBoton.getChildren().addAll(imgFoto);
        hboxImagen.getChildren().addAll(btnIzquierda, vboxImagenYBoton, btnDerecha);
        contenedor.getChildren().add(hboxImagen);
        
        if (contacto instanceof PersonaNatural) {
            PersonaNatural pn = (PersonaNatural) contacto;
            contenedor.getChildren().add(crearVistaPersonaNatural(pn));
        } else if (contacto instanceof Empresa) {
            Empresa em = (Empresa) contacto;
            contenedor.getChildren().add(crearVistaEmpresa(em));
        }    
        vboxContactos.getChildren().add(contenedor);
    }
    
    @FXML
    private void siguienteContacto() {
        if (nodoActual != null && nodoActual.siguiente != null) {
            nodoActual = nodoActual.siguiente;
            mostrarContacto(nodoActual.dato);
        }
    }

    @FXML
    private void anteriorContacto() {
        if (nodoActual != null && nodoActual.anterior != null) {
            nodoActual = nodoActual.anterior;
            mostrarContacto(nodoActual.dato);
        }
    }
    
    private Node crearVistaPersonaNatural(PersonaNatural p) {
        GridPane grid = crearGrid();

        agregarFila(grid, "Nombre:", crearCampo(p.getNombre()), 0);
        agregarFila(grid, "Apellido:", crearCampo(p.getApellido()), 1);
        agregarFila(grid, "Teléfono:", crearCampo(p.getTelefono()), 2);
        agregarFila(grid, "Fecha de Nacimiento:", crearCampo(p.getFechaNacimiento()), 3);
        agregarFila(grid, "Email:", crearCampo(p.getEmail()), 4);
        agregarFila(grid, "País:", crearCampo(p.getPais()), 5);

        return grid;
    }

    private Node crearVistaEmpresa(Empresa e) {
        GridPane grid = crearGrid();

        agregarFila(grid, "Nombre:", crearCampo(e.getNombre()), 0);
        agregarFila(grid, "Teléfono:", crearCampo(e.getTelefono()), 1);
        agregarFila(grid, "Email:", crearCampo(e.getEmail()), 2);
        agregarFila(grid, "País:", crearCampo(e.getPais()), 3);
        agregarFila(grid, "Rubro:", crearCampo(e.getRubro()), 4);
        agregarFila(grid, "Dirección:", crearCampo(e.getDireccion()), 5);

        return grid;
    }

    private GridPane crearGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER); 
        return grid;
    }

    private void agregarFila(GridPane grid, String etiqueta, Node campo, int fila) {
        grid.add(new Label(etiqueta), 0, fila);
        grid.add(campo, 1, fila);
    }

    private TextField crearCampo(String valor) {
        TextField field = new TextField(valor);
        field.setDisable(true);
        camposActuales.addLast(field);
        return field;
    }
}

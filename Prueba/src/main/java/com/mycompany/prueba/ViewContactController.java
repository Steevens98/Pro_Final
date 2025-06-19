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
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
public class ViewContactController implements Initializable {
    @FXML
    private VBox vboxContactos;
    @FXML
    private Button btnSiguiente, btnAtras, btnEditar;
    @FXML
    private Button btnFotoSiguiente, btnFotoAnterior;

    private ImageView imgFoto;    
    private ListaDobleCircular<Contacto> listaContactos;
    private NodoDobleCircular<Contacto> nodoActual;
    private NodoDobleCircular<Foto> nodoFotoActual;
    private boolean modoEdicion = false;
    private ArrayList<TextField> camposActuales = new ArrayList<>(10);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaContactos = Contacto.cargarContactos();
        Contacto.cargarFotos(listaContactos);
        if (!listaContactos.estaVacia()) {
            System.out.println("Contactos cargados exitosamente:");
            listaContactos.mostrarAdelante(); 
        } else {
            System.out.println("No se encontraron contactos.");
        }
        if (!listaContactos.estaVacia()) {
            nodoActual = listaContactos.cabeza;
            mostrarContacto(nodoActual.dato);
        }
        vboxContactos.setAlignment(Pos.CENTER);

        btnSiguiente.setOnAction(e -> siguienteContacto());
        btnAtras.setOnAction(e -> anteriorContacto());
        btnEditar.setOnAction(e -> alternarModoEdicion());
        btnFotoSiguiente.setOnAction(e -> mostrarFotoSiguiente());
        btnFotoAnterior.setOnAction(e -> mostrarFotoAnterior());
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

    private void siguienteContacto() {
        nodoActual = listaContactos.avanzar(nodoActual);
        mostrarContacto(nodoActual.dato);
    }

    private void anteriorContacto() {
        nodoActual = listaContactos.retroceder(nodoActual);
        mostrarContacto(nodoActual.dato);
    }

    private void mostrarContacto(Contacto contacto) {
        camposActuales = new ArrayList<>(10);
        System.out.println("Contacto actual:");
        System.out.println("Mostrando: " + contacto);
        System.out.println("Clase: " + contacto.getClass().getSimpleName());
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

        hboxImagen.getChildren().addAll(btnIzquierda, imgFoto, btnDerecha);
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
    
//    private void cargarImagen(String ruta) {
//        try {
//            Image imagen;
//            if (ruta.startsWith("/")) {
//                imagen = new Image(getClass().getResourceAsStream(ruta));
//            } else {
//                imagen = new Image("file:" + ruta);
//            }
//            imgFoto.setImage(imagen);
//        } catch (Exception e) {
//            System.out.println("Error cargando imagen: " + ruta);
//            imgFoto.setImage(null);
//        }
//    }
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
    
    private void alternarModoEdicion() {
        if (modoEdicion) {
            // Guardar cambios y desactivar campos
            guardarCambios();
            for (TextField campo : camposActuales) {
                campo.setDisable(true);
            }
            btnEditar.setText("Editar");
            modoEdicion = false;
        } else {
            // Activar edición
            for (TextField campo : camposActuales) {
                campo.setDisable(false);
            }
            btnEditar.setText("Guardar");
            modoEdicion = true;
        }
    }
    
    private void guardarCambios() {
        Contacto c = nodoActual.dato;

        if (c instanceof PersonaNatural && camposActuales.size() == 6) {
            PersonaNatural p = (PersonaNatural) c;
            p.setNombre(camposActuales.get(0).getText());
            p.setApellido(camposActuales.get(1).getText());
            p.setTelefono(camposActuales.get(2).getText());
            p.setFechaNacimiento(camposActuales.get(3).getText());
            p.setEmail(camposActuales.get(4).getText());
            p.setPais(camposActuales.get(5).getText());
        } else if (c instanceof Empresa && camposActuales.size() == 6) {
            Empresa e = (Empresa) c;
            e.setNombre(camposActuales.get(0).getText());
            e.setTelefono(camposActuales.get(1).getText());
            e.setEmail(camposActuales.get(2).getText());
            e.setPais(camposActuales.get(3).getText());
            e.setRubro(camposActuales.get(4).getText());
            e.setDireccion(camposActuales.get(5).getText());
        }
        System.out.println("Cambios guardados: " + c);
        Contacto.guardarContactosEnArchivo(listaContactos);
        Contacto.guardarFotosEnArchivo(listaContactos);
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
    
    @FXML
    private void switchAtras() throws IOException {
        App.setRoot("mainView");
    }
}

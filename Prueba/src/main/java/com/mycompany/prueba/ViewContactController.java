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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    @FXML
    private Button btnEliminar;
    @FXML
    private ComboBox<String> comboOrdenar;
    @FXML
    private Button btnOrdenar;
    @FXML
    private TextField txtFiltro;
    @FXML
    private Button btnFiltrar;
    
    private HBox hboxAsociar;
    private ComboBox<Contacto> comboContactosAsociar = new ComboBox<>();
    private Button btnAsociar = new Button("Asociar contacto");
    private Button btnSeleccionarImagen = new Button("Seleccionar Imagen");
    private ImageView imgFoto;    
    private File archivoImagenSeleccionada;
    private ListaDobleCircular<Contacto> listaContactos;
    private NodoDobleCircular<Contacto> nodoActual;
    private NodoDobleCircular<Foto> nodoFotoActual;
    private ListaDobleCircular<Contacto> listaFiltrada; 
    private boolean modoEdicion = false;
    private ArrayList<TextField> camposActuales = new ArrayList<>(10);
    private Comparator<Contacto> comparadorPorNombre = Comparator.comparing(
            Contacto::getNombre, String.CASE_INSENSITIVE_ORDER);
    private Comparator<Contacto> comparadorPorPais = Comparator.comparing(
            c -> c.getPais() == null ? "" : c.getPais().trim(), // evita null y espacios
            String.CASE_INSENSITIVE_ORDER);
    private Comparator<Contacto> comparadorPorTipo = Comparator.comparing(
            c -> (c instanceof PersonaNatural) ? "Persona" : "Empresa", String.CASE_INSENSITIVE_ORDER);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaContactos = Contacto.cargarContactos();
        Contacto.cargarFotos(listaContactos);
        Contacto.cargarContactosAsociados(listaContactos);
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
        comboOrdenar.getItems().addAll("Nombre", "País", "Tipo de contacto");
        vboxContactos.setAlignment(Pos.CENTER);
        btnSeleccionarImagen.setVisible(false);
        btnSeleccionarImagen.setOnAction(e -> seleccionarImagen());
        btnSiguiente.setOnAction(e -> siguienteContacto());
        btnAtras.setOnAction(e -> anteriorContacto());
        btnEditar.setOnAction(e -> alternarModoEdicion());
        btnEliminar.setOnAction(e -> eliminarContacto());
        btnFotoSiguiente.setOnAction(e -> mostrarFotoSiguiente());
        btnFotoAnterior.setOnAction(e -> mostrarFotoAnterior());
        btnOrdenar.setOnAction(e -> ordenar());
        btnFiltrar.setOnAction(e -> filtrarContactos());
        btnAsociar.setOnAction(e -> {
        Contacto seleccionado = comboContactosAsociar.getValue();
            if (seleccionado != null && nodoActual != null) {
                // Evita duplicados
                if (!nodoActual.dato.getContactosAsociados().contiene(seleccionado)) {
                    nodoActual.dato.agregarContactoAsociado(seleccionado);
                    guardarAsociadosArchivo(nodoActual.dato, seleccionado);
                    mostrarMensaje("Contacto asociado correctamente: " + seleccionado.getNombre());
                } else {
                    mostrarMensaje("Este contacto ya está asociado.");
                }
            }
        });
        comboContactosAsociar.setPromptText("Selecciona un contacto");
    }
    
    private void ordenar() {
        String criterio = comboOrdenar.getValue();
        if (criterio != null) {
            ordenarContactos(criterio.toLowerCase());
            mostrarMensaje("Contactos ordenados por " + criterio);
        } else {
            mostrarMensaje("Seleccione un criterio de ordenamiento");
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
        
        ListaDobleCircular<Contacto> asociados = nodoActual.dato.getContactosAsociados();
        if (asociados == null || asociados.estaVacia()) {
            System.out.println("Este contacto no tiene contactos asociados.");
        } else {
            System.out.println("Contactos asociados:");
            NodoDobleCircular<Contacto> nodoAsociado = asociados.cabeza;
            do {
                System.out.println("  - " + nodoAsociado.dato.getNombre());
                nodoAsociado = nodoAsociado.siguiente;
            } while (nodoAsociado != asociados.cabeza);
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
        vboxImagenYBoton.getChildren().addAll(imgFoto, btnSeleccionarImagen);
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
        cargarContactosParaAsociar(); 
        hboxAsociar = new HBox(10, comboContactosAsociar, btnAsociar);
        hboxAsociar.setAlignment(Pos.CENTER);
        contenedor.getChildren().add(hboxAsociar);
        hboxAsociar.setVisible(false);
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
    
    private void alternarModoEdicion() {
        if (modoEdicion) {
            // Guardar cambios y desactivar campos
            guardarCambios();
            for (TextField campo : camposActuales) {
                campo.setDisable(true);
            }
            btnSeleccionarImagen.setVisible(false);
            if (hboxAsociar != null) hboxAsociar.setVisible(false);
            btnEditar.setText("Editar");
            modoEdicion = false;
        } else {
            // Activar edición
            for (TextField campo : camposActuales) {
                campo.setDisable(false);
            }
            btnSeleccionarImagen.setVisible(true);
            if (hboxAsociar != null) hboxAsociar.setVisible(true);
            btnEditar.setText("Guardar");
            modoEdicion = true;
        }
    }
    
    
    
    private void guardarAsociadosArchivo(Contacto dueño, Contacto asociado) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("recursos/usuariosAsociados.txt", true))) {
            bw.write(dueño.getId() + "," + asociado.getId());
            bw.newLine();
        } catch (IOException e) {
            mostrarMensaje("Error al guardar asociación: " + e.getMessage());
        }
    }
    
    private void cargarContactosParaAsociar() {
        comboContactosAsociar.getItems().clear();

        if (!listaContactos.estaVacia()) {
            NodoDobleCircular<Contacto> actual = listaContactos.cabeza;
            do {
                // Agrega todos menos el contacto actual (nodoActual)
                if (!actual.dato.getId().equals(nodoActual.dato.getId())) {
                    comboContactosAsociar.getItems().add(actual.dato);
                }
                actual = actual.siguiente;
            } while (actual != listaContactos.cabeza);
        }

        // Establece la forma en que se muestra cada item (nombre + id)
        comboContactosAsociar.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Contacto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " (ID: " + item.getId() + ")");
                }
            }
        });
        comboContactosAsociar.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contacto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " (ID: " + item.getId() + ")");
                }
            }
        });
    }
    
    private void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            archivoImagenSeleccionada = file;
            Image image = new Image(file.toURI().toString());
            imgFoto.setImage(image);
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
        
        // Si se seleccionó una nueva imagen, copiarla y guardar referencia
        if (archivoImagenSeleccionada != null) {
            File carpetaImagenes = new File("imagenes/");
            if (!carpetaImagenes.exists()) {
                carpetaImagenes.mkdirs();
            }

            String nombreOriginal = archivoImagenSeleccionada.getName();
            File destino = new File(carpetaImagenes, nombreOriginal);

            int contador = 1;
            String nombreSinExtension = nombreOriginal.substring(0, nombreOriginal.lastIndexOf('.'));
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));

            while (destino.exists()) {
                String nuevoNombre = nombreSinExtension + "_" + contador + extension;
                destino = new File(carpetaImagenes, nuevoNombre);
                contador++;
            }

            try {
                Files.copy(archivoImagenSeleccionada.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Agregar foto al contacto en memoria
                Foto foto = new Foto("imagenes/" + destino.getName(), LocalDate.now());
                c.agregarFoto(foto);

                // Guardar archivo imagenes.txt
                try (BufferedWriter fotoWriter = new BufferedWriter(new FileWriter("recursos/imagenes.txt", true))) {
                    String rutaRelativa = "imagenes/" + destino.getName();
                    String fechaHoy = LocalDate.now().toString();
                    fotoWriter.write(c.getId() + "," + rutaRelativa + "," + fechaHoy + System.lineSeparator());
                }

                // Limpiar la selección para no volver a guardar la misma imagen
                archivoImagenSeleccionada = null;

            } catch (IOException ex) {
                System.out.println("Error al copiar la imagen: " + ex.getMessage());
            }
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
    private void eliminarContacto() {
        if (listaContactos.estaVacia() || nodoActual == null) {
            mostrarMensaje("No hay contactos para eliminar.");
            return;
        }

        Contacto contactoAEliminar = nodoActual.dato;

        // Eliminar del archivo usuarios.txt
        File archivoOriginal = new File("recursos/usuarios.txt");
        File archivoTemporal = new File("recursos/usuarios_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoOriginal)); BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTemporal))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.contains(contactoAEliminar.getId())) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            mostrarMensaje("Error al eliminar del archivo usuarios.txt: " + e.getMessage());
            return;
        }

        archivoOriginal.delete();
        archivoTemporal.renameTo(archivoOriginal);

        // Eliminar entradas del archivo imagenes.txt
        File archivoFotos = new File("recursos/imagenes.txt");
        File archivoFotosTemp = new File("recursos/imagenes_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoFotos)); BufferedWriter writer = new BufferedWriter(new FileWriter(archivoFotosTemp))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.startsWith(contactoAEliminar.getId() + ",")) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            mostrarMensaje("Error al eliminar fotos del archivo imagenes.txt: " + e.getMessage());
            return;
        }

        archivoFotos.delete();
        archivoFotosTemp.renameTo(archivoFotos);

        // Eliminar del sistema (de la lista en memoria)
        listaContactos.eliminar(contactoAEliminar);

        mostrarMensaje("Contacto eliminado correctamente.");

        // Mostrar siguiente o mensaje si ya no hay contactos
        if (!listaContactos.estaVacia()) {
            nodoActual = listaContactos.cabeza;
            mostrarContacto(nodoActual.dato);
        } else {
            vboxContactos.getChildren().clear();
            mostrarMensaje("No hay más contactos.");
        }
    }
    
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    
    public void ordenarContactos(String criterio) {
        Comparator<Contacto> comparador;
        switch (criterio.toLowerCase()) {
            case "nombre":
                comparador = comparadorPorNombre;
                break;
            case "país":
                comparador = comparadorPorPais;
                break;
            case "tipo de contacto":
                comparador = comparadorPorTipo;
                break;
            default:
                comparador = comparadorPorNombre;
        }
        System.out.println("Ordenando por: " + criterio);
        listaContactos.ordenarPor(comparador);
        nodoActual = listaContactos.cabeza;
        mostrarContacto(nodoActual.dato);
    }
    
    
    @FXML
    private void filtrarContactos() {
        String entrada = txtFiltro.getText().trim().toLowerCase();

        if (entrada.isBlank()) {
            // Si el usuario borra el filtro, vuelve a mostrar toda la lista original
            listaFiltrada = null;  // o usa listaContactos directamente
            nodoActual = listaContactos.cabeza;
            mostrarContacto(nodoActual.dato);
            mostrarMensaje("Mostrando todos los contactos.");
        } else {
            aplicarFiltro(entrada);  // llama al método privado que tú tienes
        }
    }
    
    private void aplicarFiltro(String entrada) {
        Predicate<Contacto> filtro;

        if (entrada.equals("empresa")) {
            filtro = c -> c instanceof Empresa;
        } else if (entrada.equals("persona")) {
            filtro = c -> c instanceof PersonaNatural;
        } 
         else {
            filtro = c -> c.getNombre().toLowerCase().contains(entrada.toLowerCase()) ||
                     (c.getPais() != null && c.getPais().toLowerCase().contains(entrada));
        }

        listaFiltrada = new ListaDobleCircular<>();

       if (!listaContactos.estaVacia()) {
        NodoDobleCircular<Contacto> actual = listaContactos.cabeza;
        do {
            if (filtro.test(actual.dato)) {
                listaFiltrada.agregar(actual.dato);
            }
            actual = actual.siguiente;
        } while (actual != listaContactos.cabeza);
    }

        if (!listaFiltrada.estaVacia()) {
            nodoActual = listaFiltrada.cabeza;
            mostrarContacto(nodoActual.dato);
            mostrarMensaje("Filtrado aplicado: " + entrada);
        } else {
            mostrarMensaje("No se encontraron contactos con ese criterio.");
        }
    }
    
    @FXML
    private void switchCancelar() throws IOException {
        App.setRoot("mainView");
    }
    
    @FXML
    private void verContactosAsociados() {
        if (nodoActual != null && nodoActual.dato != null) {
            ListaDobleCircular<Contacto> asociados = nodoActual.dato.getContactosAsociados();
            if (asociados == null || asociados.estaVacia()) {
                mostrarMensaje("Este contacto no tiene contactos asociados.");
                return;
            }

            try {
                // Cargar FXML de la vista de asociados
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/prueba/asociadosView.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y pasarle la lista de asociados
                AsociadosViewController controlador = loader.getController();
                controlador.setContactosAsociados(asociados);

                // Mostrar en nueva ventana
                Stage stage = new Stage();
                stage.setTitle("Contactos Asociados");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarMensaje("No hay un contacto seleccionado.");
        }
    }
}

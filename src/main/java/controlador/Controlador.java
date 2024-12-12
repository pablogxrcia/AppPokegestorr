package controlador;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import modelo.Entrenador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox; 
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import modelo.Captura;
import modelo.Pokemon;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

 

public class Controlador implements Initializable{
    
    Connection conexion;
    Statement st;
    ResultSet rs;

    @FXML
    private TableView<Entrenador> entrenadorTable;
    @FXML
    private TableView<Captura> capturaTable;
    @FXML
    private TableView<Pokemon> pokemonTable;
    @FXML
    private TableColumn<Entrenador, String> colID;
    @FXML
    private ImageView imgBuscadorPokemon;
    @FXML
    private ImageView imgFondoCaptura;
    @FXML
    private ImageView imgBuscadorPokemon2;
    @FXML
    private TableColumn<Entrenador, String> colNombre;
    @FXML
    private TableColumn<Entrenador, String> colEdad;
    @FXML
    private TableColumn<Entrenador, String> colMedallas;
    @FXML
    private TableColumn<Entrenador, String> colRegion;
    @FXML
    private TableColumn<Entrenador, String> colSexo;
    @FXML
    private TableColumn<Captura, String> colEntrenadorCaptura;
    @FXML
    private TableColumn<Captura, String> colFechaCaptura;
    @FXML
    private TableColumn<Pokemon, String> colIDPokemon;
    @FXML
    private TableColumn<Pokemon, ImageView> colImagenPokemon;
    @FXML
    private TableColumn<Captura, String> colLocalizacionCaptura;
    @FXML
    private TableColumn<Pokemon, String> colNivelPokemon;
    @FXML
    private TableColumn<Pokemon, String> colNombrePokemon;
    @FXML
    private TableColumn<Pokemon, String> colPokedexPokemon;
    @FXML
    private TableColumn<Captura, String> colPokemonCaptura;
    @FXML
    private TableColumn<Pokemon, ImageView> colTipoPokemon;
     @FXML
    private TableColumn<Captura, String> colIdEntrenador;
    @FXML
    private TableColumn<Captura, String> colIdPokemon;
    @FXML
    private TextField searchFieldCaptura;
    @FXML
    private TextField searchFieldEntrenador;
    @FXML
    private TextField searchFieldPokemon;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnExit;
    @FXML
    private ImageView imgAñadir;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgSalir;
    @FXML
    private TabPane tabPane;
    
    List<ValidationSupport> validadores;
    
    // Declaración de las variables de instancia Entrenador
    private TextField txtNombreEntrenador;
    private Spinner<Integer> spinerEdadEntrenador;
    private ComboBox<Integer> comboMedallasEntrenador;
    private TextField txtRegionEntrenador;
    private RadioButton selectedSexoEntrenador;
    
    // Declaración de las variables de instancia Pokemon
    private TextField txtNombrePokemon;
    private Spinner<Integer> spinerNivelPokemon;
    private Spinner<Integer> spinerPokedex;
    private ComboBox<String> comboTipoPokemon;
    private File imagenSeleccionadaPokemon;
    private Button btnSeleccionarImagen;
    private ImageView imageViewPokemon;
    private Label lblValidacionImagen;
    
    //Declaracion de las variables de instancia Captura
    private TextField entrenadorComboBox;
    private TextField pokemonComboBox;
    private DatePicker fechaPicker;
    private TextField localizacionField;
    private ListView<String> listView;
    private ListView<String> listView2;
    
    //Declaracion Observables
    ObservableList<Entrenador> ObservableEntrenadores = FXCollections.observableArrayList();
    ObservableList<Pokemon> ObservablePokemons = FXCollections.observableArrayList();
    ObservableList<Captura> ObservableCapturas = FXCollections.observableArrayList();
    

    private Entrenador entrenadorCaptura;
    private Pokemon pokemonCaptura;

    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        try {
            conexion = this.getConnection();
            if (conexion != null) {
                this.st = conexion.createStatement();
            }
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
        }
        
        if (conexion != null) {
            establecerIconos();
            inicializarTablaEntrenadores();
            inicializarTablaPokemons();
            inicializarTablaCapturas();
         
            btnExit.setOnAction(e -> salirPrograma());
            
            configurarBotonesPorTab();
            configurarBuscadorEntrenador();
            configurarBuscadorPokemon();
            configurarBuscadorCaptura();
    
            tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
                configurarBotonesPorTab();
            });
            
            configurarSeleccionTablaEntrenador();
            configurarSeleccionTablaPokemon();
            configurarSeleccionTablaCaptura();
            
            pokemonTable.setRowFactory(tv -> {
                TableRow<Pokemon> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        Pokemon pokemonSeleccionado = row.getItem();
                        mostrarDetallesPokemon(pokemonSeleccionado); // Llama al método que abre la ventana
                    }
                });
                return row;
            });
            entrenadorTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Detectar doble clic
                    Entrenador entrenadorSeleccionado = entrenadorTable.getSelectionModel().getSelectedItem();
                    if (entrenadorSeleccionado != null) {
                        fichaEntrenador(entrenadorSeleccionado);
                    } else {
                        mostrarAlerta("Atención", "Selecciona un entrenador para editar.");
                    }
                }
            });
            
            Platform.runLater(()->{
            capturaTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Captura capturaSeleccionada = capturaTable.getSelectionModel().getSelectedItem();
                    if (capturaSeleccionada != null) {
                        pokemonCaptura = buscarPokemonPorId(capturaSeleccionada.getIdPokemon());
                                            System.out.println(pokemonCaptura);

                        entrenadorCaptura = buscarEntrenadorPorId(capturaSeleccionada.getIdEntrenador());
                                            System.out.println(entrenadorCaptura);

                      
                        if (pokemonCaptura == null || entrenadorCaptura == null) {
                            mostrarAlerta("Error", "No se encontró el Pokémon o el Entrenador asociado a esta captura.");
                        } else {
                            mostrarDetallesCaptura(capturaSeleccionada, pokemonCaptura, entrenadorCaptura);
                        }
                    }
                }
            });
            });
            
        }
    }
    
    private void salirPrograma() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir de la aplicación");
        alert.setHeaderText("¿Estás seguro de que deseas salir?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    private void mostrarAlerta(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void establecerIconos(){
        imgFondoCaptura.setImage(new Image(getClass().getClassLoader().getResourceAsStream("fondoCaptura.png")));
        imgAñadir.setImage(new Image(getClass().getClassLoader().getResourceAsStream("boton-añadir.png")));
        imgEditar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("boton-editar.png")));
        imgEliminar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("boton-eliminar.png")));
        imgSalir.setImage(new Image(getClass().getClassLoader().getResourceAsStream("boton-salir.png")));
        imgBuscadorPokemon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-azul.png")));
        imgBuscadorPokemon2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-azul.png")));
    }
    
    private void configurarBotonesPorTab() {
        switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
            case "Entrenadores" -> configurarBotonesEntrenadores();
            case "Pokemons" -> configurarBotonesPokemon();
            case "Capturas" -> configurarBotonesCaptura();
        }
    }
    
    public Connection getConnection() throws IOException {
        //Importante: hay que separar los datos de conexión del programa, así, al cambiar, no tendría
        //que cambiar nada internamente, o al menos, el mínimo posible.
        Properties properties = new Properties();
        String IP, PORT, BBDD, USER, PWD;
        //Se lee IP desde fuera del jar
        try {
            InputStream input_ip = new FileInputStream("ip.properties");//archivo debe estar junto al jar
            properties.load(input_ip);
            IP = (String) properties.get("IP");
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo encontrar el archivo de propiedades para IP, se establece localhost por defecto");
            IP = "localhost";
        }

        InputStream input = getClass().getClassLoader().getResourceAsStream("bbdd.properties");
        if (input == null) {
            System.out.println("No se pudo encontrar el archivo de propiedades");
            return null;
        } else {
            // Cargar las propiedades desde el archivo
            properties.load(input);
            // String IP = (String) properties.get("IP"); //Tiene sentido leerlo desde fuera del Jar por si cambiamos la IP, el resto no debería de cambiar
            //ni debería ser público
            PORT = (String) properties.get("PORT");//En vez de crear con new, lo crea por asignación + casting
            BBDD = (String) properties.get("BBDD");
            USER = (String) properties.get("USER");//USER de MARIADB en LAMP 
            PWD = (String) properties.get("PWD");//PWD de MARIADB en LAMP 

            Connection conn;
            try {
                String cadconex = "jdbc:mariadb://" + IP + ":" + PORT + "/" + BBDD + " USER:" + USER + "PWD:" + PWD;
                System.out.println(cadconex);
                //Si usamos LAMP Funciona con ambos conectores
                conn = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + PORT + "/" + BBDD, USER, PWD);
                //conn = DriverManager.getConnection(cadconex);
                return conn;
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Ha ocurrido un error de conexión");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                exit(0);
                return null;
            }
        }
    }
    
    private void gestionarEntrenador(Entrenador entrenadorSeleccionado) {
        Stage dialog = new Stage();
        dialog.getIcons().add((entrenadorSeleccionado == null) ? new Image(getClass().getClassLoader().getResourceAsStream("boton-añadir.png")) : new Image(getClass().getClassLoader().getResourceAsStream("boton-editar.png"))  );
        String title = (entrenadorSeleccionado == null) ? "Añadir Entrenador" : "Editar Entrenador";
        dialog.setTitle(title);

        VBox mainLayout = new VBox(15); 
        mainLayout.setPadding(new Insets(15));

        // Nombre
        HBox nombreBox = new HBox(10); 
        Label lblNombre = new Label("Nombre:");
        lblNombre.setMinWidth(100);
        txtNombreEntrenador = new TextField(entrenadorSeleccionado != null ? entrenadorSeleccionado.getNombre() : "");
        //txtNombre.setId("txtNombre");
        txtNombreEntrenador.setPrefWidth(200);
        Label etiqNombre = new Label();
        nombreBox.getChildren().addAll(lblNombre, txtNombreEntrenador, etiqNombre);

        // Edad
        HBox edadBox = new HBox(10);
        Label lblEdad = new Label("Edad:");
        lblEdad.setMinWidth(100);
        spinerEdadEntrenador = new Spinner<>(1, 120, entrenadorSeleccionado != null ? entrenadorSeleccionado.getEdad() : 18);
        //spinerEdad.setId("spinerEdad");
        spinerEdadEntrenador.setPrefWidth(100);
        Label etiqEdad= new Label();
        edadBox.getChildren().addAll(lblEdad, spinerEdadEntrenador, etiqEdad);
        spinerEdadEntrenador.setEditable(false);
    
        // Número de Medallas
        HBox medallasBox = new HBox(10);
        Label lblMedallas = new Label("Num. Medallas:");
        lblMedallas.setMinWidth(100);
        comboMedallasEntrenador = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));
        //comboMedallas.setId("comboMedallas");
        comboMedallasEntrenador.setValue(entrenadorSeleccionado != null ? entrenadorSeleccionado.getNumMedallas() : 1);
        comboMedallasEntrenador.setPrefWidth(100); 
        medallasBox.getChildren().addAll(lblMedallas, comboMedallasEntrenador);

        // Región
        HBox regionBox = new HBox(10);
        Label lblRegion = new Label("Región:");
        lblRegion.setMinWidth(100);
        txtRegionEntrenador = new TextField(entrenadorSeleccionado != null ? entrenadorSeleccionado.getRegion() : "");
        txtRegionEntrenador.setPrefWidth(200);
        Label etiqRegion= new Label();
        regionBox.getChildren().addAll(lblRegion, txtRegionEntrenador, etiqRegion);
        
        // Sexo
        HBox sexoBox = new HBox(10);
        Label lblSexo = new Label("Sexo:");
        lblSexo.setMinWidth(100);
        
        ToggleGroup sexoGroup = new ToggleGroup();
        
        RadioButton rbHombre = new RadioButton("Hombre");
        rbHombre.setToggleGroup(sexoGroup);
        rbHombre.setUserData("Hombre");
        
        RadioButton rbMujer = new RadioButton("Mujer");
        rbMujer.setToggleGroup(sexoGroup);
        rbMujer.setUserData("Mujer");
        
        RadioButton rbOtro = new RadioButton("Otro");
        rbOtro.setToggleGroup(sexoGroup);
        rbOtro.setUserData("Otro");

        if (entrenadorSeleccionado != null) {
            switch (entrenadorSeleccionado.getSexo()) {
                case "Hombre" -> rbHombre.setSelected(true);
                case "Mujer" -> rbMujer.setSelected(true);
                case "Otro" -> rbOtro.setSelected(true);
            }
        }
       

        sexoBox.getChildren().addAll(lblSexo, rbHombre, rbMujer, rbOtro);

        // Botones
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); 
        Button btnConfirmar = new Button("Confirmar");
        configurarEfectoHover(btnConfirmar);
        btnConfirmar.setPrefWidth(80); 
        Button btnCancelar = new Button("Cancelar");
        configurarEfectoHover(btnCancelar);
        btnCancelar.setPrefWidth(80);
        buttonBox.getChildren().addAll(btnConfirmar, btnCancelar);

        mainLayout.getChildren().addAll(nombreBox, edadBox, medallasBox, regionBox, sexoBox, buttonBox);
        
        lblNombre.setTooltip(new Tooltip("El nombre debe tener entre 2 y 10 caracteres"));
        lblEdad.setTooltip(new Tooltip("La edad tiene que ser entre 1 y 100"));
        lblRegion.setTooltip(new Tooltip("El nombre debe tener entre 2 y 20 caracteres"));

        comprobarCamposEntrenador();
        // Eventos
        btnConfirmar.setOnAction(e -> {
            boolean camposValidos = validadores.stream().allMatch(validator -> 
                validator.getValidationResult().getErrors().isEmpty()); // Comprobar si no hay errores en cada validator

            if (!camposValidos) {
                mostrarAlerta("Error", "Por favor, corrige los campos marcados antes de continuar.");
                return;
            }

            // Si todos los campos son válidos, continúa con la lógica
            if (txtNombreEntrenador.getText().isEmpty() || txtRegionEntrenador.getText().isEmpty() || sexoGroup.getSelectedToggle() == null) {
                mostrarAlerta("Error", "Todos los campos deben estar rellenos.");
            } else {
                selectedSexoEntrenador = (RadioButton) sexoGroup.getSelectedToggle();

                if (entrenadorSeleccionado == null) {
                    añadirEntrenador();
                } else {
                    editarEntrenador(entrenadorSeleccionado);
                }
                dialog.close();
            }
        });


        btnCancelar.setOnAction(e -> dialog.close());

        Scene scene = new Scene(mainLayout, 340, 270);
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
    
   
    private void fichaEntrenador(Entrenador entrenadorSeleccionado) {
    Stage dialog = new Stage();
    dialog.setTitle("Ficha de Entrenador");
    

    // VBox principal
    VBox mainLayout = new VBox(10);
    mainLayout.setPadding(new Insets(15));
    mainLayout.setStyle("-fx-background-color:  #659CD2;"
            + "-fx-border-color:  #014689; -fx-border-width: 4px; -fx-border-radius: 8px;");

    // Título
    HBox titulo = new HBox(10);
    ImageView imgPokeball1 = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-azul.png")));
    imgPokeball1.setFitHeight(30);
    imgPokeball1.setFitWidth(30);
    
    Label lblTitulo = new Label("FICHA DE ENTRENADOR");
    lblTitulo.setStyle(" -fx-text-fill: white;");
    
    ImageView imgPokeball2 = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-azul.png")));
    imgPokeball2.setFitHeight(30);
    imgPokeball2.setFitWidth(30);
    
    titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color:  #014689; "
            + "-fx-padding: 5px; -fx-alignment: center; -fx-border-radius: 8px; ");
    titulo.setMaxWidth(Double.MAX_VALUE);
    titulo.setAlignment(Pos.CENTER);
    titulo.getChildren().addAll(imgPokeball1,lblTitulo,imgPokeball2);

    // VBox para los datos del entrenador
    VBox datosVBox = new VBox(10);

    // Nombre
    HBox nombreBox = new HBox(10);
    nombreBox.setAlignment(Pos.CENTER);
    nombreBox.setMinWidth(200);
    Label lblNombre = new Label("Nombre");
    lblNombre.setMinWidth(80);
    Label txtNombre = new Label(entrenadorSeleccionado != null ? entrenadorSeleccionado.getNombre() : "");
    txtNombre.setPrefWidth(100);
    txtNombre.setPrefHeight(30); // Asegura que todos los TextField tengan la misma altura
    nombreBox.getChildren().addAll(lblNombre, txtNombre);
    nombreBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" 
        + "-fx-border-radius: 8px; -fx-background-radius: 8px;"
        + "-fx-padding: 4px;"); 
    
    // Edad
    HBox edadBox = new HBox(10);
    edadBox.setAlignment(Pos.CENTER);
    Label lblEdad = new Label("Edad");
    lblEdad.setMinWidth(80);
    edadBox.setStyle("-fx-font-weight: bold;");
    Label spinerEdad = new Label(entrenadorSeleccionado != null ? entrenadorSeleccionado.getEdad() + "" : "Desconocido");
    spinerEdad.setPrefWidth(100);
    spinerEdad.setPrefHeight(30); // Asegura que todos los TextField tengan la misma altura
    edadBox.getChildren().addAll(lblEdad, spinerEdad);
    edadBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" 
        + "-fx-border-radius: 8px; -fx-background-radius: 8px;"
        + "-fx-padding: 4px;"); 

    // Medallas
    HBox medallasBox = new HBox(10);
    medallasBox.setAlignment(Pos.CENTER);
    Label lblMedallas = new Label("Medallas");
    medallasBox.setStyle("-fx-font-weight: bold;");
    lblMedallas.setMinWidth(80);
    Label comboMedallas = new Label();
    comboMedallas.setPrefWidth(100);
    comboMedallas.setText(entrenadorSeleccionado != null ? entrenadorSeleccionado.getNumMedallas()+ "" : "Desconocido");
    comboMedallas.setPrefHeight(30); // Asegura que todos los TextField tengan la misma altura
    medallasBox.getChildren().addAll(lblMedallas, comboMedallas);
    medallasBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" 
        + "-fx-border-radius: 8px; -fx-background-radius: 8px;"
        + "-fx-padding: 4px;"); 

    // Región
    HBox regionBox = new HBox(10);
    regionBox.setAlignment(Pos.CENTER);
    Label lblRegion = new Label("Región");
    lblRegion.setMinWidth(80);
    regionBox.setStyle("-fx-font-weight: bold;");
    Label txtRegion = new Label(entrenadorSeleccionado != null ? entrenadorSeleccionado.getRegion() : "Desconocido");
    txtRegion.setPrefWidth(100);
    txtRegion.setPrefHeight(30); // Asegura que todos los TextField tengan la misma altura
    regionBox.getChildren().addAll(lblRegion, txtRegion);
    regionBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" 
        + "-fx-border-radius: 8px; -fx-background-radius: 8px;"
        + "-fx-padding: 4px;"); 
    
    

    // Añadir filas al VBox de datos
    datosVBox.getChildren().addAll(nombreBox, edadBox, medallasBox, regionBox);
    
    VBox informacionImagen = new VBox(10);
    
    HBox imagenBox = new HBox(10);
    imagenBox.setAlignment(Pos.CENTER);
    Label lblId = new Label("Nº ID");
    lblId.setMinWidth(40);
    imagenBox.setStyle("-fx-font-weight: bold;");
    Label txtID = new Label(entrenadorSeleccionado != null ? entrenadorSeleccionado.getId()+ "" : "Desconocido");
    txtID.setPrefWidth(30);
    txtID.setPrefHeight(30); // Asegura que todos los TextField tengan la misma altura
    imagenBox.getChildren().addAll(lblId, txtID);
    imagenBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" 
        + "-fx-border-radius: 8px; -fx-background-radius: 8px;"
        + "-fx-padding: 4px;"); 
    
    
    // Contenedor para el ImageView con estilo
    HBox imageContainer = new HBox();
    imageContainer.setAlignment(Pos.CENTER);
    imageContainer.setPrefHeight(150);
    imageContainer.setPrefWidth(270);
    imageContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);"
            + "-fx-border-radius: 8px; -fx-background-radius: 8px;");

    // Imagen del entrenador
    ImageView imageView = new ImageView();
    imageView.setFitWidth(230);
    imageView.setFitHeight(140);
    imageView.setPreserveRatio(true);

    // Seleccionar la imagen según el sexo
    String sexo = entrenadorSeleccionado != null ? entrenadorSeleccionado.getSexo()+ "" : "Desconocido"; // Por defecto "Otro"
    Image image = switch (sexo.toLowerCase()) {
        case "hombre" -> new Image(getClass().getClassLoader().getResourceAsStream("entrenador.png"));
        case "mujer" -> new Image(getClass().getClassLoader().getResourceAsStream("entrenadora.png"));
        case "otro" -> new Image(getClass().getClassLoader().getResourceAsStream("otro.png"));
        default -> null;
    };

    try {
        imageView.setImage(image);
    } catch (Exception e) {
        System.err.println("No se pudo cargar la imagen: ");
    }

    // Añadir el ImageView al contenedor
    imageContainer.getChildren().add(imageView);
    
    informacionImagen.getChildren().addAll(imagenBox, imageContainer);

    // Contenedor HBox: datos + imagen
    HBox contenidoHBox = new HBox(40);
    contenidoHBox.getChildren().addAll(datosVBox, informacionImagen);
    // Boton
    HBox buttonBox = new HBox();
    buttonBox.setAlignment(Pos.BOTTOM_LEFT);
    Button btnConfirmar = new Button("Confirmar");
    configurarEfectoHover(btnConfirmar);
    btnConfirmar.requestFocus();
    btnConfirmar.setPrefWidth(100);
    btnConfirmar.setStyle("-fx-background-color:  #014689; -fx-text-fill: white; -fx-font-weight: bold;");
    buttonBox.getChildren().add(btnConfirmar);

    // Botón Confirmar
    btnConfirmar.setOnAction(e -> {
        // Aquí añades la lógica para confirmar
        dialog.close();
    });

    // Añadir elementos al VBox principal
    mainLayout.getChildren().addAll(titulo, contenidoHBox,buttonBox);

    Scene scene = new Scene(mainLayout, 420, 310);
    dialog.setScene(scene);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.showAndWait();
}



  
    public void comprobarCamposEntrenador(){
        
        ValidationSupport vSnombre= new ValidationSupport();
        vSnombre.registerValidator(txtNombreEntrenador, false,(Control c, String texto) -> {
            if (texto.length() == 0) {
                return ValidationResult.fromWarning(c, "El nombre no debe estar vacío");
            } else if (texto.length() < 1 || texto.length() > 10) {
                return ValidationResult.fromError(c, "El nombre debe tener entre 2 y 10 caracteres");
            } else {
                return ValidationResult.fromInfo(c, "OK");
            }

        });
        
        ValidationSupport vSRegion= new ValidationSupport();
        vSRegion.registerValidator(txtRegionEntrenador, false,(Control c, String texto) -> {
            if (texto.length() == 0) {
                return ValidationResult.fromWarning(c, "La región no debe estar vacía");
            } else if (texto.length() < 1 || texto.length() > 10) {
                return ValidationResult.fromError(c, "La región debe tener entre 2 y 20 caracteres");
            } else {
                return ValidationResult.fromInfo(c, "OK");
            }

        });
        
        ValidationSupport vSEdad = new ValidationSupport();
        vSEdad.registerValidator(spinerEdadEntrenador.getEditor(),false, (Control c, String texto) -> {
            try {
                int edad = spinerEdadEntrenador.getValue();
                if (edad <= 0 || edad >= 100) {
                    return ValidationResult.fromError(c, "La edad debe ser mayor que 1 y menor que 100.");
                } else {
                    return ValidationResult.fromInfo(c, "OK");
                }
            } catch (NumberFormatException e) {
                return ValidationResult.fromWarning(c, "Ingresa un número válido.");
            }
        });
        validadores = new ArrayList<>();
        validadores.addAll(Arrays.asList(vSnombre, vSEdad, vSRegion));
    }
    
    public ObservableList<Entrenador> dameListaEntrenadores() {
        if (conexion != null) {
            ObservableEntrenadores.clear(); //Limpiamos el contenido actual
            String query = "SELECT * FROM ENTRENADOR";
            try {
                rs = st.executeQuery(query);
                Entrenador entrenador;
                while (rs.next()) { //Se usan los identificadores propios en la BBDD (no es case sensitive). Revisar en phpmyadmin
                    entrenador = new Entrenador(rs.getInt("id_entrenador"), rs.getString("nombre"), rs.getInt("edad"), rs.getInt("medallas"), rs.getString("region"),rs.getString("sexo"));
                    ObservableEntrenadores.add(entrenador);
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: "+e.getMessage());
            }
            return ObservableEntrenadores;
        }
        return null;
    }
    
    private void inicializarTablaEntrenadores(){
        colID.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colEdad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEdad())));
        colMedallas.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumMedallas())));
        colRegion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRegion()));
        colSexo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSexo()));
        
        entrenadorTable.setItems(dameListaEntrenadores());
    }
    
    private void configurarBuscadorEntrenador() {
        searchFieldEntrenador.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarEntrenadores(newValue);
        });
    }
    
    private void filtrarEntrenadores(String criterio) {
        ObservableList<Entrenador> listaCompleta = dameListaEntrenadores();
        
        FilteredList<Entrenador> listaFiltrada = new FilteredList<>(listaCompleta, entrenador -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return entrenador.getNombre().toLowerCase().contains(criterioEnMinusculas) ||
                   entrenador.getRegion().toLowerCase().contains(criterioEnMinusculas);
        });

        entrenadorTable.setItems(listaFiltrada);
    }
    
    private void añadirEntrenadorVentana() {
        gestionarEntrenador(null);
    }
    
    private void editarEntrenadorVentana() {
        Entrenador entrenadorSeleccionado = entrenadorTable.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado != null) {
            gestionarEntrenador(entrenadorSeleccionado);
        }
    }
    
    private void eliminarEntrenadorVentana() {
        
        Entrenador entrenadorSeleccionado = entrenadorTable.getSelectionModel().getSelectedItem();
        if (entrenadorSeleccionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Entrenador");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar al entrenador seleccionado?");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("boton-eliminar.png")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eliminarEntrenador(entrenadorSeleccionado);
        }
    }
    
    private void configurarBotonesEntrenadores() {
        btnAdd.setOnAction(event -> añadirEntrenadorVentana()); 
        btnEdit.setOnAction(event -> editarEntrenadorVentana());
        btnDelete.setOnAction(event -> eliminarEntrenadorVentana());
    }
    
    private void configurarSeleccionTablaEntrenador() {
        entrenadorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean itemSelected = newValue != null;
            btnEdit.setDisable(!itemSelected);
            btnDelete.setDisable(!itemSelected);
        });
    }
    
    private void añadirEntrenador() {
    String query = "INSERT INTO ENTRENADOR (nombre, edad, sexo, region, medallas) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            
            preparedStatement.setString(1, txtNombreEntrenador.getText());  
            preparedStatement.setInt(2, spinerEdadEntrenador.getValue());  
            preparedStatement.setString(3,(String) selectedSexoEntrenador.getUserData());  
            preparedStatement.setString(4, txtRegionEntrenador.getText());  
            preparedStatement.setInt(5, comboMedallasEntrenador.getValue());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        entrenadorTable.setItems(dameListaEntrenadores());
    }

    private void editarEntrenador(Entrenador entrenadorSeleccionado) {
        String query = "UPDATE ENTRENADOR SET nombre=?, edad=?, sexo=?, region=?, medallas=? WHERE id_entrenador=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            
            preparedStatement.setString(1, txtNombreEntrenador.getText());  
            preparedStatement.setInt(2, spinerEdadEntrenador.getValue());  
            preparedStatement.setString(3, (String) selectedSexoEntrenador.getUserData()); 
            preparedStatement.setString(4, txtRegionEntrenador.getText());  
            preparedStatement.setInt(5, comboMedallasEntrenador.getValue()); 
            preparedStatement.setInt(6, entrenadorSeleccionado.getId());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        entrenadorTable.setItems(dameListaEntrenadores());
    }
    
    private void eliminarEntrenador(Entrenador entrenadorSeleccionado) {
        String query = "DELETE FROM ENTRENADOR WHERE id_entrenador=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            
            preparedStatement.setInt(1,entrenadorSeleccionado.getId());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: "+e.getMessage());
        }
        entrenadorTable.setItems(dameListaEntrenadores());
    }
    
    
    
    
    private void gestionarPokemon(Pokemon pokemonSeleccionado) { 
        Stage dialog = new Stage();
        dialog.getIcons().add((pokemonSeleccionado == null) ? new Image(getClass().getClassLoader().getResourceAsStream("boton-añadir.png")) : new Image(getClass().getClassLoader().getResourceAsStream("boton-editar.png"))  );
        String title = (pokemonSeleccionado == null) ? "Añadir Pokémon" : "Editar Pokémon";
        dialog.setTitle(title);

        VBox mainLayout = new VBox(15); 
        mainLayout.setPadding(new Insets(15));

        // Nombre
        HBox nombreBox = new HBox(10); 
        Label lblNombre = new Label("Nombre:");
        lblNombre.setMinWidth(100);
        txtNombrePokemon = new TextField(pokemonSeleccionado != null ? pokemonSeleccionado.getNombrePokemon(): "");
        txtNombrePokemon.setPrefWidth(150);
        nombreBox.getChildren().addAll(lblNombre, txtNombrePokemon);

        //Nivel
        HBox nivelBox = new HBox(10);
        Label lblNivel = new Label("Nivel:");
        lblNivel.setMinWidth(100);
        spinerNivelPokemon = new Spinner<>(1, 100, pokemonSeleccionado != null ? pokemonSeleccionado.getNivelPokemon(): 1);
        spinerNivelPokemon.setPrefWidth(100);
        nivelBox.getChildren().addAll(lblNivel, spinerNivelPokemon);
        spinerNivelPokemon.setEditable(false);

        // Número Pokédex
        HBox pokedexBox = new HBox(10);
        Label lblPokedex = new Label("Nº Pokédex:");
        lblPokedex.setMinWidth(100);
        spinerPokedex = new Spinner<>(1, 1000, pokemonSeleccionado != null ? pokemonSeleccionado.getNumPokedex(): 1);
        spinerPokedex.setPrefWidth(100);
        pokedexBox.getChildren().addAll(lblPokedex, spinerPokedex);
        spinerPokedex.setEditable(false);

        // Tipo
        HBox tipoBox = new HBox(10);
        Label lblTipo = new Label("Tipo:");
        lblTipo.setMinWidth(100);
        comboTipoPokemon = new ComboBox<>(FXCollections.observableArrayList(
            "Normal", "Fuego", "Agua", "Electrico", "Planta", "Hielo", "Lucha", "Veneno", 
            "Tierra", "Volador", "Psiquico", "Bicho", "Roca", "Fantasma", "Dragon", "Siniestro", 
            "Acero", "Hada"));
        comboTipoPokemon.setValue(pokemonSeleccionado != null ? pokemonSeleccionado.getTipo() : "Normal");
        comboTipoPokemon.setPrefWidth(150);
        tipoBox.getChildren().addAll(lblTipo, comboTipoPokemon);

        // Imagen
        VBox imagenBox = new VBox(10); // Cambiar HBox a VBox para apilar el botón y el ImageView verticalmente
        
        HBox hboxImagen = new HBox(10);
        Label lblImagen = new Label("Imagen:");
        lblImagen.setMinWidth(100);

        // Botón para seleccionar imagen
        btnSeleccionarImagen = new Button("Seleccionar archivo");
        btnSeleccionarImagen.setPrefWidth(150);
        hboxImagen.getChildren().addAll(lblImagen,btnSeleccionarImagen);
        
        // ImageView para mostrar la imagen seleccionada
        lblValidacionImagen = new Label();
        lblValidacionImagen.setVisible(false); 
        
        HBox hboxImageView = new HBox(10);
        
        imageViewPokemon = new ImageView();
        imageViewPokemon.setFitWidth(90);  
        imageViewPokemon.setFitHeight(90); 
        imageViewPokemon.setPreserveRatio(true); 
        if (pokemonSeleccionado!=null) {
            imageViewPokemon.setImage(base64ToImage(pokemonSeleccionado.getImagen()));
        }
        
        hboxImageView.getChildren().addAll(imageViewPokemon);
        hboxImageView.setAlignment(Pos.CENTER_RIGHT);
        //imageViewPokemon.set
        
        btnSeleccionarImagen.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(dialog);
            if (file != null) {
                imagenSeleccionadaPokemon = file;
                imageViewPokemon.setImage(new Image(file.toURI().toString()));
                lblValidacionImagen.setVisible(false);
            } else {
                lblValidacionImagen.setVisible(true); 
                
            }
        });

        // Añadir elementos al VBox
        imagenBox.getChildren().addAll(hboxImagen, hboxImageView,lblValidacionImagen);


        // Botones
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); 
        Button btnConfirmar = new Button("Confirmar");
        configurarEfectoHover(btnConfirmar);
        btnConfirmar.setPrefWidth(80); 
        Button btnCancelar = new Button("Cancelar");
        configurarEfectoHover(btnCancelar);
        btnCancelar.setPrefWidth(80);
        buttonBox.getChildren().addAll(btnConfirmar, btnCancelar);

        mainLayout.getChildren().addAll(nombreBox, nivelBox, pokedexBox, tipoBox, imagenBox, buttonBox);

        comprobarCamposPokemon();
        // Eventos
        btnConfirmar.setOnAction(e -> {
            boolean camposValidos = validadores.stream().allMatch(validator -> 
                validator.getValidationResult().getErrors().isEmpty()); // Comprobar si no hay errores en cada validator

            if (!camposValidos) {
                mostrarAlerta("Error", "Por favor, corrige los campos marcados antes de continuar.");
                return;
            }
            
            if (txtNombrePokemon.getText().isEmpty() || txtNombrePokemon.getText().trim().equals("") || comboTipoPokemon.getValue() == null || imagenSeleccionadaPokemon == null) {
                mostrarAlerta("Error", "Todos los campos deben estar rellenos.");
            } else {
                String imagenBase64 = convertirImagenA64(imagenSeleccionadaPokemon); 
                
                if (pokemonSeleccionado == null) {
                    añadirPokemon(imagenBase64);
                } else {
                    editarPokemon(pokemonSeleccionado, imagenBase64);
                }
                dialog.close();
            }
        });


        btnCancelar.setOnAction(e -> dialog.close());

        Scene scene = new Scene(mainLayout, 300, 390);
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        
    }
    
    private void comprobarCamposPokemon() {
        // Validador para el nombre
        ValidationSupport vSnombre= new ValidationSupport();
        vSnombre.registerValidator(txtNombrePokemon, false,(Control c, String texto) -> {
            if (texto.length() == 0) {
                return ValidationResult.fromWarning(c,"El nombre no debe estar vacío");
            } else if (texto.length() < 1 || texto.length() > 10) {
                return ValidationResult.fromError(c, "El nombre debe tener entre 2 y 10 caracteres");
            } else {
                return ValidationResult.fromInfo(c, "OK");
            }

        });
       ValidationSupport vSImageView = new ValidationSupport();
        vSImageView.registerValidator(lblValidacionImagen, false, (Control c, Object value) -> {
            if (imageViewPokemon.getImage() == null) {
                return ValidationResult.fromError(c, "Por favor, selecciona una imagen.");
            } else {
                return ValidationResult.fromInfo(c, "Imagen válida.");
            }
        });
        // Guardamos los validadores en una lista para comprobarlos fácilmente
        validadores = new ArrayList<>();
        validadores.addAll(Arrays.asList(vSnombre));
    }

    public void mostrarDetallesPokemon(Pokemon pokemonSeleccionado){
        
        // Crear el Stage (ventana)
        Stage stage = new Stage();
        stage.setTitle("Detalles del Pokémon");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        // VBox principal
        VBox mainLayout = new VBox();
        mainLayout.setPrefSize(554, 287);
        mainLayout.setStyle("-fx-background-color: #030732;");

        // HBox superior
        HBox topBar = new HBox(400);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPrefSize(450, 54);

     

        // HBox central
        HBox centralBox = new HBox();
        centralBox.setAlignment(Pos.CENTER_LEFT);
        centralBox.setPrefSize(506, 194);
        centralBox.setStyle("-fx-background-color: white; -fx-border-color: #71848C; -fx-border-width: 4;");
        VBox.setMargin(centralBox, new Insets(0, 20, 10, 20));

        // Imagen del Pokémon
        HBox pokemonImageBox = new HBox();
        pokemonImageBox.setAlignment(Pos.CENTER);
        pokemonImageBox.setPrefSize(160, 160);
        pokemonImageBox.setStyle("-fx-background-color: #71848C;");

        ImageView imgViewPokemon = new ImageView(base64ToImage(pokemonSeleccionado.getImagen()));
        imgViewPokemon.setFitHeight(80);
        imgViewPokemon.setFitWidth(80);

        pokemonImageBox.getChildren().add(imgViewPokemon);
        HBox.setMargin(pokemonImageBox, new Insets(10, 0, 10, 10));

        // VBox de la información
        VBox infoBox = new VBox(10);
        infoBox.setPrefSize(274, 161);

        // Primer VBox (ID y Nombre)
        VBox idAndNameBox = new VBox(10);
        idAndNameBox.setAlignment(Pos.CENTER_LEFT);
        idAndNameBox.setPrefSize(274, 55);
        idAndNameBox.setStyle("-fx-background-color: #D8D8D8;");
        VBox.setMargin(idAndNameBox, new Insets(10, 0, 0, 0));
        idAndNameBox.setPadding(new Insets(10, 0, 10, 10));

        Label idLabel = new Label("Nº "+pokemonSeleccionado.getIdPokemon());
        idLabel.setStyle("-fx-font-weight: bold;");
        Label nameLabel = new Label("Nombre: "+pokemonSeleccionado.getNombrePokemon());
        nameLabel.setStyle("-fx-font-weight: bold;");
        
        idAndNameBox.getChildren().addAll(idLabel, nameLabel);

        // Segundo VBox (Nivel y Num. Pokédex)
        VBox levelAndPokedexBox = new VBox(3);
        levelAndPokedexBox.setPrefSize(274, 70);
        levelAndPokedexBox.setPadding(new Insets(10, 0, 0, 25));

        Label levelLabel = new Label("Nivel: "+pokemonSeleccionado.getNivelPokemon());
        levelLabel.setStyle("-fx-font-weight: bold;");
        Button levelBar = new Button();
        levelBar.setPrefSize(230, 1);
        levelBar.setMinSize(230, 1);

        Label pokedexLabel = new Label("Num. Pokedex: "+pokemonSeleccionado.getNumPokedex());
        pokedexLabel.setStyle("-fx-font-weight: bold;");
        Button pokedexBar = new Button();
        pokedexBar.setPrefSize(230, 1);
        pokedexBar.setMinSize(230, 1);

        levelAndPokedexBox.getChildren().addAll(levelLabel, levelBar, pokedexLabel, pokedexBar);

        HBox tipoBox = new HBox(10);
        // Imagen del tipo del Pokémon
        ImageView imgViewTipo = new ImageView(obtenerImagenPorTipo(pokemonSeleccionado.getTipo()));
        imgViewTipo.setFitHeight(40);
        imgViewTipo.setFitWidth(40);
        VBox.setMargin(imgViewTipo, new Insets(0, 0, 10, 25));
        Label lblTipo = obtenerTextoPorTipo(pokemonSeleccionado.getTipo());
        tipoBox.getChildren().addAll(imgViewTipo,lblTipo);
        tipoBox.setPadding(new Insets(0, 0, 10, 25));
        tipoBox.setAlignment(Pos.CENTER_LEFT);
        
        infoBox.getChildren().addAll(idAndNameBox, levelAndPokedexBox, tipoBox);

        // VBox para la Pokéball
        VBox pokeballBox = new VBox();
        pokeballBox.setAlignment(Pos.TOP_CENTER);
        pokeballBox.setPrefSize(70, 100);

        ImageView imgViewPokeball = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-normal.png")));
        imgViewPokeball.setFitHeight(50);
        imgViewPokeball.setFitWidth(50);
        VBox.setMargin(imgViewPokeball, new Insets(15, 0, 0, 0));
        
        HBox botonBox = new HBox();
        Button btnCerrar = new Button("Confirmar");
        configurarEfectoHover(btnCerrar);
        botonBox.getChildren().add(btnCerrar);
        btnCerrar.setOnAction(e -> stage.close());
        btnCerrar.setPrefWidth(150);
        HBox.setMargin(btnCerrar, new Insets(10,15,15,430));

        pokeballBox.getChildren().add(imgViewPokeball);

        // Añadir al HBox central
        centralBox.getChildren().addAll(pokemonImageBox, infoBox, pokeballBox);

        // Añadir todo al VBox principal
        mainLayout.getChildren().addAll(topBar, centralBox,botonBox);

        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);

        // Ajustar el tamaño del Stage al contenido
        stage.sizeToScene();
        stage.showAndWait();
    }
    
    public ObservableList<Pokemon> dameListaPokemons() {
        if (conexion != null) {
            ObservablePokemons.clear(); //Limpiamos el contenido actual
            String query = "SELECT * FROM POKEMON";
            try {
                rs = st.executeQuery(query);
                Pokemon pokemon;
                while (rs.next()) { //Se usan los identificadores propios en la BBDD (no es case sensitive). Revisar en phpmyadmin
                    pokemon = new Pokemon(rs.getInt("id_pokemon"), rs.getString("nombre_pokemon"), rs.getInt("nivel_pokemon"), rs.getString("tipo"), rs.getInt("num_pokedex"),rs.getString("imagen"));
                    ObservablePokemons.add(pokemon);
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: "+e.getMessage());
            }
            return ObservablePokemons;
        }
        return null;
    }
    
    private void inicializarTablaPokemons(){        
        colIDPokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdPokemon())));
        colImagenPokemon.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView(base64ToImage(cellData.getValue().getImagen()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            return new SimpleObjectProperty<>(imageView);
        });
        //colTipoPokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTipo())));
        colTipoPokemon.setCellValueFactory(cellData -> {
            ImageView imageView = new ImageView(obtenerImagenPorTipo(cellData.getValue().getTipo()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            return new SimpleObjectProperty<>(imageView);
        });      
        colNombrePokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNombrePokemon())));
        colNivelPokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNivelPokemon())));
        colPokedexPokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumPokedex())));
        pokemonTable.setItems(dameListaPokemons());
    }
    
    private Image obtenerImagenPorTipo(String tipo) {
        return switch (tipo) {
            case "Normal"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Normal.jpg"));
            case "Fuego"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Fuego.jpg"));
            case "Agua"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Agua.jpg"));
            case "Electrico"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_El3Fctrico.jpg"));
            case "Planta"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Planta.jpg"));
            case "Hielo"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Hielo.jpg"));
            case "Lucha"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Lucha.jpg"));
            case "Veneno"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Veneno.jpg"));
            case "Tierra"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Tierra.jpg"));
            case "Volador"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Volador.jpg"));
            case "Psiquico"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Ps3Fquico.jpg"));
            case "Bicho"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Bicho.jpg"));
            case "Roca"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Roca.jpg"));
            case "Fantasma"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Fantasma.jpg"));
            case "Dragon"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Drag3Fn.jpg"));
            case "Siniestro"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Siniestro.jpg"));
            case "Acero"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Acero.jpg"));
            case "Hada"-> new Image(getClass().getClassLoader().getResourceAsStream("Type_Hada.jpg"));
            default-> null;
        };
    }
    
    private Label obtenerTextoPorTipo(String tipo) {
        Label label = new Label(tipo.toUpperCase());
        switch (tipo) {
            case "Normal" -> label.setStyle("-fx-text-fill: grey; -fx-font-weight: bold;");
            case "Fuego" -> label.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            case "Agua" -> label.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
            case "Electrico" -> label.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold;");
            case "Planta" -> label.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            case "Hielo" -> label.setStyle("-fx-text-fill: lightblue; -fx-font-weight: bold;");
            case "Lucha" -> label.setStyle("-fx-text-fill: darkred; -fx-font-weight: bold;");
            case "Veneno" -> label.setStyle("-fx-text-fill: purple; -fx-font-weight: bold;");
            case "Tierra" -> label.setStyle("-fx-text-fill: brown; -fx-font-weight: bold;");
            case "Volador" -> label.setStyle("-fx-text-fill: skyblue; -fx-font-weight: bold;");
            case "Psiquico" -> label.setStyle("-fx-text-fill: pink; -fx-font-weight: bold;");
            case "Bicho" -> label.setStyle("-fx-text-fill: darkgreen; -fx-font-weight: bold;");
            case "Roca" -> label.setStyle("-fx-text-fill: darkgoldenrod; -fx-font-weight: bold;");
            case "Fantasma" -> label.setStyle("-fx-text-fill: darkviolet; -fx-font-weight: bold;");
            case "Dragon" -> label.setStyle("-fx-text-fill: darkblue; -fx-font-weight: bold;");
            case "Siniestro" -> label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            case "Acero" -> label.setStyle("-fx-text-fill: silver; -fx-font-weight: bold;");
            case "Hada" -> label.setStyle("-fx-text-fill: lightpink; -fx-font-weight: bold;");
            default -> label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        }
        return label;
    }

    
    private void configurarBuscadorPokemon() {
        searchFieldPokemon.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPokemons(newValue);
        });
    }
    
    private void filtrarPokemons(String criterio) {
        ObservableList<Pokemon> listaCompleta = dameListaPokemons();

        FilteredList<Pokemon> listaFiltrada = new FilteredList<>(listaCompleta, pokemon -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return pokemon.getNombrePokemon().toLowerCase().contains(criterioEnMinusculas);
        });

        pokemonTable.setItems(listaFiltrada);
    }
    
    private void añadirPokemonVentana() {
        gestionarPokemon(null);
    }
    
    private void editarPokemonVentana() {
        Pokemon pokemonSeleccionado = pokemonTable.getSelectionModel().getSelectedItem();
        if (pokemonSeleccionado != null) {
            gestionarPokemon(pokemonSeleccionado);
        }
    }
    
    private void eliminarPokemonVentana() {
        Pokemon pokemonSeleccionado = pokemonTable.getSelectionModel().getSelectedItem();
        if (pokemonSeleccionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Pokemon");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar al Pokemon seleccionado?");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("boton-eliminar.png")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eliminarPokemon(pokemonSeleccionado);
        }
    }
    
    private String convertirImagenA64(File archivoImagen) {
        try (
            FileInputStream fileInputStream = new FileInputStream(archivoImagen);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes); 
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    private Image base64ToImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            return image;
            
	} catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
	}
    }
    
    private void configurarBotonesPokemon() {
        btnAdd.setOnAction(event -> añadirPokemonVentana()); 
        btnEdit.setOnAction(event -> editarPokemonVentana());
        btnDelete.setOnAction(event -> eliminarPokemonVentana());
    }
    
    private void configurarSeleccionTablaPokemon() {
        pokemonTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean itemSelected = newValue != null;
            btnEdit.setDisable(!itemSelected);
            btnDelete.setDisable(!itemSelected);
        });
    }
    
    private void añadirPokemon(String imagenBase64) {
        String query = "INSERT INTO POKEMON (nombre_pokemon, nivel_pokemon, tipo, num_pokedex, imagen) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);

            preparedStatement.setString(1, txtNombrePokemon.getText());  
            preparedStatement.setInt(2, spinerNivelPokemon.getValue()); 
            preparedStatement.setString(3, comboTipoPokemon.getValue());  
            preparedStatement.setInt(4, spinerPokedex.getValue()); 
            preparedStatement.setString(5, imagenBase64); 

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        pokemonTable.setItems(dameListaPokemons());  
    }
    
    private void editarPokemon(Pokemon pokemonSeleccionado,  String imagenBase64) {
        String query = "UPDATE POKEMON SET nombre_pokemon=?, nivel_pokemon=?, tipo=?, num_pokedex=?, imagen=? WHERE id_pokemon=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);

            preparedStatement.setString(1, txtNombrePokemon.getText());  
            preparedStatement.setInt(2, spinerNivelPokemon.getValue()); 
            preparedStatement.setString(3, comboTipoPokemon.getValue());  
            preparedStatement.setInt(4, spinerPokedex.getValue()); 
            preparedStatement.setString(5, imagenBase64); 
            preparedStatement.setInt(6, pokemonSeleccionado.getIdPokemon());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        pokemonTable.setItems(dameListaPokemons()); 
    }

    private void eliminarPokemon(Pokemon pokemonSeleccionado) {
        String query = "DELETE FROM POKEMON WHERE id_pokemon=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            
            preparedStatement.setInt(1,pokemonSeleccionado.getIdPokemon());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: "+e.getMessage());
        }
        pokemonTable.setItems(dameListaPokemons());
    }
    
    
    
    private void gestionarCaptura(Captura capturaSeleccionada) {
        Stage dialog = new Stage();
        dialog.getIcons().add((capturaSeleccionada == null) ? new Image(getClass().getClassLoader().getResourceAsStream("boton-añadir.png")) : new Image(getClass().getClassLoader().getResourceAsStream("boton-editar.png"))  );
        String title = (capturaSeleccionada == null) ? "Añadir Captura" : "Editar Captura";
        dialog.setTitle(title);

        VBox mainLayout = new VBox(15); 
        mainLayout.setPadding(new Insets(15));

        // Bucadores
        
        HBox buscadoresBox = new HBox(10); 
        //Buscador Entrenadores
        FilteredList<String> filteredItems = new FilteredList<>(dameListaNombresEntrenadores(), p -> true);
        TextField filterField = new TextField();
        filterField.setPromptText("Introduce un nombre para filtrar..");

        // ListView para mostrar los elementos 
        listView = new ListView<>(filteredItems);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                //Si el TextField está vacío, muestra todos los elementos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //Devuelve los items filtrados
                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });
        
        entrenadorComboBox = new TextField();
        entrenadorComboBox.setDisable(true);
        
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    entrenadorComboBox.setText(selectedItem);
                }
            }
        });
        VBox vbox = new VBox(10, filterField, listView, entrenadorComboBox);
        
        //Buscador Pokemon
        FilteredList<String> filteredPokemon = new FilteredList<>(dameListaNombresPokemons(), p -> true);
        TextField filterField2 = new TextField();
        filterField2.setPromptText("Introduce un nombre para filtrar..");

        // ListView para mostrar los elementos 
        listView2 = new ListView<>(filteredPokemon);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                //Si el TextField está vacío, muestra todos los elementos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //Devuelve los items filtrados
                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });
        
        pokemonComboBox = new TextField();
        pokemonComboBox.setDisable(true);
        
        listView2.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String selectedItem2 = listView2.getSelectionModel().getSelectedItem();
                if (selectedItem2 != null) {
                    pokemonComboBox.setText(selectedItem2);
                }
            }
        });
        VBox vbox2 = new VBox(10, filterField2, listView2, pokemonComboBox);

        
        buscadoresBox.getChildren().addAll(vbox,vbox2);
        
        
        
        // Fecha
        
        HBox fecahaLocalizacionBox = new HBox(10);
        HBox fechaBox = new HBox(10);
        Label lblFecha = new Label("Fecha:");
        lblFecha.setMinWidth(100);
        fechaPicker = new DatePicker();
        fechaPicker.setValue(capturaSeleccionada != null ? capturaSeleccionada.getFecha().toLocalDate() : LocalDate.now());
        fechaPicker.setPrefWidth(150);
        fechaBox.getChildren().addAll(lblFecha, fechaPicker);
        fechaPicker.setEditable(false);

        // Localización
        HBox localizacionBox = new HBox(10);
        Label lblLocalizacion = new Label("Localización:");
        lblLocalizacion.setMinWidth(100);
        localizacionField = new TextField(capturaSeleccionada != null ? capturaSeleccionada.getLocalizacion() : "");
        localizacionField.setPrefWidth(150);
        localizacionBox.getChildren().addAll(lblLocalizacion, localizacionField);
        
        fecahaLocalizacionBox.getChildren().addAll(fechaBox,localizacionBox);
        // Botones
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); 
        Button btnConfirmar = new Button("Confirmar");
        configurarEfectoHover(btnConfirmar);
        btnConfirmar.setPrefWidth(80); 
        Button btnCancelar = new Button("Cancelar");
        configurarEfectoHover(btnCancelar);
        btnCancelar.setPrefWidth(80);
        buttonBox.getChildren().addAll(btnConfirmar, btnCancelar);

        mainLayout.getChildren().addAll(buscadoresBox, fecahaLocalizacionBox, buttonBox);

        // Eventos
        btnConfirmar.setOnAction(e -> {
            if (entrenadorComboBox.getText()== null || pokemonComboBox.getText()== null || 
                fechaPicker.getValue() == null || localizacionField.getText().trim().isEmpty() ||
                pokemonComboBox.getText().trim().equals("") || entrenadorComboBox.getText().trim().equals("")) {
                mostrarAlerta("Error", "Todos los campos deben estar rellenos.");
            } else {
                if (capturaSeleccionada == null) {
                    añadirCaptura();
                } else {
                    editarCaptura(capturaSeleccionada);
                }
                dialog.close();
            }
        });

        btnCancelar.setOnAction(e -> dialog.close());

        Scene scene = new Scene(mainLayout, 490, 390);
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }
    
    public void mostrarDetallesCaptura(Captura capturaSeleccionada, Pokemon pokemonSeleccionado, Entrenador entrenadorSeleccionado) {
        Stage stage = new Stage();
        stage.setTitle("Detalles de la Captura");
        stage.initModality(Modality.APPLICATION_MODAL);

        // Crear el fondo como ImageView
        ImageView fondo = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("cesped.jpg")));
        fondo.setFitWidth(500); // Ajusta al tamaño del Stage
        fondo.setFitHeight(470);
        fondo.setPreserveRatio(false); // Cubrir todo el área
        fondo.setOpacity(0.9); // Ajustar opacidad si es necesario

        // Contenedor principal
        VBox contenido = new VBox();
        contenido.setPrefSize(500, 470);
        contenido.setSpacing(20);
        contenido.setPadding(new Insets(10, 10, 10, 10)); // Margen de los elementos

        // Primera sección (Pokémon)
        HBox hBoxPokemon = new HBox();
        hBoxPokemon.setPrefSize(570, 196);
        hBoxPokemon.setAlignment(Pos.TOP_CENTER);

        VBox vBoxPokemonDetails = new VBox(10);
        vBoxPokemonDetails.setPrefSize(250, 80); // Ajustar altura y ancho
        vBoxPokemonDetails.setMaxHeight(80);
        vBoxPokemonDetails.setStyle("-fx-background-color: #FDFED6; -fx-border-color: #324921; -fx-border-width: 4; -fx-background-radius: 15 2 17 2; -fx-border-radius: 15 2 15 2;");
        vBoxPokemonDetails.setPadding(new Insets(10));
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(10.0);
        shadow.setColor(Color.rgb(110, 106, 106));
        vBoxPokemonDetails.setEffect(shadow);

        Label lblPokemonName = new Label("Nº"+pokemonCaptura.getIdPokemon()+ " " + pokemonCaptura.getNombrePokemon().toUpperCase()+ ", " + pokemonCaptura.getTipo().toUpperCase());
        Label lblPokemonLevel = new Label("Nivel: " + pokemonCaptura.getNivelPokemon() + ",  Nº Pokédex: " + pokemonCaptura.getNumPokedex());
        vBoxPokemonDetails.getChildren().addAll(lblPokemonName, lblPokemonLevel);
        lblPokemonLevel.setStyle("-fx-font-weight: bold;");
        lblPokemonName.setStyle("-fx-font-weight: bold;");


        ImageView imgViewPokemon = new ImageView(base64ToImage(pokemonSeleccionado.getImagen()));
        imgViewPokemon.setFitWidth(120); // Ajustar al tamaño de su contenedor
        imgViewPokemon.setFitHeight(120);
        imgViewPokemon.setPreserveRatio(true);

        HBox hBoxPokemonImage = new HBox(imgViewPokemon);
        hBoxPokemonImage.setPrefSize(150, 150); // Tamaño fijo
        hBoxPokemonImage.setMaxWidth(150);
        hBoxPokemonImage.setMaxHeight(150);
        hBoxPokemonImage.setAlignment(Pos.CENTER);
        hBoxPokemonImage.setStyle("-fx-background-color: #BBFD8D; " + // Color de fondo
                          "-fx-border-color: #83E672; " +    // Color del borde
                          "-fx-border-width: 4px; " +       // Ancho del borde
                          "-fx-background-radius: 60; " +   // Radio del fondo para hacerlo circular
                          "-fx-border-radius: 60;");
        //hBoxPokemonImage.setStyle("-fx-background-color: #C8FB90;");
        hBoxPokemonImage.setPadding(new Insets(10, 10, 10, 10));

        hBoxPokemon.getChildren().addAll(vBoxPokemonDetails, hBoxPokemonImage);
        contenido.getChildren().add(hBoxPokemon);

        // Segunda sección (Captura)
        HBox hBoxCaptura = new HBox(10);
        hBoxCaptura.setStyle("-fx-background-color: #BBFD8D; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-color: #83E672; -fx-border-width: 4px;" );
        hBoxCaptura.setPrefSize(600, 90);
        hBoxCaptura.setAlignment(Pos.CENTER);
        hBoxCaptura.setMaxHeight(50);
        Label lblFechaCaptura = new Label("Fecha: " + capturaSeleccionada.getFecha());
        lblFechaCaptura.setStyle("-fx-fill-text: white; " +   // Radio del fondo para hacerlo circular
                          "-fx-font-weight: bold;");

        Label lblLocalizacion = new Label("Localización: " + capturaSeleccionada.getLocalizacion());
        lblLocalizacion.setStyle("-fx-fill-text: white; " +   // Radio del fondo para hacerlo circular
                          "-fx-font-weight: bold;");

        ImageView imgPokeball = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("pokeball-normal.png")));
        imgPokeball.setFitWidth(50); // Ajustar tamaño
        imgPokeball.setFitHeight(50);
        imgPokeball.setPreserveRatio(true);

        hBoxCaptura.getChildren().addAll(lblFechaCaptura, imgPokeball, lblLocalizacion);
        contenido.getChildren().add(hBoxCaptura);


        // Tercera sección (Entrenador)
        // Tercera sección (Entrenador)
        VBox vboxEntrenador = new VBox();

        HBox hBoxEntrenador = new HBox();
        hBoxEntrenador.setPrefSize(570, 220);
        hBoxEntrenador.setAlignment(Pos.TOP_CENTER);


        String sexo = entrenadorSeleccionado != null ? entrenadorSeleccionado.getSexo()+ "" : "Desconocido"; // Por defecto "Otro"
        Image image = switch (sexo.toLowerCase()) {
            case "hombre" -> new Image(getClass().getClassLoader().getResourceAsStream("entrenador-captura.png"));
            case "mujer" -> new Image(getClass().getClassLoader().getResourceAsStream("entrenadora-captura.png"));
            case "otro" -> new Image(getClass().getClassLoader().getResourceAsStream("otro.png"));
            default -> null;
        };
        ImageView imgViewEntrenador = new ImageView(image);
        imgViewEntrenador.setFitWidth(150); // Ajustar tamaño
        imgViewEntrenador.setFitHeight(150);
        imgViewEntrenador.setPreserveRatio(true);

        HBox hBoxEntrenadorImage = new HBox(imgViewEntrenador);
        hBoxEntrenadorImage.setMaxWidth(150);
        hBoxEntrenadorImage.setMaxHeight(150);
        hBoxEntrenadorImage.setPrefSize(150, 150); // Tamaño fijo
        hBoxEntrenadorImage.setAlignment(Pos.CENTER);
        hBoxEntrenadorImage.setStyle("-fx-background-color: #BBFD8D; " + // Color de fondo
                          "-fx-border-color: #83E672; " +    // Color del borde
                          "-fx-border-width: 4px; " +       // Ancho del borde
                          "-fx-background-radius: 60; " +   // Radio del fondo para hacerlo circular
                          "-fx-border-radius: 60;");
        //hBoxEntrenadorImage.setStyle("-fx-background-color: #C8FB90;");
        hBoxEntrenadorImage.setPadding(new Insets(10, 10, 10, 10)); // Espaciado interno del HBox de la imagen Entrenador

        hBoxEntrenador.setSpacing(50);
        hBoxPokemon.setSpacing(50); // Espaciado interno del HBox de la imagen Pokémon


        VBox vBoxEntrenadorDetails = new VBox(10);
        vBoxEntrenadorDetails.setPrefSize(250, 130); // Ajustar tamaño
        vBoxEntrenadorDetails.setMaxHeight(80);
        vBoxEntrenadorDetails.setStyle("-fx-background-color: #FDFED6; -fx-border-color: #324921; -fx-border-width: 4; -fx-background-radius: 15 2 17 2; -fx-border-radius: 15 2 15 2;");
        vBoxEntrenadorDetails.setPadding(new Insets(10));
        vBoxEntrenadorDetails.setEffect(shadow);


        Label lblEntrenadorName = new Label(entrenadorCaptura.getNombre() + ",  " + entrenadorCaptura.getEdad());
        Label lblEntrenadorRegion = new Label("Región: " + entrenadorCaptura.getRegion() + ", Medallas: " + entrenadorCaptura.getNumMedallas());
        vBoxEntrenadorDetails.getChildren().addAll(lblEntrenadorName, lblEntrenadorRegion);
        lblEntrenadorName.setStyle("-fx-font-weight: bold;");
        lblEntrenadorRegion.setStyle("-fx-font-weight: bold;");

        Button btnCerrar = new Button("Confirmar");
        btnCerrar.setOnAction(e -> stage.close());
        btnCerrar.setPrefWidth(150);
        btnCerrar.setStyle("-fx-background-radius : 15;");
        configurarEfectoHover(btnCerrar);
        HBox cerrarBox = new HBox(btnCerrar);
        cerrarBox.setPadding(new Insets(0,15,15,0));
        cerrarBox.setAlignment(Pos.CENTER_RIGHT);



        hBoxEntrenador.getChildren().addAll(hBoxEntrenadorImage, vBoxEntrenadorDetails);
        vboxEntrenador.getChildren().addAll(hBoxEntrenador,cerrarBox);
        contenido.getChildren().add(vboxEntrenador);

        // Usar StackPane para superponer la imagen de fondo y los elementos
        StackPane root = new StackPane();
        root.getChildren().addAll(fondo, contenido);

        // Crear la escena y mostrar el Stage
        Scene scene = new Scene(root, 500, 470);
        stage.setScene(scene);
        stage.showAndWait();
    }


    public ObservableList<String> dameListaNombresEntrenadores() {
        ObservableList<String> listaNombres = FXCollections.observableArrayList();
        for (Entrenador entrenador : dameListaEntrenadores()) {
            listaNombres.add(entrenador.getNombre()+" ("+entrenador.getId()+")");
        }
        return listaNombres;
    }
    
    
    public ObservableList<String> dameListaNombresPokemons() {
        ObservableList<String> listaNombres = FXCollections.observableArrayList();
        for (Pokemon pokemon : dameListaPokemons()) {
            listaNombres.add(pokemon.getNombrePokemon()+" ("+pokemon.getIdPokemon()+")");
        }
        return listaNombres;
    }
    
       
    public Entrenador obtenerEntrenadorPorNombre(String nombre) {
        for (Entrenador entrenador : dameListaEntrenadores()) {
            if (entrenador.getNombre().equals(nombre)) {
                return entrenador;
            }
        }
        return null; 
    }

    public Pokemon obtenerPokemonPorNombre(String nombre) {
        for (Pokemon pokemon : dameListaPokemons()) {
            if (pokemon.getNombrePokemon().equals(nombre)) {
                return pokemon;
            }
        }
        return null; 
    }

    
    public ObservableList<Captura> dameListaCapturas() {
        if (conexion != null) {
            ObservableCapturas.clear(); //Limpiamos el contenido actual
            String query = "SELECT ENTRENADOR.nombre AS nombreEntrenador , POKEMON.nombre_pokemon AS nombrePokemon, CAPTURA.fecha, CAPTURA.localizacion, CAPTURA.id_pokemon, CAPTURA.id_entrenador FROM CAPTURA JOIN ENTRENADOR ON CAPTURA.id_entrenador = ENTRENADOR.id_entrenador JOIN POKEMON ON CAPTURA.id_pokemon = POKEMON.id_pokemon;";
            try {
                rs = st.executeQuery(query);
                Captura captura;
                while (rs.next()) { //Se usan los identificadores propios en la BBDD (no es case sensitive). Revisar en phpmyadmin
                        captura = new Captura(rs.getString("nombreEntrenador"), rs.getString("nombrePokemon"), rs.getDate("fecha"), rs.getString("localizacion"),rs.getInt("id_pokemon"),rs.getInt("id_entrenador"));
                    ObservableCapturas.add(captura);         
                }
            } catch (SQLException e) {
                System.out.println("Excepción SQL: "+e.getMessage());
            }
            return ObservableCapturas;
        }
        return null;
    }
    
    private Entrenador buscarEntrenadorPorId(int idBuscado) {
        for (Entrenador entrenador : ObservableEntrenadores) {
            if (entrenador.getId() == idBuscado) {
                return entrenador; // Devuelve el entrenador encontrado
            }
        }
        return null; // Si no encuentra el entrenador, devuelve null
    }

    private Pokemon buscarPokemonPorId(int idBuscado) {
        for (Pokemon pokemon : ObservablePokemons) {
            System.out.println(idBuscado);
            if (pokemon.getIdPokemon() == idBuscado) {
                return pokemon; // Devuelve el Pokémon encontrado
            }
        }
        return null; // Si no encuentra el Pokémon, devuelve null
    }

    
    private void inicializarTablaCapturas(){
        colEntrenadorCaptura.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNombreEntrenador())));
        colPokemonCaptura.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNombrePokemon())));
        colLocalizacionCaptura.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalizacion()));
        colFechaCaptura.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getFecha())));
        colIdPokemon.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdPokemon())));
        colIdEntrenador.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdEntrenador())));
        
        capturaTable.setItems(dameListaCapturas());
    }
    
    private void configurarBuscadorCaptura() {
        searchFieldCaptura.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarCapturas(newValue);
        });
    }
    
    private void filtrarCapturas(String criterio) {
        ObservableList<Captura> listaCompleta = dameListaCapturas();

        FilteredList<Captura> listaFiltrada = new FilteredList<>(listaCompleta, captura -> {
            if (criterio == null || criterio.isEmpty()) {
                return true; 
            }
            String criterioEnMinusculas = criterio.toLowerCase();

            return captura.getLocalizacion().toLowerCase().contains(criterioEnMinusculas);
        });

        capturaTable.setItems(listaFiltrada);
    }
    
    private void añadirCapturaVentana() {
        gestionarCaptura(null);
    }
    
    private void editarCapturaVentana() {
        Captura capturaSeleccionada = capturaTable.getSelectionModel().getSelectedItem();
        if (capturaSeleccionada != null) {
            gestionarCaptura(capturaSeleccionada);
        }
    }
    
    private void eliminarCapturaVentana() {
        Captura capturaSeleccionada = capturaTable.getSelectionModel().getSelectedItem();
        if (capturaSeleccionada == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Captura");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar la captura seleccionada?");
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("boton-eliminar.png")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            eliminarCaptura(capturaSeleccionada);
        }
    }
    
    private void configurarBotonesCaptura() {
        btnAdd.setOnAction(event -> añadirCapturaVentana()); 
        btnEdit.setOnAction(event -> editarCapturaVentana());
        btnDelete.setOnAction(event -> eliminarCapturaVentana());
    }
    
    private void configurarSeleccionTablaCaptura() {
        capturaTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean itemSelected = newValue != null;
            btnEdit.setDisable(!itemSelected);
            btnDelete.setDisable(!itemSelected);
        }); 
    }
    
    private void añadirCaptura() {
        String nombreEntrenador = entrenadorComboBox.getText();
        String nombrePokemon = pokemonComboBox.getText();
        LocalDate fechaCaptura = fechaPicker.getValue();
        String localizacion = localizacionField.getText().trim();
        
        Entrenador entrenador = obtenerEntrenadorPorNombre(nombreEntrenador.replaceAll("\\s*\\(\\d+\\)\\s*", "").trim());
        Pokemon pokemon = obtenerPokemonPorNombre(nombrePokemon.replaceAll("\\s*\\(\\d+\\)\\s*", "").trim());
        
        String query = "INSERT INTO CAPTURA (id_entrenador, id_pokemon, fecha, localizacion) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            preparedStatement.setInt(1, entrenador.getId());  
            preparedStatement.setInt(2, pokemon.getIdPokemon()); 
            preparedStatement.setDate(3, java.sql.Date.valueOf(fechaCaptura));  
            preparedStatement.setString(4, localizacion); 

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        capturaTable.setItems(dameListaCapturas());  
    }
    
    private void editarCaptura(Captura capturaSeleccionada) {
        String nombreEntrenadorAnterior = capturaSeleccionada.getNombreEntrenador();
        String nombrePokemonAnterior = capturaSeleccionada.getNombrePokemon();
        String nombrePokemonNuevo = pokemonComboBox.getText().replaceAll("\\s*\\(\\d+\\)\\s*", "").trim(); 
        String nombreEntrenadorNuevo = entrenadorComboBox.getText().replaceAll("\\s*\\(\\d+\\)\\s*", "").trim();
        String nuevaLocalizacion = localizacionField.getText();  // La nueva localización
        LocalDate nuevaFecha = fechaPicker.getValue();  // La nueva fecha

        Entrenador entrenadorAnterior = obtenerEntrenadorPorNombre(nombreEntrenadorAnterior);
        Entrenador entrenadorNuevo = obtenerEntrenadorPorNombre(nombreEntrenadorNuevo);
        Pokemon pokemonAnterior = obtenerPokemonPorNombre(nombrePokemonAnterior);
        Pokemon pokemonNuevo = obtenerPokemonPorNombre(nombrePokemonNuevo);

        String query = "UPDATE CAPTURA SET id_entrenador=?, id_pokemon=?, fecha=?, localizacion=? WHERE id_entrenador=? AND id_pokemon=? AND fecha=? AND localizacion=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            preparedStatement.setInt(1, entrenadorNuevo.getId());  
            preparedStatement.setInt(2, pokemonNuevo.getIdPokemon()); 
            preparedStatement.setDate(3, java.sql.Date.valueOf(nuevaFecha));  
            preparedStatement.setString(4, nuevaLocalizacion);  
            preparedStatement.setInt(5, entrenadorAnterior.getId()); 
            preparedStatement.setInt(6, pokemonAnterior.getIdPokemon());  
            preparedStatement.setDate(7, capturaSeleccionada.getFecha());
            preparedStatement.setString(8, capturaSeleccionada.getLocalizacion());  

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        capturaTable.setItems(dameListaCapturas()); 
    }

    private void eliminarCaptura(Captura capturaSeleccionada) {
        String nombreEntrenador = capturaSeleccionada.getNombreEntrenador();
        String nombrePokemon = capturaSeleccionada.getNombrePokemon();

        Entrenador entrenador = obtenerEntrenadorPorNombre(nombreEntrenador);
        Pokemon pokemon = obtenerPokemonPorNombre(nombrePokemon);

        String query = "DELETE FROM CAPTURA WHERE id_entrenador=? AND id_pokemon=? AND fecha=?";
        try {
            PreparedStatement preparedStatement = this.conexion.prepareStatement(query);
            preparedStatement.setInt(1, entrenador.getId());  
            preparedStatement.setInt(2, pokemon.getIdPokemon());  
            preparedStatement.setDate(3, capturaSeleccionada.getFecha()); 

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Excepción: " + e.getMessage());
        }
        capturaTable.setItems(dameListaCapturas()); 
    }
    
    private void configurarEfectoHover(Button boton) {
        double scaleFactor = 1.1;

        ScaleTransition agrandar = new ScaleTransition(Duration.seconds(0.5), boton);
        agrandar.setToX(scaleFactor);
        agrandar.setToY(scaleFactor);

        ScaleTransition restaurar = new ScaleTransition(Duration.seconds(1), boton);
        restaurar.setToX(1.0);
        restaurar.setToY(1.0);

        boton.setOnMouseEntered(event -> {
            restaurar.stop();
            agrandar.play();
        });

        boton.setOnMouseExited(event -> {
            agrandar.stop();
            restaurar.play();
        });
    }
}

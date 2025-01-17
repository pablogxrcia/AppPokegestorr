package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class ControladorInforme implements Initializable{

    Map parametros = new HashMap();
    Connection conexion;
    Statement st;
    ResultSet rs;
    private ToggleGroup opciones;
    
    @FXML
    private HBox filtroBox;

    @FXML
    private ImageView imgBtnCaptura;

    @FXML
    private ImageView imgBtnEntrenador;
        
    @FXML
    private Button btnCerrar;

    @FXML
    private ImageView imgBtnPokemon;
        
    @FXML
    private Button btnInformeGrafico;
    
    @FXML
    private WebView wv;
    
    @FXML
    private RadioButton botonEntrenador;

    @FXML
    private RadioButton btnCaptura;
    
    @FXML
    private TextField txtTitulo;
      
    @FXML
    private CheckBox chkIncrustado;


    @FXML
    private RadioButton btnPokemon;
    
    
    @FXML
    void btnInformeNormal(ActionEvent event) {    
        ToggleButton selectedButton = (ToggleButton) opciones.getSelectedToggle();
        
        if (selectedButton == null || selectedButton.getId() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("El informe no se puede generar");
            alert.setContentText("Tienes que seleccionar de qué tabla quieres hacer el informe");
            alert.showAndWait();
            return; // Termina la ejecución del método.
        }
        
        switch (selectedButton.getId()) {
            case "botonEntrenador" -> {
                if (chkIncrustado.isSelected()) {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaEntrenador.jasper", parametros, 0);
                } else {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaEntrenador.jasper", parametros, 1);
                }
            }
            case "btnPokemon" -> {
                if (chkIncrustado.isSelected()) {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaPokemon.jasper", parametros, 0);
                } else {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaPokemon.jasper", parametros, 1);
                }
            }
            case "btnCaptura" -> {
                if (chkIncrustado.isSelected()) {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaCaptura.jasper", parametros, 0);
                } else {
                    parametros.put("FiltroNombre", "%" + txtTitulo.getText() + "%");
                    lanzaInforme("/reports/informePruebaCaptura.jasper", parametros, 1);
                }
            }
        }

    }
    
    @FXML
    void btnInformeGraf(ActionEvent event) {
        ToggleButton selectedButton = (ToggleButton) opciones.getSelectedToggle();
        
        if (selectedButton == null || selectedButton.getId() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("El informe no se puede generar");
            alert.setContentText("Tienes que seleccionar de qué tabla quieres hacer el informe");
            alert.showAndWait();
            return; // Termina la ejecución del método.
        }
        
        switch (selectedButton.getId()) {
            case "botonEntrenador" -> {
                if (chkIncrustado.isSelected()) {
                    lanzaInforme("/reports/informeGraficoPokemon.jasper", parametros, 0);
                } else {
                    lanzaInforme("/reports/informeGraficoPokemon.jasper", parametros, 1);
                }
            }
            case "btnPokemon" -> {
                if (chkIncrustado.isSelected()) {
                    lanzaInforme("/reports/informeGraficoPokemon.jasper", parametros, 0);
                } else {
                    lanzaInforme("/reports/informeGraficoPokemon.jasper", parametros, 1);
                }
            }
        }

    }
    
    @FXML
    void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            opcionesBotones();
            
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
    private void establecerIconos(){
        imgBtnCaptura.setImage(new Image(getClass().getClassLoader().getResourceAsStream("pokeballInforme.png")));
        Tooltip tooltipCaptura = new Tooltip("Captura");
        //Tooltip.install(imgBtnCaptura, tooltipCaptura);
        imgBtnCaptura.setOnMouseEntered(event -> {
            tooltipCaptura.show(imgBtnCaptura, event.getScreenX(), event.getScreenY() + 10);
        });

        imgBtnCaptura.setOnMouseExited(event -> tooltipCaptura.hide());
        
        imgBtnEntrenador.setImage(new Image(getClass().getClassLoader().getResourceAsStream("entrenador.png")));
        Tooltip tooltipEntrenador = new Tooltip("Entrenador");
        //Tooltip.install(imgBtnCaptura, tooltipEntrenador);
        imgBtnEntrenador.setOnMouseEntered(event -> {
            tooltipEntrenador.show(imgBtnEntrenador, event.getScreenX(), event.getScreenY() + 10);
        });

        imgBtnEntrenador.setOnMouseExited(event -> tooltipEntrenador.hide());
        
        imgBtnPokemon.setImage(new Image(getClass().getClassLoader().getResourceAsStream("pokemon.png")));
        Tooltip tooltipPokemon = new Tooltip("Pokemon");
        //Tooltip.install(imgBtnCaptura, tooltipPokemon);
        imgBtnPokemon.setOnMouseEntered(event -> {
            tooltipPokemon.show(imgBtnPokemon, event.getScreenX(), event.getScreenY() + 10);
        });

        imgBtnPokemon.setOnMouseExited(event -> tooltipPokemon.hide());
    }
    private void opcionesBotones() {
        opciones = new ToggleGroup();

        btnCaptura.setToggleGroup(opciones);
        btnPokemon.setToggleGroup(opciones);
        botonEntrenador.setToggleGroup(opciones);

        opciones.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;

                if ("botonEntrenador".equals(selectedButton.getId()) || "btnPokemon".equals(selectedButton.getId())) {
                    filtroBox.setOpacity(1.0); // Visible completamente
                    filtroBox.setDisable(false); // Asegurarse de que no esté deshabilitado
                    btnInformeGrafico.setDisable(false); // Habilitar el botón
                } else if ("btnCaptura".equals(selectedButton.getId())) {
                    filtroBox.setOpacity(0); // Semitransparente
                    filtroBox.setDisable(true); // Deshabilitar
                    btnInformeGrafico.setDisable(true); // Deshabilitar el botón
                }
            }
        });
    }

    private void lanzaInforme(String rutaInf, Map<String, Object> param, int tipo) {
        try {
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream(rutaInf));
            try {
                // Llena el informe con los datos de la conexión
                JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, this.conexion);

                if (!jasperPrint.getPages().isEmpty()) {

                    //Exporta el informe a un archivo PDF (necesita librería)
                    String pdfOutputPath = "informe.pdf";
                    JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);

                    //Exporta el informe a un archivo HTML
                    String outputHtmlFile = "informeHTML.html";
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputHtmlFile);
                    
                    //Crea un WebView para mostrar la versión HTML del informe
                    if (tipo == 0) {
                        wv.getEngine().load(new File(outputHtmlFile).toURI().toString());
                    } else { //tipo==1
                        WebView wvnuevo = new WebView();
                        wvnuevo.getEngine().load(new File(outputHtmlFile).toURI().toString());
                        StackPane stackPane = new StackPane(wvnuevo);
                        Scene scene = new Scene(stackPane, 600, 500);
                        Stage stage = new Stage();
                        stage.setTitle("Informe en HTML");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(true);
                        stage.setScene(scene);
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText("Alerta de Informe");
                    alert.setContentText("La búsqueda " + txtTitulo.getText() + " no generó páginas");
                    alert.showAndWait();
                }

            } catch (JRException e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error al generar el informe: " + e.getMessage());
            }
        } catch (JRException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

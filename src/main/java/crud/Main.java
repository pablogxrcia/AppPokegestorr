package crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../vista/MainEntrenador.fxml"));
        primaryStage.setTitle("Poke Gestor");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icono.png")));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/styles.css").toString());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

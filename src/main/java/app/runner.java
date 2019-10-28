package app;

import Controladores.ControladorSplash.splashController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class runner extends Application {

    private Stage st;

    public void start(Stage primaryStage) throws Exception {
           this.st = primaryStage;
           startSplash();

    }
    public Stage getSt() {
        return st;
    }

    private void startSplash() throws IOException {
        splashController control = new splashController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/splash/splash.fxml"));
        loader.setControllerFactory(t->control);
        this.st.setScene(new Scene(loader.load()));
        this.st.initStyle(StageStyle.UNDECORATED);
        this.st.setTitle("pruebaSplash");
        this.st.getIcons().add(new Image("/utils/logo/iconoProyecto.png"));
        this.st.show();
        control.start();
    }

    public static void main(String[] args){
        launch(args);
    }


}

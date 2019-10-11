
/*
 * Derechos de autor 2019 Rodolfo Elias Ojeda Almada
 *
 * Licenciado bajo la Licencia Apache, Versión 2.0 (la "Licencia");
 * no puede utilizar este archivo, excepto en cumplimiento con la Licencia.
 * Puede obtener una copia de la licencia en
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * A menos que sea requerido por la ley aplicable o acordado por escrito, software
 * distribuido bajo la Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS O CONDICIONES DE NINGÚN TIPO, ya sea expresa o implícita.
 * Consulte la Licencia para el idioma específico que rige los permisos y
 * Limitaciones bajo la Licencia.
 */

package app;

import Controladores.ControladorSplash.splashController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
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

    private boolean isScreenSizeCompatible(){
        int MINIMUN_WIDHT_SIZE_REQUIRED = 1280;
        int MINIMUN_HEIGHT_SIZE_REQUIRED = 720;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        return primaryScreenBounds.getHeight() >= MINIMUN_HEIGHT_SIZE_REQUIRED && primaryScreenBounds.getWidth() >= MINIMUN_WIDHT_SIZE_REQUIRED;
    }

    private void stoppall(){
        Platform.exit();
        System.exit(1);
    }

}

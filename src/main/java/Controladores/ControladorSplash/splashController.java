

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

package Controladores.ControladorSplash;

import Controladores.ControladorMenu.menu.MenuController;
import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Directorio.Directory;
import Modelos.Directorio.DirectoryConstants;
import Modelos.Directorio.NormalDirectory;
import Modelos.Hilos.HiloExecutor;
import Servicios.alerta.Alerta;
import app.runner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//splashcontroller
public class splashController implements Runnable{
    private runner runer;
    public splashController(runner runer){
        this.runer = runer;
    }

    public void start(){
        Thread th = new Thread(this,"Splash Hilo");
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void run() {
        Task<Boolean> booleanTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Directory DataDbDirectory = new NormalDirectory(DirectoryConstants.DataFilesRoot, DirectoryConstants.DATABASENAME);
                if(!DataDbDirectory.exists()){
                    DataDbDirectory.create();
                    new sqliteConection(false).conect();
                }
                return true;
            }
        };
        booleanTask.setOnSucceeded(event -> {
            if(booleanTask.getValue()){
                loadMenu();
            }
        });
        HiloExecutor.execute(booleanTask);
    }

    private void loadMenu(){
        Platform.runLater(() -> {
            try {
                Stage st = new Stage();
                MenuController menu = new MenuController(st);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu/menu.fxml"));
                loader.setController(menu);
                st.setScene(new Scene(loader.load()));
                runer.getSt().hide();
                menu.start();
            } catch (IOException e) {
                Alerta.ShowError(e);
                e.printStackTrace();
            }
        });
    }
}

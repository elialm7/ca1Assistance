package Controladores.ControladorMenu.Bienvenida;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.tabcontrol.TabManipulator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class BienvenidaController implements NodeUpdator, Initializable {


    @FXML
    private Button next;

    private TabManipulator tabManipulator;

    @Override
    public void updateNode() {
    }
    private void loadTab(){
        try {
            tabManipulator = TabManipulator.getCurrentInstance();
        } catch (IllegalAccessException e) {e.printStackTrace();

        }

    }
    private void next(){
        tabManipulator.remove(Fxmls.BIENVENIDA);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTab();
        next.setText("Cerrar Pestaña.");
        next.setOnAction(event -> next());

    }
}

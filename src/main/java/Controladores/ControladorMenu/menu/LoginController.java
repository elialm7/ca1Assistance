package Controladores.ControladorMenu.menu;

import Controladores.ControladorMenu.Bienvenida.BienvenidaController;
import Controladores.Enums.Fxmls;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.Users.User;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable,NodeUpdator {

    @FXML
    private Button enter;

    @FXML
    private TextField usarname;

    @FXML
    private PasswordField password;

    @FXML
    private Button cancel;

    @FXML
    private Label message;
    private List<User> userList;
    private MenuController menuController;
    public LoginController (MenuController menuController){
        this.menuController = menuController;
    }
    @Override
    public void updateNode() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUsers();
        loadEvents();
        loadpropertys();
    }

    private void cancel(){
        try {
            TabManipulator.getCurrentInstance().getParentStage().close();
            Platform.exit();
            System.exit(0);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void enter(){
        try {
            User user = getDatafromform();
            boolean iscorrect = DaoService.getInstance().getUserInstance().exists(user);
            if (iscorrect) {
                menuController.disablebuttons(false);
                TabManipulator.getCurrentInstance().remove(Fxmls.SYSTEM_LOGIN);
                TabManipulator.getCurrentInstance().unblockAll();
                if(!TabManipulator.getCurrentInstance().IsThereTabOpened()) {
                    TabManipulator.getCurrentInstance().add(Fxmls.BIENVENIDA, BienvenidaController.class, "Bienvenido/a");
                }
            }else{
                fireMessage();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }
    private void loadEvents(){
        this.enter.setOnAction(t->enter());
        this.cancel.setOnAction(t->cancel());
    }
    private void loadpropertys(){
        this.message.setVisible(false);
    }

    private void fireMessage(){
        this.message.setVisible(true);
    }
    private void loadUsers(){
        userList = new ArrayList<>();
    }
    private User getDatafromform(){
            String usernametxt = this.usarname.getText();
            String passwordtxt = this.password.getText();
        return new User(usernametxt, passwordtxt);
    }


}

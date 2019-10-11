package Controladores.ControladorMenu.informaciones;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.Users.User;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class configurationController implements Initializable, NodeUpdator {

    @FXML
    private TextField usernametxt;

    @FXML
    private TextField passwordtxt;

    @FXML
    private Button acept;

    @FXML
    private Button eliminar;

    private User currentUser;

    private TabManipulator tabManipulator;

    public configurationController(){}

    @Override
    public void updateNode() {
        loadUser();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.acept.setOnAction(event -> accept());
        this.eliminar.setOnAction(event -> delete());
        loadManipulator();
        loadUser();

    }

    private void loadUser(){
        //si no existe sale.
        if(!exist()){
            this.eliminar.setDisable(true);
            return;
        }
        currentUser = DaoService.getInstance().getUserInstance().getall().AsObservableList().get(0);
        setData(currentUser);
    }

    private User getData(){
        String username = this.usernametxt.getText();
        String password = this.passwordtxt.getText();
        if(username.isEmpty() && password.isEmpty()){
            return null;
        }else if(username.isEmpty()){
            return null;
        }else if(password.isEmpty()){
            return null;
        }else{
            return new User(username, password);
        }
    }

    private void setData(User user){
        this.usernametxt.setText(user.getUsername());
        this.passwordtxt.setText(user.getPassword());
    }

    private void cleardata(){
        this.usernametxt.setText("");
        this.passwordtxt.setText("");
    }

    private void delete(){
        if(currentUser!=null){
            DaoService.getInstance().getUserInstance().delete(currentUser.getId());
            notifyEliminated();
            cleardata();
            this.currentUser = null;
        }
    }

    private void accept(){
        if(currentUser == null){
            currentUser = getData();
            addtodb(currentUser);
        }else{
            User temp = getData();
            currentUser.setUsername(temp.getUsername());
            currentUser.setPassword(temp.getPassword());
            updatetodb(currentUser);
        }
    }

    private void addtodb(User user){
        DaoService.getInstance().getUserInstance().add(user);
        notifyAdded();
    }
    private void updatetodb(User user){
        DaoService.getInstance().getUserInstance().change(user);
        notifyChanged();
    }
    private boolean exist(){
        return DaoService.getInstance().getUserInstance().getall().AsArrayList().size() > 0;
    }
    private void loadManipulator(){
        try{
            tabManipulator = TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void notifyChanged(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", "Se ha modificado correctamente","El usuario ha sido modificado corectamente",
               tabManipulator.getParentStage());
    }

    private void notifyAdded(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", "Se ha añadido correctamente", "El nuevo usuario ha sido añadido correctamente",
                tabManipulator.getParentStage());
    }

    private void notifyEliminated(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", "Se ha eliminado correctamente", "El usuario se ha eliminador correctamente. Tenga cuidado con quien comparte el ordenador",
                tabManipulator.getParentStage());
    }

}

package Controladores.ControladorMenu.Profesores;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.Sexo;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
public class ProfesorEdicionController implements Initializable, NodeUpdator {

    @FXML
    private TextField nombretxt;

    @FXML
    private TextField apellidotxt;

    @FXML
    private ComboBox<Sexo> sexocombo;

    @FXML
    private TextField nromadretxt;

    @FXML
    private Button aplicar;

    @FXML
    private Button cancelar;

    @FXML
    private ProgressIndicator indicador;

    @FXML
    private Label idData;

    private Profesor profesorMateria;

    private TabManipulator tabManipulator;

    public ProfesorEdicionController(Profesor profesorMateria) {
        this.profesorMateria = profesorMateria;
    }

    private void loadtabmanipulator(){
        try{
            tabManipulator = TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void load(){
        set_evens();
        loadSexoCombo();
        loadDatatotextfields();
        loadtabmanipulator();
    }
    private void set_evens(){
        this.indicador.setVisible(false);
        this.aplicar.setOnAction(event -> save());
        this.cancelar.setOnAction(event ->cancelar());
    }

    private void loadSexoCombo(){
        ObservableList<Sexo> sexos = FXCollections.observableArrayList();
        sexos.add(Sexo.Masculino);
        sexos.add(Sexo.Femenino);
        this.sexocombo.setItems(sexos);
        this.sexocombo.getSelectionModel().select(getsexo(profesorMateria.getSexo()));
    }

    private Sexo getsexo(String str){
        if(str.equals(Sexo.Masculino.name())){
            return Sexo.Masculino;
        }else if(str.equals(Sexo.Femenino.name())){
            return Sexo.Femenino;
        }
        return null;
    }
    private void loadDatatotextfields(){
        this.idData.setText("ID: "+this.profesorMateria.getCi());
        this.nombretxt.setText(this.profesorMateria.getNombres());
        this.apellidotxt.setText(this.profesorMateria.getApellidos());
        this.nromadretxt.setText(this.profesorMateria.getNroprofesor());
    }

    private void save(){
            Profesor profesorMateria = getDatafromForm();
            DaoService.getInstance().getProfesorInstance().change(profesorMateria);
            NotificationHelper.get().notifyOnchanged();
            remove();
    }


    private void cancelar(){
        remove();
    }

    private void remove(){
        tabManipulator.remove(Fxmls.PROFESOR_EDICION);
    }
    private Profesor getDatafromForm(){
        String dni = this.profesorMateria.getCi();
        String nombre = this.nombretxt.getText();
        String apellido = this.apellidotxt.getText();
        String nro = this.nromadretxt.getText();
        String sexo = this.sexocombo.getSelectionModel().getSelectedItem().name();
        return new Profesor(nombre, apellido, dni, sexo, nro);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    @Override
    public void updateNode() {
    }
}

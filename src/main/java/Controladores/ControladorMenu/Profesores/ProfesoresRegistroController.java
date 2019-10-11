package Controladores.ControladorMenu.Profesores;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.Sexo;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SQLITE.Interfaces.IProfesor;
import Modelos.SqliteDaoService.DaoService;
import Servicios.Utils.Utils;
import Servicios.alerta.Alerta;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfesoresRegistroController implements Initializable, NodeUpdator {

    @FXML
    private TextField nombretxt;

    @FXML
    private TextField apellidotxt;

    @FXML
    private ComboBox<Sexo> sexocombo;

    @FXML
    private TextField nromadretxt;

    @FXML
    private TableView<Profesor> tabla;

    @FXML
    private TableColumn<Profesor, String> columnadni;

    @FXML
    private TableColumn<Profesor, String> columnanombre;

    @FXML
    private TableColumn<Profesor, String> columnaApellido;

    @FXML
    private TableColumn<Profesor, String> columnasexo;

    @FXML
    private TableColumn<Profesor, String> nropart;


    @FXML
    private Button anadir;

    @FXML
    private Button cancelar;

    @FXML
    private Button guardar;

    @FXML
    private ProgressIndicator pregressindicator;


    private TabManipulator tabManipulator;

    public ProfesoresRegistroController(){}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    @Override
    public void updateNode() {

    }

    private void loadTabManipulator(){
        try {
            tabManipulator = TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void load(){
        try {
            initialValues();
            set_events();
            tablePropertys();
            tablerowProperty();
            loadSexoCombo();
            loadTabManipulator();
        }catch (NullPointerException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void initialValues(){
        this.guardar.setDisable(true);
    }
    private void loadSexoCombo(){
        ObservableList<Sexo> sexos = FXCollections.observableArrayList();
        sexos.add(Sexo.Masculino);
        sexos.add(Sexo.Femenino);
        this.sexocombo.setItems(sexos);
        this.sexocombo.getSelectionModel().select(0);
    }
    private void set_events(){
        this.anadir.setOnAction(t->add());
        this.guardar.setOnAction(t->saveData());
        this.cancelar.setOnAction(t->cancel());
        this.pregressindicator.setVisible(false);
    }
    private void tablePropertys(){
        columnadni.setCellValueFactory(new PropertyValueFactory<>("ci"));
        columnanombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnasexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        nropart.setCellValueFactory(new PropertyValueFactory<>("nroprofesor"));
    }

    private void tablerowProperty(){
        this.tabla.setRowFactory(param -> {
            final TableRow<Profesor> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Eliminar de la lista");
            menuItem.setOnAction(t-> deletefromlist(tabla.getSelectionModel().getSelectedItem()));
            contextMenu.getItems().add(menuItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(contextMenu).otherwise((ContextMenu)null));
            return row;
        });
        this.tabla.getItems().addListener((ListChangeListener<Profesor>) c -> {
            while(c.next()){
                if(tabla.getItems().size()==0){
                    guardar.setDisable(true);
                }else{
                    guardar.setDisable(false);
                }
            }
        });
    }

    private void deletefromlist(Profesor profesorMateria){
        Optional<ButtonType> resul = NotificationHelper.get().notifyDeleteOption();
        if(resul.get() == ButtonType.OK) this.tabla.getItems().remove(profesorMateria);
    }

    private void add(){
        Profesor profesorMateria = getprofesorfromForm();
        if(profesorMateria==null)return;
        this.tabla.getItems().add(profesorMateria);
    }

    private void resultOperation(int skipped) {
        if (skipped == 0) {
            NotificationHelper.get().notifySuccessOninsertOperation(skipped);
            tabManipulator.remove(Fxmls.PROFESOR_REGISTRO);
        } else {
            NotificationHelper.get().notifySkippedOnInsertOperation(skipped);
            tabManipulator.remove(Fxmls.PROFESOR_REGISTRO);
        }
    }

    private void cancel(){
            Optional<ButtonType> buttonType = NotificationHelper.get().notifyOnCancelOperation();
            if (buttonType.get() == ButtonType.OK) {
                tabManipulator.remove(Fxmls.PROFESOR_REGISTRO);
            }
    }

    private String getID(){
        if(tabla.getItems().size()>0){
            return Utils.getNewIntegerID(tabla.getItems());
        }else{
            return Utils.getNewIntegerID(DaoService.getInstance().getProfesorInstance().getall().AsArrayList());
        }
    }
    private Profesor getprofesorfromForm(){
        String nombre = this.nombretxt.getText();
        String apellido = this.apellidotxt.getText();
        String ci =  getID();
        String nroTelefono = this.nromadretxt.getText();
        String sexo = this.sexocombo.getSelectionModel().getSelectedItem().name();
        Profesor profesor = new Profesor(nombre, apellido, ci, sexo, nroTelefono);
        if(verifyData(profesor)){
            return profesor;
        }else{
            NotificationHelper.get().notifyIncorrectDniFormat();
        }
        return null;
    }
    private void saveData(){
            NotificationHelper.get().notifyInsertionData();
            Task<Integer> integerTask = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    return IntoDataBase();
                }
            };
            integerTask.setOnSucceeded(event -> resultOperation(integerTask.getValue()));
            HiloExecutor.execute(integerTask);
    }

    private int IntoDataBase(){
        int size = tabla.getItems().size();
        int dbtablesize = DaoService.getInstance().getProfesorInstance().getall().AsArrayList().size();
        int counter = 0;
        IProfesor profesor = DaoService.getInstance().getProfesorInstance();
        for (Profesor profe : tabla.getItems()) {
            if(dbtablesize>0){
                if(Objects.isNull(profesor.get(profe.getCi()))){
                    profesor.add(profe);
                    counter++;
                }
            }else{
                profesor.add(profe);
                counter++;
            }
        }
        return size - counter;
    };



    private Boolean verifyData(Profesor profesorMateria){
        return Utils.VerifyDNI(profesorMateria.getCi());
    }
}

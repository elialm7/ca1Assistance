package Controladores.ControladorMenu.Profesores;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.BuscarType;
import Controladores.Enums.Fxmls;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfesoresInicioController implements Initializable, NodeUpdator {
    @FXML
    private TableView<Profesor> tabla;
    @FXML
    private TableColumn<Profesor, String> ci;
    @FXML
    private TableColumn<Profesor, String> nombre;
    @FXML
    private TableColumn<Profesor, String> apellido;
    @FXML
    private TableColumn<Profesor, String> sexo;
    @FXML
    private TableColumn<Profesor, String> materia;  // materia, pero en realidad es el campo de nro de telefono
    @FXML
    private Button nuevo;
    @FXML
    private Button update;
    @FXML
    private TextField buscartxt;
    @FXML
    private ComboBox<BuscarType> filtro;
    @FXML
    private ProgressIndicator indicator;

    private TabManipulator tabManipulator ;
    public ProfesoresInicioController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    @Override
    public void updateNode() {
        TableThread();
    }

    private void Editar(Profesor profesorMateria){
        if(Objects.isNull(profesorMateria))return;
        if(tabManipulator.isOpen(Fxmls.PROFESOR_EDICION)){
            NotificationHelper.get().notifyEditionPending();
        }else {
            tabManipulator.add(Fxmls.PROFESOR_EDICION, new ProfesorEdicionController(profesorMateria), "Profesor | Edición");
        }
    }
    private void eliminar(Profesor profesorMateria){

        if(NotificationHelper.get().notifyDeleteOption().get()== ButtonType.OK){
            DaoService.getInstance().getProfesorInstance().delete(profesorMateria.getCi());
            this.tabla.getItems().remove(profesorMateria);
        }
    }
    private void nuevo(){
        tabManipulator.add(Fxmls.PROFESOR_REGISTRO, new ProfesoresRegistroController(), "Profesores | Registro");
    }

    private void updatetable(){
        TableThread();
    }
    private void set_events(){
        this.nuevo.setOnAction(t->nuevo());
        this.update.setOnAction(t->updatetable());
        this.buscartxt.textProperty().addListener((observable, oldValue, newValue) -> buscar(newValue, filtro.getSelectionModel().getSelectedItem()));
    }

    private void loadFilter(){
        ObservableList<BuscarType> buscarTypes = FXCollections.observableArrayList();
        buscarTypes.add(BuscarType.ID);
        buscarTypes.add(BuscarType.Nombre);
        buscarTypes.add(BuscarType.Apellido);
        this.filtro.setItems(buscarTypes);
        this.filtro.getSelectionModel().select(0);
    }
    private void load(){
        set_events();
        load_propertys();
        setrowFactory();
        loadFilter();
        initialize_tabManipulator();
        TableThread();
    }

    private void load_propertys(){
        this.indicator.setVisible(false);
        ci.setCellValueFactory(new PropertyValueFactory<>("ci"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        sexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        materia.setCellValueFactory(new PropertyValueFactory<>("nroprofesor"));
    }

    private void setrowFactory(){

        tabla.setRowFactory(param -> {
            TableRow<Profesor> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editar = new MenuItem("Editar");
            MenuItem eliminar = new Menu("Eliminar");
            editar.setOnAction(event -> Editar(tabla.getSelectionModel().getSelectedItem()));
            eliminar.setOnAction(event -> eliminar(tabla.getSelectionModel().getSelectedItem()));
            contextMenu.getItems().addAll(editar, eliminar);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

    }

    private  void LoadTable(ObservableList<Profesor> profesorMaterias){
        this.tabla.getItems().clear();
        this.tabla.setItems(profesorMaterias);
    }

    private void initialize_tabManipulator(){
        try {
            tabManipulator = TabManipulator.getCurrentInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }
    private void buscar(String busqueda, BuscarType buscarType){
        BuscarThread(busqueda, buscarType);
    }

    private void TableThread(){
        tablethread();
    }

    private  void BuscarThread(String busqueda, BuscarType buscarType){
        Task<ObservableList<Profesor>> task = new Task<ObservableList<Profesor>>() {
            @Override
            protected ObservableList<Profesor> call() throws Exception {
               return DaoService.getInstance().getProfesorInstance().findby(busqueda, buscarType).AsObservableList();
            }
        };

        task.runningProperty().addListener((observable, olvalue, newvalue)->modifyIndicator(newvalue));
        task.setOnSucceeded(event -> LoadTable(task.getValue()));
        HiloExecutor.execute(task);
    }



    private void tablethread(){
        Task<ObservableList<Profesor>> call = new Task<ObservableList<Profesor>>() {
            @Override
            protected ObservableList<Profesor> call() throws Exception {
                return  DaoService.getInstance().getProfesorInstance().getall().AsObservableList();
            }
        };

        call.runningProperty().addListener((observable, oldValue, newValue) -> modifyIndicator(newValue));
        call.setOnSucceeded(event -> LoadTable(call.getValue()));
        HiloExecutor.execute(call);
    }

    private void modifyIndicator(boolean b){
        if(b){
            this.indicator.setVisible(b);
        }else{
            this.indicator.setVisible(b);
        }
    }

}

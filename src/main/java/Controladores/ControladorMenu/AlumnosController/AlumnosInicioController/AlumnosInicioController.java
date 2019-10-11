package Controladores.ControladorMenu.AlumnosController.AlumnosInicioController;

import Controladores.ControladorMenu.AlumnosController.EdicionController.AlumnosEdicionController;
import Controladores.ControladorMenu.AlumnosController.RegistroController.AlumnosRegistroController;
import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.BuscarType;
import Controladores.Enums.Fxmls;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AlumnosInicioController implements Initializable,NodeUpdator {

    @FXML
    private TableView<Alumno> tableview;

    @FXML
    private TableColumn<Alumno, String> dnicolumn;

    @FXML
    private TableColumn<Alumno, String> nombrecolumn;

    @FXML
    private TableColumn<Alumno, String> apellidocolumn;

    @FXML
    private TableColumn<Alumno, String> sexocolumn;

    @FXML
    private TableColumn<Alumno, String> nrpadrecolumn;

    @FXML
    private TableColumn<Alumno, String> nromadrecolumn;

    @FXML
    private TableColumn<Alumno, String> promocolumn;

    @FXML
    private Button nuevo;

    @FXML
    private Button eliminar;

    @FXML
    private Button editar;

    @FXML
    private TextField txtbuscar;

    @FXML
    private ComboBox<BuscarType> filtrocombo;


    @FXML
    private CheckBox checkLook;

    @FXML
    private Button update;

    @FXML
    private ComboBox<String> filtrobachiller;

    @FXML
    private ComboBox<String> filtrobseccion;

    @FXML
    private ComboBox<String> filtrogrado;

    @FXML
    private ProgressIndicator indicator;

    @FXML
    private Label size;

    private TabManipulator tabManipulator;

    public AlumnosInicioController() {
    }

    private void showEditar(Alumno alumno) {
            boolean isopen = tabManipulator.isOpen(Fxmls.AlUMNOS_EDICION);
            if(!isopen)
                tabManipulator.add(Fxmls.AlUMNOS_EDICION, new AlumnosEdicionController(alumno), "Alumnos | Edición");
            else
               NotificationHelper.get().notifyEditionPending();


    }

    private void set_cellfactory() {
        dnicolumn.setCellValueFactory(new PropertyValueFactory<>("ci"));
        nombrecolumn.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        apellidocolumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        sexocolumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        nrpadrecolumn.setCellValueFactory(new PropertyValueFactory<>("nropadre"));
        nromadrecolumn.setCellValueFactory(new PropertyValueFactory<>("nromadre"));
        promocolumn.setCellValueFactory(new PropertyValueFactory<>("promo"));
    }


    private void settablerowfactory() {
        tableview.setRowFactory((TableView<Alumno> param) -> {
            final TableRow<Alumno> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem editar = new MenuItem("Editar");
            editar.setOnAction(event -> showEditar(tableview.getSelectionModel().getSelectedItem()));
            MenuItem delete = new MenuItem("Eliminar");
            delete.setOnAction(event -> delete(tableview.getSelectionModel().getSelectedItem()));
            contextMenu.getItems().addAll(editar, delete);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu) null));

            return row;
        });
    }


    private void delete(Alumno A) {
        Optional<ButtonType> result = NotificationHelper.get().notifyDeleteOption();
        if (result.get() == ButtonType.OK) {
            DaoService.getInstance().getAlumnoInstance().delete(A.getCi());
            updatetable();
        }
    }

    private void loadfilter() {
        List<BuscarType> list = new ArrayList<>();
        list.add(BuscarType.ID);
        list.add(BuscarType.Nombre);
        list.add(BuscarType.Apellido);
        filtrocombo.setItems(FXCollections.observableList(list));
        filtrocombo.getSelectionModel().select(list.get(0));
    }

    private void setevents() {
        this.indicator.setVisible(false);
        this.txtbuscar.textProperty().addListener((observable, oldValue, newValue) -> buscar(newValue, filtrocombo.getSelectionModel().getSelectedItem()));
        this.update.setOnAction(t -> TableThread());
        this.nuevo.setOnAction(t -> tabManipulator.add(Fxmls.ALUMNOS_REGISTRO, new AlumnosRegistroController(), "Alumnos | Registro "));
    }

    private void buscar(String busqueda, BuscarType buscarType) {
        if (checkLook.isSelected()) {
            Curso curso = new Curso(filtrobachiller.getSelectionModel().getSelectedItem(), filtrobseccion.getSelectionModel().getSelectedItem(), filtrogrado.getSelectionModel().getSelectedItem());
            FindThread(busqueda, curso, buscarType, true);
        } else {
            FindThread(busqueda, null, buscarType, false);
        }
    }

    private void load_filtroavanzado() {
        ObservableList<String> seccion = DaoService.getInstance().getCursoInstance().getAllseccion().AsObservableList();
        ObservableList<String> grados = DaoService.getInstance().getCursoInstance().getAllGrado().AsObservableList();
        ObservableList<String> bachiller = DaoService.getInstance().getCursoInstance().getAllBachiller().AsObservableList();
        this.filtrobachiller.setItems(bachiller);
        this.filtrobachiller.getSelectionModel().select(0);
        this.filtrobseccion.setItems(seccion);
        this.filtrobseccion.getSelectionModel().select(0);
        this.filtrogrado.setItems(grados);
        this.filtrogrado.getSelectionModel().select(0);
    }


    private void updatetable(ObservableList<Alumno> observableList) {
        tableview.setItems(observableList);
    }

    private void updatetable() {
        TableThread();
    }

    private void instancecontroller() {
        try {
            tabManipulator = TabManipulator.getCurrentInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void setSize(int size){
        String mensaje = "Cantidad de Alumnos: "+size;
        this.size.setText(mensaje);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setevents();
        instancecontroller();
        set_cellfactory();
        settablerowfactory();
        loadfilter();
        load_filtroavanzado();
        TableThread();
    }

    @Override
    public void updateNode() {
        load_filtroavanzado();
        TableThread();
    }

    private void indicatorstatus(boolean bool){
        //JKAJAJJAJAJAJ tengo que simplificar esto jajajaj
        if (bool) {
            indicator.setVisible(true);
        } else {
            indicator.setVisible(false);
        }
    }

    private void TableThread(){
        this.tableview.getItems().clear();
        Task<ObservableList<Alumno>> taskcall = new Task<ObservableList<Alumno>>() {
            @Override
            protected ObservableList<Alumno> call() throws Exception {
                return DaoService.getInstance().getAlumnoInstance().getall().AsObservableList();
            }
        };
        taskcall.runningProperty().addListener((observable, oldValue, newValue) -> indicatorstatus(newValue));
        taskcall.setOnSucceeded(event ->{
            ObservableList<Alumno> alumnos = taskcall.getValue();
            AlumnosInicioController.this.updatetable(alumnos);
            setSize(alumnos.size());
        });
        HiloExecutor.execute(taskcall);
    }

    private void FindThread(String busqueda, Curso curso, BuscarType buscarType, boolean to){
        Task<ObservableList<Alumno>> call = new Task<ObservableList<Alumno>>() {
            @Override
            protected ObservableList<Alumno> call() throws Exception {
                return DaoService.getInstance().getAlumnoInstance().findby(busqueda, curso, buscarType, to).AsObservableList();
            }
        };
        call.runningProperty().addListener((observable, oldValue, newValue) -> indicatorstatus(newValue));
        call.setOnSucceeded(event -> AlumnosInicioController.this.updatetable(call.getValue()));
        HiloExecutor.execute(call);
    }
}

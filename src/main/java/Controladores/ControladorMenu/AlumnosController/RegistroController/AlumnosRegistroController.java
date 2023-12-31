package Controladores.ControladorMenu.AlumnosController.RegistroController;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.Sexo;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ALumno.AlumnoCurso;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SQLITE.Interfaces.IAlumno;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static Servicios.Utils.Utils.VerifyPromo;
import static Servicios.Utils.Utils.getNewIntegerID;

public class AlumnosRegistroController implements Initializable, NodeUpdator {


    @FXML
    private TextField nombretxt;

    @FXML
    private TextField apellidotxt;

    @FXML
    private ComboBox<Sexo> sexocombo;

    @FXML
    private TextField nromadretxt;

    @FXML
    private TextField nropadretxt;

    @FXML
    private ComboBox<String> bachillercombo;

    @FXML
    private ComboBox<String> gradocombo;

    @FXML
    private ComboBox<String> seccionCombo;

    @FXML
    private TextField promotxt;

    @FXML
    private TableView<AlumnoCurso> tabla;

    @FXML
    private TableColumn<AlumnoCurso, String> columnadni;

    @FXML
    private TableColumn<AlumnoCurso, String> columnanombre;

    @FXML
    private TableColumn<AlumnoCurso, String> columnaApellido;

    @FXML
    private TableColumn<AlumnoCurso, String> columnasexo;

    @FXML
    private TableColumn<AlumnoCurso, String> columnapadre;

    @FXML
    private TableColumn<AlumnoCurso, String> columnamadre;

    @FXML
    private TableColumn<AlumnoCurso, String> columnapromo;

    @FXML
    private TableColumn<AlumnoCurso, String> columnabachiller;

    @FXML
    private TableColumn<AlumnoCurso, String> columnagrado;

    @FXML
    private TableColumn<AlumnoCurso, String> columnaseccion;

    @FXML
    private Button anadir;

    @FXML
    private Button cancelar;

    @FXML
    private Button guardar;

    @FXML
    private ProgressIndicator indicator;

    @FXML
    private Label size;

    private int registros = 0;

    private TabManipulator tabManipulator;

    public AlumnosRegistroController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    @Override
    public void updateNode() {
        load_cursoCombos();
    }


    private void load(){
        loadPropertys();
        set_events();
        set_valuesFactory();
        setrowfactory();
        load_sexoCombo();
        load_cursoCombos();
        loadtabmanipulator();
    }

    private void loadtabmanipulator(){
        try {
            this.tabManipulator = TabManipulator.getCurrentInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void setrowfactory(){
        this.tabla.setRowFactory(param -> {
            TableRow<AlumnoCurso> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Eliminar de la lista");
            menuItem.setOnAction(t-> delete(tabla.getSelectionModel().getSelectedItem()));
            contextMenu.getItems().add(menuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

        this.tabla.getItems().addListener((ListChangeListener<AlumnoCurso>) c -> {
            while(c.next()){
                if(tabla.getItems().size()==0){
                    guardar.setDisable(true);
                }else{
                    guardar.setDisable(false);
                }
            }
        });
    }
    private void set_valuesFactory(){
        columnadni.setCellValueFactory(new PropertyValueFactory<>("ci"));
        columnanombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnasexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        columnapadre.setCellValueFactory(new PropertyValueFactory<>("nropadre"));
        columnamadre.setCellValueFactory(new PropertyValueFactory<>("nromadre"));
        columnapromo.setCellValueFactory(new PropertyValueFactory<>("promo"));
        columnabachiller.setCellValueFactory(new PropertyValueFactory<>("bachiller"));
        columnagrado.setCellValueFactory(new PropertyValueFactory<>("grado"));
        columnaseccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
    }
    private void set_events(){
        this.anadir.setOnAction(event -> addtotable());
        this.guardar.setOnAction(t->addAll());
        this.cancelar.setOnAction(t->cancelar());
    }
    private void loadPropertys(){
        this.indicator.setVisible(false);
        this.guardar.setDisable(true);
    }
    private void cancelar(){
            if(registros>0) {
                Optional<ButtonType> result = NotificationHelper.get().notifyOnCancelOperation();
                if (result.get() == ButtonType.OK) {
                   tabManipulator.remove(Fxmls.ALUMNOS_REGISTRO);
                }
            }else{
                tabManipulator.remove(Fxmls.ALUMNOS_REGISTRO);
            }
    }
    private void load_cursoCombos(){
        ObservableList<String>  bachiller = DaoService.getInstance().getCursoInstance().getAllBachiller().AsObservableList();
        ObservableList<String> seccion = DaoService.getInstance().getCursoInstance().getAllseccion().AsObservableList();
        ObservableList<String> grado = DaoService.getInstance().getCursoInstance().getAllGrado().AsObservableList();
        this.bachillercombo.setItems(bachiller);
        this.bachillercombo.getSelectionModel().select(0);
        this.seccionCombo.setItems(seccion);
        this.seccionCombo.getSelectionModel().select(0);
        this.gradocombo.setItems(grado);
        this.gradocombo.getSelectionModel().select(0);
    }

    private void load_sexoCombo(){
        List<Sexo> sexos = new ArrayList<>();
        sexos.add(Sexo.Femenino);
        sexos.add(Sexo.Masculino);
        this.sexocombo.setItems(FXCollections.observableList(sexos));
        this.sexocombo.getSelectionModel().select(sexos.get(0));
    }
    private String getID(){
        if(tabla.getItems().size()>0){
            return getNewIntegerID(tabla.getItems());
        }else{
            return getNewIntegerID(DaoService.getInstance().getAlumnoInstance().getall().AsArrayList());
        }
    }

    private AlumnoCurso getdatafromform(){
        AlumnoCurso alumnoCurso = null;
        String ci = getID();
        String promo = this.promotxt.getText();
        String nombre = this.nombretxt.getText();
        String apellidos = this.apellidotxt.getText();
        String sexo = this.sexocombo.getSelectionModel().getSelectedItem().name();
        String nrop = this.nropadretxt.getText();
        String nrom = this.nromadretxt.getText();
        String bachiller = this.bachillercombo.getSelectionModel().getSelectedItem();
        String seccion = this.seccionCombo.getSelectionModel().getSelectedItem();
        String grado = this.gradocombo.getSelectionModel().getSelectedItem();
        int id_curso = DaoService.getInstance().getCursoInstance().getcursoIDbybachiller(bachiller, seccion, grado);
        if (!VerifyPromo(promo, '#')){
             NotificationHelper.get().notifyIncorrectPromoFormat();
        }else{
            //error corregido.
            Alumno alumno = new Alumno(nombre, apellidos , ci, sexo, nrom, nrop, promo, id_curso);
            Curso curso = new Curso(bachiller, seccion, grado);
            alumnoCurso = new AlumnoCurso(alumno, curso);
        }
            return alumnoCurso;
    }
    private void addtotable(){
        AlumnoCurso alumnoCurso = getdatafromform();
        if(alumnoCurso!=null){
            boolean exists = Utils.ExistsInTable(alumnoCurso, this.tabla);
            if(!exists) {
                this.tabla.getItems().add(alumnoCurso);
                registros++;
                updatelabel();
            }else{
                NotificationHelper.get().notifyDuplicatedDni(alumnoCurso);
            }
        }

    }
    private void addAll()  {
            Optional<ButtonType> result = NotificationHelper.get().notifyInsertionData();
            if (result.get() == ButtonType.OK) {
                if (tabla.getItems().size() != 0)
                    InsertingDataThread();
            }
    }

    private void delete(AlumnoCurso alumnoCurso){
        this.tabla.getItems().remove(alumnoCurso);
        registros--;
        updatelabel();
    }



    private void setSize(int size){
        String mensaje= "Cantidad : "+size;
        this.size.setText(mensaje);
    }

    private void updatelabel(){
        setSize(registros);
    }

    private void IndicatorStatus(boolean bool){
        if(bool){
            this.indicator.setVisible(bool);
        }else{
            this.indicator.setVisible(bool);
        }
    }

    private void InsertingDataThread(){
        Task<Integer> call = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return loadingtoDatabase();

            }
        };
        call.runningProperty().addListener((observable, oldValue, newValue) -> IndicatorStatus(newValue));
        call.setOnSucceeded(event -> {
            int skipped = call.getValue();
            if(skipped>0){
                NotificationHelper.get().notifySkippedOnInsertOperation(skipped);
            }else{
                NotificationHelper.get().notifySuccessOninsertOperation(skipped);
            }
            this.tabla.getItems().clear();
        });

        HiloExecutor.execute(call);
    }

    private int loadingtoDatabase(){
        IAlumno alumno = DaoService.getInstance().getAlumnoInstance();
        int size = this.tabla.getItems().size();
        int current = 0;
        for(AlumnoCurso alumnoCurso: this.tabla.getItems()){
            if(!alumno.exists(alumnoCurso)){
                alumno.add(alumnoCurso);
                current++;
            }
        }
        return size - current;
    }

}

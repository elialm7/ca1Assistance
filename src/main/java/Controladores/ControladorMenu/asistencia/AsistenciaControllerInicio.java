package Controladores.ControladorMenu.asistencia;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.TypesAccess;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SqliteDaoService.DaoService;
import Servicios.Utils.Utils;
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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class AsistenciaControllerInicio implements Initializable, NodeUpdator {


    @FXML
    private TableView<AsistenciaDiaria> table;

    @FXML
    private TableColumn<AsistenciaDiaria, Integer> id_column;

    @FXML
    private TableColumn<AsistenciaDiaria, String> fecha_column;

    @FXML
    private TableColumn<AsistenciaDiaria, String> bachiller_column;

    @FXML
    private TableColumn<AsistenciaDiaria, String> seccion;

    @FXML
    private TableColumn<AsistenciaDiaria, String> grado;

    @FXML
    private DatePicker fecha_solo;

    @FXML
    private CheckBox avanzado;

    @FXML
    private DatePicker fecha_uno;

    @FXML
    private DatePicker fecha_dos;

    @FXML
    private ChoiceBox<String> cursos;

    @FXML
    private Button filtrar;

    @FXML
    private Button mostrartodo;

    @FXML
    private Button nuevo;

    @FXML
    private ProgressIndicator indicator;

    private TabManipulator tabManipulator;

    private HashMap<String, Curso> CursosMap;


    public AsistenciaControllerInicio(){}

    @Override
    public void updateNode() {
        loadmap();
        loadtable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    private void load(){
        loadIndicator();
        set_events();
        loadtableColumnPropertys();
        loadTableOptions();
        loadCursoOptions();
        loadTabManipulator();
        loadPickersPropertys();
        loadtable();
    }

    private void loadIndicator(){
        this.indicator.setVisible(false);
    }

    private void set_events(){
        this.mostrartodo.setOnAction(t->mostrartodo());
        this.filtrar.setOnAction(t->filtrar());
        this.nuevo.setOnAction(t -> opennewForm());
    }

    private void loadTabManipulator(){
        try{
            tabManipulator = TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    private void loadtableColumnPropertys(){
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        fecha_column.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        bachiller_column.setCellValueFactory(new PropertyValueFactory<>("bachiller"));
        seccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        grado.setCellValueFactory(new PropertyValueFactory<>("grado"));

    }

    private void loadPickersPropertys(){
        List<DatePicker> pickers = new ArrayList<>();
        pickers.add(fecha_solo);
        pickers.add(fecha_uno);
        pickers.add(fecha_dos);
        Utils.setpickerpropery(pickers);
    }

    private void loadTableOptions(){
        table.setRowFactory(param -> {
            final TableRow<AsistenciaDiaria> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem delete = new MenuItem("Eliminar");
            MenuItem ver = new MenuItem("Ver");
            delete.setOnAction(event -> delete());
            ver.setOnAction(event -> ver());
            contextMenu.getItems().addAll(delete, ver);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(contextMenu).otherwise((ContextMenu) null));
            return row;
        });
    }

    private void opennewForm(){
        if(tabManipulator.isOpen(Fxmls.ASISTENCIA_FORM)){
            NotificationHelper.get().notifyTabOpened();
        }else{
             tabManipulator.add(Fxmls.ASISTENCIA_FORM,
                    new AsistenciaControllerForm(TypesAccess.AS_NEW), "Asistencia | Nuevo");
        }
    }


    private void loadCursoOptions(){
        CursosMap = new HashMap<>();
        loadmap();
    }

    private void loadmap(){
        ObservableList<String> listcurso = FXCollections.observableArrayList();
        ObservableList<Curso> cursos =  DaoService.getInstance().getCursoInstance().getall().AsObservableList();
        for(Curso curso : cursos){
            String key = getKey(curso);
            CursosMap.put(key, curso);
            listcurso.add(key);
        }
        this.cursos.setItems(listcurso);
        this.cursos.getSelectionModel().select(0);
    }

    private String getKey(Curso curso){
        return curso.getBachiller() + " - "+curso.getGrado() + " - "+curso.getSeccion();
    }

    private void delete(){
        //esta cargando
        AsistenciaDiaria asistenciaDiaria = table.getSelectionModel().getSelectedItem();
        if(NotificationHelper.get().notifyDeleteOption().get() == ButtonType.OK){
            DaoService.getInstance().getAsistenciaUnionInstance().delete(asistenciaDiaria.getId());
            updatetable();
        }
    }
    private void ver(){
        AsistenciaDiaria asistenciaDiaria = table.getSelectionModel().getSelectedItem();
        if(!tabManipulator.isOpen(Fxmls.ASISTENCIA_FORM)) {
            tabManipulator.add(Fxmls.ASISTENCIA_FORM, new AsistenciaControllerForm(TypesAccess.AS_VIEWER, asistenciaDiaria), "Asistencia | Detalles");
        }else{
            NotificationHelper.get().notifyTabOpened();
        }
    }

    private void mostrartodo(){
        loadtable();
    }
    private void filtrar(){
        if(isOktoFind()) {
            find();
        }else{
            NotificationHelper.get().notifyProblemTofind();
        }
    }
    private void updatetable(){
        loadtable();
    }
    private void setData(ObservableList<AsistenciaDiaria> asistenciaDiariaObservableList){
        this.table.getItems().clear();
        this.table.setItems(asistenciaDiariaObservableList);
    }

    private void loadtable(){
        Task<ObservableList<AsistenciaDiaria>> task = new Task<ObservableList<AsistenciaDiaria>>() {
            @Override
            protected ObservableList<AsistenciaDiaria> call() throws Exception {
                return DaoService.getInstance().getAsistenciaUnionInstance().getall().AsObservableList();
            }
        };
        task.runningProperty().addListener((observable, oldValue, newValue) -> setStatus(newValue));
        task.setOnSucceeded(event -> setData(task.getValue()));
        HiloExecutor.execute(task);
    }

    private  void find(){
        Task<ObservableList<AsistenciaDiaria>> task = new Task<ObservableList<AsistenciaDiaria>>() {
            @Override
            protected ObservableList<AsistenciaDiaria> call() throws Exception {
               return getFindindResults();
            }
        };
        task.runningProperty().addListener((observable, oldValue, newValue) -> setStatus(newValue));
        task.setOnSucceeded(event -> setData(task.getValue()));
        HiloExecutor.execute(task);
    }

    private synchronized void setStatus(boolean status){
        if(status) {
            this.indicator.setVisible(true);
        }else{
            this.indicator.setVisible(false);
        }
    }

    private Curso getSelectedCurso(){
        String select = this.cursos.getSelectionModel().getSelectedItem();
        if(CursosMap.containsKey(select)){
            return CursosMap.get(select);
        }
        return null;
    }

    private ObservableList<AsistenciaDiaria> getFindindResults(){
        if(avanzado.isSelected()){
            String date1 = this.fecha_uno.getEditor().getText();
            String date2 = this.fecha_dos.getEditor().getText();
            return  DaoService.getInstance().getAsistenciaUnionInstance().getBetween(date1,date2, getSelectedCurso()).AsObservableList();
        }else {
            String fecha = this.fecha_solo.getEditor().getText();
            return DaoService.getInstance().getAsistenciaUnionInstance().getby(fecha).AsObservableList();
        }
    }


    private boolean isOktoFind(){
        if(avanzado.isSelected()){
            String date1 = this.fecha_uno.getEditor().getText();
            String date2 = this.fecha_dos.getEditor().getText();
          if(date1.isEmpty()){
              return false;
          }else return !date2.isEmpty();
        }else{
            return !fecha_solo.getEditor().getText().isEmpty();
        }
    }
}

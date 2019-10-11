package Controladores.ControladorMenu.Horarios;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class HorariosController implements Initializable, NodeUpdator {
    @FXML
    private TitledPane horario_inicio_pane;

    @FXML
    private TableView<Horario> tableviewInicio;

    @FXML
    private TableColumn<Horario, String> Hora_ini_Inicio;

    @FXML
    private TableColumn<Horario, String> hora_fin_Inicio;

    @FXML
    private JFXTimePicker horainipicker_inicio;

    @FXML
    private JFXTimePicker horafinpicker_ini;

    @FXML
    private Button aplicar_inicio;

    @FXML
    private Button anadir_inicio;

    @FXML
    private Button cancel;

    private TabManipulator tabManipulator;

    private Horario CurrentHorario = null;

    public HorariosController() {

    }

    private void initializaHorarioInicio(){
        loadTablePropertys( Hora_ini_Inicio, hora_fin_Inicio);
        setPropertyTime(horainipicker_inicio);
        setPropertyTime(horafinpicker_ini);
        this.aplicar_inicio.setVisible(false);
        this.anadir_inicio.setOnAction(event -> add_Inicio());
        this.aplicar_inicio.setOnAction(event -> apply_Inicio());
        this.cancel.setOnAction(event -> {
            cancel_Inicio();
        });
        loadtabmanipulator();
        loadTable();
    }

    private void loadtabmanipulator(){
        try {
            this.tabManipulator = TabManipulator.getCurrentInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializaHorarioInicio();
        updateInicio();
    }

    @Override
    public void updateNode() {
        updateInicio();
    }

    private void loadTablePropertys(TableColumn<Horario, String> hora_ini, TableColumn<Horario, String> hora_fin){
        if(Objects.nonNull(hora_ini) && Objects.nonNull(hora_fin)){
            hora_ini.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioInicio"));
            hora_fin.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioFin"));
        }

    }
    private String getTime(JFXTimePicker timePicker){
        if(!timePicker.getEditor().getText().isEmpty()){
            try {
                String st = timePicker.getEditor().getText();
                Date date = new SimpleDateFormat("h:mm a").parse(st);
                return new SimpleDateFormat("HH:mm:ss").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            return "";
        }
        return null;
    }
    private String parseAgain(String txt){
        try {
            Date date = new SimpleDateFormat("HH:mm:ss").parse(txt);
            return new SimpleDateFormat("h:mm a").format(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void setPropertyTime(JFXTimePicker time){
        time.set24HourView(true);
    }


    private void add_Inicio(){
        Horario horario = getDataFromForm();
        if(Objects.isNull(horario)){
           NotificationHelper.get().notifyOnnulldata();
            return;
        }
        if(!verifyIfExistintable(horario)){
            DaoService.getInstance().getHorarioInstance().add(horario);
            NotificationHelper.get().notifyOnAdded();
            UpdateDataTable();
        }
    }
    private void apply_Inicio(){
        Horario temp = getDataFromForm();
        if(Objects.isNull(temp)){
            NotificationHelper.get().notifyOnnulldata();
            return;
        }
        if(!Objects.isNull(CurrentHorario)){
            CurrentHorario.setHorarioInicio(temp.getHorarioInicio());
            CurrentHorario.setHorarioFin(temp.getHorarioFin());
            DaoService.getInstance().getHorarioInstance().change(CurrentHorario);
            NotificationHelper.get().notifyOnchanged();
            setDatatopickers(null);
            UpdateDataTable();
        }
    }
    private void delete_Inicio(Horario horario){
        if(NotificationHelper.get().notifyDeleteOption().get() == ButtonType.OK){
            DaoService.getInstance().getHorarioInstance().delete(horario.getIdhorario());
            setDatatopickers(null);
            UpdateDataTable();
        }
    }

    private void cancel_Inicio(){
        this.tabManipulator.remove(Fxmls.HORARIO_INICIO);
    }

    private void updateInicio(){
        UpdateDataTable();
    }

    private void UpdateDataTable(){
       ObservableList<Horario> items = DaoService.getInstance().getHorarioInstance().getall().AsObservableList();
        if(Objects.isNull(items))return;
        this.tableviewInicio.getItems().clear();
        this.tableviewInicio.setItems(items);
    }
    private void setDatatopickers(Horario horario){
        if(Objects.isNull(horario)){
            CurrentHorario = null;
            this.horainipicker_inicio.getEditor().setText("");
            this.horafinpicker_ini.getEditor().setText("");
        }else{
            CurrentHorario = horario;
            this.horainipicker_inicio.getEditor().setText(parseAgain(horario.getHorarioInicio()));
            this.horafinpicker_ini.getEditor().setText(parseAgain(horario.getHorarioFin()));
        }
    }
    private void loadTable(){
        tableviewInicio.setRowFactory(param -> {
            final TableRow<Horario> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem delete = new MenuItem("Eliminar");
            delete.setOnAction(event -> delete_Inicio(this.tableviewInicio.getSelectionModel().getSelectedItem()));
            row.setOnMouseClicked(event -> {
                if(row.isSelected()){
                    anadir_inicio.setVisible(false);
                    aplicar_inicio.setVisible(true);
                    setDatatopickers(row.getItem());
                }else{
                    anadir_inicio.setVisible(true);
                    aplicar_inicio.setVisible(false);
                    setDatatopickers(null);
                }
            });
            contextMenu.getItems().add(delete);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(contextMenu).otherwise((ContextMenu)null));
            return row;
        });
    }

    private Horario getDataFromForm(){
        String horaInicio = getTime(horainipicker_inicio);
        String horaFin = getTime(horafinpicker_ini);
        if(horaInicio.isEmpty()){
            return null;
        }else if(horaFin.isEmpty()){
            return null;
        }else{
            return new Horario(horaInicio, horaFin);
        }
    }

    private boolean verifyIfExistintable(Horario horario){
        ObservableList<Horario> horarios = this.tableviewInicio.getItems();
        int count = 0;
        for(Horario h: horarios){
            if(h.equals(horario)){
                count++;
            }
        }
        return count>0;
    }
}


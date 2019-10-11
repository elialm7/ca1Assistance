package Controladores.ControladorMenu.asistencia;

import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.Pojos.Asistencia.util.AsistenciaManipulator;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.SqliteDaoService.DaoService;
import Servicios.Utils.ASAS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlumnoAsistenciaAdderForm implements Initializable {

    @FXML
    private Label titulo;

    @FXML
    private TableView<AlumnoHorarioAsistencia> tablapresencia;

    @FXML
    private TableColumn<AlumnoHorarioAsistencia, String> horarioColumn;

    @FXML
    private TableColumn<AlumnoHorarioAsistencia, String> presenciacolumn;

    @FXML
    private Label titulo1;

    @FXML
    private ChoiceBox<String> alumnobox;

    @FXML
    private Label titulo11;

    @FXML
    private ChoiceBox<String> horariobox;

    @FXML
    private Label titulo111;

    @FXML
    private ChoiceBox<String> presenciabox;

    @FXML
    private Button add;

    @FXML
    private Button aceptar;

    // campos de clase
    private boolean closed  = false;
    private Stage st;

    //listas y manpuladores
    private List<Alumno> alumnos;
    private ObservableList<String> listaAlumnos;
    private AsistenciaManipulator<String, Horario> horariomapa;
    private AsistenciaManipulator<String, Alumno> alumnomapa;
    private List<AlumnoHorarioAsistencia> presencias;

    public AlumnoAsistenciaAdderForm(List<Alumno> alumnos) {
        this.alumnos = alumnos;
        horariomapa = new AsistenciaManipulator<>();
        alumnomapa = new AsistenciaManipulator<>();
        presencias = new ArrayList<>();
    }

    private void addevents(){

        this.add.setOnAction(event -> add());

        this.aceptar.setOnAction(event -> aceptar());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTablePropertys();
        loadCombos();
        laodAlumnos();
        addevents();
    }
    private void laodAlumnos(){
        System.out.println("no llego ");
        for (Alumno alumno : alumnos){
            alumnomapa.addObject((alumno.getApellidos() + " "+alumno.getNombres()), alumno);
        }
        listaAlumnos = ASAS.ToASAS(alumnomapa.getKeysList()).AsObservableList();
        this.alumnobox.setItems(listaAlumnos);
        this.alumnobox.getSelectionModel().selectFirst();
    }
    public void setSt(Stage stage) {
        this.st = stage;
    }

    private void loadCombos(){
        ObservableList<Horario> horarios = DaoService.getInstance().getHorarioInstance().getall().AsObservableList();
        ObservableList<String> presencias = FXCollections.observableArrayList("Presente", "Ausente");
        this.presenciabox.setItems(presencias);
        this.presenciabox.getSelectionModel().selectFirst();
        for(Horario horario : horarios){
            String llave = horario.getHorarioInicio() + " - "+horario.getHorarioFin();
            horariomapa.addObject(llave, horario);
        }
        this.horariobox.setItems(ASAS.ToASAS(horariomapa.getKeysList()).AsObservableList());
        this.horariobox.getSelectionModel().selectFirst();
    }

    private void loadTablePropertys(){
        this.horarioColumn.setCellValueFactory(new PropertyValueFactory<>("horarioAsistencia"));
        this.presenciacolumn.setCellValueFactory(new PropertyValueFactory<>("Asistencia"));
    }

    private void add(){
        String presencia = this.presenciabox.getSelectionModel().getSelectedItem();
        Horario horario = horariomapa.getObject(this.horariobox.getSelectionModel().getSelectedItem());
        AlumnoHorarioAsistencia alumnoHorarioAsistencia = new AlumnoHorarioAsistencia(horario, presencia);
        this.presencias.add(alumnoHorarioAsistencia);
        this.tablapresencia.setItems(ASAS.ToASAS(presencias).AsObservableList());
    }

    private void aceptar(){
        Alumno alumno = alumnomapa.getObject(alumnobox.getSelectionModel().getSelectedItem());
        alumnos.remove(alumno);
        this.st.hide();
    }

    public Pair<AlumnoAsistencia,List<Alumno>> getData() {
        if(closed){
            return null;
        }else {
            AlumnoAsistencia alumnoAsistencia = new AlumnoAsistencia(alumnomapa.getObject(alumnobox.getSelectionModel().getSelectedItem()));
            alumnoAsistencia.setHorarioAsistidos(presencias);
            return new Pair<>(alumnoAsistencia, alumnos);
        }
    }

    public void setClosed(boolean b) {
        this.closed = b;
    }
}

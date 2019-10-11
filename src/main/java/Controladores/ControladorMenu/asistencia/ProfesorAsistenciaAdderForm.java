package Controladores.ControladorMenu.asistencia;

import Modelos.Pojos.Asistencia.ProfsorAsistencia.AsistenciaProfe;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.PresenciaProfe;
import Modelos.Pojos.Asistencia.util.AsistenciaManipulator;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.Pojos.ColegioEtc.Materia;
import Modelos.Pojos.Profesor.Profesor;
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

public class ProfesorAsistenciaAdderForm implements Initializable {


    @FXML
    private Label titulo;

    @FXML
    private TableView<PresenciaProfe> tablapresencia;

    @FXML
    private TableColumn<PresenciaProfe, String> horarioColumn;

    @FXML
    private TableColumn<PresenciaProfe, String> materiacolumn;

    @FXML
    private TableColumn<PresenciaProfe, String> presenciacolumn;

    @FXML
    private Label titulo1;

    @FXML
    private ChoiceBox<String> Profesorbox;

    @FXML
    private Label titulo11;

    @FXML
    private ChoiceBox<String> horariobox;

    @FXML
    private Label titulo111;

    @FXML
    private ChoiceBox<String> materiabox;

    @FXML
    private Label titulo1111;

    @FXML
    private ChoiceBox<String> presenciabox;

    @FXML
    private Button add;

    @FXML
    private Button aceptar;

    private ObservableList<String> ListaProfesores;
    private List<PresenciaProfe> presenciaProfes;
    private AsistenciaProfe asistenciaProfe;
    private List<Profesor> profesors;
    private AsistenciaManipulator<String, Profesor> mapaProfe;
    private AsistenciaManipulator<String, Horario> mapaHorario;
    private AsistenciaManipulator<String, Materia> mapaMateria;
    private Stage st;
    private AsistenciaProfe profe;
    private boolean closed = false;
    public ProfesorAsistenciaAdderForm(List<Profesor> profesores){
        mapaProfe = new AsistenciaManipulator<>();
        mapaHorario = new AsistenciaManipulator<>();
        mapaMateria = new AsistenciaManipulator<>();
        presenciaProfes =new ArrayList<>();
        this.profesors = profesores;
    }

    public void setSt(Stage st){
        this.st = st;
    }
    private void loadChoiceBox(){
        for(Profesor profesor: profesors){
            mapaProfe.addObject(llave(profesor), profesor);
        }
        ListaProfesores = ASAS.ToASAS(mapaProfe.getKeysList()).AsObservableList();
        this.Profesorbox.setItems(ListaProfesores);
        this.Profesorbox.getSelectionModel().selectFirst();
    }

    private String llave(Profesor profesor){
        return profesor.getApellidos() + "  "+profesor.getNombres();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }
    private void load(){
            loadtablepropertys();
            loadChoiceBox();
            loaddb();
            loadevents();
    }
    private void loadevents(){
        this.aceptar.setOnAction(t->aceptar());
        this.add.setOnAction(t->addtotable());
       /* this.st.setOnCloseRequest(event -> {
            closed = true;
        });*/
    }
    public void setClosed(boolean bool){
        this.closed = bool;
    }
    private void loadtablepropertys(){
        this.horarioColumn.setCellValueFactory(new PropertyValueFactory<>("horario"));
        this.presenciacolumn.setCellValueFactory(new PropertyValueFactory<>("presencia"));
        this.materiacolumn.setCellValueFactory(new PropertyValueFactory<>("materia"));
    }
    private void addtotable(){
        //Profesor SelectedProfesor = mapaProfe.getObject(Profesorbox.getSelectionModel().getSelectedItem());
        Horario horario = this.mapaHorario.getObject(horariobox.getSelectionModel().getSelectedItem());
        Materia materia = this.mapaMateria.getObject(materiabox.getSelectionModel().getSelectedItem());
        String presencia = presenciabox.getSelectionModel().getSelectedItem();
        PresenciaProfe presenciaProfe = new PresenciaProfe(horario, materia, presencia);
        this.presenciaProfes.add(presenciaProfe);
        this.tablapresencia.setItems(ASAS.ToASAS(presenciaProfes).AsObservableList());
    }

    private void loaddb(){
        ObservableList<Horario> horarios = DaoService.getInstance().getHorarioInstance().getall().AsObservableList();
        ObservableList<Materia> materias = DaoService.getInstance().getMateriaInstance().getall().AsObservableList();
        ObservableList<String> presencia = FXCollections.observableArrayList("Presente", "Ausente");
        presenciabox.setItems(presencia);
        presenciabox.getSelectionModel().selectFirst();
        loadMapaHorario(horarios);
        loadMapaMateria(materias);
    }
    private void loadMapaHorario(ObservableList<Horario> horarios){
        for(Horario horario : horarios){
            String clave = horario.getHorarioInicio() + "  " + horario.getHorarioFin();
            mapaHorario.addObject(clave, horario);
        }
        this.horariobox.setItems(ASAS.ToASAS(mapaHorario.getKeysList()).AsObservableList());
        this.horariobox.getSelectionModel().selectFirst();
    }

    private void loadMapaMateria(ObservableList<Materia> materias){

        for(Materia materia : materias){
            String clave = materia.getNombre();
            mapaMateria.addObject(clave, materia);
        }
        this.materiabox.setItems(ASAS.ToASAS(mapaMateria.getKeysList()).AsObservableList());
        this.materiabox.getSelectionModel().selectFirst();
    }
    private void aceptar(){
        Profesor profe = this.mapaProfe.getObject(this.Profesorbox.getSelectionModel().getSelectedItem());
        this.profesors.remove(profe);
        this.st.hide();
    }
    public Pair<AsistenciaProfe, List<Profesor>> getData() {
        System.out.println(closed);
        if(closed) {
           return null;
        }else{
            profe = new AsistenciaProfe(this.mapaProfe.getObject(this.Profesorbox.getSelectionModel().getSelectedItem()));
            profe.setPresenciaProfes(presenciaProfes);
            return new Pair<>(profe, profesors);
        }
    }
}

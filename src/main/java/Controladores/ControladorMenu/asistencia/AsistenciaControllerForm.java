package Controladores.ControladorMenu.asistencia;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.TypesAccess;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Hilos.HiloExecutor;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.AsistenciaProfe;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.PresenciaProfe;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.Asistencia.util.AsistenciaManipulator;
import Modelos.Pojos.Asistencia.util.RegistroActividades;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SQLITE.Dao.Asistencia.Facade.RegistroActividadesFachada;
import Modelos.SqliteDaoService.DaoService;
import Servicios.Utils.ASAS;
import Servicios.Utils.Utils;
import Servicios.alerta.Alerta;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AsistenciaControllerForm implements Initializable, NodeUpdator {
    @FXML
    private Label title;

    @FXML
    private Button nuevohorariocatedra;

    @FXML
    private ComboBox<String> combobachiler;

    @FXML
    private ComboBox<String> seccioncombo;

    @FXML
    private ComboBox<String> gradocombo;

    @FXML
    private DatePicker fechapicker;

    @FXML
    private Button guardar;

    @FXML
    private Button cancelar;


    @FXML
    private ListView<String> alumnosview;

    @FXML
    private Label title1;



    @FXML
    private TableView<AlumnoHorarioAsistencia> tabla_asistencia;

    @FXML
    private TableColumn<AlumnoHorarioAsistencia, String> horarioasistencia;

    @FXML
    private TableColumn<AlumnoHorarioAsistencia, String> presencia_asistencia;

    @FXML
    private Button nuevaasistencia;

    @FXML
    private Button steppaso;

    @FXML
    private ListView<String> profelistview;

    @FXML
    private TableView<PresenciaProfe> profePresencia;

    @FXML
    private TableColumn<PresenciaProfe, String> horario_profe;

    @FXML
    private TableColumn<PresenciaProfe, String> MateriaProfe;

    @FXML
    private TableColumn<PresenciaProfe, String> presencia_profe;
    @FXML
    private ProgressIndicator indicator;


    private TypesAccess typesAccess;

    private AsistenciaDiaria asistenciaDiaria;


    private AsNewMode asNewMode;
    private AsViewerMode asViewerMode;

    public AsistenciaControllerForm(TypesAccess typesAccess, AsistenciaDiaria asistenciaDiaria){
        this.typesAccess = typesAccess;
        this.asistenciaDiaria = asistenciaDiaria;
    }
    public AsistenciaControllerForm(TypesAccess typesAccess){
        this.typesAccess = typesAccess;
    }

    @Override
    public void updateNode() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.indicator.setVisible(false);
        load();
        addevents();
    }

    private void addevents(){
        if(!Objects.isNull(asNewMode)) {
            this.nuevohorariocatedra.setOnAction(t -> asNewMode.OpenProfesorPresencia());
            this.nuevaasistencia.setOnAction(t -> asNewMode.OpenAlumnoPresencia());
            this.steppaso.setOnAction(t -> asNewMode.step());
            this.guardar.setOnAction(t->asNewMode.save());
            this.cancelar.setOnAction(t->asNewMode.cancelar());
        }

    }

    private void load(){
        switch (typesAccess){
            case AS_VIEWER:
                ViewerMode();
                break;
            case AS_NEW:
                ConstructionMode();
                break;

        }
    }
    private void loadTablesPropertys(){
        //primera tabla
        this.horario_profe.setCellValueFactory(new PropertyValueFactory<>("horario"));
        this.presencia_profe.setCellValueFactory(new PropertyValueFactory<>("presencia"));
        this.MateriaProfe.setCellValueFactory(new PropertyValueFactory<>("materia"));
        // segunda tabla
        this.horarioasistencia.setCellValueFactory(new PropertyValueFactory<>("horarioAsistencia"));
        this.presencia_asistencia.setCellValueFactory(new PropertyValueFactory<>("Asistencia"));
    }
    private void loadPickerProperty(){
        List<DatePicker> pickers = new ArrayList<>();
        pickers.add(fechapicker);
        Utils.setpickerpropery(pickers);
    }

    private void ViewerMode(){
        asViewerMode = new AsViewerMode();
        asViewerMode.setTitle();
        asViewerMode.blockAll();
        asViewerMode.loadAll();

    }
    private void ConstructionMode(){
        asNewMode = new AsNewMode();
    }

    /**
     * Esta clase se encarga del funcionmiento de visor.
     */
    private class AsViewerMode{

        private final String detalles = "Asistencia | Detalles";
        AsistenciaManipulator<String, AlumnoAsistencia> AsistenciaAlumno;
        AsistenciaManipulator<String, AsistenciaProfe> asistenciaProfe;
        AsViewerMode(){
            AsistenciaControllerForm.this.loadTablesPropertys();
            AsistenciaControllerForm.this.loadPickerProperty();
            loadlistpropertys();
        }
        private void loadlistpropertys(){
            alumnosview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                    tabla_asistencia.setItems(AsistenciaAlumno.getObject(newValue).getHorarioAsistidos().AsObservableList())
            );
            profelistview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                    profePresencia.setItems(ASAS.ToASAS(asistenciaProfe.getObject(newValue).getPresenciaProfes()).AsObservableList())
            );
        }
        private void blockAll(){
            combobachiler.setDisable(false);
            combobachiler.setEditable(false);

            gradocombo.setDisable(false);
            gradocombo.setEditable(false);

            seccioncombo.setDisable(false);
            seccioncombo.setEditable(false);

            fechapicker.setDisable(true);
            fechapicker.getEditor().setStyle("-fx-text-inner-color: black ;-fx-font-size: 16px;");

            steppaso.setDisable(true);
            nuevaasistencia.setDisable(true);
            nuevohorariocatedra.setDisable(true);
            guardar.setDisable(true);
        }
        private void cancelar(){
            try {
                TabManipulator.getCurrentInstance().remove(Fxmls.ASISTENCIA_FORM);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        private void loadAll(){
            this.loadmanipulators();
            this.loadListview();
            this.loadcomboInformations();
            cancelar.setOnAction(event ->cancelar());
        }
        private void loadmanipulators(){
            RegistroActividades registroActividades = RegistroActividadesFachada.getInstance().get(asistenciaDiaria);
            this.AsistenciaAlumno = registroActividades.getAlumnoAsistenciaManipulator();
            this.asistenciaProfe = registroActividades.getProfeAsistenciaManipulator();
        }

        private void loadListview(){
            alumnosview.setItems(ASAS.ToASAS(AsistenciaAlumno.getKeysList()).AsObservableList());
            profelistview.setItems(ASAS.ToASAS(asistenciaProfe.getKeysList()).AsObservableList());
            alumnosview.getSelectionModel().selectFirst();
            profelistview.getSelectionModel().selectFirst();
        }
        private void loadcomboInformations(){
            combobachiler.getItems().add(asistenciaDiaria.getBachiller());
            combobachiler.getSelectionModel().select(0);
            gradocombo.getItems().add(asistenciaDiaria.getGrado());
            gradocombo.getSelectionModel().select(0);
            seccioncombo.getItems().add(asistenciaDiaria.getSeccion());
            seccioncombo.getSelectionModel().select(0);
            fechapicker.getEditor().setText(asistenciaDiaria.getFecha());
        }
        private void setTitle(){
            title.setText(detalles);
        }
    }
    /**
     *
     *
     */
    private class AsNewMode{
        private boolean IsprofesorDataCharged = false;
        private boolean IsAlumnoDataCharged = false;
        private boolean step = false;
        private List<Profesor> profesors;
        private List<Alumno> alumnos;
        private ProfesorAsistenciaAdderForm profesorAsistenciaAdderFormController;
        private AlumnoAsistenciaAdderForm alumnoAsistenciaAdderForm;
        private AsistenciaManipulator<String, AsistenciaProfe> AsisProfe;
        private AsistenciaManipulator<String , AlumnoAsistencia> asisalumno;
        private String detalles = "Asistencia | Nuevo";

        AsNewMode(){
            AsistenciaControllerForm.this.loadTablesPropertys();
            AsistenciaControllerForm.this.loadPickerProperty();
            this.AsisProfe = new AsistenciaManipulator<>();
            this.asisalumno = new AsistenciaManipulator<>();
            title.setText(detalles);
            bloquearSegundaSeccion(true);
            loadcombos();
            loadListProperty();
        }
        private void loadcombos(){
            ObservableList<String> bachiller = DaoService.getInstance().getCursoInstance().getAllBachiller().AsObservableList();
            ObservableList<String> seccion =  DaoService.getInstance().getCursoInstance().getAllseccion().AsObservableList();
            ObservableList<String> grado =  DaoService.getInstance().getCursoInstance().getAllGrado().AsObservableList();
            combobachiler.setItems(bachiller);
            combobachiler.getSelectionModel().selectFirst();
            seccioncombo.setItems(seccion);
            seccioncombo.getSelectionModel().selectFirst();
            gradocombo.setItems(grado);
            gradocombo.getSelectionModel().selectFirst();
        }
        private void loadListProperty(){
            profelistview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(Objects.isNull(newValue)){
                    System.out.println("new value is null");
                }else {

                    profePresencia.setItems(ASAS.ToASAS(AsisProfe.getObject(newValue).getPresenciaProfes()).AsObservableList());
                }
                    }
            );
            alumnosview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                    tabla_asistencia.setItems(asisalumno.getObject(newValue).getHorarioAsistidos().AsObservableList())
            );
        }
        private void OpenProfesorPresencia(){
            if(!IsprofesorDataCharged) {
                profesors = DaoService.getInstance().getProfesorInstance().getall().AsArrayList();
                IsprofesorDataCharged = true;
            }
            profesorAsistenciaAdderFormController = this.showandwaitprofesores(
                    this.getClass().getResource("/view/Asistencias/Asistenciaprofesoradder.fxml"),
                    new ProfesorAsistenciaAdderForm(profesors)
            );
            Pair<AsistenciaProfe, List<Profesor>> pairresult = profesorAsistenciaAdderFormController.getData();
            if(Objects.isNull(pairresult))return;
            profesors = pairresult.getValue();
            AddToMapAndUpdate(pairresult.getKey());

        }
        private Curso getCurso(){
            Curso curso = null;
            String bachiller = combobachiler.getSelectionModel().getSelectedItem();
            String seccion = seccioncombo.getSelectionModel().getSelectedItem();
            String grado = gradocombo.getSelectionModel().getSelectedItem();
            String fecha = fechapicker.getEditor().getText();
            curso = DaoService.getInstance().getCursoInstance().get(
                    DaoService
                            .getInstance()
                            .getCursoInstance()
                            .getcursoIDbybachiller(bachiller, seccion, grado)
            );
            return curso;
        }

        private Curso getCursoverified(){
            Curso curso = null;
            String bachiller = combobachiler.getSelectionModel().getSelectedItem();
            String seccion = seccioncombo.getSelectionModel().getSelectedItem();
            String grado = gradocombo.getSelectionModel().getSelectedItem();
            String fecha = fechapicker.getEditor().getText();
            if(verify(bachiller, seccion, grado, fecha)) {
                curso = DaoService.getInstance().getCursoInstance().get(
                                DaoService
                                        .getInstance()
                                        .getCursoInstance()
                                        .getcursoIDbybachiller(bachiller, seccion, grado)
                        );
            }


                return curso;
        }

        private void OpenAlumnoPresencia(){
            if(!IsAlumnoDataCharged){
                Curso curso = getCurso();
                alumnos = DaoService.getInstance().getAlumnoInstance().getAlumnospercurso(curso).AsArrayList();
                IsAlumnoDataCharged = true;
            }

            alumnoAsistenciaAdderForm = this.showandwaitAlumnos(
                    getClass().getResource("/view/Asistencias/AlumnoAsistenciaAdder.fxml"),
                    new AlumnoAsistenciaAdderForm(alumnos)
            );
            Pair<AlumnoAsistencia, List<Alumno>> pairresult = alumnoAsistenciaAdderForm.getData();
            if(Objects.isNull(pairresult))return;
            alumnos = pairresult.getValue();
            AddtoMapAndUpdateAlumno(pairresult.getKey());
        }

        private boolean exists(){
            Curso curso;
            String bachiller = combobachiler.getSelectionModel().getSelectedItem();
            String seccion = seccioncombo.getSelectionModel().getSelectedItem();
            String grado = gradocombo.getSelectionModel().getSelectedItem();
            String fecha = fechapicker.getEditor().getText();
            if(verify(bachiller, seccion, grado, fecha)) {
                curso = DaoService
                        .getInstance()
                        .getCursoInstance()
                        .get(
                                DaoService
                                        .getInstance()
                                        .getCursoInstance()
                                        .getcursoIDbybachiller(bachiller, seccion, grado)
                        );
                return DaoService.getInstance().getAsistenciaUnionInstance().exists(new AsistenciaDiaria(fecha, curso));
            }else{
                return false;
            }

        }
        private boolean verify(String b, String seccion, String grado, String fecha){
            return !b.isEmpty() && !seccion.isEmpty() && !grado.isEmpty() && !fecha.isEmpty();
        }
        private void step(){
            if(exists()){
                NotificationHelper.get().notifyDataDuplicated();
            }else{
                if(!step){
                    bloquearprimerseccion(true);
                    bloquearSegundaSeccion(false);
                    step = true;
                    steppaso.setText("<<");
                }else{
                    bloquearSegundaSeccion(true);
                    step = false;
                    steppaso.setText(">>");
                    bloquearprimerseccion(false);
                }
            }
        }
        private void  bloquearSegundaSeccion(boolean b){
            nuevaasistencia.setDisable(b);
        }

        private void bloquearprimerseccion(boolean b) {
            combobachiler.setDisable(b);
            seccioncombo.setDisable(b);
            gradocombo.setDisable(b);
            fechapicker.setDisable(b);
            nuevohorariocatedra.setDisable(b);
        }

        private  ProfesorAsistenciaAdderForm showandwaitprofesores(URL url, ProfesorAsistenciaAdderForm controller){
            try {
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(controller);
                Stage stage = new Stage();
                stage.setScene(new Scene((AnchorPane)loader.load()));
                controller.setSt(stage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(TabManipulator.getCurrentInstance().getParentStage());
                stage.setOnCloseRequest(t->controller.setClosed(true));
                stage.showAndWait();
            }catch (IOException e){
                Alerta.ShowError(e);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return controller;
        }

        private  AlumnoAsistenciaAdderForm showandwaitAlumnos(URL url, AlumnoAsistenciaAdderForm controller){
            try {
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(controller);
                Stage stage = new Stage();
                stage.setScene(new Scene((AnchorPane)loader.load()));
                controller.setSt(stage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnCloseRequest(t->controller.setClosed(true));
                stage.showAndWait();
            }catch (IOException e){
                Alerta.ShowError(e);
                e.printStackTrace();
            }
            return controller;
        }
        private void AddToMapAndUpdate(AsistenciaProfe asistenciaProfe){
            AsisProfe.addObject(llaveProfe(asistenciaProfe), asistenciaProfe);
            updatelistview();
        }
        private void AddtoMapAndUpdateAlumno(AlumnoAsistencia alumnoAsistencia){
            asisalumno.addObject(llaveAlumno(alumnoAsistencia), alumnoAsistencia);
            updatelistview();
        }
        private String llaveAlumno(AlumnoAsistencia asis){
            return asis.getApellidos() + "  "+asis.getNombres();
        }
        private String llaveProfe(AsistenciaProfe asis){
            return asis.getApellidos() + "  "+asis.getNombres();
        }

        private void updatelistview(){
            profelistview.setItems(ASAS.ToASAS(AsisProfe.getKeysList()).AsObservableList());
            alumnosview.setItems(ASAS.ToASAS(asisalumno.getKeysList()).AsObservableList());
        }

        private AsistenciaDiaria createOne(){
            Curso curso = getCurso();
            String fecha = fechapicker.getEditor().getText();
            return new AsistenciaDiaria(fecha, curso);

        }

        private void save(){
            RegistroActividades registroActividades = new RegistroActividades();
            registroActividades.setAsistenciaDiaria(this.createOne());
            registroActividades.setProfeAsistenciaManipulator(AsisProfe);
            registroActividades.setAlumnoAsistenciaManipulator(asisalumno);
            SavetoDB(registroActividades);
        }

        private void SavetoDB(RegistroActividades registroActividades){
            Task<Boolean> booleanTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                     RegistroActividadesFachada.getInstance().add(registroActividades);
                     return true;
                }
            };
            booleanTask.runningProperty().addListener((observable, oldValue, newValue) ->setEstatus(newValue));
            booleanTask.setOnSucceeded(event -> NotificationHelper.get().notifySuccessOninsertOperation(0));
            HiloExecutor.execute(booleanTask);

        }

        private void cancelar(){
            try {
                if (NotificationHelper.get().notifyOnCancelOperation().get() == ButtonType.OK) {
                    TabManipulator.getCurrentInstance().remove(Fxmls.ASISTENCIA_FORM);
                }
            }catch (IllegalAccessException e){
                Alerta.ShowError(e);
                e.printStackTrace();
            }

        }

        private void setEstatus(boolean b){
            if(b){
                indicator.setVisible(true);
            }else{
                indicator.setVisible(false);
            }
        }

    }

}

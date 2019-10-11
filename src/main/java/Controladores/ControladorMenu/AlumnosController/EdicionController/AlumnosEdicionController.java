package Controladores.ControladorMenu.AlumnosController.EdicionController;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Controladores.Enums.Sexo;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlumnosEdicionController implements Initializable, NodeUpdator {
    private Alumno alumno;

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
    private Button anadir;

    @FXML
    private Button cancelar;

    @FXML
    private Label idData;

    public AlumnosEdicionController(Alumno alumno) {
        this.alumno = alumno;
    }

    private void load() {
        if (alumno != null) {
            loadcampos();
            loadcombosexo();
            selectSexo();
            loadCurso();
            selectCursoALumno();
        }

    }
    private void loadcampos() {
        this.idData.setText("ID: "+this.alumno.getCi());
        this.nombretxt.setText(this.alumno.getNombres());
        this.apellidotxt.setText(this.alumno.getApellidos());
        this.nromadretxt.setText(this.alumno.getNromadre());
        this.nropadretxt.setText(this.alumno.getNropadre());
        this.promotxt.setText(this.alumno.getPromo());
    }

    private void loadcombosexo() {
        List<Sexo> sexos = new ArrayList<>();
        sexos.add(Sexo.Masculino);
        sexos.add(Sexo.Femenino);
        this.sexocombo.setItems(FXCollections.observableList(sexos));
    }

    private void selectSexo() {
        String sexo = this.alumno.getSexo();
        if (sexo.equalsIgnoreCase(Sexo.Masculino.name())) {
            this.sexocombo.getSelectionModel().select(Sexo.Masculino);
        } else if (sexo.equalsIgnoreCase(Sexo.Femenino.name())) {
            this.sexocombo.getSelectionModel().select(Sexo.Femenino);
        }
    }
    private void loadCurso(){
        ObservableList<String> bachiller = DaoService.getInstance().getCursoInstance().getAllBachiller().AsObservableList();
        ObservableList<String> seccion = DaoService.getInstance().getCursoInstance().getAllseccion().AsObservableList();
        ObservableList<String> grado = DaoService.getInstance().getCursoInstance().getAllGrado().AsObservableList();
        bachillercombo.setItems(bachiller);
        seccionCombo.setItems(seccion);
        gradocombo.setItems(grado);
    }

    private void selectCursoALumno(){
        Curso curso = DaoService.getInstance().getCursoInstance().getcursoperAlumno(alumno);
        if(curso == null){
            return;
        }
        bachillercombo.getSelectionModel().select(curso.getBachiller());
        seccionCombo.getSelectionModel().select(curso.getSeccion());
        gradocombo.getSelectionModel().select(curso.getGrado());

    }
    private void removethis(){
        try{
            TabManipulator.getCurrentInstance().remove(Fxmls.AlUMNOS_EDICION);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }
    private void aplicar(){
        DaoService.getInstance().getAlumnoInstance().change(getAlumnofromform());
        removethis();
    }

    private void cancelar(){
        removethis();
    }
    private void set_events(){
        this.anadir.setOnAction(t->aplicar());
        this.cancelar.setOnAction(t->cancelar());
    }

    private Alumno getAlumnofromform(){
        String dni = this.alumno.getCi();
        String nombre = this.nombretxt.getText();
        String apellido = this.apellidotxt.getText();
        String nromadre = this.nromadretxt.getText();
        String nropadre = this.nropadretxt.getText();
        String promo = this.promotxt.getText();
        String  sexo = this.sexocombo.getSelectionModel().getSelectedItem().name();
        String bachiller = this.bachillercombo.getSelectionModel().getSelectedItem();
        String seccion = this.seccionCombo.getSelectionModel().getSelectedItem();
        String grado = this.gradocombo.getSelectionModel().getSelectedItem();
        int id_curso = DaoService.getInstance().getCursoInstance().getcursoIDbybachiller(bachiller, seccion, grado);
        return new Alumno(nombre, apellido, dni, sexo, nromadre, nropadre, promo, id_curso);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         set_events();
         load();
    }
    @Override
    public void updateNode() {
       loadCurso();
    }
}

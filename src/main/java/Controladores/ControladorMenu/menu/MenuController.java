/*
 * Derechos de autor 2019 Rodolfo Elias Ojeda Almada
 *
 * Licenciado bajo la Licencia Apache, Versión 2.0 (la "Licencia");
 * no puede utilizar este archivo, excepto en cumplimiento con la Licencia.
 * Puede obtener una copia de la licencia en
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * A menos que sea requerido por la ley aplicable o acordado por escrito, software
 * distribuido bajo la Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS O CONDICIONES DE NINGÚN TIPO, ya sea expresa o implícita.
 * Consulte la Licencia para el idioma específico que rige los permisos y
 * Limitaciones bajo la Licencia.
 */
package Controladores.ControladorMenu.menu;

import Controladores.ControladorMenu.AlumnosController.AlumnosInicioController.AlumnosInicioController;
import Controladores.ControladorMenu.AlumnosController.RegistroController.AlumnosRegistroController;
import Controladores.ControladorMenu.Bienvenida.Acerca_deController;
import Controladores.ControladorMenu.Bienvenida.BienvenidaController;
import Controladores.ControladorMenu.Cursos.CursosController;
import Controladores.ControladorMenu.Horarios.HorariosController;
import Controladores.ControladorMenu.Materias.MateriasController;
import Controladores.ControladorMenu.Profesores.ProfesoresInicioController;
import Controladores.ControladorMenu.Profesores.ProfesoresRegistroController;
import Controladores.ControladorMenu.asistencia.AsistenciaControllerInicio;
import Controladores.ControladorMenu.informaciones.configurationController;
import Controladores.Enums.Fxmls;
import Controladores.tabcontrol.TabManipulator;
import Modelos.IO.IOManager;
import Modelos.Pojos.Users.User;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MenuController implements Initializable {
    private Stage st;

    private TabManipulator tabController;

    @FXML
    private MenuItem inicio_alumno;

    @FXML
    private MenuItem registro_alumno;

    @FXML
    private MenuItem inicio_profesor;

    @FXML
    private MenuItem registro_profesor;

    @FXML
    private MenuItem inicio_materia;

    @FXML
    private MenuItem inicio_horarios;

    @FXML
    private MenuItem inicio_cursos;

    @FXML
    private MenuItem asistencia_inicio;

    @FXML
    private MenuItem configuracion;

    @FXML
    private MenuItem acerca_de;

    @FXML
    private MenuItem inicio;

    @FXML
    private MenuItem importar;

    @FXML
    private MenuItem exportar;

    @FXML
    private TabPane tabmenu;

    public MenuController(Stage st) {
        this.st = st;
    }

    public void start( ) {
        st.setTitle("CA1 Assistance ");
        //st.getIcons().add(new Image("/utils/logo/iconoProyecto.png"));
        st.sizeToScene();
        st.setResizable(false);
        st.setOnCloseRequest(this::salir);
        st.show();
    }

    private void salir(WindowEvent event){
        try{
            if(TabManipulator.getCurrentInstance().IsThereTabOpened()){
                if(notifyOpenedTabs()){
                    TabManipulator.getCurrentInstance().CloseAll();
                    Platform.exit();
                }else{
                    event.consume();
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private boolean notifyOpenedTabs(){
        Optional<ButtonType> result = Alerta.CreateAlert(Alert.AlertType.CONFIRMATION, "Advertencia", "Pestañas abiertas... ", "Existen pestañas abiertas" +
                "¿Desea cerrarlas y sallir ?", st);
        return result.get() == ButtonType.OK;
    }

    private void mostrarAlumnoInicio(){
        tabController.add(Fxmls.INICIO_ALUMNOS,AlumnosInicioController.class, "Alumnos | Inicio");
    }
    private void mostrarRegistroAlumno(){
        tabController.add(Fxmls.ALUMNOS_REGISTRO, AlumnosRegistroController.class, "Alumnos | Registro ");
    }

    private void mostrarProfesorInicion(){
        tabController.add(Fxmls.PROFESOR_INICIO, ProfesoresInicioController.class, "Profesores | Inicio");
    }

    private void mostrarRegistroProfesor(){
        tabController.add(Fxmls.PROFESOR_REGISTRO, ProfesoresRegistroController.class, "Profesores | Registro");
    }
    private void mostrarInicioCursos(){
        tabController.add(Fxmls.CURSOS_INICIO, CursosController.class, "Cursos | Inicio");
    }
    private void mostrarInicioHorario(){
        tabController.add(Fxmls.HORARIO_INICIO, HorariosController.class, "Horarios | Inicio");
    }
    private void mostrarInicioMateria(){
        tabController.add(Fxmls.MATERIA_INICIO, MateriasController.class, "Materias | Inicio");
    }

    private void abrirInicio(){
        tabController.add(Fxmls.BIENVENIDA, BienvenidaController.class, "Bienvenido.");
    }
    private void abrirAcerca_de(){
        tabController.add(Fxmls.ACERCA_DE, Acerca_deController.class, "Acerca de");
    }
    private void mostrarAsistencia(){
        tabController.add(Fxmls.ASISTENCIA_INICIO, AsistenciaControllerInicio.class, "Asistencia | Inicio");
    }
    private void mostrarConfiguracion(){
        tabController.add(Fxmls.CONFIG, configurationController.class, "Configuración");
    }


    private void importar(){
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Data Base file", "*.db");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(st);
        if(Objects.isNull(file)){
            Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Error", "No archivo db seleccionado", "Debe seleccionar un archivo .db para importar.", st);
        }else{
            String name = file.getName();
            if(name.endsWith(".db")){
                IOManager manager = new IOManager();
                manager.importar(file);
            }else{
                Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Error", "No se reconoce este tipo de archivo", "Debe seleccionar un archivo .db para importar.", st);
            }
        }

    }

    private void exportar(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(st);

        if(Objects.isNull(file)){
            Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Error", "No ubicacion seleccionada", "Debe seleccionar un archivo .dbca1 para exportar.", st);
        }else{
            String name = file.getName();
            if(name.endsWith(".db")){
                IOManager manager = new IOManager();
                manager.export(file);
            }else{
                Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Error", "No se reconoce este tipo de archivo", "Debe seleccionar un archivo .db para importar.", st);
            }
        }



    }

    private void set_Events(){
        inicio_alumno.setOnAction(event -> mostrarAlumnoInicio());
        registro_alumno.setOnAction(event -> mostrarRegistroAlumno());
        inicio_profesor.setOnAction(event -> mostrarProfesorInicion());
        registro_profesor.setOnAction(event -> mostrarRegistroProfesor());
        inicio_cursos.setOnAction(event -> mostrarInicioCursos());
        inicio_horarios.setOnAction(event -> mostrarInicioHorario());
        inicio_materia.setOnAction(event -> mostrarInicioMateria());
        asistencia_inicio.setOnAction(event -> mostrarAsistencia());
        configuracion.setOnAction(event -> mostrarConfiguracion());
        acerca_de.setOnAction(event -> abrirAcerca_de());
        inicio.setOnAction(event -> abrirInicio());
        importar.setOnAction(event -> importar());
        exportar.setOnAction(event -> exportar());
        tabController = TabManipulator.getInstance(tabmenu, st);
        st.iconifiedProperty().addListener((observable, oldValue, newValue) -> minizedprocedure(newValue));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set_Events();
        loginprocedure();
    }

    private void loginprocedure(){
        try {
            if (verifyifexistsesions()) {
                disablebuttons(true);
                TabManipulator.getCurrentInstance().blockAllexcept(Fxmls.SYSTEM_LOGIN);
                openLogin();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void minizedprocedure(boolean bool){
        if(bool){
            loginprocedure();
        }
    }

    private boolean verifyifexistsesions(){
        ObservableList<User> users = DaoService.getInstance().getUserInstance().getall().AsObservableList();
        return users.size()>0;
    }

    private boolean openLogin(){
        try {
            LoginController loginController = new LoginController(this);
            TabManipulator.getCurrentInstance().add(Fxmls.SYSTEM_LOGIN, loginController, "Login | System", false);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void disablebuttons(boolean bool){
        for(MenuItem m: loadArraysButtons()){
            m.setDisable(bool);
        }
    }

     public List<MenuItem> loadArraysButtons() {
         List<MenuItem> menus = new ArrayList<>();
         menus.add(inicio_alumno);
         menus.add(registro_alumno);
         menus.add(inicio_profesor);
         menus.add(registro_profesor);
         menus.add(inicio_horarios);
         menus.add(inicio_cursos);
         menus.add(inicio_materia);
         menus.add(asistencia_inicio);
         menus.add(configuracion);
         menus.add(acerca_de);
         menus.add(inicio);
         menus.add(importar);
         menus.add(exportar);
         return menus;
     }

}






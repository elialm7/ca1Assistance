package Controladores.tabcontrol;

import Controladores.Enums.Fxmls;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class FxmlFactory implements NodeSupplier<Fxmls> {

    private URL getlocation(String resource){
        return FxmlFactory.class.getResource(resource);
    }

    public  Node getNode(Fxmls fxmls, Object controller){
        try{
            switch (fxmls){
                case INICIO_ALUMNOS:
                    return loadFxml(getlocation("/view/Alumnos/Alumnos_Inicio.fxml"), controller);
                case ALUMNOS_REGISTRO:
                    return loadFxml(getlocation("/view/Alumnos/alumnos_registro.fxml"), controller);
                case ASITENCIA:
                    return loadFxml(getlocation("/view/Asistencias/asistencia.fxml"), controller);
                case AlUMNOS_EDICION:
                    return loadFxml(getlocation("/view/Alumnos/alumnos_edicion.fxml"), controller);
                case CURSOS_INICIO:
                    return loadFxml(getlocation("/view/Cursos/Cursos_Inicio.fxml"), controller);
                case MATERIA_INICIO:
                    return loadFxml(getlocation("/view/Materias/materia_inicio.fxml"), controller);
                case HORARIO_INICIO:
                    return loadFxml(getlocation("/view/Horarios/Horarios_inicio.fxml"), controller);
                case PROFESOR_REGISTRO:
                    return loadFxml(getlocation("/view/Profesores/profesores_registro.fxml"), controller);
                case PROFESOR_INICIO:
                    return loadFxml(getlocation("/view/Profesores/profesores_inicio.fxml"), controller);
                case PROFESOR_EDICION:
                    return loadFxml(getlocation("/view/Profesores/Profesores_Edicion.fxml"), controller);
                case SYSTEM_LOGIN:
                    return loadFxml(getlocation("/view/menu/login/login.fxml"), controller);
                case CONFIG:
                    return loadFxml(getlocation("/view/informaciones/config.fxml"), controller);
                case ASISTENCIA_FORM:
                    return loadFxml(getlocation("/view/Asistencias/AsistenciaForm.fxml"), controller);
                case ASISTENCIA_INICIO:
                    return loadFxml(getlocation("/view/Asistencias/asistencia.fxml"), controller);
                case ACERCA_DE:
                    return loadFxml(getlocation("/view/bienvenida/Acerca de.fxml"), controller);
                case BIENVENIDA:
                    return loadFxml(getlocation("/view/bienvenida/bienvenida.fxml"), controller);
                default:
                    System.out.println("No hay mas casos para los fxml.");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private  <T> T loadFxml(URL location, Object Controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(location);
        loader.setController(Controller);
        return loader.load();
    }

}
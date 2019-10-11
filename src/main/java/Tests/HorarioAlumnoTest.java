package Tests;

import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.SQLITE.Dao.Asistencia.Alumno.HorarioAlumno;
import Modelos.SQLITE.Dao.Colegio.HorarioIMP;
import Modelos.SQLITE.Interfaces.IHorario;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HorarioAlumnoTest extends TestCase {

    private List<AlumnoHorarioAsistencia> gethorarios(){
       // Horario horario, String asistencia, int id_asistencia
        IHorario horario = new HorarioIMP(new sqliteConection(true));
        List<AlumnoHorarioAsistencia> horarios = new ArrayList<>();
        Horario hora = ((HorarioIMP) horario).get(1);
        for(int i = 0 ; i < 5; i++)
            horarios.add(new AlumnoHorarioAsistencia(hora, "Asistio", 1));
        return horarios;
    }

    @Test
    void add() {

        Horario horario = new HorarioIMP(new sqliteConection(true)).get(1);
        System.out.println(horario.getHorarioInicio());

    }

    @Test
    void getAllby() {
        MyConnection connection = new sqliteConection(true);
        HorarioAlumno horarioAlumno = new HorarioAlumno(connection, new HorarioIMP(connection));
        for(AlumnoHorarioAsistencia al : horarioAlumno.getAllby(1).AsObservableList()){
            System.out.println(al.toString());
        }

    }

    @Test
    void addAll() {
        MyConnection connection = new sqliteConection(true);
        HorarioAlumno horarioAlumno = new HorarioAlumno(connection, new HorarioIMP(connection));
        horarioAlumno.addAll(gethorarios());
    }
}
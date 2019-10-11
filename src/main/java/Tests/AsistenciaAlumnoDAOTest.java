package Tests;

import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.SQLITE.Dao.Alumno_Profes.AlumnoIMP;
import Modelos.SQLITE.Dao.Asistencia.Alumno.AsistenciaAlumnoDAO;
import Modelos.SQLITE.Dao.Asistencia.Alumno.HorarioAlumno;
import Modelos.SQLITE.Dao.Asistencia.Union.AsistenciaUnionIMP;
import Modelos.SQLITE.Dao.Colegio.CursoIMP;
import Modelos.SQLITE.Dao.Colegio.HorarioIMP;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

class AsistenciaAlumnoDAOTest extends TestCase {

    @Test
    void add() {




    }
    private AsistenciaAlumnoDAO getinstance(){

        MyConnection connection = new sqliteConection(true);
        HorarioAlumno horarioAlumno = new HorarioAlumno(connection, new HorarioIMP(connection));
        return new AsistenciaAlumnoDAO(new AlumnoIMP(connection), horarioAlumno, connection);
    }
    @Test
    void getAllby() {
        AsistenciaDiaria asistenciaDiaria = new AsistenciaUnionIMP(new sqliteConection(true), new CursoIMP(new sqliteConection(true))).get(1);
        AlumnoAsistencia alumnoAsistencia = getinstance().getAllby(1).AsArrayList().get(0);
        String curso  = asistenciaDiaria.getBachiller() + "-"+asistenciaDiaria.getSeccion()+"-"+asistenciaDiaria.getGrado();
        System.out.println("Fecha : "+asistenciaDiaria.getFecha() + "  Curso : "+ curso);
        System.out.println(alumnoAsistencia.toString());
        System.out.println("Horarios Asistidos: ");
        for (AlumnoHorarioAsistencia horario : alumnoAsistencia.getHorarioAsistidos().AsArrayList()){
            System.out.println(horario.toString());
        }
    }

    @Test
    void addAll() {
    }
}
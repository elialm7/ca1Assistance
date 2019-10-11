package Modelos.SQLITE.Dao.Asistencia.Facade;

import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.AsistenciaProfe;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.Asistencia.util.AsistenciaManipulator;
import Modelos.Pojos.Asistencia.util.RegistroActividades;
import Modelos.SQLITE.Dao.Alumno_Profes.AlumnoIMP;
import Modelos.SQLITE.Dao.Alumno_Profes.ProfesorIMP;
import Modelos.SQLITE.Dao.Asistencia.Alumno.AsistenciaAlumnoDAO;
import Modelos.SQLITE.Dao.Asistencia.Alumno.HorarioAlumno;
import Modelos.SQLITE.Dao.Asistencia.Profesor.AsistenciaProfesorDAO;
import Modelos.SQLITE.Dao.Asistencia.Profesor.PresenciaProfesorDAO;
import Modelos.SQLITE.Dao.Colegio.HorarioIMP;
import Modelos.SQLITE.Dao.Colegio.MateriaIMP;
import Modelos.SqliteDaoService.DaoService;

import java.util.List;
import java.util.Objects;

public class RegistroActividadesFachada {
        private AsistenciaAlumnoDAO asistenciaAlumnoDAO;
        private AsistenciaProfesorDAO asistenciaProfesorDAO;
        private static RegistroActividadesFachada instance;

        private RegistroActividadesFachada(){
            MyConnection connection = new sqliteConection(true);
            asistenciaAlumnoDAO = new AsistenciaAlumnoDAO(
                    new AlumnoIMP(connection),
                    new HorarioAlumno(connection,
                            new HorarioIMP(connection)),
                    connection
            );
            asistenciaProfesorDAO = new AsistenciaProfesorDAO(
                    new ProfesorIMP(connection),
                    connection,
                    new PresenciaProfesorDAO(
                        new HorarioIMP(connection),
                        new MateriaIMP(connection),
                        connection
                    )
            );

        }
        public synchronized static RegistroActividadesFachada getInstance(){
            if(Objects.isNull(instance)){
                instance = new RegistroActividadesFachada();
            }
            return instance;
        }


        public void add(RegistroActividades actividades){
            AsistenciaDiaria asistenciaDiaria = actividades.getAsistenciaDiaria();
            DaoService.getInstance().getAsistenciaUnionInstance().add(asistenciaDiaria);
            asistenciaDiaria.setId(DaoService.getInstance().getAsistenciaUnionInstance().getLastID());
            AsistenciaManipulator<String, AlumnoAsistencia> mapaalumnos = actividades.getAlumnoAsistenciaManipulator();
            AsistenciaManipulator<String, AsistenciaProfe> mapaprofes = actividades.getProfeAsistenciaManipulator();
            setAlumnoIDunion(mapaalumnos.getObjectList(), asistenciaDiaria);
            setProfesorIDunion(mapaprofes.getObjectList(), asistenciaDiaria);
            addTodbAlumno(mapaalumnos.getObjectList());
            addtodbProfesor(mapaprofes.getObjectList());
        }

        public RegistroActividades get(AsistenciaDiaria diaria){
            RegistroActividades registroActividades = new RegistroActividades();
            registroActividades.setAsistenciaDiaria(diaria);
            registroActividades.setAlumnoAsistenciaManipulator(getMapaAlumnos(diaria));
            registroActividades.setProfeAsistenciaManipulator(getMapaProfes(diaria));
            return registroActividades;
        }

        private void addTodbAlumno(List<AlumnoAsistencia> alumnoAsistencias){
            this.asistenciaAlumnoDAO.addAll(alumnoAsistencias);
        }
        private void addtodbProfesor(List<AsistenciaProfe> asistenciaProfes){
            this.asistenciaProfesorDAO.addAll(asistenciaProfes);
        }
        private void setAlumnoIDunion(List<AlumnoAsistencia> alumnoIDunion, AsistenciaDiaria asistenciaDiaria){
            for (AlumnoAsistencia asis : alumnoIDunion){
                asis.setId_union(asistenciaDiaria.getId());
            }
        }

        private void setProfesorIDunion(List<AsistenciaProfe> objetos, AsistenciaDiaria asistenciaDiaria){
            for(AsistenciaProfe profe : objetos){
                profe.setIdunion(asistenciaDiaria.getId());
            }
        }

        //get
        private AsistenciaManipulator<String, AlumnoAsistencia> getMapaAlumnos(AsistenciaDiaria asistenciaDiaria){
            List<AlumnoAsistencia> alumnoAsistenciaList = this.asistenciaAlumnoDAO.getAllby(asistenciaDiaria.getId()).AsArrayList();
            AsistenciaManipulator<String, AlumnoAsistencia> mapa = new AsistenciaManipulator<>();
            for(AlumnoAsistencia al: alumnoAsistenciaList) {
                mapa.addObject(llaveAlumnoAsistencia(al), al);
            }
            return mapa;
        }
        private AsistenciaManipulator<String, AsistenciaProfe> getMapaProfes(AsistenciaDiaria diaria){
            List<AsistenciaProfe> asistenciaProfes = this.asistenciaProfesorDAO.getAllby(diaria.getId()).AsArrayList();
            AsistenciaManipulator<String, AsistenciaProfe> mapa = new AsistenciaManipulator<>();
            for(AsistenciaProfe asis : asistenciaProfes){
                mapa.addObject(llaveProfesorAsistencia(asis), asis);
            }
            return mapa;
        }

        private String llaveAlumnoAsistencia(AlumnoAsistencia alumnoAsistencia){
            return alumnoAsistencia.getApellidos() + "  " + alumnoAsistencia.getNombres();
        }
        private String llaveProfesorAsistencia(AsistenciaProfe asistenciaProfe){
            return asistenciaProfe.getApellidos() + "  "+asistenciaProfe.getNombres();
        }

}

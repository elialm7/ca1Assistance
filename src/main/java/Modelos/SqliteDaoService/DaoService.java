package Modelos.SqliteDaoService;

import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Conexion.MyConnection;
import Modelos.SQLITE.Dao.Alumno_Profes.AlumnoIMP;
import Modelos.SQLITE.Dao.Alumno_Profes.ProfesorIMP;
import Modelos.SQLITE.Dao.Alumno_Profes.UserIMP;
import Modelos.SQLITE.Dao.Asistencia.Union.AsistenciaUnionIMP;
import Modelos.SQLITE.Dao.Colegio.CursoIMP;
import Modelos.SQLITE.Dao.Colegio.HorarioIMP;
import Modelos.SQLITE.Dao.Colegio.MateriaIMP;
import Modelos.SQLITE.Interfaces.*;

/**
    Esta clase devuelve la instancia de los patrones ya con la conexion cargada.
 */
public class DaoService {

    private MyConnection connection;
    private IAlumno AlumnoInstance;
    private ICurso CursoInstance;
    private IHorario HorarioInstance;
    private IMateria MateriaInstance;
    private IProfesor ProfesorInstance;
    private IUser UserInstance;
    private IAsistenciaUnion AsistenciaUnionInstance;
    private static DaoService INSTANCE;

    private DaoService() {
        this.connection = new sqliteConection(true);
        loadInstances();
    }

    private void loadInstances(){
        AlumnoInstance = new AlumnoIMP(connection);
        CursoInstance = new CursoIMP(connection);
        HorarioInstance = new HorarioIMP(connection);
        MateriaInstance = new MateriaIMP(connection);
        ProfesorInstance = new ProfesorIMP(connection);
        UserInstance = new UserIMP(connection);
        AsistenciaUnionInstance = new AsistenciaUnionIMP(connection, CursoInstance);
    }

    public synchronized static DaoService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DaoService();
        }
        return INSTANCE;
    }

    public IAlumno getAlumnoInstance() {
        return AlumnoInstance;
    }

    public ICurso getCursoInstance() {
        return CursoInstance;
    }

    public IHorario getHorarioInstance() {
        return HorarioInstance;
    }

    public IMateria getMateriaInstance() {
        return MateriaInstance;
    }

    public IProfesor getProfesorInstance() {
        return ProfesorInstance;
    }

    public IUser getUserInstance() {
        return UserInstance;
    }

    public IAsistenciaUnion getAsistenciaUnionInstance() {
        return AsistenciaUnionInstance;
    }
}

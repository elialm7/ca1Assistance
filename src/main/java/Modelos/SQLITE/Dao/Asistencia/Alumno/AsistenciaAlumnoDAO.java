package Modelos.SQLITE.Dao.Asistencia.Alumno;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.SQLITE.Dao.Asistencia.OwnInterface.IAsistencia;
import Modelos.SQLITE.Interfaces.IAlumno;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsistenciaAlumnoDAO extends IAsistencia<AlumnoAsistencia> {
    private IAlumno alumno;
    private HorarioAlumno horarioAlumno;
    private MyConnection connection;


    public AsistenciaAlumnoDAO(IAlumno alumno, HorarioAlumno horarioAlumno, MyConnection connection) {
        this.alumno = alumno;
        this.horarioAlumno = horarioAlumno;
        this.connection = connection;
    }

    private ASAS<AlumnoAsistencia> load(ResultSet rs) throws SQLException {
        List<AlumnoAsistencia> alumnos = new ArrayList<>();
        AlumnoAsistencia alumnoAsistencia = null;
        while (rs.next()) {
            Alumno CurrentAlumno = getalumno(rs.getString("id_alumno"));
            alumnoAsistencia = new AlumnoAsistencia(
                    CurrentAlumno,
                    rs.getInt("id_asistencia"),
                    rs.getInt("idunion")
            );
            alumnos.add(alumnoAsistencia);
        }
        for (AlumnoAsistencia alumno : alumnos) {
            alumno.setHorarioAsistidos(this.getHorariosforAlumno(alumno.getId_asistencia()));
        }
        return new ASAS<>(alumnos);
    }

    private List<AlumnoHorarioAsistencia> getHorariosforAlumno(int id){
        return horarioAlumno.getAllby(id).AsArrayList();
    }

    private Alumno getalumno(String id){
        return alumno.get(id);
    }
    private void  setLastID(List<AlumnoHorarioAsistencia> asistencias){
        int lastid = getlastID();
        for(AlumnoHorarioAsistencia horario : asistencias){
            horario.setId_asistencia(lastid);
        }
    }
    @Override
    public void add(AlumnoAsistencia object) {
        try(Connection conn = this.connection.conect()){
            String sql = "insert into asistenciaAlumno(id_alumno, idunion) values (?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, object.getCi());
            preparedStatement.setInt(2, object.getId_union());
            preparedStatement.executeUpdate();
            setLastID(object.getHorarioAsistidos().AsArrayList());
            horarioAlumno.addAll(object.getHorarioAsistidos().AsArrayList());
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }

    }

    @Override
    public ASAS<AlumnoAsistencia> getAllby(int id) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaAlumno where idunion = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return load(preparedStatement.executeQuery());
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addAll(List<AlumnoAsistencia> objects) {
        if(!Objects.isNull(objects)){
            for(AlumnoAsistencia alumno : objects){
                this.add(alumno);
            }
        }else{
            throw new NullPointerException("No se puede agregar un objeto nulo. ");
        }
    }
    @Override
    public int getlastID() {
        int ID = 0;
        try(Connection conn = this.connection.conect()){
            String sql = "select max(id_asistencia) as highestid from asistenciaAlumno";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while(rs.next()){
                ID = rs.getInt("highestid");
            }
            rs.close();
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }
        return ID;
    }
}






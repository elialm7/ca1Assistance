package Modelos.SQLITE.Dao.Asistencia.Alumno;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoHorarioAsistencia;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.SQLITE.Dao.Asistencia.OwnInterface.IAsistencia;
import Modelos.SQLITE.Interfaces.IHorario;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HorarioAlumno extends IAsistencia<AlumnoHorarioAsistencia> {
    private MyConnection connection;
    private IHorario horario;
    public HorarioAlumno(MyConnection connection, IHorario iHorario){
        this.connection = connection;
        this.horario = iHorario;
    }

    private ASAS<AlumnoHorarioAsistencia> load(ResultSet rs) throws SQLException{
        List<AlumnoHorarioAsistencia> horariosasistdos = new ArrayList<>();
        AlumnoHorarioAsistencia alumnoHorarioAsistencia = null;
        while(rs.next()){
            Horario currentHorario = getCurrentHorario(rs.getInt("idhorario"));
            alumnoHorarioAsistencia = new AlumnoHorarioAsistencia(
                    rs.getInt("idpresencia"),
                    currentHorario,
                    rs.getString("presencia"),
                    rs.getInt("id_asistencia")
            );

            horariosasistdos.add(alumnoHorarioAsistencia);
        }
        return new ASAS<>(horariosasistdos);
    }

    private Horario getCurrentHorario(int id){
        return horario.get(id);
    }

    @Override
    public void add(AlumnoHorarioAsistencia object) {
        try(Connection conn = this.connection.conect()){
            String sql = "insert into presenciaAlumno(idhorario, id_asistencia, presencia) values (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, object.getId_horario());
            preparedStatement.setInt(2, object.getId_asistencia());
            preparedStatement.setString(3, object.getAsistencia());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }
    }

    @Override
    public ASAS<AlumnoHorarioAsistencia> getAllby(int id) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from presenciaAlumno where id_asistencia = ? ;";
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
    public void addAll(List<AlumnoHorarioAsistencia> objects) {
        if(Objects.isNull(objects)){
            throw  new NullPointerException("No se pude insertar un objeto nulo");
        }else{
            for(AlumnoHorarioAsistencia obj : objects){
                this.add(obj);
            }
        }
    }
}

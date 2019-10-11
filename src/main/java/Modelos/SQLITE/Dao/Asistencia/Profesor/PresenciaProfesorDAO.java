package Modelos.SQLITE.Dao.Asistencia.Profesor;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.PresenciaProfe;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.Pojos.ColegioEtc.Materia;
import Modelos.SQLITE.Dao.Asistencia.OwnInterface.IAsistencia;
import Modelos.SQLITE.Interfaces.IHorario;
import Modelos.SQLITE.Interfaces.IMateria;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PresenciaProfesorDAO extends IAsistencia<PresenciaProfe> {

    private IHorario horario;
    private IMateria materia;
    private MyConnection connection;


    public PresenciaProfesorDAO(IHorario horario, IMateria materia, MyConnection connection) {
        this.horario = horario;
        this.materia = materia;
        this.connection = connection;
    }

    private ASAS<PresenciaProfe> load(ResultSet resultSet)throws SQLException {
        List<PresenciaProfe> asistencia = new ArrayList<>();
        PresenciaProfe presenciaProfe = null;
        while(resultSet.next()){
            Horario currentHorario = getCurrentHorario(resultSet.getInt("idhorario"));
            Materia currentMateria = getCurrentMateria(resultSet.getInt("idmateria"));
            presenciaProfe = new PresenciaProfe(currentHorario,
                    currentMateria,
                    resultSet.getString("presencia"),
                    resultSet.getInt("id_presencia"),
                    resultSet.getInt("id_asistencia")
            );

            asistencia.add(presenciaProfe);
        }

        return new ASAS<>(asistencia);
    }

    private Horario getCurrentHorario(int id){
        return horario.get(id);
    }
    private Materia getCurrentMateria(int id){
       return materia.get(id);
    }

    @Override
    public void add(PresenciaProfe object) {
        try(Connection conn = this.connection.conect()){
            String sql = "insert into presenciaprofesor(idhorario, idmateria, id_asistencia, presencia) VALUES (?,?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, object.getId_horario());
            preparedStatement.setInt(2, object.getId_materia());
            preparedStatement.setInt(3, object.getId_asistencia());
            preparedStatement.setString(4, object.getPresencia());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }

    }

    @Override
    public ASAS<PresenciaProfe> getAllby(int id) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from presenciaprofesor where id_asistencia = ?;";
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
    public void addAll(List<PresenciaProfe> objects) {
        if(!Objects.isNull(objects)){
            for(PresenciaProfe presencia : objects){
                this.add(presencia);
            }
        }else{
            throw  new NullPointerException("No se puede añadir un objeto nulo.");
        }

    }
}

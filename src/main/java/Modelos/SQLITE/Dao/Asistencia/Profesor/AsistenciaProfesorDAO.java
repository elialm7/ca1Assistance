package Modelos.SQLITE.Dao.Asistencia.Profesor;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.AsistenciaProfe;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.PresenciaProfe;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SQLITE.Dao.Asistencia.OwnInterface.IAsistencia;
import Modelos.SQLITE.Interfaces.IProfesor;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsistenciaProfesorDAO extends IAsistencia<AsistenciaProfe> {
    private IProfesor profesor;
    private MyConnection connection;
    private PresenciaProfesorDAO presenciaProfesorDAO;

    public AsistenciaProfesorDAO(IProfesor profesor, MyConnection connection, PresenciaProfesorDAO presenciaProfesorDAO) {
        this.profesor = profesor;
        this.connection = connection;
        this.presenciaProfesorDAO = presenciaProfesorDAO;
    }

    private ASAS<AsistenciaProfe> load(ResultSet rs) throws SQLException {

        List<AsistenciaProfe> profes = new ArrayList<>();
        AsistenciaProfe asistencia = null;
        while(rs.next()){
            Profesor CurrentProfesor = getCurrentProfesor(rs.getString("id_profesor"));
            asistencia = new AsistenciaProfe(CurrentProfesor
                    , rs.getInt("idunion"),
                    rs.getInt("id_asistencia")
            );
            profes.add(asistencia);
        }
        for(AsistenciaProfe asis : profes){
            asis.setPresenciaProfes(getPresenciaprofes(asis.getOwnid()));
        }

        return new ASAS<>(profes);
    }
    private List<PresenciaProfe> getPresenciaprofes(int id){
        return presenciaProfesorDAO.getAllby(id).AsArrayList();
    }

    private Profesor getCurrentProfesor(String id){
        return profesor.get(id);
    }

    private void setlastid(List<PresenciaProfe> lista){
        int id = getlastID();
        for(PresenciaProfe asis : lista){
            asis.setId_asistencia(id);
        }
    }
    @Override
    public void add(AsistenciaProfe object) {

        try(Connection conn = this.connection.conect()){
            String sql = "insert into asistenciaprofe(id_profesor, idunion) VALUES (?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, object.getCi());
            preparedStatement.setInt(2, object.getIdunion());
            preparedStatement.executeUpdate();
            setlastid(object.getPresenciaProfes());
            presenciaProfesorDAO.addAll(object.getPresenciaProfes());
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }

    }

    @Override
    public ASAS<AsistenciaProfe> getAllby(int id) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaprofe where idunion = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            return load(preparedStatement.executeQuery());
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addAll(List<AsistenciaProfe> objects) {

        if(!Objects.isNull(objects)){
            for(AsistenciaProfe asis : objects){
                this.add(asis);
            }
        }else {
            throw new NullPointerException("No se pude añadir un objeto nulo.");
        }

    }

    @Override
    public int getlastID() {
        int id = 0;
        try(Connection conn = this.connection.conect()){
            String sql = "select max(id_asistencia) as highestid from asistenciaprofe;";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while(rs.next()){
                id = rs.getInt("highestid");
            }
            rs.close();
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }

        return id;
    }
}

package Modelos.SQLITE.Dao.Alumno_Profes;

import Controladores.Enums.BuscarType;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Profesor.Profesor;
import Modelos.SQLITE.Interfaces.IProfesor;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ProfesorIMP implements IProfesor {
    private MyConnection connection;

    public ProfesorIMP(MyConnection connection) {
        this.connection = connection;
    }

    private ASAS<Profesor> readM(ResultSet rs) throws SQLException {

        List<Profesor> profesorList = new ArrayList<>();
        Profesor pr;
        while(rs.next()){
            pr = new Profesor(rs.getString("nombres"), rs.getString("apellidos"), rs.getString("dniprofesor"), rs.getString("sexo"),rs.getString("NroContacto"));
            profesorList.add(pr);
        }
        rs.close();
        return new ASAS<>(profesorList);

    }

    @Override
    public void add(Profesor toadd) {
        try(Connection conn = this.connection.conect()){
            String sql = "insert into profesores(dniprofesor, nombres, apellidos, sexo, NroContacto) values (?,?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, toadd.getCi());
            preparedStatement.setString(2, toadd.getNombres());
            preparedStatement.setString(3, toadd.getApellidos());
            preparedStatement.setString(4, toadd.getSexo());
            preparedStatement.setString(5, toadd.getNroprofesor());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public Profesor get(String toget) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from profesores where dniprofesor = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,toget);
            return readM(preparedStatement.executeQuery()).AsObservableList().get(0);
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public void delete(String todelete) {
        try(Connection conn = this.connection.conect()){
            String sql = "delete from profesores where dniprofesor = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, todelete);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public void change(Profesor tochange) {
        try(Connection conn = this.connection.conect()){
            String sql = "update profesores set nombres = ?, apellidos = ?, sexo = ?, NroContacto = ? where dniprofesor = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tochange.getNombres());
            preparedStatement.setString(2, tochange.getApellidos());
            preparedStatement.setString(3, tochange.getSexo());
            preparedStatement.setString(4, tochange.getNroprofesor());
            preparedStatement.setString(5, tochange.getCi());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public ASAS<Profesor> getall() {
        try(Connection conn = this.connection.conect()){
            String sql  = "select * from profesores";
            return readM(conn.prepareStatement(sql).executeQuery());
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public ASAS<Profesor> findby(String busqueda, BuscarType buscarType) {
        // try-with-resources...
        try(Connection conn = this.connection.conect()){
            String sql = getSql(busqueda, buscarType);
            if(busqueda.isEmpty()){
                return readM(conn.prepareStatement(sql).executeQuery());
            }else{
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,"%"+busqueda+"%");
                return readM(preparedStatement.executeQuery());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }
    private String getSql(String buscar, BuscarType buscarType){
        String sql = "";
        if(buscar.isEmpty()){
            sql = "select * from profesores;";
        }else {
            switch (buscarType) {
                case ID:
                    sql = "select * from profesores where dniprofesor like ?;";
                    break;
                case Nombre:
                    sql = "select * from profesores where nobres like ?;";
                    break;
                case Apellido:
                    sql = "select * from profesores where apellidos like ?;";
                    break;
            }
        }
        return sql;
    }
}

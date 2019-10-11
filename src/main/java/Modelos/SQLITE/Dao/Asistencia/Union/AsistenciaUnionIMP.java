package Modelos.SQLITE.Dao.Asistencia.Union;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SQLITE.Interfaces.IAsistenciaUnion;
import Modelos.SQLITE.Interfaces.ICurso;
import Servicios.Utils.ASAS;
import Servicios.alerta.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsistenciaUnionIMP implements IAsistenciaUnion {
    private MyConnection connection ;
    private ICurso curso;
    public AsistenciaUnionIMP(MyConnection connection, ICurso curso) {
        this.connection = connection;
        this.curso = curso;
    }

    private ASAS<AsistenciaDiaria> load(ResultSet rs) throws SQLException {
        AsistenciaDiaria asistenciaDiaria = null;
        List<AsistenciaDiaria> list = new ArrayList<>();
        while(rs.next()){
            Curso l = curso.get(rs.getInt("idcursofk"));
            if(Objects.isNull(l))
                l = new Curso("", "", "");
            asistenciaDiaria = new AsistenciaDiaria(rs.getString("fecha"), l.getBachiller(), l.getSeccion(), l.getGrado(), rs.getInt("idunion"));
            asistenciaDiaria.setCursofk(rs.getInt("idcursofk"));
            list.add(asistenciaDiaria);
        }
        return new ASAS<>(list);
    }
    @Override
    public void add(AsistenciaDiaria toadd) {
        try(Connection conn = this.connection.conect()){
            String sql = "insert into main.asistenciaunion(fecha,idcursofk) values (?,?);";
            toadd.setCursofk(getMateriaFk(toadd));
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, toadd.getFecha());
            preparedStatement.setInt(2, toadd.getCursofk());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public AsistenciaDiaria get(Integer toget) {

        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaunion where idunion = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, toget);
            return load(preparedStatement.executeQuery()).AsArrayList().get(0);
        }catch (SQLException e) {
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public void delete(Integer todelete) {
        try(Connection conn = this.connection.conect()){
            String sql = "delete from asistenciaunion where idunion = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, todelete);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public void change(AsistenciaDiaria tochange) {
        throw new UnsupportedOperationException("Esta operacion no esta soportada para este DAO.");
    }

    @Override
    public ASAS<AsistenciaDiaria> getall() {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaunion limit ?  ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, ROWLIMIT);
            return load(preparedStatement.executeQuery());
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public int getLastID() {

        try(Connection conn = this.connection.conect()){
            String sql = "select max(idunion) as higherID from asistenciaunion;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs;
            if((rs = preparedStatement.executeQuery()).next()){
                return rs.getInt("higherID");
            }
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return -1;
    }

    @Override
    public ASAS<AsistenciaDiaria> getby(String date) {
        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaunion where fecha = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, date);
            return load(preparedStatement.executeQuery());
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public ASAS<AsistenciaDiaria> getBetween(String date1, String date2, Curso curso) {
        if(Objects.isNull(curso))return null;
        String sql = "select * from asistenciaunion left join cursos on asistenciaunion.idcursofk = cursos.id_curso where fecha between ? and ? ";
        String sql2 = "and cursos.bachiller = ? and cursos.grado = ? and cursos.seccion = ?";
        StringBuilder builder = new StringBuilder();
        builder.append(sql);
        builder.append(sql2);
        try(Connection conn = this.connection.conect()){
            PreparedStatement preparedStatement = conn.prepareStatement(builder.toString());
            preparedStatement.setString(1, date1);
            preparedStatement.setString(2, date2);
            preparedStatement.setString(3, curso.getBachiller());
            preparedStatement.setString(4, curso.getGrado());
            preparedStatement.setString(5, curso.getSeccion());
            return load(preparedStatement.executeQuery());
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    @Override
    public boolean exists(AsistenciaDiaria asistenciaDiaria) {
        boolean exist = false;
        try(Connection conn = this.connection.conect()){
            String sql = "select * from asistenciaunion where idcursofk = ? and fecha = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, asistenciaDiaria.getCursofk());
            preparedStatement.setString(2, asistenciaDiaria.getFecha());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                exist = true;
            }
        }catch (SQLException e){
            Alerta.ShowError(e);
            e.printStackTrace();
        }
        return exist;
    }


    private int getMateriaFk(AsistenciaDiaria diaria){
        int id;

        if(diaria.getCursofk() != 0){
            id = diaria.getCursofk();
        } else {
            int CI = curso.getcursoIDbybachiller(diaria.getBachiller(), diaria.getSeccion(), diaria.getGrado());
            if(CI!=-1)
               id = CI;
            else
                id = 0;
        }

        return id;
    }
}

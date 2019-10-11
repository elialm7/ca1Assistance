package Modelos.SQLITE.Dao.Colegio;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.SQLITE.Interfaces.IHorario;
import Servicios.Utils.ASAS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HorarioIMP implements IHorario {

    private MyConnection connection;

    public HorarioIMP(MyConnection connection) {
        this.connection = connection;
    }

    private List<Horario> read(ResultSet rs) throws SQLException {
        Horario horario = null;
        List<Horario> horarios = new ArrayList<>();
        while(rs.next()){
            horario = new Horario(rs.getInt("id_horario"), rs.getString("horario_inicio"), rs.getString("horario_fin"));
            horarios.add(horario);
        }
        rs.close();
        return horarios;
    }


    @Override
    public void add(Horario toadd) {

        Connection conn = null;

        try{
            conn = connection.conect();
            String sql = "insert into horarios(horario_inicio, horario_fin) values (?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, toadd.getHorarioInicio());
            preparedStatement.setString(2, toadd.getHorarioFin());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Horario get(Integer toget) {

        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select * from horarios where id_horario = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, toget);
            List<Horario> horarios = read(preparedStatement.executeQuery());
            if(verify(horarios)!=null){
                return horarios.get(0);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void delete(Integer todelete) {

        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "delete from horarios where id_horario = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, todelete);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void change(Horario tochange) {


        Connection conn = null;

        try {
            conn = connection.conect();
            String sql = "update horarios set horario_inicio = ?, horario_fin = ? where id_horario = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tochange.getHorarioInicio());
            preparedStatement.setString(2,tochange.getHorarioFin());
            preparedStatement.setInt(3, tochange.getIdhorario());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }


    @Override
    public ASAS<Horario> getall() {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select * from horarios;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            List<Horario> horarios = read(preparedStatement.executeQuery());
            return new ASAS<>(horarios);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null)
                    conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}

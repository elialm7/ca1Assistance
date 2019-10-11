package Modelos.SQLITE.Dao.Colegio;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.Pojos.ColegioEtc.Materia;
import Modelos.SQLITE.Interfaces.IMateria;
import Servicios.Utils.ASAS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaIMP implements IMateria {

    private MyConnection connection;

    public MateriaIMP(MyConnection connection) {
        this.connection = connection;
    }

    private List<Materia> read(ResultSet resultSet) throws SQLException {
        Materia materia = null;
        List<Materia> materias = new ArrayList<>();
        while(resultSet.next()){
            materia = new Materia(resultSet.getInt("id_materia"), resultSet.getString("nombre"));
            materias.add(materia);
        }
        resultSet.close();
        return materias;
    }

    @Override
    public void add(Materia toadd) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "insert into materias(nombre) values (?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1 ,toadd.getNombre());
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
    public Materia get(Integer toget) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select * from materias where id_materia = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, toget);
            List<Materia> materias = read(preparedStatement.executeQuery());
            if(verify(materias)!=null)
                return materias.get(0);
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

            String sql = "delete from materias where id_materia = ?;";
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
    public void change(Materia tochange) {

        Connection con = null;

        try{
            con = connection.conect();
            String sql = "update materias set nombre = ? where id_materia = ?;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,tochange.getNombre());
            preparedStatement.setInt(2, tochange.getId_materia());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(con!=null)
                    con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }


    }

    @Override
    public ASAS<Materia> getall() {
        Connection con = null;
        try{
            con = connection.conect();
            String sql = "select * from materias;";
            Statement st = con.createStatement();
            List<Materia> materias = read(st.executeQuery(sql));
            return new ASAS<>(materias);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(con!=null)
                    con.close();
            }catch (SQLException e){
                e.printStackTrace();

            }
        }
        return null;
    }

    @Override
    public void add(Materia m, Horario horario) {
        /*m.setHorario_id(horario.getIdhorario());
        add(m);*/
    }

    @Override
    public int getID(String nombre) {
        List<Materia> materia = this.getall().AsArrayList();
        for(Materia m : materia){
            if(m.getNombre().equals(nombre)){
                return m.getId_materia();
            }
        }

        return -1;
    }
}

package Modelos.SQLITE.Dao.Colegio;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SQLITE.Interfaces.ICurso;
import Servicios.Utils.ASAS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoIMP implements ICurso {

    private MyConnection connection;

    public CursoIMP(MyConnection connection) {
        this.connection = connection;
    }


    private List<Curso> read(ResultSet rs) throws SQLException {
        Curso curso = null;
        List<Curso> cursos = new ArrayList<>();
        while(rs.next()){
            curso = new Curso(rs.getInt("id_curso"),rs.getString("bachiller"), rs.getString("seccion"), rs.getString("grado"));
            cursos.add(curso);
        }
        rs.close();
        return cursos;
    }

    @Override
    public void add(Curso toadd) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "insert into cursos(bachiller, seccion, grado) values(?, ?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, toadd.getBachiller());
            preparedStatement.setString(2, toadd.getSeccion());
            preparedStatement.setString(3, toadd.getGrado());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Curso get(Integer toget) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select * from cursos where id_curso = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, toget);
            List<Curso> cursos =  read(preparedStatement.executeQuery());
            if(verify(cursos)!=null)
                return cursos.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null){
                    conn.close();
                }
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
            String sql = "delete from cursos where id_curso = ?;";
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
    public void change(Curso tochange) {

        Connection conn = null;

        try{
            conn = connection.conect();
            String sql = "update cursos set bachiller = ?, seccion = ?, grado = ? where id_curso = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tochange.getBachiller());
            preparedStatement.setString(2,tochange.getSeccion());
            preparedStatement.setString(3, tochange.getGrado());
            preparedStatement.setInt(4, tochange.getIdcurso());
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
    public ASAS<Curso> getall() {
        Connection conn = null;

        try{
            conn = connection.conect();
            String sql = "select id_curso, bachiller, seccion, grado from cursos;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return new ASAS<>(read(rs));
        }catch (SQLException e ){
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
    public Curso getcursoperAlumno(Alumno alumno) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select id_curso, bachiller, seccion, grado from cursos left join alumnos on cursos.id_curso = alumnos.idcurso where dniAlumno = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, alumno.getCi());
            List<Curso> curso = read(preparedStatement.executeQuery());
            if(verify(curso)!=null)
                return curso.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
         return  null;
    }

    @Override
    public int getcursoIDbybachiller(String bachiller, String seccion, String grado) {
        List<Curso> cursos = getall().AsArrayList();
        for (Curso curso : cursos) {
            if (curso.getBachiller().equalsIgnoreCase(bachiller) && curso.getSeccion().equalsIgnoreCase(seccion) && curso.getGrado().equalsIgnoreCase(grado))
                return curso.getIdcurso();
        }
        return -1;
    }

    @Override
    public ASAS<String> getAllseccion() {
        Connection conn = null;
        List<String> ss = new ArrayList<>();
        try{
            conn = connection.conect();
            String sql = "select distinct seccion from cursos;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                ss.add(rs.getString("seccion"));
            }
            rs.close();
            return new ASAS<>(ss);
        }catch (SQLException E){
            E.printStackTrace();
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public ASAS<String> getAllBachiller() {

        Connection conn = null;
        List<String> ss = new ArrayList<>();
        try{
            conn = connection.conect();
            String sql = "select distinct bachiller from cursos;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ss.add(rs.getString("bachiller"));
            }
            rs.close();
            return new ASAS<String>(ss);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    @Override
    public ASAS<String> getAllGrado() {

        Connection conn = null;
        List<String> ss = new ArrayList<>();
        try{
            conn = connection.conect();
            String sql = "select distinct grado from cursos;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ss.add(rs.getString("grado"));
            }
            rs.close();
            return new ASAS<String>(ss);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

package Modelos.SQLITE.Dao.Alumno_Profes;

import Controladores.Enums.BuscarType;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
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

public class AlumnoIMP implements IAlumno {
    private MyConnection connection;
    public AlumnoIMP(MyConnection connection){
        this.connection = connection;
    }
    private List<Alumno> read(ResultSet resultSet) throws SQLException {
        Alumno alumno = null;
        List<Alumno> lista = new ArrayList<>();
        while(resultSet.next()){
            alumno = new Alumno();
            alumno.setCi(resultSet.getString("dniAlumno"));
            alumno.setNombres(resultSet.getString("nombres"));
            alumno.setApellidos(resultSet.getString("apellidos"));
            alumno.setSexo(resultSet.getString("sexo"));
            alumno.setNromadre(resultSet.getString("NroMadre"));
            alumno.setNropadre(resultSet.getString("NroPadre"));
            alumno.setPromo(resultSet.getString("promo"));
            alumno.setIdcurso(resultSet.getInt("idcurso"));
            lista.add(alumno);
        }
        return lista;
    }
    @Override
    public void add(Alumno toadd) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "insert into alumnos(dniAlumno, nombres, apellidos, sexo, NroMadre, NroPadre, promo, idcurso) values (?,?,?, ?, ?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, toadd.getCi());
            preparedStatement.setString(2, toadd.getNombres());
            preparedStatement.setString(3, toadd.getApellidos());
            preparedStatement.setString(4, toadd.getSexo());
            preparedStatement.setString(5, toadd.getNromadre());
            preparedStatement.setString(6, toadd.getNropadre());
            preparedStatement.setString(7, toadd.getPromo());
            preparedStatement.setInt(8, toadd.getIdcurso());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public Alumno get(String toget) {
            Connection conn = null;
            Alumno alumno = null;
            try{
                conn = connection.conect();
                String sql = "select * from alumnos where dniAlumno = ?;";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, toget);
                ResultSet rs = preparedStatement.executeQuery();
                List<Alumno> alumnos = read(rs);
                if(verify(alumnos)!=null){
                    alumno = alumnos.get(0);
                }
                return alumno;
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                if(conn!=null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        return alumno;
    }
    @Override
    public void delete(String todelete) {
        Connection connection = null;
        try{
            connection = this.connection.conect();
            String sql = "delete from alumnos where dniAlumno = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, todelete);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    public void change(Alumno tochange) {

        Connection connection = null;

        try{
            connection = this.connection.conect();
            String sql = "update alumnos set nombres = ?, apellidos = ?, sexo = ?, NroMadre = ?, NroPadre = ?, promo = ?, idcurso = ? where dniAlumno = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tochange.getNombres());
            preparedStatement.setString(2, tochange.getApellidos());
            preparedStatement.setString(3, tochange.getSexo());
            preparedStatement.setString(4, tochange.getNromadre());
            preparedStatement.setString(5, tochange.getNropadre());
            preparedStatement.setString(6, tochange.getPromo());
            preparedStatement.setInt(7, tochange.getIdcurso());
            preparedStatement.setString(8, tochange.getCi());
            System.out.println(tochange.getCi());
            System.out.println(preparedStatement.executeUpdate());
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public ASAS<Alumno> getall() {

        Connection connection = null;
        try{
            connection = this.connection.conect();
            String sql = "select * from alumnos;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            return new ASAS<>(read(rs));
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    @Override
    public void add(Alumno alumno, Curso curso) {
        alumno.setIdcurso(curso.getIdcurso());
        this.add(alumno);
    }
    @Override
    public void deleteall(String promo) {
        Connection connection = null;
        try{
            connection = this.connection.conect();
            String sql = "delete from alumnos where promo = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, promo);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public ASAS<Alumno>findby(String by, Curso curso, BuscarType buscarType, boolean toallfilter) {

            try(Connection connection = this.connection.conect()){
                PreparedStatement preparedStatement;
                String sql;
                if(by.isEmpty()){
                    sql = "select * from alumnos;";
                    preparedStatement = connection.prepareStatement(sql);
                    return new ASAS<>(read(preparedStatement.executeQuery()));
                }else {
                    if (toallfilter) {
                        sql = getcompletesqlfilter(buscarType);
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, curso.getBachiller());
                        preparedStatement.setString(2, curso.getSeccion());
                        preparedStatement.setString(3, curso.getGrado());
                        preparedStatement.setString(4, "%" + by + "%");
                        ResultSet resultSet = preparedStatement.executeQuery();
                        return new ASAS<>(read(resultSet));
                    } else {
                        sql = getsimplesqlfilter(buscarType);
                        preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, "%"+by+"%");
                        return new ASAS<>(read(preparedStatement.executeQuery()));
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
                Alerta.ShowError(e);
            }

        return null;
    }

    @Override
    public boolean exists(Alumno alumno) {
        return !(Objects.isNull(this.get(alumno.getCi())));
    }

    @Override
    public ASAS<Alumno> getAlumnospercurso(Curso curso) {
        try(Connection conn = this.connection.conect()) {
            String sql = "select * from alumnos inner join cursos c on alumnos.idcurso = c.id_curso where idcurso = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, curso.getIdcurso());
            return new ASAS<>(read(preparedStatement.executeQuery()));
        }catch (SQLException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
        return null;
    }

    private String getsimplesqlfilter(BuscarType buscarType){
        String sql = "";
        switch (buscarType) {
            case ID:
                sql = "select * from alumnos where dniAlumno like ? ;";
                break;
            case Nombre:
                sql = "select * from alumnos where nombres like ?;";
                break;
            case Apellido:
                sql = "select * from alumnos where apellidos like ?;";
                break;
        }
        return sql;
    }
    private String getcompletesqlfilter(BuscarType buscarType){
        String sql = "";
        switch (buscarType){
            case ID:
                sql = "select * from alumnos left join cursos on alumnos.idcurso = cursos.id_curso where bachiller = ? and seccion = ? and grado = ? and dniAlumno like ? ;";
                break;
            case Nombre:
                sql = "select * from alumnos left join cursos on alumnos.idcurso = cursos.id_curso where bachiller = ? and seccion = ? and grado = ? and nombres like ?;";
                break;
            case Apellido:
                sql = "select * from alumnos left join cursos on alumnos.idcurso = cursos.id_curso where bachiller = ? and seccion = ? and grado = ? and apellidos like ?;";
                break;
        }
        return sql;
    }
}

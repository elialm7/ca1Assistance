package Modelos.SQLITE.Dao.Alumno_Profes;

import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Users.User;
import Modelos.SQLITE.Interfaces.IUser;
import Servicios.Utils.ASAS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserIMP implements IUser {
    private MyConnection connection;
    public UserIMP(MyConnection connection) {
        this.connection = connection;
    }

    private ASAS<User> loaduser(ResultSet rs) throws SQLException {
        ASAS<User> userASAS;
        List<User> userList = new ArrayList<>();
        User user = null;
        while(rs.next()){
            user = new User(rs.getString("username"), rs.getString("password"));
            user.setId(rs.getInt("iduser"));
            userList.add(user);
        }
        userASAS = new ASAS<>(userList);
        return  userASAS;
    }

    @Override
    public void add(User toadd) {
        Connection conn = null;
        User user = toadd;
        try{
            conn = connection.conect();
            String sql = "insert into users(username, password) values (?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
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

    }

    @Override
    public User get(Integer toget) {

        Connection conn = null;

        try{
            conn = connection.conect();
            String sql = "select * from users where iduser = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, toget);
            ResultSet rs = preparedStatement.executeQuery();
            return loaduser(rs).AsArrayList().get(0);
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
    public void delete(Integer todelete) {
        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "delete from users where iduser = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, todelete);
            preparedStatement.executeUpdate();
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
    }

    @Override
    public void change(User tochange) {
        Connection conn = null;
        User user = tochange;
        try{
            conn = connection.conect();
            String sql = "update users set username = ?, password = ? where iduser = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.executeUpdate();
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
    }

    @Override
    public ASAS<User> getall() {

        Connection conn = null;
        try{
            conn = connection.conect();
            String sql = "select * from users ;";
            Statement st = conn.createStatement();
            return loaduser(st.executeQuery(sql));
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
    public boolean exists(User user) {
        boolean iscorrect = false;
        for(User us : getall().AsObservableList()){
            if(us.equals(user)){
                iscorrect = true;
            }
        }
        return iscorrect;
    }
}

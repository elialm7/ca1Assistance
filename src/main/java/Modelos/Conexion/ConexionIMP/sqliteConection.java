package Modelos.Conexion.ConexionIMP;

import Modelos.Conexion.MyConnection;
import Modelos.Directorio.Directory;
import Modelos.Directorio.DirectoryConstants;
import Modelos.Directorio.JarDirectory;
import Modelos.Directorio.NormalDirectory;
import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqliteConection implements MyConnection {

    private Directory DbDataFile = new NormalDirectory(DirectoryConstants.DataFilesRoot, DirectoryConstants.DATABASENAME); // ubicacion de la base de datos
    private Directory SqlDataFile = new JarDirectory(DirectoryConstants.DBFILEDIRECTORY, DirectoryConstants.DBFILEDATA); // ubicacion del archivo sql para la creacion de la base de datos
    private boolean verified;
    public sqliteConection(boolean isverified){
        this.verified = isverified;
    }
    @Override
    public synchronized Connection conect() throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            if(verified){
                return getconnection();
            }else {
                if (!DbDataFile.exists()) {
                    c = getconnection();
                    createdatabase(c);
                } else {
                    c = getconnection();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return c;
    }
    private void createdatabase(Connection connection) throws IOException, SQLException {
        ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(SqlDataFile.getpath()));
        scriptRunner.runScript(bufferedReader);
    }
    private Connection getconnection() throws SQLException {
        String pragma = "PRAGMA foreign_keys=on";
        Connection conn =  DriverManager.getConnection("jdbc:sqlite:" + DbDataFile.getpath());
        conn.createStatement().execute(pragma);
        return conn;
    }
}

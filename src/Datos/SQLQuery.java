/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;
import java.sql.*;

/**
 *
 * @author Cristian
 */
public class SQLQuery {
    protected Connection conn;
    protected PreparedStatement consulta;
    protected ResultSet datos;

    protected String servidor = "127.0.0.1";
    protected String base_de_datos = "SGA_2020";
    protected String usuario = "root";
    protected String password = "mysql";
    
    public void conectar() throws ClassNotFoundException, SQLException{
        
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection("jdbc:mysql://"+servidor+"/"+base_de_datos,usuario,password);
    }
    
    public void desconectar() throws SQLException{
        this.conn.close();
        this.consulta.close();
    }
    
    public void desconectar(ResultSet rs) throws SQLException{
        this.desconectar();
        rs.close();
    }
    
}

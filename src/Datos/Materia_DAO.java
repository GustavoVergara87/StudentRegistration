//@author Gustavo
package Datos;

import Modelo.Materia_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Materia_DAO extends SQLQuery {

    public Materia_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    //CRUD
    //Create
    public boolean crear_registro(Materia_Modelo materia) {
        String sql;
        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO materia (mat_nombre, mat_prof_dni, mat_cod) VALUES (?,?,null)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, materia.getMat_nombre());
            this.consulta.setObject(2, materia.getMat_prof_dni());
            this.consulta.execute();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //Update
    public boolean guardar_registro(Materia_Modelo materia) {
        String sql;
        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "UPDATE materia SET mat_nombre=?, mat_prof_dni=? WHERE mat_cod=?";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, materia.getMat_nombre());
            this.consulta.setObject(2, materia.getMat_prof_dni());
            this.consulta.setObject(3, materia.getMat_cod());

            this.consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //Read
    public ArrayList listar_registros() {
        Materia_Modelo materia;
        ArrayList materias = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM materia");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                materia = new Modelo.Materia_Modelo(
                        hojadeResultados.getLong("mat_cod"),
                        hojadeResultados.getString("mat_nombre"),
                        hojadeResultados.getLong("mat_prof_dni"));
                materias.add(materia);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materias;
    }

    //Delete
    public boolean quitar_registro(Materia_Modelo materia) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM materia WHERE mat_cod=?");
            this.consulta.setLong(1, materia.getMat_cod());
            this.consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean existe_registro(Materia_Modelo materia) {
        String sql;
        try {
            this.conectar();
            sql = "SELECT * FROM materia WHERE mat_cod=?";
            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setLong(1, materia.getMat_cod());

            ResultSet hojadeResultados = this.consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Set<String> listar_mat_prof_dni() {
        Set<String> mat_prof_dni = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT prof_dni FROM profesor");
            ResultSet hojadeResultados = consulta.executeQuery();
            mat_prof_dni.add("");
            while (hojadeResultados.next()) {
                mat_prof_dni.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Materia_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mat_prof_dni;
    }

    public Long obtener_autoincremeto() {
        String sql;
        Long nueva_clave=null;
        try {
            this.conectar();
            sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_NAME =? AND TABLE_SCHEMA=?";
            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setString(1, "materia");
            this.consulta.setString(2, this.base_de_datos);

            ResultSet hojadeResultados = this.consulta.executeQuery();
            while (hojadeResultados.next()) {
                nueva_clave = hojadeResultados.getLong("auto_increment");
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nueva_clave;
        
    }

}

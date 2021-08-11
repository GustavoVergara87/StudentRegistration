//@author Gustavo
package Datos;

import Modelo.Inscripcion_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Inscripcion_DAO extends SQLQuery {

    public Inscripcion_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    public ArrayList listar_registros() {
        Inscripcion_Modelo inscripcion;
        ArrayList alumnos = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM inscripcion");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                inscripcion = new Modelo.Inscripcion_Modelo(
                        hojadeResultados.getLong("insc_cod"),
                        hojadeResultados.getString("insc_nombre"),
                        hojadeResultados.getString("insc_fecha"),
                        hojadeResultados.getLong("insc_car_cod"));
                alumnos.add(inscripcion);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return alumnos;
    }

    public boolean crear_registro(Inscripcion_Modelo inscripcion) {
        String sql;

        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO inscripcion (insc_cod, insc_nombre, insc_fecha, insc_car_cod) VALUES (null,?,?,?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, inscripcion.getInsc_nombre());
            this.consulta.setObject(2, inscripcion.getInsc_fechaSQL());
            this.consulta.setObject(3, inscripcion.getInsc_car_cod());
            this.consulta.execute();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean guardar_registro(Inscripcion_Modelo inscripcion) {
        String sql;

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            sql = "UPDATE inscripcion SET insc_nombre=?, insc_fecha=?, insc_car_cod=? WHERE insc_cod=?";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, inscripcion.getInsc_nombre());
            this.consulta.setObject(2, inscripcion.getInsc_fechaSQL());
            this.consulta.setObject(3, inscripcion.getInsc_car_cod());
            this.consulta.setObject(4, inscripcion.getInsc_cod());

            this.consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean existe_registro(Inscripcion_Modelo inscripcion) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM inscripcion WHERE insc_cod=?");
            this.consulta.setLong(1, inscripcion.getInsc_cod());
            ResultSet hojadeResultados = consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean quitar_registro(Inscripcion_Modelo inscripcion) {
        try {
            this.conectar();
            //Evita el error
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM inscripcion WHERE insc_cod=?");
            this.consulta.setLong(1, inscripcion.getInsc_cod());
            consulta.executeUpdate();

            //continúa revisando las restricciones de llaves foráneas
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();

            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Set<String> listar_cod_insc() {
        Set<String> cod_insc = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT car_cod FROM carrera");
            ResultSet hojadeResultados = consulta.executeQuery();
            cod_insc.add("");
            while (hojadeResultados.next()) {
                cod_insc.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Inscripcion_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cod_insc;
    }

    public Long obtener_autoincremeto() {
        String sql;
        Long nueva_clave = null;
        try {
            this.conectar();
            sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_NAME =? AND TABLE_SCHEMA=?";
            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setString(1, "inscripcion");
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

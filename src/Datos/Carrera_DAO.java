//@author Gustavo

package Datos;

import Modelo.Carrera_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Carrera_DAO extends SQLQuery {

    public Carrera_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    public ArrayList listar_registros() {
        Carrera_Modelo carrera;
        ArrayList alumnos = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM carrera");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                carrera = new Modelo.Carrera_Modelo(
                        hojadeResultados.getLong("car_cod"),
                        hojadeResultados.getString("car_nombre"),
                        hojadeResultados.getInt("car_duracion"));
                alumnos.add(carrera);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return alumnos;
    }

    public boolean crear_registro(Carrera_Modelo carrera) {
        String sql;

        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO carrera (car_cod, car_nombre, car_duracion) VALUES (null,?,?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, carrera.getCar_nombre());
            this.consulta.setObject(2, carrera.getCar_duracion());
            this.consulta.execute();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean guardar_registro(Carrera_Modelo carrera) {
        String sql;

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            sql = "UPDATE carrera SET car_nombre=?, car_duracion=? WHERE car_cod=?";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, carrera.getCar_nombre());
            this.consulta.setObject(2, carrera.getCar_duracion());
            this.consulta.setObject(3, carrera.getCar_cod());

            this.consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean existe_registro(Carrera_Modelo carrera) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM carrera WHERE car_cod=?");
            this.consulta.setLong(1, carrera.getCar_cod());
            ResultSet hojadeResultados = consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean quitar_registro(Carrera_Modelo carrera) {
        try {
            this.conectar();
            //Evita el error
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM carrera WHERE car_cod=?");
            this.consulta.setLong(1, carrera.getCar_cod());
            consulta.executeUpdate();

            //continúa revisando las restricciones de llaves foráneas
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();

            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Set<String> listar_cod_car() {
        Set<String> cod_car = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT car_cod FROM carrera");
            ResultSet hojadeResultados = consulta.executeQuery();
            cod_car.add("");
            while (hojadeResultados.next()) {
                cod_car.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Carrera_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cod_car;
    }

    public Long obtener_autoincremeto() {
        String sql;
        Long nueva_clave=null;
        try {
            this.conectar();
            sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_NAME =? AND TABLE_SCHEMA=?";
            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setString(1, "carrera");
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

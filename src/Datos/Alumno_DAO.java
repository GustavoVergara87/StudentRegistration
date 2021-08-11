//@author Gustavo
package Datos;

import Modelo.Alumno_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;
import java.util.Set;

public class Alumno_DAO extends SQLQuery {

    public Alumno_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    public ArrayList listar_registros() {
        Alumno_Modelo alumno;
        ArrayList alumnos = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM alumno");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                alumno = new Modelo.Alumno_Modelo(
                        hojadeResultados.getLong("alu_dni"),
                        hojadeResultados.getString("alu_nombre"),
                        hojadeResultados.getString("alu_apellido"),
                        hojadeResultados.getString("alu_fec_nac"),
                        hojadeResultados.getString("alu_domicilio"),
                        hojadeResultados.getString("alu_telefono"),
                        (Long) hojadeResultados.getObject("alu_insc_cod"));
                alumnos.add(alumno);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return alumnos;
    }

    //CRUD
    //Create
    public boolean crear_registro(Alumno_Modelo alumno) {
        String sql;

        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO alumno (alu_nombre, alu_apellido, alu_fec_nac, alu_domicilio, alu_telefono, alu_insc_cod, alu_dni) VALUES (?,?,?,?,?,?,?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, alumno.getAlu_nombre());
            this.consulta.setObject(2, alumno.getAlu_apellido());
            this.consulta.setObject(3, alumno.getAlu_fec_nacSQL());
            this.consulta.setObject(4, alumno.getAlu_domicilio());
            this.consulta.setObject(5, alumno.getAlu_telefono());
            this.consulta.setObject(6, alumno.getAlu_insc_cod());
            this.consulta.setObject(7, alumno.getAlu_dni());

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
    public boolean guardar_registro(Alumno_Modelo alumno) {
        String sql;

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            sql = "UPDATE alumno SET alu_nombre=?, alu_apellido=?, alu_fec_nac=?, alu_domicilio=?, alu_telefono=?, alu_insc_cod=? WHERE alu_dni=?";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, alumno.getAlu_nombre());
            this.consulta.setObject(2, alumno.getAlu_apellido());
            this.consulta.setObject(3, alumno.getAlu_fec_nacSQL());
            this.consulta.setObject(4, alumno.getAlu_domicilio());
            this.consulta.setObject(5, alumno.getAlu_telefono());
            this.consulta.setObject(6, alumno.getAlu_insc_cod());
            this.consulta.setObject(7, alumno.getAlu_dni());

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
    public Set<String> listar_cod_insc() {
        Set<String> cod_insc = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT insc_cod FROM inscripcion");
            ResultSet hojadeResultados = consulta.executeQuery();
            cod_insc.add("");
            while (hojadeResultados.next()) {
                cod_insc.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cod_insc;
    }

    //Delete
    public boolean quitar_registro(Alumno_Modelo alumno) {
        try {
            this.conectar();
            //Evita el error
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: 
            //Cannot delete or update a parent row: a foreign key constraint fails
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM alumno WHERE alu_dni=?");
            this.consulta.setLong(1, alumno.getAlu_dni());
            consulta.executeUpdate();

            //continúa revisando las restricciones de llaves foráneas
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();

            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean existe_registro(Alumno_Modelo alumno) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM alumno WHERE alu_dni=?");
            this.consulta.setLong(1, alumno.getAlu_dni());
            ResultSet hojadeResultados = consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

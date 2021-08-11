//@author Gustavo
package Datos;

import Modelo.Profesor_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Profesor_DAO extends SQLQuery {

    public Profesor_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    public ArrayList listar_registros() {
        Profesor_Modelo profesor;
        ArrayList profesores = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM profesor");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                profesor = new Modelo.Profesor_Modelo(
                        hojadeResultados.getLong("prof_dni"),
                        hojadeResultados.getString("prof_nombre"),
                        hojadeResultados.getString("prof_apellido"),
                        hojadeResultados.getString("prof_fec_nac"),
                        hojadeResultados.getString("prof_domicilio"),
                        hojadeResultados.getString("prof_telefono"));
                profesores.add(profesor);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Profesor_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profesores;
    }

    public boolean crear_registro(Profesor_Modelo profesor) {
        String sql;

        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO profesor (prof_nombre, prof_apellido, prof_fec_nac, prof_domicilio, prof_telefono, prof_dni) VALUES (?,?,?,?,?,?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, profesor.getProf_nombre());
            this.consulta.setObject(2, profesor.getProf_apellido());
            this.consulta.setObject(3, profesor.getProf_fec_nacSQL());
            this.consulta.setObject(4, profesor.getProf_domicilio());
            this.consulta.setObject(5, profesor.getProf_telefono());
            this.consulta.setObject(6, profesor.getProf_dni());

            this.consulta.execute();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Profesor_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean guardar_registro(Profesor_Modelo profesor) {
        String sql;

        try {
            this.conectar();

            sql = "UPDATE profesor SET prof_nombre=?, prof_apellido=?, prof_fec_nac=?, prof_domicilio=?, prof_telefono=? WHERE prof_dni=?";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, profesor.getProf_nombre());
            this.consulta.setObject(2, profesor.getProf_apellido());
            this.consulta.setObject(3, profesor.getProf_fec_nacSQL());
            this.consulta.setObject(4, profesor.getProf_domicilio());
            this.consulta.setObject(5, profesor.getProf_telefono());
            this.consulta.setObject(6, profesor.getProf_dni());

            this.consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Profesor_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean existe_registro(Profesor_Modelo profesor) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * FROM profesor WHERE prof_dni=?");
            this.consulta.setLong(1, profesor.getProf_dni());
            ResultSet hojadeResultados = consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Profesor_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean quitar_registro(Profesor_Modelo profesor) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM profesor WHERE prof_dni=?");
            this.consulta.setLong(1, profesor.getProf_dni());
            consulta.executeUpdate();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            this.datos = this.consulta.executeQuery();
            this.desconectar();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Profesor_DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}

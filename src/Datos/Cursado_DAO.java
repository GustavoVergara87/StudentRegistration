//@author Gustavo
package Datos;

import Modelo.Cursado_Modelo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cursado_DAO extends SQLQuery {

    public Cursado_DAO() {
        this.servidor = "127.0.0.1";
        this.base_de_datos = "SGA_2020";
        this.usuario = "root";
        this.password = "mysql";
    }

    public ArrayList listar_registros() {
        Cursado_Modelo curso;
        ArrayList cursos = new ArrayList();

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT * from cursado");
            ResultSet hojadeResultados = consulta.executeQuery();

            while (hojadeResultados.next()) {
                curso = new Modelo.Cursado_Modelo(
                        hojadeResultados.getLong("cur_alu_dni"),
                        hojadeResultados.getLong("cur_mat_cod"),
                        hojadeResultados.getFloat("cur_nota"));
                cursos.add(curso);
            }
            this.desconectar();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Alumno_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cursos;
    }

    public boolean guardar_registro(Cursado_Modelo curso) {
        String sql;

        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "UPDATE cursado SET cur_nota=? WHERE (cur_alu_dni=?) AND (cur_mat_cod=?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, curso.getCur_nota());
            this.consulta.setObject(2, curso.getCur_alu_dni());
            this.consulta.setObject(3, curso.getCur_mat_cod());
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

    public boolean crear_registro(Cursado_Modelo curso) {
        String sql;

        try {
            this.conectar();

            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();
            sql = "INSERT INTO cursado (cur_nota, cur_alu_dni, cur_mat_cod) VALUES (?,?,?)";

            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setObject(1, curso.getCur_nota());
            this.consulta.setObject(2, curso.getCur_alu_dni());
            this.consulta.setObject(3, curso.getCur_mat_cod());

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

    public boolean existe_registro(Cursado_Modelo curso) {
        String sql;
        try {
            this.conectar();
            sql = "SELECT * FROM cursado WHERE (cur_alu_dni=?) AND (cur_mat_cod=?)";
            this.consulta = this.conn.prepareStatement(sql);
            this.consulta.setLong(1, curso.getCur_alu_dni());
            this.consulta.setLong(2, curso.getCur_mat_cod());

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

    public boolean quitar_registro(Cursado_Modelo curso) {
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            this.datos = this.consulta.executeQuery();

            this.consulta = this.conn.prepareStatement("DELETE FROM cursado WHERE (cur_alu_dni=?) AND (cur_mat_cod=?)");
            this.consulta.setLong(1, curso.getCur_alu_dni());
            this.consulta.setLong(2, curso.getCur_mat_cod());
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

    public Set<String> listar_cur_alu_dni() {
        Set<String> cur_alu_dni = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT alu_dni FROM alumno");
            ResultSet hojadeResultados = consulta.executeQuery();
            cur_alu_dni.add("");
            while (hojadeResultados.next()) {
                cur_alu_dni.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Cursado_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cur_alu_dni;
    }

    public Set<String> listar_cur_mat_cod() {
        Set<String> cur_mat_cod = new HashSet<>();
        try {
            this.conectar();
            this.consulta = this.conn.prepareStatement("SELECT mat_cod FROM materia");
            ResultSet hojadeResultados = consulta.executeQuery();
            cur_mat_cod.add("");
            while (hojadeResultados.next()) {
                cur_mat_cod.add(Long.toString(hojadeResultados.getLong(1)));
            }
            this.desconectar();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Cursado_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cur_mat_cod;
    }

}

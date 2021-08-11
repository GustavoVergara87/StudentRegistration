//@author Gustavo
package Modelo;

import Datos.Cursado_DAO;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JOptionPane;

public class Cursado_Modelo {

    private Long cur_alu_dni; //notNull, bigint(10), unsigned, unica
    private Long cur_mat_cod; //notNull, bigint(10), unica
    private Float cur_nota; //float, unsigned

    public static final int CUR_ALU_DNI_POS = 0;
    public static final int CUR_MAT_POS = 1;
    public static final int CUR_NOTA_POS = 2;

    public static final String[] Titulos = {"DNI", "Código de Materia", "Nota"};

    private Cursado_DAO objeto_dao = new Cursado_DAO();

    public boolean Curso_Modelo_ValidarCastearSetear(String cur_alu_dni, String cur_mat_cod, String cur_nota) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setCur_alu_dni(cur_alu_dni);
            this.setCur_mat_cod(cur_mat_cod);
            this.setCur_nota(cur_nota);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursado_Modelo(Long cur_alu_dni, Long cur_mat_cod, Float cur_nota) {
        //recibe de DAO todos los atributos, castea las fechas
        this.cur_alu_dni = cur_alu_dni;
        this.cur_mat_cod = cur_mat_cod;
        this.cur_nota = cur_nota;
    }

    public Cursado_Modelo() {
    }

    public Long getCur_alu_dni() {
        return cur_alu_dni;
    }

    public Long getCur_mat_cod() {
        return cur_mat_cod;
    }

    public Float getCur_nota() {
        return cur_nota;
    }

    public Cursado_DAO getAlumno_dao() {
        return objeto_dao;
    }

    public void setCur_alu_dni(String cur_alu_dni) throws Exception {
        try {
            this.cur_alu_dni = Long.valueOf(cur_alu_dni);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DNI error. " + e.getMessage());
            throw e;
        }
    }

    public void setCur_mat_cod(String cur_mat_cod) throws Exception {
        try {
            this.cur_mat_cod = Long.valueOf(cur_mat_cod);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Codigo de Materia error. " + e.getMessage());
            throw e;
        }
    }

    public void setCur_nota(String cur_nota) throws Exception {
        if (Casteo.FloatOrNullValue(cur_nota) < 0 || Casteo.FloatOrNullValue(cur_nota) > 10.0F) {
            JOptionPane.showMessageDialog(null, "Nota invalida. Debe estar entre 0 y 10");
            throw new Exception();
        }

        this.cur_nota = Casteo.FloatOrNullValue(cur_nota);

    }

    public void setAlumno_dao(Cursado_DAO objeto_dao) {
        this.objeto_dao = objeto_dao;
    }

    public ArrayList listar_registros() {
        ArrayList registros = objeto_dao.listar_registros();
        if (registros == null) {
            JOptionPane.showMessageDialog(null, "Falló la coneccion a la Base de Datos, revise que AMPPS o su servidor web se esté ejecutando");
            System.exit(0);
        }
        return registros;
    }

    public boolean crear_registro(Cursado_Modelo curso) {
        if (objeto_dao.existe_registro(curso)) {
            JOptionPane.showMessageDialog(null, "Ya existe un registo con ese DNI y Codigo de Materia");
            return false;
        }
        if (objeto_dao.crear_registro(curso)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean guardar_registro(Cursado_Modelo curso) {
        if (objeto_dao.guardar_registro(curso)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Cursado_Modelo cursado) {
        if (objeto_dao.quitar_registro(cursado)) {
            JOptionPane.showMessageDialog(null, "Cursado eliminado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar el cursado");
        return false;
    }

    public Set<String> listar_cur_alu_dni() {
        return objeto_dao.listar_cur_alu_dni();
    }

    public Set<String> listar_cur_mat_cod() {
        return objeto_dao.listar_cur_mat_cod();
    }

    public Object getCampo(int index) {

        switch (index) {
            case CUR_ALU_DNI_POS:
                return cur_alu_dni;
            case CUR_MAT_POS:
                return cur_mat_cod;
            case CUR_NOTA_POS:
                return cur_nota;

        }
        return null;
    }

}

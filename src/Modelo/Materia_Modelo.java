//@author Gustavo
package Modelo;

import Datos.Materia_DAO;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JOptionPane;

public class Materia_Modelo {

    private Long mat_cod;
    private String mat_nombre;
    private Long mat_prof_dni;

    public static final int MAT_COD_POS = 0;
    public static final int MAT_NOMBRE_POS = 1;
    public static final int MAT_PROF_DNI_POS = 2;

    public static final int MAT_NOMBRE_MAX = 50;

    public static final String[] Titulos = {"Codigo de Materia", "Nombre", "DNI de Profesor"};

    private Materia_DAO objeto_dao = new Materia_DAO();

    public boolean Curso_Modelo_ValidarCastearSetear(String mat_cod, String mat_nombre, String mat_prof_dni) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setMat_cod(mat_cod);
            this.setMat_nombre(mat_nombre);
            this.setMat_prof_dni(mat_prof_dni);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Materia_Modelo(Long mat_cod, String mat_nombre, Long mat_prof_dni) {
        //recibe de DAO todos los atributos, castea las fechas
        this.mat_cod = mat_cod;
        this.mat_nombre = mat_nombre;
        this.mat_prof_dni = mat_prof_dni;
    }

    public Materia_Modelo() {
    }

    public Long getMat_cod() {
        return mat_cod;
    }

    public String getMat_nombre() {
        return mat_nombre;
    }

    public Long getMat_prof_dni() {
        return mat_prof_dni;
    }

    public Materia_DAO getMateria_dao() {
        return objeto_dao;
    }

    public void setMat_cod(String mat_cod) {
        //se espera un valor nulo en Mat_cod por ser autoincremental
        this.mat_cod = Casteo.LongOrNullValue(mat_cod);
    }

    public void setMat_nombre(String mat_nombre) throws Exception {
        if (mat_nombre.length() > MAT_NOMBRE_MAX) {
            JOptionPane.showMessageDialog(null, "Nombre tiene mas de " + MAT_NOMBRE_MAX + " caracteres");
            throw new Exception();
        }
        if (mat_nombre.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");
            throw new Exception();
        }
        this.mat_nombre = mat_nombre;
    }

    public void setMat_prof_dni(String mat_prof_dni) {
        try {
            this.mat_prof_dni = Long.valueOf(mat_prof_dni);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DNI profesor error. " + e.getMessage());
            throw e;
        }
    }

    public void setMateria_dao(Materia_DAO objeto_dao) {
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

    public boolean nuevo_registro(Materia_Modelo materia) {
//Esta situacion nunca se va a dar por que el codigo de materia es autoincremental
//puede suceder que haya dos materias con igual nombre y distinto codigo
//esa situacion no se evalúa
//        if (objeto_dao.existe_registro(materia)) {
//            JOptionPane.showMessageDialog(null, "Ya existe un registo con ese DNI y Codigo de Materia");
//            return false;
//        }
        if (objeto_dao.crear_registro(materia)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al crear el registro");
        return false;
    }

    public boolean guardar_registro(Materia_Modelo materia) {
        if (objeto_dao.guardar_registro(materia)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Materia_Modelo materia) {
        if (objeto_dao.quitar_registro(materia)) {
            JOptionPane.showMessageDialog(null, "Materia eliminada correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar la materia");
        return false;
    }

    public Set<String> listar_mat_prof_dni() {
        return objeto_dao.listar_mat_prof_dni();
    }

    public Object getCampo(int index) {

        switch (index) {
            case MAT_COD_POS:
                return mat_cod;
            case MAT_NOMBRE_POS:
                return mat_nombre;
            case MAT_PROF_DNI_POS:
                return mat_prof_dni;
        }
        return null;
    }

    public Long obtener_autoincremeto() {
        return objeto_dao.obtener_autoincremeto();
    }

}

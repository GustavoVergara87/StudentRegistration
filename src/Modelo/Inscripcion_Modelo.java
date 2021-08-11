//@author Gustavo
package Modelo;

import Controlador.Comunes;
import Datos.Inscripcion_DAO;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JOptionPane;

public class Inscripcion_Modelo {

    private Long insc_cod;
    private String insc_nombre;
    private String insc_fecha;
    private Long insc_car_cod;

    public static final int INSC_COD_POS = 0;
    public static final int INSC_NOMBRE_POS = 1;
    public static final int INSC_FECHA_POS = 2;
    public static final int INSC_CAR_COD_POS = 3;

    public static final int INSC_NOMBRE_MAX = 30;

    public static final String[] Titulos = {"Codigo", "Nombre", "Fecha de Inscripcion", "Cód. Carrera"};

    private Inscripcion_DAO objeto_dao = new Inscripcion_DAO();

    public boolean Inscripcion_Modelo_ValidarCastearSetear(String insc_cod, String insc_nombre, String insc_fecha, Object insc_car_cod) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setInsc_cod(insc_cod);
            this.setInsc_nombre(insc_nombre);
            this.setInsc_fecha(insc_fecha);
            this.setInsc_car_cod(insc_car_cod);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Inscripcion_Modelo(Long insc_cod, String insc_nombre, String insc_fecha, Long insc_car_cod) {
        //recibe de DAO todos los atributos, castea las fechas
        this.insc_cod = insc_cod;
        this.insc_nombre = insc_nombre;
        this.insc_fecha = Casteo.DateOrNullValue(insc_fecha);
        this.insc_car_cod = insc_car_cod;
    }

    public Inscripcion_Modelo() {
    }

    public Long getInsc_cod() {
        return insc_cod;
    }

    public String getInsc_nombre() {
        return insc_nombre;
    }

    public String getInsc_fecha() {
        return insc_fecha;
    }

    public Long getInsc_car_cod() {
        return insc_car_cod;
    }

    public String getInsc_fechaSQL() {         //Inscripcion_DAO pide formato yyyy-mm-dd
        return Casteo.Date_a_DateSQLOrNullValue(insc_fecha);
    }

    public void setInsc_cod(String insc_cod) {
        //se espera un valor nulo en insc_cod por ser autoincremental
        this.insc_cod = Casteo.LongOrNullValue(insc_cod);
    }

    public void setInsc_nombre(String insc_nombre) throws Exception {
        if (insc_nombre.length() > INSC_NOMBRE_MAX) {
            JOptionPane.showMessageDialog(null, "Nombre tiene mas de " + INSC_NOMBRE_MAX + " caracteres");
            throw new Exception();
        }
        if (insc_nombre.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");

            throw new Exception();
        }
        this.insc_nombre = insc_nombre;
    }

    public void setInsc_fecha(String insc_fecha) throws Exception {
        if (insc_fecha.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Fecha no puede ser nula");
            throw new Exception();
        }
        this.insc_fecha = Casteo.DateOrNullValue(insc_fecha);
    }

    public void setInsc_car_cod(Object insc_car_cod) throws Exception {
        try {
            this.insc_car_cod = Casteo.LongOrNullValue(insc_car_cod);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Codigo de Carrera invalido");
            throw e;
        }
    }

    public ArrayList listar_registros() {
        ArrayList registros = objeto_dao.listar_registros();
        if (registros == null) {
            JOptionPane.showMessageDialog(null, "Falló la coneccion a la Base de Datos, revise que AMPPS o su servidor web se esté ejecutando");
            System.exit(0);
        }
        return registros;
    }

    public boolean crear_registro(Inscripcion_Modelo inscripcion) {
        if (objeto_dao.crear_registro(inscripcion)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al crear el registro");
        return false;
    }

    public boolean guardar_registro(Inscripcion_Modelo inscripcion) {
        if (objeto_dao.guardar_registro(inscripcion)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Inscripcion_Modelo inscripcion) {
        if (objeto_dao.quitar_registro(inscripcion)) {
            JOptionPane.showMessageDialog(null, "Inscripcion eliminada correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar la inscripcion");
        return false;
    }

    public Set<String> listar_cod_insc() {
        return objeto_dao.listar_cod_insc();
    }

    public Object getCampo(int index) {

        switch (index) {
            case INSC_COD_POS:
                return insc_cod;
            case INSC_NOMBRE_POS:
                return insc_nombre;
            case INSC_FECHA_POS:
                return insc_fecha;
            case INSC_CAR_COD_POS:
                return insc_car_cod;
        }
        return null;
    }

    public Long obtener_autoincremeto() {
        return objeto_dao.obtener_autoincremeto();
    }

}

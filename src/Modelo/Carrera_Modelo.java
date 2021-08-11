//@author Gustavo
package Modelo;

import Datos.Carrera_DAO;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JOptionPane;

public class Carrera_Modelo {

    private Long car_cod;
    private String car_nombre;
    private Integer car_duracion;

    public static final int CAR_COD_POS = 0;
    public static final int CAR_NOMBRE_POS = 1;
    public static final int CAR_DURACION_POS = 2;

    public static final int CAR_NOMBRE_MAX = 30;

    public static final String[] Titulos = {"Codigo", "Nombre", "Duracion (años)"};

    private Carrera_DAO objeto_dao = new Carrera_DAO();

    public boolean Carrera_Modelo_ValidarCastearSetear(String car_cod, String car_nombre, String car_duracion) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setCar_cod(car_cod);
            this.setCar_nombre(car_nombre);
            this.setCar_duracion(car_duracion);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Carrera_Modelo(Long car_cod, String car_nombre, Integer car_duracion) {
        //recibe de DAO todos los atributos, castea las fechas
        this.car_cod = car_cod;
        this.car_nombre = car_nombre;
        this.car_duracion = car_duracion;
    }

    public Carrera_Modelo() {
    }

    public Long getCar_cod() {
        return car_cod;
    }

    public String getCar_nombre() {
        return car_nombre;
    }

    public Integer getCar_duracion() {
        return car_duracion;
    }

    public void setCar_cod(String car_cod) {
        //se espera un valor nulo en car_cod por ser autoincremental
        this.car_cod = Casteo.LongOrNullValue(car_cod);
    }

    public void setCar_nombre(String car_nombre) throws Exception {
        if (car_nombre.length() > CAR_NOMBRE_MAX) {
            JOptionPane.showMessageDialog(null, "Nombre tiene mas de " + CAR_NOMBRE_MAX + " caracteres");
            throw new Exception();
        }
        if (car_nombre.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");

            throw new Exception();
        }
        this.car_nombre = car_nombre;
    }

    public void setCar_duracion(String car_duracion) throws Exception {
        try {
            this.car_duracion = Integer.valueOf(car_duracion);
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "Duracion error.");
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

    public boolean crear_registro(Carrera_Modelo carrera) {
        if (objeto_dao.crear_registro(carrera)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al crear el registro");
        return false;
    }

    public boolean guardar_registro(Carrera_Modelo carrera) {
        if (objeto_dao.guardar_registro(carrera)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Carrera_Modelo carrera) {
        if (objeto_dao.quitar_registro(carrera)) {
            JOptionPane.showMessageDialog(null, "Carrera eliminada correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar la carrera");
        return false;
    }

    public Set<String> listar_cod_car() {
        return objeto_dao.listar_cod_car();
    }

    public Object getCampo(int index) {

        switch (index) {
            case CAR_COD_POS:
                return car_cod;
            case CAR_NOMBRE_POS:
                return car_nombre;
            case CAR_DURACION_POS:
                return car_duracion;
        }
        return null;
    }

    public Long obtener_autoincremeto() {
        return objeto_dao.obtener_autoincremeto();
    }

}

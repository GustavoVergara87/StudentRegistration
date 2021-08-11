//@author Gustavo
package Modelo;

import Datos.Profesor_DAO;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Profesor_Modelo {

    private Long prof_dni; //notNull, bigint(10), unsigned, unica
    private String prof_nombre; //notNull, varchar(30)
    private String prof_apellido; //notNull, varchar(50)
    private String prof_fec_nac; //date
    private String prof_domicilio; //varchar(100)
    private String prof_telefono; //varchar(50)

    public static final int PROF_DNI_POS = 0;
    public static final int PROF_NOMBRE_POS = 1;
    public static final int PROF_APELLIDO_POS = 2;
    public static final int PROF_FEC_NAC_POS = 3;
    public static final int PROF_DOMICILIO_POS = 4;
    public static final int PROF_TELEFONO_POS = 5;

    public static final int PROF_NOMBRE_MAX = 30;
    public static final int PROF_APELLIDO_MAX = 50;
    public static final int PROF_DOMICILIO_MAX = 100;
    public static final int PROF_TELEFONO_MAX = 50;

    public static final String[] Titulos = {"DNI", "Nombre", "Apellido", "Fecha Nacimiento", "Domicilio", "Telefono"};

    private Profesor_DAO objeto_dao = new Profesor_DAO();

    public boolean Profesor_Modelo_ValidarCastearSetear(String prof_dni, String prof_nombre, String prof_apellido, String prof_fec_nac, String prof_domicilio, String prof_telefono) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setProf_dni(prof_dni);
            this.setProf_nombre(prof_nombre);
            this.setProf_apellido(prof_apellido);
            this.setProf_fec_nac(prof_fec_nac);
            this.setProf_domicilio(prof_domicilio);
            this.setProf_telefono(prof_telefono);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Profesor_Modelo(Long prof_dni, String prof_nombre, String prof_apellido, String prof_fec_nac, String prof_domicilio, String prof_telefono) {
        //recibe de DAO todos los atributos, castea las fechas
        this.prof_dni = prof_dni;
        this.prof_nombre = prof_nombre;
        this.prof_apellido = prof_apellido;
        this.prof_fec_nac = Casteo.DateOrNullValue(prof_fec_nac);
        this.prof_domicilio = prof_domicilio;
        this.prof_telefono = prof_telefono;
    }

    public Profesor_Modelo() {
    }

    public long getProf_dni() {
        return prof_dni;
    }

    public String getProf_nombre() {
        return prof_nombre;
    }

    public String getProf_apellido() {
        return prof_apellido;
    }

    public String getProf_fec_nac() {
        return prof_fec_nac;
    }

    public String getProf_fec_nacSQL() {         //Profesor_DAO pide formato yyyy-mm-dd
        return Casteo.Date_a_DateSQLOrNullValue(prof_fec_nac);
    }

    public String getProf_domicilio() {
        return prof_domicilio;
    }

    public String getProf_telefono() {
        return prof_telefono;
    }

    public void setProf_dni(String prof_dni) throws Exception {
        try {
            this.prof_dni = Long.valueOf(prof_dni);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DNI error. " + e.getMessage());
            throw e;
        }
    }

    public void setProf_nombre(String prof_nombre) throws Exception {
        if (prof_nombre.length() > PROF_NOMBRE_MAX) {
            JOptionPane.showMessageDialog(null, "Nombre tiene mas de " + PROF_NOMBRE_MAX + " caracteres");
            throw new Exception();
        }
        if (prof_nombre.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");
            throw new Exception();
        }
        this.prof_nombre = prof_nombre;
    }

    public void setProf_apellido(String prof_apellido) throws Exception {
        if (prof_apellido.length() > PROF_APELLIDO_MAX) {
            JOptionPane.showMessageDialog(null, "Apellido tiene mas de " + PROF_APELLIDO_MAX + " caracteres");
            throw new Exception();
        }
        if (prof_apellido.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Apellido no puede estar vacio");
            throw new Exception();
        }
        this.prof_apellido = prof_apellido;
    }

    public void setProf_fec_nac(String prof_fec_nac) { //Vista pide formato dd/mm/yyyy
        this.prof_fec_nac = Casteo.DateOrNullValue(prof_fec_nac);
    }

    public void setProf_domicilio(String prof_domicilio) throws Exception {
        if (prof_domicilio.length() > PROF_DOMICILIO_MAX) {
            JOptionPane.showMessageDialog(null, "Domicilio tiene mas de " + PROF_DOMICILIO_MAX + " caracteres");
            throw new Exception();
        }
        this.prof_domicilio = prof_domicilio;
    }

    public void setProf_telefono(String prof_telefono) throws Exception {
        if (prof_telefono.length() > PROF_TELEFONO_MAX) {
            JOptionPane.showMessageDialog(null, "Telefono tiene mas de " + PROF_TELEFONO_MAX + " caracteres");
            throw new Exception();
        }
        this.prof_telefono = prof_telefono;
    }

    public ArrayList listar_registros() {
        ArrayList registros = objeto_dao.listar_registros();
        if (registros == null) {
            JOptionPane.showMessageDialog(null, "Falló la coneccion a la Base de Datos, revise que AMPPS o su servidor web se esté ejecutando");
            System.exit(0);
        }
        return registros;
    }

    public boolean crear_registro(Profesor_Modelo profesor) {
        if (objeto_dao.existe_registro(profesor)) {
            JOptionPane.showMessageDialog(null, "DNI ya existe");
            return false;
        }
        if (objeto_dao.crear_registro(profesor)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al crear el registro");
        return false;
    }

    public boolean guardar_registro(Profesor_Modelo profesor) {

        if (objeto_dao.guardar_registro(profesor)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Profesor_Modelo profesor) {
        if (objeto_dao.quitar_registro(profesor)) {
            JOptionPane.showMessageDialog(null, "Profesor eliminado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar el profesor");
        return false;
    }

    public Object getCampo(int index) {
        switch (index) {
            case PROF_DNI_POS:
                return prof_dni;
            case PROF_NOMBRE_POS:
                return prof_nombre;
            case PROF_APELLIDO_POS:
                return prof_apellido;
            case PROF_FEC_NAC_POS:
                return prof_fec_nac;
            case PROF_DOMICILIO_POS:
                return prof_domicilio;
            case PROF_TELEFONO_POS:
                return prof_telefono;
        }
        return null;
    }

}

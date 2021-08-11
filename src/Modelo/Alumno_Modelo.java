//@author Gustavo
package Modelo;

import java.util.ArrayList;
import Datos.Alumno_DAO;
import java.util.Set;
import javax.swing.JOptionPane;

public class Alumno_Modelo {

    private Long alu_dni; //notNull, bigint(10), unsigned, unica
    private String alu_nombre; //notNull, varchar(30)
    private String alu_apellido; //notNull, varchar(50)
    private String alu_fec_nac; //date
    private String alu_domicilio; //varchar(100)
    private String alu_telefono; //varchar(50)
    private Long alu_insc_cod; //bigint(10)

    //Posicion en la base de datos de cada campo
    public static final int ALU_DNI_POS = 0;
    public static final int ALU_NOMBRE_POS = 1;
    public static final int ALU_APELLIDO_POS = 2;
    public static final int ALU_FEC_NAC_POS = 3;
    public static final int ALU_DOMICILIO_POS = 4;
    public static final int ALU_TELEFONO_POS = 5;
    public static final int ALU_INSC_COD_POS = 6;

    //Caractares maximos de cada campo varchar para hacer validaciones
    public static final int ALU_NOMBRE_MAX = 30;
    public static final int ALU_APELLIDO_MAX = 50;
    public static final int ALU_DOMICILIO_MAX = 100;
    public static final int ALU_TELEFONO_MAX = 50;

    public static final String[] Titulos = {"DNI", "Nombre", "Apellido", "Fecha Nacimiento", "Domicilio", "Telefono", "Codigo de Inscripcion"};

    private Alumno_DAO objeto_dao = new Alumno_DAO();

    public boolean Alumno_Modelo_ValidarCastearSetear(String alu_dni, String alu_nombre, String alu_apellido, String alu_fec_nac, String alu_domicilio, String alu_telefono, Object alu_insc_cod) {
        //recibe de la Vista todos los atributos como String, luego los valida y castea con los setters
        try {
            this.setAlu_dni(alu_dni);
            this.setAlu_nombre(alu_nombre);
            this.setAlu_apellido(alu_apellido);
            this.setAlu_fec_nac(alu_fec_nac);
            this.setAlu_domicilio(alu_domicilio);
            this.setAlu_telefono(alu_telefono);
            this.setAlu_insc_cod(alu_insc_cod);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Alumno_Modelo(Long alu_dni, String alu_nombre, String alu_apellido, String alu_fec_nac, String alu_domicilio, String alu_telefono, Long alu_insc_cod) {
        //recibe de DAO todos los atributos, castea las fechas
        this.alu_dni = alu_dni;
        this.alu_nombre = alu_nombre;
        this.alu_apellido = alu_apellido;
        this.alu_fec_nac = Casteo.DateOrNullValue(alu_fec_nac);
        this.alu_domicilio = alu_domicilio;
        this.alu_telefono = alu_telefono;
        this.alu_insc_cod = alu_insc_cod;
    }

    public Alumno_Modelo() {
    }

    public long getAlu_dni() {
        return alu_dni;
    }

    public String getAlu_nombre() {
        return alu_nombre;
    }

    public String getAlu_apellido() {
        return alu_apellido;
    }

    public String getAlu_fec_nac() {
        return alu_fec_nac;
    }

    public String getAlu_fec_nacSQL() { //Alumno_DAO pide formato yyyy-mm-dd
        return Casteo.Date_a_DateSQLOrNullValue(alu_fec_nac);
    }

    public String getAlu_domicilio() {
        return alu_domicilio;
    }

    public String getAlu_telefono() {
        return alu_telefono;
    }

    public Long getAlu_insc_cod() {
        return alu_insc_cod;
    }

    //Setters hacen Casteos, Validaciones y devuelven mensajes de error
    public void setAlu_dni(String alu_dni) throws Exception {
        try {
            this.alu_dni = Long.valueOf(alu_dni);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DNI error. " + e.getMessage());
            throw e;
        }
    }

    public void setAlu_nombre(String alu_nombre) throws Exception {
        if (alu_nombre.length() > ALU_NOMBRE_MAX) {
            JOptionPane.showMessageDialog(null, "Nombre tiene mas de " + ALU_NOMBRE_MAX + " caracteres");
            throw new Exception();
        }
        if (alu_nombre.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Nombre no puede estar vacio");

            throw new Exception();
        }
        this.alu_nombre = alu_nombre;
    }

    public void setAlu_apellido(String alu_apellido) throws Exception {
        if (alu_apellido.length() > ALU_APELLIDO_MAX) {
            JOptionPane.showMessageDialog(null, "Apellido tiene mas de " + ALU_APELLIDO_MAX + " caracteres");
            throw new Exception();
        }
        if (alu_apellido.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Apellido no puede estar vacio");
            throw new Exception();
        }
        this.alu_apellido = alu_apellido;
    }

    public void setAlu_fec_nac(String alu_fec_nac) { //Vista pide formato dd/mm/yyyy
        this.alu_fec_nac = Casteo.DateOrNullValue(alu_fec_nac);
    }

    public void setAlu_domicilio(String alu_domicilio) throws Exception {
        if (alu_domicilio.length() > ALU_DOMICILIO_MAX) {
            JOptionPane.showMessageDialog(null, "Domicilio tiene mas de " + ALU_DOMICILIO_MAX + " caracteres");
            throw new Exception();
        }
        this.alu_domicilio = alu_domicilio;
    }

    public void setAlu_telefono(String alu_telefono) throws Exception {
        if (alu_telefono.length() > ALU_TELEFONO_MAX) {
            JOptionPane.showMessageDialog(null, "Telefono tiene mas de " + ALU_TELEFONO_MAX + " caracteres");
            throw new Exception();
        }
        this.alu_telefono = alu_telefono;
    }

    public void setAlu_insc_cod(Object alu_insc_cod) throws Exception {
        try {
            this.alu_insc_cod = Casteo.LongOrNullValue(alu_insc_cod);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Codigo de inscripcion invalido");
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

    public boolean crear_registro(Alumno_Modelo alumno) {
        if (objeto_dao.existe_registro(alumno)) {
            JOptionPane.showMessageDialog(null, "DNI ya existe");
            return false;
        }

        if (objeto_dao.crear_registro(alumno)) {
            JOptionPane.showMessageDialog(null, "Registro creado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al crear el registro");
        return false;
    }

    public boolean guardar_registro(Alumno_Modelo alumno) {
        if (objeto_dao.guardar_registro(alumno)) {
            JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al guardar el registro");
        return false;
    }

    public boolean quitar_registro(Alumno_Modelo alumno) {
        if (objeto_dao.quitar_registro(alumno)) {
            JOptionPane.showMessageDialog(null, "Alumno eliminado correctamente");
            return true;
        }
        JOptionPane.showMessageDialog(null, "Error al eliminar el alumno");
        return false;
    }

    public Set<String> listar_cod_insc() {
        return objeto_dao.listar_cod_insc();
    }

    public Object getCampo(int index) {
        switch(index){
            case ALU_DNI_POS:
                return alu_dni;
            case ALU_NOMBRE_POS:
                return alu_nombre;
            case ALU_APELLIDO_POS:
                return alu_apellido;
            case ALU_FEC_NAC_POS:
                return alu_fec_nac;
            case ALU_DOMICILIO_POS:
                return alu_domicilio;
            case ALU_TELEFONO_POS:
                return alu_telefono;
            case ALU_INSC_COD_POS:
                return alu_insc_cod;                
        }
        return null;
}

}

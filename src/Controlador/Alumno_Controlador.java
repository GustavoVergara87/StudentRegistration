//@author Gustavo
package Controlador;

import Modelo.Alumno_Modelo;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.table.DefaultTableModel;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTable;

public class Alumno_Controlador implements ActionListener, MouseListener, FocusListener, KeyListener {

    private Alumno_Modelo alumno_modelo;
    private Vista vista;

    public Alumno_Controlador(Alumno_Modelo modelo, Vista vista) {
        this.alumno_modelo = modelo;
        this.vista = vista;

        tabla_llenar();
        tabla_selecionar(null);

        escucharObjetos();

        //Quita el editor de la tabla al hacer dobleclick
        this.vista.table_alu.setDefaultEditor(Object.class, null);
    }

    public void escucharObjetos() {
        this.vista.btn_alu_Eliminar.addActionListener(this);
        this.vista.btn_alu_Guardar.addActionListener(this);
        this.vista.tbtn_alu_Nuevo.addActionListener(this);
        this.vista.ftf_alu_fec_nac.addFocusListener(this);
        this.vista.table_alu.addKeyListener(this);
        this.vista.table_alu.addMouseListener(this);
        this.vista.cbb_alu_insc_cod.addActionListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Alumno_Modelo.Titulos);
        ArrayList<Alumno_Modelo> alumnos = this.alumno_modelo.listar_registros();

        Object[] fila_de_datos = new Object[7];

        if (alumnos.size() > 0) {
            for (Alumno_Modelo alumno : alumnos) {
                fila_de_datos[Alumno_Modelo.ALU_DNI_POS] = alumno.getAlu_dni();
                fila_de_datos[Alumno_Modelo.ALU_NOMBRE_POS] = alumno.getAlu_nombre();
                fila_de_datos[Alumno_Modelo.ALU_APELLIDO_POS] = alumno.getAlu_apellido();
                fila_de_datos[Alumno_Modelo.ALU_FEC_NAC_POS] = alumno.getAlu_fec_nac();
                fila_de_datos[Alumno_Modelo.ALU_DOMICILIO_POS] = alumno.getAlu_domicilio();
                fila_de_datos[Alumno_Modelo.ALU_TELEFONO_POS] = alumno.getAlu_telefono();
                fila_de_datos[Alumno_Modelo.ALU_INSC_COD_POS] = alumno.getAlu_insc_cod();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_alu.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_alu.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_alu.setModel(modeloTabla);
    }

    public void cuadros_llenar(Object alu_dni, Object alu_nombre, Object alu_apellido, Object alu_fec_nac, Object alu_domicilio, Object alu_telefono, Object alu_insc_cod) {

        this.vista.txt_alu_dni.setText(alu_dni.toString());
        this.vista.txt_alu_nombre.setText(alu_nombre.toString());
        this.vista.txt_alu_apellido.setText(alu_apellido.toString());
        this.vista.ftf_alu_fec_nac.setValue(Comunes.DateSQL_a_DateOrNull(alu_fec_nac)); //Si no se modifica el valor y solo el texto, trae el ultimo valor valido cuando se ingresa uno invalido
        this.vista.txt_alu_domicilio.setText(alu_domicilio.toString());
        this.vista.txt_alu_telefono.setText(alu_telefono.toString());
        cbb_alu_insc_cod_llenar(alu_insc_cod);

    }

    public boolean cuadros_a_modelo() {
        //Set_Alumno_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.alumno_modelo.Alumno_Modelo_ValidarCastearSetear(
                (this.vista.txt_alu_dni.getText()),
                this.vista.txt_alu_nombre.getText(),
                this.vista.txt_alu_apellido.getText(),
                this.vista.ftf_alu_fec_nac.getText(),
                this.vista.txt_alu_domicilio.getText(),
                this.vista.txt_alu_telefono.getText(),
                this.vista.cbb_alu_insc_cod.getSelectedItem());
    }

    public void cbb_alu_insc_cod_llenar(Object clave_seleccionada) {
        Comunes.ClavesForaneasToComboBox(this.vista.cbb_alu_insc_cod, this.alumno_modelo.listar_cod_insc(), clave_seleccionada);
    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "", "", "", "", "");
        cbb_alu_insc_cod_llenar("");
        this.vista.txt_alu_dni.setEditable(false);
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_alu.getSelectedRow();
        if (fila > -1 && this.vista.table_alu.isEnabled()) {
            cuadros_llenar(
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_DNI_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_NOMBRE_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_APELLIDO_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_FEC_NAC_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_DOMICILIO_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_TELEFONO_POS),
                    this.vista.table_alu.getValueAt(fila, Alumno_Modelo.ALU_INSC_COD_POS));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Crear
        if (e.getSource().equals(this.vista.tbtn_alu_Nuevo)) {
            if (this.vista.tbtn_alu_Nuevo.isSelected()) {
                cuadros_limpiar();
                this.vista.table_alu.setEnabled(false);
                this.vista.txt_alu_dni.setEditable(true);
                this.vista.txt_alu_dni.requestFocus();
            } else {
                this.vista.table_alu.setEnabled(true);
                this.vista.txt_alu_dni.setEditable(false);
                tabla_a_cuadros();
            }
            //Guardar
        } else if (e.getSource().equals(this.vista.btn_alu_Guardar)) {
            if (this.vista.tbtn_alu_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.alumno_modelo.crear_registro(alumno_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_alu_Nuevo.setSelected(false);
                        this.vista.table_alu.setEnabled(true);
                        tabla_selecionar(alumno_modelo); //Selecciona en la tabla el modelo recien creado
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.alumno_modelo.guardar_registro(alumno_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(alumno_modelo);
                    }
                }
            }
            //Eliminar    
        } else if (e.getSource().equals(this.vista.btn_alu_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.alumno_modelo.quitar_registro(alumno_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_alu)) {
            tabla_a_cuadros();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(this.vista.ftf_alu_fec_nac)) {
            //Evita que cuando se deja un valor nulo en un formatedtext tome el anterior
            if (this.vista.ftf_alu_fec_nac.getText().trim().equals("")) {
                this.vista.ftf_alu_fec_nac.setValue(null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(this.vista.table_alu)) {
            tabla_a_cuadros();
        }
    }

    public void tabla_selecionar(Alumno_Modelo modelo) {
        JTable tabla = this.vista.table_alu;
        if (modelo == null && tabla.getRowCount() > 0) {
            tabla.setRowSelectionInterval(0, 0);
            tabla_a_cuadros();
            return;
        }
        filas:
        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            for (int campo = 0; campo < 1; campo++) {
                if (!tabla.getValueAt(fila, campo).toString().equals(modelo.getCampo(campo).toString())) {
                    continue filas; //Si alguna condicion no se cumple pasa a la siguiente fila
                }
            }
            tabla.setRowSelectionInterval(fila, fila);
            tabla_a_cuadros();
            return;
        }
    }

}

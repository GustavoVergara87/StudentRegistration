//@author Gustavo
package Controlador;

import Modelo.Profesor_Modelo;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Profesor_Controlador implements ActionListener, MouseListener, FocusListener, KeyListener {

    private Profesor_Modelo profesor_modelo;
    private Vista vista;

    public Profesor_Controlador(Profesor_Modelo modelo, Vista vista) {
        this.profesor_modelo = modelo;
        this.vista = vista;

        this.tabla_llenar();

        tabla_selecionar(null);

        escucharObjetos();

        this.vista.table_prof.setDefaultEditor(Object.class, null);

    }

    public void escucharObjetos() {
        this.vista.btn_prof_Eliminar.addActionListener(this);
        this.vista.btn_prof_Guardar.addActionListener(this);
        this.vista.tbtn_prof_Nuevo.addActionListener(this);
        this.vista.ftf_prof_fec_nac.addFocusListener(this);
        this.vista.table_prof.addKeyListener(this);
        this.vista.table_prof.addMouseListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Profesor_Modelo.Titulos);
        ArrayList<Profesor_Modelo> profesores = this.profesor_modelo.listar_registros();

        Object[] fila_de_datos = new Object[7];
        if (profesores.size() > 0) {
            for (Profesor_Modelo profesor : profesores) {
                fila_de_datos[Profesor_Modelo.PROF_DNI_POS] = profesor.getProf_dni();
                fila_de_datos[Profesor_Modelo.PROF_NOMBRE_POS] = profesor.getProf_nombre();
                fila_de_datos[Profesor_Modelo.PROF_APELLIDO_POS] = profesor.getProf_apellido();
                fila_de_datos[Profesor_Modelo.PROF_FEC_NAC_POS] = profesor.getProf_fec_nac();
                fila_de_datos[Profesor_Modelo.PROF_DOMICILIO_POS] = profesor.getProf_domicilio();
                fila_de_datos[Profesor_Modelo.PROF_TELEFONO_POS] = profesor.getProf_telefono();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_prof.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_prof.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_prof.setModel(modeloTabla);
    }

    public boolean cuadros_a_modelo() {
        //Set_Profesor_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.profesor_modelo.Profesor_Modelo_ValidarCastearSetear(
                (this.vista.txt_prof_dni.getText()),
                this.vista.txt_prof_nombre.getText(),
                this.vista.txt_prof_apellido.getText(),
                this.vista.ftf_prof_fec_nac.getText(),
                this.vista.txt_prof_domicilio.getText(),
                this.vista.txt_prof_telefono.getText());
    }

    public void cuadros_llenar(Object prof_dni, Object prof_nombre, Object prof_apellido, Object prof_fec_nac, Object prof_domicilio, Object prof_telefono) {
        this.vista.txt_prof_dni.setText(prof_dni.toString());
        this.vista.txt_prof_nombre.setText(prof_nombre.toString());
        this.vista.txt_prof_apellido.setText(prof_apellido.toString());
        this.vista.ftf_prof_fec_nac.setValue(Comunes.DateSQL_a_DateOrNull(prof_fec_nac));
        this.vista.txt_prof_domicilio.setText(prof_domicilio.toString());
        this.vista.txt_prof_telefono.setText(prof_telefono.toString());
    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "", "", "", "");
         this.vista.txt_prof_dni.setEditable(false);
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_prof.getSelectedRow();
        if (fila > -1 && this.vista.table_prof.isEnabled()) {
            cuadros_llenar(
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_DNI_POS),
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_NOMBRE_POS),
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_APELLIDO_POS),
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_FEC_NAC_POS),
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_DOMICILIO_POS),
                    this.vista.table_prof.getValueAt(fila, Profesor_Modelo.PROF_TELEFONO_POS));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(this.vista.tbtn_prof_Nuevo)) {
            if (this.vista.tbtn_prof_Nuevo.isSelected()) {
                cuadros_limpiar();
                this.vista.txt_prof_dni.setEditable(true);
                this.vista.table_prof.setEnabled(false);
                this.vista.txt_prof_dni.requestFocus();
            } else {
                this.vista.table_prof.setEnabled(true);
                this.vista.txt_prof_dni.setEditable(false);
                tabla_a_cuadros();
            }
        } else if (e.getSource().equals(this.vista.btn_prof_Guardar)) {
            if (this.vista.tbtn_prof_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.profesor_modelo.crear_registro(profesor_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_prof_Nuevo.setSelected(false);
                        this.vista.table_prof.setEnabled(true);
                        tabla_selecionar(profesor_modelo);
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.profesor_modelo.guardar_registro(profesor_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(profesor_modelo);
                    }
                }
            }
        } else if (e.getSource().equals(this.vista.btn_prof_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.profesor_modelo.quitar_registro(profesor_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_prof)) {
            tabla_a_cuadros();
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
    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(this.vista.ftf_prof_fec_nac)) {
            //Resuelve el problema que cuando se deja un valor invalido en un formatedtext retoma su valor anterior y es imposible dejarlo vacio
            if (this.vista.ftf_prof_fec_nac.getText().trim().equals("")) {
                this.vista.ftf_prof_fec_nac.setValue(null);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(this.vista.table_prof)) {
            tabla_a_cuadros();
        }
    }

    public void tabla_selecionar(Profesor_Modelo modelo) {
        JTable tabla = this.vista.table_prof;
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

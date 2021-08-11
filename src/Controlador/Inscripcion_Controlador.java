//@author Gustavo
package Controlador;

import Modelo.Inscripcion_Modelo;
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

public class Inscripcion_Controlador implements ActionListener, MouseListener, FocusListener, KeyListener {

    private Inscripcion_Modelo inscripcion_modelo;
    private Vista vista;

    public Inscripcion_Controlador(Inscripcion_Modelo modelo, Vista vista) {
        this.inscripcion_modelo = modelo;
        this.vista = vista;

        this.tabla_llenar();

        tabla_selecionar(null);

        escucharObjetos();

        this.vista.table_insc.setDefaultEditor(Object.class, null); //Quita el editor de la tabla al hacer dobleclick
    }

    public void escucharObjetos() {
        this.vista.btn_insc_Eliminar.addActionListener(this);
        this.vista.btn_insc_Guardar.addActionListener(this);
        this.vista.tbtn_insc_Nuevo.addActionListener(this);
        this.vista.ftf_insc_fecha.addFocusListener(this);
        this.vista.table_insc.addKeyListener(this);
        this.vista.table_insc.addMouseListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Inscripcion_Modelo.Titulos);
        ArrayList<Inscripcion_Modelo> inscripciones = this.inscripcion_modelo.listar_registros();

        Object[] fila_de_datos = new Object[4];

        if (inscripciones.size() > 0) {
            for (Inscripcion_Modelo inscripcion : inscripciones) {
                fila_de_datos[Inscripcion_Modelo.INSC_COD_POS] = inscripcion.getInsc_cod();
                fila_de_datos[Inscripcion_Modelo.INSC_NOMBRE_POS] = inscripcion.getInsc_nombre();
                fila_de_datos[Inscripcion_Modelo.INSC_FECHA_POS] = inscripcion.getInsc_fecha();
                fila_de_datos[Inscripcion_Modelo.INSC_CAR_COD_POS] = inscripcion.getInsc_car_cod();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_insc.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_insc.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_insc.setModel(modeloTabla);
    }

    public boolean cuadros_a_modelo() {
        //Set_Inscripcion_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.inscripcion_modelo.Inscripcion_Modelo_ValidarCastearSetear(
                (this.vista.txt_insc_cod.getText()),
                this.vista.txt_insc_nombre.getText(),
                this.vista.ftf_insc_fecha.getText(),
                this.vista.cbb_insc_car_cod.getSelectedItem());
    }

    public void cuadros_llenar(Object insc_cod, Object insc_nombre, Object insc_fecha, Object insc_car_cod) {
        this.vista.txt_insc_cod.setText(insc_cod.toString());
        this.vista.txt_insc_nombre.setText(insc_nombre.toString());
        this.vista.ftf_insc_fecha.setValue(Comunes.DateSQL_a_DateOrNull(insc_fecha));
        cbb_insc_insc_cod_llenar(insc_car_cod);

    }

    public void cbb_insc_insc_cod_llenar(Object clave_seleccionada) {
        Comunes.ClavesForaneasToComboBox(this.vista.cbb_insc_car_cod, this.inscripcion_modelo.listar_cod_insc(), clave_seleccionada);
    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "", "");
        cbb_insc_insc_cod_llenar("");
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_insc.getSelectedRow();
        if (fila > -1 && this.vista.table_insc.isEnabled()) {
            cuadros_llenar(
                    this.vista.table_insc.getValueAt(fila, Inscripcion_Modelo.INSC_COD_POS),
                    this.vista.table_insc.getValueAt(fila, Inscripcion_Modelo.INSC_NOMBRE_POS),
                    this.vista.table_insc.getValueAt(fila, Inscripcion_Modelo.INSC_FECHA_POS),
                    this.vista.table_insc.getValueAt(fila, Inscripcion_Modelo.INSC_CAR_COD_POS));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(this.vista.tbtn_insc_Nuevo)) {
            if (this.vista.tbtn_insc_Nuevo.isSelected()) {
                this.vista.table_insc.setEnabled(false);
                cuadros_limpiar();
                this.vista.txt_insc_cod.setText(inscripcion_modelo.obtener_autoincremeto().toString());
                this.vista.txt_insc_nombre.requestFocus();
            } else {
                this.vista.table_insc.setEnabled(true);
                tabla_a_cuadros();
            }
        } else if (e.getSource().equals(this.vista.btn_insc_Guardar)) {
            if (this.vista.tbtn_insc_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.inscripcion_modelo.crear_registro(inscripcion_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_insc_Nuevo.setSelected(false);
                        this.vista.table_insc.setEnabled(true);
                        tabla_selecionar(inscripcion_modelo);
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.inscripcion_modelo.guardar_registro(inscripcion_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(inscripcion_modelo);
                    }
                }
            }
        } else if (e.getSource().equals(this.vista.btn_insc_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.inscripcion_modelo.quitar_registro(inscripcion_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_insc)) {
            tabla_a_cuadros();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(this.vista.ftf_insc_fecha)) {
            //Cuando se deja un valor invalido en un formatedte
            if (this.vista.ftf_insc_fecha.getText().trim().equals("")) {
                this.vista.ftf_insc_fecha.setValue(null);
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
        if (e.getSource().equals(this.vista.table_insc)) {
            tabla_a_cuadros();
        }
    }

    public void tabla_selecionar(Inscripcion_Modelo modelo) {
        JTable tabla = this.vista.table_insc;
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

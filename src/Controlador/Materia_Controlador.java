//@author Gustavo
package Controlador;

import Modelo.Materia_Modelo;
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

public class Materia_Controlador implements ActionListener, MouseListener, FocusListener, KeyListener {

    private Materia_Modelo materia_modelo;
    private Vista vista;

    public Materia_Controlador(Materia_Modelo modelo, Vista vista) {
        this.materia_modelo = modelo;
        this.vista = vista;

        this.tabla_llenar();

        tabla_selecionar(null);

        escucharObjetos();

        this.vista.table_mat.setDefaultEditor(Object.class, null); //Quita el editor de la tabla al hacer dobleclick
    }

    public void escucharObjetos() {
        this.vista.btn_mat_Eliminar.addActionListener(this);
        this.vista.btn_mat_Guardar.addActionListener(this);
        this.vista.tbtn_mat_Nuevo.addActionListener(this);
        this.vista.table_mat.addKeyListener(this);
        this.vista.table_mat.addMouseListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Materia_Modelo.Titulos);
        ArrayList<Materia_Modelo> materias = this.materia_modelo.listar_registros();

        Object[] fila_de_datos = new Object[3];

        if (materias.size() > 0) {
            for (Materia_Modelo materia : materias) {
                fila_de_datos[Materia_Modelo.MAT_COD_POS] = materia.getMat_cod();
                fila_de_datos[Materia_Modelo.MAT_NOMBRE_POS] = materia.getMat_nombre();
                fila_de_datos[Materia_Modelo.MAT_PROF_DNI_POS] = materia.getMat_prof_dni();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_mat.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_mat.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_mat.setModel(modeloTabla);
    }

    public boolean cuadros_a_modelo() {
        //Set_Alumno_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.materia_modelo.Curso_Modelo_ValidarCastearSetear(
                (this.vista.txt_mat_cod.getText()),
                this.vista.txt_mat_nombre.getText(),
                this.vista.cbb_mat_prof_dni.getSelectedItem().toString());
    }

    public void cuadros_llenar(Object mat_cod, Object mat_nombre, Object mat_prof_dni) {
        this.vista.txt_mat_cod.setText(mat_cod.toString());
        this.vista.txt_mat_nombre.setText(mat_nombre.toString());
        cbb_mat_prof_dni_llenar(mat_prof_dni.toString());

    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "");
        cbb_mat_prof_dni_llenar("");
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_mat.getSelectedRow();
        if (fila > -1 && this.vista.table_mat.isEnabled()) {
            cuadros_llenar(this.vista.table_mat.getValueAt(fila, materia_modelo.MAT_COD_POS),
                    this.vista.table_mat.getValueAt(fila, materia_modelo.MAT_NOMBRE_POS),
                    this.vista.table_mat.getValueAt(fila, materia_modelo.MAT_PROF_DNI_POS));
        }
    }

    public void cbb_mat_prof_dni_llenar(Object clave_seleccionada) {
        Comunes.ClavesForaneasToComboBox(this.vista.cbb_mat_prof_dni, this.materia_modelo.listar_mat_prof_dni(), clave_seleccionada);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.tbtn_mat_Nuevo)) {
            if (this.vista.tbtn_mat_Nuevo.isSelected()) {
                this.vista.table_mat.setEnabled(false);
                cuadros_limpiar();
                this.vista.txt_mat_cod.setText(materia_modelo.obtener_autoincremeto().toString());
                this.vista.txt_mat_nombre.requestFocus();
            } else {
                this.vista.table_mat.setEnabled(true);
                tabla_a_cuadros();
            }
        } else if (e.getSource().equals(this.vista.btn_mat_Guardar)) {
            if (this.vista.tbtn_mat_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.materia_modelo.nuevo_registro(materia_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_mat_Nuevo.setSelected(false);
                        this.vista.table_mat.setEnabled(true);
                        tabla_selecionar(materia_modelo);
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.materia_modelo.guardar_registro(materia_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(materia_modelo);
                    }
                }
            }
        } else if (e.getSource().equals(this.vista.btn_mat_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.materia_modelo.quitar_registro(materia_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_mat)) {
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(this.vista.table_mat)) {
            tabla_a_cuadros();
        }

    }

    public void tabla_selecionar(Materia_Modelo modelo) {
        JTable tabla = this.vista.table_mat;
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

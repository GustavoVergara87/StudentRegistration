//@author Gustavo
package Controlador;

import Modelo.Cursado_Modelo;
import Vista.Vista;
import Controlador.Comunes;
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

public class Cursado_Controlador implements ActionListener, MouseListener, FocusListener, KeyListener {

    private Cursado_Modelo cursado_modelo;
    private Vista vista;

    public Cursado_Controlador(Cursado_Modelo modelo, Vista vista) {
        this.cursado_modelo = modelo;
        this.vista = vista;

        this.tabla_llenar();

        tabla_selecionar(null);

        escucharObjetos();

        this.vista.table_cur.setDefaultEditor(Object.class, null);
    }

    public void escucharObjetos() {
        this.vista.btn_cur_Eliminar.addActionListener(this);
        this.vista.btn_cur_Guardar.addActionListener(this);
        this.vista.tbtn_cur_Nuevo.addActionListener(this);
        this.vista.table_cur.addKeyListener(this);
        this.vista.table_cur.addMouseListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Cursado_Modelo.Titulos);
        ArrayList<Cursado_Modelo> cursados = this.cursado_modelo.listar_registros();

        Object[] fila_de_datos = new Object[3];

        if (cursados.size() > 0) {
            for (Cursado_Modelo cursado : cursados) {
                fila_de_datos[Cursado_Modelo.CUR_ALU_DNI_POS] = cursado.getCur_alu_dni();
                fila_de_datos[Cursado_Modelo.CUR_MAT_POS] = cursado.getCur_mat_cod();
                fila_de_datos[Cursado_Modelo.CUR_NOTA_POS] = cursado.getCur_nota();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_cur.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_cur.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_cur.setModel(modeloTabla);
    }

    public boolean cuadros_a_modelo() {
        //Set_Alumno_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.cursado_modelo.Curso_Modelo_ValidarCastearSetear(
                (this.vista.cbb_cur_alu_dni.getSelectedItem().toString()),
                this.vista.cbb_cur_mat_cod.getSelectedItem().toString(),
                this.vista.txt_cur_nota.getText());
    }

    public void cuadros_llenar(Object cur_alu_dni, Object cur_mat_cod, Object cur_nota) {
        cbb_cur_alu_dni_llenar(cur_alu_dni);
        cbb_cur_mat_cod_llenar(cur_mat_cod);
        this.vista.txt_cur_nota.setText(cur_nota.toString());
    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "");
        cbb_cur_alu_dni_llenar("");
        cbb_cur_mat_cod_llenar("");
        this.vista.cbb_cur_alu_dni.setEnabled(false);
        this.vista.cbb_cur_mat_cod.setEnabled(false);
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_cur.getSelectedRow();
        if (fila > -1 && this.vista.table_cur.isEnabled()) {
            cuadros_llenar(this.vista.table_cur.getValueAt(this.vista.table_cur.getSelectedRow(), cursado_modelo.CUR_ALU_DNI_POS),
                    this.vista.table_cur.getValueAt(this.vista.table_cur.getSelectedRow(), cursado_modelo.CUR_MAT_POS),
                    this.vista.table_cur.getValueAt(this.vista.table_cur.getSelectedRow(), cursado_modelo.CUR_NOTA_POS));
        }
    }

    public void cbb_cur_mat_cod_llenar(Object clave_seleccionada) {
        Comunes.ClavesForaneasToComboBox(this.vista.cbb_cur_mat_cod, this.cursado_modelo.listar_cur_mat_cod(), clave_seleccionada);
    }

    public void cbb_cur_alu_dni_llenar(Object clave_seleccionada) {
        Comunes.ClavesForaneasToComboBox(this.vista.cbb_cur_alu_dni, this.cursado_modelo.listar_cur_alu_dni(), clave_seleccionada);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.tbtn_cur_Nuevo)) {
            if (this.vista.tbtn_cur_Nuevo.isSelected()) {
                this.vista.table_cur.setEnabled(false);
                cuadros_limpiar();
                this.vista.cbb_cur_alu_dni.setEnabled(true);
                this.vista.cbb_cur_mat_cod.setEnabled(true);
                this.vista.cbb_cur_alu_dni.requestFocus();
            } else {
                this.vista.table_cur.setEnabled(true);
                this.vista.cbb_cur_alu_dni.setEnabled(false);
                this.vista.cbb_cur_mat_cod.setEnabled(false);
                tabla_a_cuadros();
            }
        } else if (e.getSource().equals(this.vista.btn_cur_Guardar)) {
            if (this.vista.tbtn_cur_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.cursado_modelo.crear_registro(cursado_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_cur_Nuevo.setSelected(false);
                        this.vista.table_cur.setEnabled(true);
                        tabla_selecionar(cursado_modelo);
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.cursado_modelo.guardar_registro(cursado_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(cursado_modelo);
                    }
                }
            }
        } else if (e.getSource().equals(this.vista.btn_cur_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.cursado_modelo.quitar_registro(cursado_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_cur)) {
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
        if (e.getSource().equals(this.vista.table_cur)) {
            tabla_a_cuadros();
        }
    }

    public void tabla_selecionar(Cursado_Modelo modelo) {
        JTable tabla = this.vista.table_cur;
        if (modelo == null && tabla.getRowCount() > 0) {
            tabla.setRowSelectionInterval(0, 0);
            tabla_a_cuadros();
            return;
        }

        filas:
        for (int fila = 0; fila < tabla.getRowCount(); fila++) {

            for (int campo = 0; campo < 2; campo++) {
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

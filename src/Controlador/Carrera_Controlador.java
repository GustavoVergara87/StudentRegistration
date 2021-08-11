//@author Gustavo
package Controlador;

import Modelo.Carrera_Modelo;
import Vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Carrera_Controlador implements ActionListener, MouseListener, KeyListener {

    private Carrera_Modelo carrera_modelo;
    private Vista vista;

    public Carrera_Controlador(Carrera_Modelo modelo, Vista vista) {
        this.carrera_modelo = modelo;
        this.vista = vista;

        this.tabla_llenar();

        tabla_selecionar(null);

        escucharObjetos();

        this.vista.table_car.setDefaultEditor(Object.class, null); //Quita el editor de la tabla al hacer dobleclick
    }

    public void escucharObjetos() {
        this.vista.btn_car_Eliminar.addActionListener(this);
        this.vista.btn_car_Guardar.addActionListener(this);
        this.vista.tbtn_car_Nuevo.addActionListener(this);
        this.vista.table_car.addKeyListener(this);
        this.vista.table_car.addMouseListener(this);
    }

    public void tabla_llenar() {
        DefaultTableModel modelo_tabla = new DefaultTableModel(null, Carrera_Modelo.Titulos);
        ArrayList<Carrera_Modelo> carreras = this.carrera_modelo.listar_registros();

        Object[] fila_de_datos = new Object[4];

        if (carreras.size() > 0) {
            for (Carrera_Modelo carrera : carreras) {
                fila_de_datos[Carrera_Modelo.CAR_COD_POS] = carrera.getCar_cod();
                fila_de_datos[Carrera_Modelo.CAR_NOMBRE_POS] = carrera.getCar_nombre();
                fila_de_datos[Carrera_Modelo.CAR_DURACION_POS] = carrera.getCar_duracion();
                modelo_tabla.addRow(fila_de_datos);
            }
        }

        this.vista.table_car.setModel(modelo_tabla);

    }

    public void tabla_limpiar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.vista.table_car.getModel();
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        this.vista.table_car.setModel(modeloTabla);
    }

    public boolean cuadros_a_modelo() {
        //Set_Carrera_Modelo hace los casteos y validaciones. Devuelve mensaje de error si algo falló.
        return this.carrera_modelo.Carrera_Modelo_ValidarCastearSetear(
                (this.vista.txt_car_cod.getText()),
                this.vista.txt_car_nombre.getText(),
                this.vista.txt_car_duracion.getText());
    }

    public void cuadros_llenar(Object car_cod, Object car_nombre, Object car_duracion) {
        this.vista.txt_car_cod.setText(car_cod.toString());
        this.vista.txt_car_nombre.setText(car_nombre.toString());
        this.vista.txt_car_duracion.setText(car_duracion.toString());
    }

    public void cuadros_limpiar() {
        cuadros_llenar("", "", "");
        this.vista.txt_car_cod.setEditable(false);
    }

    public void tabla_a_cuadros() {
        int fila = this.vista.table_car.getSelectedRow();
        if (fila > -1 && this.vista.table_car.isEnabled()) {
            cuadros_llenar(
                    this.vista.table_car.getValueAt(fila, Carrera_Modelo.CAR_COD_POS),
                    this.vista.table_car.getValueAt(fila, Carrera_Modelo.CAR_NOMBRE_POS),
                    this.vista.table_car.getValueAt(fila, Carrera_Modelo.CAR_DURACION_POS));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(this.vista.tbtn_car_Nuevo)) {
            if (this.vista.tbtn_car_Nuevo.isSelected()) {
                this.vista.table_car.setEnabled(false);
                cuadros_limpiar();
                this.vista.txt_car_cod.setText(carrera_modelo.obtener_autoincremeto().toString());
                this.vista.txt_car_nombre.requestFocus();
            } else {
                this.vista.table_car.setEnabled(true);
                tabla_a_cuadros();
            }
        } else if (e.getSource().equals(this.vista.btn_car_Guardar)) {
            if (this.vista.tbtn_car_Nuevo.isSelected()) {
                if (cuadros_a_modelo()) {
                    if (this.carrera_modelo.crear_registro(carrera_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        this.vista.tbtn_car_Nuevo.setSelected(false);
                        this.vista.table_car.setEnabled(true);
                        tabla_selecionar(carrera_modelo);
                    }
                }
            } else {
                if (cuadros_a_modelo()) {
                    if (this.carrera_modelo.guardar_registro(carrera_modelo)) {
                        tabla_llenar();
                        cuadros_limpiar();
                        tabla_selecionar(carrera_modelo);
                    }
                }
            }
        } else if (e.getSource().equals(this.vista.btn_car_Eliminar)) {
            if (cuadros_a_modelo()) {
                if (this.carrera_modelo.quitar_registro(carrera_modelo)) {
                    tabla_llenar();
                    cuadros_limpiar();
                    tabla_selecionar(null);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getSource().equals(this.vista.table_car)) {
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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(this.vista.table_car)) {
            tabla_a_cuadros();
        }
    }

    public void tabla_selecionar(Carrera_Modelo modelo) {
        JTable tabla = this.vista.table_car;
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

//@author Gustavo
package Controlador;

import Vista.Vista;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Aspecto_Controlador implements MouseListener {

    private Vista vista;

    public Aspecto_Controlador(Vista vista) {
        this.vista = vista;

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            SwingUtilities.updateComponentTreeUI(vista);
            vista.pack();
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }

        this.vista.jLabel_titulo.addMouseListener(this);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(this.vista.jLabel_titulo)) {
            if (UIManager.getLookAndFeel().getClass().getName().equals("javax.swing.plaf.nimbus.NimbusLookAndFeel")) {

                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                    SwingUtilities.updateComponentTreeUI(vista);
                    vista.pack();
                } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
                    System.out.println("unsupportedLookAndFeelException");
                }

            } else if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                    SwingUtilities.updateComponentTreeUI(vista);
                    vista.pack();
                } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
                    System.out.println("unsupportedLookAndFeelException");
                }

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

}

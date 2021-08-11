//@author Gustavo
package Controlador;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComboBox;

public class Comunes {

    public static String StringValue(Object objeto) {
        try {
            return String.valueOf(objeto);
        } catch (Exception e) {
        }
        return "";
    }

    public static Date DateSQL_a_DateOrNull(Object dateSQL) {
        //Devuelve una fecha en formato dd/mm/yyyy o un nulo
        try {
            String StrDateSQL = dateSQL.toString();
            if (StrDateSQL.contains("/")) {
                //dd/mm/yyyy -> mm/dd/yyyy
                String tmp[] = StrDateSQL.split("/");
                return (new Date(tmp[1] + "/" + tmp[0] + "/" + tmp[2]));
            } else if (StrDateSQL.contains("-")) {
                //yyyy-mm-dd -> mm/dd/yyyy
                String tmp[] = StrDateSQL.split("-");
                return (new Date(tmp[1] + "/" + tmp[2] + "/" + tmp[0]));
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String DateSQLOrNullValue(String date) {
        //Devuelve una fecha en formato yyyy-mm-dd o un nulo
        try {
            if (date.contains("/")) {
                String tmp[] = date.split("/");
                return tmp[2] + "-" + tmp[1] + "-" + tmp[0];
            } else if (date.contains("-")) {
                String tmp[] = date.split("-");
                return tmp[0] + "-" + tmp[1] + "-" + tmp[2];
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void ClavesForaneasToComboBox(JComboBox cb, Set<String> claves_foraneas, Object clave_seleccionada) {
        //Object esta para contemplar el valor nulo. Puede no estar seleccionada ninguna clave
        cb.removeAllItems();
        Iterator<String> claves_foraneas_iterator = claves_foraneas.iterator();
        while (claves_foraneas_iterator.hasNext()) {
            cb.addItem(claves_foraneas_iterator.next());
        }

        //Si un alumno esta inscripto a un cursado y borramos el alumno, no va a apareceria en las claves_foraneas y se veria en combobox de cursado
        if (!claves_foraneas.contains(StringValue(clave_seleccionada))) {
            cb.addItem(StringValue(clave_seleccionada));
        }

        cb.setSelectedItem(StringValue(clave_seleccionada));
    }

}

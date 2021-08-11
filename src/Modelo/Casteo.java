//@author Gustavo
package Modelo;

public class Casteo {

    public static Float FloatOrNullValue(String s) {
        //Devuelve un Float o un nulo
        try {
            return Float.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long LongOrNullValue(Object s) {
        //Devuelve un Long o un nulo
        try {
            return Long.valueOf(StringValue(s));
        } catch (Exception e) {
            return null;
        }
    }

    public static String DateOrNullValue(String dateSQL) {
        //Devuelve una fecha en formato dd/mm/yyyy o un nulo
        try {
            if (dateSQL.contains("/")) {
                String tmp[] = dateSQL.split("/");
                return tmp[0] + "/" + tmp[1] + "/" + tmp[2];
            } else if (dateSQL.contains("-")) {
                String tmp[] = dateSQL.split("-");
                return tmp[2] + "/" + tmp[1] + "/" + tmp[0];
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String Date_a_DateSQLOrNullValue(String date) {
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

    public static String StringValue(Object objeto) {
        //Siempre devuelve una cadena, aun si el objeto es nulo
        try {
            return String.valueOf(objeto);
        } catch (Exception e) {
        }
        return "";
    }

}

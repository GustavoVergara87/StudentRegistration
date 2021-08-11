package Controlador;

//@author Gustavo
import Modelo.*;
import Vista.*;

public class Main {

    public static void main(String[] args) {
        Alumno_Modelo alumno_modelo = new Alumno_Modelo();
        Cursado_Modelo cursado_modelo = new Cursado_Modelo();
        Profesor_Modelo profesor_modelo = new Profesor_Modelo();
        Materia_Modelo materia_modelo = new Materia_Modelo();
        Inscripcion_Modelo inscripcion_modelo = new Inscripcion_Modelo();
        Carrera_Modelo carrera_modelo = new Carrera_Modelo();
        
        Vista vista = new Vista();

        Aspecto_Controlador aspecto_Controlador = new Aspecto_Controlador(vista);
        Alumno_Controlador alumno_controlador = new Alumno_Controlador(alumno_modelo, vista);
        Cursado_Controlador curso_controlador = new Cursado_Controlador(cursado_modelo, vista);
        Profesor_Controlador profesor_controlador = new Profesor_Controlador(profesor_modelo, vista);
        Materia_Controlador materia_controlador = new Materia_Controlador(materia_modelo, vista);
        Inscripcion_Controlador inscripcion_controlador = new Inscripcion_Controlador(inscripcion_modelo, vista);
        Carrera_Controlador carrera_controlador = new Carrera_Controlador(carrera_modelo, vista);
        
        vista.setVisible(true);

        
    }

}

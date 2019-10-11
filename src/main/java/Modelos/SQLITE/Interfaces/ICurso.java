package Modelos.SQLITE.Interfaces;

import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
import Servicios.Utils.ASAS;

public interface ICurso extends genericdomain<Curso, Integer> {
        Curso getcursoperAlumno(Alumno alumno);
        int getcursoIDbybachiller(String bachiller, String seccion, String grado);
        ASAS<String> getAllseccion();
        ASAS<String> getAllBachiller();
        ASAS<String> getAllGrado();
}

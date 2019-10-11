package Modelos.SQLITE.Interfaces;

import Controladores.Enums.BuscarType;
import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.ColegioEtc.Curso;
import Servicios.Utils.ASAS;

public interface IAlumno extends genericdomain<Alumno, String> {
    // mas metodos por agregarse
        void add(Alumno alumno, Curso curso);
        void deleteall(String promo);
        ASAS<Alumno> findby(String by, Curso curso, BuscarType buscarType, boolean toallfilter);
        boolean exists(Alumno alumno);
        ASAS<Alumno> getAlumnospercurso(Curso curso);
}

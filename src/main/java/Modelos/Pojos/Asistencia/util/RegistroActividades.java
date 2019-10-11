package Modelos.Pojos.Asistencia.util;

import Modelos.Pojos.Asistencia.AlumnoAsistencia.AlumnoAsistencia;
import Modelos.Pojos.Asistencia.ProfsorAsistencia.AsistenciaProfe;

public class RegistroActividades {

    private AsistenciaDiaria asistenciaDiaria;
    private AsistenciaManipulator<String, AlumnoAsistencia> AlumnoAsistenciaManipulator;
    private AsistenciaManipulator<String, AsistenciaProfe>  profeAsistenciaManipulator;
    public RegistroActividades() {
    }

    public AsistenciaDiaria getAsistenciaDiaria() {
        return asistenciaDiaria;
    }

    public void setAsistenciaDiaria(AsistenciaDiaria asistenciaDiaria) {
        this.asistenciaDiaria = asistenciaDiaria;
    }

    public AsistenciaManipulator<String, AlumnoAsistencia> getAlumnoAsistenciaManipulator() {
        return AlumnoAsistenciaManipulator;
    }

    public void setAlumnoAsistenciaManipulator(AsistenciaManipulator<String, AlumnoAsistencia> alumnoAsistenciaManipulator) {
        AlumnoAsistenciaManipulator = alumnoAsistenciaManipulator;
    }

    public AsistenciaManipulator<String, AsistenciaProfe> getProfeAsistenciaManipulator() {
        return profeAsistenciaManipulator;
    }

    public void setProfeAsistenciaManipulator(AsistenciaManipulator<String, AsistenciaProfe> profeAsistenciaManipulator) {
        this.profeAsistenciaManipulator = profeAsistenciaManipulator;
    }
}
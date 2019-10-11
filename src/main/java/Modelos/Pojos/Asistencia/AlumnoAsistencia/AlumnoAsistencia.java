package Modelos.Pojos.Asistencia.AlumnoAsistencia;

import Modelos.Pojos.ALumno.Alumno;
import Modelos.Pojos.Entidad.EntidadPersona;
import Servicios.Utils.ASAS;

import java.util.ArrayList;
import java.util.List;

public class AlumnoAsistencia extends EntidadPersona {
    private List<AlumnoHorarioAsistencia> HorarioAsistidos;
    private int id_asistencia;
    private int id_union;
    public AlumnoAsistencia(Alumno alumno){
        super(alumno.getNombres(), alumno.getApellidos(), alumno.getCi(), alumno.getSexo());
        HorarioAsistidos = new ArrayList<>();
    }
    public AlumnoAsistencia(Alumno alumno, int idunion){
        super(alumno.getNombres(), alumno.getApellidos(), alumno.getCi(), alumno.getSexo());
        HorarioAsistidos = new ArrayList<>();
        this.id_union = idunion;
    }
    public AlumnoAsistencia(Alumno alumno, int id_asistencia, int idunion){
        super(alumno.getNombres(), alumno.getApellidos(), alumno.getCi(), alumno.getSexo());
        HorarioAsistidos = new ArrayList<>();
        this.id_asistencia= id_asistencia;
        this.id_union = idunion;
    }

    public void add(AlumnoHorarioAsistencia toadd){
        HorarioAsistidos.add(toadd);
    }

    public ASAS<AlumnoHorarioAsistencia> getHorarioAsistidos(){
        return new ASAS<>(HorarioAsistidos);
    }

    public void setHorarioAsistidos(List<AlumnoHorarioAsistencia> asistidos){
        this.HorarioAsistidos = asistidos;
    }

    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public int getId_union() {
        return id_union;
    }

    public void setId_union(int id_union) {
        this.id_union = id_union;
    }
}

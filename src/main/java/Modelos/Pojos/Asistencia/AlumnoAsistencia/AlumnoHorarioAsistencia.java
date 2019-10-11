package Modelos.Pojos.Asistencia.AlumnoAsistencia;

import Modelos.Pojos.ColegioEtc.Horario;

public class AlumnoHorarioAsistencia{
    private String Asistencia;
    private String horarioAsistencia;
    private int ownid;
    private int id_asistencia;
    private int id_horario;

    public AlumnoHorarioAsistencia(Horario horario, String asistencia){
        this.Asistencia = asistencia;
        this.horarioAsistencia = horario.getHorarioInicio() + " - " + horario.getHorarioFin();
        this.id_horario = horario.getIdhorario();
    }

    public AlumnoHorarioAsistencia(Horario horario, String asistencia, int id_asistencia) {
        this.Asistencia = asistencia;
        this.horarioAsistencia = horario.getHorarioInicio() + " - " + horario.getHorarioFin();
        this.id_asistencia = id_asistencia;
        this.id_horario = horario.getIdhorario();
    }

    public AlumnoHorarioAsistencia(int ownid, Horario horario, String asistencia, int id_asistencia){
        this.Asistencia = asistencia;
        this.horarioAsistencia = horario.getHorarioInicio()+" - "+horario.getHorarioFin();
        this.id_asistencia = id_asistencia;
        this.ownid = ownid;
        this.id_horario = horario.getIdhorario();
    }

    public String getAsistencia() {
        return Asistencia;
    }

    public void setAsistencia(String asistencia) {
        Asistencia = asistencia;
    }

    public String getHorarioAsistencia() {
        return horarioAsistencia;
    }

    public void setHorarioAsistencia(String horarioAsistencia) {
        this.horarioAsistencia = horarioAsistencia;
    }

    public int getOwnid() {
        return ownid;
    }

    public void setOwnid(int ownid) {
        this.ownid = ownid;
    }

    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    @Override
    public String toString() {
        return horarioAsistencia + "   "+ Asistencia;
    }
}

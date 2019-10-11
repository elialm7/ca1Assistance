package Modelos.Pojos.Asistencia.ProfsorAsistencia;

import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.Pojos.ColegioEtc.Materia;

public class PresenciaProfe {

    private String horario;
    private String materia;
    private String presencia;

    private int ownid;
    private int id_materia;
    private int id_horario;
    private int id_asistencia;
    public PresenciaProfe(Horario horario, Materia materia, String presencia){
        this.horario = horario.getHorarioInicio() + " - "+horario.getHorarioFin();
        this.id_horario = horario.getIdhorario();
        this.id_materia = materia.getId_materia();
        this.materia   =   materia.getNombre();
        this.presencia = presencia;
    }
    public PresenciaProfe(Horario horario, Materia materia, String presencia, int ownid, int id_asistencia){
        this.horario = horario.getHorarioInicio() + " - "+horario.getHorarioFin();
        this.id_horario = horario.getIdhorario();
        this.id_materia = materia.getId_materia();
        this.materia   =   materia.getNombre();
        this.presencia = presencia;
        this.ownid = ownid;
        this.id_asistencia = id_asistencia;
    }


    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getPresencia() {
        return presencia;
    }

    public void setPresencia(String presencia) {
        this.presencia = presencia;
    }

    public int getOwnid() {
        return ownid;
    }

    public void setOwnid(int ownid) {
        this.ownid = ownid;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }



}

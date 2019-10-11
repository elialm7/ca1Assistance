package Modelos.Pojos.ColegioEtc;

import java.util.Objects;

public class Horario {

    private int idhorario;
    private String horarioInicio;
    private String horarioFin;

    public Horario(String horarioInicio, String horarioFin) {
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    public Horario(int idhorario, String horarioInicio, String horarioFin) {
        this.idhorario = idhorario;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    public int getIdhorario() {
        return idhorario;
    }

    public void setIdhorario(int idhorario) {
        this.idhorario = idhorario;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(String horarioFin) {
        this.horarioFin = horarioFin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Horario)) return false;
        Horario horario = (Horario) o;
        return Objects.equals(getHorarioInicio(), horario.getHorarioInicio()) &&
                Objects.equals(getHorarioFin(), horario.getHorarioFin());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getHorarioInicio(), getHorarioFin());
    }

    @Override
    public String toString() {
        return horarioInicio + " - "+horarioFin;
    }
}

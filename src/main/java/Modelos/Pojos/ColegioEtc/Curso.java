package Modelos.Pojos.ColegioEtc;

import java.util.Objects;

public class Curso {

    private int idcurso;
    private String bachiller;
    private String seccion;
    private String grado;

    public Curso(String bachiller, String seccion, String grado) {
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
    }

    public Curso(int idcurso, String bachiller, String seccion, String grado) {
        this.idcurso = idcurso;
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
    }

    public int getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(int idcurso) {
        this.idcurso = idcurso;
    }

    public String getBachiller() {
        return bachiller;
    }

    public void setBachiller(String bachiller) {
        this.bachiller = bachiller;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Curso)) return false;
        Curso curso = (Curso) o;
        return Objects.equals(getBachiller(), curso.getBachiller()) &&
                Objects.equals(getSeccion(), curso.getSeccion()) &&
                Objects.equals(getGrado(), curso.getGrado());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdcurso(), getBachiller(), getSeccion(), getGrado());
    }
}

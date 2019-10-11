package Modelos.Pojos.Asistencia.util;

import Modelos.Pojos.ColegioEtc.Curso;

public class AsistenciaDiaria {
    private String fecha;
    private String bachiller;
    private String seccion;
    private String grado;
    private int id;
    private int cursofk;

    public AsistenciaDiaria(String fecha, String bachiller, String seccion, String grado, int id, int cursofk) {
        this.fecha = fecha;
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
        this.id = id;
        this.cursofk = cursofk;
    }

    public AsistenciaDiaria(String fecha, String bachiller, String seccion, String grado, int id) {
        this.fecha = fecha;
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
        this.id = id;
    }

    public AsistenciaDiaria(String fecha, String bachiller, String seccion, String grado) {
        this.fecha = fecha;
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
    }

    public AsistenciaDiaria(String fecha, Curso curso){
        this.fecha = fecha;
        this.bachiller = curso.getBachiller();
        this.seccion = curso.getSeccion();
        this.grado = curso.getGrado();
        this.cursofk = curso.getIdcurso();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCursofk() {
        return cursofk;
    }

    public void setCursofk(int cursofk) {
        this.cursofk = cursofk;
    }

    @Override
    public String toString() {
        return fecha +" "+ bachiller +" "+ grado +" "+ seccion +" "+ id;
    }
}

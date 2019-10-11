package Modelos.Pojos.ALumno;

import Modelos.Pojos.Entidad.EntidadPersona;

public class Alumno extends EntidadPersona {
    private String nromadre;
    private String nropadre;
    private String promo;
    private int idcurso;

    public Alumno() {
    }

    public Alumno(String nombres, String apellidos, String ci, String sexo) {
        super(nombres, apellidos, ci, sexo);
    }

    public Alumno(String nombres, String apellidos, String ci, String sexo, String nromadre, String nropadre, String promo, int idcurso) {
        super(nombres, apellidos, ci, sexo);
        this.nromadre = nromadre;
        this.nropadre = nropadre;
        this.promo = promo;
        this.idcurso = idcurso;
    }

    public String getNromadre() {
        return nromadre;
    }

    public void setNromadre(String nromadre) {
        this.nromadre = nromadre;
    }

    public String getNropadre() {
        return nropadre;
    }

    public void setNropadre(String nropadre) {
        this.nropadre = nropadre;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public int getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(int idcurso) {
        this.idcurso = idcurso;
    }
}

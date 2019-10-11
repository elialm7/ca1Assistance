package Modelos.Pojos.ALumno;

import Modelos.Pojos.ColegioEtc.Curso;

public class AlumnoCurso extends Alumno{
    private String bachiller;
    private String seccion;
    private String grado;
     public  AlumnoCurso(Alumno alumno , Curso curso){
         super(alumno.getNombres(), alumno.getApellidos(), alumno.getCi(), alumno.getSexo(), alumno.getNromadre(), alumno.getNropadre(), alumno.getPromo(), alumno.getIdcurso());
         this.bachiller = curso.getBachiller();
         this.seccion = curso.getSeccion();
         this.grado = curso.getGrado();
    }

    public AlumnoCurso(String nombres, String apellidos, String ci, String sexo, String nromadre, String nropadre, String promo, int idcurso, String bachiller, String seccion, String grado) {
        super(nombres, apellidos, ci, sexo, nromadre, nropadre, promo, idcurso);
        this.bachiller = bachiller;
        this.seccion = seccion;
        this.grado = grado;
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
}

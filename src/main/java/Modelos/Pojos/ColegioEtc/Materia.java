package Modelos.Pojos.ColegioEtc;

public class Materia {
    private int id_materia;
    private String nombre;

    public Materia(int id_materia, String nombre) {
        this.id_materia = id_materia;
        this.nombre = nombre;
    }

    public Materia(String nombre) {
        this.nombre = nombre;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

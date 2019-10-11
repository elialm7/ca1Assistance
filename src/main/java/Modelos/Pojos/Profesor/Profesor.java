package Modelos.Pojos.Profesor;

import Modelos.Pojos.Entidad.EntidadPersona;

public class Profesor extends EntidadPersona {
    private String nroprofesor;

    public Profesor(String nombres, String apellidos, String ci, String sexo, String nroprofesor) {
        super(nombres, apellidos, ci, sexo);
        this.nroprofesor = nroprofesor;
    }

    public String getNroprofesor() {
        return nroprofesor;
    }

}

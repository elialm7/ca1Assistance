package Modelos.Pojos.Entidad;

import java.util.Objects;

public class EntidadPersona {

    private String nombres;
    private String apellidos;
    private String ci;
    private String sexo;

    public EntidadPersona(){}
    public EntidadPersona(String nombres, String apellidos, String ci, String sexo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.ci = ci;
        this.sexo = sexo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntidadPersona)) return false;
        EntidadPersona that = (EntidadPersona) o;
        return Objects.equals(getCi(), that.getCi());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCi());
    }

    @Override
    public String toString() {

        return "Nombre : " + this.nombres + " | Apellido : " + this.apellidos + " | Cedula de identidad : "+this.ci;
    }
}

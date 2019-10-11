package Modelos.Pojos.Asistencia.ProfsorAsistencia;

import Modelos.Pojos.Entidad.EntidadPersona;
import Modelos.Pojos.Profesor.Profesor;

import java.util.List;

public class AsistenciaProfe extends EntidadPersona {
    private List<PresenciaProfe> presenciaProfes;
    private int ownid;
    private int idunion;

    public AsistenciaProfe(Profesor profesor){
        super(profesor.getNombres(), profesor.getApellidos(), profesor.getCi(), profesor.getSexo());
    }
    public AsistenciaProfe(Profesor profesor, int idunion){
        super(profesor.getNombres(), profesor.getApellidos(), profesor.getCi(), profesor.getSexo());
        this.idunion = idunion;
    }

    public AsistenciaProfe(Profesor profesor, int idunion, int ownid){
        super(profesor.getNombres(), profesor.getApellidos(), profesor.getCi(), profesor.getSexo());
        this.idunion = idunion;
        this.ownid = ownid;
    }

    public List<PresenciaProfe> getPresenciaProfes() {
        return presenciaProfes;
    }

    public void setPresenciaProfes(List<PresenciaProfe> presenciaProfes) {
        this.presenciaProfes = presenciaProfes;
    }

    public int getOwnid() {
        return ownid;
    }

    public void setOwnid(int ownid) {
        this.ownid = ownid;
    }

    public int getIdunion() {
        return idunion;
    }

    public void setIdunion(int idunion) {
        this.idunion = idunion;
    }
}

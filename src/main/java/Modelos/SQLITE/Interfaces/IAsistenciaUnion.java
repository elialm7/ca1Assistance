package Modelos.SQLITE.Interfaces;

import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.ColegioEtc.Curso;
import Servicios.Utils.ASAS;

public interface IAsistenciaUnion extends genericdomain<AsistenciaDiaria, Integer> {
    int getLastID();
    // busqueda
    ASAS<AsistenciaDiaria> getby(String date);
    ASAS<AsistenciaDiaria> getBetween(String date1, String date2, Curso curso);
    boolean exists(AsistenciaDiaria asistenciaDiaria);
}

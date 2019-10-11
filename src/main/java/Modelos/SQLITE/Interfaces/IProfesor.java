package Modelos.SQLITE.Interfaces;

import Controladores.Enums.BuscarType;
import Modelos.Pojos.Profesor.Profesor;
import Servicios.Utils.ASAS;

public interface IProfesor extends genericdomain<Profesor, String> {
    ASAS<Profesor> findby(String busqueda, BuscarType buscarType);
}

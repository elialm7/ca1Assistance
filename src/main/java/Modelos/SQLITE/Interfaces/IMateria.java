package Modelos.SQLITE.Interfaces;

import Modelos.Pojos.ColegioEtc.Horario;
import Modelos.Pojos.ColegioEtc.Materia;

public interface IMateria extends genericdomain<Materia, Integer>{
        void add(Materia m, Horario horario);
        int getID(String nombre);
}

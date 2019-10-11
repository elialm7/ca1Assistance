package Modelos.SQLITE.Dao.Asistencia.OwnInterface;

import Servicios.Utils.ASAS;

import java.util.List;

public abstract class IAsistencia<T> {
    public abstract void add(T object);
    public abstract ASAS<T> getAllby(int id);
    public abstract void addAll(List<T> objects);
    public int getlastID(){
        return 0;
    }
    public void update(T object){}
    public void delete(T object){}
}

package Modelos.SQLITE.Interfaces;

import Servicios.Utils.ASAS;

import java.util.List;
import java.util.Objects;

interface genericdomain<T, ID> {
    int ROWLIMIT = 500;
    void add(T toadd);
    T get(ID toget);
    void delete(ID todelete);
    void change(T tochange);
    ASAS<T> getall();

    default  <T extends List> T verify(T E) {
        T t = Objects.requireNonNull(E);
        if (t.isEmpty()) {
            return null;
        }
        return t;
    }
    default int count(ASAS<T> rs){
        return rs.AsArrayList().size();
    }
}

package Servicios.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ASAS<T> {

    private List<T> collection;

    public ASAS(List<T> t){
        this.collection = t;
    }
    public ASAS(T[] array){
        this.collection = new ArrayList<>();
        Collections.addAll(this.collection, array);
    }

    public List<T> AsArrayList(){
        return new ArrayList<>(this.collection);
    }
    public ObservableList<T> AsObservableList(){
        return FXCollections.observableList(collection);
    }

    public static <T> ASAS<T> ToASAS(List<T> list){
        return new ASAS<T>(list);
    }

}

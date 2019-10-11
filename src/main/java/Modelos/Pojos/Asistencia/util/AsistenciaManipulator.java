package Modelos.Pojos.Asistencia.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class AsistenciaManipulator<K, O> {

    private Hashtable<K, O> LlaveObjeto;
    private List<O> objectList;
    private List<K> keysList;
    public AsistenciaManipulator(){
        LlaveObjeto = new Hashtable<>();
        objectList = new ArrayList<>();
        keysList = new ArrayList<>();
    }

    public void addObject(K llave, O objeto){
        if(!Objects.isNull(objeto)) {
            this.LlaveObjeto.put(llave, objeto);
            this.objectList.add(objeto);
            this.keysList.add(llave);
        }

    }

    public O getObject(K llave){
        if(Objects.isNull(llave))return null;
        if(LlaveObjeto.containsKey(llave)){
            return LlaveObjeto.get(llave);
        }
        return null;
    }

    public void remove(K llave){
        if(LlaveObjeto.containsKey(llave)){
            O object = LlaveObjeto.get(llave);
            this.objectList.remove(object);
            this.keysList.remove(llave);
            LlaveObjeto.remove(llave);
        }
    }

    public List<O> getObjectList() {
        return objectList;
    }

    public List<K> getKeysList() {
        return keysList;
    }
}

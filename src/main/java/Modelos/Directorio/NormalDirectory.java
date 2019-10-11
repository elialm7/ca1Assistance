package Modelos.Directorio;

import java.io.File;

public class NormalDirectory extends Directory {

    public NormalDirectory(String direction, String file) {
        super(direction, file);
    }
    public NormalDirectory(String direction){
        super(direction);
    }

    @Override
    public boolean exists() {
        if(file == null && directory == null){
            throw new IllegalStateException("No se puede saber la existencia de un directorio vacio sin definir.");
        }else if(file == null){
            return new File(directory).exists();
        }else{
            return new File(directory, file).exists();
        }
    }

    @Override
    public boolean create() {
        if(file == null && directory == null){
            throw new IllegalStateException("No se puede crear un directorio nulo");
        }else if(file == null){
            return new File(directory).mkdirs();
        }else if(directory != null) {
            return new File(directory).mkdirs();
        }
        return false;
    }

    @Override
    public String getpath() {
        return this.directory + this.file;
    }
}

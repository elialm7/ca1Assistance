package Modelos.Directorio;

public abstract class Directory {
    protected String directory;
    protected String file;
    public Directory(String direction, String file){
        this.directory = direction;
        this.file = file;
    }

    public Directory(String direction){
        this.directory = direction;
        this.file = null;
    }

    public abstract boolean exists();
    public abstract boolean create();
    public abstract String  getpath();

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}

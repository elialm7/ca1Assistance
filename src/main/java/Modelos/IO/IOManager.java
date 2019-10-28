/*
 * Copyright (C) 2019 Rodolfo Elias Ojeda Almada
 *
 * Este programa es software libre: puede redistribuirlo y/o modificarlo bajo
 * los términos de la Licencia General Pública de GNU publicada por la Free
 * Software Foundation, ya sea la versión 3 de la Licencia, o (a su elección)
 * cualquier versión posterior.
 *
 *
 *
 * Este programa se distribuye con la esperanza de que sea útil pero SIN
 * NINGUNA GARANTÍA; incluso sin la garantía implícita de MERCANTIBILIDAD o
 * CALIFICADA PARA UN PROPÓSITO EN PARTICULAR. Vea la Licencia General Pública
 * de GNU para más detalles.
 */

package Modelos.IO;

import Controladores.tabcontrol.TabManipulator;
import Modelos.Directorio.Directory;
import Modelos.Directorio.DirectoryConstants;
import Modelos.Directorio.NormalDirectory;
import Modelos.Hilos.HiloExecutor;
import Servicios.alerta.Alerta;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.*;
import java.util.Objects;

public class IOManager {

    private Directory DatabaseDirectory;
    private Image image;
    private TrayIcon exporting;
    private TrayIcon importing;
    public IOManager(){
        this.DatabaseDirectory =  new NormalDirectory(DirectoryConstants.DataFilesRoot, DirectoryConstants.DATABASENAME);
        this.image = Toolkit.getDefaultToolkit().getImage("/utils/logoofi.png");
    }

    public void importar(File file){
        if(!Objects.isNull(file)){
            String name = file.getName();
            showMessage("Importando", "Comienza la importacion en segundo plano del archivo: "+name);
            _COPY_ copy = new _COPY_(file, new File(DatabaseDirectory.getpath()));
            copy.setOnSucceeded(event -> showMessage("Importación Exitosa", "La importacion ha sido exitosa"));
            HiloExecutor.execute(copy);
        }
    }

    public void export(File file){
        if(!Objects.isNull(file)){
            String name = file.getName();
            showMessage("Exportando", "Iniciando la exportacion en segundo plano del archivo: "+name);
            _COPY_ copy = new _COPY_(new File(DatabaseDirectory.getpath()), file);
            copy.setOnSucceeded(event -> showMessage("Exportación Exitosa", "La exportacion de la DB ha sido exitosa : "+ name));
            HiloExecutor.execute(copy);
        }
    }

    private void showMessage(String title, String information){

        if(SystemTray.isSupported()){
            try {
                TrayIcon trayIcon = new TrayIcon(image, "Notification");
                trayIcon.setImageAutoSize(true);
                SystemTray trayn = SystemTray.getSystemTray();
                trayn.add(trayIcon);
                trayIcon.displayMessage(title, information, TrayIcon.MessageType.INFO);
                trayn.remove(trayIcon);
            }catch  (AWTException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", title, information, TabManipulator.getCurrentInstance().getParentStage());
            }catch (Exception e){
                System.err.println(e);
            }
        }
    }

    private class _COPY_ extends Task<Boolean> {

        private BufferedInputStream bufferInput;
        private BufferedOutputStream bufferOutput;
        private int sizeData;
        private File in;
        private File out;

        public _COPY_(File in, File out){
            this.in = in;
            this.out = out;
        }
        @Override
        protected Boolean call() throws Exception {
            boolean doit = false;
            try {
                bufferInput = new BufferedInputStream(new FileInputStream(in));
                bufferOutput = new BufferedOutputStream(new FileOutputStream(out));
                sizeData = bufferInput.available();
                int dat;
                for(int i = 0; i<sizeData; i++){
                    dat = bufferInput.read();
                    bufferOutput.write(dat);
                }
                bufferInput.close();
                bufferOutput.close();
                doit = true;
            } catch (IOException ex) {
                doit = false;
                System.err.println(ex.getMessage());
            }
            return doit;
        }
    }


}

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

package Modelos.Directorio;

import java.util.Objects;

public class DirectoryFactory {

    public static Directory getDirectoryType(String directorytype, String[] argumentos){

        if(Objects.isNull(directorytype) && Objects.isNull(argumentos))throw new UnsupportedOperationException("No se puede  validar un dato nulo");

        if(directorytype.equalsIgnoreCase("Jar")){
            return new JarDirectory(argumentos[0], argumentos[1]);
        }else if(directorytype.equalsIgnoreCase("normal")){
            return new NormalDirectory(argumentos[0], argumentos[1]);
        }else{
            return null;
        }
    }





}

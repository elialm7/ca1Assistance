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

package test.Tests;

import Modelos.Conexion.ConexionIMP.sqliteConection;
import Modelos.Conexion.MyConnection;
import Modelos.Pojos.Asistencia.util.AsistenciaDiaria;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SQLITE.Dao.Asistencia.Union.AsistenciaUnionIMP;
import Modelos.SQLITE.Dao.Colegio.CursoIMP;
import Modelos.SQLITE.Interfaces.IAsistenciaUnion;
import Modelos.SQLITE.Interfaces.ICurso;
import Servicios.Utils.ASAS;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

//prueba unitaria exitosa.
class AsistenciaUnionIMPTest extends TestCase {

    private IAsistenciaUnion getasistencia(){
        MyConnection connection = new sqliteConection(true);
        ICurso curso = new CursoIMP(connection);
        return new AsistenciaUnionIMP(connection, curso);
    }
    @org.junit.jupiter.api.Test
    void add() {

       /* AsistenciaDiaria asistenciaDiaria;
        for(int i = 0; i<30; i++) {
            if(i <= 9) {
                asistenciaDiaria = new AsistenciaDiaria("2019-02-"+"0"+i, "bti", "b", "1ro", 1);
            }else{
                asistenciaDiaria = new AsistenciaDiaria( "2019-02-"+i, "bti", "b", "1ro", 1);
            }
            getasistencia().add(asistenciaDiaria);
        }

       AsistenciaDiaria asistenciaDiaria;
       int mes = 1;
       String fecha;
       while(mes <= 12){
           for(int dia = 1; dia < 31; dia++){
               if(dia<10){
                   fecha = "2019-0"+mes+"-"+"0"+dia;
                   asistenciaDiaria = new AsistenciaDiaria(fecha, "bti", "b", "1ro", 1);

               }else{
                   fecha = "2019-"+mes+"-"+dia;
                   asistenciaDiaria = new AsistenciaDiaria(fecha, "bti", "b", "1ro", 1);

               }
               getasistencia().add(asistenciaDiaria);
           }
           mes++;
       }*/

        System.out.println(System.getProperty("user.dir"));


    }
    @org.junit.jupiter.api.Test
    void getall(){
     List<prueba> pruebas = new ArrayList<>();
     for(int i = 0; i < 10 ; i++)
         pruebas.add(new prueba("hola papa"));

     modifier(pruebas);

     for (prueba pr : pruebas){
         System.out.println(pr.getDato());
     }



    }
    private void modifier(List<prueba> pruebas){
        for(prueba pr : pruebas){
            pr.setDato("Hola mundo");
        }
    }

    @org.junit.jupiter.api.Test
     void find(){
            show(getasistencia().getBetween("1/02/2019", "400/02/2019", new Curso("bti", "b", "1ro")));
     }

     private void show(ASAS<AsistenciaDiaria> show){
         for (AsistenciaDiaria diaria : show.AsArrayList()){
             System.out.println(diaria.toString());
         }
     }
}

class prueba{
    String dato;

    public prueba(String dato) {
        this.dato = dato;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }
}
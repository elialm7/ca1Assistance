package Controladores.NotificationUtil;

import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.Entidad.EntidadPersona;
import Servicios.alerta.Alerta;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class NotificationHelper {

    private Stage parentStage;
    private static volatile NotificationHelper notificationHelper;
    private NotificationHelper(){
        try {
            this.parentStage = TabManipulator.getCurrentInstance().getParentStage();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }
    public synchronized static NotificationHelper get(){
        if(Objects.isNull(notificationHelper)){
            notificationHelper = new NotificationHelper();
        }
        return notificationHelper;
    }

    // todos los metodos aca
    public void notifyEditionPending(){
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Alerta", "Una edición sigue pendiente...",
                "Cierre la edicíon pendiente, Para poder abrir otra... ", parentStage);
    }
    public Optional<ButtonType> notifyDeleteOption(){
        return Alerta.CreateAlert(Alert.AlertType.CONFIRMATION, "Alerta", "¿Seguro?", "Este registro se eliminara de la base de datos... ", parentStage);
    }
    public void notifyIncorrectDniFormat(){
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Alerta","Información necesaria" , "La cedula de identidad debe ser de solo números y no se acepta uno vacío. ", parentStage);
    }
    public void notifyIncorrectPromoFormat(){
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Alerta","Promoción del alumno necesaria", "No se acepta una promo vacia o con doble hash , Ejemplo de promoción correcta : #019 o #2019. "
                ,parentStage);
    }
    public  <T extends EntidadPersona> void notifyDuplicatedDni(T ci){
        Alerta.CreateAlert(Alert.AlertType.ERROR, "Información", "Cedula de identidad duplicada", "EL nro de cedula: "+ci.getCi() + " Ya existe en la tabla", parentStage);

    }
    public void notifyDataDuplicated(){
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Información", "Ya existe", "Este registro y existe en la base de datos.", parentStage);
    }
    public Optional<ButtonType> notifyInsertionData(){
      return Alerta.CreateAlert(Alert.AlertType.CONFIRMATION, "Inserción", "Entrada de datos",
                "Los datos de la tabla seran añadidos en segundo plano, por favor no cierre la pestaña o perdera la información .", parentStage);
    }
    public void notifySuccessOninsertOperation(int skipped){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", "Exito!", "Los datos han sido insertado con exito en la base  de datos!, Datos ignorados: "+skipped, parentStage);

    }
    public void notifySkippedOnInsertOperation(int skipped){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Información", "Los datos han sido insertados en la base de datos!", "Algunos datos no se han podido insertar en la base de datos debido a que ya existen," +
                "Estos han sido ignorados, Cantidad de datos ignorados: "+skipped, parentStage);

    }
    public Optional<ButtonType> notifyOnCancelOperation(){
        return Alerta.CreateAlert(Alert.AlertType.CONFIRMATION, "Salida", "¿Está Seguro que desea cancelar?", "Si usted cancela perdera los datos... ", parentStage);
    }

    public void notifyOnAdded(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Exito!", "Se ha añadido con exito", "El nuevo registro se ha añadido con exito !!",parentStage);
    }
    public void notifyOnnulldata(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Vacio ", "No se puede agregar datos vacios", "No se aceptan datos vacios. ",parentStage);
    }
    public void notifyOnchanged(){
        Alerta.CreateAlert(Alert.AlertType.INFORMATION, "Exito!", "Se ha modificado con exito", "El registro ha sido modificado correctamente!",parentStage);
    }

    public void notifyUncampatibleScreenSize(){
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Advertencia",  "Resolucion de pantalla no compatible", "La resolucion de su pantalla no es compatible con este " +
                "software. Lo sentimos :(", parentStage);
    }

    public void notifyTabOpened() {
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Advertencia",  "Un Pestaña de registro de actividades diarias sigue abierto", "Por favro cierre la pestaña para abrir otro... :)", parentStage);
    }

    public void notifyProblemTofind() {
        Alerta.CreateAlert(Alert.AlertType.WARNING, "Advertencia",
                "Datos insuficientes",
                "Por favor complete los datos para poder realizar la busqueda.", parentStage);

    }
}

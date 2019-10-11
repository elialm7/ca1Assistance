package Servicios.Utils;
import Modelos.Pojos.Entidad.EntidadPersona;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Utils {

    public static boolean VerifyDNI(String dni){
        if(!dni.isEmpty()){
            String regex = "\\d+";
            return dni.matches(regex);
        }else{
            return  false;
        }
    }

    public static boolean VerifyPromo(String text, char c){
        boolean accepted = false;
        if(text.startsWith(String.valueOf(c))){
            if(count(text, c)== 1){
                accepted = true;
            }
        }
        return accepted;
    }

    private static int count(String text, char c){
        int i = 0;
        int count = 0;
        while(i<text.length()){
            if(text.charAt(i)== c){
                count++;
            }
            i++;
        }
        return count;
    }

    public static  <T> boolean ExistsInTable(T tofind, TableView<T> tableView){
        int count = 0;
        ObservableList<T> observableList = tableView.getItems();
        for(T t : observableList){
            if(t.equals(tofind)){
                count++;
            }
        }
        return count>0;
    }

    public static void setpickerpropery(List<DatePicker> datePickers){
        for(DatePicker piker : datePickers) {
            piker.setConverter(new StringConverter<LocalDate>() {
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                {
                    piker.setPromptText(pattern.toUpperCase());
                }
                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }
                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
        }
    }


    public static <T extends EntidadPersona> String getNewIntegerID(List<T> integers){
        int alto = 0;
        boolean paso = false;
        if(integers.size() != 0) {
            for (T str : integers) {
                int current = Integer.parseInt(str.getCi());
                if (!paso) {
                    alto = current;
                    paso = true;
                }
                if (current > alto) {
                    alto = current;
                }
            }
            alto++;
            return String.valueOf(alto);
        }else{
            alto++;
            return String.valueOf(alto);
        }
    }

}

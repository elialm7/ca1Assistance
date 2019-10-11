package Controladores.ControladorMenu.Materias;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.ColegioEtc.Materia;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MateriasController implements Initializable, NodeUpdator {


    @FXML
    private TableView<Materia> table;

    @FXML
    private TableColumn<Materia, Integer> id;

    @FXML
    private TableColumn<Materia, String> nombrecolumn;

    @FXML
    private TextField materia_txt;

    @FXML
    private Button apply;

    @FXML
    private Button add;

    private TabManipulator tabManipulator;

    public MateriasController(){}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    private void load(){
        set_events();
        addpropertys();
        setrowfactory();
        loadtabmanipulator();
    }

    private void reset(Materia materia){
        if(materia!=null){
            this.materia_txt.setText(materia.getNombre());
        }else{
            this.materia_txt.setText("");
        }
    }

    private void updatetable(){
       ObservableList<Materia> materias = DaoService.getInstance().getMateriaInstance().getall().AsObservableList();
       this.table.getItems().clear();
       for(Materia m : materias){
           this.table.getItems().add(m);
       }
       if(table.getItems().isEmpty()){
           reset(null);
           this.apply.setDisable(true);
       }else{
           this.apply.setDisable(false);
       }
    }
    private void apply(){
        DaoService.getInstance().getMateriaInstance().change(new Materia(this.table.getSelectionModel().getSelectedItem().getId_materia(), this.materia_txt.getText()));
        updatetable();
    }
    private void add(){
        String txt = adding();
        if(Objects.isNull(txt))return;
        DaoService.getInstance().getMateriaInstance().add(new Materia(txt));
        updatetable();
    }

    private String adding(){
        boolean bool = false;
        String str;
        do{
             str = dialog();
             if(Objects.isNull(str))return null;
             if(str.isEmpty()) return null;
             if(existeya(str)==0)
                bool = true;
        }while (!bool);
        return str;
    }

    private int existeya(String str){
        int n = 0;
        for(Materia m : this.table.getItems()){
            if(m.getNombre().equals(str)){
                n++;
            }
        }
        return n;
    }

    private String dialog(){
        TextInputDialog txt = new TextInputDialog();
        txt.setTitle("Registro Materia");
        txt.setHeaderText("Inserte nombre de la materia");
        txt.setContentText("Nombre de la materia : ");
        Optional<String> str = txt.showAndWait();
        if(str.isPresent()){
            return str.get();
        }
        return null;
    }

    private void setrowfactory(){
        table.setRowFactory(param -> {
            final TableRow<Materia> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Eliminar");
            row.setOnMouseClicked(event -> {
                if(row.isSelected()){
                    apply.setVisible(true);
                    materia_txt.setDisable(false);
                    reset(row.getItem());
                }else{
                    apply.setVisible(false);
                    materia_txt.setDisable(true);
                    reset(null);
                }
            });
            menuItem.setOnAction(t->{
                Materia materia = this.table.getSelectionModel().getSelectedItem();
                delete(materia);
                updatetable();
            });
            contextMenu.getItems().add(menuItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(contextMenu).otherwise((ContextMenu)null));
            return row;
        });
    }

    private void set_events(){
        apply.setVisible(false);
        materia_txt.setDisable(true);
        apply.setOnAction(t->apply());
        add.setOnAction(t->add());
    }
    private void addpropertys(){
        id.setCellValueFactory(new PropertyValueFactory<>("id_materia"));
        nombrecolumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    }
    private void delete(Materia materia){
        if(NotificationHelper.get().notifyDeleteOption().get() == ButtonType.OK) {
            DaoService.getInstance().getMateriaInstance().delete(materia.getId_materia());
        }
    }

    private void loadtabmanipulator(){
        try{
            tabManipulator = TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }

    @Override
    public void updateNode() {
        updatetable();
    }
}

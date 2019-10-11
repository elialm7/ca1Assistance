package Controladores.ControladorMenu.Cursos;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.NotificationUtil.NotificationHelper;
import Controladores.tabcontrol.TabManipulator;
import Modelos.Pojos.ColegioEtc.Curso;
import Modelos.SqliteDaoService.DaoService;
import Servicios.alerta.Alerta;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CursosController implements Initializable, NodeUpdator {


    @FXML
    private TableView<Curso> table;

    @FXML
    private TableColumn<Curso, String> bachiller_column;

    @FXML
    private TableColumn<Curso, String> grado_column;

    @FXML
    private TableColumn<Curso, String> seccion_column;

    @FXML
    private TextField bachiller_txt;

    @FXML
    private Button add;

    @FXML
    private Button apply;

    @FXML
    private TextField grado_txt;

    @FXML
    private TextField seccion_txt;


    private TabManipulator CurrentTabManager;


    public CursosController() {

    }

    private void load(){
        set_events();
        loadcolumnpropertys();
        updatetable();
        loadTabM();
    }

    private void resettxt(Curso curso){
        if(curso == null) {
            this.bachiller_txt.setText("");
            this.seccion_txt.setText("");
            this.grado_txt.setText("");
        }else{
            this.bachiller_txt.setText(curso.getBachiller());
            this.seccion_txt.setText(curso.getSeccion());
            this.grado_txt.setText(curso.getGrado());
        }
    }

    private void add(){
            Curso curso = getcursofromform();
            if (existsinDB(curso) > 0) {
                NotificationHelper.get().notifyDataDuplicated();
            } else {
                executerole(ActionType.add, curso);
            }
            updatetable();
    }

    private void executerole(ActionType actiontype, Curso curso){
        switch (actiontype){
            case add:
                addtodb(curso);
                break;
            case update:
                applytodb(curso);
                break;
        }

    }
    private void addtodb(Curso curso){
      DaoService.getInstance().getCursoInstance().add(curso);
    }
    private void applytodb(Curso curso){
        DaoService.getInstance().getCursoInstance().change(curso);
    }
    private void apply(){
        Curso curso = getcursofromform();
        curso.setIdcurso(DaoService.getInstance().getCursoInstance().getcursoIDbybachiller(curso.getBachiller(), curso.getSeccion(), curso.getGrado()));
        executerole(ActionType.update, curso);
        updatetable();
    }
    private void loadcolumnpropertys(){
        bachiller_column.setCellValueFactory(new PropertyValueFactory<>("bachiller"));
        seccion_column.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        grado_column.setCellValueFactory(new PropertyValueFactory<>("grado"));
    }
    private void set_events(){
        this.apply.setVisible(false);
        this.add.setOnAction(t->add());
        this.apply.setOnAction(t->apply());
        this.table.setRowFactory(param -> {
            final TableRow<Curso> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Eliminar");
            row.setOnMouseClicked(event -> {
                        if(row.isSelected()){
                            apply.setVisible(true);
                            add.setVisible(false);
                            resettxt(row.getItem());
                        }else{
                            add.setVisible(true);
                            apply.setVisible(false);
                            resettxt(null);
                        }
            });

            menuItem.setOnAction(t->{
                Curso curso = this.table.getSelectionModel().getSelectedItem();
                delete(curso);
                updatetable();
            });
            contextMenu.getItems().add(menuItem);
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(contextMenu).otherwise((ContextMenu)null));
            return row;
        });
    }

    private Curso getcursofromform(){
       return new Curso(this.bachiller_txt.getText(), this.seccion_txt.getText(), this.grado_txt.getText());
    }

    private void updatetable(){
        ObservableList<Curso> cursos = DaoService.getInstance().getCursoInstance().getall().AsObservableList();
        this.table.getItems().clear();
        if(cursos==null)return;
        for(Curso curso : cursos){
            this.table.getItems().add(curso);
        }
    }

    private int existsinDB(Curso curso){
        int n = 0;
        ObservableList<Curso> cursos = DaoService.getInstance().getCursoInstance().getall().AsObservableList();
        for(Curso c : cursos){
               if(curso.equals(c)){
                  n++;
               }
        }
        System.out.println(n);
        return n;
    }


    private void delete(Curso curso){
        Optional<ButtonType> result = NotificationHelper.get().notifyDeleteOption();
        if(result.get() == ButtonType.OK) {
           DaoService.getInstance().getCursoInstance().delete(curso.getIdcurso());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        load();
    }

    @Override
    public void updateNode() {
        updatetable();
    }
    private void loadTabM(){
        try{
           this.CurrentTabManager =TabManipulator.getCurrentInstance();
        }catch (IllegalAccessException e){
            e.printStackTrace();
            Alerta.ShowError(e);
        }
    }
    enum ActionType{
        add, update
    }
}

package Controladores.tabcontrol;

import Controladores.ControladorMenu.menu.NodeUpdator;
import Controladores.Enums.Fxmls;
import Servicios.alerta.Alerta;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
public class TabManipulator{


    //private NodeSupplier<Fxmls> nodeFilter;
    private FxmlFactory nodeFilter;
    private TabPane tabPane;
    private  Stage stage;
    private Datamanipulator datamanipulator;
    private static long IDGENERATED = 0;
    private static volatile TabManipulator tabManipulator;
    private  TabManipulator(TabPane tabPane, Stage stage){
        this.nodeFilter = new FxmlFactory();
        this.tabPane = tabPane;
        this.datamanipulator = new Datamanipulator();
        this.stage = stage;
    }
    private static IllegalAccessException getExceptionto(){
        return new IllegalAccessException("No ecistes una instancia, Debe existir una. ");
    }
    public static TabManipulator getInstance(TabPane tabPane, Stage st){
        if(tabManipulator == null){
            synchronized (TabManipulator.class){
                if(tabManipulator == null){
                    tabManipulator = new TabManipulator(tabPane, st);
                }
            }
        }
        return  tabManipulator;
    }

    public  static TabManipulator getCurrentInstance() throws IllegalAccessException {
        if(tabManipulator == null){
           throw getExceptionto();
        }
        return tabManipulator;
    }

    public  Stage getParentStage(){
        return stage;
    }
    public <T extends NodeUpdator>void add(Fxmls fxmls, T controller, String title){
        String id  = datamanipulator.getIdifexist(fxmls);
        if(id==null){
            DoTab(title, fxmls, controller, true);
        }else{
            Tab tab = datamanipulator.getTab(id);
            tabPane.getSelectionModel().select(tab);

        }
    }

    public <T extends NodeUpdator> void add(Fxmls fxmls, T controller, String title,  boolean closeable){
        String id;
        if((id = datamanipulator.getIdifexist(fxmls))==null){
            DoTab(title, fxmls, controller, closeable);
        }else{
            Tab tab = datamanipulator.getTab(id);
            tabPane.getSelectionModel().select(tab);
        }
    }

    public <T extends NodeUpdator> void add(Fxmls fxmls, Class<T> cls, String title){
        try {
            this.add(fxmls, cls.newInstance(), title,true);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void remove(Fxmls fxmls){
        String id = datamanipulator.getIdifexist(fxmls);
        if(id!=null){
            Tab tab = datamanipulator.getTab(id);
            datamanipulator.remove(id);
            tabPane.getTabs().remove(tab);
        }
    }

    public boolean isOpen(Fxmls fxmls){
        return !(Objects.isNull(datamanipulator.getIdifexist(fxmls)));
    }
    public void  blockAllexcept(Fxmls fxmls){
        ObservableList<TabMask> tabMasks = datamanipulator.getTabEnvolts();
        for(TabMask mask : tabMasks){
            if(!mask.getFxmls().equals(fxmls)){
                mask.setIsblocked(true);
                mask.getTab().setDisable(true);
            }
        }
    }

    public void unblockAll(){
        ObservableList<TabMask> tabMasks = datamanipulator.getTabEnvolts();
        for(TabMask mask: tabMasks){
            if(mask.isIsblocked()){
                mask.getTab().setDisable(false);
                mask.setIsblocked(false);
            }
        }

    }

    public void changeTabTitle(Fxmls fxmls, String title){
        if(isOpen(fxmls)){
            getmask(fxmls).getTab().setText(title);
        }
    }

    private TabMask getmask(Fxmls fxmls){
        for(TabMask tabMask : datamanipulator.getTabEnvolts()){
            if(fxmls.name().equals(tabMask.getFxmls().name())){
                return tabMask;
            }
        }
        return null;
    }

    public void CloseAll(){
        datamanipulator.removeAll();
        tabPane.getTabs().removeAll(tabPane.getTabs());
    }

    public boolean IsThereTabOpened(){
        return datamanipulator.getTabEnvolts().size()>0;
    }

    private <T extends NodeUpdator> void DoTab(String title,Fxmls fxmls, T controller, boolean closeable){
        Tab tab = new Tab(title);
        tab.setId(generateID());
        tab.setOnSelectionChanged(event -> controller.updateNode());
        Node node = nodeFilter.getNode(fxmls, controller);
        tab.setContent(node);
        tab.setClosable(closeable);
        tab.setOnCloseRequest(eventlistener(title));
        TabMask Tbmsk = new TabMask(tab, fxmls);
        Tbmsk.setIsopen(true);
        tabPane.getTabs().add(tab);
        datamanipulator.add(Tbmsk);
        tabPane.getSelectionModel().select(tab);

    }
    private EventHandler<Event> eventlistener(String title){
        return event -> {
            Optional<ButtonType> result = Alerta.CreateAlert(Alert.AlertType.CONFIRMATION, title, "Confirmacion de Salida", "Está Cerrando la pestañña, ¿Desea cerrar la pestaña?", stage);
            if (result.get() == ButtonType.OK) {
                Tab toremove = tabPane.getSelectionModel().getSelectedItem();
                datamanipulator.remove(toremove.getId());
                tabPane.getTabs().remove(toremove);
            } else {
                event.consume();
            }
        };
    }
    private void resetId(){
        IDGENERATED = 0;
    }
    private String generateID(){
        IDGENERATED++;
        return String.valueOf(IDGENERATED);
    }


    private class TabMask{
        private String id;
        private Tab  tab;
        private Fxmls fxmls;
        private boolean isblocked = false;
        private boolean isopen = false;

        private  TabMask(Tab tab, Fxmls fxmls) {
            this.tab = tab;
            this.fxmls = fxmls;
            addid(tab);
        }
        private void addid(Tab id){
            this.id = id.getId();
        }
        private String getId(){
            return this.id;
        }
        private Fxmls getFxmls() {
            return fxmls;
        }
        private Tab getTab(){
            return this.tab;
        }

        public boolean isIsblocked() {
            return isblocked;
        }

        public void setIsblocked(boolean isblocked) {
            this.isblocked = isblocked;
        }

        public boolean isIsopen() {
            return isopen;
        }

        public void setIsopen(boolean isopen) {
            this.isopen = isopen;
        }
    }

    private class Datamanipulator implements ListChangeListener<TabMask>{

        private ObservableList<TabMask> tabEnvolts;

        private Datamanipulator(){
            tabEnvolts = FXCollections.observableArrayList();
            tabEnvolts.addListener(this);
        }
        private  void add(TabMask tabEnvolt){
            tabEnvolts.add(tabEnvolt);
        }

        private  String getIdifexist(Fxmls fxmls){
            for (TabMask tabMask: tabEnvolts) {
                if(tabMask.getFxmls().name().equals(fxmls.name())){
                    return tabMask.getId();
                }
            }
            return null;
        }
        private Tab getTab(String id){
            Tab tab = null;
            for (TabMask tabMask : tabEnvolts) {
                if (tabMask.getId().equals(id)) {
                    tab = tabMask.getTab();
                }
            }
            return tab;
        }

        private ObservableList<TabMask> getTabEnvolts(){
            return this.tabEnvolts;
        }

         private void remove(String id){
             Iterator<TabMask> it = tabEnvolts.iterator();
             while(it.hasNext()){
                 TabMask tab = it.next();
                 if(tab.getId().equals(id)){
                     it.remove();
                 }
             }
         }

         private boolean removeAll(){
            return tabEnvolts.removeIf(TabMask::isIsopen);
         }
        @Override
        public void onChanged(Change<? extends TabMask> c) {
            while(c.next()) {
                if (c.wasRemoved()) {
                    if (tabEnvolts.size() == 0) {
                        resetId();
                    }
                }
            }
        }
    }
}





















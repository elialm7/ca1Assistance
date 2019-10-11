package Controladores.tabcontrol;

import javafx.scene.Node;

public interface NodeSupplier<T> {
    Node getNode(T define, Object controller);
}

package cz.thewhiterabbit.electronicapp.view.components;

import cz.thewhiterabbit.electronicapp.view.events.NodeListEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeListView extends ScrollPane {
    private final VBox holder = new VBox();
    private final List<NodeListItem> itemsList = new ArrayList<>();
    private final ObservableList<NodeListItem> items = FXCollections.observableList(itemsList);
    private final HashMap<NodeListItem, InvalidationListener> listenerMap = new HashMap<>();

    public NodeListView(){
        setContent(holder);
        initListListener();
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setFitToWidth(true);
    }

    private void initListListener() {
        items.addListener(new ListChangeListener<NodeListItem>() {
            @Override
            public void onChanged(Change<? extends NodeListItem> change) {
                change.next();
                change.getAddedSubList().forEach(NodeListView.this::onItemAdded);
                change.getRemoved().forEach(NodeListView.this::onItemRemoved);
            }
        });
    }

    private void onItemAdded(NodeListItem item){
        holder.getChildren().add(item);
        InvalidationListener invalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                fireEvent(new NodeListEvent(NodeListEvent.NODE_CHECK_CHANGE, item));
            }
        };
        listenerMap.put(item, invalidationListener);
        item.checkedPropertyProperty().addListener(invalidationListener);
        item.getShowButton().setOnAction(e->{
            fireEvent(new NodeListEvent(NodeListEvent.SHOW_NODE, item));
        });
    }

    private void onItemRemoved(NodeListItem item){
        holder.getChildren().remove(item);
        item.getShowButton().setOnAction(e->{});
        item.checkedPropertyProperty().removeListener(listenerMap.get(item));
        listenerMap.remove(item);
    }

    public ObservableList<NodeListItem> getItems() {
        return items;
    }

}

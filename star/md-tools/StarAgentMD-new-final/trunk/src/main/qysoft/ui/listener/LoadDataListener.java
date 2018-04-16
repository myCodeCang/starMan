package main.qysoft.ui.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.Controller;
import main.qysoft.ui.task.LoadDataTask;

public class LoadDataListener implements ChangeListener<Integer> {
    private Controller controller;
    private  LoadDataTask task;

    public LoadDataListener(Controller controller, LoadDataTask task) {
        this.task = task;
        this.controller = controller;
    }

    @Override
    public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
        controller.setTask(task);
        controller.init();
    }
}

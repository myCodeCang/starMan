package main.qysoft.ui.control;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 透明加载页面
 */
public class ProgressForm {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Stage dialogStage;
    private ProgressIndicator progressIndicator;

    public ProgressForm(Stage primaryStage) {
        dialogStage = new Stage();
        progressIndicator = new ProgressIndicator();

        // 窗口父子关系
        dialogStage.initOwner(primaryStage);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        // progress bar
        Label label = new Label("数据加载中, 请稍后...");
        label.setTextFill(Color.YELLOW);
        //label.getStyleClass().add("progress-bar-root");
        progressIndicator.setProgress(-1F);
        //progressIndicator.getStyleClass().add("progress-bar-root");
        //progressIndicator.progressProperty().bind(task.progressProperty());

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setBackground(Background.EMPTY);
        vBox.getChildren().addAll(progressIndicator,label);

        Scene scene = new Scene(vBox);
        scene.setFill(null);
        dialogStage.setScene(scene);
    }

    public void activateProgressBar() {
        dialogStage.show();
    }

    public void setTask(final Task<?> task) {
        progressIndicator.progressProperty().bind(task.progressProperty());
        Thread inner = new Thread(task);
        inner.start();

        task.setOnSucceeded(event -> dialogStage.close());
    }

    public Stage getDialogStage(){
        return dialogStage;
    }

    public void cancelProgressBar() {
        dialogStage.close();
    }
}

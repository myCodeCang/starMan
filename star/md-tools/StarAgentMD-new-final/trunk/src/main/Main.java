package main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.qysoft.md.actors.ActorMsg;
import main.qysoft.md.actors.MainActor;
import main.qysoft.ui.control.ProgressForm;
import main.qysoft.ui.listener.LoadDataListener;
import main.qysoft.ui.task.LoadDataTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import main.qysoft.jpa.support.CustomRepositoryFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Valid;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableTransactionManagement    //开启事务注解
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@EnableScheduling
public class Main extends Application {
    public static ConfigurableApplicationContext applicationContext;
    public static Stage stage;
    public static ProgressForm progressFrom;
    private Controller controller;

    @Override
    public void init() throws Exception {
        CompletableFuture.supplyAsync(() -> {
            ConfigurableApplicationContext ctx = SpringApplication.run(this.getClass());
            return ctx;
        }).thenAccept(c -> {
            launchApplicationView(c);

            // 非UI线程加载数据并刷新页面
            Platform.runLater(() -> loadData());
        });
    }

    /**
     * 加载交易数据
     */
    private void loadData() {
        LoadDataTask task = new LoadDataTask(null);
        task.valueProperty().addListener(new LoadDataListener(controller, task));
        progressFrom.setTask(task);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.stage = primaryStage;
//        Parent root = FXMLLoader.load(getClass().getResource("starAgent.fxml"));
//        primaryStage.setTitle("撮合交易系统");
//        primaryStage.setScene(new Scene(root, 1280, 800));
//        primaryStage.setResizable(false);
//        primaryStage.setMaximized(false);
//        primaryStage.show();
        URL location = getClass().getResource("starAgent.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        //如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
        primaryStage.setTitle("撮合交易系统");
        primaryStage.setScene(new Scene(root, 1280, 800));
        root.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        controller = fxmlLoader.getController();   //获取Controller的实例对象
        primaryStage.setResizable(false);
        primaryStage.setMaximized(false);
        primaryStage.show();

        // 创建进度指示器
        progressFrom = new ProgressForm(Main.stage);
        progressFrom.activateProgressBar();

        primaryStage.setOnCloseRequest(handler -> SpringApplication.exit(applicationContext));
    }

    private void launchApplicationView(ConfigurableApplicationContext ctx) {
        Main.applicationContext = ctx;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

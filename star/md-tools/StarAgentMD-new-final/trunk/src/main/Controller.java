package main;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import main.qysoft.md.entity.BaseGoodsGroup;
import main.qysoft.md.entity.MdTrade;
import main.qysoft.md.entity.MdTradeLog;
import main.qysoft.md.entity.MdTradeMain;
import main.qysoft.ui.control.ProgressForm;
import main.qysoft.ui.task.LoadDataTask;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.domain.Page;
import scala.annotation.meta.param;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    //定义头部label
    @FXML
    private TextField userNameInput;
    @FXML
    private ComboBox comboxGroupIds;
    @FXML
    private Label openingPrice;
    @FXML
    private Label closingPrice;
    @FXML
    private Label highestPrice;
    @FXML
    private Label lowestPrice;
    @FXML
    private Label transPrice;
    @FXML
    private DatePicker createDateBegin;
    @FXML
    private DatePicker createDateEnd;

    //定义成交表格
    @FXML
    private TableView transList;
    @FXML
    private TableColumn colgroupId;
    @FXML
    private TableColumn coltransName;
    @FXML
    private TableColumn coltransId;
    @FXML
    private TableColumn coluserName;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colAmount;
    @FXML
    private TableColumn colcreareDate;
    //定义求购表格
    @FXML
    private TableView buyList;
    @FXML
    private TableColumn buyGroupId;
    @FXML
    private TableColumn buyUserName;
    @FXML
    private TableColumn buyPrice;
    @FXML
    private TableColumn buyNum;

    //定义转让表格
    @FXML
    private TableView sellList;
    @FXML
    private TableColumn sellGroupId;
    @FXML
    private TableColumn sellUserName;
    @FXML
    private TableColumn sellPrice;
    @FXML
    private TableColumn sellNum;

    //定义分页
    @FXML
    private TextField nowPage;


    private List<MdTrade> tradeList;
    private List<MdTradeLog> tradeLogList;
    private Page<MdTradeLog> tradeLogFilterList;
    private MdTradeMain tradeMain;
    private List<MdTradeMain> mdTradeMainList;
    private List<BaseGoodsGroup> baseGoodsGroupList;
    private String groupId;
    private String userName;
    private LocalDate beginDate;
    private LocalDate endDate;
    private int pageNum;
    private ProgressForm progressForm;
    private LoadDataTask task;

    public void init() {
        taskLoadData();
        groupId = String.valueOf(task.getGoodGroupId());
        List<String> collect = this.getBaseGoodsGroupList().stream().map(p -> p.getName()).collect(Collectors.toList());
        ObservableList<String> options = FXCollections.observableArrayList(collect);
        this.comboxGroupIds.setItems(options);
        this.comboxGroupIds.getSelectionModel().select(0);
        this.reloadInfo();
        this.comboxGroupIds.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Optional<BaseGoodsGroup> first = baseGoodsGroupList.stream().filter(p -> p.getName().equals(newValue)).findFirst();
                if (first.isPresent()) {
                    groupId = String.valueOf(first.get().getId());
                }
                loadData();
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                loadData();
            }
        }, 3000, 3000);
    }

    public void loadData() {
        Platform.runLater(() -> {
            LoadDataTask task = new LoadDataTask(Long.valueOf(groupId));
            Integer integer = task.loadData(userName, beginDate, endDate, pageNum);
            this.task = task;
            taskLoadData();
            reloadInfo();
            Main.progressFrom.cancelProgressBar();
        });
    }

    public void reloadInfo() {
        if (StringUtils.isBlank(groupId)) {
            return;
        }
        this.headInfo();
        this.findmdTradeLogList();
        this.findmdTradeBuyList();
        this.findmdTradeSellList();
    }

    public void headInfo() {
        this.openingPrice.setText(tradeMain.getOpeningPrice().toString());
        this.closingPrice.setText(tradeMain.getClosingPrice().toString());
        this.highestPrice.setText(tradeMain.getHighestPrice().toString());
        this.lowestPrice.setText(tradeMain.getLowestPrice().toString());
        this.transPrice.setText(tradeMain.getTransactionPrice().toString());
    }

    //加载历史成交数据
    public void findmdTradeLogList() {
        final ObservableList<MdTradeLog> data = FXCollections.observableArrayList(this.tradeLogFilterList.getContent());
        this.transList.sort();
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colgroupId.setCellValueFactory(new PropertyValueFactory("groupId"));
        coltransId.setCellValueFactory(new PropertyValueFactory("mdTradeId"));
        coluserName.setCellValueFactory(new PropertyValueFactory("userName"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        colcreareDate.setCellValueFactory(new PropertyValueFactory("createDate"));
        this.transList.setEditable(false);
        final int[] colorFlag = new int[1];
        for (int j = 0; j < this.transList.getColumns().size(); j++) {
            TableColumn visibleLeafColumn = this.transList.getVisibleLeafColumn(j);
            visibleLeafColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<MdTradeLog,Object>() {
                        ObservableValue ov;
                        @Override
                        public void updateItem(Object item, boolean empty) {

                            MdTradeLog mdTradeLog = (MdTradeLog) this.getTableRow().getItem();
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                setText(String.valueOf(item));
                                if (mdTradeLog==null){
                                    return;
                                }
                                if(mdTradeLog.getPrice().compareTo(tradeMain.getOpeningPrice())>0){
                                    this.setStyle("-fx-text-fill: #ed0506;");
                                }else{
                                    this.setStyle("-fx-text-fill: #09ed05;");
                                }
                            }
                        }
                    };
                }
            });
        }

        this.transList.setItems(data);

    }

    //加载求购数据信息
    public void findmdTradeBuyList() {
        final ObservableList<MdTrade> buyData = FXCollections.observableArrayList(this.tradeList.stream().filter(p -> p.getType() == 1).collect(Collectors.toList()));
        buyGroupId.setCellValueFactory(new PropertyValueFactory("groupId"));
        buyUserName.setCellValueFactory(new PropertyValueFactory("userName"));
        buyPrice.setCellValueFactory(new PropertyValueFactory("price"));
        buyNum.setCellValueFactory(new PropertyValueFactory("publishNum"));
        buyGroupId.setStyle("-fx-text-fill: #09ed05;");
        buyUserName.setStyle("-fx-text-fill: #09ed05;");
        buyPrice.setStyle("-fx-text-fill: #09ed05;");
        buyNum.setStyle("-fx-text-fill: #09ed05;");
        this.buyList.setEditable(false);
        this.buyList.setItems(buyData);
    }

    //加载转让信息数据
    public void findmdTradeSellList() {
        final ObservableList<MdTrade> sellData = FXCollections.observableArrayList(this.tradeList.stream().filter(p -> p.getType() == 2).collect(Collectors.toList()));
        sellGroupId.setCellValueFactory(new PropertyValueFactory("groupId"));
        sellUserName.setCellValueFactory(new PropertyValueFactory("userName"));
        sellPrice.setCellValueFactory(new PropertyValueFactory("price"));
        sellNum.setCellValueFactory(new PropertyValueFactory("publishNum"));
        sellGroupId.setStyle("-fx-text-fill: #ed0506;");
        sellUserName.setStyle("-fx-text-fill: #ed0506;");
        sellPrice.setStyle("-fx-text-fill: #ed0506;");
        sellNum.setStyle("-fx-text-fill: #ed0506;");
        this.sellList.setEditable(false);
        this.sellList.setItems(sellData);
    }

    public void taskLoadData() {
        this.setBaseGoodsGroupList(task.getBaseGoodsGroupList());
        this.setTradeList(task.getTradeList());
        this.setTradeLogList(task.getTradeLogList());
        this.setTradeLogFilterList(task.getTradeLogFilterList());
        this.setTradeMain(task.getTradeMain());
        this.setMdTradeMainList(task.getMdTradeMainList());
    }

    public void search() {
        Main.progressFrom.activateProgressBar();
        beginDate = this.createDateBegin.getValue();
        endDate = this.createDateEnd.getValue();
        userName = this.userNameInput.getText().trim();
        this.findmdTradeLogList();
    }

    //分页控件
    public void firstPage() {
        Main.progressFrom.activateProgressBar();
        this.nowPage.setText("1");
        pageNum = 1;
    }

    public void prvePage() {
        Main.progressFrom.activateProgressBar();
        String nowPageText = this.nowPage.getText();
        if (pageNum <= 1) {
            this.nowPage.setText("1");
            pageNum = 1;
        } else {
            pageNum = Integer.valueOf(nowPageText) - 1;
            this.nowPage.setText(String.valueOf(pageNum));
        }

    }

    public void nextPage() {
        Main.progressFrom.activateProgressBar();
        String nowPageText = this.nowPage.getText();

        int totalPages = this.tradeLogFilterList.getTotalPages();
        if (pageNum >= totalPages) {
            this.nowPage.setText(String.valueOf(totalPages));
            pageNum = totalPages;
        } else {
            pageNum = Integer.valueOf(nowPageText) + 1;
            this.nowPage.setText(String.valueOf(pageNum));
        }
    }

    public void lastPage() {
        Main.progressFrom.activateProgressBar();
        int totalPages = this.tradeLogFilterList.getTotalPages();
        this.nowPage.setText(String.valueOf(totalPages));
        pageNum = totalPages;
    }

    public void goPage() {
        Main.progressFrom.activateProgressBar();
        String nowPageText = this.nowPage.getText();
        pageNum = Integer.valueOf(nowPageText);
    }

    public List<MdTrade> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<MdTrade> tradeList) {
        this.tradeList = tradeList;
    }

    public List<MdTradeLog> getTradeLogList() {
        return tradeLogList;
    }

    public void setTradeLogList(List<MdTradeLog> tradeLogList) {
        this.tradeLogList = tradeLogList;
    }

    public MdTradeMain getTradeMain() {
        return tradeMain;
    }

    public void setTradeMain(MdTradeMain tradeMain) {
        this.tradeMain = tradeMain;
    }

    public List<BaseGoodsGroup> getBaseGoodsGroupList() {
        return baseGoodsGroupList;
    }

    public void setBaseGoodsGroupList(List<BaseGoodsGroup> baseGoodsGroupList) {
        this.baseGoodsGroupList = baseGoodsGroupList;
    }

    public List<MdTradeMain> getMdTradeMainList() {
        return mdTradeMainList;
    }

    public void setMdTradeMainList(List<MdTradeMain> mdTradeMainList) {
        this.mdTradeMainList = mdTradeMainList;
    }

    public ProgressForm getProgressForm() {
        return progressForm;
    }

    public void setProgressForm(ProgressForm progressForm) {
        this.progressForm = progressForm;
    }

    public LoadDataTask getTask() {
        return task;
    }

    public void setTask(LoadDataTask task) {
        this.task = task;
    }

    public Page<MdTradeLog> getTradeLogFilterList() {
        return tradeLogFilterList;
    }

    public void setTradeLogFilterList(Page<MdTradeLog> tradeLogFilterList) {
        this.tradeLogFilterList = tradeLogFilterList;
    }
}

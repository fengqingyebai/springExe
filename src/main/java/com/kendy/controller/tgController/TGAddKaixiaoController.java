package com.kendy.controller.tgController;

import com.kendy.db.entity.Player;
import com.kendy.db.service.PlayerService;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.DataConstans;
import com.kendy.controller.BaseController;
import com.kendy.controller.MyController;
import com.kendy.db.DBService;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.util.CollectUtil;
import com.kendy.util.MapUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

/**
 * 新增托管开销控制类
 *
 * @author 林泽涛
 * @time 2018年3月3日 下午2:25:46
 */
@Component
public class TGAddKaixiaoController extends BaseController implements Initializable {

  @Autowired
  public DBService dbService;
  @Autowired
  public MyController myController;
  @Autowired
  public TGController tgController; // 托管控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Resource
  PlayerService playerService;
  
  @FXML
  private TextField searchField; // 玩家名称(模糊搜索)

  @FXML
  private TextField FinalPlaerNameField; // 玩家名称

  @FXML
  private ListView<String> playersView; // 待补充的玩家视图

  @FXML
  private ChoiceBox<String> payItemsChoice; // 支出项目

  @FXML
  private ChoiceBox<String> tgCompanyChoice; // 托管公司

  @FXML
  private TextField kaixiaoMoneyField; // 开销金额

  private static final String PAY_ITEMS_DB_KEY = "tg_pay_items"; // 保存到数据库的key

  public static List<String> payItems = new ArrayList<>();

  private static List<Player> players = new ArrayList<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 获取人员数据
    initPlayers();

    // 添加文本框监听
    addListener();

    // 初始化支出项目数据
    initPayItemChoice();

    // 初始化托管项目数据
    initTGCompanyChoice();

    // 自动选值第一个
    if (CollectUtil.isHaveValue(payItems)) {
      payItemsChoice.getSelectionModel().select(0);
    }

  }

  /**
   * 获取人员数据
   *
   * @time 2018年3月3日
   */
  private void initPlayers() {
    Map<String, Player> membersMap = dataConstants.membersMap;
    if (MapUtil.isHavaValue(membersMap)) {
      players = new ArrayList<>(membersMap.values());
    }
  }

  /**
   * 添加文本框监听(监听输入框和ListView点击框)
   *
   * @time 2018年3月3日
   */
  private void addListener() {
    // 监听输入框
    searchField.textProperty().addListener(event -> {
      String text = searchField.getText();
      List<String> playerNames =
          players.parallelStream().filter(info -> info.getPlayername().contains(text))
              .map(Player::getPlayername).collect(Collectors.toList());
      playersView.setItems(FXCollections.observableArrayList(playerNames));
    });

    // 监听ListView点击框
    playersView.getSelectionModel().selectedItemProperty().addListener(event -> {
      String text = playersView.getSelectionModel().getSelectedItem();
      if (StringUtil.isBlank(text)) {
        FinalPlaerNameField.setText("");
      } else {
        FinalPlaerNameField.setText(text);
      }
    });
  }

  /**
   * 初始化支出项目数据
   *
   * @time 2018年3月3日
   */
  private void initPayItemChoice() {
    String payItemsJson = dbService.getValueByKey(PAY_ITEMS_DB_KEY);
    if (StringUtil.isNotBlank(payItemsJson) && !"{}".equals(payItemsJson)) {
      payItems = JSON.parseObject(payItemsJson, new TypeReference<List<String>>() {
      });
    } else {
      if (payItems == null || payItems.isEmpty()) {
        payItems = new ArrayList<>(Arrays.asList("推荐奖励", "金币", "打牌奖励"));
        savePayItem();
      }
    }
    payItemsChoice.setItems(FXCollections.observableArrayList(payItems));
  }

  /**
   * 初始化托管公司名称项目数据
   *
   * @time 2018年3月3日
   */
  private void initTGCompanyChoice() {
    List<TGCompanyModel> tgCompanys =
        dbService.get_all_tg_company_By_clubId(myController.currentClubId.getText());
    List<String> nameList = new ArrayList<>();
    if (CollectUtil.isHaveValue(tgCompanys)) {
      nameList =
          tgCompanys.stream().map(TGCompanyModel::getTgCompanyName).collect(Collectors.toList());
    }
    tgCompanyChoice.setItems(FXCollections.observableArrayList(nameList));
  }


  /**
   * 添加支出项目
   *
   * @time 2018年3月3日
   */
  public void addPayItemAction(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("添加");
    dialog.setHeaderText(null);
    dialog.setContentText("新增支出项目:");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String newPayItem = result.get();
      if (payItems.contains(newPayItem)) {
        ShowUtil.show("已经存在支出项目：" + newPayItem);
      } else {
        // 修改界面和缓存
        payItems.add(newPayItem);
        payItemsChoice.setItems(FXCollections.observableArrayList(payItems));
        // 更新到数据库
        savePayItem();
        // 刷新
        initPayItemChoice();
        // 自动选值
        payItemsChoice.getSelectionModel().select(newPayItem);
      }
    }
  }

  /**
   * 保存支出项目到数据库
   *
   * @time 2018年3月3日
   */
  private void savePayItem() {
    String payItemsJson = JSON.toJSONString(payItems);
    dbService.saveOrUpdateOthers(PAY_ITEMS_DB_KEY, payItemsJson);
  }

  /**
   * 检验参数
   *
   * @time 2018年3月3日
   */
  private boolean hasAnyParamBlank() {
    return StringUtil.isAnyBlank(FinalPlaerNameField.getText(), kaixiaoMoneyField.getText(),
        payItemsChoice.getSelectionModel().getSelectedItem(),
        tgCompanyChoice.getSelectionModel().getSelectedItem());
  }

  /**
   * 获取待提交的数据
   *
   * @time 2018年3月3日
   */
  private TGKaixiaoInfo getSubmitData() {

    String tgCompany = tgCompanyChoice.getSelectionModel().getSelectedItem();

    TGKaixiaoInfo TGKaixiaoEntity =
        new TGKaixiaoInfo(UUID.randomUUID().toString(), tgController.getDateString(),
            FinalPlaerNameField.getText(), payItemsChoice.getSelectionModel().getSelectedItem(),
            kaixiaoMoneyField.getText(), tgCompany);
    return TGKaixiaoEntity;
  }


  /**
   * 按钮：确定提交托管开销数据
   *
   * @time 2018年3月3日
   */
  public void addTGKaixiaoBtnAction(ActionEvent event) {
    // 检验参数
    if (hasAnyParamBlank()) {
      ShowUtil.show("Sorry, 提交信息不完整，请查看！");
      return;
    }
    // 传递给主控制类处理逻辑 TODO
    TGKaixiaoInfo TGKaixiaoEntity = getSubmitData();
    //TGController tgController = MyController.tgController;
    // 保存到数据库
    dbService.saveOrUpdate_tg_kaixiao(TGKaixiaoEntity);
    // 刷新界面
    if (equalsCurrentCompany()) {
      tgController.refreshTableTGKaixiao();
    }

    ShowUtil.show("添加完成", 1);
  }

  /**
   * 添加开销时判断是否添加的公司与当前公司是否一致，一致则刷新当前页面
   *
   * @time 2018年3月24日
   */
  private boolean equalsCurrentCompany() {
    //TGController tgController = MyController.tgController;
    String tgCompany = tgController.getCurrentTGCompany();
    String selectedCompany = tgCompanyChoice.getSelectionModel().getSelectedItem();
    return tgCompany.equals(selectedCompany);
  }


  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}

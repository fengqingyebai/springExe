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
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGCompanyModel;
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
 * 新增玩家备注控制类
 *
 * @author 林泽涛
 * @time 2018年3月3日 下午2:25:46
 */
@Component
public class TGAddCommentController extends BaseController implements Initializable {


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
  private TextField FinalPlaerIdField; // 玩家ID

  @FXML
  private ListView<String> playersView; // 待补充的玩家视图

  @FXML
  private ChoiceBox<String> typeChoice; // 类别

  @FXML
  private ChoiceBox<String> tgCompanyChoice; // 托管公司

  @FXML
  private TextField IDField; // ID

  @FXML
  private TextField nameField; // 名称

  @FXML
  private TextField beizhuField; // 备注

  private static final String TG_WANGJIA_COMMENT_DB_KEY = "tg_wangjia_comment"; // 保存到数据库的key

  public static List<String> typeItems = new ArrayList<>();

  private static List<Player> players = new ArrayList<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 获取人员数据
    initPlayers();

    // 添加文本框监听
    addListener();

    // 初始化类别数据
    initTypeChoice();

    // 初始化托管项目数据
    initTGCompanyChoice();

    // 自动选值第一个
    if (CollectUtil.isHaveValue(typeItems)) {
      typeChoice.getSelectionModel().select(0);
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
      List<String> playerNames = players.parallelStream()
          .filter(info -> info.getPlayername().contains(text))
          .map(info -> info.getPlayername() + "##" + info.getPlayerid()).collect(Collectors.toList());
      playersView.setItems(FXCollections.observableArrayList(playerNames));
    });
  }

  /**
   * 初始化类别数据
   *
   * @time 2018年3月3日
   */
  private void initTypeChoice() {
    String typeItemsJson = dbService.getValueByKey(TG_WANGJIA_COMMENT_DB_KEY);
    if (StringUtil.isNotBlank(typeItemsJson) && !"{}".equals(typeItemsJson)) {
      typeItems = JSON.parseObject(typeItemsJson, new TypeReference<List<String>>() {
      });
    } else {
      if (typeItems == null || typeItems.isEmpty()) {
        typeItems = new ArrayList<>(Arrays.asList("推荐玩家", "改号", "小号"));
        saveTypeItem();
      }
    }
    typeChoice.setItems(FXCollections.observableArrayList(typeItems));
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
   * 添加类别选项
   *
   * @time 2018年3月3日
   */
  public void addTypeItemAction(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("添加");
    dialog.setHeaderText(null);
    dialog.setContentText("新增类别选项:");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      String newPayItem = result.get();
      if (typeItems.contains(newPayItem)) {
        ShowUtil.show("已经存在类别选项：" + newPayItem);
      } else {
        // 修改界面和缓存
        typeItems.add(newPayItem);
        typeChoice.setItems(FXCollections.observableArrayList(typeItems));
        // 更新到数据库
        saveTypeItem();
        // 刷新
        initTypeChoice();
        // 自动选值
        typeChoice.getSelectionModel().select(newPayItem);
      }
    }
  }

  /**
   * 保存类别选项到数据库
   *
   * @time 2018年3月3日
   */
  private void saveTypeItem() {
    String typeItemsJson = JSON.toJSONString(typeItems);
    dbService.saveOrUpdateOthers(TG_WANGJIA_COMMENT_DB_KEY, typeItemsJson);
  }

  /**
   * 检验参数
   *
   * @time 2018年3月3日
   */
  private boolean hasAnyParamBlank() {
    return StringUtil.isAnyBlank(FinalPlaerNameField.getText(), FinalPlaerIdField.getText(),
        typeChoice.getSelectionModel().getSelectedItem(), IDField.getText(), nameField.getText());
  }

  /**
   * 获取待提交的数据
   *
   * @time 2018年3月3日
   */
  private TGCommentInfo getSubmitData() {
    // 获取当前托管公司
    String tgCompany = tgCompanyChoice.getSelectionModel().getSelectedItem();

    TGCommentInfo entity =
        new TGCommentInfo(UUID.randomUUID().toString(), tgController.getDateString(),
            FinalPlaerIdField.getText(), FinalPlaerNameField.getText(),
            typeChoice.getSelectionModel().getSelectedItem(), IDField.getText(),
            nameField.getText(), StringUtil.nvl(beizhuField.getText(), ""), tgCompany);

    return entity;
  }


  /**
   * 按钮：确定提交托管开销数据
   *
   * @time 2018年3月3日
   */
  public void addTGCommentBtnAction(ActionEvent event) {
    // 检验参数
    if (hasAnyParamBlank()) {
      ShowUtil.show("Sorry, 提交信息不完整，请查看！");
      return;
    }
    // 传递给主控制类处理逻辑 TODO
    TGCommentInfo tgCommentInfo = getSubmitData();
    // TGController tgController = MyController.tgController;

    // 保存到数据库
    dbService.saveOrUpdate_tg_comment(tgCommentInfo);
    // 刷新界面
    if (equalsCurrentCompany()) {
      tgController.refreshTableTGComment();
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

  /**
   * 设置第一个
   *
   * @time 2018年3月22日
   */
  public void setFirstDataAction(ActionEvent event) {
    String text = playersView.getSelectionModel().getSelectedItem();
    if (StringUtil.isBlank(text)) {
      FinalPlaerNameField.setText("");
      FinalPlaerIdField.setText("");
    } else {
      FinalPlaerNameField.setText(text.split("##")[0]);
      FinalPlaerIdField.setText(text.split("##")[1]);
    }
  }

  /**
   * 设置第二个
   *
   * @time 2018年3月22日
   */
  public void setSecondDataAction(ActionEvent event) {
    String text = playersView.getSelectionModel().getSelectedItem();
    if (StringUtil.isBlank(text)) {
      nameField.setText("");
      IDField.setText("");
    } else {
      nameField.setText(text.split("##")[0]);
      IDField.setText(text.split("##")[1]);
    }
  }

  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}

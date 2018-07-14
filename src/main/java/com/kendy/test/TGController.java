package com.kendy.test;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.BankFlowController;
import com.kendy.controller.BaseController;
import com.kendy.controller.CombineIDController;
import com.kendy.controller.GDController;
import com.kendy.controller.LMController;
import com.kendy.controller.MyController;
import com.kendy.controller.QuotaController;
import com.kendy.controller.SMAutoController;
import com.kendy.controller.TeamProxyController;
import com.kendy.db.DBUtil;
import com.kendy.entity.ProxyTeamInfo;
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGFwfinfo;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.entity.TGLirunInfo;
import com.kendy.entity.TGTeamInfo;
import com.kendy.entity.TGTeamModel;
import com.kendy.entity.TypeValueInfo;
import com.kendy.excel.ExportExcelTemplate;
import com.kendy.interfaces.Entity;
import com.kendy.service.AutoDownloadZJExcelService;
import com.kendy.service.JifenService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TGExportExcelService;
import com.kendy.service.TGFwfService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.TgWaizhaiService;
import com.kendy.service.WaizhaiService;
import com.kendy.service.ZonghuiService;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.InputDialog;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import com.kendy.util.TableUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

/**
 * 处理联盟配额的控制器
 * 
 * @author 林泽涛
 * @time 2017年11月24日 下午9:31:04
 */
@Component
public class TGController {

  private   Logger log = Logger.getLogger(TGController.class);
  
  @Autowired
  private OrderService orderService;


  public TGController() {
    if(orderService == null) {
      log.info("在TGController中 orderservice is null");
    }else {
      log.info("在TGController中 orderservice is not null");
    }
  }
  
  @PostConstruct
  public void inits() {
    if(orderService == null) {
      log.info("在@PostConstructTGController中 orderservice is null");
    }else {
      log.info("在@PostConstructTGController中 orderservice is not null");
    }
  }

}

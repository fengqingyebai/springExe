package com.kendy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import com.kendy.util.ShowUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * 银行流水控制类
 * 
 * @author linzt
 * @time 2018年6月27日
 */
public class BankFlowController implements Initializable{
	
	
	private static Logger log = Logger.getLogger(BankFlowController.class);

//	//=====================================================================
//	@FXML public VBox TG_Company_VBox; // 托管公司（按钮）
	
	@FXML public ScrollPane scrollDates; // 
	@FXML public AnchorPane detailFlowDataAnchor; // 
	

	
//	@FXML private Label currentTGCompanyLabel; //当前托管公司
	@FXML private Label currentTGTeamLabel; //当前托管团队
	
    private static final String FX_TEXT_FILL_WHITE = "-fx-text-fill:BLACK";
    private static final String ANIMATED_OPTION_BUTTON = "animated-option-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON = "animated-option-sub-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON2 = "animated-option-sub-button2";
	

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    LocalDate localDate = LocalDate.now();
    VBox cacheContent = new VBox();

    cacheContent.setPrefHeight(1000);
    for(int i=0; i<50; i++) {
      Button sbutton = new Button(localDate.minusDays(i).toString().replace("2018-", ""));
      sbutton.setPrefWidth(100);
      sbutton.getStyleClass().add(ANIMATED_OPTION_BUTTON);
  //    sbutton.setStyle("-fx-font-size: 23px");
  //    sbutton.setButtonType(ButtonType.RAISED);
  //    sbutton.getStyleClass().addAll(ANIMATED_OPTION_BUTTON, ANIMATED_OPTION_SUB_BUTTON);
      sbutton.setOnAction(e->{
        ShowUtil.show(sbutton.getText(), 1);
      });
      TableView table = new TableView();
      table.setPrefHeight(200);
      table.setMinHeight(200);
      table.getColumns().add(new TableColumn(i+""));
      cacheContent.getChildren().add(sbutton);
      //cacheContent.setVgrow(table, Priority.ALWAYS);
      cacheContent.getChildren().add(table);
//      detailFlowDataAnchor.getChildren().add(table);
   }
   // nodesList.setRotate(-90);
  
    scrollDates.setPadding(new Insets(10));
    scrollDates.setFitToHeight(true);
    scrollDates.setFitToWidth(true);

   // cacheContent.getChildren().addAll(nodesList);
    scrollDates.setContent(cacheContent);//.add(nodesList);
    
  }
	
//	@FXML public TextField tgTeamHSRate; //托管团队回水比例
    
    
    
	
}

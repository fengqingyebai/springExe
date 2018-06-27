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

	//=====================================================================
	@FXML public ScrollPane scrollDates; // 
	

	
//	@FXML private Label currentTGCompanyLabel; //当前托管公司
	@FXML private Label currentTGTeamLabel; //当前托管团队
	
	private static final String DATE_LABEL_CSS = "dateLabel";
    private static final String FX_TEXT_FILL_WHITE = "-fx-text-fill:BLACK";
    private static final String ANIMATED_OPTION_BUTTON = "animated-option-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON = "animated-option-sub-button";
    private static final String ANIMATED_OPTION_SUB_BUTTON2 = "animated-option-sub-button2";
	

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    LocalDate localDate = LocalDate.now();
    VBox cacheContent = new VBox();

    cacheContent.setPrefHeight(1000);
    for(int i=0; i<20; i++) {
      Label dateLabel = new Label(localDate.minusDays(i).toString().replace("2018-", ""));
      dateLabel.setStyle("-fx-font-size: 1.75em");
      dateLabel.setStyle("-fx-text-fill:blue");
      TableView table = new TableView();
      table.setPrefHeight(200);
      table.setMinHeight(200);
      table.getColumns().add(new TableColumn(i+""));
      cacheContent.getChildren().add(dateLabel);
      cacheContent.getChildren().add(table);
   }
  
    scrollDates.setPadding(new Insets(10));
    scrollDates.setFitToHeight(true);
    scrollDates.setFitToWidth(true);

   // cacheContent.getChildren().addAll(nodesList);
    scrollDates.setContent(cacheContent);//.add(nodesList);
    
  }
	
//	@FXML public TextField tgTeamHSRate; //托管团队回水比例
    
    
    
	
}

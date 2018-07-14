package com.kendy.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBUtil;
import com.kendy.service.AutoDownloadZJExcelService;
import com.kendy.service.JifenService;
import com.kendy.service.MemberService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TGExportExcelService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.TgWaizhaiService;
import com.kendy.service.WaizhaiService;
import com.kendy.service.ZonghuiService;

public class InstancePool {

  
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  
  
  
  public InstancePool() {
    super();
//    logger.info("执行InstancePool构造方法");
//    if(dbUtil == null) {
//      //logger.info("dbUtil is null .....");
//    }
  }

//  @Autowired
//  public DBUtil dbUtil;
//  @Autowired
//  public DataConstans dataConstants; // 数据控制类
//  @Autowired
//  public MyController myController ;
//  @Autowired
//  public CombineIDController combineIDController ;
//  @Autowired
//  public BaseController baseController; // 基本控制类
//  @Autowired
//  public TeamProxyController teamProxyController; // 代理控制类
//  @Autowired
//  public TGController tgController; // 托管控制类
//  @Autowired
//  public SMAutoController smAutoController; // 托管控制类
//  @Autowired
//  public BankFlowController bankFlowController; // 银行流水控制类
//  @Autowired
//  public GDController gdController; // 股东控制类
//  @Autowired
//  public LMController lmController; // 联盟控制类
//  @Autowired
//  public QuotaController quotaController; // 配帐控制类
//  @Autowired
//  public AutoDownloadZJExcelService autoDownloadZJExcelService; // 自动下载服务类
//  @Autowired
//  public JifenService jifenService; // 积分控制类
//  @Autowired
//  public MemberService memberService; // 配帐控制类
//  @Autowired
//  public ShangmaService shangmaService; // 上码控制类
//  @Autowired
//  public TeamProxyService teamProxyService; // 配帐控制类
//  @Autowired
//  public TGExportExcelService tGExportExcelService; // 配帐控制类
//  @Autowired
//  public TgWaizhaiService tgWaizhaiService; // 配帐控制类
//  @Autowired
//  public WaizhaiService waizhaiService; // 配帐控制类
//  @Autowired
//  public ZonghuiService zonghuiService; // 配帐控制类
//  @Autowired
//  public MoneyService moneyService; // 配帐控制类
//  

  
  

  
}

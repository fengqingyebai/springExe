# 肯迪软件工作室
# financial(财务软件3.0)
## 一个尝试代替Excel做业务功能的Java桌面应用
## 背景介绍
    该公司之前的大部分财务工作是用Excel来完成的，每天最高会处理1500个Excel帐单文件，Excel页签不一。各个页签之间都是单元格公式进行相互关联。当相互之间的关联越来越多时，
    Excel的打开就会变得很慢，严重影响工作效率，也不利于业务的开展。因此开发一款可以替代Excel，完成财务统计工作的软件就称为迫切的需求。因此，
    财务软件应运而生。
## 建设目标
    开发一个可以支撑目前Excel汇总统计工作所有功能的财务软件。
    Excel能做的，财务软件也要能做：如总汇、个人当天结算、团队回水、前后表的衔接。
## 功能迁移
    Excel能做的，财务软件也要能做：如总汇、个人当天结算、团队回水、前后表的衔接。
## 部分效果
![](https://github.com/greatkendy123/springExe/raw/master/src/main/resources/images/4.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/7.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/1.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/2.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/5.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/6.png)
![](https://github.com/greatkendy123/springExe/raw/master/resources/images/3.png)

## 代码示例（自动配额）
	/**
	* 联盟自动配额
	* 这是本控制类最核心的代码
	* 算法：找剩余值中的两个最大最小值进行一方清零，不断循环
	* 
	* @time 2017年12月18日
	*/
    public void autoQuota() {
        
        if(TableUtil.isNullOrEmpty(tableQuota))
            return;
        
        boolean isDone = false;
        int count = 0;
        // TODO 目前只有5个对冲，如果超过5过需要调整对冲
        while(!isDone) {
            count++;
            ClubQuota row1 = getRecord(1);
            ClubQuota row2 = getRecord(0);
            
            //log.info(String.format("最大值：%s::%s", row1.getQuotaClubName(),row1.getQuotaRest()));
            //log.info(String.format("最小值：%s::%s", row2.getQuotaClubName(),row2.getQuotaRest()));
            
            Double first = NumUtil.getNum(row1.getQuotaRest());
            Double  second= NumUtil.getNum(row2.getQuotaRest());
            if(first * second >= 0) {
                isDone = true;
                log.info("=====================联盟配额结束！count:"+(count-1));
                break;
            }
            //转换（row1永远是绝对值的大数，row2是绝对值的小数  ）
            if( Double.compare(first, second) > 0 ) {
                if( Double.compare(Math.abs(first), Math.abs(second)) < 0 ) {
                    ClubQuota tempRow ;
                    tempRow = row1;
                    row1 = row2;
                    row2 = tempRow;
                    Double tempVal ;
                    tempVal = first;
                    first = second;
                    second = tempVal;
                    //log.info(String.format("转换最大值：%s::%s", row1.getQuotaClubName(),row1.getQuotaRest()));
                    //log.info(String.format("转换最小值：%s::%s", row2.getQuotaClubName(),row2.getQuotaRest()));
                }
            }
            
            //log.info(String.format("%s转%s到%s", from,money,to));
            addRecord2TableQuotaPay(new QuotaMoneyInfo(winner.getQuotaClubId(),from,money,to));
            
        }
        tableQuotaPay.refresh();
        tableQuota.refresh();
        //配额最后还有剩余为负数的则全部结转到当前俱乐部
        addNegativeRest2CurrentClub();
    }
    
    /**
     * 配额最后还有剩余为负数的则全部结转到银河ATM
     * @time 2017年12月18日
     */
    private void addNegativeRest2ATM() {
    	tableQuota.getItems()
    		.parallelStream()
    		.filter( info -> NumUtil.getNum(info.getQuotaRest()) < 0)
    		.forEach( info -> {
    			String from , to , money = "";
    			Club winnerClub = allClubMap.get("555551");//555551为银河ATM的俱乐部ID
    			to = winnerClub.getName();		
    			money = NumUtil.digit0((-1) * NumUtil.getNum(info.getQuotaRest()));
    			from = info.getQuotaClubName();
    			//log.info(String.format("%s转%s到%s", from,money,to));
    			addRecord2TableQuotaPay(new QuotaMoneyInfo(winnerClub.getClubId(),from,money,to));
    		});
    	tableQuotaPay.refresh();
    }
    
    /**
     * 往结账表新增一条记录
     * 应用场景：自动配额时，每配额一次就产生一条记录
     * @time 2017年12月18日
     * @param info
     */
    private void addRecord2TableQuotaPay(QuotaMoneyInfo info) {
    	ObservableList<QuotaMoneyInfo> obList = tableQuotaPay.getItems();
    	if(obList == null)  obList = FXCollections.observableArrayList();
    	obList.add(info);
    	tableQuotaPay.setItems(obList);
    }
    
	/**
	 * 获取剩余最大和最小的两行
	 * @time 2017年12月16日
	 * @param type 0:最小值  1：最大值 
	 * @return
	 */
    private ClubQuota getRecord(int type) {
        if(1 ==  type )
            return tableQuota.getItems().parallelStream().max((o1, o2) ->   
                NumUtil.getNum(o1.getQuotaRest()).compareTo(NumUtil.getNum(o2.getQuotaRest()))
            ).get();  
        else
            return tableQuota.getItems().parallelStream().min((o1, o2) ->   
                NumUtil.getNum(o1.getQuotaRest()).compareTo(NumUtil.getNum(o2.getQuotaRest()))
            ).get();  
    }

	/**
	 * 将原始数据转换成特定的数据结构
	 * {俱乐部ID:{团队ID:List<Record}}
	 * 
	 * @time 2018年1月19日
	 */
	private static void initClubTeamMap() {
		if(CollectUtil.isNullOrEmpty(dataList)) return;
		clubTeamMap = 
		dataList.stream()
			    .collect(Collectors.groupingBy(
			    		Record::getClubId, //先按俱乐部分
			    		Collectors.groupingBy(Record::getTeamId)));//再按团队分
	}
	
## 技术参考
    AwesomeJavaFX：https://github.com/mhrimaz/AwesomeJavaFX#tutorials
    JFoenix：https://github.com/jfoenixadmin/JFoenix

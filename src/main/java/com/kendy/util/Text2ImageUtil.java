package com.kendy.util;

import com.kendy.constant.Constants;
import com.kendy.entity.KeyValue;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import com.kendy.entity.WanjiaInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * 文字转图片工具类
 *
 * @author 林泽涛
 * @time 2017年10月7日 下午4:02:28
 */
public class Text2ImageUtil {

  public static void main(String[] args) throws Exception {
    WanjiaInfo wj = new WanjiaInfo("第120局", "kendy", "35", "100", "135", "15626460750");
    String html = getHtml(wj);
    BufferedImage img = toImage(html, "utf8", 400, 500);
    ImageIO.write(img, "png", new File("D:/picTest/" + System.currentTimeMillis() + ".png"));
    System.out.println("finishes...");
  }


  public static String getHtml(WanjiaInfo wj) {
    if (wj.getYicunJifen() == null) {
      wj.setYicunJifen("");
    }
    String html =
        "<style>.paiju{background-color:white;border:0px;}.td{border:1px black solid;}.table{border-collapse:collapse;}</style><body><table  width='400' class='table'>  <tr style='background-color:#EEE0E5'>    <th class='paiju'>"
            + wj.getPaiju()
            + "</th><th class='td'>玩家</th><th class='td'>战绩</th><th class='td'>联盟币</th><th class='td'>合计</th></tr><tr style='background-color:white'><th class='paiju'></th><th class='td'>"
            + wj.getWanjiaName() + "</th><th class='td'>" + wj.getZhangji()
            + "</th><th class='td'>" + wj.getYicunJifen() + "</th><th class='td'>"
            + wj.getHeji() + "</th></tr></table></body>";
    return html;
  }

  public static String getHtml2(List<KeyValue> list, String totalDesc) {
    StringBuilder sb = new StringBuilder();
    for (KeyValue keyValue : list) {
      String playerName = keyValue.getKey();
      String ssje = keyValue.getValue();
      //<tr><th>B</th><th class='red'>-1256</th></tr>
      String redCss = StringUtils.contains(ssje, "-") ? " class='red'" : "";
      sb.append("<tr><th>").append(playerName).append("</th>")
          .append("<th ").append(redCss).append(">").append(ssje).append("</th></tr>");
    }
    String nagativeCss = StringUtils.contains(totalDesc, "-") ? Constants.RED : Constants.ORANGE;
    String html =
        "<style> table,table tr th {border:2px solid #039ede;} table {width:300px;text-align:center;padding:0;border-color:#b6ff00;border-collapse:collapse;}.bigDiv{border:solid 1px #d0d0d0;width:300px;}.sum{font-size:50px;font-weight:bold;color:"
            + nagativeCss
            + ";margin-bottom:10px;}.firstRow{background-color:#e9e9e9;}.red{color:#d82608;}</style><body><div class='bigDiv'><div class='sum'>"
            + totalDesc + "</div><div><table><tr class='firstRow'><th>玩家名称</th><th>资金</th></tr>"
            + sb.toString() + "</table> </div> </div> </body>";

    return html;
  }


  /**
   * 文本转图片自制方法：解析html文本并渲染成图片
   *
   * 原理：使用Jsoup解析html文本，再用JDK自带的JEditorPane组件渲染Html，最后将其保存成图片 问题：渲染gif图时，gif图会比较大，后期待处理
   *
   * @param html 待渲染的Html文本
   * @return 图片缓冲
   * @author 林泽涛
   */
  public static BufferedImage toImage(String html, String sysCode, int width, int height)
      throws Exception {

    // 加上<html>节点使其更像一个网页源代码
    html = "<html>" + html + "</html>";

    // JEditorPane组件设置
    JEditorPane editorPane = new JEditorPane();
    editorPane.setEditable(false);
    editorPane.setSize(width, height); // 750是模拟淘宝文本编辑器的宽度
    editorPane.setContentType("text/html");// 必填，它会根据这个类型去渲染html
    editorPane.read(new ByteArrayInputStream(html.getBytes(sysCode)), "");// 备注，后期看下什么原因 GBK

    // 截图
    Dimension prefSize = editorPane.getSize(); // 获取JEditorPane的大小
    BufferedImage img = new BufferedImage(prefSize.width, editorPane.getPreferredSize().height,
        // 使高度自动化
        BufferedImage.TYPE_INT_BGR);

    // 渲染html文本到缓冲区
    Graphics graphics = img.getGraphics();
    editorPane.setSize(prefSize);
    editorPane.paint(graphics);

    return img;
  }

}

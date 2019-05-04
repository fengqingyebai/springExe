package kendy.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linzt
 * @date
 */
public class ExcelGenerator {

  static Logger logger = LoggerFactory.getLogger(ExcelGenerator.class);


  public static void main(String[] args) {
//String str = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n"
//    + "{lmSumName_0_0}：\t{lmSumZJ_0_0}\t\t\t\t{lmSumName_1_0}：\t{lmSumZJ_1_0}\t\t\t\t{lmSumName_2_0}：\t{lmSumZJ_2_0}\t\t\t\t{lmSumName_3_0}：\t{lmSumZJ_3_0}\t\t\n"
//    + "{lmSumName_0_1}：\t{lmSumZJ_0_1}\t\t\t\t{lmSumName_1_1}：\t{lmSumZJ_1_1}\t\t\t\t{lmSumName_2_1}：\t{lmSumZJ_2_1}\t\t\t\t{lmSumName_3_1}：\t{lmSumZJ_3_1}\t\t\n"
//    + "{lmSumName_0_2}：\t{lmSumZJ_0_2}\t{lmSumInsure_0_2}\t{lmSumPersonCount_0_2}\t\t{lmSumName_1_2}：\t{lmSumZJ_1_2}\t{lmSumInsure_1_2}\t{lmSumPersonCount_1_2}\t\t{lmSumName_2_2}：\t{lmSumZJ_2_2}\t{lmSumInsure_2_2}\t{lmSumPersonCount_2_2}\t\t{lmSumName_3_2}：\t{lmSumZJ_3_2}\t{lmSumInsure_3_2}\t{lmSumPersonCount_3_2}\n"
//    + "{clubName0}\t\t保险\t人数\t\t{clubName1}\t\t保险\t人数\t\t{clubName2}\t\t保险\t人数\t\t{clubName3}\t\t保险\t人数\n";
//
//    System.out.println(str);


    generator1(60);



  }


  /**
   * 自动生成联盟总帐
   * @param index
   */
  private static void generator1(int index) {
    for (int i = 0; i < index; i+=4) {
      int zero = 0;
      int one = 1;
      int tow = 2;
      int a = i ;
      int b = i + 1 ;
      int c = i + 2 ;
      int d = i + 3 ;
      String format = String.format(
          "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n"
              + "{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\n"
              + "{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t\t\n"
              + "{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t{lmSumInsure_%d_%d}\t{lmSumPersonCount_%d_%d}\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t{lmSumInsure_%d_%d}\t{lmSumPersonCount_%d_%d}\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t{lmSumInsure_%d_%d}\t{lmSumPersonCount_%d_%d}\t\t{lmSumName_%d_%d}：\t{lmSumZJ_%d_%d}\t{lmSumInsure_%d_%d}\t{lmSumPersonCount_%d_%d}\n"
              + "{clubName%d}\t\t保险\t人数\t\t{clubName%d}\t\t保险\t人数\t\t{clubName%d}\t\t保险\t人数\t\t{clubName%d}\t\t保险\t人数\n",
          a, zero, a, zero,
          b, zero, b, zero,
          c, zero, c, zero,
          d, zero, d, zero,
          // ===============
          a, one, a, one,
          b, one, b, one,
          c, one, c, one,
          d, one, d, one,
          // ===============
          a, tow, a, tow, a, tow, a, tow,
          b, tow, b, tow, b, tow, b, tow,
          c, tow, c, tow, c, tow, c, tow,
          d, tow, d, tow, d, tow, d, tow,
          // ===============
          a, b, c, d
      );
      System.out.print(format);
    }
  }


  private static void generator(int index) {
    for (int i = 0; i < index; i+=4) {

      String a = i + "";
      String b = i + 1 + "";
      String c = i + 2 + "";
      String d = i + 3 + "";
      String format = String.format(
          "#lm_name_%s_0\t#sum_jiaoshou_%s_0\t#sum_baoxian_%s_0\t#sum_renci_%s_0\t#sum_zj_%s_0\t\t#lm_name_%s_0\t#sum_jiaoshou_%s_0\t#sum_baoxian_%s_0\t#sum_renci_%s_0\t#sum_zj_%s_0\t\t#lm_name_%s_0\t#sum_jiaoshou_%s_0\t#sum_baoxian_%s_0\t#sum_renci_%s_0\t#sum_zj_%s_0\t\t#lm_name_%s_0\t#sum_jiaoshou_%s_0\t#sum_baoxian_%s_0\t#sum_renci_%s_0\t#sum_zj_%s_0\n"
              + "日期\t交收\t保险\t人次\t战绩\t\t日期\t交收\t保险\t人次\t战绩\t\t日期\t交收\t保险\t人次\t战绩\t\t日期\t交收\t保险\t人次\t战绩\n"
              + "#static_time_%s_0\t#jiaoshou_%s_0\t#baoxian_%s_0\t#renci_%s_0\t#zj_%s_0\t\t#static_time_%s_0\t#jiaoshou_%s_0\t#baoxian_%s_0\t#renci_%s_0\t#zj_%s_0\t\t#static_time_%s_0\t#jiaoshou_%s_0\t#baoxian_%s_0\t#renci_%s_0\t#zj_%s_0\t\t#static_time_%s_0\t#jiaoshou_%s_0\t#baoxian_%s_0\t#renci_%s_0\t#zj_%s_0\n",
          a, a, a, a, a,
          b, b, b, b, b,
          c, c, c, c, c,
          d, d, d, d, d,
          a, a, a, a, a,
          b, b, b, b, b,
          c, c, c, c, c,
          d, d, d, d, d
      );
      System.out.println(format);
      System.out.println(System.lineSeparator());
      System.out.println(System.lineSeparator());
      System.out.println(System.lineSeparator());
    }
  }

}

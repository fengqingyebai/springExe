package kendy.excel;

import com.kendy.excel.myExcel4j.MyExcelUtils;
import java.io.IOException;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * @author linzt
 * @date 2018-12-15
 */
public class ColumnTest {

  public static void main(String[] args)
      throws InvalidFormatException, IOException, com.kendy.excel.myExcel4j.exceptions.Excel4JException {
    String excelPath = "E:\\财务软件\\加勒比.xlsx";
    List<ExcelRecord> records = MyExcelUtils.getInstance().readExcel2Objects(excelPath, ExcelRecord.class, 1, 0);

    System.out.println("finishes...");
  }

}



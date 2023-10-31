package utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ClassName: ExcelOpections
 * Package: utils.excel
 * Description:
 *
 * @Author: 闫守瑞
 * @Create: 2023/10/26 - 14:10
 * @Version: v1.0
 */
public class ExcelOpections {


    /**
     * 从Excel文件中读取第二列的数据，并返回一个字符串数组。
     *
     * @param filePath Excel文件路径
     * @return 包含第二列数据的字符串数组
     */
    public static String[] readSecondColumn(String filePath) {
        List<String> strings = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell cell = row.getCell(1); // 获取第二列单元格

                if (cell != null) {
                    String value = "";
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            value = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            value = cell.getCellFormula();
                            break;
                        default:
                            value = "";
                    }
                    strings.add(value);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings.toArray(new String[0]);
    }

    /**
     * 读取 Excel 表格中的第一行第一列
     *
     * @param filePath Excel 文件路径
     * @return 第一行第一列的值
     */
    public static String readFirstCell(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            Row row = sheet.getRow(0); //获取第一行
            Cell cell = row.getCell(0); //获取第一列

            // 根据单元格类型获取单元格的值
            String value = "";
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        value = String.valueOf(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        value = cell.getCellFormula();
                        break;
                    default:
                        value = "";
                }
            }
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

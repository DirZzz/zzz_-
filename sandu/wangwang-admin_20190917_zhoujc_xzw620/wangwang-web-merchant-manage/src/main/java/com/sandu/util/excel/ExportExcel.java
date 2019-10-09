package com.sandu.util.excel;

import com.google.common.base.Strings;
import com.sandu.annotation.HSSARow;
import com.sandu.annotation.HSSFAFont;
import com.sandu.annotation.HSSFColumn;
import com.sandu.annotation.HSSFStyle;
import com.sandu.api.goods.output.GoodsSKUExportVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
/**
 * @ClassName: ExcelUtil
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: xxxx
 * @date: 2017年2月22日 下午1:21:45
 * @Copyright: 2017 www.xxxx.com Inc. All rights reserved.
 * 注意：本内容仅限于xxxx内部传阅，禁止外泄以及用于其他的商业目
 */
public class ExportExcel {

    @Value("${server.url}")
    private String serverPath;

    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String columnNames[]) {

        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
            if (columnNames[i].startsWith("pic_mark")) {
                //包含图片列，行高设置
                columnNames[i] = columnNames[i].replace("pic_mark", "");
//                sheet.setColumnWidth((short) i, (short) (35.7 * 150 * 2));
                sheet.setDefaultRowHeight((short) 1500);
            }
        }

        // 创建第一行
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
//        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        Drawing drawing = sheet.createDrawingPatriarch();
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                if (list.get(i).get(keys[j]) instanceof byte[]) {
                    //处理图片列
                    byte[] bytes = (byte[]) list.get(i).get(keys[j]);
                    if (bytes.length > 0) {
                        ClientAnchor clientAnchor = wb.getCreationHelper().createClientAnchor();
                        clientAnchor.setRow1(i);
                        clientAnchor.setCol1(j);
                        int index = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                        Picture picture = drawing.createPicture(clientAnchor, index);
                        picture.resize(1, 1);
                    }
                } else {
                    cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                }
                cell.setCellStyle(cs2);
            }
        }

        return wb;
    }


    public static Workbook createWorkBookTwo(List<Map<String, Object>> list, String[] keys, String columnNames[], List<Map<String, Object>> countList) {

        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }
        // 创建第一行
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            org.apache.poi.ss.usermodel.Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        for (Map<String, Object> m : countList) {
            for (Map.Entry<String, Object> e : m.entrySet()) {
                if (e.getKey().toString().equals("state") && e.getValue().equals(0)) {
                    org.apache.poi.ss.usermodel.Row row2 = sheet.createRow((short) list.size() + 1);
                    Cell cell = row2.createCell((short) 0);
                    cell.setCellValue("未上架");
                    Cell cell2 = row2.createCell((short) 1);
                    cell2.setCellValue(m.get("count").toString());
                }
                if (e.getKey().toString().equals("state") && e.getValue().equals(1)) {
                    org.apache.poi.ss.usermodel.Row row3 = sheet.createRow((short) list.size() + 2);
                    Cell cell = row3.createCell((short) 0);
                    cell.setCellValue("已上架");
                    Cell cell3 = row3.createCell((short) 1);
                    cell3.setCellValue(m.get("count").toString());
                }
                if (e.getKey().toString().equals("state") && e.getValue().equals(2)) {
                    org.apache.poi.ss.usermodel.Row row4 = sheet.createRow((short) list.size() + 3);
                    Cell cell = row4.createCell((short) 0);
                    cell.setCellValue("测试中");
                    Cell cell4 = row4.createCell((short) 1);
                    cell4.setCellValue(m.get("count").toString());

                }
                if (e.getKey().toString().equals("state") && e.getValue().equals(3)) {
                    org.apache.poi.ss.usermodel.Row row5 = sheet.createRow((short) list.size() + 4);
                    Cell cell = row5.createCell((short) 0);
                    cell.setCellValue("已发布");
                    Cell cell5 = row5.createCell((short) 1);
                    cell5.setCellValue(m.get("count").toString());
                }
                if (e.getKey().toString().equals("state") && e.getValue().equals(4)) {
                    org.apache.poi.ss.usermodel.Row row6 = sheet.createRow((short) list.size() + 5);
                    Cell cell = row6.createCell((short) 0);
                    cell.setCellValue("已下架");
                    Cell cell6 = row6.createCell((short) 1);
                    cell6.setCellValue(m.get("count").toString());
                }
                if (e.getKey().toString().equals("state") && e.getValue().equals(5)) {
                    org.apache.poi.ss.usermodel.Row row7 = sheet.createRow((short) list.size() + 6);
                    Cell cell = row7.createCell((short) 0);
                    cell.setCellValue("处理中");
                    Cell cell7 = row7.createCell((short) 1);
                    cell7.setCellValue(m.get("count").toString());
                }
            }
        }
        return wb;
    }

    public static Workbook createWorkBookThree(Map<String,List<Map<String, Object>>> list, Map<String,String[]> keys, Map<String,String[]> columnNames,String[] sheetName ){

        Workbook wb = new HSSFWorkbook();
        for(String str : sheetName) {
            if(Strings.isNullOrEmpty(str)){
                return wb;
            }
            Sheet sheet = wb.createSheet(str);
            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
            for (int i = 0; i < keys.get(str).length; i++) {
                sheet.setColumnWidth((short) i, (short) (35.7 * 150));
            }

            // 创建第一行
            org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);

            // 创建两种单元格格式
            CellStyle cs = wb.createCellStyle();
            CellStyle cs2 = wb.createCellStyle();

            // 创建两种字体
            Font f = wb.createFont();
            Font f2 = wb.createFont();

            // 创建第一种字体样式（用于列名）
            f.setFontHeightInPoints((short) 10);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);

            // 创建第二种字体样式（用于值）
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(IndexedColors.BLACK.getIndex());

            // 设置第一种单元格的样式（用于列名）
            cs.setFont(f);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setAlignment(CellStyle.ALIGN_CENTER);

            // 设置第二种单元格的样式（用于值）
            cs2.setFont(f2);
            cs2.setBorderLeft(CellStyle.BORDER_THIN);
            cs2.setBorderRight(CellStyle.BORDER_THIN);
            cs2.setBorderTop(CellStyle.BORDER_THIN);
            cs2.setBorderBottom(CellStyle.BORDER_THIN);
            cs2.setAlignment(CellStyle.ALIGN_CENTER);
            //设置列名
            for (int i = 0; i < columnNames.get(str).length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(columnNames.get(str)[i]);
                cell.setCellStyle(cs);
            }
            //设置每行每列的值
            if(list.get(str).size() == 0) {
                continue;
            }
            for (short i = 0; i < list.get(str).size(); i++) {

                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建一行，在页sheet上
                org.apache.poi.ss.usermodel.Row row1 = sheet.createRow((short) i+1);
                // 在row行上创建一个方格
                for (short j = 0; j < keys.get(str).length; j++) {
                    Cell cell = row1.createCell(j);
                    cell.setCellValue(list.get(str).get(i).get(keys.get(str)[j]) == null ? " " : list.get(str).get(i).get(keys.get(str)[j]).toString());
                    cell.setCellStyle(cs2);
                }
            }
        }
        return wb;
    }

    /**
     * 处理Cell内容
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    value = cell.getNumericCellValue() + "";
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    value = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
     /*   value = cell.getCellFormula() + "";*/
                    try {
                        Double num = cell.getNumericCellValue();
                        value = num.intValue() + "";
                    } catch (IllegalStateException e) {
                        value = cell.getRichStringCellValue() + "";
                    }
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    value = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    value = "非法字符";
                    break;
                default:
                    value = "未知类型";
                    break;
            }
        }
        return value.trim();
    }

    //判断行为空
    public static int CheckRowNull(org.apache.poi.ss.usermodel.Row row) {
        int num = 0;
        Iterator<Cell> cellItr = row.iterator();
        while (cellItr.hasNext()) {
            Cell c = cellItr.next();
            if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                num++;
            }
        }
        return num;
    }

    public static <T> HSSFWorkbook createExcel(List<T> list) {
        // 参数校验
        if (list == null || list.size() <= 0) {
            return null;
        }
        Class clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return null;
        }
        // 创建Excel文件
        HSSFWorkbook book = new HSSFWorkbook();
        // 创建sheet标签页
        HSSFSheet sheet = book.createSheet("默认标签页");
        // 声明行对象
        HSSFRow row = null;
        // 声明列对象
        HSSFCell cell = null;
        // 列样式
        List<HSSFCellStyle> colStyles = getColStyles(book, fields);
        // 行样式
        List<HSSFCellStyle> rowStyles = getRowStyles(book, fields);
        // 创建标题
        row = sheet.createRow(0);
        // 列宽度
        int[] width = new int[fields.length];
        for (int j = 0, c = 0; j < fields.length; j++) {
            Field field = fields[j];
            // 加了注解的话标题名称为注解的值
            boolean isAnnotation = field.isAnnotationPresent(HSSFColumn.class);
            if (isAnnotation) {
                // 创建单元格样式
                HSSFCellStyle style = book.createCellStyle();
                // 创建一个居中格式
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // 设置字体加粗
                HSSFFont font = book.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font.setFontHeightInPoints((short) 12);
                style.setFont(font);
                // 创建标题
                cell = row.createCell(c);
                // 获取注解
                HSSFColumn annotation = field.getAnnotation(HSSFColumn.class);
                cell.setCellValue(annotation.title());
                cell.setCellStyle(style);
                // 自动调整宽度
                if (annotation.autoWidth()) {
                    if (annotation.title() != null) {
                        if (annotation.title().toString().getBytes().length * 2 * 256 > width[c]) {
                            sheet.setColumnWidth(c, annotation.title().toString().getBytes().length * 2 * 256);
                            width[c] = annotation.title().toString().getBytes().length * 2 * 256;
                        }
                    }
                }
                c++;
            } else {
                continue;
            }
        }
        // 创建内容
        HSSFCellStyle colStyle = null;
        HSSFCellStyle rowStyle = null;
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0, c = 0, r = 0; j < fields.length; j++) {
                Field field = fields[j];
                // 添加了HSSFColumn注解的字段则导出
                boolean isAnnotation = field.isAnnotationPresent(HSSFColumn.class);
                if (isAnnotation) {
                    try {
                        // 获取当前列列样式
                        colStyle = colStyles.get(c);
                        field.setAccessible(true);
                        Object value = field.get(list.get(i));
                        // 给单元格赋值
                        cell = row.createCell(c);
                        if (value != null) {
                            if (value instanceof Date){
                                cell.setCellValue((Date) value);
                            } else if (value instanceof Calendar){
                                cell.setCellValue((Calendar)value);
                            } else if (value instanceof RichTextString){
                                cell.setCellValue((RichTextString)value);
                            }else {
                                cell.setCellValue(value.toString());
                            }
                        }
                        // 设置列样式
                        cell.setCellStyle(colStyle);
                        // 自动调整宽度
                        HSSFColumn hssfColumn = field.getAnnotation(HSSFColumn.class);
                        if (hssfColumn.autoWidth()) {
                            if (value != null) {
                                if (value.toString().getBytes().length * 2 * 256 > width[c]) {
                                    if(value.toString().getBytes().length * 2 > 255) {
                                        //防止单元格过大，报单元格最大列宽255错误
                                        sheet.setColumnWidth(c, 6000);
                                        width[c] = 6000;
                                    }else {
                                        sheet.setColumnWidth(c, value.toString().getBytes().length * 2 * 256);
                                        width[c] = value.toString().getBytes().length * 2 * 256;
                                    }
                                   
                                }
                            }
                        }
                        c++;
                        // 设置行样式
                        boolean isRowAnnotation = field.isAnnotationPresent(HSSARow.class);
                        if (isRowAnnotation) {
                        	HSSARow hssfRow =
                                    field.getAnnotation(HSSARow.class);
                            if (field.get(list.get(i)) != null) {
                                // 匹配正则的行设置样式
                                if (Pattern.matches(hssfRow.pattern(), field.get(list.get(i)).toString())) {
                                    rowStyle = rowStyles.get(r);
                                    cell.setCellStyle(rowStyle);
                                }
                            }
                            r++;
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return book;
    }

    private static List<HSSFCellStyle> getColStyles(HSSFWorkbook book, Field[] fields) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        HSSFDataFormat format = book.createDataFormat();
        List<HSSFCellStyle> colStyles = new ArrayList<>();
        for (int j = 0; j < fields.length; j++) {
            Field field = fields[j];
            boolean colFlag = field.isAnnotationPresent(HSSFColumn.class);
            if (colFlag) {
                HSSFCellStyle hssfStyle = book.createCellStyle();
                HSSFColumn column = field.getAnnotation(HSSFColumn.class);
                HSSFAFont font = column.font();
                HSSFFont hssfFont = book.createFont();
                hssfFont.setBoldweight(font.bold());
                hssfFont.setCharSet(font.charset());
                hssfFont.setFontHeightInPoints(font.height());
                hssfFont.setFontName(font.name());
                hssfFont.setItalic(font.italic());
                hssfFont.setColor(font.color());
                hssfFont.setUnderline(font.underline());
                hssfStyle.setFont(hssfFont);

                HSSFStyle style = column.style();
                hssfStyle.setAlignment(style.align());
                hssfStyle.setVerticalAlignment(style.verticalAlign());
                hssfStyle.setBorderTop(style.topBorder());
                hssfStyle.setBorderBottom(style.bottomBorder());
                hssfStyle.setBorderLeft(style.leftBorder());
                hssfStyle.setBorderRight(style.rightBorder());
                hssfStyle.setTopBorderColor(style.topBorderColor());
                hssfStyle.setBottomBorderColor(style.bottomBorderColor());
                hssfStyle.setLeftBorderColor(style.leftBorderColor());
                hssfStyle.setRightBorderColor(style.rightBorderColor());
                if (!"".endsWith(style.dataFormat())) {
                    hssfStyle.setDataFormat(format.getFormat(style.dataFormat()));
                }
                hssfStyle.setFillPattern(style.fillPattern());
                hssfStyle.setFillBackgroundColor(style.fillBackgorundColor());
                hssfStyle.setFillForegroundColor(style.fillForegroundColor());
                hssfStyle.setShrinkToFit(style.shrinkToFit());
                hssfStyle.setWrapText(style.wrapText());

                colStyles.add(hssfStyle);
            }
        }
        return colStyles;
    }

    private static List<HSSFCellStyle> getRowStyles(HSSFWorkbook book, Field[] fields) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        HSSFDataFormat format = book.createDataFormat();
        List<HSSFCellStyle> rowStyles = new ArrayList<>();
        for (int j = 0; j < fields.length; j++) {
            Field field = fields[j];
            boolean colFlag = field.isAnnotationPresent(HSSFColumn.class);
            if (colFlag) {
                boolean rowFlag = field.isAnnotationPresent(HSSARow.class);
                if (rowFlag) {
                    HSSFCellStyle hssfStyle = book.createCellStyle();
                    HSSARow row = field.getAnnotation(HSSARow.class);
                    HSSFAFont font = row.font();
                    HSSFFont hssfFont = book.createFont();
                    hssfFont.setBoldweight(font.bold());
                    hssfFont.setCharSet(font.charset());
                    hssfFont.setFontHeightInPoints(font.height());
                    hssfFont.setFontName(font.name());
                    hssfFont.setItalic(font.italic());
                    hssfFont.setColor(font.color());
                    hssfFont.setUnderline(font.underline());
                    hssfStyle.setFont(hssfFont);

                    HSSFStyle style = row.style();
                    hssfStyle.setAlignment(style.align());
                    hssfStyle.setVerticalAlignment(style.verticalAlign());
                    hssfStyle.setBorderTop(style.topBorder());
                    hssfStyle.setBorderBottom(style.bottomBorder());
                    hssfStyle.setBorderLeft(style.leftBorder());
                    hssfStyle.setBorderRight(style.rightBorder());
                    hssfStyle.setTopBorderColor(style.topBorderColor());
                    hssfStyle.setBottomBorderColor(style.bottomBorderColor());
                    hssfStyle.setLeftBorderColor(style.leftBorderColor());
                    hssfStyle.setRightBorderColor(style.rightBorderColor());
                    if (!"".endsWith(style.dataFormat())) {
                        hssfStyle.setDataFormat(format.getFormat(style.dataFormat()));
                    }
                    hssfStyle.setFillPattern(style.fillPattern());
                    hssfStyle.setFillBackgroundColor(style.fillBackgorundColor());
                    hssfStyle.setFillForegroundColor(style.fillForegroundColor());
                    hssfStyle.setShrinkToFit(style.shrinkToFit());
                    hssfStyle.setWrapText(style.wrapText());

                    rowStyles.add(hssfStyle);
                }
            }
        }
        return rowStyles;
    }

    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String []title, List<GoodsSKUExportVo> list, HSSFWorkbook wb,String basePath){
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 650);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            sheet.setColumnWidth(i, 6000);
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            HSSFFont font = wb.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 15);//设置字体大小
            style.setFont(font);
            cell.setCellStyle(style);
        }
        BufferedImage bufferImg = null;//图片
        try {
            //创建内容
            HSSFCellStyle styleCon = wb.createCellStyle();
            styleCon.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            styleCon.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            for(int i=0;i<list.size();i++){
                row = sheet.createRow(i + 1);
                row.setHeight((short) 550);
                GoodsSKUExportVo keeSpecimen = list.get(i);
                //将内容按顺序赋给对应的列对象
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                //将两张图片读到BufferedImage
                String qrcode = basePath + keeSpecimen.getSpePic();
                if (new File(qrcode).exists()) {
                    //String qrcode2 = basePath + "/AA/c_basedesign/2019/8/7/15/product/baseProduct/piclist/20190807150945765.jpg";
                    bufferImg = ImageIO.read(new File(qrcode));
                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                    //图片一导出到单元格F2中
                    HSSFClientAnchor anchor = new HSSFClientAnchor(400, 30, 700, 220, (short) 5, i+1, (short) 5, i+1);
                    // 插入图片
                    patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                cell = row.createCell(0);cell.setCellValue(keeSpecimen.getSpuCode());
                cell.setCellStyle(styleCon);
                cell = row.createCell(1);cell.setCellValue(keeSpecimen.getSpuName());
                cell.setCellStyle(styleCon);
                cell = row.createCell(2);cell.setCellValue(keeSpecimen.getModelNumber());
                cell.setCellStyle(styleCon);
                cell = row.createCell(3);
                cell.setCellValue(null == keeSpecimen.getSalePrice() ? "" : String.valueOf(keeSpecimen.getSalePrice()));
                cell.setCellStyle(styleCon);
                cell = row.createCell(4);
                String price = String.valueOf(keeSpecimen.getPrice());
                if (price.equals("0.0000")){
                    price = "";
                }
                cell.setCellValue(price);
                cell.setCellStyle(styleCon);
                cell = row.createCell(6);cell.setCellValue(keeSpecimen.getBigsmallType());
                cell.setCellStyle(styleCon);
                cell = row.createCell(7);cell.setCellValue(keeSpecimen.getInventory());
                cell.setCellStyle(styleCon);
                cell = row.createCell(8);cell.setCellValue(keeSpecimen.getIsPutaway());
                cell.setCellStyle(styleCon);
                cell = row.createCell(9);cell.setCellValue(keeSpecimen.getModelOrMaterial());
                cell.setCellStyle(styleCon);
                /*cell = row.createCell(10);cell.setCellValue(keeSpecimen.getProductDesc());
                cell.setCellStyle(styleCon);*/
            }
            return wb;
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }
        return wb;

    }
}

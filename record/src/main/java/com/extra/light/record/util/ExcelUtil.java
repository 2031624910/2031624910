package com.extra.light.record.util;

import com.extra.light.record.annotation.ExcelAnnotation;
import com.extra.light.record.model.ExcelModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author rookie
 * @version 1.0
 * @date 2019/11/18 22:35
 * @description excel工具类
 */
@Slf4j
@Component
public class ExcelUtil {
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private FieldUtil fieldUtil;

    /**
     * Excel后缀
     */
    private static final String EXCEL_XLSX = ".xlsx";
    private static final String EXCEL_XLS = ".xls";
    /**
     * 默认文件名
     */
    private static final String DEFAULT_NAME = "默认导出摸板";

    /**
     * 创建excel
     * 导出EXCEL
     *
     * @param list             要导出的数据集合
     * @param title            excel表头
     * @param contentFieldName 对应相应实体类的属性名
     * @param fileName         文件名
     * @param req              要求事情
     * @return 文件路径，可直接下载
     */
    public <T> String createExcel(List<T> list, String[] title, String[] contentFieldName, String fileName, HttpServletRequest req) {
        //设置默认文件名
        if (StringUtil.isEmpty(fileName)) {
            fileName = DEFAULT_NAME;
        }
        //创建一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //添加sheet
        HSSFSheet sheet = workbook.createSheet(fileName);
        //设置表头
        setSheetHead(workbook, sheet, title);
        //内容
        //内容样式
        HSSFCellStyle contentStyle = setContentStyle(workbook);
        try {
            if (StringUtil.isNotEmpty(list)) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    HSSFRow contentRow = sheet.createRow(i + 1);
                    //设置内容
                    Object o = list.get(i);
                    setSheetContent(contentFieldName, contentRow, contentStyle, o);
                }
            }
            //访问地址
            return excelUpload(req, fileName, workbook);
        } catch (Exception e) {
            log.error("excel导出错误--->" + e.getMessage());
            return "";
        }
    }

    public <T, Y> String createExcel(List<T> list, Class<Y> clazz, String fileName, HttpServletRequest req) {
        List<String[]> all = getAllTitleAndFieldName(clazz);
        String[] title = all.get(0);
        String[] contentFieldName = all.get(1);
        return createExcel(list, title, contentFieldName, fileName, req);
    }

    /**
     * 创建excel总
     *
     * @param list     列表
     * @param clazz    clazz
     * @param fileName 文件名称
     * @param request  请求
     * @return {@link String}
     */
    public <T, Y> String createExcelTotal(List<T> list, Class<Y> clazz, String fileName, HttpServletRequest request) {
        List<Field> fieldList = getFieldListByClazz(clazz);
        int length = getTotalLength(clazz);
        int size = fieldList.size();
        String[] title = new String[size];
        String[] contentFieldName = new String[size];
        if (length == 0) {
            return "";
        }
        Object[] totalValue = getTotalValue(fieldList, list, length);
        int[] totalValueIndex = getTotalValueIndex(fieldList, length);
        return createExcelTotal(list, title, contentFieldName, fileName, totalValue, totalValueIndex, request);
    }

    /**
     * 设置单内容
     * 在表格中填充数据
     *
     * @param contentFieldName 内容字段名
     * @param contentRow       行内容
     * @param contentStyle     内容样式
     * @param o                o
     */
    private static void setSheetContent(String[] contentFieldName, HSSFRow contentRow, HSSFCellStyle contentStyle, Object o) {
        try {
            int contentLength = contentFieldName.length;
            for (int i = 0; i < contentLength; i++) {
                Field valueField = o.getClass().getDeclaredField(contentFieldName[i]);
                valueField.setAccessible(true);
                //创建单元格
                HSSFCell contentCell = contentRow.createCell(i);
                //设置值和样式
                if (StringUtil.isNotEmpty(valueField.get(o))) {
                    contentCell.setCellValue(String.valueOf(valueField.get(o)));
                } else {
                    contentCell.setCellValue("");
                }
                contentCell.setCellStyle(contentStyle);
            }
        } catch (Exception e) {
            log.error("报表导出错误--->>" + e.getMessage());
            return;
        }
    }

    /**
     * 设置内容样式
     *
     * @param workbook 工作簿
     * @return {@link HSSFCellStyle}
     */
    private static HSSFCellStyle setContentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        return contentStyle;
    }

    /**
     * 设置表头样式
     * 表头样式
     *
     * @param workbook 工作簿
     * @return {@link HSSFCellStyle}
     */
    private static HSSFCellStyle setSheetHeadStyle(HSSFWorkbook workbook) {
        //设置表头居中
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置字体
        HSSFFont headFont = workbook.createFont();
        headFont.setBold(true);
        headStyle.setFont(headFont);
        return headStyle;
    }

    /**
     * 设置单头
     * 设置表头
     *
     * @param workbook 工作簿
     * @param sheet    表
     * @param title    标题
     */
    private void setSheetHead(HSSFWorkbook workbook, HSSFSheet sheet, String[] title) {
        //添加表头
        HSSFRow headRow = sheet.createRow(0);
        //表头样式
        HSSFCellStyle headStyle = setSheetHeadStyle(workbook);
        //设置表头值
        for (int i = 0; i < title.length; i++) {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(title[i]);
            headCell.setCellStyle(headStyle);
        }
    }

    /**
     * 创建excel总
     * 创建一个excel  最后一行为合计值
     *
     * @param list             内需导出的数据集合
     * @param title            标题/表头
     * @param contentFieldName 需要导出的数据对应的属性名
     * @param fileName         导出的文件名
     * @param totalValue       最后一行/合计的值对应的数组
     * @param totalValueIndex  合计值对应的列索引
     * @param request          请求
     * @return {@link String}
     */
    public <T> String createExcelTotal(List<T> list, String[] title, String[] contentFieldName, String fileName,
                                       Object[] totalValue, int[] totalValueIndex, HttpServletRequest request) {
        //创建一个EXCEL
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet
        setCellValue(workbook, fileName, title, contentFieldName, totalValueIndex, totalValue, true, list);
        //访问地址
        return excelUpload(request, fileName, workbook);
    }

    /**
     * 2003版本 HSSFWorkbook
     * 生成.xls后缀的excel
     * 适用于数据量小于65535条
     * 一张sheet表允许存2^16 ＝ 次方行数据，2^8 = 256列数据，
     *
     * 2007版本以上 XSSFWork
     * 后缀.xlsx
     * 一张sheet表允许存的数据更大，
     * 是百万级别的。
     * 行： 2^20 = 1048576;
     * 列：2^14 = 16384。
     */

    /**
     * excel上传
     * 上传excel到服务器
     *
     * @param fileName 文件名
     * @param request  请求
     * @param workbook 工作簿
     * @return 返回文件访问路径
     */
    private String excelUpload(HttpServletRequest request, String fileName, HSSFWorkbook workbook) {
        FileOutputStream fileOutputStream = null;
        String filePath = "";
        try {
            File dir = fileUtil.fileUploadPath(request);
            File file = new File(dir, fileName + EXCEL_XLS);
            log.info("上传文件--->>" + file);
            fileOutputStream = new FileOutputStream(file);
            //上传
            workbook.write(fileOutputStream);
            //下载
            filePath = fileUtil.fileDownloadPath(fileName, EXCEL_XLS);
        } catch (IOException e) {
            log.error("IO异常" + e.getMessage());
        } finally {
            //释放资源
            try {
                if (StringUtil.isNotEmpty(workbook)) {
                    workbook.close();
                }
                if (StringUtil.isNotEmpty(fileOutputStream)) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error("释放资源错误--->" + e.getMessage());
                return "";
            }
        }
        return filePath;
    }

    public <T> List<String[]> getAllTitleAndFieldName(Class<T> clazz) {
        List<Field> fieldList = getFieldListByClazz(clazz);
        int size = fieldList.size();
        String[] title = new String[size];
        String[] contentFieldName = new String[size];
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String value = annotation.value();
            String name = field.getName();
            title[i] = value;
            contentFieldName[i] = name;
            log.info(name + ":" + value);
        }
        List<String[]> list = new ArrayList<>();
        list.add(title);
        list.add(contentFieldName);
        return list;
    }


    public <T> List<String[]> getAllTitleAndFieldName(Class<T> clazz, String[] hide) {
        List<Field> fieldList = getFieldListByClazz(clazz, hide);
        int size = fieldList.size();
        String[] title = new String[size];
        String[] contentFieldName = new String[size];
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            String value = annotation.value();
            String name = field.getName();
            title[i] = value;
            contentFieldName[i] = name;
            log.info(name + ":" + value);
        }
        List<String[]> list = new ArrayList<>();
        list.add(title);
        list.add(contentFieldName);
        return list;
    }

    /**
     * 创建excel表测试
     * 将文件直接写道D盘，便于测试
     *
     * @param excelModel excel模型
     */
    public void createExcelListTest(ExcelModel excelModel) {
        try {
            String filedName = excelModel.getFiledName();
            //创建一个EXCEL
            File downFile = new File("D://" + filedName + EXCEL_XLS);
            FileOutputStream fileOutputStream = new FileOutputStream(downFile);
            HSSFWorkbook workbook = createExcelListNotUpload(excelModel);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            log.error("测试程序错误》》" + e.getMessage());
            e.printStackTrace();
        }
    }


    public String createExcelList(ExcelModel excelModel, HttpServletRequest request) {
        String filedName = excelModel.getFiledName();
        //创建一个EXCEL
        HSSFWorkbook workbook = createExcelListNotUpload(excelModel);
        return excelUpload(request, filedName, workbook);
    }

    public HSSFWorkbook createExcelListNotUpload(ExcelModel excelModel) {
        //创建一个EXCEL
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ExcelModel.ExcelNote> noteList = excelModel.getNoteList();
        if (StringUtil.isNotEmpty(noteList)) {
            log.warn("===开始进行导出作业===");
            for (ExcelModel.ExcelNote note : noteList) {
                String sheetName = note.getSheetName();
                List list = note.getList();
                Class clazz = note.getClazz();
                String[] hide = note.getHide();
                List<String[]> all = getAllTitleAndFieldName(clazz, hide);
                //确认是否需要开启汇总
                boolean total = note.isTotal();
                int[] totalValueIndex = new int[0];
                Object[] totalValue = new String[0];
                if (total) {
                    int totalLength = getTotalLength(clazz);
                    List<Field> fieldList = getFieldListByClazz(clazz, hide);
                    totalValueIndex = getTotalValueIndex(fieldList, totalLength);
                    totalValue = getTotalValue(fieldList, list, totalLength);
                }
                String[] title = all.get(0);
                String[] contentFieldName = all.get(1);
                //创建sheet
                setCellValue(workbook, sheetName, title, contentFieldName, totalValueIndex, totalValue, total, list);
            }
        }
        //访问地址
        return workbook;
    }

    private <T> void setCellValue(HSSFWorkbook workbook,
                                  String sheetName, String[] title, String[] contentFieldName,
                                  int[] totalValueIndex, Object[] totalValue, boolean isTotal, List<T> list) {
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //创建表头
        setSheetHead(workbook, sheet, title);
        //表头样式
        HSSFCellStyle headStyle = setSheetHeadStyle(workbook);
        //数据
        if (StringUtil.isNotEmpty(list)) {
            //内容
            //内容样式
            HSSFCellStyle contentStyle = setContentStyle(workbook);
            try {
                for (int i = 0; i < list.size(); i++) {
                    HSSFRow contentRow = sheet.createRow(i + 1);
                    //设置内容
                    Object o = list.get(i);
                    setSheetContent(contentFieldName, contentRow, contentStyle, o);
                }
                if (isTotal) {
                    if (StringUtil.isNotEmpty(totalValue)) {
                        //最后一行（合计）
                        HSSFRow totalRow = sheet.createRow(list.size() + 1);
                        HSSFCell firstTotalCell = totalRow.createCell(0);
                        firstTotalCell.setCellStyle(headStyle);
                        firstTotalCell.setCellValue("合计");
                        for (int i = 0; i < totalValue.length; i++) {
                            HSSFCell totalCell = totalRow.createCell(totalValueIndex[i]);
                            totalCell.setCellStyle(headStyle);
                            totalCell.setCellValue(String.valueOf(totalValue[i]));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("excel导出错误--->" + e.getMessage());
                return;
            }
        }
    }

    private <T> int[] getTotalValueIndex(Class<T> clazz) {
        List<Field> fieldList = getFieldListByClazz(clazz);
        int length = getTotalLength(clazz);
        return getTotalValueIndex(fieldList, length);
    }

    private <T> List<Field> getFieldListByClazz(Class<T> clazz) {
        if (StringUtil.isEmpty(fieldUtil)) {
            fieldUtil = new FieldUtil();
        }
        return fieldUtil.getFieldListByClazz(clazz, ApiModelProperty.class);
    }

    private <T> List<Field> getFieldListByClazz(Class<T> clazz, String[] hide) {
        if (StringUtil.isEmpty(fieldUtil)) {
            fieldUtil = new FieldUtil();
        }
        List<Field> fieldListByClazz = fieldUtil.getFieldListByClazz(clazz, ApiModelProperty.class);
        if (StringUtil.isNotEmpty(hide)) {
            List<Field> removeList = new ArrayList<>();
            if (StringUtil.isNotEmpty(fieldListByClazz)) {
                for (Field field : fieldListByClazz) {
                    String name = field.getName();
                    for (String s : hide) {
                        if (s.equals(name)) {
                            removeList.add(field);
                        }
                    }
                }
                if (StringUtil.isNotEmpty(removeList)) {
                    fieldListByClazz.removeAll(removeList);
                }
            }
        }
        return fieldListByClazz;
    }

    private <T> int getTotalLength(Class<T> clazz) {
        int length = 0;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ApiModelProperty.class)) {
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                    if (annotation.isTotal()) {
                        length++;
                    }
                }
            }
        }
        return length;
    }

    private <T> int[] getTotalValueIndex(List<Field> fieldList, int length) {
        int[] totalValueIndex = new int[length];
        if (StringUtil.isNotEmpty(fieldList)) {
            int i = 0;
            for (int i1 = 0; i1 < fieldList.size(); i1++) {
                Field field = fieldList.get(i1);
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                    if (annotation.isTotal()) {
                        totalValueIndex[i] = i1;
                        i++;
                    }
                }
            }
        }
        return totalValueIndex;
    }

    private <T> Object[] getTotalValue(Class<T> clazz, List<T> list) {
        List<Field> fieldList = getFieldListByClazz(clazz);
        int length = getTotalLength(clazz);
        return getTotalValue(fieldList, list, length);
    }

    private <T> Object[] getTotalValue(List<Field> fieldList, List<T> list, int length) {
        try {
            Object[] totalValue = new Object[length];
            int a = 0;
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                    ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                    //进行合计
                    boolean total = annotation.isTotal();
                    if (total) {
                        Class<?> type = field.getType();
                        if (type.equals(String.class) ||
                                type.equals(BigDecimal.class) ||
                                type.equals(Double.class) ||
                                type.equals(Double.TYPE) ||
                                type.equals(Float.class) ||
                                type.equals(Float.TYPE)
                        ) {
                            int totalSize = annotation.totalSize();
                            BigDecimal sum = new BigDecimal("0.00").setScale(totalSize);
                            if (StringUtil.isNotEmpty(list)) {
                                field.setAccessible(true);
                                for (T t : list) {
                                    Object o = field.get(t);
                                    if (o instanceof String) {
                                        BigDecimal bigDecimal = new BigDecimal((String) o);
                                        sum = sum.add(bigDecimal);
                                    } else if (o instanceof BigDecimal) {
                                        sum = sum.add((BigDecimal) o);
                                    } else if (o instanceof Double) {
                                        sum = sum.add(BigDecimal.valueOf((Double) o));
                                    } else if (o instanceof Float) {
                                        sum = sum.add(BigDecimal.valueOf((Float) o));
                                    }
                                }
                            }
                            totalValue[a] = sum.setScale(totalSize).toString();
                        } else if (type.equals(Integer.class) ||
                                type.equals(Long.class) ||
                                type.equals(Byte.class) ||
                                type.equals(Short.class) ||
                                type.equals(Integer.TYPE) ||
                                type.equals(Long.TYPE) ||
                                type.equals(Byte.TYPE) ||
                                type.equals(Short.TYPE)
                        ) {
                            long sum = 0L;
                            if (StringUtil.isNotEmpty(list)) {
                                field.setAccessible(true);
                                for (T t : list) {
                                    Object o = field.get(t);
                                    if (o instanceof Integer) {
                                        sum = sum + (Integer) o;
                                    } else if (o instanceof Long) {
                                        sum = sum + (Long) o;
                                    } else if (o instanceof Short) {
                                        sum = sum + (Short) o;
                                    } else if (o instanceof Byte) {
                                        sum = sum + (Byte) o;
                                    }
                                }
                            }
                            totalValue[a] = sum;
                        }
                        a++;
                    }
                }
            }
            return totalValue;
        } catch (Exception e) {
            return null;
        }
    }
}

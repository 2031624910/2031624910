package com.extra.light.record.model;

import com.extra.light.record.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 林树毅
 * 说明：fileNmae 是调用excelUtil.createExcelList()的方法最后的文件名字
 * List<ExcelNote> 内部内容是单个表格文件内，左下角中的一个。
 */
@Data
public class ExcelModel {
    /**
     * 文件名字
     */
    private String filedName;
    /**
     * 这里不能加泛型，会导致多种类型表，无法加入第二个的问题
     */
    private List<ExcelNote> noteList;

    public ExcelModel() {
    }

    /**
     * 直接创建
     *
     * @param filedName 文件名
     * @param excelNote 模块
     */
    public ExcelModel(String filedName, ExcelNote excelNote) {
        this.filedName = filedName;
        List<ExcelNote> list = new ArrayList<>();
        list.add(excelNote);
        this.noteList = list;
    }

    public <T> ExcelModel(String filedName, List<T> list, Class<T> clazz, String sheetName, boolean isTotal, String... hide) {
        this.filedName = filedName;
        addList(list, clazz, sheetName, isTotal, hide);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExcelNote<T> {
        @ApiModelProperty("数据列表")
        private List<T> list;
        @ApiModelProperty("文件的类型")
        private Class<T> clazz;
        @ApiModelProperty("文件shhet名字，不可重复")
        private String sheetName;
        @ApiModelProperty("该页是否进行汇总操作，启用后会在最后一行添加汇总数据，需要配合ExcelAnnotation.isTotal进行操作")
        private boolean isTotal;
        @ApiModelProperty("隐藏字段")
        private String[] hide;

        public ExcelNote(List<T> list, Class<T> clazz, String sheetName, boolean isTotal) {
            this.list = list;
            this.clazz = clazz;
            this.sheetName = sheetName;
            this.isTotal = isTotal;
        }
    }

    /**
     * 添加列表
     *
     * @param list      数据列表
     * @param clazz     文件的类型
     * @param sheetName 文件shhet名字，不可重复
     * @param isTotal   该页是否进行汇总操作，启用后会在最后一行添加汇总数据，需要配合ExcelAnnotation.isTotal进行操
     * @param <T>       文件类型
     */
    public <T> void addList(List<T> list, Class<T> clazz, String sheetName, boolean isTotal) {
        List<ExcelNote> noteList = this.noteList;
        if (StringUtil.isEmpty(noteList)) {
            noteList = new ArrayList<>();
        } else {
            //校验是否sheetName已有
            List<String> collect = noteList.stream().map(ExcelNote::getSheetName).collect(Collectors.toList());
            if (collect.contains(sheetName)) {
                //已经存在则名字后面添加文件结构大小
                int size = noteList.size();
                sheetName = sheetName + size;
            }
        }
        //if (list.size())
        List<ExcelNote> note = getList(list, clazz, sheetName, isTotal);
        if (StringUtil.isNotEmpty(note)){
            noteList.addAll(note);
        }
        this.noteList = noteList;
    }

    public  List<ExcelNote> getList(List list, Class clazz, String sheetName, boolean isTotal) {
        ArrayList<ExcelNote> returnList = new ArrayList<>();
        if (StringUtil.isNotEmpty(list)){
            int size = list.size();
            if (size>50000){
                List<List> list1 = batchList(list, 50000);
                int i = 0;
                for (List l1 : list1) {
                    i++;
                    returnList.add(new ExcelNote(l1, clazz, sheetName+"第"+i+"页", isTotal));
                }
                return returnList;
            } else {
                returnList.add(new ExcelNote(list, clazz, sheetName, isTotal));
                return returnList;
            }
        }
        return returnList;
    }

    private <T> List<List<T>>  batchList(List<T> list, int everySize) {
        List<List<T>> batchList = new ArrayList<>();
        //原集合的数量
        int size = list.size();
        if (size > everySize) {
            //记录循环次数
            int count = 0;
            //循环插入
            for (int i = 0; i < size; i += everySize) {
                //如果剩余的不足everySize，则截取剩余的list
                int listSize = (size - count * everySize);
                //需要截取的list下标
                int index;
                if (listSize >= everySize) {
                    index = i + everySize;
                } else {
                    index = size;
                }
                List<T> everyList = new ArrayList<>(list.subList(i, index));
                batchList.add(everyList);
                count++;
            }
        } else {
            List<T> everyList = new ArrayList<>(list);
            batchList.add(everyList);
        }
        batchList = batchList.stream().filter(StringUtil::isNotEmpty).collect(Collectors.toList());
        return batchList;
    }


    public <T> void addList(List<T> list, Class<T> clazz, String sheetName, boolean isTotal, String... hide) {
        List<ExcelNote> noteList = this.noteList;
        if (StringUtil.isEmpty(noteList)) {
            noteList = new ArrayList<>();
        } else {
            //校验是否sheetName已有
            List<String> collect = noteList.stream().map(ExcelNote::getSheetName).collect(Collectors.toList());
            if (collect.contains(sheetName)) {
                //已经存在则名字后面添加文件结构大小
                int size = noteList.size();
                sheetName = sheetName + size;
            }
        }
        ExcelNote<T> note = new ExcelNote<T>(list, clazz, sheetName, isTotal, hide);
        noteList.add(note);
        this.noteList = noteList;
    }

    /**
     * 添加结构
     *
     * @param note
     * @param <T>
     */
    public <T> void addList(ExcelNote<T> note) {
        if (StringUtil.isNotEmpty(note)) {
            List<ExcelNote> noteList = this.noteList;
            if (StringUtil.isEmpty(noteList)) {
                noteList = new ArrayList<>();
            } else {
                //校验是否sheetName已有
                List<String> collect = noteList.stream().map(ExcelNote::getSheetName).collect(Collectors.toList());
                String sheetName = note.getSheetName();
                if (collect.contains(sheetName)) {
                    //已经存在则名字后面添加文件结构大小
                    int size = noteList.size();
                    note.setSheetName(sheetName + size);
                }
            }
            noteList.add(note);
            this.noteList = noteList;
        }
    }
}

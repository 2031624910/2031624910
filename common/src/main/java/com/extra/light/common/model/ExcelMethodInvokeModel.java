package com.extra.light.common.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 林树毅
 */
@Data
public class ExcelMethodInvokeModel {
    /**
     * 方法名
     */
    private String fileName;
    private String excelTarget;
    private List<Note> noteList;

    public void addNote(String methodName, String sheetName, int page, int size, Class<?>[] args, Class<?> resultType, Class<?> classType) {
        List<Note> noteList = this.noteList;
        //如果为空，则新增
        if (noteList == null) {
            noteList = new ArrayList<>();
        }
        Note note = new Note();
        note.setMethodName(methodName);
        note.setSheetName(sheetName);
        note.setPage(page);
        note.setSize(size);
        note.setArgs(args);
        note.setResultType(resultType);
        note.setClassType(classType);
        noteList.add(note);
        this.noteList = noteList;
    }

    @Data
    public static class Note {
        private String methodName;
        private String sheetName;
        private int page;
        private int size;
        private Class<?>[] args;
        private Class<?> resultType;
        private Class<?> classType;
    }
}

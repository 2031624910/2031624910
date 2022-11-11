package com.extra.light.record.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.extra.light.record.config.ResultConfig;
import com.extra.light.record.exception.BusinessException;
import com.extra.light.record.model.ExcelMethodInvokeModel;
import com.extra.light.record.model.bo.ExportBo;
import com.extra.light.record.util.FileUtil;
import com.extra.light.record.util.SpringUtil;
import com.extra.light.record.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.io.*;

/**
 * @author 林树毅
 */
@AllArgsConstructor
@RestController
@RequestMapping("excel")
@Slf4j
public class ExcelController {
    private final FileUtil fileUtil;
    private final Map<String, ExcelMethodInvokeModel> excelMethodInvokeMap;

    @ApiOperation("export")
    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestBody ExportBo bo, HttpServletRequest request) {
        String filePath = null;
        ExcelMethodInvokeModel invokeModel = excelMethodInvokeMap.get(bo.getExcelTarget());
        if (StringUtil.isEmpty(invokeModel)) {
            return ResultConfig.failure("获取不到对应方法");
        }
        String suffix = getSuffix(bo.getSuffix());
        boolean test = bo.isTest();
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            String fileName = invokeModel.getFileName();
            if (test) {
                fileName = "D:/" + fileName + suffix;
                file = new File(fileName);
            } else {
                File dir = fileUtil.fileUploadPath(request);
                file = new File(dir, fileName + suffix);
                log.info("上传文件--->>" + file);
                fileOutputStream = new FileOutputStream(file);
            }
            //创建一个新表
            List<ExcelMethodInvokeModel.Note> noteList = invokeModel.getNoteList();
            int i = 0;
            ExcelWriter excelWriter = null;
            try {
                //确认
                if (test) {
                    excelWriter = EasyExcel.write(file).build();
                } else {
                    excelWriter = EasyExcel.write(fileOutputStream).build();
                }
                for (ExcelMethodInvokeModel.Note note : noteList) {
                    try {
                        noteWriter(note, excelWriter, i, bo.getObjects(), request);
                        i++;
                    } catch (BusinessException e) {
                        log.error("异常" + e.getMsg());
                        return ResultConfig.failure("异常: " + e.getMsg());
                    }
                }
                //获取地址
                filePath = fileUtil.fileDownloadPath(fileName, suffix);
            } catch (Exception e) {
                return ResultConfig.failure("创建异常" + e.getMessage());
            } finally {
                excelWriter.close();
            }
        } catch (IOException e) {
            log.error("IO异常");
            return ResultConfig.failure("IO异常", e.getMessage());
        } finally {
            fileOutPutClose(fileOutputStream, test);
        }
        return ResultConfig.success("导出成功", filePath);
    }

    /**
     * 关闭输出流
     *
     * @param fileOutputStream
     * @param test
     */
    private void fileOutPutClose(FileOutputStream fileOutputStream, boolean test) {
        if (!test) {
            try {
                if (StringUtil.isNotEmpty(fileOutputStream)) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error("关闭输出流失败");
            }
        }
    }

    /**
     * 文件处理
     *
     * @param note
     * @param excelWriter
     * @param i
     * @param objects
     * @param request
     */
    private void noteWriter(ExcelMethodInvokeModel.Note note, ExcelWriter excelWriter, int i, List<Object> objects, HttpServletRequest request) {
        Class<?> classType = note.getClassType();
        Object bean = null;
        try {
            bean = SpringUtil.getBean(classType);
        } catch (Exception e) {
            throw new BusinessException("获取bean失败");
        }
        //调用对应的方法
        try {
            String methodName = note.getMethodName();
            Class<?>[] args = note.getArgs();
            Method method = classType.getMethod(methodName, args);
            Class<?> returnType = method.getReturnType();
            //判断类型
            if (!List.class.equals(returnType)) {
                throw new BusinessException("返回类型非列表");
            }
            //判断参数个数
            boolean verify = argsVerify(args, objects, note.getPage(), note.getSize());
            if (verify) {
                throw new BusinessException("参数校验失败");
            }
            //填充调用的参数数值
            Object[] oList = argsFill(args, objects, note.getPage(), note.getSize(), request);
            //调用方法
            Object invoke = method.invoke(bean, oList);
            if (invoke instanceof List) {
                //使用工具导出列表
                System.out.println(invoke);
                WriteSheet writeSheet = EasyExcel.writerSheet(i, note.getSheetName() + i).head(note.getResultType()).build();
                excelWriter.write((List) invoke, writeSheet);
            } else {
                throw new BusinessException("返回类型非列表");
            }
        } catch (Exception e) {
            throw new BusinessException("反射异常" + e.getMessage());
        }
    }

    private String getSuffix(String suffix) {
        return ExcelTypeEnum.XLS.getValue();
    }

    private Object[] argsFill(Class<?>[] args, List<Object> objects, int page, int size, HttpServletRequest request) {
        int length = args.length;
        Object[] list = new Object[length];
        int j = 0;
        for (int i = 0; i < length; i++) {
            if (i == page - 1) {
                list[i] = 0;
                continue;
            }
            if (i == size - 1) {
                list[i] = Integer.MAX_VALUE;
                continue;
            }
            Class<?> arg = args[i];
            if (arg.equals(HttpServletRequest.class)) {
                list[i] = request;
                continue;
            }
            list[i] = objects.get(j);
            j++;
        }
        return list;
    }

    private boolean argsVerify(Class<?>[] args, List<Object> objects, int page, int size) {
        //校验参数类型个数
        int length = args.length;
        int oSize = objects.size();
        int del = 0;
        //校验参数类型
        int j = 0;
        for (int i = 0; i < length; i++) {
            if (i == page - 1) {
                del++;
                continue;
            }
            if (i == size - 1) {
                del++;
                continue;
            }
            Class<?> arg = args[i];
            if (arg.equals(HttpServletRequest.class)) {
                del++;
                continue;
            }
            Object o = objects.get(j);
            if (!o.getClass().equals(arg)) {
                return true;
            }
            j++;
        }
        if (length - del != oSize) {
            return true;
        }
        return false;
    }
}

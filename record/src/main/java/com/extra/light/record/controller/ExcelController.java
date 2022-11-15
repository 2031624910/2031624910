package com.extra.light.record.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.extra.light.record.config.ResultConfig;
import com.extra.light.record.exception.BusinessException;
import com.extra.light.record.model.ExcelMethodInvokeModel;
import com.extra.light.record.model.bo.ExportBo;
import com.extra.light.record.util.ClassUtil;
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
    @PostMapping("/export/{excelTarget}")
    public ResponseEntity<?> export(@RequestBody ExportBo bo,
                                    @PathVariable(value = "excelTarget") String excelTarget,
                                    HttpServletRequest request) {
        String filePath = null;
        ExcelMethodInvokeModel invokeModel = excelMethodInvokeMap.get(excelTarget);
        if (StringUtil.isEmpty(invokeModel)) {
            return ResultConfig.failure("获取不到对应方法");
        }
        if (StringUtil.isEmpty(bo) || StringUtil.isEmpty(bo.getObjects())) {
            return ResultConfig.failure("未找到参数");
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
                //用于判断是否是已有名字
                List<String> sheetNameList = new ArrayList<>();
                for (ExcelMethodInvokeModel.Note note : noteList) {
                    try {
                        noteWriter(note, excelWriter, i, bo.getObjects().get(i), sheetNameList, request);
                        i++;
                    } catch (BusinessException e) {
                        log.error("异常" + e.getMessage());
                        return ResultConfig.failure("异常: " + e.getMessage());
                    }
                }
                //获取地址
                filePath = fileUtil.fileDownloadPath(fileName, suffix);
            } catch (Exception e) {
                return ResultConfig.failure("创建异常" + e.getMessage());
            } finally {
                if (StringUtil.isNotEmpty(excelWriter)) {
                    excelWriter.close();
                }
            }
        } catch (IOException e) {
            log.error("IO异常");
            return ResultConfig.failure("IO异常", e.getMessage());
        } finally {
            fileOutPutClose(fileOutputStream, test);
        }
        if (test) {
            return ResultConfig.success("导出成功", null);
        } else {
            return ResultConfig.success("导出成功", filePath);
        }
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
     * 单个方法调用，并写入文件
     *
     * @param note
     * @param excelWriter
     * @param i
     * @param objects
     * @param sheetNameList
     * @param request
     */
    private void noteWriter(ExcelMethodInvokeModel.Note note,
                            ExcelWriter excelWriter,
                            int i, List<Object> objects,
                            List<String> sheetNameList,
                            HttpServletRequest request) {
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
            String sheetName = getSheetName(note.getSheetName(), sheetNameList, i);
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
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName).head(note.getResultType()).build();
                excelWriter.write((List) invoke, writeSheet);
            } else {
                throw new BusinessException("返回类型非列表");
            }
        } catch (Exception e) {
            log.error("反射异常" + e.getMessage());
        }
    }

    /**
     * 获取sheetName，并保证不重复
     *
     * @param sheetName
     * @param sheetNameList
     * @param i
     * @return
     */
    private String getSheetName(String sheetName, List<String> sheetNameList, int i) {
        if (sheetNameList.contains(sheetName)) {
            sheetName = sheetName + i;
        }
        sheetNameList.add(sheetName);
        return sheetName;
    }

    /**
     * 校验后缀名
     *
     * @param suffix
     * @return
     */
    private String getSuffix(String suffix) {
        ExcelTypeEnum[] values = ExcelTypeEnum.values();
        for (ExcelTypeEnum value : values) {
            if (value.getValue().equalsIgnoreCase(suffix)) {
                return value.getValue();
            }
        }
        throw new BusinessException("后缀校验异常", 502);
    }

    /**
     * 参数填充
     *
     * @param args    参数列表
     * @param objects 参数值
     * @param page    页码忽略位
     * @param size    大小忽略位
     * @param request 请求值
     * @return 可以用于请求的参数列表
     */
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
            Object o = objects.get(j);
            if (o instanceof Map && ClassUtil.isNotList(arg) && ClassUtil.isNotBasicType(arg)) {
                Object bean = ClassUtil.mapToClass(arg, ClassUtil.getStringMap((Map) o));
                list[i] = bean;
            } else {
                list[i] = o;
            }
            j++;
        }
        return list;
    }

    /**
     * 参数校验
     *
     * @param args    参数列表
     * @param objects 参数值列表
     * @param page    页码忽略位
     * @param size    大小忽略位
     * @return 是否不通过校验
     */
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
            //如果arg不是基础和List类型，o是map类型，则返回continue
            if (ClassUtil.isNotBasicType(arg) && ClassUtil.isNotList(arg) && o instanceof Map) {
                continue;
            }
            //去掉是自定义对象的，其他必须是他的子类的或者需要忽略的，其他需要是子类或者本类，才可以成功
            if (!o.getClass().equals(arg) || !arg.isAssignableFrom(o.getClass())) {
                return true;
            }
            j++;
        }
        return length - del != oSize;
    }
}

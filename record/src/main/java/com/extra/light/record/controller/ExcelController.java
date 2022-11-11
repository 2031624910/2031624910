package com.extra.light.record.controller;

import com.extra.light.record.config.ResultConfig;
import com.extra.light.record.model.ExcelMethodInvokeModel;
import com.extra.light.record.model.bo.ExportBo;
import com.extra.light.record.util.SpringUtil;
import com.extra.light.record.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 林树毅
 */
@AllArgsConstructor
@RestController
@RequestMapping("excel")
public class ExcelController {
    private final Map<String, ExcelMethodInvokeModel> excelMethodInvokeMap;

    @ApiOperation("export")
    @PostMapping("export")
    public ResponseEntity<?> export(@RequestBody ExportBo bo, HttpServletRequest request) {
        ExcelMethodInvokeModel invokeModel = excelMethodInvokeMap.get(bo.getMethodName());
        if (StringUtil.isEmpty(invokeModel)) {
            return ResultConfig.failure("获取不到对应方法");
        }
        Class<?> classType = invokeModel.getClassType();
        Object bean = null;
        try {
            bean = SpringUtil.getBean(classType);
        } catch (Exception e) {
            return ResultConfig.failure("获取bean失败");
        }
        //调用对应的方法
        try {
            String methodName = invokeModel.getMethodName();
            Class<?>[] args = invokeModel.getArgs();
            Method method = classType.getMethod(methodName, args);
            Class<?> returnType = method.getReturnType();
            //判断类型
            if (!List.class.equals(returnType)) {
                return ResultConfig.failure("返回类型非列表");
            }
            List<Object> objects = bo.getObjects();
            //判断参数个数
            boolean verify = argsVerify(args, objects, invokeModel.getPage(), invokeModel.getSize());
            if (verify) {
                return ResultConfig.failure("参数校验失败");
            }
            //填充调用的参数数值
            Object[] oList = argsFill(args, objects, invokeModel.getPage(), invokeModel.getSize(), request);
            //调用方法
            Object invoke = method.invoke(bean, oList);
            if (invoke instanceof List) {
                //使用工具导出列表
                System.out.println(invoke);
            } else {
                return ResultConfig.failure("返回类型非列表");
            }
        } catch (Exception e) {
            return ResultConfig.failure("反射异常" + e.getMessage());
        }
        return ResultConfig.success("导出成功", null);
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

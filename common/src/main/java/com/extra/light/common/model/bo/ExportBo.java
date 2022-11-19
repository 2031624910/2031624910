package com.extra.light.common.model.bo;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.extra.light.common.util.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * @author 林树毅
 */
@Data
public class ExportBo {
    private List<List<Object>> objects;
    private boolean test;
    private String suffix;

    public String getSuffix() {
        if (StringUtil.isEmpty(this.suffix)) {
            this.suffix = ExcelTypeEnum.XLS.getValue();
        }
        return suffix;
    }
}

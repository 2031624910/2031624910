package com.extra.light.record.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 林树毅
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestExcelModel {
    @ExcelProperty("等待配置")
    @ColumnWidth(16)
    @ApiModelProperty("等待配置")
    private String wait;
    @ColumnWidth(8)
    @ExcelProperty("时间")
    @ApiModelProperty("时间")
    private int time;
    @ColumnWidth(8)
    @ExcelProperty("金额")
    @ApiModelProperty("金额")
    private BigDecimal money;
}

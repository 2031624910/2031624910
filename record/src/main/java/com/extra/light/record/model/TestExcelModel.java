package com.extra.light.record.model;

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
    @ApiModelProperty("等待配置")
    private String wait;
    @ApiModelProperty("时间")
    private int time;
    @ApiModelProperty("金额")
    private BigDecimal money;
}

package com.extra.light.record.model.bo;

import lombok.Data;
import java.util.List;

/**
 * @author 林树毅
 */
@Data
public class ExportBo {
    private String excelTarget;
    private List<Object> objects;
    private boolean test;
    private String suffix;
}

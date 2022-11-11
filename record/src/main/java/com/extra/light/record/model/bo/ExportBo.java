package com.extra.light.record.model.bo;

import lombok.Data;
import java.util.List;

/**
 * @author 林树毅
 */
@Data
public class ExportBo {
    String methodName;
    List<Object> objects;
}

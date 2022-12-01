package com.extra.light.common.enums;

/**
 * @author 林树毅
 */
public interface TypeEnum<T, Y> {
    /**
     * 获取code根据数据
     *
     * @param y 返回code
     * @return
     */
    Object getCodeByValue(Object y);

    /**
     * 返回C
     *
     * @param t
     * @return
     */
    Object getValueByCode(Object t);
}

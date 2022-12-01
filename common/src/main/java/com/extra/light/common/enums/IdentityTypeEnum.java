package com.extra.light.common.enums;

import com.extra.light.common.annotation.EnumChange;

/**
 * @author 林树毅
 */
public enum IdentityTypeEnum {
    /**
     * 学生
     */
    NORMAL("normal", "普通人"),
    ADMIN("admin", "管理员"),
    MEMBER("member", "会员");
    private String code;
    private String value;

    IdentityTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @EnumChange
    public static String getCodeByValue(String code) {
        IdentityTypeEnum[] values = values();
        for (IdentityTypeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getValue();
            }
        }
        return null;
    }

    @EnumChange(isReversal = false)
    public static String getValueByCode(String v) {
        IdentityTypeEnum[] values = values();
        for (IdentityTypeEnum value : values) {
            if (value.getValue().equals(v)) {
                return value.getCode();
            }
        }
        return null;
    }
}

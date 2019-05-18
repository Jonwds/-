package com.wds.sell.common.util;

import com.wds.sell.common.enums.CodeEnum;

public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for (T e:enumClass.getEnumConstants()) {
            if(e.getCode().equals(code))
                return e;
        }
        return null;
    }
}

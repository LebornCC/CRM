package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

public class UUIDUtil {
    public static String UUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}

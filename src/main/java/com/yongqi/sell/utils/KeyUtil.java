package com.yongqi.sell.utils;

import java.util.Random;

public class KeyUtil {
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        Integer number = random.nextInt(900000)+1000000;
        return System.currentTimeMillis()+String.valueOf(number);
    }
}

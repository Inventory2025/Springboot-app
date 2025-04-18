package com.ims.inventory.utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

    public static String generateCustomId() {
        long timestamp = System.currentTimeMillis() % 1_000_000_0000L;
        int random = (int) (Math.random() * 100);
        return String.format("%08d%02d", timestamp, random); // 10-digit
    }

}

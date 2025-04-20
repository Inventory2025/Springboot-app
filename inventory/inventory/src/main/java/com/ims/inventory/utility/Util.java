package com.ims.inventory.utility;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Slf4j
public class Util {

    public static String generateCustomId() {
        long timestamp = System.currentTimeMillis() % 1_000_000_0000L;
        int random = (int) (Math.random() * 100);
        return String.format("%08d%02d", timestamp, random); // 10-digit
    }

    public static String formatToIndianCurrency(Object amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "IN"));
        DecimalFormat formatter = new DecimalFormat("##,##,###.00", symbols);
        return formatter.format(amount);
    }

}

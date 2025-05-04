package com.takeout.model;

import java.util.HashMap;

public class StatusMap {
    private static final HashMap<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("pending", "待付款");
        statusMap.put("paid", "已支付");
        statusMap.put("completed", "已完成");
    }

    public static String translate(String englishStatus) {
        return statusMap.getOrDefault(englishStatus, "未知状态");
    }
}

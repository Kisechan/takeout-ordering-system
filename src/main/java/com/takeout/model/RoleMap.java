package com.takeout.model;

import java.util.HashMap;

public class RoleMap {
    private static final HashMap<String, String> roleMap = new HashMap<>();

    static {
        roleMap.put("customer", "顾客");
        roleMap.put("merchant", "商家");
    }

    public static String translate(String englishrole) {
        return roleMap.getOrDefault(englishrole, "未知状态");
    }
}

package com.takeout.util;

import javax.swing.*;

public class IconUtil {
    public static void setWindowIcon(JFrame frame) {
        ImageIcon icon = new ImageIcon(IconUtil.class.getResource("/logo.png"));
        frame.setIconImage(icon.getImage());
    }
}
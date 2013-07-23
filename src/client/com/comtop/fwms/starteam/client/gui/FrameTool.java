/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * JFrame工具类
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class FrameTool extends JFrame {
    
    /**
     * 设置程序大小并定位程序在屏幕正中
     * 
     * @param frame JFrame
     * @param width width 宽
     * @param height height 高
     */
    public static void setSizeAndCentralizeMe(JFrame frame, int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(width, height);
        frame.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
    }
}

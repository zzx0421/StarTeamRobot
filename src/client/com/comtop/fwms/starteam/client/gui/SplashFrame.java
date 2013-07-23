/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.gui;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 闪屏
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class SplashFrame extends JFrame {
    
    /**
     * 构造函数
     */
    public SplashFrame() {
        // 设置程序大小并定位程序在屏幕正中
        FrameTool.setSizeAndCentralizeMe(this, 455, 295);
        
        // 闪屏
        this.setUndecorated(true);
        JPanel splash = new JPanel(new BorderLayout());
        URL url = getClass().getClassLoader().getResource("images/splash.png");// 获得指定资源文件的绝对路径。
        if (url != null) {
            splash.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER);
        }
        this.setContentPane(splash);
        this.setVisible(true);
    }
}

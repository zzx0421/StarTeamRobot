/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 状态栏
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class StatusBar extends JPanel {
    
    /**
     * 构造函数
     */
    public StatusBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(10, 23));
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
        rightPanel.setOpaque(false);
        
        add(rightPanel, BorderLayout.EAST);
        setBackground(SystemColor.control);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 状态栏顶部阴影
        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);
        // 状态栏底部阴影
        y = getHeight() - 3;
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
    }
}

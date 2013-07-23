/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * 状态栏右侧的图标
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
class AngledLinesWindowsCornerIcon implements Icon {
    
    /** WHITE_LINE_COLOR */
    private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);
    
    /** WHITE_LINE_COLOR */
    private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
    
    /** WIDTH */
    private static final int WIDTH = 13;
    
    /** HEIGHT */
    private static final int HEIGHT = 13;
    
    public int getIconHeight() {
        return WIDTH;
    }
    
    public int getIconWidth() {
        return HEIGHT;
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        
        g.setColor(WHITE_LINE_COLOR);
        g.drawLine(0, 12, 12, 0);
        g.drawLine(5, 12, 12, 5);
        g.drawLine(10, 12, 12, 10);
        
        g.setColor(GRAY_LINE_COLOR);
        g.drawLine(1, 12, 12, 1);
        g.drawLine(2, 12, 12, 2);
        g.drawLine(3, 12, 12, 3);
        
        g.drawLine(6, 12, 12, 6);
        g.drawLine(7, 12, 12, 7);
        g.drawLine(8, 12, 12, 8);
        
        g.drawLine(11, 12, 12, 11);
        g.drawLine(12, 12, 12, 12);
        
    }
}

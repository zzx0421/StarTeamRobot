/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.comtop.fwms.starteam.StarTeamSearcherConstant;
import com.comtop.fwms.starteam.client.socket.Client;
import com.comtop.fwms.starteam.model.FileInfo;
import com.comtop.fwms.starteam.util.ServerConfig;

/**
 * 查询检索客户端Frame
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class SearcherFrame extends JFrame {
    
    /** 系统托盘 */
    private TrayIcon trayIcon = null;
    
    /** 状态栏 */
    private StatusBar statusBar = new StatusBar();
    
    /** 表格列名 */
    private String columnNames[] = { StarTeamSearcherConstant.SEARCH_RESULT_TITLE_NAME, StarTeamSearcherConstant.SEARCH_RESULT_TITLE_PATH };
    
    /** 表格模型对象 */
    private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    
    /** 查询结果表格 */
    private JTable table = new JTable(tableModel);
    
    /**
     * 构造函数
     */
    private SearcherFrame() {
        init();
    }
    
    /**
     * 初始化
     */
    private void init() {
        this.setTitle(StarTeamSearcherConstant.FRAME_TITLE);
        // 设置程序大小并定位程序在屏幕正中
        FrameTool.setSizeAndCentralizeMe(this, 800, 625);
        
        // 添加菜单栏
        setJMenuBar();
        
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        // 搜索框
        final JTextField searchText = new JTextField();
        searchText.addKeyListener(new KeyAdapter() {// 适配器，只要实现自己感兴趣的方法即可。
            
                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    // 回车事件
                    if (keyCode == KeyEvent.VK_ENTER) {
                        final String strText = searchText.getText();
                        if ("".equals(strText.trim())) {
                            return;
                        }
                        // 新线程任务
                        Thread thread = new Thread() {
                            
                            @Override
                            public void run() {
                                // 状态栏
                                statusBar.setBackground(SystemColor.yellow);
                                int port = Integer.parseInt(ServerConfig.getInstance().getConfigValue(StarTeamSearcherConstant.SERVER_PORT));
                                Client client = new Client(ServerConfig.getInstance().getConfigValue(StarTeamSearcherConstant.SERVER_ADDRESS), port);
                                try {
                                    List<FileInfo> lstServerFile = client.searchServerFile(strText);
                                    // 清空历史数据
                                    tableModel.setRowCount(0);
                                    // 设置查询结果
                                    for (FileInfo fileInfo : lstServerFile) {
                                        tableModel.addRow(new String[] { fileInfo.getFileName(), fileInfo.getFilePath() });
                                    }
                                    statusBar.setBackground(SystemColor.green);
                                } catch (Exception ex) {
                                    statusBar.setBackground(SystemColor.red);
                                    ex.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }
                }
            });
        // label.setFont(new Font("Courier", Font.BOLD, 36));
        // label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        contentPane.add(searchText, BorderLayout.NORTH);
        
        // 搜索结果表格
        JTableHeader strTableHeader = table.getTableHeader();
        strTableHeader.setReorderingAllowed(true);
        strTableHeader.setResizingAllowed(true);
        
        // strTableHeader.getColumnModel().setColumnSelectionAllowed(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        contentPane.add(statusBar, BorderLayout.SOUTH);
        
        if (SystemTray.isSupported()) {
            // 右键菜单
            PopupMenu popupMenu = new PopupMenu();
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ExitItemActionListener());
            popupMenu.add(exitItem);
            
            SystemTray tray = SystemTray.getSystemTray();
            // 系统托盘
            trayIcon =
                new TrayIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/search.png")).getImage(),
                    StarTeamSearcherConstant.FRAME_TITLE, popupMenu);
            trayIcon.addMouseListener(new TrayIconMouseListener());
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        
        // 退出关闭
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
    }
    
    /**
     * 设置菜单栏
     */
    private void setJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu searchMenu = new JMenu("搜索(S)");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        menuBar.add(searchMenu);
        
        JMenuItem caseItem = new JMenuItem("匹配大小写(C)", KeyEvent.VK_C);
        searchMenu.add(caseItem);
        
        JMenu helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);
        
        JMenuItem aboutItem = new JMenuItem("关于(A)", KeyEvent.VK_A);
        aboutItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String strMessage = "生产系统产品部技术管理组\n周振兴   刘壮";
                JOptionPane.showMessageDialog(SearcherFrame.this, strMessage, "开发者", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutItem);
        
        this.setJMenuBar(menuBar);
    }
    
    /**
     * 托盘监听器
     * 
     * @author 周振兴
     * @since 1.0
     * @version 2013-4-26 周振兴
     */
    private class TrayIconMouseListener extends MouseAdapter {
        
        /**
         * @param me MouseEvent
         */
        @Override
        public void mousePressed(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                SearcherFrame.this.setVisible(true);
            }
        }
    }
    
    /**
     * 系统退出
     * 
     * @author 周振兴
     * @since 1.0
     * @version 2013-4-27 周振兴
     */
    private class ExitItemActionListener implements ActionListener {
        
        public void actionPerformed(ActionEvent ae) {
            System.exit(0);
        }
    }
    
    /**
     * 启动接口
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        JFrame splashFrame = new SplashFrame();
        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        splashFrame.dispose();
        
        JFrame mainFrame = new SearcherFrame();
        mainFrame.setVisible(true);
    }
    
}

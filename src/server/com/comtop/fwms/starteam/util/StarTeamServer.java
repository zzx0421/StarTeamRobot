/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.util;

import com.starbase.starteam.Folder;
import com.starbase.starteam.Item;
import com.starbase.starteam.Project;
import com.starbase.starteam.Property;
import com.starbase.starteam.Server;
import com.starbase.starteam.Type;
import com.starbase.starteam.View;

/**
 * StarTeam服务
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-25 周振兴
 */
public class StarTeamServer {
    
    /** StarTeam服务 */
    private Server server;
    
    /**
     * 获取StarTeam服务
     * 
     * @param address 服务ip地址
     * @param port 服务端口
     */
    public void getServer(String address, int port) {
        server = new Server(address, port);
    }
    
    /**
     * 服务连接与登陆
     * 
     * @param userName 登陆用户名
     * @param password 登陆密码
     */
    public void serverConnectAndLogOn(String userName, String password) {
        server.connect();
        
        server.logOn(userName, password);
    }
    
    /**
     * 获取StarTeam服务
     * 
     * @return StarTeam服务
     */
    public Server getServer() {
        return server;
    }
    
    /**
     * 服务断开
     */
    public void disconnect() {
        server.disconnect();
    }
    
    /**
     * 查找项目
     * 
     * @param ProjectName 项目名称
     * @return StarTeam项目
     */
    public Project findProject(String ProjectName) {
        Project[] projects = server.getProjects();
        Project result = null;
        for (int i = 0; i < projects.length; i++) {
            Project next = projects[i];
            if (next.getName().equals(ProjectName)) {
                result = next;
                break;
            }
        }
        return result;
    }
    
    /**
     * 查找视图,权限限定
     * 
     * @param ProjectName 项目名称
     * @param viewName 视图名称
     * @return StarTeam项目中的视图
     */
    public View findView(String ProjectName, String viewName) {
        Project[] projects = server.getProjects();
        View result = null;
        for (int i = 0; i < projects.length; i++) {
            Project project = projects[i];
            if (project.getName().equals(ProjectName)) {
                View[] views = project.getAccessibleViews();
                for (View view : views) {
                    if (view.getName().equals(viewName)) {
                        result = view;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * 获取主要的描述
     * 
     * @param t Type
     * @return Property
     */
    public Property getPrimaryDescriptor(Type t) {
        Property[] properties = t.getProperties();
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            if (p.isPrimaryDescriptor()) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * 获取次要的描述
     * 
     * @param t Type
     * @return Property
     */
    public Property getSecondaryDescriptor(Type t) {
        Property[] properties = t.getProperties();
        for (int i = 0; i < properties.length; i++) {
            Property p = properties[i];
            if ((p.isDescriptor()) && (!p.isPrimaryDescriptor())) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * 递归文件夹
     * 
     * @param folder f
     * @return 文件总数
     */
    public int tree(Folder folder) {
        int count = 0;
        Folder[] childs = folder.getSubFolders();
        for (Folder objFolder : childs) {
            Item[] lstFolderItems = objFolder.getItems(server.typeForName("Folder").getName());
            for (Item folderItem : lstFolderItems) {
                int subCount = tree((Folder) folderItem);
                System.out.println(((Folder) folderItem).getPath() + ":" + subCount);
                count = count + subCount;
            }
            Item[] lstFileItems = objFolder.getItems(server.typeForName("File").getName());
            for (int i = 0, iSize = lstFileItems.length; i < iSize; i++) {
                count++;
                // System.out.println(((File) fileItem).getFullName());
            }
        }
        return count;
    }
}

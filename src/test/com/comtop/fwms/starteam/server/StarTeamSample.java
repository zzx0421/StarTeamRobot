/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.server;

import com.comtop.fwms.starteam.util.StarTeamServer;
import com.starbase.starteam.Folder;
import com.starbase.starteam.View;

/**
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-25 周振兴
 */
public class StarTeamSample {
    
    /**
     * Sample code showing how to connect to a StarTeam Server using the Java interfaces.
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        String strAddress = "10.10.6.19";
        int nPort = 55555;
        String strUserName = "zhouzhenxing";
        String strPassword = "";
        
        StarTeamServer starTeamServer = new StarTeamServer();
        starTeamServer.getServer(strAddress, nPort);
        starTeamServer.serverConnectAndLogOn(strUserName, strPassword);
        
        // Project objProject = starTeamServer.findProject("FWMS");
        // System.out.println(objProject);
        View objView = starTeamServer.findView("FWMS", "FWMS_BRANCH");
        // View objView = starTeamServer.findView("FWMS", "FWMS");
        objView = starTeamServer.findView("FWMS_CHRIST", "FWMS_CR4CSG");
        // System.out.println(objView);
        
        Folder f = objView.getRootFolder();
        System.out.println(f.getPath());
        System.out.println(starTeamServer.tree(f));
        
        // StarTeamFinder.findFolder(f, "");
        
        // View[] views = objProject.getViews();
        // for (View view : views) {
        // if (view.getHaveAccessRights()) {
        // System.out.println(view);
        // }
        // }
        // Use the Server object to enumerate
        // Projects and Views, etc.
        
        // Project[] projects = starTeamServer.getServer().getProjects();
        // for (Project project : projects) {
        // System.out.println("Project:" + project.getName());
        // System.out.println("*********************************");
        // View[] views = project.getAccessibleViews();
        // for (View view : views) {
        // // if (view.getHaveAccessRights()) {
        // System.out.println(view.getName());
        // // }
        // }
        // System.out.println("---------------------------------");
        // }
        
        starTeamServer.disconnect();
    }
}

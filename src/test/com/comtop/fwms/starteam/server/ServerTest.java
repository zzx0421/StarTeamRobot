/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.server;

import com.comtop.fwms.starteam.StarTeamSearcherConstant;
import com.comtop.fwms.starteam.server.socket.Server;
import com.comtop.fwms.starteam.util.ServerConfig;

/**
 * NIO服务端测试
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class ServerTest {
    
    /**
     * 启动服务端测试
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        // NIOServer objNIOServer = new NIOServer();
        // objNIOServer.initServer(8000);
        // objNIOServer.listen();
        
        Server server = new Server();
        server.setPort(Integer.parseInt(ServerConfig.getInstance().getConfigValue(StarTeamSearcherConstant.SERVER_PORT)));
        server.start();
    }
}

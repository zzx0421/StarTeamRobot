/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client;

import java.io.IOException;

import com.comtop.fwms.starteam.client.socket.Client;

/**
 * NIO客户端测试
 */
public class ClientTest {
    
    /**
     * 启动客户端测试
     * 
     * @param args 参数
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        // NIOClient objNIOClient = new NIOClient();
        // objNIOClient.initClient("localhost", 8000);
        // objNIOClient.listen();
        
        String hostname = "10.10.21.101";
        int port = 10000;
        
        String keyword = "大集中";
        
        Client client = new Client(hostname, port);
        client.searchServerFile(keyword);
    }
}

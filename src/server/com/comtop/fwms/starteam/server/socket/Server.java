/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.server.socket;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.comtop.fwms.starteam.model.FileInfo;
import com.comtop.fwms.starteam.util.ByteUtil;
import com.comtop.fwms.starteam.util.SocketChannelUtil;

/**
 * Socket服务器
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class Server extends Thread {
    
    /** 端口 */
    private int port;
    
    /**
     * @param port 设置 port 属性值为参数值 port
     */
    public void setPort(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
        try {
            ServerSocketChannel sc = ServerSocketChannel.open();
            
            ServerSocket s = sc.socket();
            s.bind(new InetSocketAddress(port));
            while (true) {
                Socket incoming = s.accept();
                
                SocketChannel objChannel = incoming.getChannel();
                Object keyword = SocketChannelUtil.read(objChannel);
                
                ExecutorService executor = Executors.newSingleThreadExecutor();
                FutureTask<List<FileInfo>> future = new FutureTask<List<FileInfo>>(new SearchFileInfoThread(keyword.toString()));
                
                executor.submit(future);
                
                try {
                    List<FileInfo> lstFileInfo = future.get();
                    System.out.println(String
                        .format("%1$s 检索关键字:%2$s 检索到的符合结果数:%3$s", incoming.getRemoteSocketAddress(), keyword, lstFileInfo.size()));
                    objChannel.write(ByteUtil.getByteBuffer(lstFileInfo));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                executor.shutdownNow();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

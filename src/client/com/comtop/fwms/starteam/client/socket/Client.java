/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.client.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

import com.comtop.fwms.starteam.model.FileInfo;
import com.comtop.fwms.starteam.util.ByteUtil;
import com.comtop.fwms.starteam.util.SocketChannelUtil;

/**
 * Socket客户端
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class Client {
    
    /** 服务器IP地址 */
    private String hostName;
    
    /** 端口 */
    private int port;
    
    /**
     * 构造函数
     * 
     * @param hostName 地址
     * @param port 端口
     */
    public Client(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }
    
    /**
     * 查询StarTeam服务器上的文件
     * 
     * @param obj Object
     * @return List<FileInfo>
     * @throws IOException IOException
     */
    public List<FileInfo> searchServerFile(Object obj) throws IOException {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open(new InetSocketAddress(hostName, port));
            channel.write(ByteUtil.getByteBuffer(obj));
            
            Object object = SocketChannelUtil.read(channel);
            if (object instanceof List<?>) {
                List<FileInfo> lstFileInfo = (List<FileInfo>) object;
                return lstFileInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
        }
        return null;
    }
    
}

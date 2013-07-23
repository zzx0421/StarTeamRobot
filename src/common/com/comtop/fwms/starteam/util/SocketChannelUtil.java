/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.util;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannel转换工具
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class SocketChannelUtil {
    
    /**
     * 处理读取信息
     * 
     * @param channel SocketChannel
     * @return Object
     */
    public static Object read(SocketChannel channel) {
        try {
            // 创建读取的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(102400);
            channel.read(buffer);
            buffer.flip();
            
            byte[] data = buffer.array();
            return ByteUtil.getObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Byte转换工具
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class ByteUtil {
    
    /**
     * 对象转换为Byte数组
     * 
     * @param obj 对象
     * @return byte数组
     * @throws IOException IOException
     */
    public static byte[] getBytes(Object obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }
    
    /**
     * byte数组转换为对象
     * 
     * @param bytes bytes
     * @return 对象
     * @throws IOException IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }
    
    /**
     * 对象转换为ByteBuffer
     * 
     * @param obj 对象
     * @return ByteBuffer
     * @throws IOException IOException
     */
    public static ByteBuffer getByteBuffer(Object obj) throws IOException {
        byte[] bytes = ByteUtil.getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }
}

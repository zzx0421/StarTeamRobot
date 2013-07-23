/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.model;

import java.io.Serializable;

/**
 * 文件信息
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class FileInfo implements Serializable {
    
    /** 文件名称 */
    private String fileName;
    
    /** 文件路径 */
    private String filePath;
    
    /**
     * 构造函数
     */
    public FileInfo() {
    }
    
    /**
     * 构造函数
     * 
     * @param fileName 文件名称
     * @param filePath 文件路径
     */
    public FileInfo(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
    
    /**
     * @return 获取 fileName属性值
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @param fileName 设置 fileName 属性值为参数值 fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return 获取 filePath属性值
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * @param filePath 设置 filePath 属性值为参数值 filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "fileName:" + fileName + " " + "filePath:" + filePath;
    }
}

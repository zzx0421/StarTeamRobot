/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.comtop.fwms.starteam.model.FileInfo;

/**
 * File I/O 工具类
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-25 周振兴
 */
public class FileUtil {
    
    /**
     * 递归查询本地磁盘文件
     * 
     * @param file 检索文件目录
     * @param name 搜索文件名关键字
     * @param fileInfos 查询结果集
     * @throws IOException IOException
     */
    public static void searchFile(File file, String name, List<FileInfo> fileInfos) throws IOException {
        File[] childs = file.listFiles();
        for (File objFile : childs) {
            if (objFile.isDirectory()) {// 目录时递归
                searchFile(objFile, name, fileInfos);
            } else {
                String strCanonicalPath = objFile.getCanonicalPath();
                if (strCanonicalPath.contains(name)) {
                    fileInfos.add(new FileInfo(objFile.getName(), strCanonicalPath));
                }
            }
        }
    }
}


package com.comtop.fwms.starteam.server.socket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.comtop.fwms.starteam.StarTeamSearcherConstant;
import com.comtop.fwms.starteam.model.FileInfo;
import com.comtop.fwms.starteam.util.FileUtil;
import com.comtop.fwms.starteam.util.ServerConfig;

/**
 * 检索文件信息线程处理
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class SearchFileInfoThread implements Callable<List<FileInfo>> {
    
    /** 检索关键字 */
    private String keyword;
    
    /**
     * 构造函数
     * 
     * @param keyword 检索关键字
     */
    public SearchFileInfoThread(String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * @return 获取 keyword属性值
     */
    public String getKeyword() {
        return keyword;
    }
    
    public List<FileInfo> call() {
        return searchServerFile();
    }
    
    /**
     * 检索文件
     * 
     * @return List<FileInfo>
     */
    public List<FileInfo> searchServerFile() {
        // 检索到的文件信息
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        
        try {
            FileUtil.searchFile(new File(ServerConfig.getInstance().getConfigValue(StarTeamSearcherConstant.LOCAL_DIRECTORY)), keyword, fileInfos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return fileInfos;
    }
}

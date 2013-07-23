/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.fwms.starteam.util;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * 配置文件
 * 
 * @author 周振兴
 * @since 1.0
 * @version 2013-4-26 周振兴
 */
public class ServerConfig {
    
    /** Properties */
    private Properties properties = new Properties();
    
    /** 单例实例对象 */
    private final static ServerConfig serverConfig = new ServerConfig();
    
    /**
     * 构造函数
     */
    private ServerConfig() {
        try {
            properties.loadFromXML(ClassLoader.getSystemResourceAsStream("config.xml"));
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取单例实例对象
     * 
     * @return ServerConfig
     */
    public static ServerConfig getInstance() {
        return serverConfig;
    }
    
    /**
     * 通过配置键获取值
     * 
     * @param key 键
     * @return 值
     */
    public String getConfigValue(String key) {
        return properties.getProperty(key);
    }
}

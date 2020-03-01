package com.soong.utils.download;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DownLoadUtils {
    public static String getFileName(String agent, String filename) throws UnsupportedEncodingException {
        if (agent.contains("Firefox")) {
            filename = new String(filename.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
        } else if (agent.contains("MSIE")) {
            filename = URLEncoder.encode(filename, "UTF-8");// IE浏览器
        }else if (agent.contains("Chrome")) {
            filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");// 谷歌
        }
        return filename;
    }
}


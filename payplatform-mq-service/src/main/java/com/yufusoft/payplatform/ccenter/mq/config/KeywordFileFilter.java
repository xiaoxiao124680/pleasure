package com.yufusoft.payplatform.ccenter.mq.config;

import java.io.File;
import java.io.FileFilter;

/**
 * 关键字文件过滤器
 */
public class KeywordFileFilter implements FileFilter {

    private String Keyword;

    public KeywordFileFilter(String keyword) {
        Keyword = keyword;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }

        if (pathname.getName().toLowerCase().contains(this.Keyword.toLowerCase())) {
            return true;
        }

        return false;
    }
}

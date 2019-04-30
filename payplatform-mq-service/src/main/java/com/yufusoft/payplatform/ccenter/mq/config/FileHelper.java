package com.yufusoft.payplatform.ccenter.mq.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作类
 *
 * @author yzp
 * @date 2018.12.1
 */
public class FileHelper {

    /**
     * 精确查找文件
     *
     * @param url directory+filename
     * @return file
     */
    public static File getFile(String url) {
        File file = new File(url);
        if (file.exists()) {
            return file;
        }
        return null;
    }


    /**
     * 在目录及子目录下查找文件
     *
     * @param url
     * @param keyword
     * @return
     */
    public static List<File> getFile(String url, String keyword) {
        {
            List<File> files = new ArrayList<File>();
            File folders = new File(url);
            if (folders.exists()) {
                if (folders.isFile()) {
                    return files;
                }
                File[] fileArray = folders.listFiles(new KeywordFileFilter(keyword));
                if (fileArray != null) {
                    for (File file : fileArray) {
                        if (file.isDirectory()) {
                            files.addAll(FileHelper.getFile(file.getPath(), keyword));
                        } else {
                            files.add(file);
                        }
                    }
                }
            }
            return files;

        }

    }

}

package com.baic.utils;

import android.content.Context;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by baic on 16/7/19.
 */
public class FileUtil {

    public static final int CACHE_SIZE_LIMIT = 30 * 1024 * 1024;

    /**
     * 检查存在目录下文件是否超过默认大小，默认值为30M。如果超过则会删除30%的文件，删除策略采用LRU算法</br>
     *
     * @param bitmapSize
     */
    public static void checkStorageAvailable(String path, long bitmapSize) {
        long dirSize = getDirSize(new File(path));
        if (dirSize + bitmapSize > CACHE_SIZE_LIMIT) {
            Map<Long, String> map = getFilePathAndModificationTime(new File(path));
            long deleteSize = 0;
            long size = (int) (dirSize * 0.3);
            File file = null;
            for (Long time : map.keySet()) {
                file = new File(map.get(time));
                deleteSize += file.length();
                if (deleteSize < size) {
                    file.delete();
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 获取目录下所有文件的大小</br>
     *
     * @param file 目录文件
     * @return 该目录下所有文件的大小
     */
    public static long getDirSize(File file) {
        long total = 0;
        if (file.exists()) {
            if (file.isFile()) {
                return file.length();
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    total += getDirSize(f);
                }
            }
        }
        return total;
    }

    /**
     * 获取该目录下文件的修改时间</br>
     *
     * @param file 目录文件
     * @return Map对象，存储该目录下所有文件的最后修改时间
     */
    public static Map<Long, String> getFilePathAndModificationTime(File file) {
        Map<Long, String> map = new HashMap<>();
        if (file.isFile()) {
            map.put(file.lastModified(), file.getAbsolutePath());
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                map.putAll(getFilePathAndModificationTime(f));
            }
        }
        return map;
    }

    public static String getFileSize(Context context, long bytes) {
        String value = "";
        if (bytes < 1000) {
            value = (int) bytes + "B";
        } else if (bytes < 1000000) {
            value = Math.round(bytes / 1000.0) + "K";
        } else if (bytes < 1000000000) {
            DecimalFormat df = new DecimalFormat("#0.0");
            value = df.format(bytes / 1000000.0) + "M";
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            value = df.format(bytes / 1000000000.0) + "G";
        }
        return value;
    }
}

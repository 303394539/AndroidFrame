package com.baic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by baic on 16/7/18.
 */

public class BitmapUtil {

    private static String SAVE_PATH = "";
    public static final int CACHE_SIZE_LIMIT = 30 * 1024 * 1024;

    public enum IMAGE_TYPE {
        JPG("jpg"), JPEG("jpeg"), GIF("gif"), PNG("png"), BMP("bmp"), PCX("pcx"), IFF("iff"), RAS("ras"), PBM("pbm"), PGM("pgm"), PPM("ppm"), PSD("psd"), SWF("swf");
        private String type;

        IMAGE_TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private BitmapUtil() {

    }

    public static String getType(byte[] imageBytes) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(imageBytes);
            int b1 = is.read();
            int b2 = is.read();
            if (b1 == 0x47 && b2 == 0x49) {
                return IMAGE_TYPE.GIF.getType();
            } else if (b1 == 0x89 && b2 == 0x50) {
                return IMAGE_TYPE.PNG.getType();
            } else if (b1 == 0xff && b2 == 0xd8) {
                return IMAGE_TYPE.JPEG.getType();
            } else if (b1 == 0x42 && b2 == 0x4d) {
                return IMAGE_TYPE.BMP.getType();
            } else if (b1 == 0x0a && b2 < 0x06) {
                return IMAGE_TYPE.PCX.getType();
            } else if (b1 == 0x46 && b2 == 0x4f) {
                return IMAGE_TYPE.IFF.getType();
            } else if (b1 == 0x59 && b2 == 0xa6) {
                return IMAGE_TYPE.RAS.getType();
            } else if (b1 == 0x50 && b2 >= 0x31 && b2 <= 0x36) {
                int id = b2 - '0';
                if (id < 1 || id > 6) {
                    return "";
                }
                switch ((id - 1) % 3) {
                    case 0:
                        return IMAGE_TYPE.PBM.getType();
                    case 1:
                        return IMAGE_TYPE.PGM.getType();
                    case 2:
                        return IMAGE_TYPE.PPM.getType();
                }
            } else if (b1 == 0x38 && b2 == 0x42) {
                return IMAGE_TYPE.PSD.getType();
            } else if (b1 == 0x46 && b2 == 0x57) {
                return IMAGE_TYPE.SWF.getType();
            } else {
                return "";
            }
        } catch (Exception ioe) {
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static boolean isAbsoluteUrl(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return false;
        } else {
            String lowerUrl = url.trim().toLowerCase();
            return lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://");
        }
    }

    /**
     * 根据url获取生成文件名。如果是本地路径，则直接返回；否则将该url地址MD5后作为文件名</br>
     *
     * @param url 图片的路径
     * @return 图片的文件名
     */
    public static String getFileName(String url) {
        String filePath = url;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            filePath = SAVE_PATH + StringUtil.getMd5(url);
        }
        return filePath;
    }

    /**
     * 根据url地址获取图片本地Stream</br>
     *
     * @param url 图片的地址
     * @return 本地图片的Stream，否则返回null
     */
    public static InputStream getBitmapStream(String url) {
        InputStream is = null;
        try {
            try {
                is = new FileInputStream(new File(getFileName(url)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (is == null || is.available() <= 0) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 根据Bitmap跟Url地址保存图片</br>
     *
     * @param context
     * @param fileName
     * @param mBitmap
     */
    public static String saveBitmap(Context context, String fileName, Bitmap mBitmap) {
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), mBitmap, fileName, null);
    }

    /**
     * 从SD卡上获取图片。如果不存在则返回null</br>
     *
     * @param url    图片的url地址
     * @param width  期望图片的宽
     * @param height 期望图片的高
     * @return 代表图片的Bitmap对象
     */
    public static Bitmap getBitmapFromSDCard(String url, int width, int height) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(getFileName(url)));
            if (inputStream != null && inputStream.available() > 0) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                        getScaleBitmapOptions(url, width, height));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据指定的宽高设置相关参数，避免出现OOM现象</br>
     *
     * @param url    图片得url地址
     * @param width  期望图片的宽
     * @param height 期望图片的高
     * @return BitmapFactory.Options对象
     */
    private static BitmapFactory.Options getScaleBitmapOptions(String url,
                                                               int width, int height) {
        InputStream inputStream = getBitmapStream(url);
        if (inputStream == null) {
            return null;
        }
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(inputStream, null, bmpFactoryOptions);

            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                    / height);
            int widthRatio = (int) Math
                    .ceil(bmpFactoryOptions.outWidth / width);

            /*
             * If both of the ratios are greater than 1, one of the sides of the
             * image is greater than the screen
             */
            if (heightRatio > 1 && widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    // Height ratio is larger, scale according to it
                    bmpFactoryOptions.inSampleSize = heightRatio;
                } else {
                    // Width ratio is larger, scale according to it
                    bmpFactoryOptions.inSampleSize = widthRatio;
                }
            }

            // Decode it for real
            bmpFactoryOptions.inJustDecodeBounds = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bmpFactoryOptions;
    }
}

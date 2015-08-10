package com.datapush.buyhand.util;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.text.format.DateFormat;

import com.daoshun.lib.util.BitmapUtils;
import com.datapush.buyhand.common.Settings;

public class CommonUtils {

    /**
     * 清理临时文件
     */
    public static final void deleteTempFile() {
        File tempFolder = new File(Settings.TEMP_PATH);
        if (tempFolder.exists()) {
            File[] tempFiles = tempFolder.listFiles();
            for (File tempFile : tempFiles) {
                tempFile.delete();
            }
        }
    }

    /**
     * 取得图片缓存大小
     */
    public static final long getCacheSize() {
        File cacheFolder = new File(Settings.PIC_PATH);
        // 文件夹是否存在
        if (cacheFolder.exists()) {
            return getFileSize(cacheFolder);
        } else {
            return 0;
        }
    }

    /**
     * 取得文件大小
     * 
     * @param file
     *            文件
     * @return 文件大小
     */
    private static long getFileSize(File file) {
        long size = 0;
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                size += getFileSize(subFile);
            } else {
                size += subFile.length();
            }
        }
        return size;
    }

    /**
     * 清理缓存
     */
    public static final void cleanCache(Context context) {
        File cacheFolder = new File(Settings.PIC_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory())
                file.delete();
        }
        // Toast.makeText(context, R.string.setting_clear_finished, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }

            int result = ExifInterface.ORIENTATION_UNDEFINED;
            try {
                ExifInterface exifInterface = new ExifInterface(dst.getPath());
                result =
                        exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int rotate = 0;
            switch (result) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            default:
                break;
            }

            Bitmap bitmap = BitmapFactory.decodeFile(dst.getPath(), opts);
            if (rotate > 0) {
                bitmap = BitmapUtils.rotateBitmap(bitmap, rotate);
            }

            return bitmap;
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
            int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
            int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound =
                (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * MD5加密
     * 
     * @param 需要加密的String
     * @return 加密后String
     */
    @SuppressLint("DefaultLocale")
	public final static String MD5(String s) {
        char hexDigits[] =
                { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }

            return new String(str).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    // 检查网络状态
    public static boolean checkNetworkState(Context context) throws SocketException {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);

        State mobileState =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifiState =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        // 未连接网络
        if (!mobileState.equals(State.CONNECTED)
                && !mobileState.equals(State.CONNECTING) && !wifiState.equals(State.CONNECTED)
                && !wifiState.equals(State.CONNECTING)) {
            return false;
        }

        return true;
    }

    public static final String getTimeDiff2(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = dateFormat.parse(strDate);
            return getTimeDiff(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff1(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff3(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    @SuppressLint("SimpleDateFormat")
	public static final String getTimeDiffByFormat(String strDate,String formatS) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat(formatS);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff(Date date) {
        Calendar currentDate = Calendar.getInstance();// 获取当前时间
        long diff = currentDate.getTimeInMillis() - date.getTime();
        if (diff < 0)
            return 0 + "秒钟前";
        else if (diff < 60000)
            return diff / 1000 + "秒钟前";
        else if (diff < 3600000)
            return diff / 60000 + "分钟前";
        else if (diff < 86400000)
            return diff / 3600000 + "小时前";
        else
            return DateFormat.format("yyyy-MM-dd kk:mm", date).toString();
    }

    public static final boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        State mobileState =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifiState =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (!mobileState.equals(State.CONNECTED)
                && !mobileState.equals(State.CONNECTING) && !wifiState.equals(State.CONNECTED)
                && !wifiState.equals(State.CONNECTING)) {
            return false;
        }
        return true;
    }

    public static String getDisplayName(String username, String loginname) {

        if (username != null) {
            return username;
        } else {
            return loginname;
        }
    }
    /**
     * 
     * @param cc_time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static String getStrTime(String cc_time){
    	
    	String re_time = "";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	long lcc_time = Long.valueOf(cc_time);
    	re_time = sdf.format(new Date(lcc_time));
    	return re_time;
    	
    }
}

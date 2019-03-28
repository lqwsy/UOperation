package com.uflycn.uoperation.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;


import com.uflycn.uoperation.constant.AppConstant;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {
	/**
     * 鑾峰彇宸ヤ綔鐩綍
     *
     * @param context
     * @return
     */
    public static String getRootStoragePath(Context context) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + AppConstant.APP_STORAGE_PATH + File.separator;
            File file = new File(savePath);
            if (!file.exists()) {
                boolean bResult = file.mkdirs();
            }
            return savePath;
        } else {
            String tmp = context.getFilesDir().getAbsolutePath();
            if (tmp.endsWith(File.separator)) {
                return tmp;
            } else {
                return tmp + File.separator;
            }
        }
    }
    public static String getUflyRootPath(Context context) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + AppConstant.UFLY_STORAGE_PATH + File.separator;
            File file = new File(savePath);
            if (!file.exists()) {
                boolean bResult = file.mkdirs();
            }
            return savePath;
        } else {
            String tmp = context.getFilesDir().getAbsolutePath();
            if (tmp.endsWith(File.separator)) {
                return tmp;
            } else {
                return tmp + File.separator;
            }
        }
    }

    /**
     * 鍐欐枃鏈枃浠�     *
     * @param fileFullName
     * @param content
     */
    public static void writeTxtFile(String fileFullName, String content) {
        FileWriter fw = null;
        try {
            File file = new File(fileFullName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String readPamFile(File file) {
        if (!file.exists()) {
            return "";
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            String p = EncodingUtils.getString(buffer, "UTF-8");
            p = p.replace("+", "=");
            p = p.replace("!", "1");
            p = p.replace("*", "7");
            p = p.replace("#", "2");
            p = p.replace("~", "0");
            p = p.replace("%", "6");
            p = p.replace("?", "3");
            p = p.replace("/", "O");
            p = p.replace("@", "M");
            p = p.replace("^", "9");
            byte[] tmp = p.getBytes();
            byte[] decodeBuff = Base64.decode(tmp, 0, tmp.length, Base64.DEFAULT);
            if (decodeBuff == null) {
                return "";
            }
            return EncodingUtils.getString(decodeBuff, "UTF-8");
        } catch (Exception e) {
            return "";
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFile(File file) {
        if (!file.exists()) {
            return "";
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            return EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            return "";
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFileSdcard(String fileName, String message) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取android当前可用内存大小 
     * @return
     */
    public static String getAvailMemory(Activity activity) {// 获取android当前可用内存大小  
    	  
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);  
        MemoryInfo mi = new MemoryInfo();  
        am.getMemoryInfo(mi);  
        //mi.availMem; 当前系统的可用内存  
  
        return Formatter.formatFileSize(activity.getBaseContext(), mi.availMem);// 将获取的内存大小规格化  
    }  
  
    /**
     * 系统内存总大小
     * @param activity
     * @return
     */
    public static String getTotalMemory(Activity activity) {  
        String str1 = "/proc/meminfo";// 系统内存信息文件  
        String str2;  
        String[] arrayOfString;  
        long initial_memory = 0;  
  
        try {  
            FileReader localFileReader = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(  
                    localFileReader, 8192);  
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小  
  
            arrayOfString = str2.split("\\s+");  
            for (String num : arrayOfString) {  
                Log.i(str2, num + "\t");  
            }  
  
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte  
            localBufferedReader.close();  
  
        } catch (IOException e) {  
        }  
        return Formatter.formatFileSize(activity.getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化  
    }
    
    /**      
    * 取得空闲sd卡空间大小      
    * @return      
    */    
    public static double getAvailaleSize(){     
	    File path = Environment.getExternalStorageDirectory(); 
	    //取得sdcard文件路径     
	    StatFs stat = new StatFs(path.getPath());      

	    //获取block的SIZE    
	    long blockSize = stat.getBlockSize();      
	    /*空闲的Block的数量*/    
	    long availableBlocks = stat.getAvailableBlocks();     
	    /* 返回bit大小值GB*/    
	    return availableBlocks * blockSize * 1.0/1024/1024/1024;      
	    //(availableBlocks * blockSize)/1024      KIB 单位     
	    //(availableBlocks * blockSize)/1024 /1024  MIB单位              
    }
    
    /**      
    * SD卡大小      
    * @return      
    */    
    public static double getAllSize(){     
	    File path = Environment.getExternalStorageDirectory();      
	    StatFs stat = new StatFs(path.getPath());      
	    /*获取block的SIZE*/    
	    long blockSize = stat.getBlockSize();      
	    /*块数量*/    
	    long availableBlocks = stat.getBlockCount();     
	    /* 返回bit大小值*/    
	    return availableBlocks * blockSize*1.0/1024/1024;      
    }


    public static boolean writeFile(String file, InputStream inputStream){
        try {
        OutputStream outputStream = null;
        File futureStudioIconFile = new File(file);

        try {
            byte[] fileReader = new byte[4096];

           // long fileSize = body.contentLength();
           // long fileSizeDownloaded = 0;
            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

             //   fileSizeDownloaded += read;
             //   Log.d("IOUtils", "file download: " + fileSizeDownloaded + " of " + fileSize);
            }

            outputStream.flush();

            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
    } catch (IOException e) {
        return false;
    }
    }

    /**
     * 删除文件
     */
    public static void delete(String filePathName) {
        if(TextUtils.isEmpty(filePathName)) return ;
        File file = new File(filePathName);
        if (file.isFile() && file.exists()) {
            boolean flag = file.delete();
        }
    }



    /**
     * 复制文件
     * @param fromPathName
     * @param toPathName
     * @return
     */
    public static int copy(String fromPathName, String toPathName) {
        try {
            InputStream from = new FileInputStream(fromPathName);
            return copy(from, toPathName);
        } catch (FileNotFoundException e) {
            return -1;
        }
    }

    /**
     * 复制文件
     * @param from
     * @param toPathName
     * @return
     */
    public static int copy(InputStream from, String toPathName) {
        try {
            delete(toPathName); // 先删除
            OutputStream to = new FileOutputStream(toPathName);
            byte buf[] = new byte[1024];
            int c;
            while ((c = from.read(buf)) > 0) {
                to.write(buf, 0, c);
            }
            from.close();
            to.close();
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 文件是否存在
     * @param filePathName
     * @return
     */
    public static boolean isExist(String filePathName) {
        File file = new File(filePathName);
        return (!file.isDirectory() && file.exists());
    }



}

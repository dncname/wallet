package com.zlw.base.ui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class ZFile {
    public static final long BYTES_KB = 1024;
    public static final long BYTES_100KB = 102400;
    public static final long BYTES_MB = 1048576;
    public static final long BYTES_10MB = 10485760;
    public static final long BYTES_GB = 1073741824;

    private static String _size(String formatter, double bytes, String unit) {
        return new DecimalFormat(formatter).format(bytes) + unit;
    }
    private static String _size(long bytes, String unit) {
        return bytes + unit;
    }

    public static String size(long bytes) {
        if (bytes > BYTES_GB) {
            return _size("#.##", bytes * 1.0 / BYTES_GB, "GB");
        }
        if (bytes > BYTES_10MB) {
            return _size("#.#", bytes * 1.0 / BYTES_MB, "MB");
        }
        if (bytes > BYTES_MB) {
            return _size("#.##", bytes * 1.0 / BYTES_MB, "MB");
        }
        if (bytes > BYTES_100KB) {
            return (bytes / BYTES_KB) + "KB";
        }
        if (bytes > BYTES_KB) {
            return _size("#.#", bytes * 1.0 / BYTES_KB, "KB");
        }
        return bytes + "B";
    }

    public static String mimeType(String filename) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(new File(filename)));
            return URLConnection.guessContentTypeFromStream(is);
        } catch (Exception e) {
            IoUtils.close(is);
        }
        return "";
    }
    public static String mimeTypeToExtension(String mimeType) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    public static String readString(Context context, String filename) {
        try {
            return IoUtils.readString(context.openFileInput(filename));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }
    public static String readString(String filename) {
        try {
            return IoUtils.readString(new File(filename));
        } catch (IOException e) {
        }
        return null;
    }
    public static void writeString(String filename, String data) {
        try {
            IoUtils.copy(new ByteArrayInputStream(data.getBytes()), new FileOutputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void writeString(Context context, String filename, String data) {
		_writeString(context, filename, data.getBytes(), true);
	}
    private static void _writeString(Context context, String filename, byte[] bytes, boolean retry) {
        try {
            IoUtils.copy(new ByteArrayInputStream(bytes), context.openFileOutput(filename, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            if (retry) {
                _writeString(context, filename, bytes, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ensureDir(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }


    public static boolean isExist(String filename) {
        return !TextUtils.isEmpty(filename) && new File(filename).exists();
    }
    public static boolean isFile(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }
        File file = new File(filename);
        return file.exists() && file.isFile();
    }
    public static boolean isDir(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }
        File file = new File(filename);
        return file.exists() && file.isDirectory();
    }

    public static boolean delete(String filename) {
        return !TextUtils.isEmpty(filename) && delete(new File(filename));
    }
    public static boolean delete(File file) {
        boolean result = true;
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File child : file.listFiles()) {
            result &= delete(child);
        }
        result &= file.delete();
        return result;
    }
    /**
     * 清空目录
     * onlyFile: true 只删除文件，目录被保留
     * */
    public static void clean(File dir, boolean onlyFile) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File child : dir.listFiles()) {
            if (!onlyFile) {
                delete(child);
            } else if (!dir.isDirectory()) {
                child.delete();
            }
        }
    }

    public static String objectToString(Object objects) {
        String oAuth_Base64 = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(objects);
            // 将字节流编码成base64的字符窜
            oAuth_Base64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oAuth_Base64;
    }

    public static Object stringToObject(String strBase64) {
        Object objects = null;
        if (TextUtils.isEmpty(strBase64)) {
            return objects;
        }
        //读取字节
        byte[] base64 = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            //读取对象
            objects = bis.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return objects;
        }
    }

    public static void writeObject(String filename, Object data) {
        writeString(filename, objectToString(data));
    }

    public static Object readObject(String filename) {
        try {
            String strBase64 = IoUtils.readString(new File(filename));
            return stringToObject(strBase64);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }
}

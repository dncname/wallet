package com.zlw.base.ui.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long copy(File in, OutputStream out) throws IOException {
        return copy(new FileInputStream(in), out);
    }

    public static long copy(InputStream in, File out) throws IOException {
        return copy(in, new FileOutputStream(out));
    }


    public static long copy(InputStream input, OutputStream output) throws IOException {
        try {
            return copy0(input, output);
        } finally {
            close(input);
            close(output);
        }
    }
    public static long copy0(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        output.flush();
        return count;
    }
    public static String readString(File in) throws IOException {
        return readString(new FileInputStream(in));
    }

    public static String readString(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os);
        return new String(os.toByteArray(), "UTF-8");
    }
    public static void writeString(String data, File out) throws IOException {
        copy(new ByteArrayInputStream(data.getBytes()), new FileOutputStream(out));
    }
    public static void writeString(String data, OutputStream os) throws IOException {
        copy(new ByteArrayInputStream(data.getBytes()), os);
    }
}
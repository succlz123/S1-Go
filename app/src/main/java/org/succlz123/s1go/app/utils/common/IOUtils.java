package org.succlz123.s1go.app.utils.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileLock;

/**
 * Created by succlz123 on 16/6/28.
 */
public class IOUtils {

    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
        }
    }

    public static void closeQuietly(FileLock fileLock) {
        try {
            if (fileLock != null) {
                fileLock.close();
            }
        }catch (Exception ignored) {
        }
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0L;
        int n1;
        for (boolean n = false; -1 != (n1 = input.read(buffer)); count += (long) n1) {
            output.write(buffer, 0, n1);
        }
        return count;
    }

    public static String getString(InputStream in) {
        byte[] data = getBytes(in);
        return data == null ? null : new String(data);
    }

    public static byte[] getBytes(InputStream in) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = in.read(buffer)) != -1)
                baos.write(buffer, 0, len);
            in.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static String inputStream2StringByGB2312(InputStream is) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "GB2312"));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception ignored) {
        } finally {
            closeQuietly(reader);
        }
        return buffer.toString();
    }
}

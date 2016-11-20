/*
 * Copyright (C) 2013 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.succlz123.s1go.app.utils.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by succlz123 on 16/5/4.
 */
public final class FileUtils {
    public static final String TAG = "FileUtils";

    public static final String ROOT = "/";
    public static final String HIDDEN_PREFIX = ".";

//    public final static int ICON_FOLDER = R.drawable.ic_folder;
//    public final static int ICON_FILE = R.drawable.ic_file;

    private FileUtils() {
    }

    /**
     * File and folder comparator. TODO Expose sorting option method
     */
    public static Comparator<File> sComparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase(Locale.US).compareTo(
                    f2.getName().toLowerCase(Locale.US));
        }
    };

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories) and skip hidden files
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    public static final int FILTER_SKIP_HIDDEN = 1;
    public static final int FILTER_SKIP_IS_DIRECTORY = 2;

    /**
     * folder filter.
     */
    public static FileFilter sDirFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return directories only and skip hidden directories
            return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    public static boolean isRoot(String path) {
        return path.equals(FileUtils.ROOT);
    }

    public static void copyFile(String oldPath, String newPath) {
        copyFile(new File(oldPath), newPath);
    }

    public static void copyFile(File oldFile, String newPath) {
        try {
            int byteSum = 0;
            int byteRead = 0;
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(oldFile.getPath());
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                int length;
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead;
                    fs.write(buffer, 0, byteRead);
                }
                fs.flush();
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyTo(File src, File newFile) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();
            fo = new FileOutputStream(newFile);
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }
}

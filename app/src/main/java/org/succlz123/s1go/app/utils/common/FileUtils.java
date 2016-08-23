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
}

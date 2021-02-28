/*
 *  Copyright (c) 2021 Allette Systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;
import java.io.FileFilter;

public class FileUtils {
  /**
   * Filters directories.
   */
  public static final FileFilter DIR_FILTER = new FileFilter() {

    @Override
    public boolean accept(File f) {
      return f.isDirectory();
    }

  };
}

/*
 *  Copyright (c) 2021 Allette Systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

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

  /**
   * Returns the content of the source file.
   *
   * @param source the source
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String read(File source) throws IOException {
    return read(source, "UTF-8");
  }

  /**
   * Returns the content of the source file.
   *
   * @param source the source
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String read(File source, String charset) throws IOException {
    Charset encoding = Charset.forName(charset);
    byte[] encoded = Files.readAllBytes(source.toPath());
    return new String(encoded, encoding);
  }
}

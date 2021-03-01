package org.pageseeder.xlsx.util;

import java.io.File;

public class ToolUtils {

  public static File getDefaultWorkingFolder() {
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    File working = new File(tempDir, "xlsx-" + System.currentTimeMillis());
    if (!working.exists()) {
      working.mkdirs();
    }
    return working;
  }
}

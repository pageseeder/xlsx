/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

/**
 * XML Name spaces used in this.
 *
 * @author Christophe Lauret
 * @version 18 April 2012
 */
public final class Namespaces {

  /**
   * Spreadsheet Markup Language, usually mapped to "ss".
   *
   * <p><code>http://schemas.openxmlformats.org/spreadsheetml/2006/main</code>
   */
  public static final String SPREADSHEETML = "http://schemas.openxmlformats.org/spreadsheetml/2006/main";

  /**
   * Relationships, usually mapped to "r".
   *
   * <p><code>http://schemas.openxmlformats.org/officeDocument/2006/relationships</code>
   */
  public static final String RELATIONSHIPS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";

  /**
   * Package relationships, used in ".rels" files.
   *
   * <p><code>http://schemas.openxmlformats.org/package/2006/relationships</code>
   */
  public static final String PACKAGE_RELATIONSHIPS = "http://schemas.openxmlformats.org/package/2006/relationships";

  /**
   * Utility class.
   */
  private Namespaces() {
  }

}

/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.config;

/**
 * The level at which the spreadsheet be split (workbook, worksheet, row).
 *
 * <p>In contrast to usual Java style, the constants in this class use lower case as they are used
 * as ANT properties.
 *
 * @author Christophe Lauret
 * @version 16 April 2012
 */
public enum SplitLevel {

  /**
   * The entire workbook should be consolidated as one XML document.
   */
  workbook,

  /**
   * The workbook should be split by work sheet.
   */
  worksheet,

  /**
   * The workbook should be split by work sheet first, then for each sheet by row.
   */
  row;

}

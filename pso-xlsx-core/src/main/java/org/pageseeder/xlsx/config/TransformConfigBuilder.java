/*
 *  Copyright (c) 2021 Allette Systems pty. ltd.
 */
package org.pageseeder.xlsx.config;

import org.pageseeder.xlsx.util.ToolUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransformConfigBuilder {
  /**
   * The spreadsheet to transform.
   */
  private File input;

  /**
   * The working directory.
   */
  private File working;

  /**
   * The destination directory or file.
   */
  private File destination;

  /**
   * whether support rich text.
   */
  private boolean richtext;

  /**
   * The level at which the workbook should be split (default is <code>row</code>).
   */
  private SplitLevel level;

  /**
   * The column to use as the filename for each row (split by row only).
   * The column to use to generate the filenames.
   */
  private int filenameColumn;

  /**
   * Indicates whether the first row should be used for headers.
   */
  private boolean headers;

  /**
   * The document type for the row documents.
   */
  private String rowDoctype;

  /**
   * The document type for the row documents.
   */
  private String sheetDoctype;

  /**
   * The document type for the row documents.
   */
  private String workbookDoctype;

  /**
   * The path to the file to use to transform into PSXML.
   */
  private File xslt;

  /**
   * List of parameters specified for the transformation into PSXML
   */
  private List<Param> parameters;

  public TransformConfigBuilder input (File input) {
    this.input = input;
    return this;
  }

  public TransformConfigBuilder working (File working) {
    this.working = working;
    return this;
  }

  public TransformConfigBuilder destination (File destination) {
    this.destination = destination;
    return this;
  }

  public TransformConfigBuilder richtext (boolean richtext) {
    this.richtext = richtext;
    return this;
  }

  public TransformConfigBuilder level (SplitLevel level) {
    this.level = level;
    return this;
  }

  public TransformConfigBuilder level (String level) {
    this.level = SplitLevel.valueOf(level);
    return this;
  }

  public TransformConfigBuilder filenameColumn (int filenameColumn) {
    this.filenameColumn = filenameColumn;
    return this;
  }

  public TransformConfigBuilder headers (boolean headers) {
    this.headers = headers;
    return this;
  }

  public TransformConfigBuilder rowDoctype (String rowDoctype) {
    this.rowDoctype = rowDoctype;
    return this;
  }

  public TransformConfigBuilder sheetDoctype (String sheetDoctype) {
    this.sheetDoctype = sheetDoctype;
    return this;
  }

  public TransformConfigBuilder workbookDoctype(String workbookDoctype) {
    this.workbookDoctype = workbookDoctype;
    return this;
  }

  public TransformConfigBuilder xslt (File xslt) {
    this.xslt = xslt;
    return this;
  }

  public TransformConfigBuilder parameters (List<Param> parameters) {
    this.parameters = parameters;
    return this;
  }

  public TransformConfigBuilder parameter (Param parameter) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(parameter);
    return this;
  }

  public TransformConfig build(){
    this.setupMissingValuesToDefault();

    return new TransformConfig(this.input, this.working, this.destination, this.richtext,
        this.level, this.filenameColumn, this.headers, this.rowDoctype, this.sheetDoctype, this.workbookDoctype, this.xslt, this.parameters);
  }

  private void setupMissingValuesToDefault () {
    // Set working directory
    if (this.working == null) {
      this.working = ToolUtils.getDefaultWorkingFolder();
    }

    if (!this.working.exists()) this.working.mkdirs();

    if (this.destination == null && this.input != null) this.destination = this.input.getParentFile();

    if (this.level == null) this.level = SplitLevel.row;

    if (this.parameters == null) this.parameters = Collections.emptyList();

  }

}

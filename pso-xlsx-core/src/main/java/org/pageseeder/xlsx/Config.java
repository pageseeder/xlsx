/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx;

import java.io.File;
import java.net.MalformedURLException;

import javax.xml.transform.Templates;

import org.pageseeder.xlsx.config.SplitLevel;
import org.pageseeder.xlsx.util.XSLT;


/**
 * Encapsulate the import configuration for this class.
 *
 * @author Christophe Lauret
 * @version 16 April 2012
 * @deprecated @see {@link org.pageseeder.xlsx.config.TransformConfig}
 */
public final class Config {

  /**
   * The level at which the workbook should be split (default is <code>row</code>).
   */
  private SplitLevel _level = SplitLevel.row;

  /**
   * The path to the file to use to xslt into PSXML.
   */
  private File _xslt;

  /**
   * Whether to use the first row as headers.
   */
  private boolean _headers = true;

  /**
   * The column to use to generate the filenames.
   */
  private int _filenameColumn = 0;

  /**
   * The document type for the row documents.
   */
  private String _rowDoctype = "standard";

  /**
   * The document type for the row documents.
   */
  private String _sheetDoctype = "master";

  /**
   * The document type for the row documents.
   */
  private String _bookDoctype = "master";

  /**
   * Creates a new configuration.
   */
  public Config() {
  }

  /**
   * Sets the level at which the workbook should be split.
   *
   * @param level the level at which it should be split.
   */
  public void setSplitLevel(SplitLevel level) {
    this._level = level;
  }

  /**
   * Sets the XSLT to use to transform the interim format into PSXML.
   *
   * @param xslt the XSLT to use to transform the interim format into PSXML.
   */
  public void setTemplates(File xslt) {
    this._xslt = xslt;
  }

  /**
   * Indicates whether the first row should be used for headers.
   *
   * @param yes <code>true</code> if the first row should be used for headers;
   *            <code>false</code> otherwise.
   */
  public void setHeaders(boolean yes) {
    this._headers = yes;
  }

  /**
   * Sets the column to use as the filename for each row (split by row only).
   *
   * @param column the column to use as the filename for each row (split by row only).
   */
  public void setFilenameColumn(int column) {
    this._filenameColumn = column;
  }

  /**
   * @param bookDoctype the document type to use for a workbook document
   */
  public void setBookDoctype(String bookDoctype) {
    this._bookDoctype = bookDoctype;
  }

  /**
   * @param sheetDoctype the document type to use for a worksheet document
   */
  public void setSheetDoctype(String sheetDoctype) {
    this._sheetDoctype = sheetDoctype;
  }

  /**
   * @param rowDoctype the document type to use for a row document
   */
  public void setRowDoctype(String rowDoctype) {
    this._rowDoctype = rowDoctype;
  }

  /**
   * @return the column to use as the filename for each row (split by row only).
   */
  public int getFilenameColumn() {
    return this._filenameColumn;
  }

  /**
   * @return the level at which the workbook should be split.
   */
  public SplitLevel getSplitLevel() {
    return this._level;
  }

  /**
   * @return <code>true</code> if the first row should be used for headers; <code>false</code> otherwise.
   */
  public boolean hasHeaders() {
    return this._headers;
  }

  /**
   * @return the XSLT to use to transform the interim format into PSXML;
   *         <code>null</code> if using default templates.
   */
  public File getTemplatesFile() {
    return this._xslt;
  }

  /**
   * @return the Templates to turn into PSXML.
   */
  public Templates getTemplates() {
    if (this._xslt != null) {
      try {
        return XSLT.getTemplates(this._xslt.toURI().toURL());
      } catch (MalformedURLException ex) {
        throw new XLSXException(ex);
      }
    } else {
      return XSLT.getTemplatesFromResource("org/pageseeder/xlsx/xslt/interim-to-psxml.xsl");
    }
  }

  /**
   * @return the document type to use for a 'workbook' document.
   */
  public String getBookDoctype() {
    return this._bookDoctype;
  }

  /**
   * @return the document type to use for a 'worksheet' document.
   */
  public String getSheetDoctype() {
    return this._sheetDoctype;
  }

  /**
   * @return the document type to use for a 'row' document.
   */
  public String getRowDoctype() {
    return this._rowDoctype;
  }

}

/*
 *  Copyright (c) 2021 Allette Systems pty. ltd.
 */
package org.pageseeder.xlsx.config;

import org.pageseeder.xlsx.XLSXException;
import org.pageseeder.xlsx.util.XSLT;

import javax.xml.transform.Templates;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Encapsulate the transformation configuration for TransformProcessor.
 *
 * @author Adriano Akaishi
 * @author Carlos Cabral
 * @version 22 February 2021
 */
public final class TransformConfig {

  /**
   * The spreadsheet to transform.
   */
  private final File _input;

  /**
   * The working directory.
   */
  private final File _working;

  /**
   * The destination directory or file.
   */
  private final File _destination;

  /**
   * whether support rich text.
   */
  private final boolean _richtext;

  /**
   * The level at which the workbook should be split (default is <code>row</code>).
   */
  private final SplitLevel _level;

  /**
   * The column to use as the filename for each row (split by row only).
   * The column to use to generate the filenames.
   */
  private final int _filenameColumn;

  /**
   * Indicates whether the first row should be used for headers.
   */
  private final boolean _headers;

  /**
   * The document type for the row documents.
   */
  private final String _rowDoctype;

  /**
   * The document type for the row documents.
   */
  private final String _sheetDoctype;

  /**
   * The document type for the row documents.
   */
  private final String _workbookDoctype;

  /**
   * The path to the file to use to xslt into PSXML.
   */
  private final File _xslt;

  /**
   * List of parameters specified for the transformation into PSXML
   */
   private final List<Param> _parameters;


  /**
   * Creates a new configuration.
   */
  public TransformConfig(File input, File working, File destination, boolean richtext, SplitLevel level, int filenameColumn,
                         boolean headers, String rowDoctype, String sheetDoctype, String workbookDoctype, File xslt, List parameters) {
    this._input = input;
    this._working = working;
    this._destination = destination;
    this._richtext = richtext;
    this._level = level;
    this._filenameColumn = filenameColumn;
    this._headers = headers;
    this._rowDoctype = rowDoctype;
    this._sheetDoctype = sheetDoctype;
    this._workbookDoctype = workbookDoctype;
    this._xslt = xslt;
    this._parameters = parameters;
  }

  public File getInput() {
    return this._input;
  }

  public File getWorking() {
    return this._working;
  }

  public File getDestination() {
    return this._destination;
  }

  public boolean getRichtext(){
    return this._richtext;
  }

  /**
   * @return the level at which the workbook should be split.
   */
  public SplitLevel getSplitLevel() {
    return this._level;
  }

  /**
   * @return the column to use as the filename for each row (split by row only).
   */
  public int getFilenameColumn() {
    return this._filenameColumn;
  }

  /**
   * @return <code>true</code> if the first row should be used for headers; <code>false</code> otherwise.
   */
  public boolean hasHeaders() {
    return this._headers;
  }

  /**
   * @return the document type to use for a 'workbook' document.
   */
  public String getWorkbookDoctype() {
    return this._workbookDoctype;
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

  /**
   * @return the XSLT to use to xslt the interim format into PSXML;
   *         <code>null</code> if using default templates.
   */
  public File getTemplatesFile() {
    return this._xslt;
  }

  /**
   * @return List of parameters specified for the transformation into PSXML
   */
  public List<Param> get_parameters() {
    return _parameters;
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

}

/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.core;

import java.io.File;

import org.pageseeder.xlsx.XLSXException;
import org.pageseeder.xlsx.util.Namespaces;
import org.pageseeder.xlsx.util.XML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public final class WorkSheet {

  /**
   * The name of the work sheet in the workbook.
   */
  private String _name;

  /**
   * The Excel source of the work sheet in the Office package.
   */
  private File _source;

  /**
   * The size of the work sheet (number or rows)
   */
  private int _size;

  /**
   * Creates a new worksheet.
   *
   * @param name   The name of the work sheet in the workbook.
   * @param source The Excel source of the work sheet in the Office package.
   */
  public WorkSheet(String name, File source) {
    this._name = name;
    this._source = source;
  }

  /**
   * @return The name of the work sheet in the workbook.
   */
  public String name() {
    return this._name;
  }

  /**
   * @return The Excel source of the work sheet in the Office package.
   */
  public File source() {
    return this._source;
  }

  /**
   * @return the size of the sheet.
   */
  public int size() {
    return this._size;
  }

  /**
   * Inspect this worksheet.
   */
  public void inspect() {
    Inspector inspector = new Inspector();
    try {
      XML.parse(this.source(), inspector, true);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to inspect worksheet:"+this._name, ex.getCause());
    }
    this._size = inspector.getSize();
  }

  /**
   * Inspects a work sheet.
   *
   * @author Christophe Lauret
   * @version 18 April 2012
   */
  private class Inspector extends DefaultHandler {

    /** The number of rows found. */
    private int count = 0;

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts)
        throws SAXException {
      if (Namespaces.SPREADSHEETML.equals(uri) && "row".equals(name)) {
        this.count++;
      }
    }

    /** @return The number of rows found in the sheet. */
    public int getSize() {
      return this.count;
    }

  }
}

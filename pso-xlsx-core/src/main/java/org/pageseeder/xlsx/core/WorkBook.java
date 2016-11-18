/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.pageseeder.xlsx.XLSXException;
import org.pageseeder.xlsx.util.Namespaces;
import org.pageseeder.xlsx.util.Relationship;
import org.pageseeder.xlsx.util.Relationships;
import org.pageseeder.xlsx.util.XML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Stores the relationships from a ".rels" file.
 *
 * @author Christophe Lauret
 * @version 18 April 2012
 */
public final class WorkBook {

  /**
   * The workbook title.
   */
  private String _title;

  /**
   * The sheets in this workbook in order.
   */
  private final List<WorkSheet> _sheets;

  /**
   * Creates a workbook.
   *
   * @param sheets the work sheets in this workbook.
   */
  private WorkBook(List<WorkSheet> sheets) {
    this._sheets = sheets;
  }

  /**
   * @return the list of sheets in this workbook.
   */
  public List<WorkSheet> sheets() {
    return this._sheets;
  }

  /**
   * @return the title of the workbook.
   */
  public String getTitle() {
    return this._title;
  }

  /**
   * @param title the title of the workbook.
   */
  public void setTitle(String title) {
    this._title = title;
  }

  /**
   * Returns the workbook.
   *
   * @param workbook      the workbook file to parse.
   * @param relationships the relationships for this workbook.
   *
   * @return the corresponding workbook.
   */
  public static WorkBook parse(File workbook, Relationships relationships) {
    Loader handler = new Loader(relationships, workbook.getParentFile());
    try {
      XML.parse(workbook, handler, true);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to parse shared strings", ex.getCause());
    }
    return new WorkBook(handler.getWorkSheets());
  }

  /**
   * Extracts the relationships from a "rels" file.
   *
   * @author Christophe Lauret
   * @version 17 April 2012
   */
  private static class Loader extends DefaultHandler {

    /** The relationships */
    private final Relationships _relationships;

    /** The directory where the workbook */
    private final File _base;

    /** The shared strings. */
    private final List<WorkSheet> _sheets = new ArrayList<WorkSheet>();

    /**
     * Sole constructor.
     *
     * @param relationships the relationships of this workbook.
     * @param base          the base directory for the workbook.
     */
    public Loader(Relationships relationships, File base) {
      this._relationships = relationships;
      this._base = base;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException {
      // <sheet name="Schools" sheetId="2" r:id="rId1" />
      if (Namespaces.SPREADSHEETML.equals(uri) && "sheet".equals(localName)) {
        String sname = atts.getValue("name");
        String rid = atts.getValue(Namespaces.RELATIONSHIPS, "id");
        Relationship r = this._relationships.get(rid);
        File file = new File(this._base, r.target());
        WorkSheet sheet = new WorkSheet(sname, file);
        this._sheets.add(sheet);
      }
    }

    /**
     * @return the work sheets found.
     */
    public List<WorkSheet> getWorkSheets() {
      return this._sheets;
    }
  }

}

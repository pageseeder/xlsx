/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.pageseeder.xlsx.XLSXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A utility class to deal with master documents.
 *
 * @author Christophe Lauret
 * @version 12 April 2012
 */
public final class PSMasterDocUtils {

  /** Utility class. */
  private PSMasterDocUtils() {
  }

  /**
   * Returns the list of files from the specified master document.
   *
   * @param master The master document.
   * @return links  THe list of files to link.
   */
  public static List<File> listLinks(File master) {
    List<File> files = new ArrayList<File>();
    LinkExtractor handler = new LinkExtractor(master, files);
    try {
      XML.parse(master, handler, false);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to parse master document", ex.getCause());
    }
    return files;
  }

  /**
   * Extracts the links from the specified master document.
   *
   * @author Christophe Lauret
   * @version 12 April 2012
   */
  private static class LinkExtractor extends DefaultHandler {

    /** The master document */
    private final File _master;

    /** The linked files */
    private final List<File> _files;

    /**
     * Creates a new link extractor.
     *
     * @param master the master document.
     * @param files  the linked files.
     */
    public LinkExtractor(File master, List<File> files) {
      this._master = master;
      this._files = files;
    }

    @Override
    public void startElement(String name, String qName, String uri, Attributes atts)
        throws SAXException {
      if ("xref".equals(name) || "xref".equals(qName)) {
        String href = atts.getValue("href");
        File linked = new File(this._master, href);
        this._files.add(linked);
      }
    }
  }
}

/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;

import org.pageseeder.xlsx.XLSXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * THe core document properties.
 *
 * @author Christophe Lauret
 * @version 13 April 2012
 */
public final class CoreProperties {

  /**
   * The title of the presentation.
   */
  private String _title = null;

  /**
   * @return the document title.
   */
  public String title() {
    return this._title;
  }

  /**
   * Sole constructor.
   */
  private CoreProperties() {
  }

  /**
   * Parses the <code>docProps/core.xml</code> file.
   *
   * @param core The core office document properties
   * @return the corresponding instance.
   *
   * @throws BuildException If an error occurs while trying to parse the presentation.
   */
  public static CoreProperties parse(File core) {
    Handler handler = new Handler();
    try {
      XML.parse(core, handler, true);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to parse core document properties", ex.getCause());
    }
    return handler.core();
  }

  /**
   * Handles the docProps/core.xml file.
   *
   * @author Christophe Lauret
   * @version 13 April 2012
   */
  private static class Handler extends DefaultHandler {

    /**
     * Namespace for the dublin core properties.
     */
    private static final String DUBLIN_CORE_NS = "http://purl.org/dc/elements/1.1/";

    /** Core properties to build. */
    private CoreProperties core = new CoreProperties();

    /** Buffer. */
    private StringBuilder buffer = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (DUBLIN_CORE_NS.equals(uri) && "title".equals(localName)) {
        this.buffer = new StringBuilder();
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (this.buffer != null) this.buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (DUBLIN_CORE_NS.equals(uri) && "title".equals(localName)) {
        this.core._title = this.buffer.toString();
        this.buffer = null;
      }
    }

    /**
     * @return the core properties created from parsing.
     */
    public CoreProperties core() {
      return this.core;
    }
  }
}

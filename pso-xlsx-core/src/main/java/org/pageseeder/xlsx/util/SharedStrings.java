/*
 * Copyright 2021 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * Stores the shared strings from the Excel Open XML format.
 *
 * @author Christophe Lauret
 * @version 19 April 2012
 */
public final class SharedStrings {

  /**
   * All the shared strings.
   */
  private final List<String> _shared;

  /**
   * Creates a new shared string instance.
   * @param shared the list of shared strings.
   */
  private SharedStrings(List<String> shared) {
    this._shared = shared;
  }

  /**
   * @param i the index of the shared string.
   * @return the shared string
   */
  public String get(int i) {
    return this._shared.get(i);
  }

  /**
   * @return the number of shared strings.
   */
  public int length() {
    return this._shared.size();
  }

  /**
   * Returns the shared strings from the specified file.
   *
   * @param shared The files where all the shared strings are stored.
   * @return links  THe list of files to link.
   */
  public static SharedStrings parse(File shared) {
    Loader handler = new Loader();
    try {
      XML.parse(shared, handler, true);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to parse shared strings", ex.getCause());
    }
    return new SharedStrings(handler.getShared());
  }

  /**
   * Loads the shared strings from the shared strings document.
   *
   * <p>Not this class will not preserve markup or formatting within the shared strings.
   *
   * <p>The XML to parse looks like:
   * <pre>{@code
   * <sst xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" count="775" uniqueCount="243">
   *   <si><t>Some string</t></si>
   *   <si><t>Some string</t></si>
   *   <si><t>Some string</t></si>
   *   <si><t>Some string</t></si>
   *   ...
   * </sst>
   * }</pre>
   *
   * @author Christophe Lauret
   * @version 17 April 2012
   */
  private static class Loader extends DefaultHandler {

    /** The shared strings. */
    private final List<String> _shared = new ArrayList<String>();

    /** Buffer. */
    private StringBuilder buffer = new StringBuilder();

    /** Indicates when we need to record the values in the buffer. */
    private boolean record = false;

    /** Indicates the number of shared strings we should expect. */
    private int expected = -1;

    /** Sole constructor. */
    public Loader() {
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts)
        throws SAXException {
      if (Namespaces.SPREADSHEETML.equals(uri)) {
        if ("t".equals(localName)) {
          this.record = true;
        } else if ("si".equals(localName)) {
          this.buffer.setLength(0);
        } else if ("sst".equals(localName)) {
          this.expected = Integer.parseInt(atts.getValue("uniqueCount"));
        }
      }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
      if (Namespaces.SPREADSHEETML.equals(uri)) {
        if ("si".equals(localName)) {
          this._shared.add(this.buffer.toString());
        } else if ("t".equals(localName)) {
          this.record = false;
        } else if ("sst".equals(localName)) {
          if (this.expected != this._shared.size()) {
            System.err.println("Expected "+this.expected+" but found "+this._shared.size());
          }
        }
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (this.record)
        this.buffer.append(ch, start, length);
    }

    /**
     * @return the shared strings found.
     */
    public List<String> getShared() {
      return this._shared;
    }
  }


}

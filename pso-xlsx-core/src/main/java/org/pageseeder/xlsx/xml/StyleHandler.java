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
package org.pageseeder.xlsx.xml;

import org.pageseeder.xlsx.core.CellFormat;
import org.pageseeder.xlsx.core.Style;
import org.pageseeder.xlsx.util.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 */
public class StyleHandler extends DefaultHandler {

  private final Style style = new Style();

  private int cellFormatIndex = 0;

  /** Sole constructor. */
  public StyleHandler() {
  }

  /**
   * element cellXfs
   */
  private boolean insideCellFormats = false;
  @Override
  public void startElement(String uri, String localName, String name, Attributes atts)
      throws SAXException {

    String elementName = localName != null ? localName : name;

    //cellXfs
    if (Namespaces.SPREADSHEETML.equals(uri)) {
      switch (elementName) {
        case "cellXfs":
          this.insideCellFormats = true;
          break;
        case "xf":
          if (this.insideCellFormats) {
            final int numberFormatId = new Integer(atts.getValue("numFmtId"));
            final boolean applyNumberFormat = "1".equals(atts.getValue("applyNumberFormat"));
            style.addCellFormat(new CellFormat(this.cellFormatIndex++, numberFormatId, applyNumberFormat));
          }
          break;
        case "numFmt":
          final int numberFormatId = new Integer(atts.getValue("numFmtId"));
          final String formatCode = atts.getValue("formatCode");
          this.style.addNumberFormat(numberFormatId, formatCode);
          break;
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String name)
      throws SAXException {

    String elementName = localName != null ? localName : name;

    if (Namespaces.SPREADSHEETML.equals(uri)) {
      if ("cellXfs".equals(elementName)) {
        this.insideCellFormats = false;
      }
    }
  }

  public Style getStyle() {
    return this.style;
  }
}

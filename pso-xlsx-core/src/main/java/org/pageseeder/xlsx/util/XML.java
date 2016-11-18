/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.pageseeder.xlsx.XLSXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A bunch of utility methods for XML.
 *
 * @author Christophe Lauret
 * @version 16 April 2012
 */
public final class XML {

  /**
   * Defines the XML extension.
   */
  public static final String XML_EXTENSION = ".xml";

  /**
   * Filters XML files.
   */
  private static final FileFilter FILE_FILTER = new FileFilter() {

    @Override
    public boolean accept(File f) {
      if (f.isDirectory()) return false;
      String name = f.getName();
      return name.length() > XML_EXTENSION.length() && name.endsWith(XML_EXTENSION);
    }

  };

  /**
   * Utility method.
   */
  private XML() {
  }

  /**
   * Returns the XML FileFilter instance.
   *
   * @return the singleton instance of the XMl file filter.
   */
  public static FileFilter getFileFilter() {
    return FILE_FILTER;
  }

  /**
   * Parses the specified file.
   *
   * @param file      the file to parse
   * @param handler   the handler
   * @param namespace whether the parse is namespace aware.
   */
  public static void parse(File file, DefaultHandler handler, boolean namespace) {
    try (InputStream in = new FileInputStream(file)) {
      InputSource source = new InputSource(in);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(namespace);
      SAXParser parser = factory.newSAXParser();
      parser.parse(source, handler);
    } catch (IOException ex) {
      throw new XLSXException("Unable to parse file "+file.getAbsolutePath(), ex);
    } catch (SAXException ex) {
      throw new XLSXException("Error while parsing file "+file.getAbsolutePath(), ex);
    } catch (ParserConfigurationException ex) {
      throw new XLSXException("Unable to configure parser", ex);
    }
  }

  /**
   * Escapes the character data within an element for an UTF-8 encoded XML document.
   *
   * <p>
   * Replace characters which are invalid in element values, by the corresponding entity in a given <code>String</code>.
   *
   * <p>
   * These characters are:<br>
   * <ul>
   *   <li>'&amp' by the ampersand entity "&amp;amp"</li>
   *   <li>'&lt;' by the entity "&amp;lt;"</li>
   * </ul>
   * </p>
   *
   * <p>
   * Empty strings or <code>null</code> return respectively "" and <code>null</code>.
   *
   * <p>
   * This method is lenient in the sense that it will not report illegal characters that cannot be escaped such as
   * control characters.
   *
   * <p>
   * This method assumes that the given text is not XML data; in other words that is it does not attempt to understand
   * entities, child elements or other XML data - entities will therefore be escapes as if they were just text.
   *
   * @param text the text value to escape
   * @return the corresponding escaped text
   */
  public static String text(String text) {
    // bypass null and empty strings
    if (text == null || "".equals(text)) return text;
    // do not process valid strings
    if (text.indexOf('&') == -1 && text.indexOf('<') == -1) return text;
    // process the rest
    StringBuilder valid = new StringBuilder(text);
    int shift = 0;
    for (int i = 0; i < text.length(); i++) {
      switch (text.charAt(i)) {
        case '&':
          valid.deleteCharAt(i + shift);
          valid.insert(i + shift, "&amp;");
          shift += "&amp;".length() - 1;
          break;
        case '<':
          valid.deleteCharAt(i + shift);
          valid.insert(i + shift, "&lt;");
          shift += "&lt;".length() - 1;
          break;
        default:
      }
    }
    return valid.toString();
  }

  /**
   * Escapes the text to be used as an attribute value for an UTF-8 encoded XML document.
   *
   * <p>
   * Replace characters which are invalid in element values, by the corresponding entity in a given <code>String</code>.
   *
   * <p>
   * These characters are:<br>
   * <ul>
   *   <li>'&amp' by the ampersand entity "&amp;amp"</li>
   *   <li>'&lt;' by the entity "&amp;lt;"</li>
   *   <li>'&apos;' by the entity "&amp;apos;"</li>
   *   <li>'&quot;' by the entity "&amp;quot;"</li>
   * </ul>
   * </p>
   *
   * <p>
   * Empty strings or <code>null</code> return respectively "" and <code>null</code>.

   * <p>
   * This method is lenient in the sense that it will not report illegal characters that cannot be escaped such as
   * control characters.
   *
   * <pre>
   * {@code
   *   [10]     AttValue     ::=    '"' ([^<&"] | Reference)* '"'
   *                             |  "'" ([^<&'] | Reference)* "'"
   * }
   * </pre>
   *
   * <p>
   * This method assumes that the given text is not XML data, that is it does not attempt to understand entities, child
   * elements or other XML data.
   *
   * @param value the attribute value to escape
   * @return the corresponding escaped text
   */
  public static String attribute(String value) {
    // bypass null and empty strings
    if (value == null || "".equals(value)) return value;
    // do not process valid strings
    if (value.indexOf('&') == -1 && value.indexOf('<') == -1 && value.indexOf('\'') == -1 && value.indexOf('"') == -1)
      return value;
    // process the rest
    final int extraLength = 8;
    StringBuilder out = new StringBuilder(value.length() + extraLength);
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '&':
          out.append("&amp;");
          break;
        case '<':
          out.append("&lt;");
          break;
        case '\'':
          out.append("&apos;");
          break;
        case '"':
          out.append("&quot;");
          break;
        default:
          out.append(c);
      }
    }
    return out.toString();
  }
}

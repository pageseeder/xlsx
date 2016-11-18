package org.pageseeder.xlsx.interim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pageseeder.xlsx.util.XML;

/**
 * Models a row in a sheet.
 *
 * <p>This class is designed so that it can be updated for each row in the sheet rather than
 * be used for individual rows. In order words, once the columns and titles have been initialised,
 * the values should be modified for each row.
 *
 * @author Christophe Lauret
 * @version 18 April 2012
 */
public final class Row {

  /**
   * Base 26 symbols.
   */
  private static final char[] XL_BASE_26 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

  /**
   * The position of the row;
   */
  private int position = 0;

  /**
   * The column identifiers in excel (A, B, C, ...);
   */
  private final String[] columns;

  /**
   * The titles of the columns.
   */
  private final String[] titles;

  /**
   * The values of each column.
   */
  private final String[] values;

  /**
   * Creates a new row for the specified columns.
   *
   * @param spans the column spans (1:6)
   */
  public Row(String spans) {
    this(Row.toColumns(spans));
  }

  /**
   *
   * @return
   */
  public boolean hasValue() {
    for (String value : this.values) {
      if (value != null) return true;
    }
    return false;
  }

  /**
   * Creates a new row for the specified columns.
   *
   * @param cols the columns (A, B, C, ...)
   */
  public Row(List<String> cols) {
    this.columns = cols.toArray(new String[cols.size()]);
    this.titles = new String[cols.size()];
    this.values = new String[cols.size()];
    for (int i = 0 ; i < this.titles.length; i++) {
      this.titles[i] = "Column "+i;
    }
  }

  /**
   * @return the number of columns in the row.
   */
  public int size() {
    return this.columns.length;
  }

  /**
   * Reset the values.
   */
  public void reset() {
    Arrays.fill(this.values, null);
  }

  /**
   * @param position the position of the row.
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * @return the position of the row.
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * Return the title at the specified column.
   *
   * @param index The index of the column.
   * @return the title at the specified column.
   */
  public String title(int index) {
    return this.titles[index];
  }

  /**
   * Set the title of the specified column.
   *
   * @param column The column (A, B, C, ...)
   * @param title  The title of the column
   */
  public void title(String column, String title) {
    int i = index(column);
    this.titles[i] = title;
  }

  /**
   * Return the value at the specified column.
   *
   * @param index The index of the column.
   * @return the value at the specified column.
   */
  public String value(int index) {
    return this.values[index];
  }

  /**
   * Set the title of the specified column.
   *
   * @param column The column (A, B, C, ...)
   * @param value  The value of the column
   */
  public void value(String column, String value) {
    int i = index(column);
    this.values[i] = value;
  }

  /**
   * @param column the name of the column.
   * @return the index of the specified column.
   */
  public int index(String column) {
    for (int i = 0; i < this.columns.length; i++) {
      if (this.columns[i].equals(column)) return i;
    }
    return -1;
  }

  /**
   * Output this row as XML.
   *
   * <pre>{@code
   * <head>
   *   <col ref="A">Column 1</col>
   *   <col ref="B">Column 2</col>
   * </head>
   * }</pre>
   *
   * @param xml   Where to append the XML.
   *
   * @throws IOException if thrown while writing the XML.
   */
  public void toHeadXML(Appendable xml) throws IOException {
    xml.append("<head>\n");
    for (int i = 0; i < this.columns.length; i++) {
      xml.append("  <col ref=\"").append(this.columns[i]).append("\">");
      xml.append(XML.text(this.titles[i]));
      xml.append("</col>\n");
    }
    xml.append("</head>\n");
  }

  /**
   * Output this row as XML.
   *
   * <pre>{@code
   * <row position="2">
   *   <col ref="A">Ainslie Primary School</col>
   *   <col ref="B">Donaldson Street</col>
   *   <col ref="C">BRADDON</col>
   *   <col ref="D">ACT</col>
   *   <col ref="E">2601</col>
   *   <col ref="F">AUSTRALIA</col>
   * </row>
   * }</pre>
   *
   * @param xml   Where to append the XML.
   *
   * @throws IOException if thrown while writing the XML.
   */
  public void toXML(Appendable xml) throws IOException {
    xml.append("<row position=\"").append(Integer.toString(this.position)).append("\">\n");
    for (int i = 0; i < this.columns.length; i++) {
      xml.append("  <col ref=\"").append(this.columns[i]).append("\">");
      if (this.values[i] != null)
        xml.append(XML.text(this.values[i]));
      xml.append("</col>\n");
    }
    xml.append("</row>\n");
  }

  /**
   * Output this row as XML.
   *
   * <pre>{@code
   * <row position="2" title="0001" sheet-title="Sheet1" book-title="test">
   *   <col ref="A" title="This"/>
   *   <col ref="B" title="Column 2">be</col>
   *   <col ref="C" title="Column 3"/>
   *   <col ref="D" title="should"/>
   *   <col ref="E" title="Column 5"/>
   * </row>
   * }</pre>
   *
   * @param xml   Where to append the XML.
   * @param title Whether to display the title of the column.
   * @param sheet The title of the work sheet
   * @param book  The title of the work book
   *
   * @throws IOException if thrown while writing the XML.
   */
  public void toXML(Appendable xml, String title, String sheet, String book) throws IOException {
    xml.append("<row position=\"").append(Integer.toString(this.position)).append('"');
    xml.append(" title=\"").append(XML.attribute(title)).append('"');
    xml.append(" sheet-title=\"").append(XML.attribute(sheet)).append('"');
    xml.append(" book-title=\"").append(XML.attribute(book)).append('"');
    xml.append(">\n");
    for (int i = 0; i < this.columns.length; i++) {
      xml.append("  <col title=\"").append(XML.attribute(this.titles[i])).append("\">");
      if (this.values[i] != null)
        xml.append(XML.text(this.values[i]));
      xml.append("</col>\n");
    }
    xml.append("</row>\n");
  }

  // static helpers
  // ----------------------------------------------------------------------------------------------

  /**
   *
   * @param i
   * @return
   */
  public static List<String> toColumns(String spans) {
    int colon = spans.indexOf(':');
    int from = Integer.parseInt(spans.substring(0, colon)) - 1;
    int to =   Integer.parseInt(spans.substring(colon+1));
    List<String> columns = new ArrayList<String>();
    for (int i = from; i < to; i++) {
      columns.add(toColumn(i));
    }
    return columns;
  }

  public static String toColumn(String cell) {
    for (int i = 0; i < cell.length(); i++) {
      if (cell.charAt(i) < 'A') return cell.substring(0, i);
    }
    return null;
  }

  /**
   * Return the column reference in Excel up to 702 columns (26 * 27).
   *
   * @param i the 0-based column number
   *
   * @return the corresponding excel column reference.
   */
  private static String toColumn(int i) {
    final int base = XL_BASE_26.length;
    if (i < 0) {
      throw new IndexOutOfBoundsException();
    } else if (i < base) {
      return Character.toString(XL_BASE_26[i]);
    } else if (i < base*(base+1)) {
      char c0 = XL_BASE_26[i / base -1];
      char c1 = XL_BASE_26[i % base];
      return new String(new char[]{c0,c1});
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

}

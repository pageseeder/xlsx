/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pageseeder.xlsx.XLSXException;
import org.pageseeder.xlsx.util.Relationship.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Stores the relationships from a ".rels" file.
 *
 * @author Christophe Lauret
 * @version 18 April 2012
 */
public final class Relationships {

  /**
   * All the relationships mapped to their ID.
   */
  private final Map<String, Relationship> _relationships;

  /**
   * Creates relationships.
   * @param relationships the relationships mapped to their ID.
   */
  private Relationships(Map<String, Relationship> relationships) {
    this._relationships = relationships;
  }

  /**
   * @param id the ID of the relationship.
   * @return the corresponding relationship or <code>null</code>
   */
  public Relationship get(String id) {
    return this._relationships.get(id);
  }

  /**
   * @param type the type of relationship
   * @return the list of relationships matching that type
   */
  public Collection<Relationship> forType(Type type) {
    List<Relationship> types = new ArrayList<Relationship>();
    for (Relationship r : this._relationships.values()) {
      if (r.type() == type) {
        types.add(r);
      }
    }
    return types;
  }

  /**
   * @return the list of relationships matching that type
   */
  public Collection<Relationship> all() {
    return this._relationships.values();
  }

  /**
   * @return the number of shared strings.
   */
  public int length() {
    return this._relationships.size();
  }

  /**
   * Returns the shared strings from the specified file.
   *
   * @param shared The files where all the shared strings are stored.
   * @return links  THe list of files to link.
   */
  public static Relationships parse(File shared) {
    Loader handler = new Loader();
    try {
      XML.parse(shared, handler, true);
    } catch (XLSXException ex) {
      throw new XLSXException("Unable to parse shared strings", ex.getCause());
    }
    return new Relationships(handler.getRelationships());
  }

  /**
   * Extracts the relationships from a "rels" file.
   *
   * @author Christophe Lauret
   * @version 17 April 2012
   */
  private static class Loader extends DefaultHandler {

    /** The shared strings. */
    private final Map<String, Relationship> _relationships = new HashMap<String, Relationship>();

    /** Sole constructor. */
    public Loader() {
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts)
        throws SAXException {
      if (Namespaces.PACKAGE_RELATIONSHIPS.equals(uri) && "Relationship".equals(name)) {
        String id = atts.getValue("Id");
        String typeURI = atts.getValue("Type");
        Type type = Relationship.Type.unknown;
        if (typeURI.startsWith(Namespaces.RELATIONSHIPS)) {
          try {
            type = Type.valueOf(typeURI.substring(Namespaces.RELATIONSHIPS.length() + 1));
          } catch (IllegalArgumentException ex) {
            System.err.println("Unknown relationship type URI found:"+typeURI);
          }
        }
        String target = atts.getValue("Target");
        Relationship r = new Relationship(id, type, target);
        this._relationships.put(id, r);
      }
    }

    /**
     * @return the relationships found.
     */
    public Map<String, Relationship> getRelationships() {
      return this._relationships;
    }
  }

}

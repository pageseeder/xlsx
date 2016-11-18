/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

/**
 * Expresses a relationship.
 *
 * @author Christophe Lauret
 * @version 18 April 2012
 */
public final class Relationship {

  /**
   * The type of relationship.
   */
  public enum Type { worksheet, sharedStrings, styles, theme, xmlMaps, calcChain, unknown };

  /**
   * The relationship ID.
   */
  private final String _id;

  /**
   * The relationship type.
   */
  private final Type _type;

  /**
   * The relationship target.
   */
  private final String _target;

  /**
   * Creates a new relationship.
   *
   * @param id     The relationship ID.
   * @param type   The relationship type (usually a URI).
   * @param target The relationship target.
   */
  public Relationship(String id, Type type, String target) {
    this._id = id;
    this._type = type;
    this._target = target;
  }

  /**
   * @return The relationship ID.
   */
  public String id() {
    return this._id;
  }

  /**
   * @return The relationship target.
   */
  public String target() {
    return this._target;
  }

  /**
   * @return The relationship type (usually a URI).
   */
  public Type type() {
    return this._type;
  }
}

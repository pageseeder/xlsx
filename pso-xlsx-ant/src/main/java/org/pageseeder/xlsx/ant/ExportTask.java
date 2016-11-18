/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * An ANT task to export a PageSeeder document to a PowerPoint Presentation.
 *
 * @author Christophe Lauret
 * @version 12 April 2012
 */
public final class ExportTask extends Task {

  /**
   * The PageSeeder documents to export
   */
  private File _source;

  /**
   * The PowerPoint Presentation to generate.
   */
  private File _destination;

  /**
   * The name of the working directory
   */
  private File _working;

  // Set properties
  // ----------------------------------------------------------------------------------------------

  /**
   * Set the source file for the PowerPoint presentation.
   *
   * @param source The master document for presentation to export.
   */
  public void setSrc(File source) {
    if (!(source.exists())) {
      throw new BuildException("the presentation " + source.getName()+ " doesn't exist");
    }
    if (source.isDirectory()) {
      throw new BuildException("the presentation " + source.getName() + " can't be a directory");
    }
    this._source = source;
  }

  /**
   * Set the destination folder where PSML files should be stored.
   *
   * @param destination Where to store the PSML files.
   */
  public void setDest(File destination) {
    if (destination.exists() && destination.isDirectory()) {
      throw new BuildException("if destination PPTX exists, it must be a file");
    }
    this._destination = destination;
  }

  /**
   * Set the working folder (optional).
   *
   * @param working The working folder.
   */
  public void setWorking(File working) {
    if (working.exists() && !working.isDirectory()) {
      throw new BuildException("if working folder exists, it must be a directory");
    }
    this._working = working;
  }

  // Execute
  // ----------------------------------------------------------------------------------------------

  @Override
  public void execute() throws BuildException {
    if (this._source == null)
      throw new BuildException("Source presentation must be specified using 'src' attribute");
    // Defaulting working directory
    if (this._working == null) {
      String tmp = "antpptx-"+System.currentTimeMillis();
      this._working = new File(System.getProperty("java.io.tmpdir"), tmp);
    }
    if (!this._working.exists()) {
      this._working.mkdirs();
    }

    // The name of the presentation
    String name = this._source.getName();
    if (name.endsWith(".xml")) name = name.substring(0, name.length() - 4);

    // Defaulting destination directory
    if (this._destination == null) {
      this._destination = new File(this._source.getParentFile(), ".pptx");
      log("Destination set to "+this._destination.getName());
    }

    // Where we are going to assemble the pptx
    File prepacked = new File(this._working, "prepacked");

    // TODO

    log("=============================================================================");
    log("SORRY, but this has yet to be implemented!!!");
    log("=============================================================================");
  }

  // Helpers
  // ----------------------------------------------------------------------------------------------


}

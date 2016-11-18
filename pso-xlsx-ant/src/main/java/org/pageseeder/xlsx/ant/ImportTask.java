/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Templates;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.pageseeder.xlsx.Config;
import org.pageseeder.xlsx.config.SplitLevel;
import org.pageseeder.xlsx.core.WorkBook;
import org.pageseeder.xlsx.core.WorkSheet;
import org.pageseeder.xlsx.interim.Interim;
import org.pageseeder.xlsx.util.CoreProperties;
import org.pageseeder.xlsx.util.Relationship;
import org.pageseeder.xlsx.util.Relationships;
import org.pageseeder.xlsx.util.SharedStrings;
import org.pageseeder.xlsx.util.XML;
import org.pageseeder.xlsx.util.XSLT;
import org.pageseeder.xlsx.util.ZipUtils;

/**
 * An ANT task to import a Excel Spreadsheet as a single or multiple PageSeeder documents.
 *
 * @author Christophe Lauret
 * @version 19 April 2012
 */
public final class ImportTask extends Task {

  /**
   * Filters directories.
   */
  private static final FileFilter DIR_FILTER = new FileFilter() {

    @Override
    public boolean accept(File f) {
      return f.isDirectory();
    }

  };

  /**
   * The PowerPoint Presentation to import.
   */
  private File _source;

  /**
   * Where to create the PageSeeder documents (a directory).
   */
  private File _destination;

  /**
   * The name of the working directory
   */
  private File _working;
  
  /**
   * whether support rich text
   */
  private boolean _richtext;

  /**
   * The configuration.
   */
  private Config _config = new Config();

  /**
   * List of parameters specified for the transformation into PSXML
   */
  private List<Param> _parameters = new ArrayList<Param>();

  // Set properties
  // ----------------------------------------------------------------------------------------------

  /**
   * Set the source file for the Excel spreadsheet.
   *
   * @param spreadsheet The Excel spreadsheet to import.
   */
  public void setSrc(File spreadsheet) {
    if (!(spreadsheet.exists())) {
      throw new BuildException("the presentation " + spreadsheet.getName()+ " doesn't exist");
    }
    if (spreadsheet.isDirectory()) {
      throw new BuildException("the presentation " + spreadsheet.getName() + " can't be a directory");
    }
    String name = spreadsheet.getName();
    if (!name.endsWith(".xlsx") && !name.endsWith(".zip")) {
      log("presentation file should generally end with .pptx or .zip - but was "+name);
    }
    this._source = spreadsheet;
  }

  /**
   * Set the destination folder where the PageSeeder document should be created.
   *
   * @param destination The destination folder.
   */
  public void setDest(File destination) {
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

  /**
   * Set the working folder (optional).
   *
   * @param working The working folder.
   */
  public void setRichText(boolean richtext) {
    if (richtext){
      this._richtext = true;
    } else {
    this._richtext = false;
    }
  }

  /**
   * Sets the level at which the workbook should be split.
   *
   * @param level the level at which it should be split.
   */
  public void setSplitLevel(SplitLevel level) {
    this._config.setSplitLevel(level);
  }

  /**
   * Sets the XSLT to use to transform the interim format into PSXML.
   *
   * @param xslt the XSLT to use to transform the interim format into PSXML.
   */
  public void setTemplates(File xslt) {
    this._config.setTemplates(xslt);
  }

  /**
   * Create a parameter object and stores it in the list To be used by the XSLT transformation
   */
  public Param createParam() {
    Param param = new Param();
    this._parameters.add(param);
    return param;
  }

  /**
   * Indicates whether the first row should be used for headers.
   *
   * @param yes <code>true</code> if the first row should be used for headers;
   *            <code>false</code> otherwise.
   */
  public void setHeaders(boolean yes) {
    this._config.setHeaders(yes);
  }

  /**
   * Sets the column to use as the filename for each row (split by row only).
   *
   * @param column the column to use as the filename for each row (split by row only).
   */
  public void setFilenameColumn(int column) {
    this._config.setFilenameColumn(column);
  }

  /**
   * @param type the document type to use for a workbook document
   */
  public void setBookDoctype(String type) {
    this._config.setBookDoctype(type);
  }

  /**
   * @param type the document type to use for a worksheet document
   */
  public void setSheetDoctype(String type) {
    this._config.setSheetDoctype(type);
  }

  /**
   * @param type the document type to use for a row document
   */
  public void setRowDoctype(String type) {
    this._config.setRowDoctype(type);
  }

  // Execute
  // ----------------------------------------------------------------------------------------------

  @Override
  public void execute() throws BuildException {
    if (this._source == null)
      throw new BuildException("Source presentation must be specified using 'src' attribute");

    // Defaulting working directory
    if (this._working == null) this._working = getDefaultWorkingFolder();
    if (!this._working.exists()) this._working.mkdirs();

    // Defaulting destination directory
    if (this._destination == null) {
      this._destination = this._source.getParentFile();
      log("Destination set to source directory "+this._destination.getAbsolutePath()+"");
    }
    
    // Rich text support
    log("Supporting Rich Text: "+ this._richtext );
    
    // Check ignored options
    if (this._config.getSplitLevel() != SplitLevel.row) {
      if (this._config.getFilenameColumn() > 0) {
        log("Setting the column filename has no effect when split level != row");
      }
    }

    // The folder and name of the presentation
    File folder = null;
    String name = null;
    if (this._destination.getName().indexOf('.') > 0) {
      folder = this._destination.getParentFile();
      name = this._destination.getName();
      if (name.endsWith(XML.XML_EXTENSION)) name = name.substring(0, name.length()-XML.XML_EXTENSION.length());
    } else {
      folder = this._destination;
      name = this._source.getName();
      if (name.endsWith(".xlsx")) name = name.substring(0, name.length()-5);
    }

    // 1. Unzip file
    log("Extracting Excel Spreadsheet: " + this._source.getName());
    File unpacked = new File(this._working, "unpacked");
    unpacked.mkdir();
    ZipUtils.unzip(this._source, unpacked);

    // 2. Extract core properties
    log("Extracting Core document properties");
    CoreProperties core = CoreProperties.parse(new File(unpacked, "docProps/core.xml"));
    String title = core.title();

    // 3. Generating interim data
    log("Generating interim data");
    File interim = new File(this._working, "interim");
    interim.mkdir();
    
    // TODO need to do a automatic detect in order switch between XSLT and SAX
    if (!this._richtext) {
      log("Interim will be generated by SAX, richtext will be IGNORED.");
      generateInterimBySax(unpacked, interim, this._config, title != null? title : name);
    } else {
      log("Interim will be generated by XSLT, richtext will be SUPPORTED." , 1);
      generateInterimByXSLT(unpacked, interim, this._config, title != null? title : name);
    }

    // 4. Convert rows to PSXML
    log("Converting interim data to PSXML");
    interimToPSXML(interim, folder, this._config, this._parameters );

  }

  /**
   * Generate the interim files.
   *
   * @param unpacked The folder containing the unzipped workbook.
   * @param interim  The folder that should contain the interim simpler format.
   * @param config   The configuration.
   * @param title    The title of the workbook
   */
  private static void generateInterimByXSLT(File unpacked, File interim, Config config, String title) {

    // Creates files and folders
    File xlWorkbookFolder = new File(unpacked, "/xl");
    File xlWorkbook = new File(xlWorkbookFolder, "workbook.xml");
    File itWorkbook = new File(interim, "workbook.xml");
    if (!interim.exists() && !interim.mkdirs())
      throw new BuildException("Unable to create interim directory structure.");

    // Parse templates
    Templates templates = XSLT.getTemplatesFromResource("org/pageseeder/xlsx/xslt/split-workbook.xsl");
    String relationships = xlWorkbookFolder.toURI().toString()+"_rels/workbook.xml.rels";
    String outuri = interim.toURI().toString();

    // Initiate parameters
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("_relationships", relationships);
    parameters.put("_outputfolder", outuri);
    parameters.put("_booktitle", title);
    parameters.put("_splitlevel", config.getSplitLevel().toString());
    parameters.put("_hasheaders", Boolean.toString(config.hasHeaders()));
    parameters.put("_filenamecolumn", Integer.toString(config.getFilenameColumn()));

    // Transform
    XSLT.transform(xlWorkbook, itWorkbook, templates, parameters);
  }


  /**
   * Generate the interim files.
   *
   * @param unpacked The folder containing the unzipped workbook.
   * @param interim  The folder that should contain the interim simpler format.
   * @param config   The configuration.
   * @param title    The title of the workbook
   */
  private static void generateInterimBySax(File unpacked, File interim, Config config, String title) {

    // Creates files and folders
    File xlWorkbookFolder = new File(unpacked, "/xl");
    File xlWorkbook = new File(xlWorkbookFolder, "workbook.xml");
//    File itWorkbook = new File(interim, "workbook.xml");
    if (!interim.exists() && !interim.mkdirs())
      throw new BuildException("Unable to create interim directory structure.");

    // Parse relationships
    Relationships relationships = Relationships.parse(new File(xlWorkbookFolder, "_rels/workbook.xml.rels"));

    try {
      // Parse shared strings
      SharedStrings shared = null;
      for (Relationship r : relationships.forType(Relationship.Type.sharedStrings)) {
        shared = SharedStrings.parse(new File(xlWorkbookFolder, r.target()));
      }

      // Parse workbook
      WorkBook workbook = WorkBook.parse(xlWorkbook, relationships);
      workbook.setTitle(title);

      // Inspect
      for (WorkSheet sheet : workbook.sheets()) {
        sheet.inspect();
      }

      // Generate the interim data
      Interim processor = new Interim(interim, config);
      processor.process(workbook, shared);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Generate the interim files.
   *
   * @param interim The folder that should contain the interim simpler format.
   * @param output  The output folder where the PSXML should be stored
   * @param config  The configuration.
   */
  private static void interimToPSXML(File interim, File output, Config config, List<Param> params) {

    // Creates files and folders
    if (!output.exists() && !output.mkdirs())
      throw new BuildException("Unable to create output folder "+output.getName());

    // Get the templates
    Templates templates = config.getTemplates();

    // Initiate parameters
    Map<String, String> parameters = new HashMap<String, String>();
    // add parameters from the ANT task
    for (Param p : params) {
      parameters.put(p.getName(), p.getValue());
    }
    // add built in
    parameters.put("_rowdoctype",   config.getRowDoctype());
    parameters.put("_sheetdoctype", config.getSheetDoctype());
    parameters.put("_bookdoctype",  config.getBookDoctype());
    parameters.put("_output",       output.toURI().toString());

    // Transform Workbook / WorkSheet
    for (File itf : interim.listFiles(XML.getFileFilter())) {
      File target = new File(output, itf.getName());
      XSLT.transform(itf, target, templates, parameters);
    }

    // Transform Rows
    for (File sheet : interim.listFiles(DIR_FILTER)) {
      File targetFolder = new File(output, sheet.getName());
      if (!targetFolder.exists()) targetFolder.mkdir();
      for (File itf : sheet.listFiles(XML.getFileFilter())) {
        File target = new File(targetFolder, itf.getName());
        XSLT.transform(itf, target, templates, parameters);
      }
    }

  }

  // Helpers
  // ----------------------------------------------------------------------------------------------

  /**
   * @return the default working folder.
   */
  private static File getDefaultWorkingFolder() {
    String tmp = "psantpptx-"+System.currentTimeMillis();
    return new File(System.getProperty("java.io.tmpdir"), tmp);
  }
  
  // Parameter inner class
  // ----------------------------------------------------------------------------------------------

}

package org.pageseeder.xlsx.ox.step;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pageseeder.ox.OXConfig;
import org.pageseeder.ox.api.Result;
import org.pageseeder.ox.step.StepSimulator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImportTest {
  private static final String DEFAULT_OUTPUT_ROOT_FOLDER = "results";
  private static final File XLSX_UPLOAD_FILE = new File("src/test/resources/org/pageseeder/xlsx/ox/step/import/sample.xlsx");
  private static final String XLSX_PATH_FILE = "copy.xsl";

  private static final File CASES = new File("src/test/import/cases");

  @Before
  public void init() {
    File modelDir = new File("src/test/resources/org/pageseeder/xlsx/ox/step/import/models");
    OXConfig.get().setModelsDirectory(modelDir);
  }

  @Test
  public void testDefaultConfiguration() throws IOException, SAXException {
    testIndividual("default");
      /*
         File contentResult = new File(RESULTS,folderName);
         contentResult.mkdirs();
      */
  }

  public void testIndividual(String folderName) throws IOException, SAXException {
    testIndividual(new File(CASES, folderName), folderName);
  }

  public void testIndividual(File dir, String folderName) throws IOException, SAXException {
    if (dir.isDirectory()) {

      File actual = process(dir, folderName);
/*        if (new File(dir, dir.getName() + ".docx").exists()) {
          System.out.println(dir.getName());
          File result = new File(RESULTS, dir.getName());
          result.mkdirs();
          File actual = process(dir, result);
          File expected = new File(dir, "expected.psml");

          // Check that the files exist
          Assert.assertTrue(actual.exists());
          Assert.assertTrue(expected.exists());

          Assert.assertTrue(actual.length() > 0);
          Assert.assertTrue(expected.length() > 0);
          assertXMLEqual(expected, actual, result);*/
      System.out.println("Stop the results...");
    } else {
      System.out.println("Unable to find files for test:" + dir.getName());
    }
  }

  private File process(File test, String folderName) {
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/" + folderName;

    String modelName = "default";

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);

    String pathFolder = simulator.getData().directory().getAbsolutePath() + "/" + output;
    File resultFolder = new File(pathFolder);

    Result result = simulator.process(new Import(), null, output, "import-XLSX", null);

    return resultFolder;
  }

  @Test
  public void testInterimSplitWorkbookHeaderTrue(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "workbook");
    stepParameter.put("headers", "true");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
    System.out.println("Stop for tests");

  }

  @Test
  public void testInterimSplitWorkbookHeaderFalse(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook-header-false";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "workbook");
    stepParameter.put("headers", "false");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowHeaderTrue(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "true");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitWorksheetHeaderFalse(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-false";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "false");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitWorksheetHeaderTrue(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-worksheet-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "worksheet");
    stepParameter.put("headers", "true");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowFilenameColumnHeaderTrue(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "true");
    stepParameter.put("file-name-column", "3");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowFilenameColumnRichtextTrueHeaderTrue(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("richtext","true");
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "true");
    stepParameter.put("file-name-column", "3");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowFilenameColumnRichtextTrueHeaderTrueWorkingdir(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-true";
    String modelName = "default";

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("richtext","true");
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "true");
    stepParameter.put("file-name-column", "3");
    stepParameter.put("working-dir", "working_dir_example");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, null);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowFilenameColumnRichtextTrueHeaderTrueWorkingdirTransformXSLTDocument(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-header-true";
    String XSLT_FILE =  "copy.xsl";

    String modelName = "default";

    Map<String,String> requestParameters = new HashMap<>();
    requestParameters.put("_xslt-field-example","field-example");

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
/*      stepParameter.put("richtext","true");
      stepParameter.put("headers", "true");
      stepParameter.put("file-name-column", "3");*/
    stepParameter.put("split-level", "row");
    stepParameter.put("working-dir", "working_dir_example");
    stepParameter.put("_xslt-template-root", "template-root_example");
    stepParameter.put("_xslt-template2-root", "template-root_example2");
    /* Use xslt to transformation using a personalised template */
    /* Example copy content and ignore the default parameters. */
    /*    stepParameter.put("transform", XLSX_PATH_FILE);*/

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, requestParameters);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  @Test
  public void testInterimSplitRowFilenameColumnRichtextTrueHeaderTrueWorkingXSLTParam(){
    String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row-filename-column-richtext-true-header-true-working-dir-xsl-param";
    String XSLT_FILE =  "copy.xsl";

    String modelName = "default";

    Map<String,String> requestParameters = new HashMap<>();
    requestParameters.put("xslt","copy.xsl");

    /* Add front parameters */
    Map<String, String> stepParameter = new HashMap<>();
    stepParameter.put("richtext","true");
    stepParameter.put("split-level", "row");
    stepParameter.put("headers", "true");
    stepParameter.put("file-name-column", "3");
    stepParameter.put("working-dir", "working_dir_example");

    StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, requestParameters);
    Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
  }

  /* Limite testing - Top tests it is tested. */
  /*
   *
   *
   *
   * */

/*
    public void testIndividual(String folderName) throws IOException, SAXException {
      testIndividual(new File(CASES, folderName));
    }

    public void testIndividual(File dir) throws IOException, SAXException {
      if (dir.isDirectory()) {

        if (new File(dir, dir.getName() + ".docx").exists()) {
          System.out.println(dir.getName());
          File result = new File(RESULTS, dir.getName());
          result.mkdirs();
          File actual = process(dir, result);
          File expected = new File(dir, "expected.psml");

          // Check that the files exist
          Assert.assertTrue(actual.exists());
          Assert.assertTrue(expected.exists());

          Assert.assertTrue(actual.length() > 0);
          Assert.assertTrue(expected.length() > 0);
          assertXMLEqual(expected, actual, result);
        } else {
          System.out.println("Unable to find DOCX file for test:" + dir.getName());
        }
      }
    }
*/


/*

builder.working(new File(output, "working"));
    @Test
    public void testInterim_SplitRow_01(){
      String output = DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row";
      String modelName = "default";
      Map<String, String> requestParameter = new HashMap<>();
 */
/*     requestParameter.put("header-row", "1");
      requestParameter.put("values-row", "2");
      requestParameter.put("sheet-name", "'Sheet2'");*//*

      Map<String, String> stepParameter = new HashMap<>();
*/
/*      stepParameter.put("templates-root", "copy.xsl");
      stepParameter.put("richtext", "false");
      stepParameter.put("split-level", "row");
      stepParameter.put("headers","true");
      stepParameter.put("file-name-column","1");*//*

   */
/*        stepParameter.put("working-dir","");
        stepParameter.put("transform","");*//*


      StepSimulator simulator = new StepSimulator(modelName, this.XLSX_UPLOAD_FILE, requestParameter);
      Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
     // System.out.println("Content: " + result);

      */
  /*  Assert.assertNotNull(result);*//*

   */
/*        XMLStringWriter writer = new XMLStringWriter(XML.NamespaceAware.No);
        result.toXML(writer);
        System.out.println(writer.toString());
        Assert.assertEquals("OK", result.status().name());*//*

    }

*/
}

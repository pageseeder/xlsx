package org.pageseeder.xlsx;

import org.junit.Assert;
import org.junit.Test;
import org.pageseeder.xlsx.config.SplitLevel;
import org.pageseeder.xlsx.config.TransformConfig;
import org.pageseeder.xlsx.config.TransformConfigBuilder;

import java.io.File;
import java.io.IOException;

public class TransformProcessorTest {
  private static final String DEFAULT_OUTPUT_ROOT_FOLDER = "/build/output/core";

  @Test
  public void testInterim_SplitRow(){
    File input = new File("src/test/resources/org/pageseeder/xlsx/core/sample.xlsx");
    File output = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-row");
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.headers(true);
    builder.transform(new File("src/test/resources/org/pageseeder/xlsx/core/copy.xsl"));
    builder.destination(output);
    builder.filenameColumn(3);
    builder.level(SplitLevel.row);
//    builder.setRowDoctype("row-doc-type");
//    builder.setSheetDoctype("sheet-doc-type");
    builder.working(new File(output, "working"));
    TransformConfig config = builder.build();
    TransformProcessor processor = new TransformProcessor(config);
    try {
      processor.process();
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    System.out.println(output.getAbsolutePath());
  }

  @Test
  public void testInterim_SplitSheet(){
    File input = new File("src/test/resources/org/pageseeder/xlsx/core/sample.xlsx");
    File output = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-sheet");
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.headers(true);
    builder.transform(new File("src/test/resources/org/pageseeder/xlsx/core/copy.xsl"));
    builder.destination(output);
    builder.filenameColumn(3);
    builder.level(SplitLevel.worksheet);
//    builder.setRowDoctype("row-doc-type");
//    builder.setSheetDoctype("sheet-doc-type");
    builder.working(new File(output, "working"));
    TransformConfig config = builder.build();
    TransformProcessor processor = new TransformProcessor(config);
    try {
      processor.process();
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    System.out.println(output.getAbsolutePath());
  }

  @Test
  public void testInterim_SplitWorkbook(){
    File input = new File("src/test/resources/org/pageseeder/xlsx/core/sample.xlsx");
    File output = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook");
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.headers(true);
    builder.transform(new File("src/test/resources/org/pageseeder/xlsx/core/copy.xsl"));
    builder.destination(output);
    builder.filenameColumn(3);
    builder.level(SplitLevel.workbook);
//    builder.setRowDoctype("row-doc-type");
//    builder.setSheetDoctype("sheet-doc-type");
    builder.working(new File(output, "working"));
    TransformConfig config = builder.build();
    TransformProcessor processor = new TransformProcessor(config);
    try {
      processor.process();
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    System.out.println(output.getAbsolutePath());
  }

//  @Test
//  public void testInterim_SplitWorkbookRichText(){
//    File input = new File("src/test/resources/org/pageseeder/xlsx/core/sample.xlsx");
//    File output = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook-richtext");
//    TransformConfigBuilder builder = new TransformConfigBuilder();
//    builder.input(input);
//    builder.headers(true);
//    builder.transform(new File("src/test/resources/org/pageseeder/xlsx/core/copy.xsl"));
//    builder.destination(output);
//    builder.filenameColumn(3);
//    builder.level(SplitLevel.workbook);
//    builder.richtext(true);
////    builder.setRowDoctype("row-doc-type");
////    builder.setSheetDoctype("sheet-doc-type");
//    builder.working(new File(output, "working"));
//    TransformConfig config = builder.build();
//    TransformProcessor processor = new TransformProcessor(config);
//    try {
//      processor.process();
//    } catch (IOException e) {
//      Assert.fail(e.getMessage());
//    }
//    System.out.println(output.getAbsolutePath());
//  }

}

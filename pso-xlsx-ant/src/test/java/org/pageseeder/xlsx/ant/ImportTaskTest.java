package org.pageseeder.xlsx.ant;

import org.junit.Assert;
import org.junit.Test;
import org.pageseeder.xlsx.config.SplitLevel;

import java.io.File;

public class ImportTaskTest {
  private static final String DEFAULT_OUTPUT_ROOT_FOLDER = "/build/output";
  @Test
  public void testInterim() {
    File input = new File("src/test/resources/org/pageseeder/xlsx/ant/sample.xlsx");
    File output = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim");
    ImportTask task = new ImportTask();

    System.out.println(output.getAbsolutePath());

    task.setSrc(input);
    task.setHeaders(true);
    task.setTemplates(new File("src/test/resources/org/pageseeder/xlsx/ant/copy.xsl"));
    task.setDest(output);
    task.setFilenameColumn(3);
    task.setSplitLevel(SplitLevel.row);
    task.setWorkbookDoctype("Workbook");
    task.setRowDoctype("row-doc-type");
    task.setSheetDoctype("sheet-doc-type");
    task.setWorking(new File(output, "working"));

/*    task.setBookDoctype();*/
    task.execute();


  }
}

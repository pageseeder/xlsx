package org.pageseeder.xlsx.ant;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ImportTaskTest {

  @Test
  public void testProcess() {
    File input = new File("d:/sample.xlsx");
    ImportTask task = new ImportTask();
   /* Assert.assertTrue(input.exists());
    Assert.assertTrue(input.isFile());
    Assert.assertFalse(input.isDirectory());*/

    task.setSrc(input);
    task.setHeaders(true);
    task.setTemplates(new File("d:/workbook-to-curriculum-codes.xsl"));
/*    task.setBookDoctype();*/
    task.execute();


  }
}

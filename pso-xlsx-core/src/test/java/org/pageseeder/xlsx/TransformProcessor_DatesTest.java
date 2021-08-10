package org.pageseeder.xlsx;

import org.junit.Assert;
import org.junit.Test;
import org.pageseeder.xlsx.config.SplitLevel;
import org.pageseeder.xlsx.config.TransformConfig;
import org.pageseeder.xlsx.config.TransformConfigBuilder;
import org.pageseeder.xlsx.util.FileUtils;

import java.io.File;
import java.io.IOException;
import static org.xmlunit.assertj3.XmlAssert.assertThat;

public class TransformProcessor_DatesTest {
  private static final String DEFAULT_OUTPUT_ROOT_FOLDER = "/build/output/core/dates";
  private static final File XSLT_COPY = new File("src/test/resources/org/pageseeder/xlsx/core/copy.xsl");

  @Test
  public void testInterim_SplitWorkbook_RichtextTrue(){
    File input = new File("src/test/resources/org/pageseeder/xlsx/core/dates/dates-sample.xlsx");
    System.out.println(input.getAbsolutePath());
    File outputFolder = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook");
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.richtext(true);
    builder.headers(true);
    builder.xslt(XSLT_COPY);
    builder.destination(outputFolder);
    builder.filenameColumn(4);
    builder.level(SplitLevel.workbook);
    builder.working(new File(outputFolder, "working"));
    TransformConfig config = builder.build();
    TransformProcessor processor = new TransformProcessor(config);
    try {
      processor.process();
      File interimOutput = new File(outputFolder, "workbook.xml");
      String interimXML = FileUtils.read(interimOutput, "UTF-8");

      //Default 14
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='A']/text()").isEqualTo("Default 14");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='B']/text()").isEqualTo("1900/02/01");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='C']/text()").isEqualTo("1900/02/28");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='D']/text()").isEqualTo("2021/02/01");

      //DD/MM/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='A']/text()").isEqualTo("DD/MM/YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='B']/text()").isEqualTo("1900/02/01");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='C']/text()").isEqualTo("1900/02/28");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='D']/text()").isEqualTo("2021/02/01");

      //M/D/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='A']/text()").isEqualTo("M/D/YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='B']/text()").isEqualTo("1900/02/01");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='C']/text()").isEqualTo("1900/02/28");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='D']/text()").isEqualTo("2021/02/01");

      //YYYY/MM/DD
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='A']/text()").isEqualTo("YYYY/MM/DD");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='B']/text()").isEqualTo("1900/02/01");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='C']/text()").isEqualTo("1900/02/28");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='D']/text()").isEqualTo("2021/02/01");

      //Empty DD/MM/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='6']/col[@ref='A']/text()").isEqualTo("Empty");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='6']/col[@ref='B']/text()").isEqualTo("");

      //D/M/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='7']/col[@ref='A']/text()").isEqualTo("D/M/YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='7']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //D/MM/YY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='8']/col[@ref='A']/text()").isEqualTo("D/MM/YY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='8']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //D/M/YY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='9']/col[@ref='A']/text()").isEqualTo("D/M/YY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='9']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //DD/MM/YY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='10']/col[@ref='A']/text()").isEqualTo("DD/MM/YY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='10']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //DD-MON-YY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='11']/col[@ref='A']/text()").isEqualTo("DD-MON-YY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='11']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //DD-MONTH-YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='12']/col[@ref='A']/text()").isEqualTo("DD-MONTH-YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='12']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //D Month YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='13']/col[@ref='A']/text()").isEqualTo("D Month YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='13']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //MM/DD/YY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='14']/col[@ref='A']/text()").isEqualTo("MM/DD/YY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='14']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //YYYY-MM-DD
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='15']/col[@ref='A']/text()").isEqualTo("YYYY-MM-DD");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='15']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //YY-MM-DD
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='16']/col[@ref='A']/text()").isEqualTo("YY-MM-DD");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='16']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //YYYY/MM/DD
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='17']/col[@ref='A']/text()").isEqualTo("YYYY/MM/DD");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='17']/col[@ref='B']/text()").isEqualTo("1900/02/01");

      //D/M/YY hh:mm AM/PM
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='18']/col[@ref='A']/text()").isEqualTo("D/M/YY hh:mm AM/PM");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='18']/col[@ref='B']/text()").isEqualTo("1900/01/10 01:00:00");

      //D/M/YY hh:mm
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='19']/col[@ref='A']/text()").isEqualTo("D/M/YY hh:mm");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='19']/col[@ref='B']/text()").isEqualTo("2021/07/26 00:00:01");

      //hh:mm:ss AM/PM
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='20']/col[@ref='A']/text()").isEqualTo("hh:mm:ss AM/PM");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='20']/col[@ref='B']/text()").isEqualTo("00:01:00");

      //h:mm:ss
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='21']/col[@ref='A']/text()").isEqualTo("h:mm:ss");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='21']/col[@ref='B']/text()").isEqualTo("01:01:01");

      //hh:mm:ss
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='22']/col[@ref='A']/text()").isEqualTo("hh:mm:ss");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='22']/col[@ref='B']/text()").isEqualTo("11:39:00");

      //String formatted as date
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='23']/col[@ref='A']/text()").isEqualTo("String formatted as date");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='23']/col[@ref='B']/text()").isEqualTo("I am a string that wants to be a date");

    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    System.out.println(outputFolder.getAbsolutePath());
  }

  @Test
  public void testInterim_SplitWorkbook_RichtextFalse(){
    File input = new File("src/test/resources/org/pageseeder/xlsx/core/dates/dates-sample_2_hour.xlsx");
    System.out.println(input.getAbsolutePath());
    File outputFolder = new File( DEFAULT_OUTPUT_ROOT_FOLDER + "/interim-workbook-richtext-false");
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.richtext(false);
    builder.headers(true);
    builder.xslt(XSLT_COPY);
    builder.destination(outputFolder);
    builder.filenameColumn(4);
    builder.level(SplitLevel.workbook);
    builder.working(new File(outputFolder, "working"));
    TransformConfig config = builder.build();
    TransformProcessor processor = new TransformProcessor(config);
    try {
      processor.process();
      File interimOutput = new File(outputFolder, "workbook.xml");
      String interimXML = FileUtils.read(interimOutput, "UTF-8");

      //Default 14
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='A']/text()").isEqualTo("Default 14");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='B']/text()").isEqualTo("01/02/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='C']/text()").isEqualTo("28/02/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='2']/col[@ref='D']/text()").isEqualTo("01/02/2021");

      //DD/MM/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='A']/text()").isEqualTo("DD/MM/YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='B']/text()").isEqualTo("01/02/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='C']/text()").isEqualTo("28/02/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='3']/col[@ref='D']/text()").isEqualTo("01/02/2021");

      //M/D/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='A']/text()").isEqualTo("M/D/YYYY");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='B']/text()").isEqualTo("2/1/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='C']/text()").isEqualTo("2/28/1900");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='4']/col[@ref='D']/text()").isEqualTo("2/1/2021");

      //YYYY/MM/DD
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='A']/text()").isEqualTo("YYYY/MM/DD");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='B']/text()").isEqualTo("1900/02/01");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='C']/text()").isEqualTo("1900/02/28");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='5']/col[@ref='D']/text()").isEqualTo("2021/02/01");


      //Empty DD/MM/YYYY
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='6']/col[@ref='A']/text()").isEqualTo("Empty");
      assertThat(interimXML).valueByXPath("/workbook/worksheet/row[@position='6']/col[@ref='B']/text()").isEqualTo("");

    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    System.out.println(outputFolder.getAbsolutePath());
  }

}

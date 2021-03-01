package org.pageseeder.xlsx.ox.step;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pageseeder.ox.OXConfig;
import org.pageseeder.ox.api.Result;
import org.pageseeder.ox.core.Model;
import org.pageseeder.ox.core.PackageData;
import org.pageseeder.ox.core.ResultStatus;
import org.pageseeder.ox.core.StepInfoImpl;
import org.pageseeder.ox.step.NOPStep;
import org.pageseeder.ox.step.StepSimulator;
import org.pageseeder.ox.step.Transformation;
import org.pageseeder.ox.util.FileUtils;
import org.pageseeder.ox.util.ZipUtils;
import org.pageseeder.ox.xml.utils.XMLComparator;
import org.pageseeder.xmlwriter.XML;
import org.pageseeder.xmlwriter.XMLStringWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportTest {
  private static final File XLSX = new File("src/test/resources/org/pageseeder/xlsx/ox/step/import/sample.xlsx");
/*    String input = "src/test/resources/org/pageseeder/xlsx/ox/step/import/test.xlsx";
    File inputFile = new File(input);
    String output = "src/test/resources/org/pageseeder/xlsx/ox/step/import/output";*/

  @Before
  public void init() {
    File modelDir = new File("src/test/resources/org/pageseeder/xlsx/ox/step/import/models");
    OXConfig.get().setModelsDirectory(modelDir);
  }

//  @Test
//  public void test_process() throws IOException {
//    File source = new File("src/test/resources/org/pageseeder/xslx/ox/step/import/sample.xlsx");
//    File targetExpected = new File("src/test/resources/org/pageseeder/xslx/ox/step/import/output");
//    File resultExpected = new File("src/test/resources/org/pageseeder/xslx/ox/step/import/result/result.xml");
//    String outputName = "/output";
//
//    Model model = new Model("common");
//    PackageData data = PackageData.newPackageData("Import", source);
//    Map<String, String> params = new HashMap<>();
//    params.put("input", "sample.xml");
//    params.put("output", outputName);
//    params.put("xsl", "workbook-to-curriculum-codes.xsl");
//    params.put("header-row", "1");
//    params.put("values-row", "2");
//    params.put("sheet-name", "'Sheet1'");
//    params.put("templates-root", "");
//    StepInfoImpl info = new StepInfoImpl("step-id", "step name", "", "sample.xml", params);
//
//    Transformation step = new Transformation();
//    Result result = step.process(model, data, info);
//    XMLStringWriter xmlWriter = new XMLStringWriter(XML.NamespaceAware.No);
//    result.toXML(xmlWriter);
//
//    xmlWriter.flush();
//    xmlWriter.close();
//    Assert.assertEquals(ResultStatus.OK, result.status());
//    File targetCreated = data.getFile(outputName);
//    String resultXML = xmlWriter.toString();
//
//
//    Assert.assertEquals(ResultStatus.OK, result.status());
//    List<String> attributesToIgnore = Arrays.asList("id", "time");
//    validateResult(targetExpected, resultExpected, targetCreated, resultXML, attributesToIgnore);
//  }

    @Test
    public void process(){

      try {
        String input = "test.xlsx";
        File inputFile = new File(input);

        //String input = "src/test/resources/org/pageseeder/xlsx/ox/step/import/test.xlsx";
        String output = "output";
        String modelName = "default";
        Map<String, String> requestParameter = new HashMap<>();
        requestParameter.put("header-row", "1");
        requestParameter.put("values-row", "2");
        requestParameter.put("sheet-name", "'Sheet1'");
        Map<String, String> stepParameter = new HashMap<>();
        stepParameter.put("templates-root", "workbook-to-curriculum-codes.xsl");
        StepSimulator simulator = new StepSimulator(modelName, this.XLSX, requestParameter);
        Result result = simulator.process(new Import(), null, output, "import-XLSX", stepParameter);
        Assert.assertNotNull(result);
        XMLStringWriter writer = new XMLStringWriter(XML.NamespaceAware.No);
        result.toXML(writer);
        System.out.println(writer.toString());
        Assert.assertEquals("OK", result.status().name());
      } catch (IOException e) {
        Assert.fail(e.getMessage());
      }


    }

  private void validateResult(File targetExpected, File resultExpected, File targetCreated, String resultXML, List<String> attributesToIgnore) throws IOException {
    String resultXMLExpected = FileUtils.read(resultExpected);
    System.out.println(resultXML);

    final boolean isOutputZip = FileUtils.isZip(targetCreated);
    //Validate Output
    if (!isOutputZip) {
      XMLComparator.compareXMLFile(targetCreated, targetExpected);
    } else {
      File outputFolder = new File(targetCreated.getParentFile(), "test");
      outputFolder.mkdir();
      ZipUtils.unzip(targetCreated, outputFolder);
      for (File created:outputFolder.listFiles()) {
        File expected = new File(targetExpected, created.getName());
        Assert.assertNotNull("Target expected is null for " + (targetExpected.getAbsolutePath() + "/" + created.getName()), expected);
        System.out.println(created.getAbsolutePath());
        System.out.println(expected.getAbsolutePath());
        XMLComparator.compareXMLFile(created, expected);
      }
    }

    //Validate Result
    XMLComparator.isSimilar(resultXMLExpected, resultXML, attributesToIgnore);
  }
}

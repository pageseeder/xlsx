package org.pageseeder.xlsx.ox.step;

import org.pageseeder.ox.api.Result;
import org.pageseeder.ox.api.Step;
import org.pageseeder.ox.api.StepInfo;
import org.pageseeder.ox.core.Model;
import org.pageseeder.ox.core.PackageData;
import org.pageseeder.ox.tool.DefaultResult;
import org.pageseeder.ox.util.FileUtils;
import org.pageseeder.ox.util.StepUtils;
import org.pageseeder.ox.util.StringUtils;
import org.pageseeder.xlsx.TransformProcessor;
import org.pageseeder.xlsx.config.SplitLevel;
import org.pageseeder.xlsx.config.TransformConfig;
import org.pageseeder.xlsx.config.TransformConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 * <h3>Step Parameters</h3>
 * <ul>
 *  <li><var>input</var> .</li>
 *  <li><var>output</var> .</li>
 *  <li><var>richtext</var> .</li>
 *  <li><var>split-level</var> .</li>
 *  <li><var>headers</var> .</li>
 *  <li><var>file-name-column</var> .</li>
 *  <li><var>working-dir</var> .</li>
 *  <li><var>xslt</var> .</li>
 * </ul>
 *
 *
 */
public class Import implements Step {

  private static Logger LOGGER = LoggerFactory.getLogger(Import.class);

  @Override
  public Result process(Model model, PackageData data, StepInfo info) {
    DefaultResult result = null;
    File input = StepUtils.getInput(data, info);
    File output = StepUtils.getOutput(data, info, input);

    try {
      TransformConfig config = getConfig(model,data,info,input,output);
      StringWriter writer = new StringWriter();
      TransformProcessor processor = new TransformProcessor(config, writer);
      processor.process();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  private TransformConfig getConfig(Model model, PackageData data, StepInfo info, File input, File output) {

    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.input(input);
    builder.destination(output);
    builder.richtext("true".equalsIgnoreCase(StepUtils.getParameter(data, info, "richtext", "false")));
    builder.level(SplitLevel.valueOf(StepUtils.getParameter(data, info, "split-level", "workbook")));
    builder.headers("true".equalsIgnoreCase(StepUtils.getParameter(data,info, "headers", "false")));
    builder.filenameColumn(StepUtils.getParameterInt(data,info,"file-name-column",0));

    String workingDirectoryParameter = StepUtils.getParameter(data,info,"working-dir", "");
    File working = null;
    if (!StringUtils.isBlank(workingDirectoryParameter)) {
      working = data.getFile(workingDirectoryParameter);
    }
    builder.working(working);

    //## Handle the transformation file
    File xslt = getXSLFile(input.getParentFile(), model, data, info);
    builder.xslt(xslt);

    return builder.build();
  }

  /**
   * Gets the XSL file.
   *
   * @param unzipedFolder the unziped folder
   * @param model the model
   * @param data  the data
   * @param info  the info
   * @return the XSL file
   */
  private File getXSLFile(File unzipedFolder, Model model, PackageData data, StepInfo info) {
    String xslParameter = StepUtils.getParameter(data, info, "xslt", "");
    File xsl = null;
    if (!StringUtils.isBlank(xslParameter)) {
      xsl = model.getFile(xslParameter);
      if (xsl == null || !xsl.exists()) {
        xsl = data.getFile(xslParameter);
      }
    }
    return xsl;
  }
}

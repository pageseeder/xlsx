package org.pageseeder.xlsx.ox.step;

import org.pageseeder.ox.api.Result;
import org.pageseeder.ox.api.Step;
import org.pageseeder.ox.api.StepInfo;
import org.pageseeder.ox.core.Model;
import org.pageseeder.ox.core.PackageData;
import org.pageseeder.ox.tool.DefaultResult;
import org.pageseeder.ox.tool.InvalidResult;
import org.pageseeder.ox.util.FileUtils;
import org.pageseeder.ox.util.StepUtils;
import org.pageseeder.ox.util.StringUtils;
import org.pageseeder.xmlwriter.XML;
import org.pageseeder.xmlwriter.XMLStringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Import implements Step {

  private static Logger LOGGER = LoggerFactory.getLogger(Import.class);

  @Override
  public Result process(Model model, PackageData data, StepInfo info) {
    DefaultResult result = null;
    File input = StepUtils.getInput(data, info);
    File output = StepUtils.getOutput(data, info, input);

    /* Default: all recursive folders and files. If it is added you can use a glob pattern. */
    String headerRowParam = info.getParameter("header-row", "1");
    String valuesRowParam = info.getParameter("values-row", "2");
    String sheetNameParam = info.getParameter("sheet-name", "'Sheet1'");

    //## Handle the transformation file
    File xsl = getXSLFile(input.getParentFile(), model, data, info);

    // throw the error
    if (xsl == null || !xsl.exists()) {
      return new InvalidResult(model, data).error(new FileNotFoundException("Cannot find the stylesheet file."));
    }

    /* Valid input : a document */
    if (input.getName() != null){
      LOGGER.info(input.getName());

        result = new DefaultResult(model, data, info, output);
        boolean isInputValid = input != null && input.exists();
        boolean isOutputValid = output != null;
        LOGGER.info("Input file {} and output {}. Input valid = {}. Output valid = {}", input, output, isInputValid, isOutputValid);
        // only process when input is exists
        if (isInputValid && isOutputValid) {
          XMLStringWriter writer = new XMLStringWriter(XML.NamespaceAware.No);
          writer.openElement("files");

          /* Find files and print a element for each file found */
/*          FilesFinder finder = new FilesFinder(globParam, input);
          List<File> files = finder.getFiles();
          for (File inputFile:files) {
            writer.openElement("file");
            writer.attribute("path", inputFile.getAbsolutePath());
            writer.attribute("sort-path", data.getPath(inputFile));
            writer.closeElement();//file
          }*/

          writer.closeElement();//files
          writer.close();
          try {
            FileUtils.write(writer.toString(), output);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }


    }else {
      LOGGER.error("It is empty input. Please fill the correct input.");
    }
    return result;
  }

  /* Verify extension document */
  private static String getFileExtension(File file) {
    String fileName = file.getName();
    if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
      return fileName.substring(fileName.lastIndexOf(".")+1);
    else return "";
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
    String xslParameter = StepUtils.getParameter(data, info, "templates-root", "");
    File xsl = null;
    if (!StringUtils.isBlank(xslParameter)) {
      xsl = model.getFile(xslParameter);
      if (xsl == null || !xsl.exists()) {
        xsl = data.getFile(xslParameter);
      }
    } else if (unzipedFolder.isDirectory()) {
      List<String> extensions = Arrays.asList("xsl");
      FileFilter filter = FileUtils.filter(extensions, true);
      List<File> filesFound = FileUtils.findFiles(unzipedFolder, filter);
      if (!filesFound.isEmpty()) {
        xsl = filesFound.get(0);
      }
    }
    return xsl;
  }

}

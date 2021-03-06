/*
 * Copyright (c) 1999-2012 weborganic systems pty. ltd.
 */
package org.pageseeder.xlsx.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.pageseeder.xlsx.XLSXException;

/**
 * A utility class for common XSLT functions.
 *
 * @author Christophe Lauret
 * @version 12 April 2012
 */
public final class XSLT {

  /**
   * Maps XSLT templates to their URL as a string for easy retrieval.
   */
  private static final Map<String, Templates> CACHE = new Hashtable<String, Templates>();

  /** Utility class. */
  private XSLT() {
  }

  /**
   * Returns the XSLT templates at the specified URL.
   *
   * <p>Templates are cached internally.
   *
   * @param url A URL to a template.
   *
   * @return the corresponding XSLT templates object or <code>null</code> if the URL was <code>null</code>.
   *
   * @throws BuildException If XSLT templates could not be loaded from the specified URL.
   */
  public static Templates getTemplates(URL url) {
    if (url == null) return null;
    Templates templates = CACHE.get(url.toString());
    if (templates == null) {
      templates = toTemplates(url);
      CACHE.put(url.toString(), templates);
    }
    return templates;
  }

  /**
   * Return the XSLT templates from the given style.
   *
   * <p>This method will first try to load the resource using the class loader used for this class.
   *
   * <p>Use this class to load XSLT from the system.
   *
   * @param resource The path to a resource.
   *
   * @return the corresponding XSLT templates object;
   *         or <code>null</code> if the resource could not be found.
   *
   * @throws BuildException If the loading fails.
   */
  public static Templates getTemplatesFromResource(String resource) {
    ClassLoader loader = XSLT.class.getClassLoader();
    URL url = loader.getResource(resource);
    if (url == null)
      throw new XLSXException("Unable to find templates at "+resource);
    return getTemplates(url);
  }

  /**
   * Utility function to transforms the specified XML source and returns the results as XML.
   *
   * Problems will be reported in the logs, the output will simply produce results as a comment.
   *
   * @param source     The Source XML data.
   * @param result     The Result XHTML data.
   * @param templates  The XSLT templates to use.
   * @param parameters Parameters to transmit to the transformer for use by the stylesheet (optional)
   *
   * @throws BuildException For XSLT Transformation errors or XSLT configuration errors
   */
  public static void transform(File source, File result, Templates templates, Map<String, String> parameters) {
    try (InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(result)) {
      Source src = new StreamSource(new BufferedInputStream(in), source.toURI().toString());
      Result res = new StreamResult(new BufferedOutputStream(out));

      // Transform
      transform(src, res, templates, parameters);

    } catch (IOException ex) {
      throw new XLSXException(ex);
    }
  }

  /**
   * Utility function to transforms the specified XML source and returns the results as XML.
   *
   * Problems will be reported in the logs, the output will simply produce results as a comment.
   *
   * @param source     The Source XML data.
   * @param result     The Result data.
   * @param templates  The XSLT templates to use.
   * @param parameters Parameters to transmit to the transformer for use by the stylesheet (optional)
   *
   * @throws XLSXException For XSLT Transformation errors or XSLT configuration errors
   */
  public static void transform(Source source, Result result, Templates templates, Map<String, String> parameters) {
    try {
      // Create a transformer from the templates
      Transformer transformer = templates.newTransformer();

      // Transmit the properties to the transformer
      if (parameters != null) {

        for (Entry<String, String> e : parameters.entrySet()) {
          transformer.setParameter(e.getKey(), e.getValue());
        }
      }
      // Transform
      transformer.transform(source, result);

    } catch (TransformerException ex) {
      //Keeping the original error message
      String errorMessage = ex.getMessage();
      if (errorMessage == null || errorMessage.isEmpty()) {
        errorMessage = "Unable to transform ";
      }
      
      throw new XLSXException(errorMessage, ex);
    }
  }

  // private helpers
  // ----------------------------------------------------------------------------------------------

  /**
   * Return the XSLT templates from the given style.
   *
   * @param stylepath The path to the XSLT style sheet
   *
   * @return the corresponding XSLT templates object
   *
   * @throws XLSXException If the loading fails.
   */
  private static Templates toTemplates(File stylepath) {
    // load the templates from the source file
    Source source = new StreamSource(stylepath);
    TransformerFactory factory = TransformerFactory.newInstance();
    // TODO Ant listening
//    factory.setErrorListener(listener);
    try {
      return factory.newTemplates(source);
    } catch (TransformerConfigurationException ex) {
      throw new XLSXException("Unable to load XSLT templates", ex);
    }
  }

  /**
   * Return the XSLT templates from the given style.
   *
   * @param url A URL to a template.
   *
   * @return the corresponding XSLT templates object or <code>null</code> if the URL was <code>null</code>.
   *
   * @throws XLSXException If XSLT templates could not be loaded from the specified URL.
   */
  private static Templates toTemplates(URL url) {
    if (url == null) return null;
    // load the templates from the source URL
    Templates templates = null;
    try (InputStream in = url.openStream()) {
      Source source = new StreamSource(in);
      source.setSystemId(url.toString());
      TransformerFactory factory = TransformerFactory.newInstance();
      templates = factory.newTemplates(source);
    } catch (TransformerConfigurationException ex) {
      throw new XLSXException("Transformer exception while trying to load XSLT templates"+ url.toString(), ex);
    } catch (IOException ex) {
      throw new XLSXException("IO error while trying to load XSLT templates"+ url.toString(), ex);
    }
    return templates;
  }

}

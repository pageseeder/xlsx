package org.pageseeder.xlsx.config;

import org.junit.Assert;
import org.junit.Test;

public class TransformConfigBuilderTest {

  @Test
  public void headers() {
    TransformConfigBuilder builder = new TransformConfigBuilder();
    builder.headers(true);
    TransformConfig config = builder.build();
    Assert.assertTrue(config.hasHeaders());

    builder = new TransformConfigBuilder();
    builder.headers(false);
    config = builder.build();
    Assert.assertFalse(config.hasHeaders());
  }
}

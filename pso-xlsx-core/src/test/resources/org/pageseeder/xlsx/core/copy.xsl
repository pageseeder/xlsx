<?xml version="1.0"?>
<xsl:transform version="2.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:t="http://pageseeder.com/psml/template"
               exclude-result-prefixes="#all">

  <xsl:output indent="yes" method="xml" encoding="utf-8" />

<!--
  <xsl:param name="header-row"      select="1" />
  <xsl:param name="values-row"      select="2" />
  <xsl:param name="sheet-name"    select="'Sheet1'"/>
  <xsl:param name="templates-root"  select="'../../../../doctemplates/'"/>
  <xsl:param name="_output" select="''" />
-->

  <!-- Create the product document using the template for new document, then modifying it  -->
  <xsl:template match="/">
<!--    <xsl:apply-templates />-->
    <xsl:copy-of select="."/>
  </xsl:template>

<!--  <xsl:template match="*">-->
<!--    <xsl:copy>-->
<!--      <xsl:copy-of select="@*" />-->
<!--      <xsl:apply-templates select="*" />-->
<!--    </xsl:copy>-->
<!--  </xsl:template>-->

</xsl:transform>

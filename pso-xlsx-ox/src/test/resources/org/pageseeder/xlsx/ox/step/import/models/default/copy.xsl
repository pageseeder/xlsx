<?xml version="1.0"?>
<xsl:transform version="2.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               exclude-result-prefixes="#all">

    <xsl:output indent="yes" method="xml" encoding="utf-8" />

    <!-- Copy example to test the xlt template transformation-->
    <xsl:template match="/">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:transform>

<!-- 
  This is a default template to convert the interim format into PSXML.
  
  @author Christophe Lauret
  @version 16 April 2012
-->
<xsl:stylesheet version="2.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:f="http://ant.pageseeder.com/functions"
      exclude-result-prefixes="#all">

<!-- Document type for a row -->
<xsl:param name="_rowdoctype" />

<!-- Document type for a worksheet -->
<xsl:param name="_sheetdoctype" />

<!-- Document type for a workbook -->
<xsl:param name="_workbookdoctype" />

<!-- We produce XML in utf-8 -->
<xsl:output encoding="UTF-8" method="xml" indent="yes" />

<!-- ========================================================================================== -->
<!-- Split by Workbook                                                                          -->
<!-- ========================================================================================== -->

<!--
  1 Workbook = 1 Standard document with tables 
-->
<xsl:template match="workbook[worksheet/row]">
<xsl:sequence select="f:ps-pi($_workbookdoctype, @title)"/>
<root>
<xsl:if test="$_workbookdoctype != 'standard'">
  <properties>
     <property name="document-type"><xsl:value-of select="$_workbookdoctype"/></property>
  </properties>
</xsl:if>
  <section id="title">
    <body><heading1><xsl:value-of select="@title"/></heading1></body>
  </section>
  <xsl:for-each select="worksheet">
	  <section id="sheet{position()}">
	    <body>
	      <heading2><xsl:value-of select="@title"/></heading2>
	      <table>
	        <!-- Table Header -->
	        <xsl:for-each select="head">
		        <row>
		          <xsl:for-each select="col">
		            <hcell><xsl:value-of select="."/></hcell>
		          </xsl:for-each>
		        </row>
		      </xsl:for-each>
	        <!-- Rows -->
          <xsl:for-each select="row">
            <row>
              <xsl:for-each select="col">
                <cell><xsl:value-of select="."/></cell>
              </xsl:for-each>
            </row>
          </xsl:for-each>
	      </table>
	    </body>
	  </section>
	</xsl:for-each>
</root>
</xsl:template>

<!-- ========================================================================================== -->
<!-- Split by Worksheet                                                                          -->
<!-- ========================================================================================== -->

<!--
  1 Workbook = 1 Standard document with tables 
-->
<xsl:template match="worksheet[row/col]">
<xsl:sequence select="f:ps-pi($_sheetdoctype, @title)"/>
<root>
  <xsl:if test="$_sheetdoctype != 'standard'">
    <properties>
      <property name="document-type"><xsl:value-of select="$_sheetdoctype"/></property>
    </properties>
  </xsl:if>
  <section id="title">
    <body><heading1><xsl:value-of select="@title"/></heading1></body>
  </section>
  <section id="sheet">
    <body>
      <table>
        <!-- Table Header -->
        <xsl:for-each select="head">
          <row>
            <xsl:for-each select="col">
              <hcell><xsl:value-of select="."/></hcell>
            </xsl:for-each>
          </row>
        </xsl:for-each>
        <!-- Rows -->
        <xsl:for-each select="row">
          <row>
            <xsl:for-each select="col">
              <cell><xsl:value-of select="."/></cell>
            </xsl:for-each>
          </row>
        </xsl:for-each>
      </table>
    </body>
  </section>
</root>
</xsl:template>

<!-- ========================================================================================== -->
<!-- Split by Row                                                                               -->
<!-- ========================================================================================== -->

<!--
  1 Workbook/Worsheet = 1 Master document 
-->
<xsl:template match="workbook[worksheet/@href]
                   | worksheet[row/@href]">
<xsl:sequence select="f:ps-pi($_workbookdoctype, @title)"/>
<root>
  <xsl:if test="self::workbook and $_workbookdoctype != 'standard'">
    <properties>
      <property name="document-type"><xsl:value-of select="$_workbookdoctype"/></property>
    </properties>
  </xsl:if>
  <xsl:if test="self::worksheet and $_sheetdoctype != 'standard'">
    <properties>
      <property name="document-type"><xsl:value-of select="$_sheetdoctype"/></property>
    </properties>
  </xsl:if>
  <section id="title">
    <body><heading1><xsl:value-of select="@title"/></heading1></body>
  </section>
  <section id="links" format="psxreflist">
    <body>
      <xsl:variable name="title" select="@title"/>
      <xsl:for-each select="worksheet|row">
        <para><xref frag="default" display="document" type="None" reverselink="true" reversetitle="{$title}" reversetype="None" 
        href="{@href}"><xsl:value-of select="@title"/></xref></para>
      </xsl:for-each>
    </body>
  </section>
</root>
</xsl:template>

<!--
  1 Row = 1 Standard document 
-->
<xsl:template match="row">
<xsl:variable name="title" select="@title"/>
<xsl:sequence select="f:ps-pi($_rowdoctype, $title)"/>
<root>
	<xsl:if test="$_rowdoctype != 'standard'">
	  <properties>
      <property name="document-type"><xsl:value-of select="$_rowdoctype"/></property>
    </properties>
	</xsl:if>
  <section id="title">
	  <body><heading1><xsl:value-of select="$title"/></heading1></body>
	</section>
	<section id="content" format="pslabelvalues">
	  <body>
      <xsl:for-each select="col[@title]">
        <para><xsl:value-of select="@title"/>: <inlineLabel name="{f:to-label(@title)}"><xsl:value-of select="text()"/></inlineLabel></para>
      </xsl:for-each>
	  </body>
	</section>
</root>
</xsl:template>

<!-- ========================================================================================== -->
<!-- FUNCTIONS                                                                                  -->
<!-- ========================================================================================== -->

<!-- 
  Generate the PageSeeder Processing instruction
 -->
<xsl:function name="f:ps-pi">
<xsl:param name="config" as="xs:string"/>
<xsl:param name="title"  as="xs:string"/>
<xsl:processing-instruction name="stylesheet">
  <xsl:text>format="standard" </xsl:text>
  <xsl:text>config="</xsl:text><xsl:value-of select="$config"/><xsl:text>" </xsl:text>
  <xsl:text>title="</xsl:text><xsl:value-of select="$title"/><xsl:text>"</xsl:text>
</xsl:processing-instruction>
</xsl:function>

<!-- 
  Converts the title into a valid PageSeeder label.
  
  @param title the title to convert. 
  @return the corresponding label
 -->
<xsl:function name="f:to-label">
<xsl:param name="title" as="xs:string"/>
<xsl:value-of select="lower-case(replace($title, '\W+', '_'))"/>
</xsl:function>

</xsl:stylesheet>

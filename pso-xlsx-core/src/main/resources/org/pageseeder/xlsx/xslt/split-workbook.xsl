<!--
  This template splits an Excel spreadsheet into a document per row so that it can be fed into a template.

  @author Christophe Lauret
  @version 16 April 2012
-->
<xsl:stylesheet version="2.0"
      xmlns:ss="http://schemas.openxmlformats.org/spreadsheetml/2006/main"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
      xmlns:rel="http://schemas.openxmlformats.org/package/2006/relationships"
      exclude-result-prefixes="#all">

<xsl:strip-space elements="*"/>

<!-- URI Relationships (.rels) document -->
<xsl:param name="_relationships" />

<!-- Styles -->
<xsl:param name="_styles" />

<!-- Output folder -->
<xsl:param name="_outputfolder" />

<!-- Splitlevel -->
<xsl:param name="_splitlevel" />

<!-- Title of the workbook -->
<xsl:param name="_booktitle" />

<!-- Can we use the first row as headers? -->
<xsl:param name="_hasheaders" select="'true'"/>

<!-- Which column to use as the filename? -->
<xsl:param name="_filenamecolumn" select="'0'"/>

<!-- We produce XML in utf-8 -->
<xsl:output encoding="UTF-8" method="xml"/>


<!-- Base URI folder (of input) -->
<xsl:variable name="base" select="string-join(tokenize(base-uri(), '/')[not(position() = last())], '/')"/>

<!-- Load Excel shared strings (some column values are stored there) -->
<xsl:variable name="sharedstrings" select="document(concat($base, '/sharedStrings.xml'))"/>

<!-- Relationships document -->
<xsl:variable name="relationships" select="document($_relationships)"/>

  <!-- Styles document -->
<xsl:variable name="styles" select="document($_styles)"/>


<!-- Process the workbook depending on the split level -->
<xsl:template match="ss:workbook">
  <xsl:choose>
    <xsl:when test="$_splitlevel = 'row'">
      <xsl:apply-templates select="." mode="row"/>
    </xsl:when>
    <xsl:when test="$_splitlevel = 'worksheet'">
      <xsl:apply-templates select="." mode="worksheet"/>
    </xsl:when>
    <xsl:when test="$_splitlevel = 'workbook'">
      <xsl:apply-templates select="." mode="workbook"/>
    </xsl:when>
  </xsl:choose>
</xsl:template>


<!-- ========================================================================================== -->
<!-- Split Level: WorkBook                                                                      -->
<!-- ========================================================================================== -->

<!--
  Process the workbook splitting at the 'row' level.
-->
<xsl:template match="ss:workbook" mode="workbook">
<xsl:call-template name="header-comment" />
<workbook title="{$_booktitle}">
  <xsl:for-each select="ss:sheets/ss:sheet">
    <xsl:sort select="@sheetId"/>
    <xsl:variable name="title"    select="@name"/>
    <xsl:variable name="ref"      select="@r:id"/>
    <xsl:variable name="filename" select="$relationships//rel:Relationship[@Id = $ref]/@Target"/>
    <xsl:apply-templates select="document(concat($base, '/', $filename))" mode="workbook">
      <xsl:with-param name="title" select="$title"/>
    </xsl:apply-templates>
  </xsl:for-each>
</workbook>
</xsl:template>

<!--
  Process the worksheet splitting at the 'row' level.

  @param title  the title of the worksheet
-->
<xsl:template match="ss:worksheet" mode="workbook">
<xsl:param name="title"/>
<xsl:choose>
	<xsl:when test="ss:sheetData/ss:row">
		<xsl:message>Processing worksheet <xsl:value-of select="$title"/></xsl:message>
		<worksheet title="{$title}">
		  <!-- Grab the titles from the first row -->
      <xsl:variable name="headers" select="ss:get-column-headers(.)"/>
		  <xsl:sequence select="$headers" />
		  <!-- Process each row -->
		  <xsl:for-each select="ss:sheetData/ss:row[if ($_hasheaders = 'true') then position() gt 1 else position() ge 1]">
		    <row position="{@r}">
		      <xsl:sequence select="ss:get-columns(., $headers)"/>
		    </row>
		  </xsl:for-each>
		</worksheet>
	</xsl:when>
	<xsl:otherwise>
	  <xsl:message>Skipping worksheet <xsl:value-of select="$title"/></xsl:message>
	</xsl:otherwise>
</xsl:choose>
</xsl:template>


<!-- ========================================================================================== -->
<!-- Split Level: Worksheet                                                                     -->
<!-- ========================================================================================== -->

<!--
  Process the workbook splitting at the 'row' level.
-->
<xsl:template match="ss:workbook" mode="worksheet">
<xsl:call-template name="header-comment" />
<workbook title="{$_booktitle}">
  <xsl:for-each select="ss:sheets/ss:sheet">
    <xsl:sort select="@sheetId"/>
    <xsl:variable name="title" select="@name"/>
    <xsl:variable name="ref"  select="@r:id"/>
    <xsl:variable name="filename" select="$relationships//rel:Relationship[@Id = $ref]/@Target"/>
    <xsl:variable name="worksheet" select="document(concat($base,'/', $filename))"/>
    <xsl:choose>
      <xsl:when test="$worksheet/ss:worksheet/ss:sheetData/ss:row">
        <xsl:message>Processing worksheet <xsl:value-of select="$title"/></xsl:message>
        <worksheet href="{ss:filename($filename)}" title="{$title}"/>
        <xsl:apply-templates select="$worksheet/ss:worksheet" mode="worksheet">
          <xsl:with-param name="title" select="$title"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message>Skipping worksheet <xsl:value-of select="$title"/></xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</workbook>
</xsl:template>

<!--
  Process the worksheet splitting at the 'row' level.

  @param title  the title of the worksheet
-->
<xsl:template match="ss:worksheet" mode="worksheet">
<xsl:param name="title"/>
<xsl:variable name="folder" select="substring-before(tokenize(base-uri(), '/')[last()], '.')"/>
<xsl:result-document href="{$_outputfolder}{$folder}.xml" method="xml" encoding="utf-8" indent="yes">
	<xsl:call-template name="header-comment"/>
	<worksheet title="{$title}" book-title="{$_booktitle}">
	  <!-- Grab the titles from the first row -->
	  <xsl:variable name="headers" select="ss:get-column-headers(.)"/>
	  <xsl:sequence select="$headers" />
	  <!-- Process each row -->
	  <xsl:for-each select="ss:sheetData/ss:row[if ($_hasheaders = 'true') then position() gt 1 else position() ge 1]">
	    <row position="{@r}">
	      <xsl:sequence select="ss:get-columns(., $headers)"/>
	    </row>
	  </xsl:for-each>
	</worksheet>
</xsl:result-document>
</xsl:template>


<!-- ========================================================================================== -->
<!-- Split Level: Row                                                                           -->
<!-- ========================================================================================== -->

<!--
  Process the workbook splitting at the 'row' level.
-->
<xsl:template match="ss:workbook" mode="row">
<xsl:call-template name="header-comment" />
<workbook title="{$_booktitle}">
  <xsl:for-each select="ss:sheets/ss:sheet">
    <xsl:sort select="@sheetId"/>
    <xsl:variable name="title" select="@name"/>
    <xsl:variable name="ref"  select="@r:id"/>
    <xsl:variable name="filename" select="$relationships//rel:Relationship[@Id = $ref]/@Target"/>
    <xsl:variable name="worksheet" select="document(concat($base,'/', $filename))"/>
		<xsl:choose>
		  <xsl:when test="$worksheet/ss:worksheet/ss:sheetData/ss:row">
		    <xsl:message>Processing worksheet <xsl:value-of select="$title"/></xsl:message>
		    <worksheet href="{ss:filename($filename)}" title="{$title}"/>
		    <xsl:apply-templates select="$worksheet/ss:worksheet" mode="row">
		      <xsl:with-param name="title" select="$title"/>
		    </xsl:apply-templates>
		  </xsl:when>
		  <xsl:otherwise>
		    <xsl:message>Skipping worksheet <xsl:value-of select="$title"/></xsl:message>
		  </xsl:otherwise>
    </xsl:choose>
  </xsl:for-each>
</workbook>
</xsl:template>

<!--
  Process the worksheet splitting at the 'row' level.
-->
<xsl:template match="ss:worksheet" mode="row">
<xsl:param name="title"/>
  <!-- The folder where the worksheet is stored -->
  <xsl:variable name="folder" select="substring-before(tokenize(base-uri(), '/')[last()], '.')"/>
  <xsl:result-document href="{$_outputfolder}{$folder}.xml" method="xml" encoding="utf-8" indent="yes">
    <xsl:call-template name="header-comment"/>
	  <worksheet title="{$title}">
		  <!-- Grab the titles from the first row -->
		  <xsl:variable name="headers" select="ss:get-column-headers(.)"/>
		  <xsl:sequence select="$headers" />
		  <xsl:variable name="fncol" select="xs:integer($_filenamecolumn)"/>
		  <!-- Process each row -->
		  <xsl:for-each select="ss:sheetData/ss:row[if ($_hasheaders = 'true') then position() gt 1 else position() ge 1]">
		    <xsl:variable name="filename" select="if ($fncol gt 0 and $fncol le count(ss:c))
		                                            then ss:get-value(ss:c[$fncol])
		                                            else format-number(position(), '0000')"/>
		    <row href="{$folder}/{$filename}.xml" title="{$filename}" />
		    <xsl:result-document href="{$_outputfolder}{$folder}/{$filename}.xml" method="xml" encoding="utf-8" indent="yes">
			    <xsl:call-template name="header-comment"/>
			    <row position="{@r}" title="{$filename}" sheet-title="{$title}" book-title="{$_booktitle}">
			      <xsl:sequence select="ss:get-columns(., $headers)"/>
			    </row>
		    </xsl:result-document>
		  </xsl:for-each>
	  </worksheet>
	</xsl:result-document>
</xsl:template>


<!-- ========================================================================================== -->
<!-- COMMON TEMPLATES                                                                           -->
<!-- ========================================================================================== -->

<!--
  Determine the column headers
-->
<xsl:template match="ss:worksheet" mode="column-headers">
  <xsl:sequence select="ss:get-column-headers(.)"/>
</xsl:template>

<!--
  Display a header comment.
-->
<xsl:template name="header-comment">
<xsl:comment>
  Generated by PageSeeder ANT Excel import Task on <xsl:value-of select="format-date(current-date(), '[D] [MNn] [Y]')" /> at <xsl:value-of select="format-time(current-time(), '[H]:[m]')" />.
</xsl:comment><xsl:text>
</xsl:text>
</xsl:template>


<!-- ========================================================================================== -->
<!-- FUNCTIONS                                                                                  -->
<!-- ========================================================================================== -->

<!--
  Retrieve the value from the shared strings

  @param c The Excel column data.
-->
<xsl:function name="ss:get-value">
  <xsl:param name="c"/>
  <xsl:variable name="cell-value" select="if ($c/@t = 's') then ss:get-share-string($c/ss:v) else $c/ss:v"/>
  <!-- @s has the style index in the cell-format(ss:cellXfs) element in styles.xml-->
  <xsl:value-of select="if ($c/@s) then ss:apply-cell-format($c/@s, $cell-value) else $cell-value"/>
</xsl:function>





<!--
  Retrieve the value from the shared strings

  @param c The Excel column data.
-->
<xsl:function name="ss:get-value-sharedstring">
  <xsl:param name="c"/>
  <xsl:variable name="cell-value" select="if ($c/@t = 's') then ss:get-share-string-richtext($c/ss:v) else $c/ss:v/text()"/>
  <!--
    @s has the style index in the cell-format(ss:cellXfs) element in styles.xml and it is not a shared string
  -->
  <xsl:value-of select="if ($c/@s and not ($c/@t = 's')) then ss:apply-cell-format($c/@s, $cell-value) else $cell-value"/>
</xsl:function>



<!--
  Retrieve the value from the shared strings

  @param i The shared string reference
-->
<xsl:function name="ss:get-share-string">
  <xsl:param name="i"/>
  <xsl:value-of select="string-join($sharedstrings//ss:si[$i + 1]//ss:t,'')"/>
</xsl:function>

<!--
  Retrieve the richtext from the shared strings

  @param i The shared string reference
-->
<xsl:function name="ss:get-share-string-richtext">
  <xsl:param name="i"/>
  <xsl:variable name="shared-string">
    <xsl:apply-templates select="$sharedstrings//ss:si[$i + 1]" mode="sharedstring-copy"/>
  </xsl:variable>
  <xsl:sequence select="$shared-string"/>
</xsl:function>

<xsl:template match="ss:si" mode="sharedstring-copy">
  <xsl:apply-templates select="ss:r | ss:t"  mode="sharedstring-copy"/>
</xsl:template>

<xsl:template match="ss:t" mode="sharedstring-copy">
  <xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="ss:r" mode="sharedstring-copy">
  <xsl:choose>
    <xsl:when test="ss:br | ss:t">
      <xsl:call-template name="apply-wr-style">
        <xsl:with-param name="text"      select="ss:br | ss:t" />
        <xsl:with-param name="bold"      select="if (ss:rPr/ss:b) then 'true' else 'false'" />
        <xsl:with-param name="italic"    select="if (ss:rPr/ss:i) then 'true' else 'false'" />
        <xsl:with-param name="underline" select="if (ss:rPr/ss:u) then 'true' else 'false'" />
      </xsl:call-template>
    </xsl:when>
  </xsl:choose>
</xsl:template>

<xsl:template name="apply-wr-style">
  <!-- This template processes inline elements recursively -->
  <xsl:param name="text" />
  <xsl:param name="bold" />
  <xsl:param name="italic" />
  <xsl:param name="underline" />
  <xsl:choose>
    <xsl:when test="$bold='true'">
      <bold>
        <xsl:call-template name="apply-wr-style">
          <xsl:with-param name="text" select="$text" />
          <xsl:with-param name="bold" select="'false'" />
          <xsl:with-param name="italic" select="$italic" />
          <xsl:with-param name="underline" select="$underline" />
        </xsl:call-template>
      </bold>
    </xsl:when>
    <xsl:when test="$italic='true'">
      <italic>
        <xsl:call-template name="apply-wr-style">
          <xsl:with-param name="text" select="$text" />
          <xsl:with-param name="bold" select="$bold" />
          <xsl:with-param name="italic" select="'false'" />
          <xsl:with-param name="underline" select="$underline" />
        </xsl:call-template>
      </italic>
    </xsl:when>
    <xsl:when test="$underline='true'">
      <underline>
        <xsl:call-template name="apply-wr-style">
          <xsl:with-param name="text" select="$text" />
          <xsl:with-param name="bold" select="$bold" />
          <xsl:with-param name="italic" select="$italic" />
          <xsl:with-param name="underline" select="'false'" />
        </xsl:call-template>
      </underline>
    </xsl:when>
    <xsl:otherwise>
      <xsl:for-each select="$text">
        <xsl:choose>
          <xsl:when test="self::ss:br"><br /></xsl:when>
          <xsl:otherwise><xsl:value-of select="." /></xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<!--
  Determine the column headers

  @param worksheet The worksheet to process
-->
<xsl:function name="ss:get-column-headers">
<xsl:param name="worksheet" as="element(ss:worksheet)"/>
<xsl:variable name="firstrow" select="$worksheet/ss:sheetData/ss:row[1]"/>
<head>
  <xsl:variable name="from" select="if ($firstrow/@spans) then xs:integer(substring-before($firstrow/@spans, ':')) else 1" as="xs:integer"/>
  <xsl:variable name="to"   select="if ($firstrow/@spans) then xs:integer(substring-after($firstrow/@spans, ':'))  else count($firstrow/col)" as="xs:integer"/>
  <xsl:for-each select="$from to $to">
    <xsl:variable name="colname" select="codepoints-to-string(64 + .)" />
    <xsl:variable name="col"     select="$firstrow/ss:c[starts-with(@r, $colname)]"/>
    <col ref="{$colname}">
		  <xsl:choose>
		    <xsl:when test="$_hasheaders = 'true' and $col/ss:v"><xsl:value-of select="ss:get-value($col)"/></xsl:when>
		    <xsl:otherwise>Column <xsl:value-of select="."/></xsl:otherwise>
		  </xsl:choose>
	  </col>
  </xsl:for-each>
</head>
</xsl:function>

<!--
  Determine the column headers

  @param row  The row being processed
  @param refs The column references
-->
<xsl:function name="ss:get-columns">
<xsl:param name="row"     as="element(ss:row)"/>
<xsl:param name="headers" as="element(head)"/>
  <xsl:for-each select="$headers//col">
    <xsl:variable name="ref" select="@ref"/>
    <xsl:variable name="col" select="$row/ss:c[starts-with(@r, $ref)]"/>
    <col ref="{@ref}">
      <xsl:if test="$_hasheaders = 'true' and $_splitlevel = 'row'">
        <xsl:variable name="p" select="position()"/>
        <xsl:attribute name="title" select="."/>
      </xsl:if>
      <xsl:if test="$col"><xsl:copy-of select="ss:get-value-sharedstring($col)"/></xsl:if>
    </col>
  </xsl:for-each>
</xsl:function>

<!--
  Returns the filename of the current URI being processed.
  @param node The context node
  @return the filename of the base URI.
-->
<xsl:function name="ss:filename">
<xsl:param name="path"/>
<xsl:value-of select="tokenize($path, '/')[last()]"/>
</xsl:function>

<!--
  Applied cell format according to the attribute ss:c/@s
  - Number Formatting
-->
<xsl:function name="ss:apply-cell-format">
  <xsl:param name="style-index" as="xs:integer"/>
  <xsl:param name="value" as="xs:string?"/>
  <!--
    The style index is zero-based however the xslt index is one-based. As result it is necessary to add one to the style-index.
  -->
  <xsl:variable name="cell-format" select="$styles/ss:styleSheet/ss:cellXfs/ss:xf[($style-index + 1)]"/>

  <xsl:choose>
    <!-- empty value/content -->
    <xsl:when test="not($value)"><xsl:value-of select="$value"/></xsl:when>
    <!-- the number format should be applied -->
    <xsl:when test="$cell-format/@applyNumberFormat"><xsl:value-of select="ss:apply-cell-number-format($cell-format/@numFmtId, $value)"/></xsl:when>
    <!-- any other case -->
    <xsl:otherwise><xsl:value-of select="$value"/></xsl:otherwise>
  </xsl:choose>

</xsl:function>

<xsl:function name="ss:apply-cell-number-format">
  <xsl:param name="number-format-id" as="xs:string"/>
  <xsl:param name="value" as="xs:string"/>

  <!--
  There are defaults format that goes from 1 to 163.
  After this it will be custom format and is stored ss:styleSheet/ss:numFmts/ss:numFmt
  -->
  <xsl:variable name="custom-format" select="$styles/ss:styleSheet/ss:numFmts/ss:numFmt[@numFmtId = $number-format-id]/@formatCode"/>

  <xsl:choose>
    <xsl:when test="$number-format-id = ('', '0')"><xsl:value-of select="$value"/></xsl:when>
    <xsl:when test="$number-format-id= '14'
                  or $custom-format = ('dd/mm/yyyy;@', 'd/m/yyyy;@', 'd/mm/yy;@', 'd/m/yy;@', 'dd/mm/yy;@',
                   '[$-C09]dd\-mmm\-yy;@', '[$-C09]dd\-mmmm\-yyyy;@', '[$-C09]d\ mmmm\ yyyy;@')">
      <!-- The default value is mm-dd-yy however as it is Australia then it will be changed to dd-mm-yy -->
      <xsl:variable name="value-as-date" select="ss:calculate-datetime(xs:integer($value))"/>
<!--      <xsl:value-of select="format-dateTime($value-as-date, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')"/>-->
      <xsl:value-of select="format-dateTime($value-as-date, '[D01]/[M01]/[Y0001]')"/>
    </xsl:when>
    <xsl:when test="$custom-format = ('m/d/yyyy;@')">
      <xsl:variable name="value-as-date" select="ss:calculate-datetime(xs:integer($value))"/>
      <xsl:value-of select="format-dateTime($value-as-date, '[M1]/[D1]/[Y0001]')"/>
    </xsl:when>
    <xsl:when test=" $custom-format = ('yyyy/mm/dd;@')">
      <xsl:variable name="value-as-date" select="ss:calculate-datetime(xs:integer($value))"/>
      <xsl:value-of select="format-dateTime($value-as-date, '[Y0001]/[M01]/[D01]')"/>
    </xsl:when>
    <xsl:otherwise><xsl:value-of select="$value"/></xsl:otherwise>
  </xsl:choose>

</xsl:function>

<xsl:variable name="base-datetime" select="xs:dateTime('1899-12-31T00:00:00.000')"/>
<!--<xsl:variable name="base-date" select="xs:date('1899-12-31')"/>-->
<!--
  Calcu
-->
<xsl:function name="ss:calculate-datetime" as="xs:dateTime">
  <xsl:param name="value" as="xs:integer"/>
  <!--
  Please note that in order to calculate dates correctly, the year 1900 must be considered a leap-year hence
  February 29th is considered a valid date – even though the year 1900 is not a leap year.  This bug originated from
  Lotus 123, and was purposely implemented in Excel for backward compatibility.)

  $value equal to 59 is 28th February 1900. And 60 due to the bug is supposed to be 29th February 1900 then it will
  shown as 28th February 1900 and 61 will be 1st March 1900.
  -->
  <xsl:variable name="adjusted-value" select="if ($value > 59) then ($value -1) else $value"/>
  <xsl:sequence select="$base-datetime + xs:dayTimeDuration(concat('P', $adjusted-value, 'D'))"/>
</xsl:function>
</xsl:stylesheet>


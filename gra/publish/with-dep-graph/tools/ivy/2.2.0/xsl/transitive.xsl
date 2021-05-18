<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:output omit-xml-declaration="yes"/>
   <xsl:template match="/">
      <xsl:variable name="alldeps">
          <xsl:for-each select="//dependencies/dependency">
              <xsl:if test="@transitive">
                  <dependency name="{@name}" rev="{@rev}"/><xsl:text>&#xA;</xsl:text>
              </xsl:if> 
          </xsl:for-each>
      </xsl:variable>
      <xsl:copy-of select="$alldeps"/>
   </xsl:template>
</xsl:stylesheet>

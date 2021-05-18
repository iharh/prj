<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<xsl:output method="xml" omit-xml-declaration="yes"/>

 <xsl:template match="/">
      <xsl:variable name="alldeps">
         <dependencies>
            <xsl:for-each select="/dependencies/dependency">
               <xsl:sort select="@org"/>
               <xsl:sort select="@name"/>
               <xsl:sort select="@rev"/>
               <dependency name="{@name}" rev="{@rev}" id="{@name}-{@rev}"/>
            </xsl:for-each>
         </dependencies>
      </xsl:variable>

      <xsl:variable name="deps">
         <dependencies>
            <xsl:for-each select="$alldeps/dependencies/dependency">
               <xsl:if test="not(preceding-sibling::dependency/@id=./@id)">
                  <xsl:copy-of select="."/><xsl:text>&#xA;</xsl:text>
               </xsl:if>
            </xsl:for-each>
         </dependencies>
      </xsl:variable>

      <xsl:variable name="modules">
         <dependencies>
            <xsl:for-each select="$alldeps/dependencies/dependency">
               <xsl:if test="not ((preceding-sibling::dependency/@name=./@name))">
                  <xsl:copy-of select="."/>
               </xsl:if>
            </xsl:for-each>
         </dependencies>
      </xsl:variable>

      <xsl:for-each select="$modules/dependencies/dependency">
         <xsl:text>svn update </xsl:text><xsl:value-of select="@name"/><xsl:text> --depth empty &#xA;</xsl:text>
      </xsl:for-each>

      <xsl:for-each select="$deps/dependencies/dependency">
         <xsl:text>svn update </xsl:text><xsl:value-of select="@name"/>/<xsl:value-of select="@rev"/><xsl:text> --set-depth infinity &#xA;</xsl:text>
      </xsl:for-each>
   </xsl:template>
</xsl:stylesheet>


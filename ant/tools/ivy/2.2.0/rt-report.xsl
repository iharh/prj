<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template name="date">
        <xsl:param name="date"/>
        <xsl:value-of select="substring($date,1,4)"/>-<xsl:value-of select="substring($date,5,2)"/>-<xsl:value-of select="substring($date,7,2)"/>
        <xsl:value-of select="' '"/>
        <xsl:value-of select="substring($date,9,2)"/>:<xsl:value-of select="substring($date,11,2)"/>:<xsl:value-of select="substring($date,13)"/>
    </xsl:template>


    <xsl:template match="/ivy-report">
        <html>
            <head>
                <title>Clarabridge runtime dependencies</title>
                <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"/>
                <meta http-equiv="content-language" content="en"/>
                <meta name="robots" content="index,follow"/>
                <link rel="stylesheet" type="text/css" href="ivy-report.css"/>
            </head>
            <body>
                <h1>Clarabridge runtime dependencies
                </h1>
                <div id="date">
                    resolved on
                    <xsl:call-template name="date">
                        <xsl:with-param name="date" select="info/@date"/>
                    </xsl:call-template>
                </div>

                <ul id="confmenu"></ul>
                <div id="content">

                <table class="deps">
                    <thead>
                        <tr>
                            <th>Module</th>
                            <th>Revision</th>
                            <th>Licenses</th>
                        </tr>
                    </thead>

                    <xsl:for-each select="//dependencies/module">
                        <xsl:sort select="@name"/>
                        <tr>
                            <td>
                                <xsl:element name="a">
                                    <xsl:attribute name="href">
                                        <xsl:value-of select="revision/@homepage"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </xsl:element>
                                (<xsl:value-of select="@organisation"/>)
                            </td>
                            <td align="center">
                                <xsl:value-of select="revision/@name"/>
                            </td>
                            <td align="center">
                              <xsl:for-each select="revision/license">
                             <xsl:if test="position()>1">
                              	<xsl:text disable-output-escaping="yes">,&#160;</xsl:text>
                              </xsl:if>
                                <xsl:element name="a">
                                    <xsl:attribute name="href">
                                        <xsl:value-of select="@url"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </xsl:element>
                             </xsl:for-each>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                </div>
            </body>
        </html>
	</xsl:template>

</xsl:stylesheet>

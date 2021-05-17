<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output omit-xml-declaration="yes" indent="yes" method="html" />
	<xsl:template match="/">
		<html>
			<head>
				<style>
					body {
					font-family: Helvetica, Arial, Verdana, sans-serif;
					color: #58595b;
					}

					a {
					text-decoration: underline;
					outline: none;
					color: #5A8221;
					}

					a:visited {
					color: #3B4416;
					}

					a:hover, a:focus {
					color: #6AA527;
					text-decoration: underline;
					}

					a:active {
					color: #6AA527;
					}

					th {
					font-weight: bold;
					text-align="left"
					}
                </style>

				<title>Third-Party Licenses</title>
			</head>
			<body>
                                <h2> Third-Party Licenses </h2>
				<table border="1" cellpadding="5" cellspacing="0" width="1200">
					<tr>
						<th width="420">Component Name</th>
						<th width="80">Version</th>
						<th width="300">License Type</th>
						<th width="400">Project Home</th>
					</tr>
					<xsl:for-each select="//dependencies/module">
						<xsl:sort select="@name" />
						<tr>
							<td>
								<xsl:value-of select="@name" />
								(
								<xsl:value-of select="@organisation" />
								)
							</td>
							<td align="right">
								<xsl:value-of select="revision/@name" />
							</td>
							<td>
	 							<xsl:for-each select="revision/license">
 									<xsl:if test="position()>1">
 										<xsl:text disable-output-escaping="yes">,&#160;</xsl:text>
 									</xsl:if>
	 								<xsl:element name="a">
										<xsl:attribute name="href"><xsl:value-of
										select="@url" /></xsl:attribute>
										<xsl:value-of select="@name" />
									</xsl:element>
	 							</xsl:for-each>
							</td>
							<td>
								<xsl:element name="a">
									<xsl:attribute name="href"><xsl:value-of
										select="revision/@homepage" /></xsl:attribute>
									<xsl:value-of select="revision/@homepage" />
								</xsl:element>
							</td>

						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>

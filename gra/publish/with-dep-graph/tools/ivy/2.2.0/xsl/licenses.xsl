<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output omit-xml-declaration="no" indent="yes" />
	<xsl:template match="/">
		<thirdparty-licenses>
			<xsl:for-each select="//dependencies/module">
				<xsl:sort select="@name"/>
	        		<module   name="{@name} ({@organisation})" 
		                	  version="{revision/@name}" 
                			  license_type="{revision/license/@name}"
			                  license_url="{revision/license/@url}"
        	        		  project_home="{revision/@homepage}"
			        />	
			</xsl:for-each>
		</thirdparty-licenses>
	</xsl:template>
</xsl:stylesheet>

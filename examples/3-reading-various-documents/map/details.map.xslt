<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:notifications="org.vsearchd.crawler.extensions.Notifications"
	version="1.0">	
	
	<!-- default parameter -->
	<xsl:param name="XmlSource" />
	<xsl:param name="SessionID" />
	<xsl:param name="HttpStatusCode" />
	
	<!-- custom xslt-parameter  -->	
	<xsl:param name="client" />
	<xsl:param name="ProjectPath" /> 	
	
	<!-- -->
	<xsl:param name="ID" />
	<xsl:param name="Name" />
	<xsl:param name="Title" />
	<xsl:param name="Description" />

	<xsl:output omit-xml-declaration="no" method="xml" indent="yes"
		encoding="UTF-8" standalone="yes"   cdata-section-elements="field" />

	<xsl:template match="/">
		
		<mapping>
			<!--
			<xsl:variable name="debug">true</xsl:variable>
		
			<xsl:if test="$debug='true'">
				<xsl:value-of select="notifications:log('info', concat('Client: ', $client))" />
				<xsl:value-of select="notifications:log('info', concat('HttpStatusCode: ', $HttpStatusCode))" />
				<xsl:value-of select="notifications:log('info', concat('SessionID: ', $SessionID ))" />
				<xsl:value-of select="notifications:log('info', concat('ProjectPath: ', $ProjectPath ))" /> 	
				<xsl:value-of select="notifications:log('info', concat('XmlSource: ', normalize-space($XmlSource)))" />
			</xsl:if>
			-->
			
			<xsl:variable name="docDetails"><xsl:value-of select="normalize-space(//body/text())" /></xsl:variable>
			
			<xsl:value-of select="notifications:log('info', concat('ID: ', $ID))" />
			<xsl:value-of select="notifications:log('info', concat('Name: ', $Name))" />
			<xsl:value-of select="notifications:log('info', concat('Title: ', $Title))" />
			<xsl:value-of select="notifications:log('info', concat('Description: ', $Description))" />
			<xsl:value-of select="notifications:log('info', concat('DocDetails: ', $docDetails))" />
			
		</mapping>	
	</xsl:template>
	
</xsl:stylesheet>

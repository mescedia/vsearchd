<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	
	xmlns:notifications="org.vsearchd.crawler.extensions.Notifications"
	version="1.0">	
	
	<!-- xmlns:java="http://xml.apache.org/xalan/java" -->
	
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
			
			<xsl:variable name="vDetailsTitle"><xsl:value-of select="normalize-space(//div[@id='maincol']/h3/text())" /></xsl:variable>
			<xsl:variable name="vDetailsText"><xsl:value-of select="normalize-space(//div[@id='maincol']/p/text())" /></xsl:variable>

			<xsl:value-of select="notifications:log('info', concat('ID: ', $ID))" />
			<xsl:value-of select="notifications:log('info', concat('Name: ', $Name))" />
			<xsl:value-of select="notifications:log('info', concat('Title: ', $Title))" />
			<xsl:value-of select="notifications:log('info', concat('Description: ', $Description))" />
			
			<xsl:value-of select="notifications:log('info', concat('vDetailsTitle: ', $vDetailsTitle))" />
			<xsl:value-of select="notifications:log('info', concat('vDetailsText: ', $vDetailsText))" />
			
		</mapping>	
	</xsl:template>
	
</xsl:stylesheet>

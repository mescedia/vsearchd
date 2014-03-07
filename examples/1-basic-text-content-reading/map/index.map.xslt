<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:notifications="org.vsearchd.crawler.extensions.Notifications"
	version="1.0">	
	
	<!-- default parameter -->
	<xsl:param name="XmlSource" />
	<xsl:param name="SessionID" />
	<xsl:param name="HttpStatusCode" />
	
	<!-- custom xslt-parameter  -->	
	<xsl:param name="client" /> 		<!--  defined in project-request file -->
	<xsl:param name="ProjectPath" /> 	<!--  defined in index.xml file -->

	<xsl:output omit-xml-declaration="no" method="xml" indent="yes"
		encoding="UTF-8" standalone="yes"   cdata-section-elements="field" />

	<xsl:template match="/">
		
		<mapping>
		
			<xsl:variable name="debug">true</xsl:variable>
		
			<xsl:if test="$debug='true'">		    
				<xsl:value-of select="notifications:log('info', concat('Client: ', $client))" />
				<xsl:value-of select="notifications:log('info', concat('HttpStatusCode: ', $HttpStatusCode))" />
				<xsl:value-of select="notifications:log('info', concat('SessionID: ', $SessionID ))" />
				<xsl:value-of select="notifications:log('info', concat('ProjectPath: ', $ProjectPath ))" /> 	
				<xsl:value-of select="notifications:log('info', concat('XmlSource: ', normalize-space($XmlSource)))" />
			</xsl:if>
			
			<xsl:for-each select="//table//tr">			 
				<xsl:if test="./td">
					<xsl:value-of select="notifications:log('info', concat('ID: ', normalize-space(./td[1]/text()) ))" />
					<xsl:value-of select="notifications:log('info', concat('Name: ', normalize-space(./td[2]/text()) ))" />
					<xsl:value-of select="notifications:log('info', concat('Title: ', normalize-space(./td[3]/text()) ))" />
					<xsl:value-of select="notifications:log('info', concat('Description: ', normalize-space(./td[4]/text()) ))" />		  
				</xsl:if>
			</xsl:for-each>
			     
		</mapping>	
	</xsl:template>
	
</xsl:stylesheet>

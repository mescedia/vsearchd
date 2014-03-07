<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:sFunc="org.vsearchd.crawler.extensions.StringFunctions"	
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
		
			<xsl:variable name="debug">false</xsl:variable>
		
			<xsl:if test="$debug='true'">		    
				<xsl:value-of select="notifications:log('info', concat('Client: ', $client))" />
				<xsl:value-of select="notifications:log('info', concat('HttpStatusCode: ', $HttpStatusCode))" />
				<xsl:value-of select="notifications:log('info', concat('SessionID: ', $SessionID ))" />
				<xsl:value-of select="notifications:log('info', concat('ProjectPath: ', $ProjectPath ))" /> 	
				<xsl:value-of select="notifications:log('info', concat('XmlSource: ', normalize-space($XmlSource)))" />
			</xsl:if>
			
			<xsl:for-each select="//table/tr">			  

				<xsl:if test="./td">

					<xsl:variable name="vID"><xsl:value-of select="normalize-space(./td[1]/text())" /></xsl:variable>
					<xsl:variable name="vName"><xsl:value-of select="normalize-space(./td[2]/text())" /></xsl:variable>
					<xsl:variable name="vTitle"><xsl:value-of select="normalize-space(./td[3]/text())" /></xsl:variable>
					<xsl:variable name="vDescription"><xsl:value-of select="normalize-space(./td[4]/text())" /></xsl:variable>
					<xsl:variable name="vUniRecordKey"><xsl:value-of select="sFunc:calcSHA1(concat($vID,$vName,$vTitle))" /></xsl:variable>


					<xsl:value-of select="notifications:log('info', concat('ID: ', $vID)  )" />
					<xsl:value-of select="notifications:log('info', concat('Name: ', $vName) )" />
					<xsl:value-of select="notifications:log('info', concat('Title: ', $vTitle)  )" />
					<xsl:value-of select="notifications:log('info', concat('Description: ', $vDescription) )" />		  
					<xsl:value-of select="notifications:log('info', concat('UniqKey: ', $vUniRecordKey) )" />		 

					<client>
						<output table="4_backend" backend="jdbc" action="checkinsert">
					      		<field type="int" name="vsdID"><xsl:value-of select="$vID" /></field>
						      	<field type="string" name="vsdName"><xsl:value-of select="$vName" /></field>						
					      		<field type="string" name="vsdDescription" ><xsl:value-of select="$vDescription" /></field> 
					      		<field type="string" name="vsdTitle" ><xsl:value-of select="$vTitle" /></field>
					      		<field type="string" name="vsdRecordKey" unique="true"><xsl:value-of select="$vUniRecordKey" /></field>
		 				</output>
					</client>
 

				</xsl:if>

			</xsl:for-each>
			     
		</mapping>	
	</xsl:template>
	
</xsl:stylesheet>
